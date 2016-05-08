package cn.dayuanzi.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import cn.dayuanzi.util.DateTimeUtil;
import cn.dayuanzi.util.YardUtils;

/**
 * 
 * @author : leihc
 * @date : 2015年4月13日 下午2:46:21
 * @version : 1.0
 */
public class Settings implements IConfig {

	private Logger logger = LoggerFactory.getLogger(Settings.class);
	/**
	 * 短信验证平台分配的KEY
	 */
	public static String SMS_NAME;
	/**
	 * 短信验证平台分配的密码
	 */
	public static String SMS_PW;
	/**
	 * 文件保存目录，最开始作为图片目录
	 */
	public static File IMAGES_DIRETORY;
	/**
	 * 意见反馈图片目录名称
	 */
	public static File BACK_IMAGES;
	/**
	 * 头像目录名称
	 */
	public static File HEAD_ICON_DIRE;
	/**
	 * 帖子图片目录名称
	 */
	public static File POST_IMAGE_DIRE;
	/**
	 * 邀约图片目录
	 */
	public static File INVITATION_IMAGE_DIRE;
	/**
	 * 商店图片
	 */
	public static File SHOP_IMAGE_DIRE;
	/**
	 * 邻豆规则图片
	 */
	public static File LINDOU_IMAGE_DIRE;
	/**
	 * 经验规则图片
	 */
	public static File EXP_IMAGE_DIRE;
	/**
	 * 活动的图片目录
	 */
	public static File ACTIVITY_IMAGE_DIRE;
	/**
	 * 一般图片保存目录，不用归类的图片
	 */
	public static File GENERAL_IMAGE_DIRE;
	/**
	 * 互助图片目录
	 */
//	public static File HELP_IMAGE_DIRE;
	/**
	 * 是否使用外部聊天平台
	 */
	public static boolean USE_INTERNAL_IM;
	
	/**
	 * 当前消息推送到APNS服务器，是沙箱模式（测试）
	 */
	public static boolean APNS_SANDBOX_MODE;
	/**
	 * APNS服务器证书密码
	 */
	public static String APNS_P12_PASSWORD;
	public static String APNS_P12_FILENAME;
	/**
	 * 白名单
	 */
	public static List<String> WHITE_LIST = new ArrayList<String>();
	public static boolean WHITE_LIST_SWITCH;
	public static int VALIDATECODE_LIMIT;
	/**
	 * 备注
	 */
	public static String WORD_MARK_BEANS;
	public static String WORD_MARK_EXP;
	public static String WORD_MARK_RANK;
	/**
	 * 地图认证时的距离范围设置，单位公里
	 */
	public static double MAP_VALIDATE_DISTANCE;
	/**
	 * 在这个时间后注册的用户发自我介绍
	 */
	public static long AFTER_REGISTER_TIME;
	/**
	 * 验证切换社区的周期时间，单位天
	 */
	public static int CHANGE_COURTYARD_CYCLE;
	/**
	 * 热帖条件，回复数达到该设置即为热帖，回复数=感谢数+一级回复数
	 */
	public static int HOT_POST_CONDITION;
	
