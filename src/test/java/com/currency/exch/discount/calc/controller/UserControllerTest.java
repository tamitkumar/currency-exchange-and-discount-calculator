package com.currency.exch.discount.calc.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.currency.exch.discount.calc.model.User;
import com.currency.exch.discount.calc.services.ExchangeRateService;
import com.currency.exch.discount.calc.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
    private ExchangeRateService exchangeRateService;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testGetAllUsers() throws Exception {
		List<User> users = Arrays.asList(User.builder().affiliate(false)
				.createdOn(LocalDate.now().minus(3, ChronoUnit.YEARS)).id(1).name("User1").email("user1@example.com")
				.phone(1234567891).employee(false).modifiedOn(LocalDate.now()).build(), User.builder().affiliate(false)
				.createdOn(LocalDate.now()).id(2).name("User2").email("user2@example.com")
				.phone(1234567892).employee(true).modifiedOn(LocalDate.now()).build());

		when(userService.getAllUsers()).thenReturn(users);

		mockMvc.perform(get("/api/v1/users")).andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(users)));
	}

	@Test
	void testAddUser() throws Exception {
		User user = User.builder().affiliate(false)
				.createdOn(LocalDate.now()).id(3).name("User3").email("user3@example.com")
				.phone(1234567893).employee(false).modifiedOn(LocalDate.now()).build();

		doNothing().when(userService).addUser(user);

		mockMvc.perform(post("/api/v1/users").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk());

		verify(userService).addUser(user);
	}

	@Test
	void testUpdateUser() throws Exception {
		int userId = 1;
		User updatedUser = User.builder().affiliate(false)
				.createdOn(LocalDate.now().minus(3, ChronoUnit.YEARS)).id(1).name("User1").email("user1@example.com")
				.phone(1234567891).employee(false).modifiedOn(LocalDate.now()).build();

		doNothing().when(userService).updateUser(updatedUser);

		mockMvc.perform(put("/api/v1/users/{id}", userId).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updatedUser))).andExpect(status().isCreated())
				.andExpect(header().string("Location", "http://localhost/api/v1/users/" + userId));

		verify(userService).updateUser(updatedUser);
	}

	@Test
	void testDeleteUser() throws Exception {
		int userId = 1;
		doNothing().when(userService).deleteUser(userId);
		mockMvc.perform(delete("/api/v1/users/{id}", userId)).andExpect(status().isAccepted());
		verify(userService).deleteUser(userId);
	}
}
