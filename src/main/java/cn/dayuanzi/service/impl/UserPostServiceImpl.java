package cn.dayuanzi.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.config.CareerConfig;
import cn.dayuanzi.config.ExpInfo;
import cn.dayuanzi.config.InterestConf;
import cn.dayuanzi.config.Settings;
import cn.dayuanzi.dao.ActivityInfoDao;
import cn.dayuanzi.dao.ActivityLaudDao;
import cn.dayuanzi.dao.AtRelationsDao;
import cn.dayuanzi.dao.BlackListDao;
import cn.dayuanzi.dao.ContentEntityDao;
import cn.dayuanzi.dao.ContentEntityLaudDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.ExpDetailDao;
import cn.dayuanzi.dao.HouseOwnersDao;
import cn.dayuanzi.dao.InvitationDao;
import cn.dayuanzi.dao.LikeInvitationMembersDao;
import cn.dayuanzi.dao.PostLaudDao;
import cn.dayuanzi.dao.PostReplyDao;
import cn.dayuanzi.dao.ThankReplyDao;
import cn.dayuanzi.dao.TopPostDao;
import cn.dayuanzi.dao.TrainInfoDao;
import cn.dayuanzi.dao.TrainLaudDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserFollowDao;
import cn.dayuanzi.dao.UserInterestDao;
import cn.dayuanzi.dao.UserPostDao;
import cn.dayuanzi.dao.UserPostViewDao;
import cn.dayuanzi.dao.UserReportDao;
import cn.dayuanzi.dao.UserStarDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.ExpDetail;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.MessageStatus;
import cn.dayuanzi.model.PostLaud;
import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.model.PushMessageStatus;
import cn.dayuanzi.model.ThankReply;
import cn.dayuanzi.model.TopPost;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserDaily;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.model.UserReport;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.model.UserStar;
import cn.dayuanzi.model.view.UserPostView;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.pojo.ThingsAdder;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.LaudListResp;
import cn.dayuanzi.resp.PostDetailResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.UserPostListResp;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.resp.dto.PostDetailDto;
import cn.dayuanzi.resp.dto.ReplyDto;
import cn.dayuanzi.service.IMessageCheckService;
import cn.dayuanzi.service.IRedisService;
import cn.dayuanzi.service.IUserCollectService;
import cn.dayuanzi.service.IUserPostService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.ApnsUtil;
import cn.dayuanzi.util.GeoRange;
import cn.dayuanzi.util.ImageUtil;
import cn.dayuanzi.util.YardUtils;

/**
 * 
 * @author : leihc
 * @date : 2015年4月16日 下午9:13:10
 * @version : 1.0
 */
@Service
public class UserPostServiceImpl implements IUserPostService {

	@Autowired
	private UserPostDao userPostDao;
	@Autowired
	private PostLaudDao postLaudDao;
	@Autowired
	private PostReplyDao postReplyDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private HouseOwnersDao houseOwnersDao;
	@Autowired
	private UserReportDao userReportDao;
	@Autowired
	private InvitationDao invitationDao;
	@Autowired
	private LikeInvitationMembersDao invitationInterestDao;
	@Autowired
	private ThankReplyDao thankReplyDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserCollectService userCollectService;
	@Autowired
	private ExpDetailDao expDetailDao;
	@Autowired
	private CourtYardDao courtyardDao;
	@Autowired
	private UserFollowDao userFollowDao;
	@Autowired
	private BlackListDao blackListDao;
	@Autowired
	private IRedisService redisService;
	@Autowired
	private IMessageCheckService messageCheckService;
	@Autowired 
	private UserStarDao userStarDao;
	@Autowired
	private UserInterestDao userInterestDao;
	@Autowired
	private AtRelationsDao atRelationsDao;
	@Autowired
	private UserPostViewDao userPostViewDao;
	@Autowired
	private TopPostDao topPostDao;
	@Autowired
	private ContentEntityLaudDao contentEntityLaudDao;
	@Autowired
	private ActivityLaudDao  activityLaudDao;
	@Autowired
	private TrainLaudDao trainLaudDao;
	@Autowired
	private ContentEntityDao contentEntityDao;
	@Autowired
	private ActivityInfoDao  activityInfoDao;
	@Autowired
	private TrainInfoDao trainInfoDao;
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#getUserPost(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserPost getUserPost(long postId) {
		return this.userPostDao.get(postId);
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#getUserPostList(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserPostListResp getUserPostListForUser(long userId,int current_page, int max_num) {

		if(userId!=UserSession.get().getUserId()){
			if(blackListDao.isBlackList(userId, UserSession.get().getUserId())){
				throw new GeneralLogicException("您不能查看此人的话题哦。");
			}
		}
		
