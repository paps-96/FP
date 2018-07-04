import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class Main {


	public static void main(String[] args) {
		HashMap<String, User> userlist;
	      try
	      {
	         FileInputStream fis = new FileInputStream("userlist.ser");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         userlist = (HashMap<String, User>) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(IOException ioe)
	      {
	         ioe.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Class not found");
	         c.printStackTrace();
	         return;
	      }
		Login b=new Login(userlist);
		b.init();
	}
}
