package cn.dayuanzi.resp.dto;

import cn.dayuanzi.pojo.PushType;


/**
 * 
 * @author : leihc
 * @date : 2015年7月9日
 * @version : 1.0
 */
public class PushMessageDto {

	/**
	 * 推送消息类型
	 */
	private int pushType;
	/**
	 * 通知类型
	 */
	private int noticeType;
	/**
	 * 推送消息内容
	 */
	private String content;
	/**
	 * 需要查看详情时，附带的信息
	 */
	private long append;
	
	public PushMessageDto(PushType pushType, String content, long append){
		this.pushType = pushType.ordinal();
		this.content = content;
		this.append = append;
	}
	
	public int getPushType() {
		return pushType;
	}
	public void setPushType(int pushType) {
		this.pushType = pushType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getAppend() {
		return append;
	}
	public void setAppend(long append) {
		this.append = append;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}
	
}
