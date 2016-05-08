package cn.dayuanzi.util;

import java.util.Map;

import javolution.util.FastMap;

/**
 * 
 * @author : leihc
 * @date : 2015年6月2日
 * @version : 1.0
 */
public class AppleCountManager {

	private Map<Long, Integer> counter = new FastMap<Long, Integer>().shared();
	
	private Map<Long, Integer> add = new FastMap<Long, Integer>().shared();
	
	private Map<Long, Integer> sub = new FastMap<Long, Integer>().shared();
	
	private static final AppleCountManager instance = new AppleCountManager();
	
	private AppleCountManager(){
		
	}

	public static AppleCountManager getInstance() {
		return instance;
	}
	
	class Computer extends Thread{
		
		/**
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			
		}
	}
}
