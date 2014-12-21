package kul.ferhat;

public class Configuration {
	private int producerTPS;
	private int consumerTPS;
	private int consumerQueueSize;
	private int taskCount;
	
	public int getProducerTPS() {
		return producerTPS;
	}
	public void setProducerTPS(int producerTPS) {
		this.producerTPS = producerTPS;
	}
	public int getConsumerTPS() {
		return consumerTPS;
	}
	public void setConsumerTPS(int consumerTPS) {
		this.consumerTPS = consumerTPS;
	}
	public int getConsumerQueueSize() {
		return consumerQueueSize;
	}
	public void setConsumerQueueSize(int consumerQueueSize) {
		this.consumerQueueSize = consumerQueueSize;
	}
	public int getTaskCount() {
		return taskCount;
	}
	public void setTaskCount(int taskCount) {
		this.taskCount = taskCount;
	}
	
	
}
