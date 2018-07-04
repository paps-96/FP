import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

public class Floor implements Serializable {

	HashMap<String,Point> ws;
	String name;
	ImageIcon floorplan;
	
	public Floor(String name) {
		this.name=name;
		this.floorplan=new ImageIcon(getClass().getResource("noplan.gif"));
		ws=new HashMap<String,Point>();
	}
	
	public Floor(String name,ImageIcon flpl) {
		this.name=name;
		this.floorplan=flpl;
		ws=new HashMap<String,Point>();
	}
	
	
	
}
