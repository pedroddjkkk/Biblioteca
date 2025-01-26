package com.teste.elotech.repository;

import com.teste.elotech.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .telephone("12312312")
                .registeredAt(LocalDate.now())
                .build();

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("12312312", savedUser.getTelephone());
        assertEquals(LocalDate.now(), savedUser.getRegisteredAt());
    }

    @Test
    void testFindUserById() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .telephone("12312312")
                .registeredAt(LocalDate.now())
                .build();

        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getId(), foundUser.get().getId());
    }

    @Test
    void testUpdateUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .telephone("12312312")
                .registeredAt(LocalDate.now())
                .build();

        User savedUser = userRepository.save(user);
        savedUser.setName("Updated Test User");
        savedUser.setEmail("updated@example.com");
        savedUser.setTelephone("12312312333");
        savedUser.setRegisteredAt(LocalDate.now().minusDays(1));

        User updatedUser = userRepository.save(savedUser);

        assertNotNull(updatedUser);
        assertEquals("Updated Test User", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
        assertEquals("12312312333", updatedUser.getTelephone());
        assertEquals(LocalDate.now().minusDays(1), updatedUser.getRegisteredAt());
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .telephone("12312312")
                .registeredAt(LocalDate.now())
                .build();

        User savedUser = userRepository.save(user);
        userRepository.deleteById(savedUser.getId());

        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSaveUserInvalidEmail() {
        User user = User.builder()
                .name("Test User")
                .email("invalid-email")
                .telephone("12312312")
                .registeredAt(LocalDate.now())
                .build();

        assertThrows(ConstraintViolationException.class, () ->
                userRepository.save(user)
        );
    }
}