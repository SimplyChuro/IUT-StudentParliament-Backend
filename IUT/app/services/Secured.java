package services;

import java.util.Optional;

import com.typesafe.config.ConfigFactory;

import models.User;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

	
	@Override
    public Result onUnauthorized(Http.Request request) {
        return unauthorized();
    }

	@Override
	public Optional<String> getUsername(Request request) {
		try {
			Long checker = Long.parseLong(request.session().getOptional("id").get());
			User userChecker = User.find.byId(checker);
			if(userChecker != null) {
				return request.session().getOptional("id");
			} else {
				return null;
			}
		} catch(Exception e) {
			return null;
		}
		
	}

}