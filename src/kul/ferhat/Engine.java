package kul.ferhat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.RateLimiter;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class Engine {
	private ExecutorService consumer;
	private Throttler throttler;
	private Configuration conf;
	private boolean stopFlagSet = false;
	private RateLimiter producerRateLimiter;
	private LogAdapter logAdapter;
	
	public Engine(Configuration conf, LogAdapter logAdapter){
		this.conf = conf;
		this.logAdapter = logAdapter;
		setup();
	}
	
	private void setup() {
		this.consumer = Executors.newFixedThreadPool(10);
		this.throttler = new Throttler(conf.getConsumerTPS(), 
				conf.getConsumerQueueSize(), consumer);
		prepareReporter();
	}
	
	private void prepareReporter() {
		PerformanceReporter reporter = new PerformanceReporter(throttler, logAdapter);
		reporter.start();
	}
	
	public void start(){
		int tps = conf.getProducerTPS();
		producerRateLimiter = RateLimiter.create(tps);

		long taskCount = conf.getTaskCount();
		
		for (int i = 0; i < taskCount && !stopFlagSet; i++){
			producerRateLimiter.acquire();
			try{
				this.throttler.put(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("Task is consumed by " + Thread.currentThread().getName());
					}
				});
			}
			catch(ThrootlerQueueFullException e){
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop(){
		this.stopFlagSet  = true;
	}
	
	public void reloadProducerTps() {
		if (producerRateLimiter != null){
			producerRateLimiter.setRate(conf.getProducerTPS());
		}
	}
	public void reloadConsumerTps() {
		if (throttler != null){
			throttler.reloadConsumerTps(conf.getConsumerTPS());
		}
	}
	
	public void reloadConsumerQueueSize() {
		if (throttler != null){
			throttler.reloadConsumerQueueSize(conf.getConsumerQueueSize());
		}
	}
}
