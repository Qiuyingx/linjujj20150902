package cn.dayuanzi.controller;




import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import cn.dayuanzi.config.InvitationConfig;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.ActivityListResp;
import cn.dayuanzi.resp.ActivityResp;
import cn.dayuanzi.resp.ContactsResp;
import cn.dayuanzi.resp.ContentEntityListResp;
import cn.dayuanzi.resp.ContentEntityResp;
import cn.dayuanzi.resp.InvitationListResp;
import cn.dayuanzi.resp.LaudListResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.UserPostListResp;
import cn.dayuanzi.service.IActivityService;
import cn.dayuanzi.service.IContentEntityService;
import cn.dayuanzi.service.ICourtYardService;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.IUserCollectService;
import cn.dayuanzi.service.IUserPostService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.service.IHelpPostService;
import cn.dayuanzi.service.impl.ServiceRegistry;
import cn.dayuanzi.util.DateTimeUtil;




/**
 * 院子相关
 * 
 * @author : leihc
 * @date : 2015年4月16日 下午9:05:25
 * @version : 1.0
 */
@Controller
public class CourtyardController {

	public static Logger logger = LoggerFactory.getLogger(CourtyardController.class);
	
	@Autowired
	private IInvitationService invitationService;
	@Autowired
	private IUserPostService userPostService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IHelpPostService helpPostService;
	@Autowired
	private IUserCollectService userCollectService;
	@Autowired
	private ICourtYardService courtYardService;
	@Autowired
	private IActivityService activityService;
	@Autowired
	private IContentEntityService contentEntityService;
	
	
	
