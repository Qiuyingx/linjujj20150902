package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.NewFollowDto;
import cn.dayuanzi.resp.dto.NewNoticeDto;
import cn.dayuanzi.resp.dto.NewReplyDto;
import cn.dayuanzi.resp.dto.PushMessageDto;

/**
 * andorid推送消息返回的数据
 * @author : leihc
 * @date : 2015年5月29日
 * @version : 1.0
 */
public class CheckNoticeResp extends Resp {

	/**
	 * 推送消息
	 */
	private List<PushMessageDto> alerts = new ArrayList<PushMessageDto>();
	/**
	 * 最新的通知
	 */
	private NewNoticeDto newNotice;
	/**
	 * 最新的好友请求
	 */
//	private NewFriendRequestDto newFriendrRequest;
	/**
	 * 最新的参与的话题的回复动态
	 */
	private NewReplyDto newReplyDto;
	/**
	 * 最新的关注自己的消息
	 */
	private NewFollowDto newFollowDto;
	

	public NewNoticeDto getNewNotice() {
		return newNotice;
	}

	public void setNewNotice(NewNoticeDto newNotice) {
		this.newNotice = newNotice;
	}

//	public NewFriendRequestDto getNewFriendrRequest() {
//		return newFriendrRequest;
//	}
//
//	public void setNewFriendrRequest(NewFriendRequestDto newFriendrRequest) {
//		this.newFriendrRequest = newFriendrRequest;
//	}

	public NewReplyDto getNewReplyDto() {
		return newReplyDto;
	}

	public void setNewReplyDto(NewReplyDto newReplyDto) {
		this.newReplyDto = newReplyDto;
	}

	public NewFollowDto getNewFollowDto() {
		return newFollowDto;
	}

	public void setNewFollowDto(NewFollowDto newFollowDto) {
		this.newFollowDto = newFollowDto;
	}

	public List<PushMessageDto> getAlerts() {
		return alerts;
	}

	public void setAlerts(List<PushMessageDto> alerts) {
		this.alerts = alerts;
	}
	
	
}
