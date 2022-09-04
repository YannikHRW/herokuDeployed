package de.hrw.distsys.project.mashup.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import de.hrw.distsys.project.mashup.service.WavesService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(path = "/get_nodes")
public class WavesController {

    private final WavesService wavesService;

    @Autowired
    public WavesController(WavesService service) {
        this.wavesService = service;
    }


    @GetMapping(path = "/active")
    public JSONObject getActiveNodes() throws IOException {return wavesService.getActiveNodes();}

    @GetMapping(path = "/main")
    public JSONObject getMainNode() throws IOException {return wavesService.getMainNode();}

    @GetMapping(path = "/all")
    public JSONObject getAllNodes() throws IOException {return wavesService.getAllNodes();}

}
