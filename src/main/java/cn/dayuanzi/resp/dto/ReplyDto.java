package cn.dayuanzi.resp.dto;

import java.util.List;

import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.util.DateTimeUtil;

/**
 * 
 * @author : leihc
 * @date : 2015年4月21日 上午11:36:43
 * @version : 1.0
 */
public class ReplyDto {

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
	 * 是否被当前用户感谢
	 */
	private boolean thanked;
	/**
	 * 感谢数
	 */
	private int thankAmount;
	/**
	 * 是否被采纳
	 */
	private boolean accepted;
	/**
	 * 该条评论下的回复
	 */
	private List<ReplyDto> replys;
	
	private List<Object[]> ats = null;
	
	public ReplyDto(PostReply postReply){
		this.replyId = postReply.getId(); 
		this.senderId = postReply.getReplyer_id();
		this.sendTime = DateTimeUtil.getDisplay(postReply.getCreate_time());
		this.content = postReply.getContent();
		this.accepted = postReply.isAccepted();
	}
	
	/**
	 * @return the senderHeadIcon
	 */
	public String getSenderHeadIcon() {
		return senderHeadIcon;
	}
	/**
	 * @param senderHeadIcon the senderHeadIcon to set
	 */
	public void setSenderHeadIcon(String senderHeadIcon) {
		this.senderHeadIcon = senderHeadIcon;
	}
	/**
	 * @return the senderName
	 */
	public String getSenderName() {
		return senderName;
	}
	/**
	 * @param senderName the senderName to set
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the atTargetName
	 */
	public String getAtTargetName() {
		return atTargetName;
	}
	/**
	 * @param atTargetName the atTargetName to set
	 */
	public void setAtTargetName(String atTargetName) {
		this.atTargetName = atTargetName;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public long getAtTargetId() {
		return atTargetId;
	}

	public void setAtTargetId(long atTargetId) {
		this.atTargetId = atTargetId;
	}

	public List<ReplyDto> getReplys() {
		return replys;
	}

	public void setReplys(List<ReplyDto> replys) {
		this.replys = replys;
	}

	public long getReplyId() {
		return replyId;
	}

	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}

	public boolean isThanked() {
		return thanked;
	}

	public void setThanked(boolean thanked) {
		this.thanked = thanked;
	}

	public int getThankAmount() {
		return thankAmount;
	}

	public void setThankAmount(int thankAmount) {
		this.thankAmount = thankAmount;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}
	
	
}
