package de.hrw.distsys.project.mashup.service;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class WavesService {

    private JSONObject activeNodes;
    private JSONObject mainNode;
    private JSONObject allNodes;

    public JSONObject getActiveNodes() {
        return activeNodes;
    }
    public void setActiveNodes(JSONObject activeNodes) {this.activeNodes = activeNodes;}

    public JSONObject getMainNode() {
        return mainNode;
    }
    public void setMainNode(JSONObject mainNode) {this.mainNode = mainNode;}

    public JSONObject getAllNodes() {
        return allNodes;
    }
    public void setAllNodes(JSONObject allNodes) {this.allNodes = allNodes;}
}
