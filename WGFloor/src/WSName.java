import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class WSName extends JFrame {

	Point p;
	HashMap<String,Point> temp;
	JPanel floorplanarea;
	HashMap<String, Building> buildinglist;
	
	public WSName(Point p,HashMap<String,Point> temp,JPanel floorplanarea,HashMap<String, Building> buildinglist) {
		super("Name of Worwstation");
		ArrayList<Image> icons = new ArrayList<Image>();
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
		icons.add(new ImageIcon(getClass().getResource("wg.png")).getImage().getScaledInstance(45, 45, Image.SCALE_DEFAULT));
		setIconImages(icons);
		setVisible(true);
		setSize(300,60);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        this.p=p;
        this.temp=temp;
        this.floorplanarea=floorplanarea;
        this.buildinglist=buildinglist;
	}
	
	public void init() {
		JPanel main=new JPanel();
		setContentPane(main);
		main.setLayout(new GridLayout(1,2));
		JPanel buttons=new JPanel();buttons.setLayout(new FlowLayout());
		JTextField name=new JTextField();
		JButton done=new JButton("Add");
		JButton cancel=new JButton("Cancel");
		main.add(name);buttons.add(done);buttons.add(cancel);main.add(buttons);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				temp.put(name.getText(), p);
				floorplanarea.repaint();
				dispose();
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
			
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				dispose();
			}
			
		});
	}
}
