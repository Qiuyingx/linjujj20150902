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
 * @date : 2015年4月13日 上午9:13:39
 * @version : 1.0
 */
public class SysMessages implements IConfig {

	public static String TEL_IS_NOT_BLANK = "手机号码不能为空";
	public static String PW_IS_NOT_BLANK = "密码不能为空";
	public static String USER_NOT_EXIST = "用户不存在";
	public static String PW_WRONG= "用户名或者密码不正确";
	
	/**
	 * @see cn.dayuanzi.config.IConfig#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stubtry {
		try{
			Properties config = new Properties();
			File file = YardUtils.getResourcesFile("sysmessages_cn.properties");
			config.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			Field[] fields = SysMessages.class.getFields();
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				if (StringUtils.isNotBlank(config.getProperty(fieldName))) {
					fields[i].set(this, config.getProperty(fieldName));
				}
			}
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
		this.reload();
	}

}
