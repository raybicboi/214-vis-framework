package edu.cs.visframework.controller;

import edu.cs.visframework.service.DataService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Controller class where we take in Post request.
 *
 * @author Shicheng Huang
 */
@RestController
public class ParamController {
    @Resource
    private DataService dataService;

    /**
     * Map post request from /inputPost.
     * @param input input body.
     * @return return a Json.
     * @throws IOException exception.
     * @throws URISyntaxException exception.
     */
    @CrossOrigin(origins = "*") // COR config.
    @PostMapping("/inputPost")
    public String postRequest(@RequestBody String input) throws IOException, URISyntaxException {
        return dataService.getProcessedData(new JSONObject(input)).toString();
    }
}
