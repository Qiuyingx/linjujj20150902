/**
 * 
 */
package cn.dayuanzi.resp.dto;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.config.Interest;
import cn.dayuanzi.model.User;

/**
 * 
 *
 * @author : leihc
 * @date : 2015年5月2日
 * 
 */
public class RecommendDto {

	/**
	 * 推荐邻居的用户ID
	 */
	private long userId;
	/**
	 * 推荐邻居的昵称
	 */
	private String nickname;
	/**
	 * 邻居的头像地址
	 */
	private String headIcon;
	/**
	 * 邻居的兴趣
	 */
	private List<Interest> interests=new ArrayList<Interest>();
	/**
	 * 信息
	 */
	private String info;

	public RecommendDto(){
	    
	}
	
	public RecommendDto(User user){
		this.userId = user.getId();
		this.nickname = user.getNickname();
		this.headIcon = user.getHead_icon();
	}
	
	
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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
	public String getHeadIcon() {
		return headIcon;
	}
	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}


	public List<Interest> getInterests() {
		return interests;
	}


	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}
	
	
	
}
