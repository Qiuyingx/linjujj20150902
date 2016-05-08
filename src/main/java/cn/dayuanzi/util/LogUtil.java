package cn.dayuanzi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dayuanzi.pojo.ExternalType;

/**
 * 
 * @author : lhc
 * @date : 2015年4月10日 下午5:47:44
 * @version : 1.0
 */
public class LogUtil {
	/**
	 * 错误日志
	 */
	private static final Logger exceptionLog = LoggerFactory.getLogger("dayuanzi.exceptionLog");
	/**
	 * 注册日志
	 */
	private static final Logger registerLog = LoggerFactory.getLogger("dayuanzi.registerLog");
	/**
	 * 登陆日志
	 */
	private static final Logger loginLog = LoggerFactory.getLogger("dayuanzi.loginLog");
	
	/**
	 * 记录错误日志
	 * 
	 * @param message
	 */
	public static void logException(String message, Exception ex){
		exceptionLog.debug(message, ex);
	}
	
	public static void logRegister(long userId, ExternalType externalType, int platform, String token,Long inviteCode){
		StringBuilder sb = new StringBuilder();
		sb.append(userId).append("|");
		sb.append(externalType.ordinal()).append("|");
		sb.append(platform).append("|");
		sb.append(token).append("|");
		sb.append(inviteCode==null?0:inviteCode).append("|");
		registerLog.debug(sb.toString());
	}
	public static void logLogin(long courtyardId, long userId, ExternalType externalType, int platform, String token){
		StringBuilder sb = new StringBuilder();
		sb.append(courtyardId).append("|");
		sb.append(userId).append("|");
		sb.append(externalType.ordinal()).append("|");
		sb.append(platform).append("|");
		sb.append(token).append("|");
		loginLog.debug(sb.toString());
	}
}
