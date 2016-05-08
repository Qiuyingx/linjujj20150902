/**
 * 
 */
package cn.dayuanzi.resp.dto;



import cn.dayuanzi.config.InvitationConfig;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.User;

import cn.dayuanzi.service.impl.ServiceRegistry;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: InvitationsDto 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月8日 下午7:55:21 
 *  
 */

public class InvitationsDto {

    	/**
	 * 该邀约的ID
	 */
	private long invitation_id;
	/**
	 * 发邀约的用户ID
	 */
	private long sender_id;
	
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
	
	
	public InvitationsDto(Invitation invitation){
		this.invitation_id = invitation.getId();
		int interestId = invitation.getInvitation_type();
		this.activity_type = InvitationConfig.getInterests().get(interestId).getInvitation();
		this.activity_time =DateTimeUtil.formatDateTime(invitation.getActivity_time()) ;
		this.activity_place = invitation.getActivity_place();
		this.content = invitation.getContent();
		User sender = ServiceRegistry.getUserService().findUserById(invitation.getUser_id());
		this.sender_id = sender.getId();
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
	

	
}
