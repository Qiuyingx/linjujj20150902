package cn.dayuanzi.resp.dto;

/**
 * 我的房屋下的成员信息
 * 
 * @author : leihc
 * @date : 2015年5月5日
 * @version : 1.0
 */
public class MemberDto {

	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 用户头像
	 */
	private String userHead;
	/**
	 * 用户昵称
	 */
	private String nickname;
	
	public MemberDto(long userId, String userHead, String nickname){
		this.userId = userId;
		this.userHead = userHead;
		this.nickname = nickname;
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserHead() {
		return userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
