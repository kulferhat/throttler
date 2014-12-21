package kul.ferhat;

import javax.swing.JTextArea;

public class LogAdapter {
	private JTextArea logArea;

	public LogAdapter(JTextArea logArea){
		this.logArea = logArea;
	}

	public void log(String log) {
		logArea.setText(log);
	}
}
