package com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.UserDTO;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Models.Users;
import com.natanfelipe.solid.solid.SingleReponsibilityPrinciple.Repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<Users> getAll() {
        return repository.findAll();
    }

    public Users getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Users createUser(UserDTO dto) {
        Users user = new Users();

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPix(dto.getPix());
        user.setCrc(dto.getCrc());
        user.setCreatedBy("SYSTEM");

        return repository.save(user);
    }
}