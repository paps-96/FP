import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


//Log In Frame
public class Login extends JFrame {
	
	HashMap<String, User> userlist;
	Admin a=new Admin("admin","admin");
	JButton login=new JButton("Log-In");
	JTextField uname=new JTextField();
	JPasswordField password=new JPasswordField();
	JLabel errormsg=new JLabel();
	public User activeuser;
	
	public Login(HashMap<String, User> userlist) {
		super("Floor Plan Demo");
		userlist.put("admin",a);
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		setIconImages(icons);
		setVisible(true);
        setSize(400, 175);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.userlist=userlist;

	}
	
	public void init() {
		JPanel loginPanel = new JPanel();
		setContentPane(loginPanel);
		
		loginPanel.setLayout(new GridLayout(3,1));
		JPanel msgs=new JPanel();
		msgs.setLayout(new GridLayout(2,1));
		msgs.add(new JLabel("Welcome!!!!",SwingConstants.CENTER));
		msgs.add(errormsg);
		loginPanel.add(msgs);
		JPanel userinfo=new JPanel();
		userinfo.setSize(250, 175);
		userinfo.setLayout(new GridLayout(2,2));
		userinfo.add(new JLabel("Username :"));
		userinfo.add(uname);
		userinfo.add(new JLabel("Password :"));
		userinfo.add(password);
		loginPanel.add(userinfo);
		loginPanel.add(login);
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			  {
				if (userlist.containsKey(uname.getText())) {
					String a=new String(password.getPassword());
					if(a.equals(userlist.get(uname.getText()).password)) {
						errormsg.setText("");
						activeuser=userlist.get(uname.getText());
						dispose();
						if (userlist.get(uname.getText()) instanceof Admin ) {
							MainMenu m=new MainMenu(userlist);
							m.init();
							/*try {
								URL url=new URL("https://gist.githubusercontent.com/davidnunez/5536346/raw/9cd6ed459b3d259508d2b9091f96a79678984953/data.csv");
								////ReadableByteChannel rbc = Channels.newChannel(url.openStream());
								//FileOutputStream fos = new FileOutputStream("information.csv");
								//fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
								InputStream input = url.openStream();
								Reader reader = new InputStreamReader(input, "UTF-8");
								Iterable<CSVRecord> records = null;
								try {
									records = CSVFormat.EXCEL.parse(reader);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								for (CSVRecord record : records) {
									System.out.println(record.get(0));
								}
							} catch (MalformedURLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}*/



						}else {
							UserMenu u=new UserMenu(userlist);
							u.init();
						}
						
						
					}else {
						errormsg.setForeground(Color.RED);
						errormsg.setText("Incorrect Username or Password");
					}
				}else {
					errormsg.setForeground(Color.RED);
					errormsg.setText("Incorrect Username or Password");
				}
			  }
		});		
	}
}








