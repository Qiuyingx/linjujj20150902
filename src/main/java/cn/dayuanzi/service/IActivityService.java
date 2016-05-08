/**
 * 
 */
package cn.dayuanzi.service;

import java.util.List;









import cn.dayuanzi.model.ActivityInfo;
import cn.dayuanzi.model.ActivityReply;
import cn.dayuanzi.resp.ActivityListResp;
import cn.dayuanzi.resp.ActivityResp;
import cn.dayuanzi.resp.ContactsResp;



/** 
 * @ClassName: IActivityService 
 * @Description: 活动相关
 * @author qiuyingxiang
 * @date 2015年5月31日 下午3:35:18 
 *  
 */

public interface IActivityService {
	
	
	/**
	 *获取当前活动Id的详情
	 * @param activityId
	 * @return
	 */
	public ActivityInfo getactivity(long activityId);
	
	/**
	 * 活动报名
	 * 
	 */
	public void activitySignUp(long ateiviteyId,long userId,String name,String tel,String content);
	
	/**
	 * 判断当前用户是否参加该活动
	 */
	public  boolean  isJoinActivity(long activityId,long userId);
	
	/**
	 * 统计喜欢该活动的数量
	 */
	public int countLikeActivity(long activityId);
	
	/**
	 * 统计评论该活动的数量
	 */
	public int  countReplyActivity(long activityId,long courtyardId);
	
	/**
	 * 判断当前用户是否点赞
	 */
	public boolean isLikeActibity(long userId,long activityId);
	
	/**
	 * 查询评论
	 */
	public List<ActivityReply> findActivityReplys(long activityId, long courtyardId );
	
	/**
	 * 查询回复
	 */
	public List<ActivityReply> findActivityReply(long activityId, long rereplyId );
	/**
	 * 活动点赞
	 */
	public void likeActibity(long userId,long activityId,long courtyardId);
	
	/**
	 * 查询指定院子的活动
	 */
	public List<ActivityInfo> findActivityInfo(long courtyardId);
	/**
	 * 评论    回复
	 */
	public void replyActivity(long targetId,long userId,long replyType,String content,long atReplyerId);
	
	/**
	 * 活动详情
	 */
	public ActivityResp getActivityDetail(long activityId);
	
	/**
	 * 分享活动
	 * @param activityId
	 * @param courtyardId
	 * @return
	 */
	public ActivityResp shareActivity(long activityId,long courtyardId);
	
	/**
	 * 活动列表
	 */
	public  ActivityListResp  getActivityList();
	/**
	 * 统计报名该活动的人数
	 * @param activityId
	 * @return
	 */
	public int countSignedAmountForActivity(long activityId);
	
	/**
	 * 查找该活动的报名用户
	 * @param activityId
	 * @return
	 */
	public ContactsResp getSignUpUsers(long activityId);
}	
