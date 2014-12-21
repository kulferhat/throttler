package kul.ferhat;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame{
	
	private final GUIActionListener actionListener;
	
	private final JTextField producerTPS = new JTextField("4");
	private final JTextField consumerTPS = new JTextField("3");
	private final JTextField consumerQueueSize = new JTextField("50");
	private final JTextField taskCount = new JTextField("10000");
	
	private final JTextArea logArea = new JTextArea(5, 50);
	
	private JButton startButton;
	private JButton stopButton;
	private Configuration conf = new Configuration();

	private static JFrame frame;
	
	private LogAdapter logAdapter;

	public GUI(GUIActionListener actionListener){
		this.actionListener = actionListener;
		frame = this;
	}
	
	public void setup(){

		setTitle("Java Throttling Demonstration");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(prepareConfPanel(), BorderLayout.CENTER);
		getContentPane().add(prepareLogPanel(), BorderLayout.EAST);
		
		this.logAdapter = new LogAdapter(logArea);
		
		//4. Size the frame.
		pack();

		//5. Show it.
		setVisible(true);
	}
	
	private JPanel prepareLogPanel() {
		JPanel logPanel = new JPanel(new BorderLayout());
		
		logPanel.add(logArea, BorderLayout.CENTER);
		
		JPanel metricsPanel = new JPanel();
		metricsPanel.setLayout(new GridLayout(2, 2));
		
		metricsPanel.add(new JLabel("X"));
		metricsPanel.add(new JLabel("X"));
		
		metricsPanel.add(new JLabel("X"));
		metricsPanel.add(new JLabel("X"));
		
		logPanel.add(metricsPanel, BorderLayout.SOUTH);
		
		return logPanel;
	}

	private JPanel prepareConfPanel(){
		JPanel confPanel = new JPanel();
		confPanel.setLayout(new GridLayout(5, 3));

		confPanel.add(new JLabel("Producer TPS"));
		confPanel.add(producerTPS);
		final JButton setProducerTPS = new JButton("Set");
		setProducerTPS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readConfiguration();
				actionListener.reloadProducerTps();
			}
		});
		confPanel.add(setProducerTPS);
		
		confPanel.add(new JLabel("Consumer TPS"));
		confPanel.add(consumerTPS);
		final JButton setConsumerTPS = new JButton("Set");
		setConsumerTPS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readConfiguration();
				actionListener.reloadConsumerTps();
			}
		});
		confPanel.add(setConsumerTPS);
		
		confPanel.add(new JLabel("Consumer Queue Size"));
		confPanel.add(consumerQueueSize);
		final JButton setConsumerQueueSize = new JButton("Set");
		setConsumerQueueSize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				readConfiguration();
				actionListener.reloadConsumerQueueSize();
			}
		});
		confPanel.add(setConsumerQueueSize);
		
		
		confPanel.add(new JLabel("TaskCount"));
		confPanel.add(taskCount);
		confPanel.add(new JLabel());
		
		
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				readConfiguration();
				actionListener.startClicked(conf, logAdapter);
			}
		});
		
		stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionListener.stopClicked();
			}
		});
		
		confPanel.add(startButton);
		confPanel.add(stopButton);
		confPanel.add(new JLabel());
		
		return confPanel;
	}
	
	private Configuration readConfiguration(){
		conf.setProducerTPS(Integer.parseInt(producerTPS.getText()));
		conf.setConsumerTPS(Integer.parseInt(consumerTPS.getText()));
		conf.setConsumerQueueSize(Integer.parseInt(consumerQueueSize.getText()));
		conf.setTaskCount(Integer.parseInt(taskCount.getText()));
		
		return conf;
	}
	
	public static void main(String[] args) {
		new GUI(null).setup();
	}
}
