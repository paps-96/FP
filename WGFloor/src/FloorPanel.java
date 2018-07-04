import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FloorPanel extends JPanel {

	Floor f;
	HashMap<String, Building> buildinglist;
	boolean editable;
	String wsname;
	JPanel main;
	JPanel buttons;
	
	JPanel floorplanarea=new JPanel() {
		@Override
		  protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		        g.drawImage(f.floorplan.getImage(), 0, 0,this.getWidth(),this.getHeight(), null);
		        g.setColor(Color.GREEN);
		        for (Map.Entry<String,Point> p:f.ws.entrySet()) {
		        	g.drawRect(p.getValue().x, p.getValue().y, 35, 17);
		        	g.drawString(p.getKey(), p.getValue().x+10,p.getValue().y+12);
		        }
		}
		

	};
	
	

	
	public FloorPanel(Floor f,HashMap<String, Building> buildinglist) {
		this.f=f;
		this.buildinglist=buildinglist;
		editable=false;
	}
	
	public void init() {
		
		this.setLayout(new BorderLayout());
		this.add(floorplanarea,BorderLayout.CENTER);
		main=new JPanel();
		this.add(main,BorderLayout.SOUTH);
		
		main.setLayout(new GridLayout(4,1));
		JPanel floorplaninfo=new JPanel();
		floorplaninfo.setLayout(new GridLayout(1,4));
		JLabel edit=new JLabel("", SwingConstants.CENTER);
		edit.setForeground(Color.BLACK);
		JLabel edit2=new JLabel();
		JLabel available=new JLabel("Workstations Available",SwingConstants.CENTER);available.setForeground(Color.GREEN);
		JLabel notav=new JLabel("Reserved Workstations",SwingConstants.CENTER);notav.setForeground(Color.RED);
		JLabel title=new JLabel("Floor Info:");
		floorplaninfo.add(edit);floorplaninfo.add(available);floorplaninfo.add(notav);floorplaninfo.add(edit2);
		main.add(floorplaninfo);
        title.setFont(new Font(title.getFont().toString(), Font.PLAIN, 22));
		main.add(title);
		JPanel info = new JPanel();
		info.setLayout(new GridLayout(2,1));
		info.add(new JLabel("Workstations:"));
		info.add(new JLabel("Workstations Available:"));
		main.add(info);
		JButton update=new JButton("Update Floorplan");
		JButton add=new JButton("Add New Workstations");
		JPanel adding=new JPanel();
		adding.setLayout(new FlowLayout());
		adding.add(new JLabel("Click on the floorplan area to add workstation"));
		JButton finish = new JButton("Done");
		adding.add(finish);

		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				editable=true;
				main.remove(3);
				main.add(adding,null,3);
				main.revalidate();
				main.repaint();
			}
			
		});
		
		JButton remove=new JButton("Remove Workstation");
		JButton report=new JButton("Generate Floor Report");		
		buttons=new JPanel(new FlowLayout());
		buttons.add(update);buttons.add(add);buttons.add(remove);buttons.add(report);
		main.add(buttons);
		
		
		finish.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				editable=false;
				main.remove(3);
				main.add(buttons,null,3);
				main.revalidate();
				main.repaint();
			}});
		
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				main.remove(3);
				main.add(deletePanel(),null,3);
				main.revalidate();
				main.repaint();
			}});
		
		
		update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			    JFileChooser chooser = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
			    chooser.setAcceptAllFileFilterUsed(false);
			    chooser.setFileFilter(filter);
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       try {
					 f.floorplan=new ImageIcon(ImageIO.read(chooser.getSelectedFile()));
					 floorplanarea.repaint();
					 f.ws.clear();
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			       
			    }
		}});
		
	      addMouseListener(new MouseAdapter() { 
	          public void mousePressed(MouseEvent me) { 
	            if(floorplanarea.contains(me.getPoint()) && editable) {
	            	WSName s=new WSName(me.getPoint(),f.ws,floorplanarea,buildinglist);
	            	s.init();
	            }
	          } 
	          
	          
	        }); 
	      
	      
		
		
	}
	
	public JPanel deletePanel() {
        JPanel removeFloor=new JPanel();
        removeFloor.setLayout(new FlowLayout());
        removeFloor.add(new JLabel("Enter workstation name you wish to remove:"));
        JTextField name=new JTextField();
        name.setColumns(20);
        

        removeFloor.add(name);
        JButton ok=new JButton("Ok");
        removeFloor.add(ok);

        ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String s=name.getText();
				if(f.ws.containsKey(s)) {
					f.ws.remove(s);
				}
				main.remove(3);
				main.add(buttons,null,3);
				main.revalidate();
				main.repaint();
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
			}});
        JButton cancel=new JButton("Cancel");
        removeFloor.add(cancel);
        cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				main.remove(3);
				main.add(buttons,null,3);
				main.revalidate();
				main.repaint();
			}
        	
        });
        
        
        return removeFloor;
	}

	

}
