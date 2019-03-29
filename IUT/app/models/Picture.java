package models;

import java.util.*;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.ebean.*;
import play.data.format.*;
import play.libs.Json;

@Entity
@Table(name = "pictures")
public class Picture extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
		
	public String name;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date published;
	
	
    //Foreign Key
    @ManyToOne @JsonIgnore
    public Post post;
    
	public static final Finder<Long, Picture> find = new Finder<>(Picture.class);
	

}