package services;

import java.util.List;

import models.User;

public class GeneratedData {
	
	public void generateAdmin() {
		User admin = new User();
		admin.name = "Zijah";
		admin.surname = "Ribic";
		admin.email = "test@email.com";
		admin.setPassword("SimplePassword");
		admin.save();
	}
	
	public Boolean noAdmin() {
		List<User> users = User.find.all();
		if(users.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}