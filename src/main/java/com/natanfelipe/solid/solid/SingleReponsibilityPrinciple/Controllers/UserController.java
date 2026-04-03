package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.UserDTO;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Models.Users;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<Users> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Users getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public Users create(@RequestBody UserDTO user) {
        return service.createUser(user);
    }
}
