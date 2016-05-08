package cn.dayuanzi.service;


import cn.dayuanzi.model.MessageStatus;
import cn.dayuanzi.model.PushMessageStatus;
import cn.dayuanzi.model.User;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.resp.AtRelationsListResp;
import cn.dayuanzi.resp.ContactsResp;
import cn.dayuanzi.resp.MessageCheckResp;
import cn.dayuanzi.resp.NewInvitationResp;
import cn.dayuanzi.resp.NewLaudResp;
import cn.dayuanzi.resp.NewNoticeResp;
import cn.dayuanzi.resp.NewReplyResp;
import cn.dayuanzi.resp.Resp;

/**
 * 
 * @author : leihc
 * @date : 2015年4月22日 下午8:45:12
 * @version : 1.0
 */
public interface IMessageCheckService {

	/**
	 * 查找指定用户在当前院子中最后读过的信息状态
	 * 
	 * @param user_id
	 * @param yard_id
	 * @return
	 */
	public MessageStatus findMessageCheck(long user_id, long yard_id);
	
	/**
	 * 获取用户的推送消息状态
	 * 
	 * @param userId
	 * @return
	 */
	public PushMessageStatus findPushMessageCheck(long userId);
	
	/**
	 * 获取用户信息提示
	 * @param user_id
	 * @param yard_id
	 * @return
	 */
	public MessageCheckResp getMessageCheckInfo(long user_id, long yard_id);
	
	/**
	 * 用户操作好友请求
	 * 
	 * @param requestId
	 * @param userId
	 * @param yardId
	 * @param operate
	 */
	public void operateFriendRequest(long friendId, long userId, long yardId, int operate);
	
	/**
	 * 通过新消息提示获取新的评论列表
	 * 
	 * @param courtyard_id
	 * @param post_sender_id
	 */
	public NewReplyResp getNewReplyList(long courtyard_id, long post_sender_id,int current_page, int max_num);
	
	/**
	 * 新消息提醒中获取新的点赞列表
	 * 
	 * @param courtyard_id
	 * @param post_sender_id
	 * @return
	 */
	public NewLaudResp getNewLaudList(long courtyard_id, long post_sender_id,int current_page, int max_num);
	
	/**
	 * 查询用户的联系人
	 * @param userId
	 * @return
	 */
	public ContactsResp getContacts(long courtyardId, long userId,int current_page, int max_num);
	/**
	 * 获取用户的粉丝
	 * @param userId
	 * @return
	 */
	public ContactsResp getFollows(long userId, long courtyardId,int current_page, int max_num);

	/**
	 * 查询用户   好友请求列表
	 * @param courtyardId
	 * @param userId
	 * @return
	 */
	public ContactsResp getreQuestContacts(long courtyardId, long userId);
	
	/**
	 * 新消息提醒中获取新的通知列表
	 * 
	 * @param courtyard_id
	 * @return
	 */
	public NewNoticeResp getNewNoticeList(long courtyard_id, long user_id,int current_page, int max_num);
	
	/**
	 * 获取新的邀约
	 * 
	 * @param courtyard_id
	 * @param user_id
	 * @return
	 */
	public NewInvitationResp getNewInvitationList(long courtyard_id, long user_id);
	
	/**
	 * 请求加好友
	 * @param CourtyardId
	 * @param UserId
	 * @param friendId
	 */
	public void requestAddFriend(long  courtyardId, long userId,long friendId,String content);
	/**
	 * 判断当前好友的状态
	 */
	public boolean isFriend(long userId,long friendId);
	/**
	 * 解除好友关系
	 */
	public void deleteFriends(long friendId);
	/**
	 * 获取用户的推送消息
	 * @param userId
	 * @param courtyardId
	 * @return
	 */
	public Resp getPushMessage(User user);
	/**
	 * 获取指定设备上的推送消息
	 * @param pf
	 * @param token
	 * @return
	 */
	public Resp getPushMessage(int pf,String token);
	
	/**
	 * 重置用户的推送消息
	 * @param userId
	 * @param courtyardId
	 * @param pushType 推送类型  {@link PushType}
	 * @param noticeType {@link NoticeType}
	 */
//	public void resetMessage(long userId, long courtyardId, int pushType, int noticeType);
	/**
	 * 重置指定设备的推送消息
	 * @param pf
	 * @param token
	 * @param pushType 推送类型  {@link PushType}
	 * @param noticeType 通知类型 {@link NoticeType}
	 */
	public void resetMessage(int pf, String token, String types);
	
	/**
	 * 获取用户的所有未读信息数
	 * @param userId
	 * @return
	 */
//	public long getNotReadMsgAmount(long userId);
	/**
	 * 更新未读回复数并获取所有未读消息数
	 * @param userId
	 * @param amount
	 * @return
	 */
	public int getAllAndUpdateNotReadReply(long userId, int amount);
	
	/**
	 * 更新未读感谢数并获取所有未读消息数 
	 * @param userId
	 * @param amount
	 * @return
	 */
	public int getAllAndUpdateNotReadLaud(long userId, int amount);
	
	/**
	 * 更新未读通知数并获取所有未读消息数
	 * @param userId
	 * @param amount
	 * @return
	 */
	public int getAllAndUpdateNotReadNotice(long userId, int amount);
	
	/**
	 * 更新未读关注请求数并获取所有未读消息数
	 * @param userId
	 * @param amount
	 * @return
	 */
	public int getAllAndUpdateNotReadFollow(long userId, int amount);
	
	/**
	 * 清除未读数
	 * @param userId
	 * @param msgType
	 */
	public void clearNotReadMsgCount(long userId, String msgType);
	
	/**
	 * 获取指定消息类型的未读数
	 * redis未保存或者缓存中未读数为0时会进行一次同步
	 * 
	 * @param userId
	 * @param msgType
	 * @return
	 */
//	public int getNotReadMsgCount(long userId, String msgType);
	
	/**
	 * at的消息列表
	 * @param current_page
	 * @param max_num
	 * @param userId
	 * @return
	 */
	public AtRelationsListResp getAtList(int current_page, int max_num,long userId);
}
