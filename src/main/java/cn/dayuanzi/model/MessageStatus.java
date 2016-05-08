package cn.dayuanzi.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.framework.persist.db.VersionPersistentSupport;

/**
 * 记录最后读取的消息，用户计算未读数等
 * 
 * @author : leihc
 * @date : 2015年4月22日 上午9:14:13
 * @version : 1.0
 */
@Entity
@Table(name = "t_message_check")
public class MessageStatus extends VersionPersistentSupport {

	/**
	 * 用户ID
	 */
	private long user_id;
	/**
	 * 院子ID
	 */
	private long courtyard_id;
	/**
	 * 最后一条已读评论的ID
	 */
	private long last_readed_reply_id;
	/**
	 * 最后一条已读活动中的回复的ID
	 */
	private long last_readed_activity_reply_id;
	/**
	 * 最后一条已读专题中的回复ID
	 */
	private long last_readed_subject_reply_id;
	/**
	 * 最后一条已读点赞的ID
	 */
	private long last_readed_laud_id;
	/**
	 * 最后一条邀约点赞的ID
	 */
	private long last_readed_invitation_like_id;
	/**
	 * 最后的好友请求的ID
	 */
	private long last_friend_request;
	/**
	 * 最后一条通知的ID
	 */
	private long last_notice;
	/**
	 * 最后一条邀约的ID
	 */
	private long last_invitation;
	/**
	 * 最后一条评论感谢
	 */
	private long last_think_reply;
	/**
	 * 看过的本社区最后一个邻居的验证时间
	 */
	private long last_neighbor_validate_time;
	/**
	 * 看过的最后一篇邻聚专题的发布时间
	 */
	private long last_linju_subject_time;
	/**
	 * 最后一位关注自己的邻友的操作时间
	 */
	private long last_follow_me_time;
	
	/**
	 * 最后一条求助通知时间
	 */
	private long last_help_time;
	/**
	 * 最后一条采纳通知时间
	 */
	private long last_accpet_time;
	/**
	 * 最后一条社区公告时间
	 */
	private long last_courtyard_time;
	/**
	 * 最后一条系统通知时间
	 */
	private long last_system_time;
	/**
	 * 最后一条审核通知时间
	 */
	private long last_verify_time;
	/**
	 * 看过的最后一个活动ID
	 */
	private long last_activity_time;
	/**
	 * 看过的最后一条新闻时间
	 */
	private long last_news_time;
	/**
	 * 看过的最新的一条回复的时间
	 */
	private long last_readed_reply_time;
	/**
	 * 看过最后的一条at消息
	 */
	private long last_readed_atTime;
	/**
	 * 看过最后一条感谢时间
	 */
	private long last_readed_laud_time;
	
	public MessageStatus(){
		
	}

	public long getLast_readed_atTime() {
		return last_readed_atTime;
	}

	public void setLast_readed_atTime(long last_readed_atTime) {
		this.last_readed_atTime = last_readed_atTime;
	}

	public MessageStatus(long user_id, long courtyard_id){
		this.user_id = user_id;
		this.courtyard_id = courtyard_id;
	}
	
	public long getLast_think_reply() {
		return last_think_reply;
	}

	public void setLast_think_reply(long last_think_reply) {
		this.last_think_reply = last_think_reply;
	}

