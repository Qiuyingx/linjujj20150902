package cn.general.test;

import java.util.HashMap;
import java.util.Map;

import org.apache.catalina.session.StandardManager;
import org.apache.commons.codec.digest.DigestUtils;

import cn.dayuanzi.config.LevelUpConfig;
import cn.dayuanzi.config.LevelUpInfo;

/**
 * 
 * @author : leihc
 * @date : 2015年6月3日
 * @version : 1.0
 */
public class Gtest {

	public static int level = 0;
	public static int exp = 0;
	public static Map<Integer, LevelUpInfo> datas = new HashMap<Integer, LevelUpInfo>();
	
	public static void main(String[] args) {
		String data = "/kdsjf/df";
		data = data.substring(1);
		System.out.println(data);
	}
	
	public static boolean addExp(int amount){
		boolean isLevelUp = false;
		LevelUpInfo info = datas.get(level+1);
		int finalExp = exp+amount;
		while(finalExp >= info.getExp()){
			level++;
			finalExp = finalExp - info.getExp();
			isLevelUp = true;
			info = datas.get(level+1);
			if(info==null){
//				finalExp = datas.get(level).getExp();
				break;
			}
		}
		exp = finalExp;
		
		return isLevelUp;
	}
}
