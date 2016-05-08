package cn.dayuanzi.service;

import cn.dayuanzi.model.Friends;
import cn.dayuanzi.model.ImToken;
import cn.dayuanzi.model.Invitation;

/**
 * 
 * @author : leihc
 * @date : 2015年4月26日 下午12:03:25
 * @version : 1.0
 */
public interface IHuanXinService {

	/**
	 * 获取APP管理员token
	 * @return
	 */
	public ImToken getToken();
	
	/**
	 * 向环信服务器注册新用户
	 * 
	 * @param userId
	 * @param password
	 * @param nickname
	 */
	public boolean registerUser(long userId, String password);
	
	/**
	 * 添加好友
	 * 
	 * @param friends
	 */
	public boolean addFriend(Friends friends);
	
	/**
	 * 添加邀约讨论组
	 *
	 * @param invitation
	 */
	public void createDiscussGroup(Invitation invitation, String group_name);
	
	/**
	 * 解散讨论组
	 * 
	 * @param invitation
	 */
	public void dismissDiscussGroup(Invitation invitation);
	
	/**
	 * 加入群组
	 * 
	 * @param userId
	 * @param groupId
	 */
	public void joinDiscussGroup(long userId, String groupId);
	
	/**
	 * 退出讨论组
	 * @param userId
	 * @param groupId
	 */
	public void exitDiscussGroup(long userId, String groupId);
	
	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @param newpw
	 */
	public void modifypassword(long userId, String newpw);
	
	/**
	 * 修改昵称
	 * @param userId
	 * @param nickname
	 */
	public void modifyNickName(long userId, String nickname);
	
	/**
	 * 发送文本信息
	 * 
	 * @param userId
	 * @param content
	 * @param invitation
	 */
	public void sendTextToGroup(long userId, String content, Invitation invitation);
	
	/**
	 * 获取离线未读消息数
	 * @param userId
	 * @return
	 */
	public long getOfflineMsgCount(long userId);
}
