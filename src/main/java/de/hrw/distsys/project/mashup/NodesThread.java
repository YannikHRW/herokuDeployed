package de.hrw.distsys.project.mashup;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hrw.distsys.project.mashup.service.WavesService;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class NodesThread {

    private ExecutorService executorService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WavesService wavesService;
    private final HashMap<String, JSONObject> nodesLocationsMap = new HashMap<>();

    private final String[] keys = {
            "af9eb05fa8094aef9f8c057e76095505",
            "865c80dd04644d618fa6ef2b6411f44c",
            "5173f0fda99a4e85bd8de1d38904b78d",
            "7675770824c747b0809f65a2cac2bda1",
            "b3b32cefc31a4ddfa11379e02eb7b88c",
            "ea73d4247ec54dc99a2a54dbfb250555"
    };

    //c3bae47657dd4ebfbac0257f03ab2263
    //a18f449f50f443e987fbc57ca8856a53
    private int keyCounter = 0;
    private String currentKey = keys[keyCounter];
    private int requestCounter = 0;

    public NodesThread(WavesService wavesService) {
        this.wavesService = wavesService;
    }

    @PostConstruct
    public void init() {

        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {

                    //https://api.ipgeolocation.io/ipgeo?apiKey=d1d8c915b7664371a2ac9fda5edbd80c&ip=13.209.60.161&fields=latitude%2Clongitude

                    JSONObject activeNodes;
                    JSONObject allNodes;

                    LinkedHashMap<String, String> mainNode = new LinkedHashMap<>();
                    mainNode.put("address", "/212.95.36.100:6868");
                    mainNode.put("declaredAddress", "/78.159.101.3:6868");
                    mainNode.put("peerName", "waves-node.tokenomica.com");
                    mainNode.put("peerNonce", "217221");
                    mainNode.put("applicationName", "wavesW");
                    mainNode.put("applicationVersion", "1.3.6");

                    JSONObject jo = new JSONObject();
                    List<LinkedHashMap<String, String>> list = new ArrayList<>();
                    list.add(mainNode);
                    jo.put("peers", list);

                    wavesService.setMainNode(mergeNodeInfos(jo));

                    while (true) {
                        try {
                            allNodes = objectMapper.readValue(new URL("https://waves-node.tokenomica.com/peers/all"), JSONObject.class);
                            activeNodes = objectMapper.readValue(new URL("https://waves-node.tokenomica.com/peers/connected"), JSONObject.class);
                            wavesService.setAllNodes(mergeNodeInfos(allNodes));
                            wavesService.setActiveNodes(mergeNodeInfos(activeNodes));
                        } catch (MalformedURLException mue) {
                            System.err.println("URL has wrong format!");
                            mue.printStackTrace();
                        } catch (IOException ioe) {
                            ioe.printStackTrace();
                        }
                        Thread.sleep(30000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
    }

    public JSONObject mergeNodeInfos(JSONObject nodes) throws IOException {

        List<LinkedHashMap<String, String>> beforeNodesList = (List<LinkedHashMap<String, String>>) nodes.get("peers");
        JSONObject nodeLocation;

        List<LinkedHashMap<String, String>> afterNodesList = new LinkedList<>();
        JSONObject mergedJSON = new JSONObject();

        int counter = 0;

        for (LinkedHashMap<String, String> node : beforeNodesList) {
            String address = extractAddress(node);
            if (nodesLocationsMap.containsKey(address)) {
                nodeLocation = nodesLocationsMap.get(address);
            } else if (address.equals("127.0.0.1")) {
                continue;
            } else {
                requestCounter++;
                nodeLocation = objectMapper.readValue(new URL("https://api.ipgeolocation.io/ipgeo?apiKey=" + currentKey + "&ip=" + address + "&fields=latitude%2Clongitude%2Ccountry_name%2Ccity%2Ccountry_flag"), JSONObject.class);
                if (requestCounter % 1000 == 0) {
                    keyCounter++;
                    if (keyCounter == 6) {
                        this.keyCounter = 0;
                    }
                    this.currentKey = keys[keyCounter];
                }
                nodesLocationsMap.put(address, nodeLocation);
                System.out.println("requested nodelocation by node: " + counter++ + ": " + node);
            }
            node.put("latitude", (String) nodeLocation.get("latitude"));
            node.put("longitude", (String) nodeLocation.get("longitude"));
            node.put("country_name", (String) nodeLocation.get("country_name"));
            node.put("city", (String) nodeLocation.get("city"));
            node.put("country_flag", (String) nodeLocation.get("country_flag"));
            //System.out.println(counter++ + ": " + node);
            afterNodesList.add(node);
            /*if (counter == 5) {
                break;
            }*/
        }
        mergedJSON.put("peers", afterNodesList);
        //System.out.println(mergedJSON);
        return mergedJSON;
    }

    public static String extractAddress(LinkedHashMap<String, String> node) {
        String[] addressArray = node.get("address").substring(1).split(":");
        return addressArray[0];
    }

    @PreDestroy
    public void beandestroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
