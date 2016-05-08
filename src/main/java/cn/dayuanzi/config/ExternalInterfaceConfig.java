package cn.dayuanzi.config;

/**
 * 外部接口配置
 * 
 * @author : leihc
 * @date : 2015年4月14日 下午2:07:55
 * @version : 1.0
 */
public class ExternalInterfaceConfig implements IConfig {

	/**
	 * 短信接口URL
	 */
	public static String SMS_URL = "http://smsapi.c123.cn/OpenPlatform/OpenApi";
	/**
	 * 接口名
	 */
	public static String ACTION = "sendOnce";
	/**
	 * 短信验证平台用户账号
	 */
	public static String AC = "1001@501123550001";
	/**
	 * 认证秘钥
	 */
	public static String AUTH_KEY = "CE16FA36C510394C711BEAFB285B4042";
	/**
	 * 通道组编号
	 */
	public static String CG_ID = "52";
	/**
	 * 签名编号
	 */
	public static String CS_ID = "4222";
	
	/**
	 * @see cn.dayuanzi.config.IConfig#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see cn.dayuanzi.config.IConfig#reload()
	 */
	@Override
	public void reload() {
		// TODO Auto-generated method stub

	}

}
