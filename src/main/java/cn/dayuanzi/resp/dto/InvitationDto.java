package cn.dayuanzi.resp.dto;

import java.util.List;




import cn.dayuanzi.config.InvitationConfig;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.User;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.impl.ServiceRegistry;
import cn.dayuanzi.util.DateTimeUtil;

/**
 * 
 * @author : leihc
 * @date : 2015年5月5日
 * @version : 1.0
 */
public class InvitationDto implements Comparable<InvitationDto>{

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
	 * 当前用户是否加入讨论组
	 */
	private boolean joined;
	/**
	 * 是否收藏
	 */
	private boolean collected;
	/**
	 * 是否点过喜欢
	 */
	private boolean clickLike;
	
	/**
	 * 评论总人数
	 */
	private long replyAmount;
	/**
	 * 该邀约所在社区离当前用户所在社区距离
	 */
	private double distance;
	/**
	 * 艾特对象
	 */
	private List<Object[]> ats = null;
	
	public InvitationDto(Invitation invitation){
		this.invitation_id = invitation.getId();
		this.send_time = DateTimeUtil.getDisplay(invitation.getCreate_time());
		int interestId = invitation.getInvitation_type();
		this.activity_type = InvitationConfig.getInterests().get(interestId).getInvitation();
		this.activity_time =DateTimeUtil.formatDateTime(invitation.getActivity_time()) ;
		this.activity_place = invitation.getActivity_place();
		this.content = invitation.getContent();
		this.images = invitation.getImage_names();
		User sender = ServiceRegistry.getUserService().findUserById(invitation.getUser_id());
		this.sender_id = sender.getId();
		this.sender_name = sender.getNickname();
		this.sender_head = sender.getHead_icon();
		IInvitationService invitionService = ServiceRegistry.getInvitationService();
		this.like_amount = invitionService.countLikeInvitation(invitation);
		this.discussGroup_amount = invitionService.countDiscussGroupMembers(invitation);
		this.replyAmount = invitionService.countReply(invitation.getId());
	}
	
	public long getInvitation_id() {
		return invitation_id;
	}
	public void setInvitation_id(long invitation_id) {
		this.invitation_id = invitation_id;
	}
	public long getSender_id() {
		return sender_id;
	}
	public void setSender_id(long sender_id) {
		this.sender_id = sender_id;
	}
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
	public String getSender_head() {
		return sender_head;
	}
	public void setSender_head(String sender_head) {
		this.sender_head = sender_head;
	}
	
	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	
	public long getReplyAmount() {
		return replyAmount;
	}

	public void setReplyAmount(long replyAmount) {
		this.replyAmount = replyAmount;
	}

	public boolean isJoined() {
		return joined;
	}

	public void setJoined(boolean joined) {
		this.joined = joined;
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
	public int getLike_amount() {
		return like_amount;
	}
	public void setLike_amount(int like_amount) {
		this.like_amount = like_amount;
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

	public boolean isClickLike() {
		return clickLike;
	}

	public void setClickLike(boolean clickLike) {
		this.clickLike = clickLike;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public List<Object[]> getAts() {
		return ats;
	}

	public void setAts(List<Object[]> ats) {
		this.ats = ats;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(InvitationDto o) {
		double r = this.distance-o.distance;
		if(r<0){
			return -1;
		}else if(r>0){
			return 1;
		}
		return 0;
	}
	
}
