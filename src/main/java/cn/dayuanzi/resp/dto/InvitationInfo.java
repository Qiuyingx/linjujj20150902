package cn.dayuanzi.resp.dto;


import cn.dayuanzi.config.InvitationConfig;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.util.DateTimeUtil;

/**
 * 用于首页对邀约的简单显示
 * 
 * @author : leihc
 * @date : 2015年5月6日
 * @version : 1.0
 */
public class InvitationInfo {

	/**
	 * 该邀约的ID
	 */
	private long invitation_id;
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
	 * 活动内容
	 */
	private String activity_content;

	
	public InvitationInfo(Invitation invitation){
		this.invitation_id = invitation.getId();
		int interestId = invitation.getInvitation_type();
		this.activity_type = InvitationConfig.getInterests().get(interestId).getInvitation();
		this.activity_time =    DateTimeUtil.formatDateTime(invitation.getActivity_time());
		this.activity_place = invitation.getActivity_place();
		this.activity_content = invitation.getContent();
	}
	
	public long getInvitation_id() {
		return invitation_id;
	}
	public void setInvitation_id(long invitation_id) {
		this.invitation_id = invitation_id;
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

	public String getActivity_content() {
		return activity_content;
	}

	public void setActivity_content(String activity_content) {
		this.activity_content = activity_content;
	}
	
	
}
