package cn.dayuanzi.resp.dto;

import cn.dayuanzi.model.User;

/**
 * 
 * @author : leihc
 * @date : 2015年7月30日
 * @version : 1.0
 */
public class AtDto {

	private long userId;
	private String nickname;
	
	public AtDto(){
		
	}
	
	public AtDto(User user){
		this.userId = user.getId();
		this.nickname = user.getNickname();
	}
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
