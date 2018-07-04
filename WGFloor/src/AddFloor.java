import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileTypeDetector;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

//Add Building Frame
class AddFloor extends JFrame{
	
	JComboBox<String> floors;
	HashMap<String, Floor> floorlist;
	HashMap<String, Building> buildinglist;
	Boolean flag;
	File loaded;
	Building bu;
	ImageIcon image;
	
	public AddFloor(Building b,JComboBox<String> floors,HashMap<String, Building> buildinglist) {
		super("Add Floor");
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		setIconImages(icons);
		setVisible(true);
        setSize(400, 150);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        this.floorlist=b.floors;
        this.floors=floors;
        this.buildinglist=buildinglist;
        this.bu=b;
        flag=false;
	}
	
	public void init() {
		JPanel AddBuildingPanel=new JPanel();
		setContentPane(AddBuildingPanel);
		AddBuildingPanel.setLayout(new GridLayout(4,2));
		JTextField bname=new JTextField();
		AddBuildingPanel.add(new JLabel("Floor Name :"));
		AddBuildingPanel.add(bname);
		AddBuildingPanel.add(new JLabel("Floorplan:"));
		JLabel imagename=new JLabel("-Empty-");
		imagename.setForeground(Color.GRAY);
		AddBuildingPanel.add(imagename);
		JLabel errormsg=new JLabel();
		errormsg.setForeground(Color.RED);
		AddBuildingPanel.add(errormsg);AddBuildingPanel.add(new JLabel());
		JButton load=new JButton("Upload Floorplan");
		AddBuildingPanel.add(load);
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
				if (bname.getText().equals("")) {
					errormsg.setText("Fill the Floor Name");
				}else if(floorlist.containsKey(bname.getText()) && floorlist!=null) {
					errormsg.setText("Floor name already exists");
				}else {
					if (flag) {
						//loaded.renameTo(new File("./"+bu.name+bname.getText()++".png"));
						floorlist.put(bname.getText(),new Floor(bname.getText(),image));
					}else {
						floorlist.put(bname.getText(),new Floor(bname.getText()));	
					}
					dispose();
					floors.removeAllItems();
					floors.addItem("-Select Floor-");
					for(String b:floorlist.keySet()) {
						floors.addItem(floors.getItemCount()+"-"+b);
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
		
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			    JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
			    chooser.setAcceptAllFileFilterUsed(false);
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       try {
					 loaded=chooser.getSelectedFile();
					 image=new ImageIcon(ImageIO.read(loaded));
					 imagename.setForeground(Color.DARK_GRAY);
					 imagename.setText(loaded.getName());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			       flag=true;
			       
			    }
		}});
		
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
			
		});
	}
	
	
	
	
	
}