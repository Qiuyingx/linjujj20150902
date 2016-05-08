package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.dayuanzi.pojo.ExternalType;
import cn.framework.persist.db.PersistentSupport;

/**
 * 用户登陆信息表
 * @author : leihc
 * @date : 2015年7月27日
 * @version : 1.0
 */
@Entity
@Table(name = "t_user_login")
public class UserLogin extends PersistentSupport {

	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 用户的当前社区
	 */
	private long courtyardId;
	/**
	 * 登陆时间
	 */
	private long loginTime;
	/**
	 * 使用平台
	 */
	private int platform;
	/**
	 * 设备TOKEN
	 */
	private String deviceToken;
	/**
	 * 外部平台
	 * {@link ExternalType}
	 */
	private int externalType;
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public long getCourtyardId() {
		return courtyardId;
	}
	public void setCourtyardId(long courtyardId) {
		this.courtyardId = courtyardId;
	}
	public int getExternalType() {
		return externalType;
	}
	public void setExternalType(int externalType) {
		this.externalType = externalType;
	}
	
	
}