		List<UserPost> userPosts = userPostDao.findForPage(current_page, max_num, "create_time", false,Restrictions.eq("user_id", userId));
		UserPostListResp resp = new UserPostListResp();
		for(UserPost userPost : userPosts){
			User sender = this.userService.findUserById(userPost.getUser_id());
			PostDetailDto postDetail = new PostDetailDto(sender, userPost);
			CourtYard yard = this.courtyardDao.get(userPost.getCourtyard_id());
			postDetail.setCourtyardName(yard.getCourtyard_name());
			postDetail.setLaudAmount(this.countLaud(userPost.getId()));
			postDetail.setReplyAmount(this.countReply(userPost.getId(),userPost.getContent_type()));
			if(StringUtils.isNotBlank(userPost.getContent())&&userPost.getContent().contains("@")){
				postDetail.setAts(this.atRelationsDao.findAtTargets(ContentType.getInstance(userPost.getContent_type()), userPost.getId()));
			}
			if(!UserSession.get().isVisitor()){
				postDetail.setCollected(userCollectService.findUserCollect(UserSession.get().getUserId(), userPost.getContent_type(), userPost.getId())!=null);
				postDetail.setLauded(this.findLaud(UserSession.get().getUserId(), userPost.getId())!=null);
			}else{
				postDetail.setCollected(false);
				postDetail.setLauded(false);
			}
			resp.getDatas().add(postDetail);
		}
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#getUserPostList(long, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserPostListResp getUserPostList(long courtyardId, int current_page, int max_num, int range) {
		List<UserPost> userPosts = null;
		// 热帖
		UserPostView hotPostView = null;
		// 置顶贴
		List<TopPost> topPostInfo = null;
		if(range < 10){
			CourtYard courtyard = this.courtyardDao.get(courtyardId);
			// 查找指定范围内的社区，不包括用户当前社区，以便于指定范围没有社区时使用=本小区查询条件，而不使用in条件
			if(range>0 && !UserSession.get().getCourtyardsPerDistance().containsKey(range)){
				GeoRange gr = YardUtils.getGetRange(courtyard.getLongitude(), courtyard.getLatitude(), range);
//				List<CourtYard> courtyards = courtyardDao.find(Restrictions.eq("city_id", courtyard.getCity_id()));
				List<Long> courtyardsIds = this.courtyardDao.findCourtyardIds(courtyard.getCity_id(), gr);
				courtyardsIds.remove(courtyard.getId());
//				for(CourtYard c : courtyards){
//					if(c.getId()==courtyardId){
//						continue;
//					}
//					if(YardUtils.getDistance(courtyard.getLongitude(), courtyard.getLatitude(), c.getLongitude(), c.getLatitude()) < range*1000){
//						courtyardsIds.add(c.getId());
//					}
//				}
				if(!courtyardsIds.isEmpty()){
					UserSession.get().getCourtyardsPerDistance().put(range, courtyardsIds);
				}
			}
			List<Long> courtyardIds = UserSession.get().getCourtyardsPerDistance().get(range);
			if(courtyardIds==null||courtyardIds.isEmpty()){
				userPosts = userPostDao.findForPage(current_page, max_num, "create_time", false, Restrictions.eq("courtyard_id", courtyardId));
				hotPostView = this.userPostViewDao.getHotPost(Restrictions.eq("courtyard_id", courtyardId));
			}else{
				// where courtyard_id=courtyardId or courtyard_id in courtyardIds and show_around=true;
				Disjunction dis = Restrictions.disjunction();
				dis.add(Restrictions.eq("courtyard_id", courtyardId));
				Conjunction con = Restrictions.conjunction();
				con.add(Restrictions.in("courtyard_id", courtyardIds));
				con.add(Restrictions.eq("show_around", true));
				dis.add(con);
				userPosts = userPostDao.findForPage(current_page, max_num, "create_time", false, dis);
				hotPostView = this.userPostViewDao.getHotPost(dis);
			}
			topPostInfo = this.topPostDao.getTop(courtyardId, courtyardIds);
		}else{
			// 同城，有多个城市时需要加入城市ID筛选
			Disjunction dis = Restrictions.disjunction();
			dis.add(Restrictions.eq("courtyard_id", courtyardId));
			dis.add(Restrictions.eq("show_around", true));
			userPosts = userPostDao.findForPage(current_page, max_num, "create_time", false, dis);
			hotPostView = this.userPostViewDao.getHotPost(dis);
			topPostInfo = this.topPostDao.getTop(courtyardId);
		}
		
		UserPostListResp resp = new UserPostListResp();
		if(CollectionUtils.isNotEmpty(topPostInfo)){
			for(TopPost tp : topPostInfo){
				Iterator<UserPost> ir = userPosts.iterator();
				while(ir.hasNext()){
					UserPost up = ir.next();
					if(up.getId()==tp.getPost_id()){
						ir.remove();
						break;
					}
				}
				UserPost userPost = this.userPostDao.get(tp.getPost_id());
				this.postDetail(resp, userPost, true,false);
			}
		}
		if(hotPostView!=null){
			Iterator<UserPost> ir = userPosts.iterator();
			while(ir.hasNext()){
				UserPost up = ir.next();
				if(up.getId()==hotPostView.getId()){
					ir.remove();
					break;
				}
			}
			UserPost userPost = this.userPostDao.get(hotPostView.getId());
			this.postDetail(resp, userPost, false,true);
		}

		for(UserPost userPost : userPosts){
			this.postDetail(resp, userPost, false,false);
		}