	/**
	 * @see cn.dayuanzi.config.IConfig#init()
	 */
	@Override
	public void init() {
		// TODO Auto-generated method stub
		try{
			Properties config = new Properties();
			File file = YardUtils.getResourcesFile("settings.properties");
			config.load(new InputStreamReader(new FileInputStream(file)));
			SMS_NAME = config.getProperty("SMS_NAME").trim();
			SMS_PW = config.getProperty("SMS_PW").trim();
			USE_INTERNAL_IM = Boolean.parseBoolean(config.getProperty("USE_INTERNAL_IM"));
			APNS_P12_PASSWORD = config.getProperty("APNS_P12_PASSWORD");
			APNS_P12_FILENAME = config.getProperty("APNS_P12_FILENAME");
			APNS_SANDBOX_MODE = Boolean.parseBoolean(config.getProperty("APNS_SANDBOX_MODE"));
			WHITE_LIST_SWITCH = Boolean.parseBoolean(config.getProperty("WHITE_LIST_SWITCH"));
			WORD_MARK_BEANS = (config.getProperty("WORD_MARK_BEANS")+"").trim();
			WORD_MARK_EXP = (config.getProperty("WORD_MARK_EXP")+"").trim();
			WORD_MARK_RANK = (config.getProperty("WORD_MARK_RANK")+"").trim();
			VALIDATECODE_LIMIT = Integer.parseInt(config.getProperty("VALIDATECODE_LIMIT", "0"));
			String whiteList = config.getProperty("WHITE_LIST");
			if(StringUtils.isNotBlank(whiteList)){
				String[] tokens = whiteList.split("\\|");
				for(String tel : tokens){
					WHITE_LIST.add(tel.trim());
				}
			}
			MAP_VALIDATE_DISTANCE = Double.parseDouble(config.getProperty("MAP_VALIDATE_DISTANCE"));
			AFTER_REGISTER_TIME = DateTimeUtil.getTime("2015-08-03 15:44:00", "yyyy-MM-dd HH:mm:ss");
			CHANGE_COURTYARD_CYCLE = Integer.parseInt(config.getProperty("CHANGE_COURTYARD_CYCLE", "90"));
			HOT_POST_CONDITION = Integer.parseInt(config.getProperty("HOT_POST_CONDITION", "30"));
			
			imagesDirectoryInitial(config);

		}catch(Exception ex){
			ex.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 初始化图片保存路径
	 * 
	 * @param config
	 */
	private void imagesDirectoryInitial(Properties config){
		String imagesDire = config.getProperty("IMAGES_DIRETORY");
		String catalinaHome = System.getProperty("catalina.home");
		
		if(StringUtils.isBlank(imagesDire) && StringUtils.isBlank(catalinaHome)){
			logger.warn("图片保存目录未设置");
			return;
		}
		if(StringUtils.isBlank(imagesDire)){
			imagesDire = catalinaHome + File.separator + "webapps"+File.separator+"Image";
		}
		IMAGES_DIRETORY = new File(imagesDire);
		if(!IMAGES_DIRETORY.exists() || !IMAGES_DIRETORY.isDirectory()){
			IMAGES_DIRETORY.mkdirs();
		}
		
		HEAD_ICON_DIRE = new File(IMAGES_DIRETORY, "HeadIcons");
		if(!HEAD_ICON_DIRE.exists() || !HEAD_ICON_DIRE.isDirectory()){
			HEAD_ICON_DIRE.mkdir();
		}
		logger.info("头像保存目录："+ HEAD_ICON_DIRE.getAbsolutePath());
		
		POST_IMAGE_DIRE = new File(IMAGES_DIRETORY, "PostImage");
		if(!POST_IMAGE_DIRE.exists() || !POST_IMAGE_DIRE.isDirectory()){
			POST_IMAGE_DIRE.mkdir();
		}
		logger.info("帖子图片保存目录："+ POST_IMAGE_DIRE.getAbsolutePath());
		
		BACK_IMAGES = new File(IMAGES_DIRETORY,"BackImage");
		if(!BACK_IMAGES.exists()||!BACK_IMAGES.isDirectory()){
			BACK_IMAGES.mkdir();
		}
		logger.info("意见反馈保存目录："+BACK_IMAGES.getAbsolutePath());
		
		INVITATION_IMAGE_DIRE = new File(IMAGES_DIRETORY, "InvitationImage");
		if(!INVITATION_IMAGE_DIRE.exists() || !INVITATION_IMAGE_DIRE.isDirectory()){
			INVITATION_IMAGE_DIRE.mkdir();
		}
		logger.info("邀约图片保存目录："+ INVITATION_IMAGE_DIRE.getAbsolutePath());
		
		SHOP_IMAGE_DIRE = new File(IMAGES_DIRETORY,"ShopImage");
		if(!SHOP_IMAGE_DIRE.exists()||!SHOP_IMAGE_DIRE.isDirectory()){
			SHOP_IMAGE_DIRE.mkdir();
		}
		logger.info("商店图片保存目录："+SHOP_IMAGE_DIRE.getAbsolutePath());
		
		ACTIVITY_IMAGE_DIRE = new File(IMAGES_DIRETORY,"ActivityImage");
		if(!ACTIVITY_IMAGE_DIRE.exists()||!ACTIVITY_IMAGE_DIRE.isDirectory()){
			ACTIVITY_IMAGE_DIRE.mkdir();
		}
		logger.info("活动图片保存目录："+ACTIVITY_IMAGE_DIRE.getAbsolutePath());

		LINDOU_IMAGE_DIRE = new File (IMAGES_DIRETORY,"LindouImage");
		if(!LINDOU_IMAGE_DIRE.exists()||!LINDOU_IMAGE_DIRE.isDirectory()){
			LINDOU_IMAGE_DIRE.mkdir();
		}
		logger.info("邻豆规则图片保存目录："+LINDOU_IMAGE_DIRE.getAbsolutePath());
		
		EXP_IMAGE_DIRE = new File (IMAGES_DIRETORY,"ExpImage");
		if(!EXP_IMAGE_DIRE.exists()||!EXP_IMAGE_DIRE.isDirectory()){
			EXP_IMAGE_DIRE.mkdir();
		}
		logger.info("经验规则图片保存目录："+EXP_IMAGE_DIRE.getAbsolutePath());
		
		GENERAL_IMAGE_DIRE = new File (IMAGES_DIRETORY,"generalImg");
		if(!GENERAL_IMAGE_DIRE.exists()||!GENERAL_IMAGE_DIRE.isDirectory()){
			GENERAL_IMAGE_DIRE.mkdir();
		}
		logger.info("一般图片保存目录："+GENERAL_IMAGE_DIRE.getAbsolutePath());
	}
	/**
	 * @see cn.dayuanzi.config.IConfig#reload()
	 */
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		this.init();
	}

}
