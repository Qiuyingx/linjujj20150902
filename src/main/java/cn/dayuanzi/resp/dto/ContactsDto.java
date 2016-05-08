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
public class ContactsDto {
     
	

	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 联系人昵称
	 */
	private String userName;
	/**
	 * 与当前用户是否加为好友
	 */
	private  int accept;
	/**
	 * 联系人头像
	 */
	private String userHead;
	/**
	 * 邻居的兴趣
	 */
	private List<Interest> interests=new ArrayList<Interest>();
	/**
	 * 信息
	 */
	private String info;
	public ContactsDto(){
		
	}
	
	public ContactsDto(User user){ 
		this.userId = user.getId();
		this.userName = user.getNickname();
		this.userHead = user.getHead_icon();
		
	}
	

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	

	public List<Interest> getInterests() {
	    return interests;
	}

	public void setInterests(List<Interest> interests) {
	    this.interests = interests;
	}

	public void setAccept(int accept) {
		this.accept = accept;
	}

	public int getAccept() {
		return accept;
	}




	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserHead() {
		return userHead;
	}
	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	
	
}
