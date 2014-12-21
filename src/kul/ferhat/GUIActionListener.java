package kul.ferhat;

public class GUIActionListener {
	private Engine machine = null;
	public GUIActionListener(){
		
	}

	public void startClicked(Configuration conf, LogAdapter logAdapter) {
		machine = new Engine(conf, logAdapter);
		new Thread(){
			public void run(){
				machine.start();
			}
		}.start();
	}

	public void stopClicked() {
		machine.stop();
	}

	public void reloadProducerTps() {
		if (machine != null){
			machine.reloadProducerTps();
		}
	}
	
	public void reloadConsumerTps() {
		if (machine != null){
			machine.reloadConsumerTps();
		}
	}
	
	public void reloadConsumerQueueSize() {
		if (machine != null){
			machine.reloadConsumerQueueSize();
		}
	}
}
