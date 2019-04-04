package services;

import java.util.List;

import com.typesafe.config.ConfigFactory;

import models.User;

public class GeneratedData {

	private static final String ADMIN_MAIL = "custom.settings.admin.mail";
	private static final String ADMIN_PASSWORD = "custom.settings.admin.password";
	
	public void generateAdmin() {
		if (ConfigFactory.load().hasPath(ADMIN_MAIL) && ConfigFactory.load().hasPath(ADMIN_PASSWORD)) {
			User admin = new User();
			admin.name = "Administrator";
			admin.surname = "Administrator";
			admin.email = ConfigFactory.load().getString(ADMIN_MAIL);
			admin.setPassword(ConfigFactory.load().getString(ADMIN_PASSWORD));
			admin.save();
		}
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