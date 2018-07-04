import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

//Add Building Frame
class AddBuilding extends JFrame{
	
	JComboBox<String> buildings;
	HashMap<String, Building> buildinglist;
	
	public AddBuilding(HashMap<String, Building> buildinglist,JComboBox<String> buildings) {
		super("Add Building");
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		setIconImages(icons);
		setVisible(true);
        setSize(400, 150);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        this.buildinglist=buildinglist;
        this.buildings=buildings;
	}
	
	public void init() {
		JPanel AddBuildingPanel=new JPanel();
		setContentPane(AddBuildingPanel);
		AddBuildingPanel.setLayout(new GridLayout(4,2));
		JTextField bname=new JTextField();
		JTextField address=new JTextField();
		AddBuildingPanel.add(new JLabel("Building Name :"));
		AddBuildingPanel.add(bname);
		AddBuildingPanel.add(new JLabel("Address :"));
		AddBuildingPanel.add(address);
		JLabel errormsg=new JLabel();
		errormsg.setForeground(Color.RED);
		AddBuildingPanel.add(errormsg);AddBuildingPanel.add(new JLabel());
		AddBuildingPanel.add(new JLabel());
		JPanel buttons=new JPanel();
		buttons.setLayout(new GridLayout(1,2));
		JButton cancel=new JButton("Cancel");
		JButton done=new JButton("Done");
		buttons.add(done);buttons.add(cancel);
		AddBuildingPanel.add(buttons);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				errormsg.setText("");
				if (bname.getText().equals("") || address.getText().equals("")) {
					errormsg.setText("Fill the Buildind Name & Address");
				}else if(buildinglist.containsKey(bname.getText())) {
					errormsg.setText("Building already exists");
				}else {
					buildinglist.put(bname.getText(),new Building(bname.getText(),address.getText()));
					dispose();
					buildings.removeAllItems();
					buildings.addItem("-Select Building-");
					for(String b:buildinglist.keySet()) {
						buildings.addItem(buildings.getItemCount()+"-"+b);
					}
					try
			           {
			                  FileOutputStream fos = new FileOutputStream("buildinglist.ser");
			                  ObjectOutputStream oos = new ObjectOutputStream(fos);
			                  oos.writeObject(buildinglist);
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