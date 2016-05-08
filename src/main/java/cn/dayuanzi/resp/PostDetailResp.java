package cn.dayuanzi.resp;

import java.util.List;


import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.resp.dto.ReplyDto;
import cn.dayuanzi.util.DateTimeUtil;

/**
 * 返回帖子详情
 *  
 * @author : leihc
 * @date : 2015年4月21日 上午11:28:45
 * @version : 1.0
 */
public class PostDetailResp extends Resp {


	/**
	 * 帖子ID
	 */
	private long postId;
	/**
	 * 发帖人ID
	 */
	private long senderId;
	/**
	 * 发帖人所在院子
	 */
	private String courtyarName;
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
	private int circle_id;
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
	 * 是否收藏
	 */
	private boolean collected;
	/**
	 * 是否点过赞
	 */
	private boolean lauded;
	/**
	 * 邻居帮帮标签
	 */
	private String tag;
	/**
	 * 评论详情
	 */
	private List<ReplyDto> replys;
	/**
	 *多少分钟以前
	 */
	private String ago;
	/**
	 * 悬赏的邻豆数
	 */
	private int reward;
	/**
	 * 采纳状态
	 */
	private boolean accepted;
	/**
	 * 感谢列表（取前几个）
	 */
	private List<LaudListDto> laudList; 
	/**
	 * @
	 */
	private List<Object[]> ats = null;


	

	public PostDetailResp(User sender,UserPost userPost){
		this.postId = userPost.getId();
		this.senderId = sender.getId();
		this.senderHeadIcon = sender.getHead_icon();
		this.senderName = sender.getNickname();
		this.sendTime = DateTimeUtil.formatDateTime(userPost.getCreate_time());
		this.ago =DateTimeUtil.getDisplay(userPost.getCreate_time());
		this.title = userPost.getTitle();
		this.content = userPost.getContent();
		this.images = userPost.getImage_names();
		this.priority = userPost.getPriority()==1;
		if(userPost.getContent_type()==ContentType.邻居帮帮.getValue()){
			//this.tag = TagConfig.getTags().get(userPost.getTag()).getName();
			this.reward = userPost.getReward();
			this.accepted = userPost.getAcceptId() > 0;
		}
		if(userPost.getContent_type()==ContentType.分享.getValue()){
		    //   this.topicTag = TopicTagConfig.getTopicTag().get(userPost.getTopicTag()).getName();
		}
		
	}
	
	
	

	

	public List<LaudListDto> getLaudList() {
	    return laudList;
	}


	public void setLaudList(List<LaudListDto> laudList) {
	    this.laudList = laudList;
	}


	public String getCourtyarName() {
	    return courtyarName;
	}

	public void setCourtyarName(String courtyarName) {
	    this.courtyarName = courtyarName;
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
	/**
	 * @return the replys
	 */
	public List<ReplyDto> getReplys() {
		return replys;
	}
	/**
	 * @param replys the replys to set
	 */
	public void setReplys(List<ReplyDto> replys) {
		this.replys = replys;
	}

	public long getSenderId() {
		return senderId;
	}

	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	public int getCircle_id() {
		return circle_id;
	}

	public void setCircle_id(int circle_id) {
		this.circle_id = circle_id;
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
	public String getAgo() {
		return ago;
	}

	public void setAgo(String ago) {
		this.ago = ago;
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

	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}

	
	

}
