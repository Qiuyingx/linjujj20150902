package cn.dayuanzi.util.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author : leihc
 * @date : 2015年6月4日
 * @version : 1.0
 */
public class ThreadPoolManager {

	public final static Logger logger = LoggerFactory.getLogger(ThreadPoolManager.class);
	
	private final ThreadPoolExecutor generalExecutor;
	
	ThreadPoolManager(){
		generalExecutor = new ThreadPoolExecutor(8, 8 + 2, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new PriorityThreadFactory("General Pool", Thread.NORM_PRIORITY));
	}
	
	private class PriorityThreadFactory implements ThreadFactory{

		private final int priority;
        private final String name;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final ThreadGroup group;
		
        public PriorityThreadFactory(String name, int priority){
        	this.priority = priority;
        	this.name = name;
        	this.group = new ThreadGroup(name);
        }
        
		/**
		 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
		 */
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, name + "-" + threadNumber.getAndIncrement());
            t.setPriority(this.priority);
            return t;
		}
		
	}
}
