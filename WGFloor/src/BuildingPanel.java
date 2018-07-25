import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class BuildingPanel extends JTabbedPane {
	
	ImageIcon x = new ImageIcon(getClass().getResource("closeTab.gif"));
	Building b;
    JPanel mainpanel=new JPanel();
	JComboBox<String> floors=new JComboBox<String>();
	HashMap<String, Building> buildinglist=new HashMap<String, Building>();
	
	public BuildingPanel(Building b,HashMap<String, Building> buildinglist) {
		this.b=b;
		this.buildinglist=buildinglist;
	}
	
	public void init() {
        JPanel maintab=new JPanel();
        maintab.setLayout(new GridLayout(1,1));
        JPanel border=new JPanel();
        border.setLayout(new BorderLayout());
        mainpanel.setLayout(new GridLayout(7,2));
        mainpanel.add(new JLabel());
        JButton showbuilding=new JButton("Show Floor");
        mainpanel.add(showbuilding);
        showbuilding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// TODO Auto-generated method stub
				if (!(floors.getSelectedIndex()==0)) {
					String s=(String) floors.getSelectedItem();
					s=s.substring(s.lastIndexOf("-") + 1);
					//System.out.println(!tabAlreadyOpen("Building: "+s));
					if(!tabAlreadyOpen("Floor: "+s)) {
						buildingtab(b.floors.get(s));
					}
				}			}
        });
        mainpanel.add(new JLabel());mainpanel.add(new JLabel());
        mainpanel.add(new JLabel());
    	floors.addItem("-Select Floor-");
    	if (b.floors!=null) {
    		for(String f:b.floors.keySet()) {
    			floors.addItem(floors.getItemCount()+"-"+f);
    		}
    	}


        JButton addbuilding =new JButton("Add New Floor");
        mainpanel.add(addbuilding);
        addbuilding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddFloor m=new AddFloor(b,floors,buildinglist);
				m.init();
				
				
				// TODO Auto-generated method stub


			}
        });
        mainpanel.add(floors);mainpanel.add(new JLabel());
        mainpanel.add(new JLabel());
        JButton deletebuild=new JButton("Delete Floor");
        deletebuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TODO Auto-generated method stub
				mainpanel.remove(6);
				//System.out.println(buildings.getItemCount()-1);
				mainpanel.add(deletePanel(),null,6);
				mainpanel.revalidate();
				mainpanel.repaint();
				
				// TODO Auto-generated method stub


				
			}});
        mainpanel.add(deletebuild);
        mainpanel.add(new JLabel());mainpanel.add(new JLabel());
        mainpanel.add(new JLabel());
        JButton adduser=new JButton("Generate building report");
        adduser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			  {
				int allws=0;
				int empl=0;
				for (Floor f:b.floors.values()) {
					FloorPanel l=new FloorPanel(f,buildinglist,b);
					//l.init();
					allws+=f.ws.size();
					try {
						empl+=l.setColor().size();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				int avws=allws-empl;

				Document document = new Document(PageSize.A6);

				
				try {
					PdfWriter.getInstance(document, new FileOutputStream(b.name+".pdf"));
					document.open();
					Paragraph hello=new Paragraph("Building: "+ b.name);
					hello.add("\n");
					hello.add("Adress: "+b.address);
					hello.add("\n");
					hello.add("\n");
					hello.add("Report for Building: "+b.name);
					hello.add("\n");
					hello.add("\n");
					hello.add("\n");
					hello.add("\n");
					hello.add("Number of employees: " + Integer.toString(empl));
					hello.add("\n");
					hello.add("Number of worksations: "+ Integer.toString(allws));
					hello.add("\n");
					hello.add("Available worksations: "+ Integer.toString(avws));
					//PdfContentByte canvas = writer.getDirectContentUnder();
					Image image = Image.getInstance("src/menu.png");
					//image.scaleAbsolute(PageSize.A6.getBorderWidth(),10);
					//image.setAbsolutePosition(0, 0);
					image.scaleToFit(240, 150);
					document.add(image);
					document.add(hello);
					
					

				} catch (FileNotFoundException | DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				 
				document.close();
				
				
			  }
			});
        mainpanel.add(adduser);
        border.add(mainpanel, BorderLayout.CENTER);
        border.add(new JLabel(new ImageIcon(getClass().getResource("menu.png"))),BorderLayout.SOUTH);
        maintab.add(border);
        maintab.setSize(400,400);
        addTab("Building Menu", null, maintab,"Building " +b.name+" main page");
	}
	
    public boolean tabAlreadyOpen(String tabName) {

        for (int i = 0; i < this.getTabCount(); i++) {

            if (this.getTitleAt(i).equals(tabName)) {
                this.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }
    
    
	public void buildingtab(Floor f) {
		
		  FloorPanel buildingpanel=new FloorPanel(f,buildinglist,b);
		  buildingpanel.init();
	      JButton close=new JButton();        close.setIcon(x);        close.setBorder(null);        close.setFocusable(false);
	      this.addTab("Floor: "+f.name, null, buildingpanel,"Floor: "+f.name);
	      JPanel tab=new JPanel();
	      tab.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
	      tab.add(new JLabel("Floor: "+f.name));
	      tab.add(close);
	      this.indexOfComponent(buildingpanel);
	      this.setTabComponentAt(this.indexOfComponent(buildingpanel),tab);
	      close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				  {
					remove(buildingpanel);
				  }
				}
	       );
	        	
	}
	
	
	
	public JPanel deletePanel() {
        JPanel removeFloor=new JPanel();
        removeFloor.setLayout(new FlowLayout());
        removeFloor.add(new JLabel("Select Floor No. to remove:"));
        SpinnerModel sm;
        if(floors.getItemCount()==1) {
       	 sm = new SpinnerNumberModel(1,1,1,1);

        }else {
            sm = new SpinnerNumberModel(1, 1, floors.getItemCount()-1, 1); //default value,lower bound,upper bound,increment by

        }

        
        JSpinner spinner = new JSpinner(sm);

        removeFloor.add(spinner);
        JButton ok=new JButton("Ok");
        removeFloor.add(ok);
        if(floors.getItemCount()==1) {
        	spinner.setEnabled(false);
        	ok.setEnabled(false);
        }
        ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String s=(String) floors.getItemAt((int) spinner.getValue());
				s=s.substring(s.lastIndexOf("-") + 1);
				//System.out.println(s);
				b.floors.remove(s);
				mainpanel.remove(6);
		    	floors=new JComboBox<String>();
		    	floors.addItem("-Select Floor-");
		    	if (b.floors!=null) {
		    		for(String f:b.floors.keySet()) {
		    			floors.addItem(floors.getItemCount()+"-"+f);
		    		}
		    	}
				mainpanel.add(floors,null,6);
				mainpanel.revalidate();
				mainpanel.repaint();
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
				mainpanel.remove(6);
				mainpanel.add(floors,null,6);
				mainpanel.revalidate();
				mainpanel.repaint();
			}
        	
        });
        
        
        return removeFloor;
	}



}
