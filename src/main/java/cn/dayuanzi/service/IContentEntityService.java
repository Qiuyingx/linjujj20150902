/**
 * 
 */
package cn.dayuanzi.service;

import java.util.List;

import cn.dayuanzi.model.ContentEntityReply;
import cn.dayuanzi.resp.ContentEntityListResp;
import cn.dayuanzi.resp.ContentEntityResp;

/** 
 * @ClassName: IContentEntityService 
 * @Description: 专题
 * @author qiuyingxiang
 * @date 2015年7月15日 上午9:10:07 
 *  
 */

public interface IContentEntityService {

    
    	/**
	 * 专题列表
	 * @return
	 */
	public ContentEntityListResp getContentEntityList(int current_page, int max_num,long courtyardId);
	/**
	 * 搜索专题
	 * @param courtyardId
	 * @param keys
	 * @return
	 */
	public ContentEntityListResp searchContentEntity(long courtyardId,String keys);
	/**
	 * 专题详情
	 */
	public ContentEntityResp getContentEntityDetail(long contentId);
	/**
	 * 分享专题
	 */
	public  ContentEntityResp shareContentEntityDetail(long contentId); 
	
	/**
	 * 专题评论    回复
	 */
	public void replycontentEntity(long targetId,long userId,long replyType,String content,long atReplyerId);
	
	/**
	 * 专题喜欢
	 */
	public void likecontentEntity(long userId,long contentId,long courtyardId);
	/**
	 * 是否喜欢
	 * @param userId
	 * @param cotentId
	 * @return
	 */
	public boolean isContentEntity(long userId,long cotentId);
	/**
	 * 查询专题下的评论
	 * @param activityId
	 * @param courtyardId
	 * @return
	 */
	public List<ContentEntityReply> findContentEntityReplys(long cotentId, long courtyardId );
	/**
	 * 查询回复
	 * @param cotentId
	 * @param rereplyId
	 * @return
	 */
	public List<ContentEntityReply> findContentEntityReply(long cotentId, long rereplyId );
	/**
	 * 专题点攒计数
	 * @param contentId
	 * @return
	 */
	public long countLaud(long contentId,long courtyardId);
	/**
	 * 专题评论计数
	 * @param contentId
	 * @return
	 */
	public long countReply(long contentId,long courtyardId);
}
