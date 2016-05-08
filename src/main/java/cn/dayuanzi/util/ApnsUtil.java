package cn.dayuanzi.util;

import java.io.File;







import cn.dayuanzi.config.Settings;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.User;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.PushType;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.PayloadBuilder;

/**
 * 
 * @author : leihc
 * @date : 2015年5月25日
 * @version : 1.0
 */
public class ApnsUtil {

	private static final ApnsUtil instance = new ApnsUtil();
	
	private ApnsService apnsService;
	
	
	private ApnsUtil(){
		File p12 = YardUtils.getResourcesFile(Settings.APNS_P12_FILENAME);
		ApnsServiceBuilder serviceBuilder = APNS.newService().withCert(p12.getPath(), Settings.APNS_P12_PASSWORD);
		serviceBuilder.withAppleDestination(Settings.APNS_SANDBOX_MODE);
		serviceBuilder.asQueued();
		apnsService = serviceBuilder.build();
	}

	public static ApnsUtil getInstance() {
		return instance;
	}

	public ApnsService getApnsService() {
		return apnsService;
	}
	/**
	 * 推送内容
	 * 
	 * @param target
	 * @param content
	 */
	public void send(User target, String content){
		if(target.isValidIOSUser()){
			String payload = APNS.newPayload().alertBody(content).build();
			apnsService.push(target.getLast_login_token(), payload);
		}
//		this.send(target);
	}
	
	/**
	 * 推送icon计数
	 * @param target
	 * @param badge
	 */
	public void send(User target, int badge){
		if(badge>0&&target.isValidIOSUser()){
			String payload = APNS.newPayload().badge(badge).build();
			apnsService.push(target.getLast_login_token(), payload);
		}
	}
	
	/**
	 * 推送内容和icon计数
	 * @param target
	 * @param noticeType
	 * @param content
	 * @param badge
	 */
	public void send(User target, PushType type, String content, long append, int badge){
		if(target.isValidIOSUser()){
			if(type==PushType.通知){
				throw new GeneralLogicException("参数错误");
			}
			PayloadBuilder payloadBuilder = APNS.newPayload();
			payloadBuilder.customField("pushType", type.ordinal());
			if(append > 0){
				payloadBuilder.customField("append", append);
			}
			payloadBuilder.alertBody(content);
			if (badge > 0) {
				payloadBuilder.badge(badge);
			}
			apnsService.push(target.getLast_login_token(), payloadBuilder.build());
		}
	}
	
	/**
	 * 推送类型为通知
	 * @param target
	 * @param type
	 * @param content
	 * @param append 活动，新闻，专题的数据库ID
	 * @param badge
	 */
	public void send(User target, NoticeType type, String content, int badge){
		if(target.isValidIOSUser()){
			PayloadBuilder payloadBuilder = APNS.newPayload();
			payloadBuilder.customField("pushType", PushType.通知.ordinal());
			payloadBuilder.customField("noticeType", type.ordinal());
			payloadBuilder.alertBody(content);
			if (badge > 0) {
				payloadBuilder.badge(badge);
			}
			apnsService.push(target.getLast_login_token(), payloadBuilder.build());
		}
	}
}
