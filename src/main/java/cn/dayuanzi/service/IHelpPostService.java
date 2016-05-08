package cn.dayuanzi.service;


import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.resp.UserPostListResp;




public interface IHelpPostService {
	
	
	/**
	 * 根据互助帖子ID获取帖子详情
	 * 
	 * @param postId
	 * @return
	 */
	public UserPost getHelpPost(long postId);
	
	/**
	 * 获取指定院子的互助列表
	 * 
	 * @param courtyard_id
	 * @param current_page
	 * @param max_num
	 * @return
	 */
//	public List<UserPost> getHelpPostList(long courtyard_id, int current_page, int max_num);
	
	/**
	 * 发新的互助贴
	 * 
	 * @param yardId
	 * @param userId
	 * @param tag
	 * @param content
	 * @param showAround 是否周围小区可以看见
	 */
	public UserPostListResp saveHelpPost(long yardId, long userId, int tag, String content, CommonsMultipartFile[] images,int priority, int reward, boolean showAround,String title);
	
	
	
	/**
	 * 统计评论的数量
	 * 
	 * @param postId
	 * @return
	 */
//	public long countReply(long postId);
	

	/**
	 * 采纳建议
	 * @param userId
	 * @param replyId
	 */
	public void acceptHelp(long userId, long replyId);
}
