import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class FloorUserPanel extends JPanel {

	Floor f;
	Building b;
	HashMap<String, Building> buildinglist;
	boolean editable;
	String wsname;
	JPanel main;
	JPanel buttons;
	JLabel av;
	JLabel all;
	
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
		        	
		        	g.fillRect(p.getValue().x, p.getValue().y, 17, 17);
		        	//g.drawString(p.getKey(), p.getValue().x+10,p.getValue().y+12);
		        }
		       all.setText(Integer.toString(f.ws.size()));
		       av.setText(Integer.toString(available));
		       
		}
		

	};
	
	

	
	public FloorUserPanel(Floor f,HashMap<String, Building> buildinglist,Building b) {
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
		JPanel adding=new JPanel();
		adding.setLayout(new FlowLayout());
		adding.add(new JLabel("Click on the floorplan area to add workstation"));
		
		JButton report=new JButton("Generate Floor Report");		
		buttons=new JPanel(new FlowLayout());
		buttons.add(report);
		main.add(buttons);
		
		


		
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
			        	if(new Rectangle(p.getValue().x, p.getValue().y, 17, 17).contains(me.getPoint())) {
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
	      
	      
	      report.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Document document = new Document(PageSize.A6);

				
				try {
					PdfWriter.getInstance(document, new FileOutputStream(b.name+"-"+f.name+".pdf"));
					document.open();
					Paragraph hello=new Paragraph("Building: "+ b.name);
					hello.add("\n");
					hello.add("Adress: "+b.address);
					hello.add("\n");
					hello.add("\n");
					hello.add("Report for Floor: "+f.name);
					hello.add("\n");
					hello.add("\n");
					hello.add("\n");
					hello.add("\n");
					hello.add("Number of employees: " + Integer.toString(setColor().size()));
					hello.add("\n");
					hello.add("Number of worksations: "+all.getText());
					hello.add("\n");
					hello.add("Available worksations: "+av.getText());
					//PdfContentByte canvas = writer.getDirectContentUnder();
					Image image = Image.getInstance("src/menu.png");
					//image.scaleAbsolute(PageSize.A6.getBorderWidth(),10);
					//image.setAbsolutePosition(0, 0);
					image.scaleToFit(240, 150);
					document.add(image);
					document.add(hello);
					
					

				} catch (FileNotFoundException | DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				document.close();				
			}
	    	  
	      });
		
		
	}
	
	
	
	public ArrayList<String> setColor() throws IOException {
		Reader in = new FileReader("db.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
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
		Reader in = new FileReader("db.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
		String name="";
		for (CSVRecord record : records) {
			if(record.get(3).equals(wsID)&&record.get(2).equals(f.name) && record.get(1).equals(b.name) ) {
				name="    Name: "+record.get(0);
			}
		    //System.out.println(lastName);
		}	
		return name;
	}
	

}