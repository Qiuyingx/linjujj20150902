package cn.dayuanzi.service;

import cn.dayuanzi.model.UserCollect;

import cn.dayuanzi.resp.InvitationListResp;
import cn.dayuanzi.resp.UserPostListResp;

/**
 * 
 * @author : leihc
 * @date : 2015年4月27日
 * @version : 1.0
 */
public interface IUserCollectService {

	/**
	 * 收藏帖子
	 * 
	 * @param courtyardId
	 * @param userId
	 * @param collectType
	 * @param targetId
	 */
	public void collect(long courtyardId, long userId, int collectType, long targetId);
	/**
	 * 取消收藏
	 * @param courtyardId
	 * @param userId
	 * @param collectType
	 * @param targetId
	 */
	public void cancelcollect(long courtyardId, long userId, int collectType, long targetId);
	/**
	 * 获取我的邀约收藏
	 * 
	 * @param courtyard_id
	 * @param userId
	 * @param current_page
	 * @param max_num
	 */
	public InvitationListResp getInvitationCollect(long courtyard_id, long userId, int current_page, int max_num);
	
	/**
	 * 获取邻居帮帮，分享收藏
	 * 
	 * @param courtyard_id
	 * @param userId
	 * @param current_page
	 * @param max_num
	 */
	public UserPostListResp getPostCollect(long courtyard_id, long userId, int current_page, int max_num);
	
	/**
	 * 查找指定用户收藏指定的内容
	 * 
	 * @param userId
	 * @param collect_type
	 * @param target_id
	 * @return
	 */
	public UserCollect findUserCollect(long userId, int collect_type, long target_id);
	
		
}
