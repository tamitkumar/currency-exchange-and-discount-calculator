package com.currency.exch.discount.calc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.currency.exch.discount.calc.model.User;

public class UserServiceTest {

	private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void testGetAllUsers_initialUsers() {
        List<User> users = userService.getAllUsers();
        assertEquals(5, users.size(), "Initial user list should contain 5 users");

        User user1 = users.get(0);
        assertEquals(1, user1.getId());
        assertEquals("User 1", user1.getName());
        assertFalse(user1.getEmployee());
        assertFalse(user1.getAffiliate());
        assertEquals(LocalDate.now().minusYears(3), user1.getCreatedOn());
    }
    
    @Test
    void testAddUser() {
        User newUser = User.builder()
                .id(6)
                .name("User 6")
                .email("user6@example.com")
                .phone(1234567896L)
                .createdOn(LocalDate.now())
                .build();
        
        userService.addUser(newUser);
        List<User> users = userService.getAllUsers();

        assertEquals(6, users.size(), "User list should contain 6 users after addition");
        assertTrue(users.contains(newUser), "Newly added user should be in the user list");
    }
    
    @Test
    void testUpdateUser_existingUser() {
        User updatedUser = User.builder()
                .id(1)
                .name("Updated User 1")
                .email("updated1@example.com")
                .employee(true)
                .affiliate(true)
                .createdOn(LocalDate.now().minusYears(5))
                .build();

        userService.updateUser(updatedUser);

        Optional<User> userOpt = userService.getUserByIdAndPhone(1, 1234567891L);
        assertTrue(userOpt.isPresent(), "User with ID 1 should exist");

        User user = userOpt.get();
        assertEquals("User 1", user.getName());
        assertEquals("user1@example.com", user.getEmail());
        assertFalse(user.getEmployee());
        assertFalse(user.getAffiliate());
        assertEquals(LocalDate.now().minusYears(3), user.getCreatedOn());
        assertEquals(LocalDate.now(), user.getModifiedOn(), "Modified date should be set to current date");
    }
    
    @Test
    void testUpdateUser_nonExistingUser() {
        User nonExistingUser = User.builder()
                .id(10)
                .name("Non-Existent User")
                .email("nonexistent@example.com")
                .build();

        userService.updateUser(nonExistingUser);

        assertEquals(5, userService.getAllUsers().size(), "User list size should remain unchanged for non-existing user update");
    }
    
    @Test
    void testGetUserByIdAndPhone_existingUser() {
        Optional<User> userOpt = userService.getUserByIdAndPhone(1, 1234567891L);

        assertTrue(userOpt.isPresent(), "User with ID 1 and correct phone should be found");
        User user = userOpt.get();
        assertEquals(1, user.getId());
        assertEquals("User 1", user.getName());
    }

    @Test
    void testGetUserByIdAndPhone_nonExistingUser() {
        Optional<User> userOpt = userService.getUserByIdAndPhone(10, 1234567890L);
        assertFalse(userOpt.isPresent(), "Non-existing user should not be found");
    }

    @Test
    void testDeleteUser_existingUser() {
        userService.deleteUser(1);

        assertEquals(4, userService.getAllUsers().size(), "User list should contain 4 users after deletion");
        Optional<User> userOpt = userService.getUserByIdAndPhone(1, 1234567891L);
        assertFalse(userOpt.isPresent(), "Deleted user should not be found in the user list");
    }
    
    @Test
    void testDeleteUser_nonExistingUser() {
        userService.deleteUser(10);

        assertEquals(5, userService.getAllUsers().size(), "User list size should remain unchanged for non-existing user deletion");
    }
}
