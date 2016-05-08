package cn.dayuanzi.service;

import cn.dayuanzi.pojo.NoticeType;

/**
 * 
 * @author : leihc
 * @date : 2015年6月24日
 * @version : 1.0
 */
public interface INoticeService {

	/**
	 * 向所有社区发送通知
	 * 
	 * @param noticeType
	 * @param content
	 */
	public void sendNoticeToAllCourtyard(NoticeType noticeType, String title, String content, long append);
	
	/**
	 * 向指定社区发送通知
	 * 
	 * @param noticeType
	 * @param courtyardId
	 * @param content
	 */
	public void sendNoticeToCourtyard(NoticeType noticeType, long courtyardId, String title, String content, long append);
	
	/**
	 * 向指定用户发送通知
	 * 
	 * @param noticeType
	 * @param userId
	 * @param content
	 */
	public void sendNoticeToUser(NoticeType noticeType, long userId, String title, String content, long append);
}
