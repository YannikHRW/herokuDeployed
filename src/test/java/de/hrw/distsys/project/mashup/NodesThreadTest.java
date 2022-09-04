package de.hrw.distsys.project.mashup;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

class NodesThreadTest {

    @Test
    public void testExtractAddress() {

        //Given
        LinkedHashMap<String, String> peer = new LinkedHashMap<>();
        peer.put("address", "/212.95.36.100:6868");

        //When
        String address = NodesThread.extractAddress(peer);

        //Then
        Assertions.assertEquals("212.95.36.100", address);
    }

}