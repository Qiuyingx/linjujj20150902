package cn.dayuanzi.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.AtRelationsListResp;
import cn.dayuanzi.resp.ContactsResp;
import cn.dayuanzi.resp.DiscussGroupResp;
import cn.dayuanzi.resp.MessageCheckResp;
import cn.dayuanzi.resp.NewInvitationResp;
import cn.dayuanzi.resp.NewLaudResp;
import cn.dayuanzi.resp.NewNoticeResp;
import cn.dayuanzi.resp.NewReplyResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.IMessageCheckService;
import cn.dayuanzi.util.RedisKey;


/**
 * 消息相关
 * 
 * @author : leihc
 * @date : 2015年4月21日 下午2:32:04
 * @version : 1.0
 */
@Controller
public class MessageController {

	@Autowired
	private IMessageCheckService messageCheckService;
	@Autowired
	private IInvitationService invitationService;

	
	/**
	 * 返回消息提醒
	 * 
	 * @return
	 */
	@RequestMapping(value = "checkMessage.do")
	public ModelAndView checkMessage(){
		UserSession userSession = UserSession.get();
		MessageCheckResp resp = messageCheckService.getMessageCheckInfo(userSession.getUserId(), userSession.getCourtyardId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	
	/**
	 * 新的点赞
	 * @return
	 */
	@RequestMapping(value = "getNewLaud.do")
	public ModelAndView getNewLaud(int current_page, int max_num){
		UserSession userSession = UserSession.get();
		//if(!userSession.isValidatedUser()){
	//		throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
	//	}
		NewLaudResp resp = messageCheckService.getNewLaudList(userSession.getCourtyardId(), userSession.getUserId(),current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 新的邀约
	 * @return
	 */
	@RequestMapping(value = "getNewInvitation.do")
	public ModelAndView getNewInvitation(){
		UserSession userSession = UserSession.get();
	//	if(!userSession.isValidatedUser()){
	//	throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
	//	}
		NewInvitationResp resp = messageCheckService.getNewInvitationList(userSession.getCourtyardId(), userSession.getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 新的通知
	 * @return
	 */
	@RequestMapping(value = "getNewNotice.do")
	public ModelAndView getNewNotice(int current_page, int max_num){
		UserSession userSession = UserSession.get();
	//	if(!userSession.isValidatedUser()){
	//		throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
	//	}
		NewNoticeResp resp = messageCheckService.getNewNoticeList(userSession.getCourtyardId(), userSession.getUserId(),current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 新的评论
	 * @return
	 */
	@RequestMapping(value = "getNewReply.do")
	public ModelAndView getNewReply(int current_page, int max_num){
		UserSession userSession = UserSession.get();
	//	if(!userSession.isValidatedUser()){
	//		throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
	//	}
		NewReplyResp resp = messageCheckService.getNewReplyList(userSession.getCourtyardId(), userSession.getUserId(), current_page, max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 用于ios 客户端内 看完消息后重置 接口
	 * @return
	 */
	@RequestMapping(value = "getNewChat.do")
	public ModelAndView getNewChat(){
		UserSession userSession = UserSession.get();
		messageCheckService.clearNotReadMsgCount(userSession.getUserId(), RedisKey.not_read_chat);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 获取用户联系人，获取邻友,自己关注的人
	 */
	@RequestMapping(value = "getMyFollows.do")
	public ModelAndView getContacts(int current_page, int max_num){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
		}
		ContactsResp resp = messageCheckService.getContacts(userSession.getCourtyardId(), userSession.getUserId(),current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获取粉丝
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "getFollows.do")
	public ModelAndView getFollows(long userId,int current_page, int max_num){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
		}
		ContactsResp resp = messageCheckService.getFollows(userId,userSession.getCourtyardId(),current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获取新关注列表
	 */
//	@RequestMapping(value = "getQuestContacts.do")
	public ModelAndView getreQuestContacts(){
		UserSession userSession = UserSession.get();
		//if(!userSession.isValidatedUser()){
	//		throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
	//	}
		ContactsResp resp = messageCheckService.getreQuestContacts(userSession.getCourtyardId(), userSession.getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 处理好友请求
	 * 
	 * @param operate   2同意  3解除好友关系  4拒绝(预留)                       好友状态   （ 0 为不是好友   1请求状态 ）   2添加好友  3解除好友   
	 * @param requestId 好友请求的ID
	 * @return
	 */
//	@RequestMapping(value = "acceptFriend.do")
	public ModelAndView acceptFriend(int operate,long friendId ){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
		}
		if(operate!=1 && operate!=2&&operate!=3&&operate!=4){
			throw new GeneralLogicException("参数错误。");
		}
		if(operate==2){
			messageCheckService.operateFriendRequest(friendId, userSession.getUserId(), userSession.getCourtyardId(), operate);
		}
		if(operate==3){
			messageCheckService.deleteFriends(friendId);
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 请求加好友
	 */
//	@RequestMapping(value = "requestAddFriend.do")
	public ModelAndView addFriend(long friendId,@RequestParam(required=false)String content){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		messageCheckService.requestAddFriend(userSession.getCourtyardId(),userSession.getUserId(),friendId,content);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	
	
	/**
	 * 获取讨论组列表
	 * 
	 * @return
	 */
//	@RequestMapping(value = "getDiscussGroups.do")
	public ModelAndView getDiscussGroups(){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		DiscussGroupResp resp = this.invitationService.getDiscussGroup(userSession.getUserId(), userSession.getCourtyardId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	

	/**
	 * 将最新推送消息重置
	 * @param token
	 * @param pushType 推送类型  {@link PushType}
	 * @param noticeType 通知类型 {@link NoticeType}
	 * @param type 格式: pushType,noticeType|pushType,noticeType|pushType,noticeType|....
	 * @return
	 */
	@RequestMapping(value = "resetMessage.do")
	public ModelAndView resetMessage(String token, String type){
		if(StringUtils.isBlank(token)){
			throw new GeneralLogicException("参数错误");
		}
		this.messageCheckService.resetMessage(1, token, type);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * android获取推送消息
	 * @return
	 */
	@RequestMapping(value = "pushMessage.do")
	public ModelAndView pushMessage(String token){
		if(StringUtils.isBlank(token)){
			throw new GeneralLogicException("参数错误");
		}
		Resp resp = this.messageCheckService.getPushMessage(1 ,token);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 *   At消息列表
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	@RequestMapping(value = "getAtList.do")
	public ModelAndView getAtList(int current_page, int max_num){
	    UserSession userSession = UserSession.get();
	    AtRelationsListResp resp = this.messageCheckService.getAtList(current_page,max_num,userSession.getUserId());
	    ModelAndView mav = new ModelAndView("jsonView");
	    mav.addObject("model", resp);
	    return mav;
	}
} 
