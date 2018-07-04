import java.io.Serializable;

public class User  implements Serializable {

	String username;
	String password;

	
	public User(String name,String password) {
		this.password=password;
		this.username=name;
	}
	
	
	
	
}
