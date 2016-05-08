package cn.dayuanzi.resp.dto;

/**
 * 
 * @author : leihc
 * @date : 2015年7月28日
 * @version : 1.0
 */
public class UserStarDto {

	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 头像
	 */
	private String headIcon;
	/**
	 * 标签名
	 */
	private String tagName;
	/**
	 * 社区名字
	 */
	private String courtyardName;
	
	
	
	public String getCourtyardName() {
		return courtyardName;
	}
	public void setCourtyardName(String courtyardName) {
		this.courtyardName = courtyardName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getHeadIcon() {
		return headIcon;
	}
	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
}
