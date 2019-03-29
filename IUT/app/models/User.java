package models;

import java.util.*;
import javax.persistence.*;

import org.mindrot.jbcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.ebean.*;
import play.libs.Json;
import play.data.format.*;
import play.data.validation.*;

@Entity
@Table(name = "users")
public class User extends Model {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;
	
	@Column(length = 256, nullable = false)
	public String name;
    
    @Column(length = 256, nullable = false)
	public String surname;
    
    @Column(length = 256, unique = true, nullable = false)
	public String email;
    
    @JsonIgnore
	private String password;
    
    @JsonIgnore
	private String authToken;
    

	//Foreign Key

	@OneToMany(fetch = FetchType.LAZY, mappedBy="user")
    public List<Post> post; 
	
	public static final Finder<Long, User> find = new Finder<>(User.class);
	
	public String createToken() {
        authToken = UUID.randomUUID().toString();
        save();
        return authToken;
    }

    public void deleteAuthToken() {
        authToken = null;
        save();
    }
   
    public Boolean hasAuthToken() {
    	if(authToken != null) {
    		return true;
    	}else {
    		return false;
    	}
    }

    public static User findByAuthToken(String authToken) {
        if (authToken == null) {
            return null;
        }

        try  {
            return find.query().where().eq("authToken", authToken).findOne();
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getAuthToken() {
    	return authToken;
    }
    
    public String getPassword() {
		return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());;
    }

}
