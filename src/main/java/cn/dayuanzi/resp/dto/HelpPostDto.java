package cn.dayuanzi.resp.dto;

import java.util.List;

import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.service.impl.ServiceRegistry;

/**
 * 
 * @author : leihc
 * @date : 2015年4月23日 下午3:54:20
 * @version : 1.0
 */
public class HelpPostDto {

	/**
	 * 发互助人的ID
	 */
	private long userId;
	/**
	 * 发互助人的昵称
	 */
	private String userName;
	/**
	 * 发互助的头像
	 */
	private String userHead;
	/**
	 * 互助题目
	 */
	private String title;
	/**
	 * 互助内容
	 */
	private String content;
	/**
	 * 图片地址
	 */
	private List<String> images;
	/**
	 * 回复人数
	 */
	private int replyAmount;
	
	public HelpPostDto(UserPost userPost){
		this.userId = userPost.getUser_id();
		User user = ServiceRegistry.getUserService().findUserById(userId);
		this.userName = user.getNickname();
		this.userHead = user.getHead_icon();
		this.title = userPost.getTitle();
		this.content = userPost.getContent();
		this.images = userPost.getImage_names();
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public int getReplyAmount() {
		return replyAmount;
	}
	public void setReplyAmount(int replyAmount) {
		this.replyAmount = replyAmount;
	}

	public String getUserHead() {
		return userHead;
	}

	public void setUserHead(String userHead) {
		this.userHead = userHead;
	}
	
	
}
