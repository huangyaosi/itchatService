package com.enosh.itchatService.common.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import com.enosh.itchatService.config.BeanFactory;

public class ActiveQueue {
	private BlockingQueue<Object> queue;
	//With processor it is very extensible.
	private Processor processor;
	private ExecutorService executor;
	//With looper the consumer can run in a new thread.
	private QueueLooper looper = new QueueLooper();
	private boolean stoped;
	private int maxThreads;
	private int runningThreads;
	private final static int defaultQueueSize = 50;
	public ActiveQueue(Processor processor) {
		this(processor, 1);
	}
	public ActiveQueue(Processor processor, int maxThreads) {
		this.processor = processor;
		this.maxThreads = maxThreads;
		this.queue = new ArrayBlockingQueue<Object>(defaultQueueSize);
	}
	public void enqueue(Object o) {
		try {
			queue.put(o);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(runningThreads < maxThreads) {
			executor.submit(looper);
			synchronized(queue) {
				runningThreads++;
			}
			
		}

	}
	
	protected Object dequeue() {
		Object obj = null;
		try {
			obj = queue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public interface Processor{
		public void process(Object obj);
	}
	
	public void stop() {
		stoped = true;//TODO
	}
	
	public ExecutorService getExecutor() {
		return executor;
	}
	public void setExecutor(ExecutorService executor) {
		this.executor = executor;
	}

	protected class QueueLooper implements Runnable {
		@Override
		public void run() {
			Processor processor = ActiveQueue.this.processor;
			while(!stoped) {
				Object obj = dequeue();
				if(obj != null) {
					processor.process(obj);
				}
			}
		}
	}
	
	public static void main(String args[]) {
		BeanFactory factory = new BeanFactory();
		ActiveQueue queue = factory.getMessageQueue();
		for (int i = 0; i < 10000; i++) {
			queue.enqueue("obj " + i);
		}
	}
}
