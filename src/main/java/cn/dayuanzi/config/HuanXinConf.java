package cn.dayuanzi.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import cn.dayuanzi.util.YardUtils;

/**
 * 
 * @author : leihc
 * @date : 2015年4月26日 下午3:40:52
 * @version : 1.0
 */
public class HuanXinConf implements IConfig {

	/**
	 * 环信接口的URL
	 */
	public static String HUANXIN_URI = "https://a1.easemob.com";
	/**
	 * 在环信注册的公司名称
	 */
	public static String ORG_NAME = "linju";
	/**
	 * 在环信注册的APP名称
	 */
	public static String APP_NAME = "linjudev";
	public static String BASE_URI ;
	/**
	 * 环信分配的客户端ID
	 */
	public static String CLIENT_ID = "YXA6AOhAcAEVEeWyhFWlw2tMtg";
	/**
	 * 环信分配的秘钥
	 */
	public static String CLIENT_SECRET = "YXA6_Ddn1KBpdVFm6hiKW1CdOai6jok";
	
	/**
	 * @see cn.dayuanzi.config.IConfig#init()
	 */
	@Override
	public void init() {
		try{
			Properties config = new Properties();
			File file = YardUtils.getResourcesFile("chatconfig.properties");
			config.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			Field[] fields = HuanXinConf.class.getFields();
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				if (StringUtils.isNotBlank(config.getProperty(fieldName))) {
					fields[i].set(this, config.getProperty(fieldName));
				}
			}
			BASE_URI = HUANXIN_URI + "/" + ORG_NAME + "/" + APP_NAME +"/";
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @see cn.dayuanzi.config.IConfig#reload()
	 */
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		this.init();
	}
	
	public static String getUrl(String path){
		if(StringUtils.isBlank(path)){
			return BASE_URI;
		}
		return BASE_URI+path;
	}

	public String getTokenUrl(){
		return null;
	}
}
