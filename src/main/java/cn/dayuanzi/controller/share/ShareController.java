package cn.dayuanzi.controller.share;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.ActivityResp;
import cn.dayuanzi.resp.ContentEntityResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.TrainDetailResp;
import cn.dayuanzi.service.IActivityService;
import cn.dayuanzi.service.IContentEntityService;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.IShopService;
import cn.dayuanzi.service.IUserPostService;
import cn.dayuanzi.service.IUserService;


/**
 * 
 * @author : leihc
 * @date : 2015年6月13日
 * @version : 1.0
 */
@Controller
public class ShareController {

	@Autowired
	private IUserPostService userPostService;
	@Autowired
	private IInvitationService invitationService;
	@Autowired
	private IActivityService activityService;
	@Autowired
	private IContentEntityService contentEntityService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IShopService shopService;
	
	
	/**
	 * 分享话题，帮帮
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "sharePost.do")
	public ModelAndView sharePost(long postId){
		Resp resp = this.userPostService.sharePost(postId);
		ModelAndView mav = new ModelAndView("post");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 分享邀约
	 * @param postId
	 * @return
	 */
	@RequestMapping(value = "shareInvitation.do")
	public ModelAndView shareInvitation(long postId){
		Resp resp = this.invitationService.shareInvitation(postId);
		ModelAndView mav = new ModelAndView("invitation");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 分享活动
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "shareActivity.do")
	public ModelAndView shareActivity(long activityId,long courtyardId){
		ActivityResp resp = this.activityService.shareActivity(activityId,courtyardId);
		ModelAndView mav = new ModelAndView("activity");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 分享专题
	 */
	@RequestMapping(value = "shareContentEntity.do")
	public ModelAndView shareContentEntity(long contentId){
	    ContentEntityResp resp =this.contentEntityService.shareContentEntityDetail(contentId);
		ModelAndView mav = new ModelAndView("content");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 分享邀请码
	 */
	@RequestMapping(value = "shareUser.do")
	public ModelAndView shareUser(long userId){
	    Resp resp = this.userService.shareUser(userId);
		ModelAndView mav = new ModelAndView("code");
		mav.addObject("model", resp );
		return mav;
	}
	
	/**
	 * 分享学堂
	 */
	@RequestMapping(value = "shareTrain.do")
	public ModelAndView trainDetail (long trainId){
		UserSession userSession = UserSession.get();
		TrainDetailResp resp = this.shopService.traindetail(trainId,userSession.getUserId());
	    ModelAndView mav = new ModelAndView("train");
		mav.addObject("model",resp);
		return mav;
	}
	
}
