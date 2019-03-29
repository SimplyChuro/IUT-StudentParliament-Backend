package models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.*;
import play.data.format.Formats;

@Entity
@Table(name = "posts")
public class Post extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	public String title;
	
	@Column(length = 2048)
	public String description;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="GMT")
	@Formats.DateTime(pattern="dd/MM/yyyy")
	public Date published;
	
	//Foreign Key
    @OneToMany(fetch = FetchType.LAZY, mappedBy="post")
    public List<Picture> pictures;
    
    @ManyToOne @JsonIgnore
    public User user;
    
	public static final Finder<Long, Post> find = new Finder<>(Post.class);
	
	
}
