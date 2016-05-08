package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.PersistentSupport;

/**
 * 
 * @author : leihc
 * @date : 2015年7月3日
 * @version : 1.0
 */
@Entity
@Table(name = "t_invitation_signup")
public class InvitationSignUp extends PersistentSupport {

	/**
	 * 报名者ID
	 */
	private long user_id;
	/**
	 * 邀约ID
	 */
	private long invitation_id;
	/**
	 * 报名时间
	 */
	private long create_time;
	
	public InvitationSignUp(){
		
	}
	
	public InvitationSignUp(long user_id, long invitation_id){
		this.user_id = user_id;
		this.invitation_id = invitation_id;
		this.create_time = System.currentTimeMillis();
	}
	
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public long getInvitation_id() {
		return invitation_id;
	}
	public void setInvitation_id(long invitation_id) {
		this.invitation_id = invitation_id;
	}
	public long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(long create_time) {
		this.create_time = create_time;
	}
	
	
}
