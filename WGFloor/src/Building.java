import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Building implements Serializable {
	
	String name;
	String address;
	HashMap<String,Floor> floors;
	
	public Building(String name,String address) {
		this.name=name;
		this.address=address;
		floors=new HashMap<String,Floor>();
	}

}
