package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by piotr on 15/11/16.
 */
@RestController
@RequestMapping(path = "/")
@CrossOrigin
public class DataController {

    @Autowired
    private DataService dataService;

    @RequestMapping(path = "ticks", produces = MediaType.APPLICATION_JSON_VALUE)
    public Object[] ticks() {
        return dataService.ticks().map((price) -> {
            TickResource res = new TickResource(price);
            return res;
        }).toArray();
    }

}
