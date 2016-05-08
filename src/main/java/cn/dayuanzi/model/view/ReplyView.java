package cn.dayuanzi.model.view;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import cn.dayuanzi.pojo.ContentType;

/**
 * 回复视图，关联三个表，帖子回复，活动回复，专题回复
 * @author : leihc
 * @date : 2015年7月20日
 * @version : 1.0
 */
@Entity
@Table(name = "v_reply")
public class ReplyView {

	/**
	 * 回复ID
	 */
	private long replyId;
	/**
	 * 回复者ID
	 */
	private long replyerId;
	/**
	 * 艾特对象的ID
	 */
	private long at_targetId;
	/**
	 * 发送者ID
	 */
	private long senderId;
	/**
	 * 回复内容
	 */
	private String content;
	/**
	 * 回复内容
	 */
	private long createTime;
	/**
	 * 回复的内容ID，比如帖子或者活动，专题的ID
	 */
	private long targetId;
	/**
	 * 内容类型，详情{@link ContentType}
	 */
	private int contentType;
	
	public ReplyView(){
		
	}
	
	@Id
	public long getReplyId() {
		return replyId;
	}
	public void setReplyId(long replyId) {
		this.replyId = replyId;
	}
	public long getReplyerId() {
		return replyerId;
	}
	public void setReplyerId(long replyerId) {
		this.replyerId = replyerId;
	}
	public long getAt_targetId() {
		return at_targetId;
	}
	public void setAt_targetId(long at_targetId) {
		this.at_targetId = at_targetId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getTargetId() {
		return targetId;
	}
	public void setTargetId(long targetId) {
		this.targetId = targetId;
	}
	public int getContentType() {
		return contentType;
	}
	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}
	
	
}
