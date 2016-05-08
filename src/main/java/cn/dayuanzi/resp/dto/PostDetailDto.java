package cn.dayuanzi.resp.dto;

import java.util.List;

import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.util.DateTimeUtil;

/**
 * 
 * @author : leihc
 * @date : 2015年5月7日
 * @version : 1.0
 */
public class PostDetailDto {

	/**
	 * 帖子ID
	 */
	private long postId;
	/**
	 * 发帖人ID
	 */
	private long senderId;
	/**
	 * 社区名称
	 */
	private String courtyardName;
	/**
	 * 发帖人头像地址
	 */
	private String senderHeadIcon;
	/**
	 * 发帖人昵称
	 */
	private String senderName;
	/**
	 * 发帖时间
	 */
	private String sendTime;
	/**
	 * 发帖标题
	 */
	private String title;
	/**
	 * 发帖内容
	 */
	private String content;
	/**
	 * 发帖图片
	 */
	private List<String> images;
	/**
	 * 圈子ID
	 */
	private int contentType;
	/**
	 * 如果是邻居帮帮，是否是紧急求助
	 */
	private boolean priority;
	/**
	 * 点赞人数
	 */
	private long laudAmount;
	/**
	 * 评论总人数
	 */
	private long replyAmount;
	/**
	 * 是否收藏过
	 */
	private boolean collected;
	/**
	 * 是否点赞过
	 */
	private boolean lauded;
	/**
	 * 邻居帮帮标签
	 */
	private String tag;
	/**
	 * 悬赏的邻豆数
	 */
	private int reward;
	/**
	 * 采纳状态
	 */
	private boolean accepted;
	/**
	 * at对象
	 */
	private List<Object[]> ats = null;
	/**
	 * 是否是热帖
	 */
	private boolean hot;

	
	public PostDetailDto(User sender,UserPost userPost){
		this.postId = userPost.getId();
		this.senderId = sender.getId();
		this.senderHeadIcon = sender.getHead_icon();
		this.senderName = sender.getNickname();
		this.sendTime = DateTimeUtil.getDisplay(userPost.getCreate_time());
		this.title = userPost.getTitle();
		this.content = userPost.getContent();
		this.images = userPost.getImage_names();
		this.contentType = userPost.getContent_type();
		this.priority = userPost.getPriority()==1;
		if(userPost.getContent_type()==ContentType.邻居帮帮.getValue()){
//			this.tag = TagConfig.getTags().get(userPost.getTag()).getName();
			this.reward = userPost.getReward();
			this.accepted = userPost.getAcceptId() > 0;
		}
//		if(userPost.getContent_type()==ContentType.分享.getValue()){
		    //   this.topicTag = TopicTagConfig.getTopicTag().get(userPost.getTopicTag()).getName();
//		}
		
	}
	

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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
	 * @return the images
	 */
	public List<String> getImages() {
		return images;
	}
	/**
	 * @param images the images to set
	 */
	public void setImages(List<String> images) {
		this.images = images;
	}
	/**
	 * @return the laudAmount
	 */
	public long getLaudAmount() {
		return laudAmount;
	}
	/**
	 * @param l the laudAmount to set
	 */
	public void setLaudAmount(long l) {
		this.laudAmount = l;
	}
	/**
	 * @return the replyAmount
	 */
	public long getReplyAmount() {
		return replyAmount;
	}
	/**
	 * @param l the replyAmount to set
	 */
	public void setReplyAmount(long l) {
		this.replyAmount = l;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public int getContentType() {
		return contentType;
	}

	public void setContentType(int contentType) {
		this.contentType = contentType;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	public boolean isLauded() {
		return lauded;
	}

	public void setLauded(boolean lauded) {
		this.lauded = lauded;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String getCourtyardName() {
		return courtyardName;
	}

	public void setCourtyardName(String courtyardName) {
		this.courtyardName = courtyardName;
	}

	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}

	public boolean isHot() {
		return hot;
	}

	public void setHot(boolean hot) {
		this.hot = hot;
	}

}
