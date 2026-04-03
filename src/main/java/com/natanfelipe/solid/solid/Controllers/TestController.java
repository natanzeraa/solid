package com.natanfelipe.solid.solid.Controllers;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    DataSource dataSource;

    @GetMapping("/test-db")
    public String test() throws Exception {
        try(Connection conn = dataSource.getConnection()) {
            return conn.isValid(2) ? "CONEXAO OK 🔥" : "FALHOU ❌";
        }
    }
}