	/**
	 * 附近的活动
	 * 
	 * @param current_page 当前第几页
	 * @param max_num 数量
	 * @return
	 */
	@RequestMapping(value = "invitationList.do")
	public ModelAndView invitationList(Integer current_page, Integer max_num){
		UserSession userSession = UserSession.get();
		InvitationListResp resp = invitationService.findInvitationsforCourtyard(userSession.getCourtyardId(), current_page, max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 
	 * 喜欢邀约&点赞分享
	 * 
	 * @param contentType   1 邀约 2 分享&邻居帮帮
	 * @param targetId   帖子id
	 * @return
	 */
	@RequestMapping(value = "likeIt.do")
	public ModelAndView likeIt(int contentType, long targetId){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}

		if(contentType!=1 && contentType!=2){
			throw new GeneralLogicException("参数错误");
		}
		if(contentType==1){
			invitationService.addLikePeopleForInvitation(userSession.getCourtyardId(),userSession.getUserId(), targetId);
		}else{
			userPostService.laudPost(userSession.getUserId(), targetId);
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 邀约详情
	 * 
	 * @param invitationId  邀约的id
	 * @return
	 */
	@RequestMapping(value = "invitationDetail.do")
	public ModelAndView invitationDetail(Long invitationId){
		//注释原因   用户在注册时选择的院子  与认证的院子不匹配  ，并在这过程中 对选择的院子中的帖子评论或者被人回复时，认证到其他院子会提示无法查看
		//if(userSession.isValidatedUser() && userSession.getCourtyardId()!=invitation.getCourtyard_id()){
			//throw new GeneralLogicException("您不能查看这条邀约哦");
		//}
		Resp resp = this.invitationService.getInvitationDetails(invitationId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 分享详情，邻居帮帮详情
	 * @param postId  详情id
	 * @return
	 */
	@RequestMapping(value = "postDetail.do")
	public ModelAndView postDetail(Long postId){
		Resp resp = this.userPostService.getPostDetail(postId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 获取当前用户所在院子的互助列表(取消)
	 * 
	 * @param current_page
	 * @param max_num
	 * @return
	 */
//	@RequestMapping(value = "helpPostList.do")
//	public ModelAndView helpPostList(Integer current_page, Integer max_num){
//		UserSession userSession = UserSession.get();
//		List<UserPost> helpPosts = null;
//		helpPosts = this.helpPostService.getHelpPostList(userSession.getUserId(), current_page, max_num);
//		HelpPostListResp resp = new HelpPostListResp();
//		if(CollectionUtils.isNotEmpty(helpPosts)){
//			for(UserPost helpPost : helpPosts){
//				HelpPostDto dto = new HelpPostDto(helpPost);
//				dto.setReplyAmount((int)helpPostService.countReply(helpPost.getId()));
//				resp.getLists().add(dto);
//			}
//		}
//		ModelAndView mav = new ModelAndView("jsonView");
//		mav.addObject("model", resp);
//		return mav;
//	}
	
	/**
	 * 
	 * 互助详情  与普通帖子的区别是没有赞  只有评论 所以不返回赞的数量和(取消)
	 */
//	@RequestMapping(value = "helpPostDetail.do")
//	public ModelAndView helpPostDetail(Long postId){
//		UserPost helpPost = this.helpPostService.getHelpPost(postId);//收到这个请求消息 
//		if(helpPost == null){  //判断是否存在
//			throw new GeneralLogicException("没有这条互助消息哦。");
//		}
//		User sender=this.userService.findUserById(helpPost.getUser_id()) ; //通过获得的帖子id  查找发帖人
//		PostDetailResp resp = new PostDetailResp(sender,helpPost);                    //返回帖子详情
//		resp.setReplyAmount(this.helpPostService.countReply(postId));      //评论数量
//		List<HelpReply> replys = this.helpPostService.findReplys(postId);  //查询回复该帖子的详情
//		if(CollectionUtils.isNotEmpty(replys)){
//			List<ReplyDto> replyDtos = new ArrayList<ReplyDto>();
//			for(HelpReply reply : replys){     //这里已经更正为显示互助 回复列表
//				ReplyDto dto = new ReplyDto(reply);
//				User replyer = this.userService.findUserById(reply.getReplyer_id());
//				dto.setSenderHeadIcon(replyer.getHead_icon());
//				dto.setSenderName(replyer.getNickname());
//				replyDtos.add(dto);
//			}
//			resp.setReplys(replyDtos);
//		}
//		ModelAndView mav = new ModelAndView("jsonView");
//		mav.addObject("model", resp);
//		return mav;
//	}
	
	
	 /**
	  *  获取邻居帮帮分享列表
	  * @param contentType    邻居帮帮(2),分享(3)
	  * @param current_page
	  * @param max_num
	  * @param range 距离范围
	  * @return
	  */
	@RequestMapping(value = "getPostList.do")
	public ModelAndView getPostList(int current_page, int max_num, int range){
		UserSession userSession = UserSession.get();
		UserPostListResp resp = userPostService.getUserPostList(userSession.getCourtyardId(), current_page, max_num, range);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 发话题
	 * 
	 * @param title  标题
	 * @param content  内容
	 * @param images  图片
	 * @param myself 是否是自我介绍 
	 * @param showAround 是否周围小区可以看到
	 *
	 * @return
	 */
	@RequestMapping(value = "sendPost.do")
	public ModelAndView sendPost(@RequestParam(required=false)String title, String content, @RequestParam(required=false)CommonsMultipartFile[] images,@RequestParam(required=false) Boolean mySelf
		,boolean showAround){
//		if(contentType!=2&&contentType!=3){
//			throw new GeneralLogicException("参数错误");
//		}
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
//		if(StringUtils.isBlank(title)){
//			throw new GeneralLogicException("给一个标题吧");
//		}
		if(StringUtils.isBlank(content) && (images==null || images.length==0)){
			throw new GeneralLogicException("还是发点什么吧");
		}
		if(StringUtils.isNotBlank(content)&&content.length()>2500){
			throw new GeneralLogicException("内容不能超过2500个字符哦");
		}
		if(images!=null && images.length>9){
			throw new GeneralLogicException("发布图片不能超过9张哦亲");
		}
//		if(mySelf==null){
//			mySelf = false;
//		}
		// 取消自我介绍
		mySelf = false;
		UserPostListResp resp = userPostService.saveUserPost(ContentType.分享.getValue(), userSession.getCourtyardId(),userSession.getUserId(), title, content, images,mySelf,showAround);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	
	/**  
	 *  发邀约
	 * @param invitation_type   邀约类型
	 * @param activityTime  活动时间
	 * @param activityPlace   活动地点
	 * @param content  活动内容
	 * @param images  图片
	 * @return
	 */
	@RequestMapping(value = "sendInvitation.do")
	public ModelAndView sendInvitation(int invitation_type, String activityTime, String activityPlace, String content, @RequestParam(required=false)CommonsMultipartFile[] images,boolean showAround){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		if(!InvitationConfig.getInterests().containsKey(invitation_type)){
			throw new GeneralLogicException("邀约类型错误");
		}
		if(StringUtils.isBlank(activityPlace)){
			throw new GeneralLogicException("地点不能为空");
		}
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("内容不能为空。");
		}
		if(content.length()>2500){
			throw new GeneralLogicException("内容不能超过2500个字符哦");
		}
		if(images!=null && images.length>9){
			throw new GeneralLogicException("发布图片不能超过9张哦亲");
		}
		long parseTime = DateTimeUtil.getTime(activityTime,"MM-dd HH:mm");
		if(parseTime==0l){
			parseTime = DateTimeUtil.getTime(activityTime,"MM月dd日HH点mm分");
		}
		this.invitationService.sendInvitation(userSession.getCourtyardId(), userSession.getUserId(), invitation_type, parseTime, activityPlace, content, images,showAround);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 发布求助
	 * @param title 标题
	 * @param content 帮帮内容
	 * @param isUrgent 是否紧急
	 * @param images 发布的图片
	 * @param tag 标签ID
	 * @param reward 悬赏
	 * @return
	 */
	@RequestMapping(value = "sendHelp.do")
	public ModelAndView sendHelp(@RequestParam(required=false) String title,String content, boolean isUrgent, @RequestParam(required=false)CommonsMultipartFile[] images,int reward, boolean showAround){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
//		if(StringUtils.isBlank(title)){
//			throw new GeneralLogicException("标题不能为空");
//		}
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("内容不能为空");
		}
		if(content.length()>2500){
			throw new GeneralLogicException("内容不能超过2500个字符哦");
		}
		if(images!=null && images.length>9){
			throw new GeneralLogicException("发布图片不能超过9张哦亲");
		}
//		if(TagConfig.getTags().get(tag)==null){
//			throw new GeneralLogicException("选择的标签不存在");
//		}
		if(reward<0){
			throw new GeneralLogicException("悬赏不能小于零哦");
		}
		UserPostListResp resp = this.helpPostService.saveHelpPost(userSession.getCourtyardId(), userSession.getUserId(), 0, content, images, isUrgent?1:0,reward,showAround,title);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	/**
	 * 对自己的帮帮采纳回复者的建议
	 * @param replyId
	 * @return
	 */
	@RequestMapping(value = "acceptHelp.do")
	public ModelAndView acceptHelp(long replyId){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		if(replyId <=0 ){
			throw new GeneralLogicException("帮帮不存在哦");
		}
		this.helpPostService.acceptHelp(userSession.getUserId(), replyId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 加入讨论组
	 * @param invitaionId
	 * @return
	 */
//	@RequestMapping(value = "joinDiscussGroup.do")
	public ModelAndView joinDiscussGroup(long invitaionId){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		this.invitationService.joinDiscussGroup(userSession.getCourtyardId(), userSession.getUserId(), invitaionId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 退出讨论组
	 * 
	 * @param invitaionId
	 * @return
	 */
//	@RequestMapping(value = "exitDiscussGroup.do")
	public ModelAndView exitDiscussGroup(long invitaionId){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		this.invitationService.exitDiscussGroup(userSession.getCourtyardId(), userSession.getUserId(), invitaionId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 *   回复帖子
	 *  
	 * @param contentType  邀约(1),邻居帮帮(2),分享(3),
	 * @param replyType 1 评论帖子 2 回复评论
	 * @param targetId  帖子id 或是评论的id
	 * @param atReplyerId  对谁回复
	 * @param content  内容
	 * @return
	 */
	@RequestMapping(value = "replyPost.do")
	public ModelAndView replyPost(int contentType, int replyType, long targetId, @RequestParam(required=false)Long atReplyerId, String content){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能回复，赶快加入我们吧");
		}
//		if(!userSession.isValidatedUser()){
//			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
//		}
		if(replyType!=1 && replyType!=2){
			throw new GeneralLogicException("参数错误");
		}
		if(ContentType.邀约.getValue()!=contentType && ContentType.分享.getValue()!=contentType && ContentType.邻居帮帮.getValue()!=contentType){
			throw new GeneralLogicException("参数错误");
		}
		// 防止调用报错，初始化一个无效值
		if(replyType==1){
			atReplyerId = 0l;
		}
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("内容不能为空哦");
		}
		if(content.length()>500){
			throw new GeneralLogicException("回复不能超过500个字符");
		}
		if(ContentType.邀约.getValue()==contentType){
			this.invitationService.replyInvitation(userSession.getUserId(), replyType, targetId, atReplyerId, content);
		}else{
			this.userPostService.replyPost(userSession.getUserId(), replyType,targetId, atReplyerId, content);
		}
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 收藏
	 * 
	 * @param collectType 收藏的帖子类型 1 邀约 2 邻居帮帮 3分享
	 * @param targetId 帖子的数据库ID
	 * @return
	 */
	@RequestMapping(value = "collect.do")
	public ModelAndView collect(int collectType, long targetId){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能收藏，赶快加入我们吧");
		}
		// 只能收藏求助和话题
		if(collectType!=2&&collectType!=3){
			throw new GeneralLogicException("参数错误");
		}
		//if(!userSession.isValidatedUser()){
	//		throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
		//}
		userCollectService.collect(userSession.getCourtyardId(), userSession.getUserId(), collectType, targetId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	
	/**
	 * 取消收藏
	 * @param collectType 收藏的帖子类型 1 邀约 2 邻居帮帮 3分享
	 * @param targetId   帖子的数据库ID
	 * @return
	 */
	@RequestMapping(value = "cancelCollect.do")
	public ModelAndView cancelCollect(int collectType, long targetId){
		UserSession userSession = UserSession.get();
		//if(!userSession.isValidatedUser()){
	//		throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
	//	}
		userCollectService.cancelcollect(userSession.getCourtyardId(),userSession.getUserId(), collectType, targetId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 *  举报帖子
	 * @param postId  帖子id
	 * @param postType  帖子类型
	 * @param reportType  举报类型
	 * @param content  内容
	 * @return
	 */
	@RequestMapping(value = "reportPost.do")
	public ModelAndView reportPost(long  postId,int postType, int reportType,String content ){
		UserSession userSession = UserSession.get();
		//if(!userSession.isValidatedUser()){
		//	throw new GeneralLogicException("您还没有进行业主验证，不能操作哦。");
	//	}
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客暂时不能举报，赶快加入我们吧");
		}
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("您想举报些什么呢？");
		}
		userPostService.reportPost(postId, postType,reportType,content,userSession.getCourtyardId(),userSession.getUserId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
			
	}
	
	/**
	 * 删除自己帖子
	 */
	@RequestMapping(value = "deletePost.do")
	public ModelAndView deletePost(long postId,int postType){
		if(postType!=ContentType.邀约.getValue()&&postType!=ContentType.分享.getValue()&&postType!=ContentType.邻居帮帮.getValue()){
			throw new GeneralLogicException("参数错误");
		}
		UserSession userSession = UserSession.get();
		userPostService.deletePost(postId, userSession.getUserId(), postType);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}

	/**
	 * 活动列表
	 */
	@RequestMapping(value = "activityList.do")
	public ModelAndView activityList(){
		ActivityListResp resp = this.activityService.getActivityList();
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	

	/**
	 * 活动详情
	 * @param   activityId  活动id
	 * @return
	 */
	@RequestMapping(value = "activityDetail.do")
	public ModelAndView activityDetail(long activityId){
		ActivityResp resp = this.activityService.getActivityDetail(activityId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}

	/**
	 * 活动报名 
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "activitySignUp.do")
	public ModelAndView activitySignUp(long activityId,String name,String tel,String content){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		activityService.activitySignUp(activityId,userSession.getUserId(),name,tel,content);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 获取报名该活动的人
	 * @param activityId
	 * @return
	 */
	@RequestMapping(value = "signUpList.do")
	public ModelAndView signUpList(long activityId){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		ContactsResp resp = activityService.getSignUpUsers(activityId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 活动里面评论活动和回复
	 *
	 * @param targetId    如果评论传 活动id  ,回复就穿评论id
	 * @param replyType 1.评论   2.回复
	 * @param content  内容
	 * @param atReplyerId  对回复评论
	 * @return
	 */
	@RequestMapping(value = "replyActivity.do")
	public ModelAndView  replyActivity(long targetId,long replyType,String content, @RequestParam(required=false)Long atReplyerId){
		UserSession userSession = UserSession.get();
		if(replyType!=1 && replyType!=2){
			throw new GeneralLogicException("参数错误");
		}
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("内容不能为空哦");
		}
		if(content.length()>500){
			throw new GeneralLogicException("回复不能超过500个字符");
		}
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		// 初始化一个值
		if(atReplyerId == null){
			atReplyerId = 0l;
		}
		this.activityService.replyActivity(targetId,userSession.getUserId(),replyType,content,atReplyerId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 感谢
	 * @param replyId 评论的ID
	 * @return
	 */
	@RequestMapping(value = "thankReply.do")
	public ModelAndView thankReply(long replyId){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		this.userService.thankReply(userSession.getUserId(), userSession.getCourtyardId(), replyId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 活动点赞
	 * @return
	 */
	@RequestMapping(value = "likeActivity.do")
	public ModelAndView  likeActivity(long activityId ){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		this.activityService.likeActibity(userSession.getUserId(), activityId,userSession.getCourtyardId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 选择社区
	 */
	@RequestMapping(value = "selectCommunity.do")
	public ModelAndView selectCommunity(long courtyardId){
		UserSession userSession = UserSession.get();
		userSession.setCourtyardId(courtyardId);
		userSession.setValidate(ServiceRegistry.getValidateUserService().isValidate(userSession.getUserId(), courtyardId));
		userSession.getCourtyardsPerDistance().clear();
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	/**
	 * 获取关注对象的帖子列表，邻友动态
	 * 
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	@RequestMapping(value = "getFollowPostList.do")
	public ModelAndView getFollowPostList(int current_page, int max_num){
		UserSession userSession = UserSession.get();
		UserPostListResp resp = userPostService.getFollowPostList(userSession.getUserId(), current_page, max_num);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 报名邀约
	 * @param invitationId
	 * @return
	 */
	//@RequestMapping(value = "invitationSignUp.do")
	public ModelAndView invitationSignUp(long invitationId){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		this.invitationService.invitationSignUp(userSession.getUserId(), invitationId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	/**
	 * 专题列表
	 */
	@RequestMapping(value = "getContentEntityList.do")
	public ModelAndView getContentEntityList(int current_page, int max_num){
		UserSession userSession = UserSession.get();
		//if(!userSession.isValidatedUser()){
		//	throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		//}
		ContentEntityListResp resp =  this.contentEntityService.getContentEntityList( current_page,  max_num, userSession.getCourtyardId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 专题详情
	 *	
	 * @param ContentId
	 * @return
	 */
	@RequestMapping(value = "getContentEntityDetail.do")
	public ModelAndView getContentEntityDetail(long contentId){
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()){
			throw new GeneralLogicException("您还没有进行业主验证，不能操作哦");
		}
		ContentEntityResp resp =  this.contentEntityService.getContentEntityDetail (contentId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
	
	/**
	 * 专题 评论和回复
	 *
	 * @param targetId    如果专题传 活动id  ,回复就穿评论id
	 * @param replyType 1.评论   2.回复
	 * @param content  内容
	 * @param atReplyerId  对回复评论
	 * @return
	 */
	@RequestMapping(value = "replyContentEntity.do")
	public ModelAndView  replyContentEntity(long targetId,long replyType,String content, @RequestParam(required=false)Long atReplyerId){
		UserSession userSession = UserSession.get();
		if(replyType!=1 && replyType!=2){
			throw new GeneralLogicException("参数错误");
		}
		if(StringUtils.isBlank(content)){
			throw new GeneralLogicException("内容不能为空哦");
		}
		if(content.length()>500){
			throw new GeneralLogicException("回复不能超过500个字符");
		}
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		// 初始化一个值
		if(atReplyerId == null){
			atReplyerId = 0l;
		}
		this.contentEntityService.replycontentEntity(targetId,userSession.getUserId(),replyType,content,atReplyerId);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	
	/**
	 * 专题喜欢
	 * @param contentId
	 * @return
	 */
	@RequestMapping(value = "likeContentEntity.do")
	public ModelAndView  likeContentEntity(long contentId ){
		UserSession userSession = UserSession.get();
		if(userSession.isVisitor()){
			throw new GeneralLogicException("访客不能操作哦，赶快加入我们吧");
		}
		this.contentEntityService.likecontentEntity(userSession.getUserId(), contentId,userSession.getCourtyardId());
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", new Resp());
		return mav;
	}
	
	
	/**
	 *  点赞列表 
	 * @param current_page
	 * @param max_num
	 * @param postId
	 * @param type 邀约(1)，帮帮(2),话题(3),活动(4),专题(5)，学堂(6)
	 * @return
	 */
	@RequestMapping(value = "getLaudList.do")
	public ModelAndView getLaudList (int current_page, int max_num,long postId,int type){
	    LaudListResp resp = this.userPostService.getLaudList(current_page, max_num,postId,type);
	    ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("model", resp);
		return mav;
	}
}
