package com.hoosteen.maps;

import javax.swing.JFrame;

public class MapFrame extends JFrame{

	
	public MapFrame(){
		
	}
	
	public void init(){
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(new MapComp("19 Iroquois Tr, Branchburg NJ, 08876"));
	//	add(new MapComp(40,-120));
		setVisible(true);
	}
}
