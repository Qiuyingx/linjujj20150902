package cn.dayuanzi.controller;


import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.DatasResp;
import cn.dayuanzi.resp.MainPageResp;
import cn.dayuanzi.resp.MyHouseResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.TaskResp;
import cn.dayuanzi.resp.VersionCheckeResp;
import cn.dayuanzi.service.IContentEntityService;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.IShopService;
import cn.dayuanzi.service.IUserCollectService;
import cn.dayuanzi.service.IUserPostService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.YardUtils;

/**
 * 
 * 我的主页上相关操作
 * 
 * @author : leihc
 * @date : 2015年4月24日 下午2:47:23
 * @version : 1.0
 */
@Controller
public class MineController {

	
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserCollectService userCollectService;
	@Autowired
	private IInvitationService invitationService;
	@Autowired
	private IUserPostService userPostService;
	@Autowired
	private IShopService shopService;
	@Autowired
	private IContentEntityService contentEntityService;

		
	/**
	 * 个人主页
	 * @param userId  用户id
	 * @return
	 */
	@RequestMapping(value = "getMainPage.do")
	public ModelAndView getMainPage(long userId){
		MainPageResp resp = this.userService.getMainPage(userId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}	
	
	/**
	 * 主页背景图图片
	 */
	@RequestMapping(value = "setMainPageImage.do")
	public ModelAndView setMainPageImage(@RequestParam(value="image")CommonsMultipartFile[] image){
	    if(image.length <=0){
		throw new GeneralLogicException("请上传图片");
	    }
	    if(image.length > 1){
		throw new GeneralLogicException("只能上传一张图片作为背景哦");
	    }
	    UserSession userSession = UserSession.get();
	    this.userService.setMainPageImage(image,userSession.getUserId());
	    ModelAndView mav = new ModelAndView("jsonView");
	    mav.addObject("model", new Resp());
	    return mav;
	}
	
	/**
	 * 我
	 * @return
	 */
	@RequestMapping(value = "main.do")
	public ModelAndView main(){
		Resp resp = this.userService.getMain(UserSession.get().getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 我的房屋
	 * 
	 * @return
	 */
	@RequestMapping(value = "getMyHouse.do")
	public ModelAndView getMyHouse(){
		UserSession userSession = UserSession.get();
		//if(!userSession.isValidatedUser()){
		//throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
		//}
		MyHouseResp resp = this.userService.getMyHouse(userSession.getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 我的收藏
	 * 
	 * @param collectType 收藏类型 1 邀约 2 邻居帮帮 3 分享
	 * @param current_page 
	 * @param max_num
	 * @return
	 */
	@RequestMapping(value = "getCollect.do")
	public ModelAndView getCollect(int current_page, int max_num){
		UserSession userSession = UserSession.get();
		Resp resp = userCollectService.getPostCollect(userSession.getCourtyardId(), userSession.getUserId(), current_page, max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获取个人帖子
	 * 
	 * @param userId
	 * @param postType 类型 1 邀约 2 邻居帮帮&分享
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	@RequestMapping(value = "getPersonalPost.do")
	public ModelAndView getPersonalPost(long userId, int postType, int current_page, int max_num){
		Resp resp = null;
		if(postType == ContentType.邀约.getValue()){
			resp = this.invitationService.findInvitationsUser(userId, current_page, max_num);
		}else if(postType == 2){
			resp = userPostService.getUserPostListForUser(userId,current_page, max_num);
		}else{
			throw new GeneralLogicException("参数错误");
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 意见反馈
	 * @param content
	 * @return
	 */
	@RequestMapping(value = "setUserFeedback.do")
	public ModelAndView setuserFeedback(String content,@RequestParam(required=false)CommonsMultipartFile[] images){
		UserSession userSession = UserSession.get();
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("您不说点什么吗？");
		}
		userService.setUserFeedback(userSession.getUserId(), userSession.getCourtyardId(),content,images);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 退出登录
	 * @param session 
	 * 
	 */
	@RequestMapping(value = "logoff.do")
	public ModelAndView logoff(HttpSession session){
		session.invalidate();
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 检查版本
	 */
	@RequestMapping(value = "versionChecking.do")
	public ModelAndView versionChecking(){
		VersionCheckeResp resp = userService.versionChecking();
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}

	/**
	 * 用户推送设置
	 * @param reply  回复推送
	 * @param laud  点赞推送
	 * @param message  消息推送
	 * @param system  系统推送
	 * @return
	 */
	@RequestMapping(value = "userSet.do")
	public ModelAndView userSet(boolean reply ,boolean laud,boolean message,boolean system){
		if(UserSession.get().isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		userService.userSetting(reply,laud,message,system);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 每日签到
	 * 
	 * @return
	 */
	@RequestMapping(value = "checkDaily.do")
	public ModelAndView checkDaily(){
		if(UserSession.get().isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		userService.checkDaily(UserSession.get().getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	} 
	
	/**
	 * 返回我的任务
	 * @return
	 */
	@RequestMapping(value = "getMyTask.do")
	public ModelAndView getMyTask(){
		UserSession userSession = UserSession.get();
		//if(!userSession.isValidatedUser()){
		//throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
		//}
		TaskResp resp = this.userService.getMyTask(userSession.getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获取我的邻豆获取列表详情
	 * @param current_page 
	 * @param max_num
	 * @return
	 */
	@RequestMapping(value = "getLindouDetail.do")
	public ModelAndView getLindouDetail(Integer current_page, Integer max_num){
		if(UserSession.get().isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		Resp resp = this.userService.getLindouDetail(UserSession.get().getUserId(),current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 我的经验值列表详情
	 */
	@RequestMapping(value = "getExpDetail.do")
	public ModelAndView getExpDetail(Integer current_page, Integer max_num){
		if(UserSession.get().isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		Resp resp = this.userService.getExpDetail(UserSession.get().getUserId(),current_page,max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 拉黑用户
	 * 
	 * @param targetId
	 * @param concel true 取消拉黑 false 拉黑
	 * @return
	 */
	@RequestMapping(value = "blackList.do")
	public ModelAndView putUserBlackList(long targetId, boolean concel){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		this.userService.putUserBlackList(userSession.getUserId(), targetId,concel);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 关注某人
	 * 
	 * @param targetId
	 * @param concel true 取消关注 false 加关注
	 * @return
	 */
	@RequestMapping(value = "follow.do")
	public ModelAndView follow(long targetId,boolean concel){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		this.userService.followUser(userSession.getUserId(), targetId,concel);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 技能认证
	 * @param skillTag 技能标签
	 * @param content 个人介绍
	 * @param tel
	 * @param images
	 * @return
	 */
	@RequestMapping(value = "applyStar.do")
	public ModelAndView applyStar(String skillTag, String content, String tel,@RequestParam(required=false)CommonsMultipartFile[] images){

		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		if(StringUtils.isBlank(skillTag)){
			throw new GeneralLogicException("请填写技能");
		}

		if(StringUtils.isBlank(content) && (images==null || images.length==0)){
			throw new GeneralLogicException("请输入内容");
		}
		if(StringUtils.isNotBlank(content)&&content.length()>2500){
			throw new GeneralLogicException("内容不能超过2500个字符哦");
		}
		if(images!=null && images.length>6){
			throw new GeneralLogicException("发布图片不能超过6张哦亲");
		}
		if(StringUtils.isBlank(tel)){
			throw new GeneralLogicException("请输入手机号码");
		}
		if(!YardUtils.isValidTel(tel)){
			throw new GeneralLogicException("请输入正确的手机号码");
		}
		this.userService.applyStar(userSession.getUserId(), tel, skillTag,content, images);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 推荐的技能达人
	 * @return
	 */
	@RequestMapping(value = "starList.do")
	public ModelAndView userStarList(Integer current_page, Integer max_num){
		UserSession userSession = UserSession.get();
		DatasResp resp = this.userService.findUserStars(userSession.getUserId(), current_page, max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 搜索学堂，或专题
	 * @param type 1 学堂 2 专题
	 * @param keys 搜索关键字
	 * @return
	 */
	@RequestMapping(value = "searchContent.do")
	public ModelAndView searchContent(int type,String keys){
		UserSession userSession = UserSession.get();
		Resp resp = null;
		if(type == 1){
			resp = shopService.searchTrain(userSession.getCourtyardId(), keys);
		}else{
			resp = contentEntityService.searchContentEntity(userSession.getCourtyardId(), keys);
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
}
