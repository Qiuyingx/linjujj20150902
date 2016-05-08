package cn.dayuanzi.service;

import java.util.List;




import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.InvitationDiscussGroupMember;
import cn.dayuanzi.model.LikeInvitationMembers;
import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.resp.DiscussGroupResp;
import cn.dayuanzi.resp.InvitationListResp;
import cn.dayuanzi.resp.Resp;

/**
 * 
 * @author : leihc
 * @date : 2015年4月20日 下午4:55:29
 * @version : 1.0
 */
public interface IInvitationService {

	/**
	 * 根据邀约ID查找指定邀约
	 * 
	 * @param invitationId
	 * @return
	 */
	public Invitation findInvitationById(long invitationId);
	
	/**
	 * 查找该用户在所在院子发出的邀约
	 * 
	 * @param user_id
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	public InvitationListResp findInvitationsUser(long user_id, int current_page, int max_num);
	
	/**
	 * 查找指定院子的邀约
	 * 
	 * @param courtyard_id
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	public InvitationListResp findInvitationsforCourtyard(long courtyard_id, int current_page, int max_num);
	
	/**
	 * 统计喜欢该邀约的人数
	 * 
	 * @param invitation
	 * @return
	 */
	public int countLikeInvitation(Invitation invitation);
	
	/**
	 * 统计参加这条邀约讨论组的人数
	 * 
	 * @param invitation
	 * @return
	 */
	public int countDiscussGroupMembers(Invitation invitation);
	
	/**
	 * 添加喜欢该邀约的人
	 * 
	 * @param userId
	 * @param invitationId
	 */
	public void addLikePeopleForInvitation(long courtyardId, long userId, long invitationId);
	
	/**
	 * 发布新的邀约
	 * 
	 * @param courtyardId
	 * @param userId
	 * @param invitation_type
	 * @param activityTime
	 * @param activityPlace
	 * @param content
	 * @param images
	 * @return 
	 */
	public Invitation sendInvitation(long courtyardId, long userId, int invitation_type, long activityTime, String activityPlace, String content, CommonsMultipartFile[] images,boolean showAround);
	
	/**
	 * 评论邀约
	 * @param user_id
	 * @param replyType
	 * @param targetId
	 * @param atReplyerId
	 * @param content
	 */
	public void replyInvitation(long user_id , int replyType, long targetId, long atReplyerId, String content);
	/**
	 * 加入讨论组
	 * 
	 * @param courtyardId
	 * @param userId
	 * @param invitaionId
	 */
	public InvitationDiscussGroupMember joinDiscussGroup(long courtyardId, long userId, long invitaionId);
	
	/**
	 * 退出讨论组
	 * 
	 * @param courtyardId
	 * @param userId
	 * @param invitaionId
	 */
	public void exitDiscussGroup(long courtyardId, long userId, long invitaionId);
	
	/**
	 * 获取用户的讨论组
	 * 
	 * @param userId
	 * @param courtyardId
	 * @return
	 */
	public DiscussGroupResp getDiscussGroup(long userId, long courtyardId);
	
	/**
	 * 查找指定用户加入讨论组的信息
	 * 
	 * @param invitationId
	 * @param userId
	 * @return
	 */
	public InvitationDiscussGroupMember findDiscussGroupMember(long invitationId, long userId);
	
	/**
	 * 查找指定用户点击关注该邀约的信息
	 * @param user_id
	 * @param invitationId
	 * @return
	 */
	public LikeInvitationMembers findLikeMember(long user_id, long invitationId);
	
	/**
	 * 统计邀约评论的数量
	 * 
	 */
	public long countReply(long invitationId);
	
	
	public List<PostReply> findReplys(long invitationId);
	
	public List<PostReply> findReplys(long invitationId, long reply_id) ;
	/**
	 * 获取邀约详情
	 * @param invitationId
	 * @return
	 */
	public Resp getInvitationDetails(long invitationId);
	
	/**
	 * 分享邀约
	 * @param invitationId
	 * @return
	 */
	public Resp shareInvitation(long invitationId);
	
	/**
	 * 邀约报名
	 * @param invitationId
	 */
	public void invitationSignUp(long userId, long invitationId);
}
