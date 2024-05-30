package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Janusz", createdUser.getFirstName());

        Optional<User> foundUser = userRepository.findById(createdUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Janusz", foundUser.get().getFirstName());
    }

    @Test
    void deleteUser() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        User createdUser = userRepository.save(user);

        userService.deleteUser(createdUser.getId());

        Optional<User> foundUser = userRepository.findById(createdUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @Test
    void updateUser() {
        User existingUser = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        existingUser = userRepository.save(existingUser);

        User updateData = new User("Zdzislaw", "Nowak", LocalDate.of(1990, 1, 1), "znowak@mail.com");

        User updatedUser = userService.updateUser(existingUser.getId(), updateData);

        assertNotNull(updatedUser);
        assertEquals("Zdzislaw", updatedUser.getFirstName());
        assertEquals("Nowak", updatedUser.getLastName());
        assertEquals("znowak@mail.com", updatedUser.getEmail());

        Optional<User> foundUser = userRepository.findById(updatedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Zdzislaw", foundUser.get().getFirstName());
    }

    @Test
    void findUsersByEmail() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        userRepository.save(user);

        List<User> users = userService.findUsersByEmail("jdomagala");

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("jdomagala@mail.com", users.get(0).getEmail());
    }

    @Test
    void findUsersOlderThan() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        userRepository.save(user);

        List<User> users = userService.findUsersOlderThan(30);

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Janusz", users.get(0).getFirstName());
    }

    @Test
    void getUser() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        user = userRepository.save(user);

        Optional<User> foundUser = userService.getUser(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Janusz", foundUser.get().getFirstName());
    }

    @Test
    void getUserByEmail() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        userRepository.save(user);

        Optional<User> foundUser = userService.getUserByEmail("jdomagala@mail.com");

        assertTrue(foundUser.isPresent());
        assertEquals("jdomagala@mail.com", foundUser.get().getEmail());
    }

    @Test
    void findAllUsers() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        userRepository.save(user);

        List<User> users = userService.findAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Janusz", users.get(0).getFirstName());
    }
}