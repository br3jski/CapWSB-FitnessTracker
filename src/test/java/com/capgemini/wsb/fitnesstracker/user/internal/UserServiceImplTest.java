package com.capgemini.wsb.fitnesstracker.user.internal;


import org.junit.jupiter.api.Test;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("Janusz", createdUser.getFirstName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deleteUser() {
        Long userId = 1L;
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void updateUser() {
        User existingUser = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        existingUser.setId(1L);

        User updateData = new User("Zdzislaw", "Nowak", LocalDate.of(1990, 1, 1), "znowak@mail.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(1L, updateData);

        assertNotNull(updatedUser);
        assertEquals("Zdzislaw", updatedUser.getFirstName());
        assertEquals("Nowak", updatedUser.getLastName());
        assertEquals("znowak@mail.com", updatedUser.getEmail());

        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void findUsersByEmail() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        when(userRepository.findByEmailContainingIgnoreCase(anyString())).thenReturn(List.of(user));

        List<User> users = userService.findUsersByEmail("jdomagala");

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("jdomagala@mail.com", users.get(0).getEmail());
    }

    @Test
    void findUsersOlderThan() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        when(userRepository.findUsersOlderThan(any(LocalDate.class))).thenReturn(List.of(user));

        List<User> users = userService.findUsersOlderThan(30);

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Janusz", users.get(0).getFirstName());
    }

    @Test
    void getUser() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUser(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Janusz", foundUser.get().getFirstName());
    }

    @Test
    void getUserByEmail() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserByEmail("jdomagala@mail.com");

        assertTrue(foundUser.isPresent());
        assertEquals("jdomagala@mail.com", foundUser.get().getEmail());
    }

    @Test
    void findAllUsers() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> users = userService.findAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("Janusz", users.get(0).getFirstName());
    }
}