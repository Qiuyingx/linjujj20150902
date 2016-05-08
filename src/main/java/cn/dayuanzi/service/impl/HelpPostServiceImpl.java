package cn.dayuanzi.service.impl;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.config.ExpInfo;
import cn.dayuanzi.config.Settings;
import cn.dayuanzi.dao.AtRelationsDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.ExpDetailDao;
import cn.dayuanzi.dao.HouseOwnersDao;
import cn.dayuanzi.dao.LindouDetailDao;
import cn.dayuanzi.dao.PostReplyDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserPostDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.ExpDetail;
import cn.dayuanzi.model.LindouDetail;
import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserDaily;
import cn.dayuanzi.model.UserLinDou;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.ThingsAdder;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.UserPostListResp;
import cn.dayuanzi.resp.dto.PostDetailDto;
import cn.dayuanzi.service.IHelpPostService;
import cn.dayuanzi.service.INoticeService;
import cn.dayuanzi.service.IUserCollectService;
import cn.dayuanzi.service.IUserPostService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.ImageUtil;
import cn.dayuanzi.util.YardUtils;



@Service
public class HelpPostServiceImpl implements IHelpPostService{

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserPostDao userPostDao;
	@Autowired
	private PostReplyDao postReplyDao;
	@Autowired
	private HouseOwnersDao houseOwnersDao;
	@Autowired
	private LindouDetailDao lindouDetailDao;
	@Autowired
	private ExpDetailDao expDetailDao;
	@Autowired
	private INoticeService noticeService;
	@Autowired
	private AtRelationsDao atRelationsDao;
	@Autowired
	private CourtYardDao courtyardDao;
	@Autowired
	private IUserPostService userPostService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserCollectService userCollectService;
	
	/**
	 * 根据帖子ID获取求助详情
	 * 
	 * @param postId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public UserPost getHelpPost(long postId){
		return this.userPostDao.get(postId);
	}
	/**
	 * 获取指定用户发布的帖子
	 * 
	 * @param userId
	 * @param current_page
	 * @param max_num
	 * @return
	 */
//	@Override
//	@Transactional(readOnly = true)
//	public List<UserPost> getHelpPostList(long user_id, int current_page, int max_num){  
//		User user = this.userDao.get(user_id);
//		Map<String, Object> conditions = new HashMap<String, Object>();
//		conditions.put("courtyard_id", user.getCourtyard_id());
//		conditions.put("content_type", ContentType.邻居帮帮.getValue());
//		return userPostDao.findForPage(current_page, max_num, conditions, "create_time", false);
//	}
	
	/**
	 * 发新的互助贴
	 * 
	 * @param yardId
	 * @param userId
	 * @param title
	 * @param content
	 */
	
	@Override
	@Transactional(readOnly = false)
	public UserPostListResp saveHelpPost(long yardId, long userId, int tag, String content,CommonsMultipartFile[] images,int priority,int reward,boolean showAround,String title){
		User user = this.userDao.get(userId);
		if(user.isBanned()){
			throw new GeneralLogicException("您已被禁言");
		}
		UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(userId);
		UserSession userSession = UserSession.get();
		if(!userSession.isValidatedUser()&&userDaily.getSend_post_count()>0){
			throw new GeneralLogicException("未验证用户只能发一条哦");
		}
		if(reward > 0){
			UserLinDou userLinDou = ServiceRegistry.getUserService().getUserLinDou(userId);
			if(userLinDou.getAmount()<reward){
				throw new GeneralLogicException("您的邻豆不够啦");
			}
			// 扣除邻豆
			userLinDou.used(reward);
			this.lindouDetailDao.save(new LindouDetail(userId, -reward, "帮帮悬赏"));
		}
		List<User> atUsers = YardUtils.findAt(content);
		
		UserPost helpPost = new UserPost(yardId,userId,tag, content);
		helpPost.setContent_type(ContentType.邻居帮帮.getValue());
		helpPost.setPriority(priority);
		helpPost.setReward(reward);
		helpPost.setMyself(false);
		helpPost.setShow_around(showAround);
		if(title!=null){
			helpPost.setTitle(title);
		}
		this.userPostDao.saveOrUpdate(helpPost);
		if(images!=null){
			helpPost.setImage_names(ImageUtil.savePostImage(helpPost.getId(), Settings.POST_IMAGE_DIRE, images));
		}
		ExpInfo info = ThingsAdder.发帮帮.getExpInfo();
		if(userDaily.getSend_help_count() < info.getLimitDaily()){
			user.addExp(info.getExp());
			ExpDetail exp = new ExpDetail(userId,info.getExp(),info.getRemark());
			this.expDetailDao.save(exp);
			userDaily.setSend_help_count(userDaily.getSend_help_count()+1);
		}
		if(atUsers!=null){
			for(User u : atUsers){
				AtRelations ar = new AtRelations(userId, u);
				ar.setScene(ContentType.邻居帮帮.getValue());
				ar.setAppend(helpPost.getId());
				this.atRelationsDao.save(ar);
			}
		}
		UserPostListResp resp = new UserPostListResp();
		this.postDetail(resp, helpPost, false, false);
		return resp;
	}
	

