package cn.dayuanzi.util;

/**
 * 存入redis中的key
 *
 * @author : leihc
 * @date : 2015年5月2日
 * 
 */
public class RedisKey {

	/**
	 * 根键
	 */
	public static String root = "COURTYARD";
	/**
	 * 兴趣
	 */
	public static String family_interest = "INTEREST";
	/**
	 * 消息未读数
	 */
	public static String msg_not_read = "MSG_COUNT";
	/**
	 * 手机验证码生成次数
	 */
	public static String tel = "TELCODE";
	/**
	 * 未读聊天消息数
	 */
	public static String not_read_chat = "CHAT";
	/**
	 * 未读回复数
	 */
	public static String not_read_reply = "REPLY";
	/**
	 * 未读感谢数
	 */
	public static String not_read_laud = "LAUD";
	/**
	 * 未读通知数
	 */
	public static String not_read_notice = "NOTICE";
	/**
	 * 未读好友请求数
	 */
	public static String not_read_follow = "FOLLOW";
	/**
	 * 显示帖子的范围
	 */
	public static String last_range = "RANGE";
	
	/**
	 * 
	 * @param family
	 * @param key
	 * @return COURTYARD#family#key
	 */
	public static String getKey(String family, String key){
		return root+"#" +family+"#"+key;
	}
}
