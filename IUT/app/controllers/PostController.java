package controllers;

import java.util.ArrayList;
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

public class PostController extends Controller {
	
	private HttpExecutionContext httpExecutionContext;
	private List<Post> posts;
	private Post post;
	private JsonNode jsonNode;
	private int size;
	
	@Inject
    public PostController(HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
    }
	
	private static CompletionStage<String> calculateResponse() {
        return CompletableFuture.completedFuture("42");
    }
	
	//Get post
	public CompletionStage<Result> get(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				post = Post.find.byId(id);
		       
				return ok(Json.toJson(post));
			} catch(Exception e) {
				return notFound();
			}
		}, httpExecutionContext.current());
	}
	
	//Get posts
	public CompletionStage<Result> getAll(Integer min, Integer max) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				posts = Post.find.query().where()
						.orderBy("published desc")
						.setFirstRow(min)
				        .setMaxRows(max)
				        .findList();
				
				return ok(Json.toJson(posts));
			} catch(Exception e) {
				return notFound();
			}
		}, httpExecutionContext.current());
	}
	
	//Get Post size	
	public CompletionStage<Result> getSize() {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				size = Post.find.all().size();
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
				size = Post.find.all().size();
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
	
	//Create post
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> create(Http.Request request) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {		
				jsonNode = request.body().asJson().get("post");
				
				post = new Post();
				post.title = jsonNode.findPath("title").asText();	
				post.description = jsonNode.findPath("description").asText();
				post.save();
				
				for(JsonNode pictureNode : jsonNode.get("pictures")) {
					Picture picture = new Picture();
					picture.name = pictureNode.findPath("name").asText();
					picture.published = new Date();
					picture.post = post;
					picture.save();
				}
				
				return ok(Json.toJson(post));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update post
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {
				jsonNode = request.body().asJson().get("product");	
				
				post = Post.find.byId(id);
				post.title = jsonNode.findPath("title").asText();	
				post.description = jsonNode.findPath("description").asText();
				post.update();
				
				for(Picture picture : post.pictures) {	
					Boolean checker = false;
					try {
						for(JsonNode pictureNode : jsonNode.get("pictures")) {

							if(picture.id == pictureNode.findPath("id").asLong()) {
								checker = true;
							}
						}
						
						if(!(checker == true)) {
							picture.delete();
						}
						
					} catch(Exception e) {
						return badRequest();
					}
				}	
				
				return ok(Json.toJson(post));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Delete post
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> delete(Long id) {
		return calculateResponse().thenApplyAsync(answer -> {
			try {	
				post = Post.find.byId(id);
				
				for(Picture picture : post.pictures) {
					picture.delete();
				}
			
				post.delete();
						
				return ok(Json.toJson(""));
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}

}