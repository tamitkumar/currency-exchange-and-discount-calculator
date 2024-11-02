package com.currency.exch.discount.calc.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.currency.exch.discount.calc.model.User;

@Service
public class UserService {
	
	private final List<User> userList = new ArrayList<>();
	
	public UserService() {
		for (int i = 1; i <= 5; i++) {
			User user = User.builder().id(i).name("User " + i).email("user" + i + "@example.com").phone(1234567890L + i)
					.build();

			if (i == 1 || i == 5) {
				user.setCreatedOn(LocalDate.now().minus(3, ChronoUnit.YEARS));
				user.setEmployee(false);
				user.setAffiliate(false);
			} else {
				user.setCreatedOn(LocalDate.now());
				user.setEmployee(i % 2 == 0);
				user.setAffiliate(i % 2 != 0);
			}
			user.setModifiedOn(LocalDate.now());
			userList.add(user);
		}
	}
	
	public List<User> getAllUsers() {
        return userList;
    }
	
	public void addUser(User user) {
        userList.add(user);
    }
	
	public void updateUser(User user) {
		getUserByIdAndPhone(user.getId(), user.getPhone()).ifPresent(existingUser -> {
			if (user.getName() != null) {
	            existingUser.setName(user.getName());
	        }
	        if (user.getEmail() != null) {
	            existingUser.setEmail(user.getEmail());
	        }
	        if (user.getEmployee() != null) {
	            existingUser.setEmployee(user.getEmployee());
	        }
	        if (user.getAffiliate() != null) {
	            existingUser.setAffiliate(user.getAffiliate());
	        }
	        if (user.getCreatedOn() != null) {
	            existingUser.setCreatedOn(user.getCreatedOn());
	        }
            existingUser.setModifiedOn(LocalDate.now());
        });
    }
	
	public Optional<User> getUserByIdAndPhone(int id, long phone) {
        return userList.stream().filter(user -> user.getId() == id && user.getPhone() == phone).findFirst();
    }
	
	public void deleteUser(int id) {
        userList.removeIf(user -> user.getId() == id);
    }
}
