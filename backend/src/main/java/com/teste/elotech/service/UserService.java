package com.teste.elotech.service;

import com.teste.elotech.dto.CreateUserDTO;
import com.teste.elotech.dto.UpdateUserDTO;
import com.teste.elotech.exception.ResourceNotFoundException;
import com.teste.elotech.mapper.UserMapper;
import com.teste.elotech.model.User;
import com.teste.elotech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User create(CreateUserDTO createUserDTO) {
        User user = userMapper.toEntity(createUserDTO);
        return userRepository.save(user);
    }

    public User update(UpdateUserDTO updateUserDTO) {
        userRepository.findById(updateUserDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        User user = userMapper.toEntity(updateUserDTO);
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
