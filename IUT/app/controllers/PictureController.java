package controllers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;

import models.Picture;
import models.Post;
import play.libs.Json;
import play.libs.Files.TemporaryFile;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import play.Environment;
import services.PictureFilter;
import services.Secured;

public class PictureController  extends Controller {

	private PictureFilter filter = new PictureFilter();
	private HttpExecutionContext httpExecutionContext;
	private JsonNode jsonNode;
	private ObjectNode objectNode;
	private Picture picture;
	private List<Picture> pictures;
	private int size;
	private static final String IMAGE_URL = "custom.settings.host.imageUrl";
	
	@Inject
	private Environment environment;
	
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
	
	//Get Picture Page size	
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
				Http.MultipartFormData<TemporaryFile> body = request.body().asMultipartFormData();
		        Http.MultipartFormData.FilePart<TemporaryFile> pictureFile = body.getFile("picture");
		        
		        if (pictureFile != null) {
		            String fileName = pictureFile.getFilename();
		            long fileSize = pictureFile.getFileSize();
		            String contentType = pictureFile.getContentType();

		            if(filter.isValidPicture(fileName, contentType, fileSize)) {
		            	if (ConfigFactory.load().hasPath(IMAGE_URL)) {
		                    TemporaryFile file = pictureFile.getRef();
				            Date currentDate = new Date();
				            String customName = currentDate.getTime() + "_" + fileName;
			                
				            Picture newPicture = new Picture();
				            
				            newPicture.name = customName;
				            newPicture.published = currentDate;
				            newPicture.type = contentType;
				            newPicture.url = ConfigFactory.load().getString(IMAGE_URL) + customName;
				            
				            File newFile = new File(environment.rootPath().toString() + "//public//images//" + customName);
			                file.moveFileTo(newFile);
			            
			                newPicture.save();
				            return ok(Json.toJson(newPicture));	
		                } else {
		                    return badRequest();    
		                }
		            } else {
		            	return forbidden();
		            }
		        } else {
		            return badRequest();
		        }
			} catch(Exception e) {
				return badRequest();
			}
		}, httpExecutionContext.current());
	}
	
	//Update picture -- inactive
	@Security.Authenticated(Secured.class)
	public CompletionStage<Result> update(Http.Request request, Long id) {
		return calculateResponse().thenApplyAsync(answer -> {	
			try {
				jsonNode = request.body().asJson();
				picture = Picture.find.byId(id);
//				picture.name = jsonNode.findValue("name").asText();
//				picture.url = jsonNode.findValue("url").asText();
//				picture.published = new Date();
//				picture.update();
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
