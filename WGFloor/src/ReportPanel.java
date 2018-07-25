import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportPanel extends JPanel {

	HashMap<String, Building> buildinglist=new HashMap<String, Building>();

	
	public ReportPanel(HashMap<String, Building> buildinglist) {
		this.buildinglist=buildinglist;
	}
	
	public void init() {
		this.setLayout(new BorderLayout());
		JTextArea reportarea=new JTextArea("Welcome to Report Generator");
		reportarea.setEditable(false);
		this.add(reportarea,BorderLayout.CENTER);
		JPanel main=new JPanel();
		
		main.setLayout(new GridLayout(4,1));
		this.add(main,BorderLayout.SOUTH);	
		JPanel lists=new JPanel();
		lists.setLayout(new GridLayout(1,2));
		main.add(lists);

		JComboBox<String> buildings=new JComboBox<String>();
		JComboBox<String> floors=new JComboBox<String>();
    	buildings.addItem("-Select Building-");
    	floors.addItem("-Select Floor-");
    	floors.setEnabled(false);
    	if (!buildinglist.isEmpty()) {
    		for(String b:buildinglist.keySet()) {
    			buildings.addItem(buildings.getItemCount()+"-"+b);
    		}
    	}
    	lists.add(buildings);
    	lists.add(floors);
    	buildings.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
            	if (!(buildings.getSelectedIndex()==0)) {
					floors.setEnabled(true);
					String s=(String) buildings.getSelectedItem();
					s=s.substring(s.lastIndexOf("-") + 1);
					Building b=buildinglist.get(s);
					floors.removeAllItems();
			    	floors.addItem("-Select Floor-");
			    	if (b.floors!=null) {
			    		for(String f:b.floors.keySet()) {
			    			floors.addItem(floors.getItemCount()+"-"+f);
			    		}
			    	}
				}else {
					floors.setEnabled(false);
				}
            }
        });
    	
    	JCheckBox dep=new JCheckBox("Show department details");
    	JCheckBox people = new JCheckBox("List people");
    	JPanel boxes=new JPanel();boxes.setLayout(new FlowLayout());
    	boxes.add(dep);boxes.add(people);
    	main.add(boxes);
    	JCheckBox male=new JCheckBox("Male");
    	male.setSelected(true);
    	JCheckBox female=new JCheckBox("Female");
    	female.setSelected(true);
    	JPanel gender=new JPanel();
    	gender.setLayout(new FlowLayout());
    	gender.add(male);
    	gender.add(female);
    	main.add(gender);
    	JPanel buttons=new JPanel();
    	buttons.setLayout(new FlowLayout());
    	JButton generate=new JButton("Generat Report");
    	JButton save = new JButton("Save Report");
    	JTextField name=new JTextField(12);
    	buttons.add(generate);
    	buttons.add(new JLabel("Report Name:"));
    	buttons.add(name);
    	buttons.add(save);
    	main.add(buttons);
    	
    	
        generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int total=0;
				int hr=0;int rec=0; int gym=0;int af=0;
				ArrayList<String> p=new ArrayList<String>();
				reportarea.setText(null);;
				Reader in = null;
				try {
					in = new FileReader("db.csv");
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Iterable<CSVRecord> records = null;
				try {
					records = CSVFormat.EXCEL.parse(in);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				

				
				if (!(buildings.getSelectedIndex()==0)) {
					String s=(String) buildings.getSelectedItem();
					s=s.substring(s.lastIndexOf("-") + 1);
					Building b=(buildinglist.get(s));
					reportarea.append("Report for Building: "+ b.name+"\n"+"Address: "+b.address+"\n\n");
					if (!(floors.getSelectedIndex()==0)) {
						s=(String) floors.getSelectedItem();
						s=s.substring(s.lastIndexOf("-") + 1);
						Floor f=b.floors.get(s);
						reportarea.append("Floor: "+ f.name+"\n\n");
						for (CSVRecord record : records) {
							if(record.get(1).equals(b.name)&&record.get(2).equals(f.name)) {
								if((male.isSelected() && female.isSelected())||(!male.isSelected() && !female.isSelected())) {
									total++;
									if (record.get(4).equals("HR")){
										hr++;
									}else if(record.get(4).equals("Receptionist")) {
										rec++;
									}else if(record.get(4).equals("A&F")) {
										af++;
									}else if(record.get(4).equals("Gym")) {
										gym++;
									}
									p.add(record.get(0));
								}else if(male.isSelected()&& !female.isSelected()&&record.get(5).equals("male")) {
									
									total++;
									if (record.get(4).equals("HR")){
										hr++;
									}else if(record.get(4).equals("Receptionist")) {
										rec++;
									}else if(record.get(4).equals("A&F")) {
										af++;
									}else if(record.get(4).equals("Gym")) {
										gym++;
									}
									p.add(record.get(0));
								}else if(male.isSelected()&& !female.isSelected()&&record.get(5).equals("female")) {
									
									total++;
									if (record.get(4).equals("HR")){
										hr++;
									}else if(record.get(4).equals("Receptionist")) {
										rec++;
									}else if(record.get(4).equals("A&F")) {
										af++;
									}else if(record.get(4).equals("Gym")) {
										gym++;
									}
									p.add(record.get(0));
								}
								
							}
						}

					}else {
						for (CSVRecord record : records) {
							if(record.get(1).equals(b.name)) {
								if((male.isSelected() && female.isSelected())||(!male.isSelected() && !female.isSelected())) {
									total++;
									if (record.get(4).equals("HR")){
										hr++;
									}else if(record.get(4).equals("Receptionist")) {
										rec++;
									}else if(record.get(4).equals("A&F")) {
										af++;
									}else if(record.get(4).equals("Gym")) {
										gym++;
									}
									p.add(record.get(0));
								}else if(male.isSelected()&& !female.isSelected()&&record.get(5).equals("male")) {
									
									total++;
									if (record.get(4).equals("HR")){
										hr++;
									}else if(record.get(4).equals("Receptionist")) {
										rec++;
									}else if(record.get(4).equals("A&F")) {
										af++;
									}else if(record.get(4).equals("Gym")) {
										gym++;
									}
									p.add(record.get(0));
								}else if(male.isSelected()&& !female.isSelected()&&record.get(5).equals("female")) {
									
									total++;
									if (record.get(4).equals("HR")){
										hr++;
									}else if(record.get(4).equals("Receptionist")) {
										rec++;
									}else if(record.get(4).equals("A&F")) {
										af++;
									}else if(record.get(4).equals("Gym")) {
										gym++;
									}
									p.add(record.get(0));
								}
							}
						}

					}

				}else {
					for (CSVRecord record : records) {
						if((male.isSelected() && female.isSelected())||(!male.isSelected() && !female.isSelected())) {
							total++;
							if (record.get(4).equals("HR")){
								hr++;
							}else if(record.get(4).equals("Receptionist")) {
								rec++;
							}else if(record.get(4).equals("A&F")) {
								af++;
							}else if(record.get(4).equals("Gym")) {
								gym++;
							}
							p.add(record.get(0));
						}else if(male.isSelected()&& !female.isSelected()&&record.get(5).equals("male")) {
							
							total++;
							if (record.get(4).equals("HR")){
								hr++;
							}else if(record.get(4).equals("Receptionist")) {
								rec++;
							}else if(record.get(4).equals("A&F")) {
								af++;
							}else if(record.get(4).equals("Gym")) {
								gym++;
							}
							p.add(record.get(0));
						}else if(male.isSelected()&& !female.isSelected()&&record.get(5).equals("female")) {
							
							total++;
							if (record.get(4).equals("HR")){
								hr++;
							}else if(record.get(4).equals("Receptionist")) {
								rec++;
							}else if(record.get(4).equals("A&F")) {
								af++;
							}else if(record.get(4).equals("Gym")) {
								gym++;
							}
							p.add(record.get(0));
						}
					}
					total--;
					if (p.size()>0) {
						p.remove(0);

					}
				}
				
				reportarea.append("Total people: "+total+"\n\n");
				if (dep.isSelected()) {
					reportarea.append("HR: "+hr+"\nReceptionist: "+rec+"\nA&F: "+af+"\nGym: "+gym+"\n\n");
				}
				if (people.isSelected()) {
					reportarea.append("List of people: \n\n");
					for (String person:p) {
						reportarea.append(person+"\n");
					}
				}

			}
        });


        save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				Document document = new Document(PageSize.A6);

				
				try {
					PdfWriter.getInstance(document, new FileOutputStream(name.getText()+".pdf"));
					document.open();
					Paragraph hello=new Paragraph(reportarea.getText());

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
		
	}
}
