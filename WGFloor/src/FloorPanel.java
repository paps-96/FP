import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;

public class FloorPanel extends JPanel {

	Floor f;
	Building b;
	HashMap<String, Building> buildinglist;
	boolean editable;
	String wsname;
	JPanel main;
	JPanel buttons;
	JLabel av;
	JLabel all;
	int sqrsize=8;
	
	JPanel floorplanarea=new JPanel() {
		@Override
		  protected void paintComponent(Graphics g) {
		    super.paintComponent(g);
		        g.drawImage(f.floorplan.getImage(), 0, 0,this.getWidth(),this.getHeight(), null);
		        ArrayList<String> wss=new ArrayList<String>();
		        int available=0;
		        try {
					wss=setColor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        //setToolTipText("Inside rect");
		        for (Map.Entry<String,Point> p:f.ws.entrySet()) {
		        	if(wss.contains(p.getKey())) {
		        		g.setColor(Color.RED);
		        	}else {
		        		g.setColor(Color.GREEN);
		        		available++;
		        	}
		        	
		        	g.fillRect(p.getValue().x, p.getValue().y, getsqrsize(), getsqrsize());
		        	//System.out.println(getsqrsize());
		        	//g.drawString(p.getKey(), p.getValue().x+10,p.getValue().y+12);
		        }
		       all.setText(Integer.toString(f.ws.size()));
		       av.setText(Integer.toString(available));
		       
		}
		

	};
	
	

	
	public FloorPanel(Floor f,HashMap<String, Building> buildinglist,Building b) {
		this.f=f;
		this.b=b;
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
		info.setLayout(new GridLayout(2,2));
		all=new JLabel();
		info.add(new JLabel("Workstations:"));info.add(all);
		av=new JLabel();
		info.add(new JLabel("Workstations Available:"));info.add(av);
		main.add(info);
		JButton update=new JButton("Update Floorplan");
		JButton add=new JButton("Add New Workstations");
		JPanel adding=new JPanel();
		adding.setLayout(new FlowLayout());
		adding.add(new JLabel("Click on the floorplan area to add workstation"));
		JButton finish = new JButton("Done");
		adding.add(finish);
		
		JSlider slider = new JSlider(JSlider.VERTICAL, 8, 34, 8);
		slider.setToolTipText("Set workstation size");
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				setsqrsize(slider.getValue());
				//main.revalidate();
				floorplanarea.repaint();

				//System.out.println(slider.getValue());
			}});
		//	this.add(new JLabel("Workstation Size"), BorderLayout.EAST);
		this.add(slider, BorderLayout.EAST);

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
		buttons=new JPanel(new FlowLayout());
		buttons.add(update);buttons.add(add);buttons.add(remove);
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
		
		floorplanarea.addMouseListener(new MouseAdapter() { 
	          public void mousePressed(MouseEvent me) { 
	            if(floorplanarea.contains(me.getPoint()) && editable) {
	            	WSName s=new WSName(me.getPoint(),f.ws,floorplanarea,buildinglist);
	            	s.init();
	            }
	          }

	        }); 
	      
	      floorplanarea.addMouseMotionListener(new MouseMotionAdapter() { 
	           public void mouseMoved(MouseEvent me) { 
	        	   
	        	   String tip=null;
			        for (Map.Entry<String,Point> p:f.ws.entrySet()) {
			        	if(new Rectangle(p.getValue().x, p.getValue().y, getsqrsize(), getsqrsize()).contains(me.getPoint())) {
				        	try {
								tip=("Workstation ID: "+p.getKey()+setWSHolder(p.getKey()));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			        	}
			        }
		        	floorplanarea.setToolTipText(tip);

	                ToolTipManager.sharedInstance().mouseMoved(me);

	          }
	        }); 
	      
	      
	}
	
	public JPanel deletePanel() {
        JPanel removeFloor=new JPanel();
        removeFloor.setLayout(new FlowLayout());
        removeFloor.add(new JLabel("Enter workstation ID you wish to remove:"));
       // JTextField name=new JTextField();
        JComboBox<String> wsdelete=new JComboBox<String>();
        for(String s:f.ws.keySet()) {
        	wsdelete.addItem(s);
        }
       // name.setColumns(20);
        
        removeFloor.add(wsdelete);
      //  removeFloor.add(name);
        JButton ok=new JButton("Ok");
        removeFloor.add(ok);

        ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
				f.ws.remove(wsdelete.getSelectedItem());
				wsdelete.remove(wsdelete.getSelectedIndex());
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
	
	public ArrayList<String> setColor() throws IOException {
		URL url=new URL("https://raw.githubusercontent.com/paps-96/FP/final3/db.csv");

		InputStream input = url.openStream();
		Reader reader = new InputStreamReader(input, "UTF-8");

		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
		ArrayList<String> wss=new ArrayList<String>();
		for (CSVRecord record : records) {
			if (record.get(2).equals(f.name) && record.get(1).equals(b.name) ) {
				wss.add(record.get(3));
			}
				
		    //System.out.println(lastName);
		}	
		return wss;
	}

	public String setWSHolder(String wsID) throws IOException {
		URL url=new URL("https://raw.githubusercontent.com/paps-96/FP/final3/db.csv");

		InputStream input = url.openStream();
		Reader reader = new InputStreamReader(input, "UTF-8");

		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
		String name="";
		for (CSVRecord record : records) {
			if(record.get(3).equals(wsID)&&record.get(2).equals(f.name) && record.get(1).equals(b.name) ) {
				name="    Name: "+record.get(0);
			}
		    //System.out.println(lastName);
		}	
		return name;
	}
	
	public void setsqrsize(int sqrsize){
		this.sqrsize=sqrsize;
	}
	
	public int getsqrsize() {
		return sqrsize;
	}
	

}
