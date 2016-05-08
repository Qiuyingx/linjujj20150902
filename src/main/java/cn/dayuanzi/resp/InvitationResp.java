package cn.dayuanzi.resp;

import java.util.List;

import cn.dayuanzi.config.InvitationConfig;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.User;
import cn.dayuanzi.resp.dto.ReplyDto;
import cn.dayuanzi.service.impl.ServiceRegistry;
import cn.dayuanzi.util.DateTimeUtil;

/**
 * 
 * @author : leihc
 * @date : 2015年4月20日 下午4:11:33
 * @version : 1.0
 */
public class InvitationResp extends Resp{

	/**
	 * 该邀约的ID
	 */
	private long invitation_id;
	/**
	 * 发邀约的用户ID
	 */
	private long sender_id;
	/**
	 * 发邀约的人的昵称
	 */
	private String sender_name;
	/**
	 * 发邀约的人的头像地址
	 */
	private String sender_head;
	/**
	 * 发邀约时间
	 */
	private String send_time;
	/**
	 * 邀约类型
	 */
	private String activity_type;
	/**
	 * 活动时间
	 */
	private String activity_time;
	/**
	 * 用户分享显示
	 */
	private String share_activity_time;
	/**
	 * 活动地点
	 */
	private String activity_place;
	/**
	 * 邀约内容
	 */
	private String content;
	/**
	 * 邀约图片
	 */
	private List<String> images;
	/**
	 * 喜欢这条邀约的人数
	 */
	private int like_amount;
	/**
	 * 讨论组人数
	 */
	private int discussGroup_amount;
	
	/**
	 *  讨论组 id
	 */
	
 	private String group_id;
	/**
	 * 是否加入了该讨论组
	 */
	private boolean joined;
	/**
	 * 是否收藏过
	 */
	private boolean collected;
	/**
	 * 是否点击过喜欢
	 */
	private boolean clickLike;
	/**
	 * 评论详情
	 */
	private List<ReplyDto> replys;
	/**
	 * 评论人数
	 */
	private  long ReplyAmount;
	
	private List<Object[]> ats = null;

	public InvitationResp(Invitation invitation){
		this.invitation_id = invitation.getId();
		this.send_time = DateTimeUtil.getDisplay(invitation.getCreate_time());
		int interestId = invitation.getInvitation_type();
		this.activity_type = InvitationConfig.getInterests().get(interestId).getInvitation();
		this.activity_time =  DateTimeUtil.formatDateTime(invitation.getActivity_time());
		this.share_activity_time = DateTimeUtil.formatDateTime(invitation.getActivity_time(), "MM月dd日 HH:mm");
		this.activity_place = invitation.getActivity_place();
		this.group_id = invitation.getGroup_id();
		this.content = invitation.getContent();
		this.images = invitation.getImage_names();
		User sender = ServiceRegistry.getUserService().findUserById(invitation.getUser_id());
		this.sender_id = sender.getId();
		this.sender_name = sender.getNickname();
		this.sender_head = sender.getHead_icon();
		this.like_amount = ServiceRegistry.getInvitationService().countLikeInvitation(invitation);
		this.discussGroup_amount = ServiceRegistry.getInvitationService().countDiscussGroupMembers(invitation);
	}
	
	
	public long getReplyAmount() {
		return ReplyAmount;
	}


	public void setReplyAmount(long replyAmount) {
		ReplyAmount = replyAmount;
	}


	public String getGroup_id() {
		return group_id;
	}


	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}


	public boolean isJoined() {
		return joined;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
	}
	public boolean isClickLike() {
		return clickLike;
	}

	public void setClickLike(boolean clickLike) {
		this.clickLike = clickLike;
	}

	public long getSender_id() {
		return sender_id;
	}

	public void setSender_id(long sender_id) {
		this.sender_id = sender_id;
	}

	/**
	 * @return the sender_name
	 */
	public String getSender_name() {
		return sender_name;
	}

	/**
	 * @param sender_name the sender_name to set
	 */
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}

	

	/**
	 * @return the sender_head
	 */
	public String getSender_head() {
		return sender_head;
	}

	/**
	 * @param sender_head the sender_head to set
	 */
	public void setSender_head(String sender_head) {
		this.sender_head = sender_head;
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

	public int getLike_amount() {
		return like_amount;
	}

	public void setLike_amount(int like_amount) {
		this.like_amount = like_amount;
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

	public String getActivity_type() {
		return activity_type;
	}

	public void setActivity_type(String activity_type) {
		this.activity_type = activity_type;
	}

	public String getActivity_time() {
		return activity_time;
	}

	public void setActivity_time(String activity_time) {
		this.activity_time = activity_time;
	}

	public String getActivity_place() {
		return activity_place;
	}

	public void setActivity_place(String activity_place) {
		this.activity_place = activity_place;
	}



	public long getInvitation_id() {
		return invitation_id;
	}

	public void setInvitation_id(long invitation_id) {
		this.invitation_id = invitation_id;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public int getDiscussGroup_amount() {
		return discussGroup_amount;
	}

	public void setDiscussGroup_amount(int discussGroup_amount) {
		this.discussGroup_amount = discussGroup_amount;
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}


	public List<ReplyDto> getReplys() {
		return replys;
	}


	public void setReplys(List<ReplyDto> replys) {
		this.replys = replys;
	}


	public String getShare_activity_time() {
		return share_activity_time;
	}


	public void setShare_activity_time(String share_activity_time) {
		this.share_activity_time = share_activity_time;
	}


	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}
	
	
}
