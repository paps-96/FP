import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

//Add User Frame
class AddUser extends JFrame {
		
	HashMap<String, User> userlist;
	public AddUser(HashMap<String, User> userlist) {
		super("Add User");
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		setIconImages(icons);
		setVisible(true);
        setSize(400, 200);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        this.userlist=userlist;
	}
	
	public void init() {
		JPanel AddUserPanel=new JPanel();
		setContentPane(AddUserPanel);
		AddUserPanel.setLayout(new GridLayout(6,2));
		JTextField uname=new JTextField();
		JPasswordField password=new JPasswordField();
		JPasswordField password2=new JPasswordField();
		AddUserPanel.add(new JLabel("Username :"));
		AddUserPanel.add(uname);
		AddUserPanel.add(new JLabel("Password :"));
		AddUserPanel.add(password);
		AddUserPanel.add(new JLabel("Confirm Password :"));
		AddUserPanel.add(password2);
		JLabel errormsg=new JLabel();
		errormsg.setForeground(Color.RED);
		AddUserPanel.add(errormsg);AddUserPanel.add(new JLabel());
		JRadioButton chooseUser = new JRadioButton("User",true);
		JRadioButton chooseAdmin = new JRadioButton("Admin");
		ButtonGroup group = new ButtonGroup();
		group.add(chooseUser);
		group.add(chooseAdmin);
		AddUserPanel.add(chooseUser);
		AddUserPanel.add(chooseAdmin);
		AddUserPanel.add(new JLabel());
		JPanel buttons=new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		JButton cancel=new JButton("Cancel");
		JButton done=new JButton("Done");
		buttons.add(done);buttons.add(cancel);
		AddUserPanel.add(buttons);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				boolean flag=true;
				errormsg.setText("");
				if (!Arrays.equals(password.getPassword(), password2.getPassword())) {errormsg.setText("The password does not match with the first one \n");flag=false;}
				if(uname.getText().length()<4 || uname.getText().length()>10 || password.getPassword().length<4 || password.getPassword().length>10) {flag=false;errormsg.setText(errormsg.getText() + "Both Username and Password must contains between 4 and 10 characters \n");}
				if(userlist.containsKey(uname.getText())) {errormsg.setText(errormsg.getText() + "This username already exists");flag=false;}
				if(flag) {
					if(chooseUser.isSelected()) {
						userlist.put(uname.getText(), new User(uname.getText(),new String(password.getPassword())));
						//System.out.println(uname.getText()+"   "+new String(password.getPassword()));
					}else if (chooseAdmin.isSelected()) {
						userlist.put(uname.getText(), new Admin(uname.getText(),new String(password.getPassword())));
					}
					dispose();
					try
			           {
			                  FileOutputStream fos = new FileOutputStream("userlist.ser");
			                  ObjectOutputStream oos = new ObjectOutputStream(fos);
			                  oos.writeObject(userlist);
			                  oos.close();
			                  fos.close();
			           }catch(IOException ioe){
			                  ioe.printStackTrace();
			           }		
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}});
	}
	
	
}