	/**
	 * @see cn.dayuanzi.service.IHelpPostService#acceptHelp(long, long)
	 */
	@Override
	@Transactional
	public void acceptHelp(long userId, long replyId) {
		PostReply postReply = postReplyDao.get(replyId);
		// 评论不存在，或者回复的不是帮帮，或者发帮帮的不是本人
		if(postReply == null||postReply.getContent_type()!=ContentType.邻居帮帮.getValue()||postReply.getPost_sender_id()!=userId){
			throw new GeneralLogicException("评论不存在哦");
		}
		// 只有第一级评论可以采纳
		if(postReply.getReply_id() > 0){
			throw new GeneralLogicException("出错了");
		}
		UserPost helpPost = this.userPostDao.get(postReply.getPost_id());
		if(helpPost.getAcceptId() > 0){
			throw new GeneralLogicException("已经采纳过啦");
		}
		helpPost.setAcceptId(replyId);
		postReply.setAccepted(true);
		int lindouAmount = helpPost.getReward()+ThingsAdder.意见被采纳.getLindouInfo().getLindou();
		// 给回复者发送邻豆
		ServiceRegistry.getUserService().addLindou(postReply.getReplyer_id(), lindouAmount, "帮帮采纳");
		// 给回复者增加经验
		ExpInfo info = ThingsAdder.意见被采纳.getExpInfo();
		User user = this.userDao.get(postReply.getReplyer_id());
		user.addExp(info.getExp());
		if(info.getExp() > 0){
			ExpDetail exp = new ExpDetail(user.getId(),info.getExp(),info.getRemark());
			this.expDetailDao.save(exp);
		}
		UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(postReply.getReplyer_id());
		userDaily.setAccepted_count(userDaily.getAccepted_count()+1);
		String answer = postReply.getContent().length()>20?postReply.getContent().substring(0, 20)+"...":postReply.getContent();
		String content = "你的答案“{0}”被采纳，获得发帖人赠送{1}个邻豆，获得系统赠送{2}个邻豆，{3}个经验值";
		content = MessageFormat.format(content, answer,helpPost.getReward(),ThingsAdder.意见被采纳.getLindouInfo().getLindou(),info.getExp());
		noticeService.sendNoticeToUser(NoticeType.采纳通知, user.getId(), NoticeType.采纳通知.getTitle(),content ,postReply.getPost_id());
	}
	
	private void postDetail(UserPostListResp resp, UserPost userPost, boolean isTop,boolean isHot){
		User sender = this.userService.findUserById(userPost.getUser_id());
		PostDetailDto postDetail = new PostDetailDto(sender, userPost);
		if(StringUtils.isNotBlank(userPost.getContent())&&userPost.getContent().contains("@")){
			postDetail.setAts(this.atRelationsDao.findAtTargets(ContentType.getInstance(userPost.getContent_type()), userPost.getId()));
		}
		CourtYard yard = this.courtyardDao.get(userPost.getCourtyard_id());
		postDetail.setCourtyardName(yard.getCourtyard_name());
		postDetail.setLaudAmount(this.userPostService.countLaud(userPost.getId()));
		postDetail.setReplyAmount(this.userPostService.countReply(userPost.getId(),userPost.getContent_type()));
		if(!UserSession.get().isVisitor()){
			postDetail.setCollected(userCollectService.findUserCollect(UserSession.get().getUserId(), userPost.getContent_type(), userPost.getId())!=null);
			postDetail.setLauded(this.userPostService.findLaud(UserSession.get().getUserId(), userPost.getId())!=null);
		}else{
			postDetail.setCollected(false);
			postDetail.setLauded(false);
		}
		
		
		if(isTop){
			resp.getTops().add(postDetail);
		}else{
			if(isHot){
				postDetail.setHot(true);
			}
			resp.getDatas().add(postDetail);
		}
	}
}