		redisService.saveLastRange(UserSession.get().getUserId(), range);
//		Map<String, Object> conditions = new HashMap<String, Object>();
//		conditions.put("courtyard_id", courtyardId);
//		conditions.put("content_type", postType.getValue());
//		return userPostDao.findForPage(current_page, max_num, conditions, "create_time", false);
		return resp;
	}
	
	private void postDetail(UserPostListResp resp, UserPost userPost, boolean isTop,boolean isHot){
		User sender = this.userService.findUserById(userPost.getUser_id());
		PostDetailDto postDetail = new PostDetailDto(sender, userPost);
		if(StringUtils.isNotBlank(userPost.getContent())&&userPost.getContent().contains("@")){
			postDetail.setAts(this.atRelationsDao.findAtTargets(ContentType.getInstance(userPost.getContent_type()), userPost.getId()));
		}
		CourtYard yard = this.courtyardDao.get(userPost.getCourtyard_id());
		postDetail.setCourtyardName(yard.getCourtyard_name());
		postDetail.setLaudAmount(this.countLaud(userPost.getId()));
		postDetail.setReplyAmount(this.countReply(userPost.getId(),userPost.getContent_type()));
		if(!UserSession.get().isVisitor()){
			postDetail.setCollected(userCollectService.findUserCollect(UserSession.get().getUserId(), userPost.getContent_type(), userPost.getId())!=null);
			postDetail.setLauded(this.findLaud(UserSession.get().getUserId(), userPost.getId())!=null);
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
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#saveUserPost(long, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public UserPostListResp saveUserPost(int contentType, long yardId, long userId, String title, String content, CommonsMultipartFile[] images,boolean myself,boolean show_around) {
		User user = this.userDao.get(userId);
		if(user.isBanned()){
			throw new GeneralLogicException("您已被禁言");
		}
		UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(userId);
		List<User> atUsers = YardUtils.findAt(content);
		UserPost userPost = new UserPost(yardId,userId, title, content);
		userPost.setContent_type(contentType);
		userPost.setMyself(myself);
		userPost.setShow_around(show_around);
		//userPost.setTopicTag(topicTag);
		this.userPostDao.save(userPost);
		if(images!=null){
			userPost.setImage_names(ImageUtil.savePostImage(userPost.getId(), Settings.POST_IMAGE_DIRE, images));
		}
		if(contentType==ContentType.分享.getValue()){
			ExpInfo info = ThingsAdder.发话题.getExpInfo();
			if(userDaily.getSend_post_count() < info.getLimitDaily()){
				user.addExp(info.getExp());
				ExpDetail exp = new ExpDetail(userId,info.getExp(),info.getRemark());
				this.expDetailDao.save(exp);
				userDaily.setSend_post_count(userDaily.getSend_post_count()+1);
			}
		}
		if(atUsers!=null){
			for(User u : atUsers){
				AtRelations ar = new AtRelations(userId, u);
				ar.setScene(contentType);
				ar.setAppend(userPost.getId());
				this.atRelationsDao.save(ar);
			}
		}
	
		UserPostListResp resp = new UserPostListResp();
		this.postDetail(resp, userPost, false, false);
		return resp;
	}

	/**
	 * @see cn.dayuanzi.service.IUserPostService#countLaud(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public long countLaud(long postId) {
		return this.postLaudDao.count("post_id", postId);
	}
	
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#countReply(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public long countReply(long postId, int contentType) {
		return this.postReplyDao.count(Restrictions.eq("post_id", postId),Restrictions.eq("content_type", contentType));
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#findReplys(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PostReply> findReplys(long postId, int contentType) {
		return postReplyDao.findPostReplys("create_time",false,Restrictions.eq("post_id", postId),Restrictions.eq("content_type", contentType),Restrictions.eq("reply_id", 0l));
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#findReplys(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<PostReply> findReplys(long postId, int contentType, long reply_id) {
		return postReplyDao.findPostReplys("create_time",true,Restrictions.eq("post_id", postId),Restrictions.eq("content_type", contentType),Restrictions.eq("reply_id", reply_id));
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#countNotReadReply(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public long countNotReadReply(long userId, long courtyard_id, long lastReadedReply) {
		// where id > lastReadedReply and courtyard_id=courtyard_id and post_sender_id = userId or at_targetId=userId and replyer_id!=userId
//		Criterion c_id = Restrictions.gt("id", lastReadedReply);
//		Criterion c_courtyardId = Restrictions.eq("courtyard_id", courtyard_id);
//		Disjunction senderIdOrAtTarget = Restrictions.disjunction();
//		senderIdOrAtTarget.add(Restrictions.eq("post_sender_id", userId));
//		senderIdOrAtTarget.add(Restrictions.eq("at_targetId", userId));
//		Criterion c_replyerId = Restrictions.ne("replyer_id", userId);
		List<Long> postIds = this.postReplyDao.getRelatedPostIds(userId);
		if(postIds.isEmpty()){
			return 0;
		}
		Criterion c_id = Restrictions.gt("id", lastReadedReply);
		Criterion c_postId = Restrictions.in("post_id", postIds);
		return postReplyDao.count(c_id,c_postId,Restrictions.ne("replyer_id", userId));

	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#countNotReadLaud(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public long countNotReadLaud(long userId, long courtyard_id, long lastReadedLaud) {
//		return postLaudDao.count(Restrictions.gt("id", lastReadedLaud),Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("post_sender_id", userId),Restrictions.ne("user_id",userId));
//		List<Long> postIds = this.postLaudDao.getRelatedPosts(userId);
//		if(postIds.isEmpty()){
//			return 0;
//		}
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		if(follows.isEmpty()){
			return postLaudDao.count(Restrictions.gt("id", lastReadedLaud),Restrictions.eq("post_sender_id", userId),Restrictions.ne("user_id",userId));
		}else{
			List<Long> postIds = this.postLaudDao.getRelatedPosts(userId);
			if(postIds.isEmpty()){
				return 0;
			}
			Disjunction followOrAtTarget = Restrictions.disjunction();
			followOrAtTarget.add(Restrictions.eq("post_sender_id", userId));
			followOrAtTarget.add(Restrictions.in("user_id", follows));
			return postLaudDao.count(Restrictions.gt("id", lastReadedLaud),Restrictions.in("post_id", postIds),Restrictions.ne("user_id",userId),followOrAtTarget);
		}
	}
	
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#replyPost(long, long, java.lang.String)
	 */
	@Override
	@Transactional
	public void replyPost(long user_id , int replyType, long targetId, long atReplyerId, String content) {
		User user = this.userDao.get(user_id);
		if(user.isBanned()){
			throw new GeneralLogicException("您已被禁言");
		}
		UserPost userPost = null;
		// 评论帖子
		if(replyType==1){
			userPost = this.userPostDao.get(targetId);
			if(userPost == null){
				throw new GeneralLogicException("回复的帖子不存在哦");
			}
			List<User> atUsers = YardUtils.findAt(content);
			
			PostReply reply = new PostReply(user, targetId,userPost.getContent_type(), content);
			reply.setPost_sender_id(userPost.getUser_id());
			this.postReplyDao.save(reply);
			
			if(atUsers!=null){
				for(User u : atUsers){
					AtRelations ar = new AtRelations(user_id, u);
					ar.setScene(ContentType.帖子评论.getValue());
					ar.setAppend(reply.getId());
					this.atRelationsDao.save(ar);
				}
			}
			// 一级评论帮帮得经验
			if(userPost.getContent_type()==ContentType.邻居帮帮.getValue()){
				UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(user_id);
				ExpInfo info = ThingsAdder.帮帮评论.getExpInfo();
				if(userDaily.getHelp_reply_count() < info.getLimitDaily()){
					user.addExp(info.getExp());
					ExpDetail exp = new ExpDetail(user_id,info.getExp(),info.getRemark());
					this.expDetailDao.save(exp);
					userDaily.setHelp_reply_count(userDaily.getHelp_reply_count()+1);
				}
			}
			MessageStatus ms = this.messageCheckService.findMessageCheck(user_id, 0);
//			ms.setLast_readed_reply_id(reply.getId());
			ms.setLast_readed_reply_time(reply.getCreate_time());
			PushMessageStatus pms = this.messageCheckService.findPushMessageCheck(user_id);
//			pms.setLast_readed_reply_id(reply.getId());
			pms.setLast_readed_reply_time(reply.getCreate_time());
			if(!userPost.isRewardLindou()){
				long laudAmount = this.postLaudDao.count(Restrictions.eq("post_id", userPost.getId()),Restrictions.ne("user_id", userPost.getUser_id()));
				long oneLevelReply = this.postReplyDao.count(Restrictions.eq("post_id", userPost.getId()),Restrictions.eq("content_type", userPost.getContent_type()),Restrictions.ne("replyer_id", userPost.getUser_id()),Restrictions.eq("reply_id", 0l));
				if(laudAmount+oneLevelReply>=10){
					ServiceRegistry.getUserService().addLindou(userPost.getUser_id(), 5, "帖子被感谢和评论10次 5个邻豆。");
					userPost.setRewardLindou(true);
					this.userPostDao.save(userPost);
				}
			}

		}else{
			// 回复评论
			PostReply replyTarget = this.postReplyDao.get(targetId);
			if(replyTarget == null){
				throw new GeneralLogicException("回复的评论不存在哦");
			}

			 //评论的回复不能回复
			if(replyTarget.getReply_id() > 0){
				throw new GeneralLogicException("不能回复");
			}
			// 验证回复@对象是否合法
			User atReplyer = this.userDao.get(atReplyerId);
			if(atReplyer==null){
				throw new GeneralLogicException("出了点小问题，稍后试试");
			}
			List<Long> replyerIds = this.postReplyDao.getReplyer(targetId);
			if(atReplyerId!=replyTarget.getReplyer_id() && !replyerIds.contains(atReplyerId)){
				throw new GeneralLogicException("出了点小问题，稍后试试");
			}
			List<User> atUsers = YardUtils.findAt(content);
			
			userPost = this.userPostDao.get(replyTarget.getPost_id());
			PostReply reply = new PostReply(user, userPost.getId(), userPost.getContent_type(),content);
			reply.setPost_sender_id(userPost.getUser_id());
			reply.setAt_targetId(atReplyerId);
			reply.setReply_id(targetId);
			this.postReplyDao.save(reply);
			
			if(atUsers!=null){
				for(User u : atUsers){
					AtRelations ar = new AtRelations(user_id, u);
					ar.setScene(ContentType.帖子评论.getValue());
					ar.setAppend(reply.getId());
					this.atRelationsDao.save(ar);
				}
			}
			MessageStatus ms = this.messageCheckService.findMessageCheck(user_id, 0);
//			ms.setLast_readed_reply_id(reply.getId());
			ms.setLast_readed_reply_time(reply.getCreate_time());
			PushMessageStatus pms = this.messageCheckService.findPushMessageCheck(user_id);
//			pms.setLast_readed_reply_id(reply.getId());
			pms.setLast_readed_reply_time(reply.getCreate_time());
//			User post_sender = this.userDao.get(userPost.getUser_id());
//			if(post_sender.isValidIOSUser()){
//				int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadReply(post_sender.getId(), 1);
//				ApnsUtil.getInstance().send(post_sender, count);
//			}
//			if(atReplyer.isValidIOSUser()){
//				int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadReply(atReplyer.getId(), 1);
//				ApnsUtil.getInstance().send(atReplyer, count);
//			}
		}
		List<Long> temp = this.postReplyDao.getReplyers(userPost.getId(), userPost.getContent_type());
		temp.add(userPost.getUser_id());
		Set<Long> replyerIds = new HashSet<Long>();
		replyerIds.addAll(temp);
		replyerIds.remove(user_id);
		if(!replyerIds.isEmpty()){
			List<User> replyers = this.userDao.get(replyerIds);
			IMessageCheckService messageCheckService = ServiceRegistry.getMessageCheckService();
			for(User replyer : replyers){
				int count = messageCheckService.getAllAndUpdateNotReadReply(replyer.getId(), 1);
				UserSetting setting = this.userService.getUserSetting(replyer.getId());
				if(setting.isReply()){
					ApnsUtil.getInstance().send(replyer, PushType.评论, "你收到一条新的评论",0, count);
				}else{
					ApnsUtil.getInstance().send(replyer, count);
				}
			}
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#laudPost(long, long)
	 */
	@Override
	@Transactional
	public void laudPost(long user_id, long post_id) {
		UserPost userPost = this.userPostDao.get(post_id);
		if(userPost == null){
			throw new GeneralLogicException("木有这个贴~");
		}
		User user = this.userDao.get(user_id);
//		if(user.getCourtyard_id()!=userPost.getCourtyard_id()){
//			if(userPost.getContent_type()==ContentType.邻居帮帮.ordinal())
//				throw new GeneralLogicException("您不能给这条邻居帮帮点赞哦");
//			else{
//				throw new GeneralLogicException("您不能给这条话题点赞哦");
//			}
//		}
		PostLaud postLaud = this.postLaudDao.findUnique(Restrictions.eq("user_id", user_id),Restrictions.eq("post_id", post_id));
		if(postLaud!=null){
			throw new GeneralLogicException("您给这条话题点过赞了。");
		}
		postLaud = new PostLaud(post_id, userPost.getUser_id(),user_id,user.getCourtyard_id());
		this.postLaudDao.save(postLaud);
		
		UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(userPost.getUser_id());
		ExpInfo info = ThingsAdder.被感谢.getExpInfo();
		if(userDaily.getBe_thank_count() < info.getLimitDaily()){
			User replyer = this.userDao.get(userPost.getUser_id());
			replyer.addExp(info.getExp());
			ExpDetail exp = new ExpDetail(replyer.getId(),info.getExp(),info.getRemark());
			this.expDetailDao.save(exp);
			userDaily.setBe_thank_count(userDaily.getBe_thank_count()+1);
		}
		
		MessageStatus ms = this.messageCheckService.findMessageCheck(user_id, 0);
//		ms.setLast_readed_laud_id(postLaud.getId());
		ms.setLast_readed_laud_time(postLaud.getCreate_time());
		PushMessageStatus pms = this.messageCheckService.findPushMessageCheck(user_id);
//		pms.setLast_readed_laud_id(postLaud.getId());
		pms.setLast_readed_laud_time(postLaud.getCreate_time());
		if(!userPost.isRewardLindou()){
			long laudAmount = this.postLaudDao.count(Restrictions.eq("post_id", userPost.getId()),Restrictions.ne("user_id", userPost.getUser_id()));
			long oneLevelReply = this.postReplyDao.count(Restrictions.eq("post_id", userPost.getId()),Restrictions.eq("content_type", userPost.getContent_type()),Restrictions.ne("replyer_id", userPost.getUser_id()),Restrictions.eq("reply_id", 0l));
			if(laudAmount+oneLevelReply>=10){
				ServiceRegistry.getUserService().addLindou(userPost.getUser_id(), 5, "帖子被感谢和评论10次 5个邻豆。");
				userPost.setRewardLindou(true);
				this.userPostDao.save(userPost);
			}
		}
		
		List<Long> temp = this.postLaudDao.getRelatedUsers(post_id);
		temp.add(userPost.getUser_id());
		Set<Long> lauderIds = new HashSet<Long>();
		lauderIds.addAll(temp);
		lauderIds.remove(user_id);
		if(!lauderIds.isEmpty()){
			for(long userId : lauderIds){
				User relateUser = this.userDao.get(userId);
				int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadLaud(userId, 1);
				if(relateUser.isValidIOSUser()){
					UserSetting setting = this.userService.getUserSetting(userId);
					if(setting.isLaud()){
						ApnsUtil.getInstance().send(relateUser, PushType.感谢, "你收到一条新的感谢", 0,count);
					}else{
						ApnsUtil.getInstance().send(relateUser, count);
					}
				}
			}
		}
		
//		User postSender = this.userDao.get(userPost.getUser_id());
//		int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadLaud(userPost.getUser_id(), 1);
//		if(postSender.isValidIOSUser()){
//			UserSetting setting = this.userService.getUserSetting(userPost.getUser_id());
//			if(setting.isLaud())
//				ApnsUtil.getInstance().send(postSender, count);
//		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#findLaud(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public PostLaud findLaud(long userId, long postId) {
		return this.postLaudDao.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("post_id", postId));
	}
	
	@Override
	@Transactional(readOnly = false  )
	public void reportPost(long  postId,int postType ,int reportType,String content ,long courtyardId,long userId){
		if(this.userReportDao.haveReport(postId,postType, userId)){
			throw new GeneralLogicException("您已经举报过了哦");
		}
		UserReport userReport =new UserReport(userId,postType,postId,courtyardId,reportType , content);
		this.userReportDao.save(userReport);
	}

	
	@Override
	@Transactional(readOnly = false  )
	public void deletePost(long postId,long userId,int postType){
		if(postType!=ContentType.邀约.getValue()){
			UserPost userPost = this.userPostDao.get(postId);
			if(userPost==null){
				throw new GeneralLogicException("您要删除的帖子不存在");
			}
			if(userPost.getUser_id()!=userId){
				throw new GeneralLogicException("您不能删除该帖子");
			}
			this.userPostDao.delete(userPost);
			List<Long> replys = this.postReplyDao.removeReply(postId, userPost.getContent_type());
			this.postLaudDao.remove(postId);
			// 删除所有感谢
			this.thankReplyDao.removeAll(replys);
		}else{
			Invitation invitation = this.invitationDao.get(postId);
			if(invitation==null){
				throw new GeneralLogicException("您要删除的邀约不存在");
			}
			if(invitation.getUser_id()!=userId){
				throw new GeneralLogicException("您不能删除该邀约");
			}
			invitationDao.delete(invitation);
			// 删除邀约评论
			List<Long> replys = this.postReplyDao.removeReply(postId, ContentType.邀约.getValue());
			// 删除所有感谢
			this.thankReplyDao.removeAll(replys);
			// 删除邀约点赞
			this.invitationInterestDao.remove(postId);
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#isThankForReply(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isThankForReply(long userId, long replyId) {
		ThankReply thank = this.thankReplyDao.findUnique(Restrictions.eq("reply_id", replyId),Restrictions.eq("user_id", userId));
		return thank!=null;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#countThankAmountForReply(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public int countThankAmountForReply(long replyId) {
		return (int)this.thankReplyDao.count(Restrictions.eq("reply_id", replyId));
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#getPostDetail(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Resp getPostDetail(long postId) {
		UserPost userPost = this.getUserPost(postId);
		if(userPost == null){
			throw new GeneralLogicException("该话题不存在或已被删除了哦");
		}
		UserSession userSession = UserSession.get();
		User sender = this.userService.findUserById(userPost.getUser_id());
		
		PostDetailResp resp = new PostDetailResp(sender, userPost);
		resp.setCourtyarName(this.courtyardDao.get(userPost.getCourtyard_id()).getCourtyard_name());
//		if(StringUtils.isNotBlank(userPost.getContent())&&userPost.getContent().contains("@")){
//			List<String> r = YardUtils.displayAt(userPost.getContent()); 
//			for(String at : r){
//				User u = this.userDao.get(Long.parseLong(at.substring(at.lastIndexOf('{')+1, at.lastIndexOf('}'))));
//				if(u!=null){
//					resp.getAts().add(new AtDto(u));
//				}
//			}
//		}
		if(StringUtils.isNotBlank(userPost.getContent())&&userPost.getContent().contains("@")){
			resp.setAts(this.atRelationsDao.findAtTargets(ContentType.getInstance(userPost.getContent_type()), userPost.getId()));
		}
		resp.setLaudAmount(this.countLaud(postId));
		resp.setReplyAmount(this.countReply(postId,userPost.getContent_type()));
		if(userSession.isVisitor()){
			resp.setCollected(false);
			resp.setLauded(false);
		}else{
//			if(userPost.getContent_type()==ContentType.邻居帮帮.getValue()){
//				resp.setCollected(userCollectService.findUserCollect(userSession.getUserId(), ContentType.邻居帮帮.getValue(), postId)!=null);
//				resp.setLauded(this.findLaud(userSession.getUserId(), postId)!=null);
//			}else{
//				resp.setCollected(userCollectService.findUserCollect(userSession.getUserId(), ContentType.分享.getValue(), postId)!=null);
//				resp.setLauded(this.findLaud(userSession.getUserId(), postId)!=null);
//			}
			resp.setCollected(userCollectService.findUserCollect(userSession.getUserId(), userPost.getContent_type(), postId)!=null);
			resp.setLauded(this.findLaud(userSession.getUserId(), postId)!=null);
		}
		//返回帖子详情的点赞人
		List<Long> userIds = this.postLaudDao.laudPostUsertoo(postId);
		if(CollectionUtils.isNotEmpty(userIds)){
		    List<LaudListDto> laudLists = new ArrayList<LaudListDto>();
		    List<User> users = this.userDao.get(userIds);
		    for(User user : users){
		    	LaudListDto dto = new LaudListDto(user);
		    	laudLists.add(dto);
		    }
		    resp.setLaudList(laudLists);
		}
		
		//}
		// 帖子的评论
		List<PostReply> replys = this.findReplys(postId,userPost.getContent_type());
		if(CollectionUtils.isNotEmpty(replys)){
			List<ReplyDto> replyDtos = new ArrayList<ReplyDto>();
			for(PostReply reply : replys){
				ReplyDto dto = new ReplyDto(reply);
				User replyer = this.userService.findUserById(reply.getReplyer_id());
				dto.setSenderHeadIcon(replyer.getHead_icon());
				dto.setSenderName(replyer.getNickname());
				dto.setThanked(this.isThankForReply(userSession.getUserId(), reply.getId()));
				dto.setThankAmount(this.countThankAmountForReply(reply.getId()));
				if(StringUtils.isNotBlank(reply.getContent())&&reply.getContent().contains("@")){
					dto.setAts(this.atRelationsDao.findAtTargets(ContentType.帖子评论, reply.getId()));
				}
				// 评论下的回复
				List<PostReply> replyAts = this.findReplys(postId, userPost.getContent_type(),reply.getId());
				if(CollectionUtils.isNotEmpty(replyAts)){
					List<ReplyDto> replyAtDtos = new ArrayList<ReplyDto>();
					for(PostReply postReply : replyAts){
						ReplyDto atdto = new ReplyDto(postReply);
						User user = this.userService.findUserById(postReply.getReplyer_id());
						atdto.setSenderName(user.getNickname());
						User atUser = this.userService.findUserById(postReply.getAt_targetId());
						atdto.setAtTargetId(postReply.getAt_targetId());
						atdto.setAtTargetName(atUser.getNickname());
						if(StringUtils.isNotBlank(postReply.getContent())&&postReply.getContent().contains("@")){
							atdto.setAts(this.atRelationsDao.findAtTargets(ContentType.帖子评论, postReply.getId()));
						}
						replyAtDtos.add(atdto);
					}
					dto.setReplys(replyAtDtos);
				}
				replyDtos.add(dto);
			}
			resp.setReplys(replyDtos);
		}
		
		return resp;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Resp sharePost(long postId) {
		UserPost userPost = this.getUserPost(postId);
		if(userPost == null){
			throw new GeneralLogicException("该话题不存在或已被删除了哦");
		}
		User sender = this.userService.findUserById(userPost.getUser_id());
		
		PostDetailResp resp = new PostDetailResp(sender, userPost);
		resp.setCourtyarName(this.courtyardDao.get(userPost.getCourtyard_id()).getCourtyard_name());
//		resp.setLaudAmount(this.countLaud(postId));
//		resp.setReplyAmount(this.countReply(postId,userPost.getContent_type()));
		//}
		// 帖子的评论
		List<PostReply> replys = this.findReplys(postId,userPost.getContent_type());
		if(CollectionUtils.isNotEmpty(replys)){
			List<ReplyDto> replyDtos = new ArrayList<ReplyDto>();
			for(PostReply reply : replys){
				ReplyDto dto = new ReplyDto(reply);
				User replyer = this.userService.findUserById(reply.getReplyer_id());
				dto.setSenderHeadIcon(replyer.getHead_icon());
				dto.setSenderName(replyer.getNickname());
//				dto.setThanked(this.isThankForReply(userSession.getUserId(), reply.getId()));
//				dto.setThankAmount(this.countThankAmountForReply(reply.getId()));
				// 评论下的回复
//				List<PostReply> replyAts = this.findReplys(postId, userPost.getContent_type(),reply.getId());
//				if(CollectionUtils.isNotEmpty(replyAts)){
//					List<ReplyDto> replyAtDtos = new ArrayList<ReplyDto>();
//					for(PostReply postReply : replyAts){
//						ReplyDto atdto = new ReplyDto(postReply);
//						User user = this.userService.findUserById(postReply.getReplyer_id());
//						atdto.setSenderName(user.getNickname());
//						User atUser = this.userService.findUserById(postReply.getAt_targetId());
//						atdto.setAtTargetId(postReply.getAt_targetId());
//						atdto.setAtTargetName(atUser.getNickname());
//						replyAtDtos.add(atdto);
//					}
//					dto.setReplys(replyAtDtos);
//				}
				replyDtos.add(dto);
			}
			resp.setReplys(replyDtos);
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserPostService#getFollowPostList(long, int, int)
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public UserPostListResp getFollowPostList(long user_id, int current_page, int max_num) {
		// 获取关注的人
		List<Long> followIds = userFollowDao.findFollowIds(user_id);
		UserPostListResp resp = new UserPostListResp();
		if(followIds.isEmpty()){
			return resp;
		}
		// 去掉把自己拉黑的人
		Iterator<Long> ir = followIds.iterator();
		while(ir.hasNext()){
			if(blackListDao.isBlackList(ir.next(), user_id)){
				ir.remove();
			}
		}
		if(followIds.isEmpty()){
			return resp;
		}
		List<UserPost> userPosts = this.userPostDao.findForPage(current_page, max_num, "create_time", false, Restrictions.in("user_id", followIds));
		for(UserPost userPost : userPosts){
			User sender = this.userService.findUserById(userPost.getUser_id());
			PostDetailDto postDetail = new PostDetailDto(sender, userPost);
			if(StringUtils.isNotBlank(userPost.getContent())&&userPost.getContent().contains("@")){
				postDetail.setAts(this.atRelationsDao.findAtTargets(ContentType.getInstance(userPost.getContent_type()), userPost.getId()));
			}
			CourtYard yard = this.courtyardDao.get(sender.getCourtyard_id());
			postDetail.setCourtyardName(yard.getCourtyard_name());
			postDetail.setLaudAmount(this.countLaud(userPost.getId()));
			postDetail.setReplyAmount(this.countReply(userPost.getId(),userPost.getContent_type()));
			if(!UserSession.get().isVisitor()){
				postDetail.setCollected(userCollectService.findUserCollect(UserSession.get().getUserId(), userPost.getContent_type(), userPost.getId())!=null);
				postDetail.setLauded(this.findLaud(UserSession.get().getUserId(), userPost.getId())!=null);
			}else{
				postDetail.setCollected(false);
				postDetail.setLauded(false);
			}
			resp.getDatas().add(postDetail);
		}
		return resp;
	}
	
	
	/**  邀约(1)，棒棒分享(2),分享(3),活动(4),专题(5),学堂(6)
	 * @see cn.dayuanzi.service.IUserPostService#getLaudList(int, int,long)
	 */
	@Override
	@Transactional(readOnly=true)
	public LaudListResp getLaudList(int current_page, int max_num,long postId,int type ){
	    List<Long> userIds = null;
	    LaudListResp resp = new LaudListResp();
	    //邀约
	    if(type ==1){
	    	if(this.invitationDao.get(postId) == null){
		    	throw new GeneralLogicException("没有这个邀约哦");
		    }
	    	 userIds = this.invitationInterestDao.laudInvitationUser(postId);
			    if(userIds.isEmpty()){
			    	return resp;
			    }
	    }
		//分享帮帮
	    if(type ==2||type==3){
			if(this.userPostDao.get(postId) == null){
		    	throw new GeneralLogicException("没有这个话题哦");
		    }
		    userIds = this.postLaudDao.laudPostUser(postId);
		    if(userIds.isEmpty()){
		    	return resp;
		    }
		}
	    //活动
	  		if(type ==4){
	  			if(this.activityInfoDao.get(postId) == null){
	  		    	throw new GeneralLogicException("没有这个活动哦");
	  		    }
	  		    userIds = this.activityLaudDao.laudActivityUser(postId);
	  		    if(userIds.isEmpty()){
	  		    	return resp;
	  		    }
	  		}
		//专题
		if(type ==5){
			if(this.contentEntityDao.get(postId) == null){
		    	throw new GeneralLogicException("没有这个专题哦");
		    }
		    userIds = this.contentEntityLaudDao.laudContentEntityUser(postId);
		    if(userIds.isEmpty()){
		    	return resp;
		    }
		}
		
		//学堂
		if(type ==6){
			if(this.trainInfoDao.get(postId) == null){
		    	throw new GeneralLogicException("没有这个学堂哦");
		    }
		    userIds = this.trainLaudDao.laudTrainUser(postId);
		    if(userIds.isEmpty()){
		    	return resp;
		    }
		}
	    
	    //返回用户与标签
	    List<User> users = this.userDao.get(userIds);
	    for(User user : users){
			LaudListDto dto = new LaudListDto(user);
			UserStar userStar = userStarDao.get(user.getId());
			String r = null;
		    if(userStar!=null){
		    	r = userStar.getSkill();
		    }else{
				if(CareerConfig.getCareers().get(user.getCareerId())!=null){
				    r = CareerConfig.getCareers().get(user.getCareerId()).getDomain();
				}else{
					List<Integer> interests = this.userInterestDao.findInterests(user.getId());
				    if(interests!=null){
						for(Integer interestid:interests){
						    if(InterestConf.getInterests().get(interestid)!=null){
						    	r = r+InterestConf.getInterests().get(interestid).getInterest();
						    }
						} 
				    }else{
				    	r = user.getSignature();
				    }
				}	
		    }
		    dto.setInfo(r);
			resp.getDatas().add(dto);
	    }
	    return resp;
	}
}
