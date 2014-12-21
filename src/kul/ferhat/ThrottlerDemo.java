package kul.ferhat;

import kul.ferhat.GUI;
import kul.ferhat.GUIActionListener;

public class ThrottlerDemo {
	public static void main(String[] args) {
		GUIActionListener actionListener = new GUIActionListener();
		GUI gui = new GUI(actionListener);
		gui.setup();
		
	}
}
