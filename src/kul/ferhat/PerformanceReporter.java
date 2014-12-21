package kul.ferhat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class PerformanceReporter {

	private Throttler throttler;
	private LogAdapter logAdapter;

	public PerformanceReporter(Throttler throttler, LogAdapter logAdapter) {
		this.throttler = throttler;
		this.logAdapter = logAdapter;
	}

	public void start() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try{
					int waitingInQueue = throttler.getWaitingCounInQueue();
					int queueSizeLimit = throttler.getConsumerQueueSizeLimit();
					String log = String
							.format("Waiting in queue: %d / %d",
									waitingInQueue, 
									queueSizeLimit);
					
					if (waitingInQueue > queueSizeLimit * (0.75)){
						log = log + 
								"\nQueue Gettting Full, Do one of followings \n " + ""
										+ "1.) Decrease producer TPS, or \n"
										+ "2.) Increase consumer TPS, or \n"
										+ "3.) Increase Queue Size, to avoid rejection of new tasks.";
					}
					
					logAdapter.log(log);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}, 1000, 1000);
	}

}
