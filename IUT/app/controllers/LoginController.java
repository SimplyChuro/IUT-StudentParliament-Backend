package controllers;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigFactory;

import models.User;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.Secured;

public class LoginController extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private User user;
	private JsonNode jsonNode;
	private ObjectNode response;
	private String authToken;
	
	@Inject
    public LoginController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	public static User getUser(Http.Request request) {
		String token = request.session().getOptional("auth_token").get();
	    return User.findByAuthToken(token);
	}
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	public CompletionStage<Result> login(Http.Request request) {
	    return calculateResponse().thenApplyAsync(answer -> {
	    jsonNode = request.body().asJson();
	    response = Json.newObject();
		    try {
			    user = User.find.query().where()
			    		.conjunction()
			    		.eq("email", jsonNode.findPath("email").textValue())
			    		.endJunction()
			    		.findOne();	 
			    
			    if (BCrypt.checkpw(jsonNode.findPath("password").textValue(), user.getPassword())) {   
			    	user.createToken();
			    	response.put("token", user.getAuthToken());
			        return ok(response).addingToSession(request, "id", user.id.toString()).addingToSession(request, "auth_token",  user.getAuthToken());    
		        } else {
		        	response.put("error_message", "Incorrect email or password");
			    	return notFound(response);
		    	}
		    } catch(Exception e) {
		    	response.put("error_message", "Incorrect email or password");
		    	return notFound(response);
		    }
	    }, httpExecutionContext.current());
	}
	
	public CompletionStage<Result> logout(Http.Request request) {
	    return calculateResponse().thenApplyAsync(answer -> {
	    	getUser(request).deleteAuthToken();
	    	return ok(Json.toJson("")).removingFromSession(request, "id");
	    }, httpExecutionContext.current());
    }
	
}