package controllers;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import models.Picture;
import models.Post;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import services.Secured;

public class PictureController  extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private JsonNode jsonNode;
	private ObjectNode objectNode;
	private Picture picture;
	private List<Picture> pictures;
	private int size;
	
	@Inject
    public PictureController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get picture	
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				picture = Picture.find.byId(id);
				return ok(Json.toJson(picture));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get pictures
	public CompletionStage<Result> getAll(Integer min, Integer max) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				pictures = Picture.find.query()
						.where()
						.orderBy("published desc")
						.setFirstRow(min)
				        .setMaxRows(max)
				        .findList();
				return ok(Json.toJson(pictures));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());		
	}
	
	//Get Picture size	
	public CompletionStage<Result> getSize() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = Picture.find.all().size();
				return ok(Json.toJson(size));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Get Post Page size	
	public CompletionStage<Result> getPageSize() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = Picture.find.all().size();
				int pageChecker = ((int) (size/10));
				if(size > (pageChecker/10)) {
					pageChecker++;
				}
				return ok(Json.toJson(pageChecker));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Create picture  
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				jsonNode = request.body().asJson();	
				picture = new Picture();
				picture.name = jsonNode.findValue("name").asText();
				picture.published = new Date();
				picture.save();
				return ok(Json.toJson(picture));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update picture  
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				jsonNode = request.body().asJson();
				picture = Picture.find.byId(id);
				picture.name = jsonNode.findValue("name").asText();
				picture.published = new Date();
				picture.update();
				return ok(Json.toJson(picture));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete picture 
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				picture = Picture.find.byId(id);
				picture.delete();
				return ok(Json.toJson(""));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
}
