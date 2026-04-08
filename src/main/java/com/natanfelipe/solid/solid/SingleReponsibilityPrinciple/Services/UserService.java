package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.services;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.dtos.UserDTO;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.events.UserCreatedEvent;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.models.Users;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository repository, 
        ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public ResponseEntity<List<Users>> getAll() {
        var users = repository.findAll();
        return ResponseEntity.status(200).body(users);
    }

    public ResponseEntity<Users> getById(Integer id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.status(200).body(user);
    }

    public ResponseEntity<Users> createUser(UserDTO dto) {
        Users user = new Users();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPix(dto.getPix());
        user.setCrc(dto.getCrc());
        user.setCreatedBy("SYSTEM");
        repository.save(user);
        eventPublisher.publishEvent(new UserCreatedEvent(user));
        return ResponseEntity.status(201).body(user);
    }

    public ResponseEntity<Users> updateUser(Integer id, UserDTO dto) {
        var user = repository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found."));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPix(dto.getPix());
        user.setCrc(dto.getCrc());
        user.setCreatedBy("SYSTEM");
        repository.save(user);
        return ResponseEntity.status(200).body(user);
    }

    public ResponseEntity<Users> deleteUser(Integer id) {
        var user = repository.findById(id).orElseThrow(
                () -> new RuntimeException("User not found."));
        repository.delete(user);
        return ResponseEntity.status(204).build();
    }
}