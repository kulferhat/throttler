package kul.ferhat;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.common.util.concurrent.RateLimiter;

public class Throttler {
	private final BlockingQueue<Runnable> queue;
	private ExecutorService consumer;
	private int tps;
	private RateLimiter rateLimiter;
	private int queueSize;
	private static final int MAX_QUEUE_SIZE = 100000;

	public Throttler(int tps, int queueSize, final ExecutorService consumer) {
		if (queueSize > MAX_QUEUE_SIZE){
			throw new RuntimeException("Queue Size can not be greater than " + MAX_QUEUE_SIZE);
		}
		
		this.queueSize = queueSize;
		this.consumer = consumer;
		this.tps = tps;
		this.queue = new LinkedBlockingQueue<Runnable>(MAX_QUEUE_SIZE);
		this.rateLimiter = RateLimiter.create(tps);
		
		prepareConsumerThread();
	}

	private void prepareConsumerThread() {
		Thread consumerThread = new Thread() {
			public void run() {
				try {
					while (true) {
						rateLimiter.acquire();
						Runnable task = queue.take();
						consumer.submit(task);
					}
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		};

		consumerThread.start();
	}

	public void put(Runnable task) throws InterruptedException, ThrootlerQueueFullException {
		if (queue.size() >= this.queueSize){
			throw new ThrootlerQueueFullException("Throttler Queue is full");
		}
		queue.put(task);
		System.out.println(queue.size());
	}
	
	public void reloadConsumerTps(int tps) {
		rateLimiter.setRate(tps);
	}
	
	public void reloadConsumerQueueSize(int queueSize) {
		if (queueSize > MAX_QUEUE_SIZE){
			throw new RuntimeException("Queue Size can not be greater than " + MAX_QUEUE_SIZE);
		}
		
		this.queueSize = queueSize;
	}
	
	public int getWaitingCounInQueue(){
		return queue.size();
	}
	
	public int getConsumerQueueSizeLimit(){
		return queueSize;
	}

	public static void main(String[] args) throws InterruptedException {
		ExecutorService es = Executors.newFixedThreadPool(10);
		Throttler t = new Throttler(10, 100, es);
		for (int i = 0; i < 10000; i++){
			try{
				t.put(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("Running " + Thread.currentThread().getName());
					}
				});
			}
			catch(ThrootlerQueueFullException e){
				e.printStackTrace();
			}
		}
	}
}
