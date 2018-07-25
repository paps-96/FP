import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

//Main Menu Frame
class MainMenu extends JFrame {
	
	ImageIcon x = new ImageIcon(getClass().getResource("closeTab.gif"));
	HashMap<String, User> userlist;
	HashMap<String, Building> buildinglist=new HashMap<String, Building>();
    JPanel mainpanel=new JPanel();
	JComboBox<String> buildings=new JComboBox<String>();
	JTabbedPane tabPane = new JTabbedPane();
	
	public MainMenu(HashMap<String, User> userlist) {
		super("Floor Plan Demo");
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		setIconImages(icons);
		setVisible(true);
        setSize(720, 600);
        setMinimumSize(new Dimension(720, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.userlist=userlist;
        if (new File("buildinglist.ser").exists())
        {
        
        try
	      {
	         FileInputStream fis = new FileInputStream("buildinglist.ser");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         this.buildinglist = (HashMap<String, Building>) ois.readObject();
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
	      }	}
	}
	
	public void init() {
		
        setContentPane(tabPane);
        JPanel maintab=new JPanel();
        maintab.setLayout(new GridLayout(1,1));
        JPanel border=new JPanel();
        border.setLayout(new BorderLayout());
        JLabel title=new JLabel("Welcome to the demo version",SwingConstants.CENTER);
        title.setFont(new Font(title.getFont().toString(), Font.PLAIN, 22));
        border.add(title,BorderLayout.NORTH);
        mainpanel.setLayout(new GridLayout(7,2));
        mainpanel.add(new JLabel());
        JButton showbuilding=new JButton("Show Building");
        mainpanel.add(showbuilding);
        showbuilding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!(buildings.getSelectedIndex()==0)) {
					String s=(String) buildings.getSelectedItem();
					s=s.substring(s.lastIndexOf("-") + 1);
					//System.out.println(!tabAlreadyOpen("Building: "+s));
					if(!tabAlreadyOpen("Building: "+s)) {
						buildingtab(buildinglist.get(s));
					}
				}
			}
        });
        mainpanel.add(new JLabel());mainpanel.add(new JLabel());
        mainpanel.add(new JLabel());
    	buildings.addItem("-Select Building-");
    	if (!buildinglist.isEmpty()) {
    		for(String b:buildinglist.keySet()) {
    			buildings.addItem(buildings.getItemCount()+"-"+b);
    		}
    	}


        JButton addbuilding =new JButton("Add New Building");
        mainpanel.add(addbuilding);
        addbuilding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AddBuilding m=new AddBuilding(buildinglist,buildings);
				m.init();
			}
        });
        mainpanel.add(buildings);mainpanel.add(new JLabel());
        mainpanel.add(new JLabel());
        JButton deletebuild=new JButton("Delete Building");
        deletebuild.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mainpanel.remove(6);
				//System.out.println(buildings.getItemCount()-1);
				mainpanel.add(deletePanel(),null,6);
				mainpanel.revalidate();
				mainpanel.repaint();
			}});
        mainpanel.add(deletebuild);
        mainpanel.add(new JLabel());mainpanel.add(new JLabel());
        JButton report=new JButton("Report Tab");
        mainpanel.add(report);
        report.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!tabAlreadyOpen("Report")) {
					reporttab();
				}
			}
        });
        
        JButton adduser=new JButton("Add New User");
        adduser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			  {
				//setEnabled(false);
				AddUser m=new AddUser(userlist);
				m.init();
			  }
			});
        mainpanel.add(adduser);
        border.add(mainpanel, BorderLayout.CENTER);
        border.add(new JLabel(new ImageIcon(getClass().getResource("menu.png"))),BorderLayout.SOUTH);
        maintab.add(border);
        maintab.setSize(400,400);
        tabPane.addTab("Main Menu", null, maintab,"Main Menu");
        

        
	}
	
	public JPanel deletePanel() {
        JPanel removeBuilding=new JPanel();
        removeBuilding.setLayout(new FlowLayout());
        removeBuilding.add(new JLabel("Select Building No. to remove:"));
        SpinnerModel sm;
        if(buildings.getItemCount()==1) {
       	 sm = new SpinnerNumberModel(1,1,1,1);

        }else {
            sm = new SpinnerNumberModel(1, 1, buildings.getItemCount()-1, 1); //default value,lower bound,upper bound,increment by

        }        JSpinner spinner = new JSpinner(sm);
        removeBuilding.add(spinner);
        JButton ok=new JButton("Ok");
        removeBuilding.add(ok);
        if(buildings.getItemCount()==1) {
        	spinner.setEnabled(false);
        	ok.setEnabled(false);
        }
        ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String s=(String) buildings.getItemAt((int) spinner.getValue());
				s=s.substring(s.lastIndexOf("-") + 1);
				//System.out.println(s);
				buildinglist.remove(s);
				mainpanel.remove(6);
		    	buildings=new JComboBox<String>();
		    	buildings.addItem("-Select Building-");
				for(String b:buildinglist.keySet()) {
					buildings.addItem(buildings.getItemCount()+"-"+b);
				}
				mainpanel.add(buildings,null,6);
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
        removeBuilding.add(cancel);
        cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				mainpanel.remove(6);
				mainpanel.add(buildings,null,6);
				mainpanel.revalidate();
				mainpanel.repaint();
			}
        	
        });
        
        
        return removeBuilding;
	}



	public void buildingtab(Building b) {
		
		  BuildingPanel buildingpanel=new BuildingPanel(b,buildinglist);
		  buildingpanel.init();
	      JButton close=new JButton();        close.setIcon(x);        close.setBorder(null);        close.setFocusable(false);
	      tabPane.addTab("Building: "+b.name, null, buildingpanel,"Building: "+b.name);
	      JPanel tab=new JPanel();
	      tab.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
	      tab.add(new JLabel("Building: "+b.name));
	      tab.add(close);
	      tabPane.indexOfComponent(buildingpanel);
	      tabPane.setTabComponentAt(tabPane.indexOfComponent(buildingpanel),tab);
	      close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				  {
					tabPane.remove(buildingpanel);
				  }
				}
	       );
	        	
	}
	
	public void reporttab() {
		
		  ReportPanel reportpanel=new ReportPanel(buildinglist);
		  reportpanel.init();
	      JButton close=new JButton();        close.setIcon(x);        close.setBorder(null);        close.setFocusable(false);
	      tabPane.addTab("Report", null, reportpanel,"Report");
	      JPanel tab=new JPanel();
	      tab.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
	      tab.add(new JLabel("Report"));
	      tab.add(close);
	      tabPane.indexOfComponent(reportpanel);
	      tabPane.setTabComponentAt(tabPane.indexOfComponent(reportpanel),tab);
	      close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
				  {
					tabPane.remove(reportpanel);
				  }
				}
	       );
	        	
	}

    public boolean tabAlreadyOpen(String tabName) {

        for (int i = 0; i < tabPane.getTabCount(); i++) {

            if (tabPane.getTitleAt(i).equals(tabName)) {
                tabPane.setSelectedIndex(i);
                return true;
            }
        }
        return false;
    }
	
	
}


