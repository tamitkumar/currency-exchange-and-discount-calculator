package com.currency.exch.discount.calc.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.currency.exch.discount.calc.model.User;
import com.currency.exch.discount.calc.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Operations related to user management")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	@Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
	@ApiResponse(responseCode = "200", description = "All User Retrieved")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@PostMapping
	@Operation(summary = "Add a new user", description = "Adds a new user to the system")
	@ApiResponse(responseCode = "200", description = "User added successfully")
	public ResponseEntity<Void> addUser(
			@Parameter(description = "Details of the user to be created", required = true) @RequestBody User user) {
		userService.addUser(user);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update existing user", description = "Updates details of the user with the provided ID")
	@ApiResponse(responseCode = "201", description = "User updated successfully")
	public ResponseEntity<Void> updateUser(
			@Parameter(description = "ID of the user to update", required = true) @PathVariable int id,
			@Parameter(description = "Updated user details", required = true) @RequestBody User user) {
		user.setId(id);
		userService.updateUser(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete user by ID", description = "Deletes the user with the specified ID from the system")
	@ApiResponse(responseCode = "202", description = "User deleted successfully")
	public ResponseEntity<Void> deleteUser(
			@Parameter(description = "ID of the user to delete", required = true) @PathVariable int id) {
		userService.deleteUser(id);
		return ResponseEntity.accepted().build();
	}

}
