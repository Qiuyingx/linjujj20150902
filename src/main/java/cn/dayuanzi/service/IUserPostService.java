package cn.dayuanzi.service;

import java.util.List;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.model.PostLaud;
import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.resp.LaudListResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.UserPostListResp;

/**
 * 
 * @author : leihc
 * @date : 2015年4月16日 下午9:11:50
 * @version : 1.0
 */
public interface IUserPostService {

	/**
	 * 根据帖子ID获取帖子详情
	 * 
	 * @param postId
	 * @return
	 */
	public UserPost getUserPost(long postId);
	
	
	/**
	 * 获取指定用户发布的帖子
	 * 
	 * @param userId
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	public UserPostListResp getUserPostListForUser(long userId,int current_page, int max_num);
	
	/**
	 * 查询指定圈子的分享
	 * 
	 * @param courtyardId
	 * @param circle
	 * @param range 距离范围，单位公里
	 * @return
	 */
	public UserPostListResp getUserPostList(long courtyardId, int current_page, int max_num, int range);
	
	/**
	 * 获取关注对象的帖子列表
	 * 
	 * @param user_id
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	public UserPostListResp getFollowPostList(long user_id, int current_page, int max_num);
	
	/**
	 * 发新贴
	 * 
	 * @param circleId
	 * @param yardId
	 * @param userId
	 * @param title
	 * @param content
	 * @param show_around 是否周围小区可以看到
	 */
	public UserPostListResp saveUserPost(int circleId, long yardId, long userId, String title, String content,CommonsMultipartFile[] images,boolean myself,boolean show_around);
	
	/**
	 * 统计给该帖子点赞的人数
	 * 
	 * @param postId
	 * @return
	 */
	public long countLaud(long postId);
	
	/**
	 * 统计评论的数量
	 * 
	 * @param postId
	 * @return
	 */
	public long countReply(long postId, int contentType);
	
	/**
	 * 查询回复该帖子的详情
	 * 
	 * @param postId
	 * @return
	 */
	public List<PostReply> findReplys(long postId, int contentType);
	
	/**
	 * 查询回复帖子评论的详情
	 * 
	 * @param postId
	 * @param reply_id
	 * @return
	 */
	public List<PostReply> findReplys(long postId, int contentType, long reply_id);
	
	/**
	 * 统计用户未读的评论条数
	 * 
	 * @param userId
	 * @param lastReadedReply 已读的最后一条评论ID
	 * @return
	 */
	public long countNotReadReply(long userId, long courtyard_id, long lastReadedReply);
	
	/**
	 * 统计未读的点赞条数
	 * 
	 * @param userId
	 * @param lastReadedLaud
	 * @return
	 */
	public long countNotReadLaud(long userId, long courtyard_id, long lastReadedLaud);
	
	/**
	 * 回复帖子
	 * 
	 * @param user_id
	 * @param post_id
	 * @param content
	 * @param replyType 1 评论帖子 2 回复评论
	 */
	public void replyPost(long user_id , int replyType, long targetId, long atReplyerId, String content);
	
	/**
	 * 给帖子点赞
	 * 
	 * @param user_id
	 * @param post_id
	 */
	public void laudPost(long user_id, long post_id);
	/**
	 * 查找指定用户点赞信息
	 * 
	 * @param userId
	 * @param postId
	 * @return
	 */
	public PostLaud findLaud(long userId, long postId);
	/**
	 * 举报帖子
	 * @param postId
	 * @param postType
	 * @param reportType
	 * @param content
	 * @param courtyardId
	 * @param userId
	 */
	public void reportPost(long  postId,int postType ,int reportType,String content ,long courtyardId,long userId);
	/**
	 * 删除帖子
	 */
	public void deletePost(long postId,long userId,int postType);
	/**
	 * 是否感谢了该回复
	 * @param userId
	 * @param replyId
	 * @return
	 */
	public boolean isThankForReply(long userId, long replyId);
	/**
	 * 这条评论的感谢有多少
	 * @param replyId
	 * @return
	 */
	public int countThankAmountForReply(long replyId);
	
	/**
	 * 获取帖子详情
	 * @param postId
	 * @return
	 */
	public Resp getPostDetail(long postId);
	
	/**
	 * 分享帖子详情
	 * @param postId
	 * @return
	 */
	public Resp sharePost(long postId);
	/**
	 * 点赞列表
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	public LaudListResp getLaudList(int current_page, int max_num ,long postId,int type);
}
