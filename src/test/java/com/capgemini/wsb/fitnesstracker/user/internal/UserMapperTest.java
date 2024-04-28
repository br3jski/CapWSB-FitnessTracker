package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void toDto_ConvertsUserToUserDtoCorrectly() {
        User user = new User("Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");
        user.setId(1L); // Ustawienie ID, które jest zazwyczaj ustawiane przez JPA

        UserDto result = userMapper.toDto(user);

        assertEquals(user.getId(), result.Id());
        assertEquals(user.getFirstName(), result.firstName());
        assertEquals(user.getLastName(), result.lastName());
        assertEquals(user.getBirthdate(), result.birthdate());
        assertEquals(user.getEmail(), result.email());
    }

    @Test
    void toEntity_ConvertsUserDtoToUserCorrectly() {
        UserDto userDto = new UserDto(null, "Janusz", "Domagała", LocalDate.of(1990, 1, 1), "jdomagala@mail.com");

        User result = userMapper.toEntity(userDto);

        assertNull(result.getId());
        assertEquals(userDto.firstName(), result.getFirstName());
        assertEquals(userDto.lastName(), result.getLastName());
        assertEquals(userDto.birthdate(), result.getBirthdate());
        assertEquals(userDto.email(), result.getEmail());
    }
}