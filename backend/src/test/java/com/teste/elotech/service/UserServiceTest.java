package com.teste.elotech.service;

import com.teste.elotech.dto.CreateUserDTO;
import com.teste.elotech.dto.UpdateUserDTO;
import com.teste.elotech.exception.ResourceNotFoundException;
import com.teste.elotech.mapper.UserMapper;
import com.teste.elotech.model.User;
import com.teste.elotech.repository.UserRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private CreateUserDTO createUserDTO;
    private UpdateUserDTO updateUserDTO;
    private User user;

    @BeforeEach
    void setUp() {
        createUserDTO = CreateUserDTO.builder()
                .name("Test User")
                .email("test@example.com")
                .build();

        updateUserDTO = UpdateUserDTO.builder()
                .id(1L)
                .name("Updated Test User")
                .email("updated@example.com")
                .build();

        user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void testCreateUserSuccess() {
        when(userMapper.toEntity(any(CreateUserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.create(createUserDTO);

        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toEntity(any(UpdateUserDTO.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userService.update(updateUserDTO);

        assertNotNull(updatedUser);
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                userService.update(updateUserDTO)
        );

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testFindByIdSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<User> foundUser = userService.findById(1L);

        assertTrue(foundUser.isEmpty());
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
        verify(userRepository, times(1)).findAll();
    }
}