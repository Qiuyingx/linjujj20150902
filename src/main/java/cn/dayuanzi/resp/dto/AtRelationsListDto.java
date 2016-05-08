/**
 * 
 */
package cn.dayuanzi.resp.dto;

import java.util.List;

import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.model.User;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: AtRelationsListDto 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年8月10日 上午10:27:55 
 *  
 */

public class AtRelationsListDto {
	
	private long Id;
	/**
	 * 帖子ID
	 */
	private long postId; 
	/**
	 * 发 at的ID
	 */
	private long senderId;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 *帖子图片地址
	 */
	private List<String> images;
	/**
	 * 头像
	 */
	private String head_icon;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 类型    邀约(1),邻居帮帮(2),分享(3),活动(4),专题(5)
	 */
	private int postType;
	/**
	 * 时间
	 */
	private String create_time;
	/**
	 * 描述
	 */
	private String  desc;
	
	
	public AtRelationsListDto(){
	    
	}
	
	public AtRelationsListDto(User user,AtRelations at){
	    this.postId = at.getAppend();
	    this.postType = at.getScene();
	    this.senderId = user.getId();
	    this.nickname = user.getNickname();
	    this.head_icon = user.getHead_icon();
	    this.create_time = DateTimeUtil.formatDateTime(at.getCreate_time());
	    
	}

	
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public long getPostId() {
	    return postId;
	}

	public void setPostId(long postId) {
	    this.postId = postId;
	}

	public long getSenderId() {
	    return senderId;
	}

	public void setSenderId(long senderId) {
	    this.senderId = senderId;
	}

	public String getNickname() {
	    return nickname;
	}

	public void setNickname(String nickname) {
	    this.nickname = nickname;
	}

	public String getHead_icon() {
	    return head_icon;
	}

	public void setHead_icon(String head_icon) {
	    this.head_icon = head_icon;
	}

	public String getContent() {
	    return content;
	}

	public void setContent(String content) {
	    this.content = content;
	}

	public int getPostType() {
	    return postType;
	}

	public void setPostType(int postType) {
	    this.postType = postType;
	}

	public String getCreate_time() {
	    return create_time;
	}

	public void setCreate_time(String create_time) {
	    this.create_time = create_time;
	}
	
}
