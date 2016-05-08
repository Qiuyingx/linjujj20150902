/**
 * 
 */
package cn.dayuanzi.resp.dto;

import java.util.List;

import cn.dayuanzi.model.ActivityReply;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: ActivityReplyDto 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月9日 下午3:06:53 
 *  
 */

public class ActivityReplyDto {
	/**
	 * 回复的数据库ID
	 */
	private long replyId;
	/**
	 * 评论者ID
	 */
	private long senderId;
	/**
	 * 评论者头像地址
	 */
	private String senderHeadIcon;
	/**
	 * 评论者昵称
	 */
	private String senderName;
	/**
	 * 评论时间
	 */
	private String sendTime;
	/**
	 * 评论内容
	 */
	private String content;
	/**
	 * at对象的ID
	 */
	private long atTargetId;
	/**
	 * at对象的昵称
	 */
	private String atTargetName;
	/**
	 * 该条评论下的回复
	 */
	private List<ActivityReplyDto> replys;
	/**
	 * 艾特对象
	 */
	private List<Object[]> ats = null;
	
	public ActivityReplyDto (){
		
	}
	
	public ActivityReplyDto(ActivityReply reply ){
		this.replyId = reply.getId(); 
		this.senderId = reply.getReplyer_id();
		this.sendTime = DateTimeUtil.getDisplay(reply.getCreate_time());
		this.content = reply.getContent();
		
		
	}

	public long getReplyId() {
		return replyId;
	}

	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public String getSenderHeadIcon() {
		return senderHeadIcon;
	}

	public void setSenderHeadIcon(String senderHeadIcon) {
		this.senderHeadIcon = senderHeadIcon;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getAtTargetId() {
		return atTargetId;
	}

	public void setAtTargetId(long atTargetId) {
		this.atTargetId = atTargetId;
	}

	public String getAtTargetName() {
		return atTargetName;
	}

	public void setAtTargetName(String atTargetName) {
		this.atTargetName = atTargetName;
	}

	public List<ActivityReplyDto> getReplys() {
		return replys;
	}

	public void setReplys(List<ActivityReplyDto> replys) {
		this.replys = replys;
	}

	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}
	
	
	
	
}
