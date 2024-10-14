package com.iot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.iot.service.DatabaseService;

@RestController
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/test-db-connection")
    public String testDatabaseConnection() {
        return databaseService.testConnection();
    }
}