	/**
	 * @return the user_id
	 */
	public long getUser_id() {
		return user_id;
	}
	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	public long getCourtyard_id() {
		return courtyard_id;
	}
	public void setCourtyard_id(long courtyard_id) {
		this.courtyard_id = courtyard_id;
	}
	/**
	 * @return the last_readed_reply_id
	 */
	public long getLast_readed_reply_id() {
		return last_readed_reply_id;
	}
	/**
	 * @param last_readed_reply_id the last_readed_reply_id to set
	 */
	public void setLast_readed_reply_id(long last_readed_reply_id) {
		this.last_readed_reply_id = last_readed_reply_id;
	}
	/**
	 * @return the last_readed_laud_id
	 */
	public long getLast_readed_laud_id() {
		return last_readed_laud_id;
	}
	/**
	 * @param last_readed_laud_id the last_readed_laud_id to set
	 */
	public void setLast_readed_laud_id(long last_readed_laud_id) {
		this.last_readed_laud_id = last_readed_laud_id;
	}
	/**
	 * @return the last_friend_request
	 */
	public long getLast_friend_request() {
		return last_friend_request;
	}
	/**
	 * @param last_friend_request the last_friend_request to set
	 */
	public void setLast_friend_request(long last_friend_request) {
		this.last_friend_request = last_friend_request;
	}
	/**
	 * @return the last_notice
	 */
	public long getLast_notice() {
		return last_notice;
	}
	/**
	 * @param last_notice the last_notice to set
	 */
	public void setLast_notice(long last_notice) {
		this.last_notice = last_notice;
	}
	/**
	 * @return the last_invitation
	 */
	public long getLast_invitation() {
		return last_invitation;
	}
	/**
	 * @param last_invitation the last_invitation to set
	 */
	public void setLast_invitation(long last_invitation) {
		this.last_invitation = last_invitation;
	}

	public long getLast_readed_invitation_like_id() {
		return last_readed_invitation_like_id;
	}

	public void setLast_readed_invitation_like_id(
			long last_readed_invitation_like_id) {
		this.last_readed_invitation_like_id = last_readed_invitation_like_id;
	}

	public long getLast_readed_activity_reply_id() {
		return last_readed_activity_reply_id;
	}

	public void setLast_readed_activity_reply_id(long last_readed_activity_reply_id) {
		this.last_readed_activity_reply_id = last_readed_activity_reply_id;
	}

	public long getLast_neighbor_validate_time() {
		return last_neighbor_validate_time;
	}

	public void setLast_neighbor_validate_time(long last_neighbor_validate_time) {
		this.last_neighbor_validate_time = last_neighbor_validate_time;
	}

	public long getLast_linju_subject_time() {
		return last_linju_subject_time;
	}

	public void setLast_linju_subject_time(long last_linju_subject_time) {
		this.last_linju_subject_time = last_linju_subject_time;
	}

	public long getLast_follow_me_time() {
		return last_follow_me_time;
	}

	public void setLast_follow_me_time(long last_follow_me_time) {
		this.last_follow_me_time = last_follow_me_time;
	}

	public long getLast_help_time() {
		return last_help_time;
	}

	public void setLast_help_time(long last_help_time) {
		this.last_help_time = last_help_time;
	}

	public long getLast_accpet_time() {
		return last_accpet_time;
	}

	public void setLast_accpet_time(long last_accpet_time) {
		this.last_accpet_time = last_accpet_time;
	}

	public long getLast_courtyard_time() {
		return last_courtyard_time;
	}

	public void setLast_courtyard_time(long last_courtyard_time) {
		this.last_courtyard_time = last_courtyard_time;
	}

	public long getLast_system_time() {
		return last_system_time;
	}

	public void setLast_system_time(long last_system_time) {
		this.last_system_time = last_system_time;
	}

	public long getLast_verify_time() {
		return last_verify_time;
	}

	public void setLast_verify_time(long last_verify_time) {
		this.last_verify_time = last_verify_time;
	}

	public long getLast_activity_time() {
		return last_activity_time;
	}

	public void setLast_activity_time(long last_activity_time) {
		this.last_activity_time = last_activity_time;
	}

	public long getLast_news_time() {
		return last_news_time;
	}

	public void setLast_news_time(long last_news_time) {
		this.last_news_time = last_news_time;
	}

	public long getLast_readed_subject_reply_id() {
		return last_readed_subject_reply_id;
	}

	public void setLast_readed_subject_reply_id(long last_readed_subject_reply_id) {
		this.last_readed_subject_reply_id = last_readed_subject_reply_id;
	}

	public long getLast_readed_reply_time() {
		return last_readed_reply_time;
	}

	public void setLast_readed_reply_time(long last_readed_reply_time) {
		this.last_readed_reply_time = last_readed_reply_time;
	}

	public long getLast_readed_laud_time() {
		return last_readed_laud_time;
	}

	public void setLast_readed_laud_time(long last_readed_laud_time) {
		this.last_readed_laud_time = last_readed_laud_time;
	}

}
