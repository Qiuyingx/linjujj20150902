package cn.dayuanzi.resp.dto;

import java.util.List;

/**
 * 新评论详情
 * 
 * @author : leihc
 * @date : 2015年4月23日 下午1:30:33
 * @version : 1.0
 */
public class NewReplyDto implements Comparable<NewReplyDto>{
	/**
	 * 评论所在数据表的唯一id
	 */
	private long id;
	/**
	 * 
	 */
	private long reply_id;
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
	 * 被评论的帖子ID
	 */
	private long postId;
	/**
	 * 被评论的帖子内容
	 */
	private String postContent;
	/**
	 * 评论的帖子类型
	 */
	private int postType;
	/**
	 * 被评论的帖子图片地址
	 */
	private List<String> postImages;
	/**
	 * 数字表示的时间，用户排序
	 */
	private long reply_time_number;
	
	
	
	public long getReply_id() {
	    return reply_id;
	}
	public void setReply_id(long reply_id) {
	    this.reply_id = reply_id;
	}
	public int getPostType() {
		return postType;
	}
	public void setPostType(int postType) {
		this.postType = postType;
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
	public long getPostId() {
		return postId;
	}
	public void setPostId(long postId) {
		this.postId = postId;
	}
	public String getPostContent() {
		return postContent;
	}
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	public List<String> getPostImages() {
		return postImages;
	}
	public void setPostImages(List<String> postImages) {
		this.postImages = postImages;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public long getReply_time_number() {
		return reply_time_number;
	}
	public void setReply_time_number(long reply_time_number) {
		this.reply_time_number = reply_time_number;
	}
	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(NewReplyDto o) {
		if(this.reply_time_number>o.reply_time_number){
			return -1;
		}else if(this.reply_time_number<o.reply_time_number){
			return 1;
		}
		return 0;
	}
	
}
