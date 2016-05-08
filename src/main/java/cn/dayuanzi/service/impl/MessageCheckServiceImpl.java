package cn.dayuanzi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.config.CareerConfig;
import cn.dayuanzi.config.ExpInfo;
import cn.dayuanzi.config.InterestConf;
import cn.dayuanzi.dao.ActivityInfoDao;
import cn.dayuanzi.dao.ActivityReplyDao;
import cn.dayuanzi.dao.AtRelationsDao;
import cn.dayuanzi.dao.ContentEntityDao;
import cn.dayuanzi.dao.ContentEntityReplyDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.CourtyardOfActivityDao;
import cn.dayuanzi.dao.FriendsDao;
import cn.dayuanzi.dao.InvitationDao;
import cn.dayuanzi.dao.LaudViewDao;
import cn.dayuanzi.dao.LikeInvitationMembersDao;
import cn.dayuanzi.dao.MessageStatusDao;
import cn.dayuanzi.dao.NoticeDao;
import cn.dayuanzi.dao.PostLaudDao;
import cn.dayuanzi.dao.PostReplyDao;
import cn.dayuanzi.dao.PushMessageStatusDao;
import cn.dayuanzi.dao.ReplyDao;
import cn.dayuanzi.dao.ThankReplyDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserFollowDao;
import cn.dayuanzi.dao.UserInterestDao;
import cn.dayuanzi.dao.UserPostDao;
import cn.dayuanzi.dao.UserStarDao;
import cn.dayuanzi.dao.ValidateUserDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.ActivityInfo;
import cn.dayuanzi.model.ActivityReply;
import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.model.ContentEntity;
import cn.dayuanzi.model.ContentEntityReply;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.Friends;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.MessageStatus;
import cn.dayuanzi.model.Notice;
import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.model.PushMessageStatus;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserDaily;
import cn.dayuanzi.model.UserFollow;
import cn.dayuanzi.model.UserLinDou;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.model.UserStar;
import cn.dayuanzi.model.view.LaudView;
import cn.dayuanzi.model.view.ReplyView;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.pojo.ThingsAdder;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.AtRelationsListResp;
import cn.dayuanzi.resp.CheckNoticeResp;
import cn.dayuanzi.resp.ContactsResp;
import cn.dayuanzi.resp.MessageCheckResp;
import cn.dayuanzi.resp.NewInvitationResp;
import cn.dayuanzi.resp.NewLaudResp;
import cn.dayuanzi.resp.NewNoticeResp;
import cn.dayuanzi.resp.NewReplyResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.dto.AtRelationsListDto;
import cn.dayuanzi.resp.dto.ContactsDto;
import cn.dayuanzi.resp.dto.InvitationDto;
import cn.dayuanzi.resp.dto.LevelExpDto;
import cn.dayuanzi.resp.dto.NewLaudDto;
import cn.dayuanzi.resp.dto.NewNoticeDto;
import cn.dayuanzi.resp.dto.NewReplyDto;
import cn.dayuanzi.resp.dto.PushMessageDto;
import cn.dayuanzi.service.IHuanXinService;
import cn.dayuanzi.service.IMessageCheckService;
import cn.dayuanzi.service.IUserPostService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.DateTimeUtil;
import cn.dayuanzi.util.RedisKey;
import cn.dayuanzi.util.YardUtils;

/**
 * 
 * @author : leihc
 * @date : 2015年4月22日 下午8:49:06
 * @version : 1.0
 */
@Service
public class MessageCheckServiceImpl implements IMessageCheckService {

	@Autowired
	private MessageStatusDao messageStatusDao;
	@Autowired
	private PushMessageStatusDao pushMessageStatusDao;
	@Autowired
	private IUserPostService userPostService;
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private InvitationDao invitationDao; 
	@Autowired
	private UserInterestDao userInterestDao;
	@Autowired
	private FriendsDao friendsDao;
	@Autowired
	private PostReplyDao postReplyDao;
	@Autowired
	private UserPostDao userPostDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private PostLaudDao postLaudDao;
	@Autowired
	private ValidateUserDao validateUserDao;
	@Autowired
	private LikeInvitationMembersDao likeInvitationMembersDao;
	@Autowired
	private ThankReplyDao thankReplyDao;
	@Autowired
	private IHuanXinService huanXinService;
	@Autowired
	private ActivityReplyDao activityReplyDao;
	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	@Autowired
	private ActivityInfoDao activityInfoDao;
	@Autowired
	private UserFollowDao userFollowDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private CourtyardOfActivityDao courtyardOfActivityDao;
	@Autowired
	private ContentEntityDao contentEntityDao;
	@Autowired
	private CourtYardDao courtyardDao;
	@Autowired
	private ContentEntityReplyDao contentEntityReplyDao;
	@Autowired
	private ReplyDao replyDao;
	@Autowired
	private UserStarDao userStarDao;
	@Autowired
	private AtRelationsDao atRelationsDao;
	@Autowired
	private LaudViewDao laudViewDao;
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#findMessageCheck(long, long)
	 */
	@Override
	@Transactional(readOnly = false)
	public MessageStatus findMessageCheck(long user_id, long courtyard_id) {
		MessageStatus messageStatus = null;
		List<MessageStatus> list = this.messageStatusDao.find(Restrictions.eq("user_id", user_id));
		if(list.isEmpty()){
			messageStatus = new MessageStatus(user_id, 0);
			this.messageStatusDao.save(messageStatus);
		}else{
			messageStatus = list.get(0);
			if(list.size()>1){
				for(int i=1 ; i<list.size();i++){
					this.messageStatusDao.delete(list.get(i));
				}
			}
		}
		
		return messageStatus;
	}
	
	@Transactional
	public PushMessageStatus findPushMessageCheck(long userId){
		PushMessageStatus pms = this.pushMessageStatusDao.get(userId);
		if(pms==null){
			pms = new PushMessageStatus();
			pms.setId(userId);
			this.pushMessageStatusDao.save(pms);
		}
		return pms;
	}

	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getMessageCheckInfo(long, long)
	 */
	@Override
	@Transactional(readOnly = false)
	public MessageCheckResp getMessageCheckInfo(long user_id, long courtyard_id) {
		MessageCheckResp resp = new MessageCheckResp();
		resp.setLevelExpDto(new LevelExpDto(this.userDao.get(user_id)));
		MessageStatus messageStatus = this.findMessageCheck(user_id, courtyard_id);
		// 未读回复数
//		long notReadReply = userPostService.countNotReadReply(user_id, courtyard_id, messageStatus.getLast_readed_reply_id());
		// 加上活动中的回复数
//		notReadReply+=activityReplyDao.countNotReadReply(user_id, courtyard_id, messageStatus.getLast_readed_activity_reply_id());
		// +专题回复数
//		notReadReply+=contentEntityReplyDao.countNotReadReply(user_id, messageStatus.getLast_readed_subject_reply_id());
		long notReadReply = this.replyDao.countNotReadReply(user_id, messageStatus.getLast_readed_reply_time());
		// 未读点赞数
//		long notReadLaud = userPostService.countNotReadLaud(user_id, courtyard_id, messageStatus.getLast_readed_laud_id());
//		notReadLaud=notReadLaud+likeInvitationMembersDao.countNotReadLike(user_id, courtyard_id, messageStatus.getLast_readed_invitation_like_id());
//		notReadLaud=notReadLaud+thankReplyDao.countNotReadThink(user_id,messageStatus.getLast_think_reply());
		long notReadLaud = this.laudViewDao.countNotReadLaud(user_id, messageStatus.getLast_readed_laud_time());
		// 未读通知数量
		long notReadNotice = noticeDao.countNotReadNotice(user_id, courtyard_id, messageStatus.getLast_notice(),UserSession.get().getRegisterTime());
		//注释原因  需求更改，不再返回该数据
		// 查询用户的兴趣
		//List<Integer> interests = userInterestDao.findInterests(user_id);
		// 未读邀约数
		//long notReadInvitation = invitationDao.countInvitations(courtyard_id, messageStatus.getLast_invitation());
		//resp.setNotReadInvitation(notReadInvitation);
		// 未读好友请求
//		long notReadFriendRequest = friendsDao.countNotReadFriendRequest(courtyard_id, user_id, messageStatus.getLast_friend_request());
//		List<Friends> friends = this.friendsDao.findFriends(user_id);
//		List<Long> friendIds = new ArrayList<Long>();
//		for(Friends friend : friends){
//			if(friend.getUser_id()==user_id){
//				friendIds.add(friend.getFriend_id());
//			}else{
//				friendIds.add(friend.getUser_id());
//			}
//		}
		// 新邻居个数
		long neighborCount = validateUserDao.count(courtyard_id, user_id, messageStatus.getLast_neighbor_validate_time(),null);
		long notReadAt = this.atRelationsDao.countNotRead(user_id, messageStatus.getLast_readed_atTime());
		resp.setNewNeighborAmount(neighborCount);
		resp.setNotReadReplyAmount(notReadReply);
		resp.setNotReadLaudAmount(notReadLaud);
		resp.setNotReadNotice(notReadNotice);
		resp.setNotReadAtAmount(notReadAt);
		//resp.setNotReadInvitation(notReadInvitation);
//		resp.setNotReadFriendRequest(notReadFriendRequest);
		//注释原因: 前端不在返回该数据字段
		//通知的时间 和内容    
		//Notice notices = this.noticeDao.getLatestNotice(courtyard_id,user_id);
		//if(notices!=null){
		//	resp.setNoticeType(notices);
		//	resp.setContent(notices.getContent());
		//	resp.setCreate_time(DateTimeUtil.getDisplay(notices.getCreate_time()));
	//	}
		UserLinDou lindou = ServiceRegistry.getUserService().getUserLinDou(user_id);
		resp.setLindouAmount(lindou.getAmount());
		boolean isValidated = validateUserDao.isValidate(user_id, courtyard_id);
		UserSession.get().setValidate(isValidated);
		resp.setValidated(isValidated);
		// 是否有未完成的任务
		resp.setHaveNotCompletedTask(haveNotCompletedTask(user_id));
		// 新的关注
		long newFollows = this.userFollowDao.countNotReadFollow(user_id,messageStatus.getLast_follow_me_time());
		resp.setNewFollowMeAmount(newFollows);
		CourtYard yard = this.courtyardDao.get(courtyard_id);
		if(yard!=null){
			// 最新专题
		    ContentEntity se = this.contentEntityDao.getLatestContentEntity(yard.getCity_id());
			//新的专题   先预留字段
		    resp.setNewSpecialAmout(se!=null&&se.getCreate_time()!=messageStatus.getLast_linju_subject_time());
			//是否有新的邀约     /  挤一挤
		    int range = 5;//找5范围里面的社区
		    if(!UserSession.get().getCourtyardsPerDistance().containsKey(range)){
			List<CourtYard> courtyards = courtyardDao.find(Restrictions.eq("city_id", yard.getCity_id()));
			List<Long> courtyardsIds = new ArrayList<Long>();
			for(CourtYard c : courtyards){
				if(c.getId()==courtyard_id){
					continue;
				}
				if(YardUtils.getDistance(yard.getLongitude(), yard.getLatitude(), c.getLongitude(), c.getLatitude()) < range*1000){
					courtyardsIds.add(c.getId());
				}
			}
			if(!courtyardsIds.isEmpty()){
				UserSession.get().getCourtyardsPerDistance().put(range, courtyardsIds);
			}
		}
		    List<Long> courtyardIds = UserSession.get().getCourtyardsPerDistance().get(range);
		    String pattern = "MM-dd HH:mm";
		    String c = DateTimeUtil.formatDateTime(System.currentTimeMillis(), pattern);
		    long current = DateTimeUtil.getTime(c, pattern);
		    Invitation invitations = null;
		    if(courtyardIds == null|| courtyardIds.isEmpty()){//如果5公立范围内没有 或者为空 就查自己的社区
		    	invitations = this.invitationDao.getLatestInvitations(courtyard_id);
		    	resp.setNewInvitationAmout(invitations!=null&&invitations.getCreate_time()!=messageStatus.getLast_invitation());
		    }else{
			    	//另外的筛选为 范围内的社区的邀约帖子为开放的帖子 
				// where courtyard_id=courtyardId or courtyard_id in courtyardIds and show_around=true;
			Disjunction dis = Restrictions.disjunction();
			dis.add(Restrictions.eq("courtyard_id", courtyard_id));
			Conjunction con = Restrictions.conjunction();
			con.add(Restrictions.in("courtyard_id", courtyardIds));
			con.add(Restrictions.eq("show_around", true));
			dis.add(con);
			invitations = this.invitationDao.getLatestInvitations(dis,Restrictions.gt("activity_time", current));
			resp.setNewInvitationAmout(invitations!=null&&invitations.getCreate_time()!=messageStatus.getLast_invitation());
		    }
		}
		return resp;
	}
	
	private boolean haveNotCompletedTask(long userId){
		User user = this.userDao.get(userId);
		if(!this.validateUserDao.isValidate(userId, user.getCourtyard_id())){
			return true;
		}
		boolean haveBlank = StringUtils.isBlank(user.getSignature());
		if(!haveBlank){
			List<Integer> interestIds = userInterestDao.findInterests(userId);
			haveBlank = interestIds.isEmpty();
		}
		if(!haveBlank){
			UserStar us = this.userStarDao.findUnique(Restrictions.eq("user_id", userId));
			haveBlank = us==null;
		}
		if(haveBlank){
			return true;
		}
//		if(this.userPostDao.findziwojieshao(userId)){
//			return true;
//		}
		if(this.userDao.find(Restrictions.eq("inviteCode",userId)).size()==0){
			return true;
		}
		
		UserDaily userDaily = this.userService.getUserDaily(userId);
		// 每日任务发话题
		ExpInfo info = ThingsAdder.发话题.getExpInfo();
		if(userDaily.getSend_post_count()<info.getLimitDaily()){
			return true;
		}
		// 每日任务帮帮评论
		info = ThingsAdder.帮帮评论.getExpInfo();
		if(userDaily.getHelp_reply_count()<info.getLimitDaily()){
			return true;
		}
		// 每日任务被感谢
		info = ThingsAdder.被感谢.getExpInfo();
		if(userDaily.getBe_thank_count()<info.getLimitDaily()){
			return true;
		}
		// 每日任务回复被采纳
		info = ThingsAdder.意见被采纳.getExpInfo();
		if(userDaily.getAccepted_count()<info.getLimitDaily()){
			return true;
		}
		return false;
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#operateFriendRequest(long, long, int)
	 */
	@Override
	@Transactional
	public void operateFriendRequest(long friendId, long userId, long yardId, int operate) {
//		Friends friends = friendsDao.findFriendsStatus(friendId,userId);
		Friends friends = friendsDao.findUnique(Restrictions.eq("user_id", friendId),Restrictions.eq("friend_id", userId));
		if(friends == null){
			throw new GeneralLogicException("好友请求不存在");
		}
		if(friends.getFriend_id()!=userId || friends.getCourtyard_id()!=yardId){
			throw new GeneralLogicException("您不能处理这条好友请求");
		}
		if(friends.getOperate_time() > 0){
			throw new GeneralLogicException("这条好友请求已经处理过了");
		}
		friends.setOperate_time(System.currentTimeMillis());
		friends.setAccept(operate);
		friendsDao.saveOrUpdate(friends);
		try {
			ServiceRegistry.getHuanXinService().addFriend(friends);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getNewReplyList(long, long)
	 */
	@Override
	@Transactional(readOnly = false)
	public NewReplyResp getNewReplyList(long courtyard_id, long post_sender_id,int current_page, int max_num) {
		NewReplyResp resp = new NewReplyResp();
		MessageStatus messageStatus = this.findMessageCheck(post_sender_id, courtyard_id);
//		Criterion c_courtyardId = Restrictions.eq("courtyard_id", courtyard_id);
//		Disjunction senderIdOrAtTarget = Restrictions.disjunction();
//		senderIdOrAtTarget.add(Restrictions.eq("post_sender_id", post_sender_id));
//		senderIdOrAtTarget.add(Restrictions.eq("at_targetId", post_sender_id));
//		Criterion c_replyerId = Restrictions.ne("replyer_id", post_sender_id);
		
//		List<Long> postIds = this.postReplyDao.getRelatedPostIds(post_sender_id);
//		List<PostReply> replys = new ArrayList<PostReply>();
//		if(!postIds.isEmpty()){
//			Criterion c = Restrictions.in("post_id", postIds);
//			replys = this.postReplyDao.findPostReplys("create_time", false, c, Restrictions.ne("replyer_id", post_sender_id));
//		}
		
//		List<PostReply> replys = this.postReplyDao.getReplys(post_sender_id);
//		long maxId = messageStatus.getLast_readed_reply_id();
//		if(CollectionUtils.isNotEmpty(replys)){
//			for(PostReply reply : replys){
//				String postContent = null;
//				List<String> images = null;
//				if(reply.getContent_type()!=ContentType.邀约.getValue()){
//					UserPost userPost = this.userPostDao.get(reply.getPost_id());
//					if(userPost == null){
//						this.postReplyDao.delete(reply);
//						continue;
//					}else{
//						postContent = userPost.getContent();
//						images = userPost.getImage_names();
//					}
//				}else{
//					Invitation invitation = this.invitationDao.get(reply.getPost_id());
//					if(invitation == null){
//						this.postReplyDao.delete(reply);
//						continue;
//					}else{
//						postContent = invitation.getContent();
//						images = invitation.getImage_names();
//					}
//				}
//				
//				NewReplyDto dto = new NewReplyDto();
//				User replyer = this.userDao.get(reply.getReplyer_id());
//				dto.setId( reply.getId());
//				dto.setSenderName(replyer.getNickname());
//				dto.setSenderHeadIcon(replyer.getHead_icon());
//				dto.setSendTime(DateTimeUtil.getDisplay(reply.getCreate_time())) ;
//				dto.setContent(reply.getContent());
//				dto.setPostId(reply.getPost_id());
//				dto.setPostType(reply.getContent_type());
//				dto.setPostContent(postContent);
//				dto.setPostImages(images);
//				dto.setReply_id(replyer.getId());
//				dto.setReply_time_number(reply.getCreate_time());
//				resp.getdatas().add(dto);
//				if(reply.getId() > maxId){
//					maxId = reply.getId();
//				}
//			}
//			replys = null;
//		}
//		long maxActivityReplyId = messageStatus.getLast_readed_activity_reply_id();
//		List<ActivityReply> activityReplys = activityReplyDao.findActivityReply("create_time",false, Restrictions.eq("at_targetId", post_sender_id));
//		for(ActivityReply activityReply : activityReplys){
//			NewReplyDto dto = new NewReplyDto();
//			User replyer = this.userDao.get(activityReply.getReplyer_id());
//			dto.setId(activityReply.getId());
//			dto.setSenderName(replyer.getNickname());
//			dto.setSenderHeadIcon(replyer.getHead_icon());
//			dto.setSendTime(DateTimeUtil.getDisplay(activityReply.getCreate_time())) ;
//			dto.setReply_time_number(activityReply.getCreate_time());
//			dto.setContent(activityReply.getContent());
//			dto.setPostId(activityReply.getActivity_id());
//			dto.setPostType(ContentType.活动.getValue());
//			ActivityInfo activityInfo = this.activityInfoDao.get(activityReply.getActivity_id());
//			if(activityInfo==null){
//				continue;
//			}
//			dto.setPostContent(activityInfo.getActivity_title());
//			resp.getdatas().add(dto);
//			if(activityReply.getId() > maxActivityReplyId){
//				maxActivityReplyId = activityReply.getId();
//			}
//		}
//		long maxContentEntityReplyId = messageStatus.getLast_readed_subject_reply_id();
//		List<ContentEntityReply> cers = contentEntityReplyDao.findContentEntityReply("create_time",false, Restrictions.eq("at_targetId", post_sender_id));
//		for(ContentEntityReply cer : cers){
//			NewReplyDto dto = new NewReplyDto();
//			User replyer = this.userDao.get(cer.getReplyer_id());
//			dto.setId(cer.getId());
//			dto.setSenderName(replyer.getNickname());
//			dto.setSenderHeadIcon(replyer.getHead_icon());
//			dto.setSendTime(DateTimeUtil.getDisplay(cer.getCreate_time())) ;
//			dto.setReply_time_number(cer.getCreate_time());
//			dto.setContent(cer.getContent());
//			dto.setPostId(cer.getContent_id());
//			dto.setPostType(ContentType.专题.getValue());
//			ContentEntity ce = this.contentEntityDao.get(cer.getContent_id());
//			if(ce==null){
//				continue;
//			}
//			dto.setPostContent(ce.getTitle());
//			resp.getdatas().add(dto);
//			if(cer.getId() > maxContentEntityReplyId){
//				maxContentEntityReplyId = cer.getId();
//			}
//		}
		
		List<ReplyView> newReplys = this.replyDao.getReplys(post_sender_id, current_page, max_num);
		long lastTime = messageStatus.getLast_readed_reply_time();
		for(ReplyView reply : newReplys){
			NewReplyDto dto = new NewReplyDto();
			if(reply.getContentType()==ContentType.邀约.getValue()){
				Invitation invitation = this.invitationDao.get(reply.getTargetId());
				if(invitation!=null){
					dto.setPostContent(invitation.getContent());
					dto.setPostImages(invitation.getImage_names());
				}else{
					continue;
				}
			}else if(reply.getContentType()==ContentType.分享.getValue()||reply.getContentType()==ContentType.邻居帮帮.getValue()){
				UserPost userPost = this.userPostDao.get(reply.getTargetId());
				if(userPost!=null){
					dto.setPostContent(userPost.getContent());
					dto.setPostImages(userPost.getImage_names());
				}else{
					continue;
				}
			}else if(reply.getContentType()==ContentType.活动.getValue()){
				ActivityInfo activityInfo = this.activityInfoDao.get(reply.getTargetId());
				if(activityInfo==null){
					continue;
				}
				dto.setPostContent(activityInfo.getActivity_title());
			}else if(reply.getContentType()==ContentType.专题.getValue()){
				ContentEntity ce = this.contentEntityDao.get(reply.getTargetId());
				if(ce==null){
					continue;
				}
				dto.setPostContent(ce.getTitle());
			}else{
				continue;
			}
			User replyer = this.userDao.get(reply.getReplyerId());
			dto.setId(reply.getReplyId());
			dto.setSenderName(replyer.getNickname());
			dto.setSenderHeadIcon(replyer.getHead_icon());
			dto.setSendTime(DateTimeUtil.getDisplay(reply.getCreateTime())) ;
			dto.setContent(reply.getContent());
			dto.setPostId(reply.getTargetId());
			dto.setPostType(reply.getContentType());
			dto.setReply_id(replyer.getId());
			dto.setReply_time_number(reply.getCreateTime());
			resp.getdatas().add(dto);
			if(reply.getCreateTime()>lastTime){
				lastTime = reply.getCreateTime();
			}
		}
		if(lastTime > messageStatus.getLast_readed_reply_time()){
			messageStatus.setLast_readed_reply_time(lastTime);
			this.messageStatusDao.save(messageStatus);
			PushMessageStatus pms = this.findPushMessageCheck(post_sender_id);
			pms.setLast_readed_reply_time(lastTime);
			this.pushMessageStatusDao.saveOrUpdate(pms);
			this.clearNotReadMsgCount(post_sender_id, RedisKey.not_read_reply);
		}
		
//		if(maxId != messageStatus.getLast_readed_reply_id() || maxActivityReplyId!=messageStatus.getLast_readed_activity_reply_id()||maxContentEntityReplyId!=messageStatus.getLast_readed_subject_reply_id()){
//			messageStatus.setLast_readed_reply_id(maxId);
//			messageStatus.setLast_readed_activity_reply_id(maxActivityReplyId);
//			messageStatus.setLast_readed_subject_reply_id(maxContentEntityReplyId);
//			this.messageStatusDao.save(messageStatus);
//			PushMessageStatus pms = this.findPushMessageCheck(post_sender_id);
//			pms.setLast_readed_reply_id(maxId);
//			pms.setLast_readed_activity_reply_id(maxActivityReplyId);
//			pms.setLast_readed_subject_reply_id(maxContentEntityReplyId);
//			this.pushMessageStatusDao.saveOrUpdate(pms);
//			this.clearNotReadMsgCount(post_sender_id, RedisKey.not_read_reply);
//		}
//		if(!activityReplys.isEmpty()||!cers.isEmpty()){
//			Collections.sort(resp.getdatas());
//		}
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getNewLaudList(long, long)
	 */
	@Override
	@Transactional
	public NewLaudResp getNewLaudList(long courtyard_id, long post_sender_id,int current_page, int max_num) {
//		UserSession userSession = UserSession.get();
		NewLaudResp resp = new NewLaudResp();
		MessageStatus messageStatus = this.findMessageCheck(post_sender_id, courtyard_id);
//		List<PostLaud> lauds = new ArrayList<PostLaud>();
//		List<Long> postIds = this.postLaudDao.getRelatedPosts(post_sender_id);
//		if(!postIds.isEmpty()){
//			lauds = this.postLaudDao.findPostLaud("create_time", false, Restrictions.in("post_id", postIds),Restrictions.ne("user_id", post_sender_id));
//		}
		
//		List<PostLaud> lauds = this.postLaudDao.getLauds(post_sender_id);
//		long maxId = messageStatus.getLast_readed_laud_id();
//		if(CollectionUtils.isNotEmpty(lauds)){
//			for(PostLaud postlaud : lauds){
//				UserPost userPost = this.userPostDao.get(postlaud.getPost_id());
//				if(userPost == null){
//					postLaudDao.delete(postlaud);
//					continue;
//				}
//				NewLaudDto dto = new NewLaudDto();
//				User lauder = this.userDao.get(postlaud.getUser_id());
//				if(postlaud.getUser_id()==userSession.getUserId()){
//					continue;
//				}
//				dto.setId(postlaud.getId());
//				dto.setLauder_id(postlaud.getUser_id());
//				dto.setLauder_nickname(lauder.getNickname());
//				dto.setLauder_head(lauder.getHead_icon());
//				dto.setPostId(postlaud.getPost_id());
//				dto.setLaud_time(DateTimeUtil.getDisplay(postlaud.getCreate_time()));
//				dto.setLaud_time_number(postlaud.getCreate_time());
//				dto.setPostType(userPost.getContent_type());
//				dto.setImages(userPost.getImage_names());
//				dto.setPostContent(userPost.getContent());
//				if(postlaud.getId() > maxId){
//					maxId = postlaud.getId();
//				}
//				resp.getDatas().add(dto);
//				
//			}
//		}
		// 查询所有邀约点赞
//		List<LikeInvitationMembers> likeMembers = this.likeInvitationMembersDao.findLikes("create_time", false, Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("invitation_sender_id", post_sender_id),Restrictions.ne("user_id", post_sender_id));
		
//		List<LikeInvitationMembers> likeMembers = new ArrayList<LikeInvitationMembers>();
//		List<Long> invitationIds = this.likeInvitationMembersDao.getRelatedInvitation(post_sender_id);
//		if(!invitationIds.isEmpty()){
//			likeMembers = this.likeInvitationMembersDao.findLikes("create_time", false, Restrictions.in("invitation_id", invitationIds),Restrictions.ne("user_id", post_sender_id));
//		}
		
//		List<LikeInvitationMembers> likeMembers = this.likeInvitationMembersDao.getInvitationMembers(post_sender_id);
//		long maxLikeId = messageStatus.getLast_readed_invitation_like_id();
//		if(CollectionUtils.isNotEmpty(likeMembers)){
//			for(LikeInvitationMembers member : likeMembers){
//				Invitation invitation = this.invitationDao.get(member.getInvitation_id());
//				if(invitation == null){
//					likeInvitationMembersDao.delete(member);
//					continue;
//				}
//				NewLaudDto dto = new NewLaudDto();
//				User lauder = this.userDao.get(member.getUser_id());
//				dto.setId(member.getId());
//				dto.setLauder_id(member.getUser_id());
//				dto.setLauder_nickname(lauder.getNickname());
//				dto.setLauder_head(lauder.getHead_icon());
//				dto.setPostId(member.getInvitation_id());
//				dto.setLaud_time(DateTimeUtil.getDisplay(member.getCreate_time()));
//				dto.setLaud_time_number(member.getCreate_time());
//				dto.setPostType(ContentType.邀约.getValue());
//				dto.setImages(invitation.getImage_names());
//				dto.setPostContent(invitation.getContent());
//				if(invitation.getId() > maxLikeId){
//					maxLikeId = invitation.getId();
//				}
//				resp.getDatas().add(dto);
//			}
//		}
		
		//查找评论感谢
//		List<ThankReply> ThankList = this.thankReplyDao.findThank("create_time", false, Restrictions.eq("replyer_id", post_sender_id));
//		long maxThinkId = messageStatus.getLast_think_reply();
//		for(ThankReply member : ThankList ){
//			if(member.getContent_type()==2||member.getContent_type()==3){
//				UserPost userPost = this.userPostDao.get(this.postReplyDao.get(member.getReply_id()).getPost_id());
//				if(userPost == null){
//					this.thankReplyDao.delete(member);
//					continue;
//				}
//				NewLaudDto dto = new NewLaudDto();
//				User lauder = this.userDao.get(member.getUser_id());
//				if(member.getUser_id()==userSession.getUserId()){
//					continue;
//				}
//				dto.setId(member.getId());
//				dto.setLauder_id(member.getUser_id());
//				dto.setLauder_nickname(lauder.getNickname());
//				dto.setLauder_head(lauder.getHead_icon());
//				dto.setPostId(userPost.getId());
//				dto.setLaud_time(DateTimeUtil.getDisplay(member.getCreate_time()));
//				dto.setLaud_time_number(member.getCreate_time());
//				dto.setPostType(userPost.getContent_type());
//				dto.setImages(userPost.getImage_names());
//				dto.setPostContent(userPost.getContent());
//				if(member.getId() > maxThinkId){
//					maxThinkId = member.getId();
//				}
//				resp.getDatas().add(dto);	
//			}else{
//				Invitation invitation = this.invitationDao.get(this.postReplyDao.get(member.getReply_id()).getPost_id());
//				if(invitation == null){
//					thankReplyDao.delete(member);
//					continue;
//				}
//				NewLaudDto dto = new NewLaudDto();
//				User lauder = this.userDao.get(member.getUser_id());
//				dto.setId(member.getId());
//				dto.setLauder_id(member.getUser_id());
//				dto.setLauder_nickname(lauder.getNickname());
//				dto.setLauder_head(lauder.getHead_icon());
//				dto.setPostId(invitation.getId());
//				dto.setLaud_time(DateTimeUtil.getDisplay(member.getCreate_time()));
//				dto.setLaud_time_number(member.getCreate_time());
//				dto.setPostType(ContentType.邀约.getValue());
//				dto.setImages(invitation.getImage_names());
//				dto.setPostContent(invitation.getContent());
//				if(member.getId() > maxThinkId){
//					maxThinkId = member.getId();
//				}
//				resp.getDatas().add(dto);
//			}
//			
//		}
		
		List<LaudView> laudviews = this.laudViewDao.getLauds(post_sender_id, current_page, max_num);
		long lastReadLaudTime = messageStatus.getLast_readed_laud_time();
		if(CollectionUtils.isNotEmpty(laudviews)){
			for(LaudView lv : laudviews){
				NewLaudDto dto = new NewLaudDto();
				User lauder = this.userDao.get(lv.getLauder_id());
				dto.setId(lv.getId());
				dto.setLauder_id(lv.getLauder_id());
				dto.setLauder_nickname(lauder.getNickname());
				dto.setLauder_head(lauder.getHead_icon());
//				dto.setPostId(member.getInvitation_id());
				dto.setLaud_time(DateTimeUtil.getDisplay(lv.getCreate_time()));
				dto.setLaud_time_number(lv.getCreate_time());
//				dto.setPostType(ContentType.邀约.getValue());
//				dto.setImages(invitation.getImage_names());
//				dto.setPostContent(invitation.getContent());
				if(lv.getLaud_type()==1){
					UserPost userPost = this.userPostDao.get(lv.getContent_id());
					if(userPost == null){
						continue;
					}
					dto.setPostId(lv.getContent_id());
					dto.setPostType(userPost.getContent_type());
					dto.setImages(userPost.getImage_names());
					dto.setPostContent(userPost.getContent());
				}else if(lv.getLaud_type()==2){
					PostReply postReply = this.postReplyDao.get(lv.getContent_id());
					if(postReply!=null){
						UserPost userPost = this.userPostDao.get(postReply.getPost_id());
						if(userPost!=null){
							dto.setPostId(lv.getContent_id());
							dto.setPostType(userPost.getContent_type());
							dto.setImages(userPost.getImage_names());
							dto.setPostContent(userPost.getContent());
						}
					}
				}
				if(lv.getCreate_time() > lastReadLaudTime){
					lastReadLaudTime = lv.getCreate_time();
				}
				resp.getDatas().add(dto);
			}
		}
		if(lastReadLaudTime!=messageStatus.getLast_readed_laud_time()){
			messageStatus.setLast_readed_laud_time(lastReadLaudTime);
			this.messageStatusDao.save(messageStatus);
			PushMessageStatus pms = this.findPushMessageCheck(post_sender_id);
			pms.setLast_readed_laud_time(lastReadLaudTime);
			this.pushMessageStatusDao.saveOrUpdate(pms);
			this.clearNotReadMsgCount(post_sender_id, RedisKey.not_read_laud);
		}
//		if(maxId != messageStatus.getLast_readed_laud_id() || maxLikeId != messageStatus.getLast_readed_invitation_like_id()||maxThinkId!=messageStatus.getLast_think_reply()){
//			messageStatus.setLast_readed_laud_id(maxId);
//			messageStatus.setLast_readed_invitation_like_id(maxLikeId);
//			messageStatus.setLast_think_reply(maxThinkId);
//			this.messageStatusDao.save(messageStatus);
//			PushMessageStatus pms = this.findPushMessageCheck(post_sender_id);
//			pms.setLast_readed_laud_id(maxId);
//			pms.setLast_readed_invitation_like_id(maxLikeId);
//			pms.setLast_think_reply(maxThinkId);
//			this.pushMessageStatusDao.saveOrUpdate(pms);
//			this.clearNotReadMsgCount(post_sender_id, RedisKey.not_read_laud);
//		}
		// 因为话题帮帮点赞查询时已排序，邀约点赞没有时就可以省去排序
//		if(CollectionUtils.isNotEmpty(likeMembers)){
//			Collections.sort(resp.getDatas());
//		}
		return resp;
	}
	
	/** (non-Javadoc)
	 * @see cn.dayuanzi.service.IMessageCheckService#getContacts(long)
	 */
	@Override
	@Transactional(readOnly = false)
	public ContactsResp getContacts(long courtyardId, long userId,int current_page, int max_num) {
		ContactsResp resp = new ContactsResp();
		// 将好友改为关注关系
		List<Friends> friends = this.friendsDao.findFriends(userId);
		for(Friends friend : friends){
//			if(friend.getUser_id()==userId){
//				User user = this.userDao.get(friend.getFriend_id());
//				ContactsDto dto = new ContactsDto(user);
//				dto.setAccept(friend.getAccept());
//				dto.setId(friend.getId());
//				resp.getDatas().add(dto);
//			}else{
//				User user = this.userDao.get(friend.getUser_id());
//				ContactsDto dto = new ContactsDto(user);
//				dto.setAccept(friend.getAccept());
//				dto.setId(friend.getId());
//				resp.getDatas().add(dto);
//			}
			this.userFollowDao.save(new UserFollow(friend.getUser_id(), friend.getFriend_id()));
			this.userFollowDao.save(new UserFollow(friend.getFriend_id(), friend.getUser_id()));
			friendsDao.delete(friend);
		}
		List<Long> followIds = this.userFollowDao.findFollowIds(userId,current_page,max_num);
		
		if(!followIds.isEmpty()){
			List<User> follows = this.userDao.get(followIds);
			Map<Long, ContactsDto> temp = new HashMap<Long, ContactsDto>();
			for(User user : follows){
			    List<Integer> interestsForRecommend = this.userInterestDao.findInterests(user.getId());
			    ContactsDto dto = new ContactsDto(user);
			    if(interestsForRecommend!=null){
					 for(Integer interestid:interestsForRecommend){
						if(InterestConf.getInterests().get(interestid)!=null){
						    dto.getInterests().add(InterestConf.getInterests().get(interestid));
						}
					 }
			    }
			    //返回用户标签
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
			    temp.put(user.getId(), dto);
			}
			for(Long followId : followIds){
				if(temp.containsKey(followId)){
					resp.getDatas().add(temp.get(followId));
				}
			}
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getFollows(long)
	 */
	@Override
	@Transactional()
	public ContactsResp getFollows(long userId,long courtyardId,int current_page, int max_num) {
		ContactsResp resp = new ContactsResp();
		List<Long> followerIds = this.userFollowDao.findFollowerIds(userId,current_page,max_num);
		if(!followerIds.isEmpty()){
			List<User> follows = this.userDao.get(followerIds);
			Map<Long, ContactsDto> temp = new HashMap<Long, ContactsDto>();
			for(User user : follows){
				ContactsDto dto = new ContactsDto(user);
			    List<Integer> interestsForRecommend = this.userInterestDao.findInterests(user.getId());
				for(Integer interestid:interestsForRecommend){
				    if(InterestConf.getInterests().get(interestid)!=null)
					dto.getInterests().add(InterestConf.getInterests().get(interestid));
				}
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
				temp.put(user.getId(), dto);
			}
			for(Long followId : followerIds){
				if(temp.containsKey(followId)){
					resp.getDatas().add(temp.get(followId));
				}
			}
		}
		MessageStatus messageStatus = this.findMessageCheck(userId, courtyardId);
		UserFollow follow = this.userFollowDao.getLatestFollowTime(userId);
		if(follow!=null && follow.getCreate_time()!=messageStatus.getLast_follow_me_time()){
			messageStatus.setLast_follow_me_time(follow.getCreate_time());
			this.messageStatusDao.saveOrUpdate(messageStatus);
			PushMessageStatus pms = this.findPushMessageCheck(userId);
			pms.setLast_follow_me_time(follow.getCreate_time());
			this.pushMessageStatusDao.saveOrUpdate(pms);
			this.clearNotReadMsgCount(userId, RedisKey.not_read_follow);
		}
		return resp;
	}
	
	@Override
	@Transactional(readOnly = true)
	public ContactsResp getreQuestContacts(long courtyardId, long userId){
		ContactsResp resp = new ContactsResp();
		MessageStatus messageStatus = this.findMessageCheck(userId, courtyardId);
		long lastFollowTime = messageStatus.getLast_follow_me_time();
		List<UserFollow> userFollow = this.userFollowDao.find(Restrictions.eq("target_id", userId),Restrictions.eq("create_time", lastFollowTime));
		for(UserFollow follow : userFollow){
			User user = this.userDao.get(follow.getUser_id());
			ContactsDto dto = new ContactsDto(user);
			List<Integer> interestsForRecommend = this.userInterestDao.findInterests(user.getId());
			for(Integer interestid:interestsForRecommend){
			    if(InterestConf.getInterests().get(interestid)!=null)
				dto.getInterests().add(InterestConf.getInterests().get(interestid));
			}
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
			if(lastFollowTime < follow.getCreate_time()){
			  lastFollowTime = follow.getCreate_time();
			}
			
		}
		if(lastFollowTime!=messageStatus.getLast_follow_me_time()){
			messageStatus.setLast_follow_me_time(lastFollowTime);
			this.messageStatusDao.saveOrUpdate(messageStatus);
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getNewNoticeList(long)
	 */
	@Override
	@Transactional
	public NewNoticeResp getNewNoticeList(long courtyard_id, long user_id,int current_page, int max_num) {
		Criterion afterRigister = Restrictions.gt("create_time", UserSession.get().getRegisterTime());
		MessageStatus messageStatus = this.findMessageCheck(user_id, courtyard_id);
		Criterion c_userId = Restrictions.eq("user_id", user_id);
		Criterion sameYard = Restrictions.eq("courtyard_id", courtyard_id);
		Criterion allUser = Restrictions.eq("user_id", 0l);
		Criterion c_all = Restrictions.and(Restrictions.eq("courtyard_id", 0l),Restrictions.eq("user_id", 0l),afterRigister);
		Criterion or = Restrictions.or(c_userId, Restrictions.and(sameYard, allUser, afterRigister), c_all);
		List<Notice> notices = this.noticeDao.findForPage(current_page,max_num,"create_time",false, or);
		NewNoticeResp resp = new NewNoticeResp();
		long lastId = messageStatus.getLast_notice();
		Notice latestNotice = null;
		for(Notice notice : notices){
			resp.getDatas().add( new  NewNoticeDto(notice));
		
			if(notice.getId()>lastId){
				lastId = notice.getId();
				latestNotice = notice;
			}
			
		}
		if(lastId!=messageStatus.getLast_notice()){
			messageStatus.setLast_notice(lastId);
			this.messageStatusDao.save(messageStatus);
			
			PushMessageStatus pms = this.findPushMessageCheck(user_id);
			pms.setLast_help_time(latestNotice.getCreate_time());
			pms.setLast_accpet_time(latestNotice.getCreate_time());
			pms.setLast_courtyard_time(latestNotice.getCreate_time());
			pms.setLast_system_time(latestNotice.getCreate_time());
			pms.setLast_verify_time(latestNotice.getCreate_time());
			this.pushMessageStatusDao.saveOrUpdate(pms);
			this.clearNotReadMsgCount(user_id, RedisKey.not_read_notice);
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getNewInvitationList(long, long)
	 */
	@Override
	@Transactional
	public NewInvitationResp getNewInvitationList(long courtyard_id, long user_id) {
		MessageStatus messageStatus = this.findMessageCheck(user_id, courtyard_id);
		List<Integer> interests = userInterestDao.findInterests(user_id);
		Criterion c_gt_id = Restrictions.gt("id", messageStatus.getLast_invitation());
		Criterion c_courtyardId = Restrictions.eq("courtyard_id", courtyard_id);
		Criterion c_invitation_type = Restrictions.in("invitation_type", interests);
		List<Invitation> invitations = this.invitationDao.find(c_gt_id,c_courtyardId,c_invitation_type);
		NewInvitationResp resp = new NewInvitationResp();
		long maxId = messageStatus.getLast_invitation();
		for(Invitation invitation : invitations){
			resp.getDatas().add(new InvitationDto(invitation));
			if(invitation.getId()>maxId){
				maxId = invitation.getId();
			}
		}
		if(maxId!=messageStatus.getLast_invitation()){
			messageStatus.setLast_invitation(maxId);
			this.messageStatusDao.save(messageStatus);
		}
		return resp;
	}
	/**
	 * 好友请求生成
	 */
	@Override
	@Transactional(readOnly = false)
	public void requestAddFriend(long courtyardId, long userId,long friendId,String content){
		if(isFriend(userId,friendId)){
			throw new GeneralLogicException("你们已经是好友了");
		}
//		User friend = this.userDao.get(friendId);
		if(!validateUserDao.isValidate(friendId, courtyardId)){
			throw new GeneralLogicException("对方还未验证，不能请求加好友");
		}
		Friends friends = friendsDao.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("friend_id", friendId));
		if(friends==null){
			friends =new Friends();
			friends.setFriend_id(friendId);
			friends.setUser_id(userId);
			friends.setAccept(1);
			friends.setCourtyard_id(courtyardId);
		}
		if(StringUtils.isBlank(content)){
			content = "我是你的邻居";
		}
		friends.setContent(content);
		friends.setRequest_time(System.currentTimeMillis());
		this.friendsDao.save(friends);

	}
	
	
	@Override
	@Transactional(readOnly = true)
	public boolean isFriend(long userId,long friendId){
		Friends friends = this.friendsDao.findFriendsStatus(userId,friendId);
		if(friends==null){
			return false;
		}
		return friends.getAccept()==2;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteFriends(long friendId){
		UserSession userSession = UserSession.get();
		this.friendsDao.deleteFriends(userSession.getUserId(), friendId);
		
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getPushMessage(long, long)
	 */
	@Override
	@Transactional
	public Resp getPushMessage(User user) {
//		MessageStatus messageStatus = this.findMessageCheck(userId, courtyardId);
		long userId = user.getId();
		long courtyardId = user.getCourtyard_id();
		PushMessageStatus messageStatus = this.findPushMessageCheck(userId);
		CheckNoticeResp resp = new CheckNoticeResp();
		if(courtyardId==0){
			return resp;
		}
		UserSetting us = ServiceRegistry.getUserService().getUserSetting(userId);
		if(us.isSystem()){
			Notice notice = this.noticeDao.getLatestNotice(courtyardId, userId, NoticeType.求助通知,user.getRegister_time());
			if(notice!=null && notice.getCreate_time()>messageStatus.getLast_help_time()){
				PushMessageDto dto = new PushMessageDto(PushType.通知, "你收到一条新的紧急求助通知，看看你是否能帮上忙？", 0);
				dto.setNoticeType(NoticeType.求助通知.ordinal());
				resp.getAlerts().add(dto);
			}
			notice = this.noticeDao.getLatestNotice(courtyardId, userId, NoticeType.采纳通知,user.getRegister_time());
			if(notice!=null && notice.getCreate_time()>messageStatus.getLast_accpet_time()){
				PushMessageDto dto = new PushMessageDto(PushType.通知, "恭喜你，你的答案被采纳，赶快去看看吧！", 0);
				dto.setNoticeType(NoticeType.采纳通知.ordinal());
				resp.getAlerts().add(dto);
			}
			notice = this.noticeDao.getLatestNotice(courtyardId, userId, NoticeType.社区公告,user.getRegister_time());
			if(notice!=null && notice.getCreate_time()>messageStatus.getLast_courtyard_time()){
				String content = "【社区公告】";
				if(notice.getContent().length()<20){
					content = content + notice.getContent();
				}else{
					content = content + notice.getContent().substring(0, 19);
				}
				PushMessageDto dto = new PushMessageDto(PushType.通知, content, 0);
				dto.setNoticeType(NoticeType.社区公告.ordinal());
				resp.getAlerts().add(dto);
			}
			notice = this.noticeDao.getLatestNotice(courtyardId, userId, NoticeType.系统通知,user.getRegister_time());
			if(notice!=null && notice.getCreate_time()>messageStatus.getLast_system_time()){
				String content = "【系统通知】";
				if(notice.getContent().length()<20){
					content = content + notice.getContent();
				}else{
					content = content + notice.getContent().substring(0, 19);
				}
				PushMessageDto dto = new PushMessageDto(PushType.通知, content, 0);
				dto.setNoticeType(NoticeType.系统通知.ordinal());
				resp.getAlerts().add(dto);
			}
			notice = this.noticeDao.getLatestNotice(courtyardId, userId, NoticeType.验证通知,user.getRegister_time());
			if(notice!=null && notice.getCreate_time()>messageStatus.getLast_verify_time()){
				PushMessageDto dto = new PushMessageDto(PushType.通知, notice.getContent(), 0);
				dto.setNoticeType(NoticeType.验证通知.ordinal());
				resp.getAlerts().add(dto);
			}
			
			List<Long> activityIds = courtyardOfActivityDao.findActivitysForCourtyardId(courtyardId);
			if(!activityIds.isEmpty()){
				ActivityInfo info = activityInfoDao.getLatestActivity(activityIds);
				if(info!=null&&info.getCreateTime()>messageStatus.getLast_activity_time()){
					resp.getAlerts().add(new PushMessageDto(PushType.活动, "【活动通知】"+info.getActivity_title(), info.getId()));
				}
				info = activityInfoDao.getLatestNews(activityIds);
				if(info!=null&&info.getCreateTime()>messageStatus.getLast_news_time()){
					resp.getAlerts().add(new PushMessageDto(PushType.新闻, "【新闻资讯】"+info.getActivity_title(), info.getId()));
				}
			}
			CourtYard couryard = this.courtyardDao.get(courtyardId);
			if(couryard!=null){
				ContentEntity se = this.contentEntityDao.getLatestContentEntity(couryard.getCity_id());
				if(se!=null&&se.getCreate_time()>messageStatus.getLast_linju_subject_time()){
					resp.getAlerts().add(new PushMessageDto(PushType.专题, "【邻聚专题】"+se.getTitle(), se.getId()));
				}
			}
			
			UserFollow f = this.userFollowDao.getLatestFollowTime(userId);
			if(f!=null&&f.getCreate_time()>messageStatus.getLast_follow_me_time()){
				resp.getAlerts().add(new PushMessageDto(PushType.关注, "有邻居关注了你", 0));
			}
			AtRelations ar = this.atRelationsDao.getLatestAtRelations(userId);
			if(ar!=null&&ar.getCreate_time()>messageStatus.getLast_readed_atTime()){
				resp.getAlerts().add(new PushMessageDto(PushType.艾特, "有邻居提到了你", 0));
			}
		}

		if(us.isReply()){
//			PostReply pr = this.postReplyDao.getLatestReply(userId);
//			ActivityReply ar = this.activityReplyDao.getLatestReply(userId);
//			ContentEntityReply cer = this.contentEntityReplyDao.getLatestReply(userId);
//			if(pr!=null || ar!=null || cer!=null){
//				boolean haveNewReply = false;
//				if(pr!=null&&pr.getCreate_time()>messageStatus.getLast_readed_reply_time()){
//					haveNewReply = true;
//				}
//				if(ar!=null&&ar.getCreate_time()>messageStatus.getLast_readed_reply_time()){
//					haveNewReply = true;
//				}
//				if(cer!=null&&cer.getCreate_time()>messageStatus.getLast_readed_reply_time()){
//					haveNewReply = true;
//				}
//				if(haveNewReply)
//					resp.getAlerts().add(new PushMessageDto(PushType.评论, "你收到一条新的评论", 0));
//			}
			ReplyView r = this.replyDao.getLatestReply(userId);
			if(r!=null&&r.getCreateTime()>messageStatus.getLast_readed_reply_time()){
				resp.getAlerts().add(new PushMessageDto(PushType.评论, "你收到一条新的评论", 0));
			}
		}
		
		if(us.isLaud()){
//			PostLaud postLaud = this.postLaudDao.getLatestLaud(userId);
//			LikeInvitationMembers invitationLaud = this.likeInvitationMembersDao.getLatestInvitationLaud(userId);
//			ThankReply thankReply = this.thankReplyDao.getLatestThankReply(userId);
			LaudView lv = this.laudViewDao.getLatestLaud(userId);
			if(lv!=null){
				boolean haveNewLaud = lv.getCreate_time()>messageStatus.getLast_readed_laud_time();
//				if(postLaud!=null&&postLaud.getId()!=messageStatus.getLast_readed_laud_id()){
//					haveNewLaud = true;
//				}
//				if(invitationLaud!=null&&invitationLaud.getId()!=messageStatus.getLast_readed_invitation_like_id()){
//					haveNewLaud = true;
//				}
//				if(thankReply!=null&&thankReply.getId()!=messageStatus.getLast_think_reply()){
//					haveNewLaud = true;
//				}
				if(haveNewLaud){
					resp.getAlerts().add(new PushMessageDto(PushType.感谢, "你收到一条新的感谢", 0));
				}
			}
		}
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getPushMessage(int, java.lang.String)
	 */
	@Override
	@Transactional
	public Resp getPushMessage(int pf, String token) {
		List<User> users = this.userDao.find(Restrictions.eq("last_login_platform", pf),Restrictions.eq("last_login_token", token));
		if(users.isEmpty()){
			throw new GeneralLogicException("没有推送信息");
		}
		User currentUser = null;
		long lastLoginTime=0;
		for(User u : users){
			if(u.getLast_login_time() > lastLoginTime){
				currentUser = u;
				lastLoginTime = u.getLast_login_time();
			}
		}
		if(currentUser==null){
			return new Resp();
		}
		return getPushMessage(currentUser);
	}
	
	
	private void resetMessage(User user, PushMessageStatus messageStatus, int pushType, int noticeType) {
//		MessageStatus messageStatus = this.findMessageCheck(userId, courtyardId);
		long userId = user.getId();
		long courtyardId = user.getCourtyard_id();
		if(courtyardId<=0){
			return;
		}
		if(pushType==PushType.通知.ordinal()){
			if(noticeType==NoticeType.求助通知.ordinal()){
				Notice notice = this.noticeDao.getLatestNotice(courtyardId, userId,NoticeType.求助通知,user.getRegister_time());
				if(notice!=null){
					messageStatus.setLast_help_time(notice.getCreate_time());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}else if(noticeType==NoticeType.采纳通知.ordinal()){
				Notice notice = this.noticeDao.getLatestNotice(courtyardId, userId,NoticeType.采纳通知,user.getRegister_time());
				if(notice!=null){
					messageStatus.setLast_accpet_time(notice.getCreate_time());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}else if(noticeType==NoticeType.社区公告.ordinal()){
				Notice notice = this.noticeDao.getLatestNotice(courtyardId, userId,NoticeType.社区公告,user.getRegister_time());
				if(notice!=null){
					messageStatus.setLast_courtyard_time(notice.getCreate_time());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}else if(noticeType==NoticeType.系统通知.ordinal()){
				Notice notice = this.noticeDao.getLatestNotice(courtyardId, userId,NoticeType.系统通知,user.getRegister_time());
				if(notice!=null){
					messageStatus.setLast_system_time(notice.getCreate_time());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}else if(noticeType==NoticeType.验证通知.ordinal()){
				Notice notice = this.noticeDao.getLatestNotice(courtyardId, userId,NoticeType.验证通知,user.getRegister_time());
				if(notice!=null){
					messageStatus.setLast_verify_time(notice.getCreate_time());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}
		}else if(pushType==PushType.感谢.ordinal()){
//			PostLaud postLaud = this.postLaudDao.getLatestLaud(userId);
//			LikeInvitationMembers invitationLaud = this.likeInvitationMembersDao.getLatestInvitationLaud(userId);
//			ThankReply thankReply = this.thankReplyDao.getLatestThankReply(userId);
			LaudView lv = this.laudViewDao.getLatestLaud(userId);
			if(lv!=null){
				messageStatus.setLast_readed_laud_time(lv.getCreate_time());
			}
//			if(postLaud!=null){
//				messageStatus.setLast_readed_laud_id(postLaud.getId());
//			}
//			if(invitationLaud!=null){
//				messageStatus.setLast_readed_invitation_like_id(invitationLaud.getId());
//			}
//			if(thankReply!=null){
//				messageStatus.setLast_think_reply(thankReply.getId());
//			}
//			this.pushMessageStatusDao.saveOrUpdate(messageStatus);
		}else if(pushType==PushType.评论.ordinal()){
//			PostReply pr = this.postReplyDao.getLatestReply(userId);
//			ActivityReply ar = this.activityReplyDao.getLatestReply(userId);
//			ContentEntityReply cer = this.contentEntityReplyDao.getLatestReply(userId);
//			long lastTime = 0;
//			if(pr!=null){
//				lastTime = pr.getCreate_time();
//			}
//			if(ar!=null&&ar.getCreate_time()>lastTime){
//				lastTime = ar.getCreate_time();
//			}
//			if(cer!=null&&cer.getCreate_time()>lastTime){
//				lastTime = cer.getCreate_time();
//			}
//			if(lastTime>messageStatus.getLast_readed_reply_time()){
//				messageStatus.setLast_readed_reply_time(lastTime);
//			}
			ReplyView r = this.replyDao.getLatestReply(userId);
			if(r!=null&&r.getCreateTime()>messageStatus.getLast_readed_reply_time()){
				messageStatus.setLast_readed_reply_time(r.getCreateTime());
			}
//			this.pushMessageStatusDao.saveOrUpdate(messageStatus);
		}else if(pushType==PushType.关注.ordinal()){
			UserFollow f = this.userFollowDao.getLatestFollowTime(userId);
			if(f!=null){
				messageStatus.setLast_follow_me_time(f.getCreate_time());
//				this.pushMessageStatusDao.saveOrUpdate(messageStatus);
			}
		}else if(pushType==PushType.活动.ordinal()){
			List<Long> activityIds = courtyardOfActivityDao.findActivitysForCourtyardId(courtyardId);
			if(!activityIds.isEmpty()){
				ActivityInfo info = activityInfoDao.getLatestActivity(activityIds);
				if(info!=null){
					messageStatus.setLast_activity_time(info.getCreateTime());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}
		}else if(pushType==PushType.新闻.ordinal()){
			List<Long> activityIds = courtyardOfActivityDao.findActivitysForCourtyardId(courtyardId);
			if(!activityIds.isEmpty()){
				ActivityInfo info = activityInfoDao.getLatestNews(activityIds);
				if(info!=null){
					messageStatus.setLast_news_time(info.getCreateTime());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}
		}else if(pushType==PushType.专题.ordinal()){
			CourtYard couryard = this.courtyardDao.get(courtyardId);
			if(couryard!=null){
				ContentEntity se = this.contentEntityDao.getLatestContentEntity(couryard.getCity_id());
				if(se!=null){
					messageStatus.setLast_linju_subject_time(se.getCreate_time());
//					this.pushMessageStatusDao.saveOrUpdate(messageStatus);
				}
			}
		}else if(pushType==PushType.艾特.ordinal()){
			AtRelations ar = this.atRelationsDao.getLatestAtRelations(userId);
			if(ar!=null){
				messageStatus.setLast_readed_atTime(ar.getCreate_time());
			}
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#resetMessage(int, java.lang.String)
	 */
	@Override
	@Transactional
	public void resetMessage(int pf, String token, String types) {
		if(StringUtils.isBlank(types)){
			return;
		}
		List<User> users = this.userDao.find(Restrictions.eq("last_login_platform", pf),Restrictions.eq("last_login_token", token));
		if(users.isEmpty()){
			return;
		}
		User currentUser = null;
		long lastLoginTime=0;
		for(User u : users){
			if(u.getLast_login_time() > lastLoginTime){
				currentUser = u;
				lastLoginTime = u.getLast_login_time();
			}
		}
		if(currentUser==null){
			return;
		}
		PushMessageStatus messageStatus = this.findPushMessageCheck(currentUser.getId());
		String s[] = types.split("\\|");
		for(String type : s){
			String[] t = type.split(",");
			if(t.length==2){
				resetMessage(currentUser,messageStatus,Integer.parseInt(t[0]), Integer.parseInt(t[1]));
			}
		}
		this.pushMessageStatusDao.saveOrUpdate(messageStatus);
	}
	
	
	private int getNotReadMsgAmount(long userId) {
		String key = RedisKey.getKey(RedisKey.msg_not_read, String.valueOf(userId));
		long count = 0;
		try {
			HashOperations<String, String, Long> hashMsgCount = redisTemplate.opsForHash();
			// 没有记录未读聊天信息数，直接查询
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_chat)){
				// 未读聊天消息数
				long chatMsgCount = huanXinService.getOfflineMsgCount(userId);
				hashMsgCount.put(key, RedisKey.not_read_chat, chatMsgCount);
			}
			User user = null;
			MessageStatus messageStatus = null;
			// 没有记录未读回复数，直接查询
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_reply)){
				if(user==null){
					user = this.userDao.get(userId);
				}
				if(messageStatus == null){
					messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
				}
				// 未读回复数
				long notReadReply = userPostService.countNotReadReply(userId, user.getCourtyard_id(), messageStatus.getLast_readed_reply_id());
				// 加上活动中的回复数
				notReadReply+=activityReplyDao.countNotReadReply(userId, user.getCourtyard_id(), messageStatus.getLast_readed_activity_reply_id());
				hashMsgCount.put(key, RedisKey.not_read_reply, notReadReply);
			}
			// 没有记录未读感谢数
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_laud)){
				if(user==null){
					user = this.userDao.get(userId);
				}
				if(messageStatus == null){
					messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
				}
				// 未读点赞数
				long notReadLaud = userPostService.countNotReadLaud(userId, user.getCourtyard_id(), messageStatus.getLast_readed_laud_id());
				notReadLaud=notReadLaud+likeInvitationMembersDao.countNotReadLike(userId, user.getCourtyard_id(), messageStatus.getLast_readed_invitation_like_id());
				notReadLaud+=thankReplyDao.countNotReadThink(userId,messageStatus.getLast_think_reply());
				hashMsgCount.put(key, RedisKey.not_read_laud, notReadLaud);
			}
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_notice)){
				if(user==null){
					user = this.userDao.get(userId);
				}
				if(messageStatus == null){
					messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
				}
				// 未读通知数量
				long notReadNotice = noticeDao.countNotReadNotice(userId, user.getCourtyard_id(), messageStatus.getLast_notice(),user.getRegister_time());
				hashMsgCount.put(key, RedisKey.not_read_notice, notReadNotice);
			}
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_follow)){
				if(user==null){
					user = this.userDao.get(userId);
				}
				if(messageStatus == null){
					messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
				}
				// 未读关注数
				long notReadFollow = this.userFollowDao.countNotReadFollow(userId, messageStatus.getLast_follow_me_time());
				hashMsgCount.put(key, RedisKey.not_read_follow, notReadFollow);
			}
			
			List<Long> msgCounts = hashMsgCount.values(key);
			if (msgCounts != null && !msgCounts.isEmpty()) {
				for (long a : msgCounts) {
					count += a;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return (int)count;
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getAllAndUpdateNotReadReply(long, int)
	 */
	@Override
	@Transactional
	public int getAllAndUpdateNotReadReply(long userId, int amount) {
		try{
			String key = RedisKey.getKey(RedisKey.msg_not_read, String.valueOf(userId));
			HashOperations<String, String, Long> hashMsgCount = redisTemplate.opsForHash();
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_reply)){
				this.getNotReadMsgCount(userId, RedisKey.not_read_reply);
			}
			
			hashMsgCount.increment(key, RedisKey.not_read_reply, amount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return getNotReadMsgAmount(userId);
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getAllAndUpdateNotReadLaud(long, int)
	 */
	@Override
	@Transactional
	public int getAllAndUpdateNotReadLaud(long userId, int amount) {
		try{
			String key = RedisKey.getKey(RedisKey.msg_not_read, String.valueOf(userId));
			HashOperations<String, String, Long> hashMsgCount = redisTemplate.opsForHash();
			// 没有记录未读感谢数
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_laud)){
				this.getNotReadMsgCount(userId, RedisKey.not_read_laud);
			}
			
			hashMsgCount.increment(key, RedisKey.not_read_laud, amount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return getNotReadMsgAmount(userId);
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getAllAndUpdateNotReadNotice(long, int)
	 */
	@Override
	@Transactional
	public int getAllAndUpdateNotReadNotice(long userId, int amount) {
		try{
			String key = RedisKey.getKey(RedisKey.msg_not_read, String.valueOf(userId));
			HashOperations<String, String, Long> hashMsgCount = redisTemplate.opsForHash();
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_notice)){
				this.getNotReadMsgCount(userId, RedisKey.not_read_notice);
			}
			hashMsgCount.increment(key, RedisKey.not_read_notice, amount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return getNotReadMsgAmount(userId);
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#getAllAndUpdateNotReadFriend(long, int)
	 */
	@Override
	@Transactional
	public int getAllAndUpdateNotReadFollow(long userId, int amount) {
		try{
			String key = RedisKey.getKey(RedisKey.msg_not_read, String.valueOf(userId));
			HashOperations<String, String, Long> hashMsgCount = redisTemplate.opsForHash();
			if(!hashMsgCount.hasKey(key, RedisKey.not_read_follow)){
				this.getNotReadMsgCount(userId, RedisKey.not_read_follow);
			}
			hashMsgCount.increment(key, RedisKey.not_read_follow, amount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return getNotReadMsgAmount(userId);
	}
	
	/**
	 * @see cn.dayuanzi.service.IMessageCheckService#clearNotReadMsgCount(long, java.lang.String)
	 */
	@Override
	public void clearNotReadMsgCount(long userId, String msgType) {
		try{
			String key = RedisKey.getKey(RedisKey.msg_not_read, String.valueOf(userId));
			long count = 0;
			redisTemplate.opsForHash().put(key, msgType, count);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取指定消息类型的未读数
	 * redis未保存或者缓存中未读数为0时会进行一次同步
	 * 
	 * @param userId
	 * @param msgType
	 * @return
	 */
	
	@Transactional
	private int getNotReadMsgCount(long userId, String msgType) {
		int result = 0;
		try {
			String key = RedisKey.getKey(RedisKey.msg_not_read, String.valueOf(userId));
			HashOperations<String, String, Long> hashMsgCount = redisTemplate.opsForHash();
			// 是否需要同步数据
			boolean needSynchronize = false;
			if(!hashMsgCount.hasKey(key, msgType)){
				needSynchronize = true;
			}else{
				Long count = hashMsgCount.get(key, msgType);
				needSynchronize = count==null||count==0l;
			}
			if(needSynchronize){
				if(msgType.equals(RedisKey.not_read_reply)){
					User user = this.userDao.get(userId);
					MessageStatus messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
					// 未读回复数
					long notReadReply = userPostService.countNotReadReply(userId, user.getCourtyard_id(), messageStatus.getLast_readed_reply_id());
					// 加上活动中的回复数
					notReadReply+=activityReplyDao.countNotReadReply(userId, user.getCourtyard_id(), messageStatus.getLast_readed_activity_reply_id());
					hashMsgCount.put(key, RedisKey.not_read_reply, notReadReply);
				}else if(msgType.equals(RedisKey.not_read_laud)){
					User user = this.userDao.get(userId);
					MessageStatus messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
					// 未读点赞数
					long notReadLaud = userPostService.countNotReadLaud(userId, user.getCourtyard_id(), messageStatus.getLast_readed_laud_id());
					notReadLaud=notReadLaud+likeInvitationMembersDao.countNotReadLike(userId, user.getCourtyard_id(), messageStatus.getLast_readed_invitation_like_id());
					notReadLaud+=thankReplyDao.countNotReadThink(userId,messageStatus.getLast_think_reply());
					hashMsgCount.put(key, RedisKey.not_read_laud, notReadLaud);
				}else if(msgType.equals(RedisKey.not_read_follow)){
					User user = this.userDao.get(userId);
					MessageStatus messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
					// 未读关注请求
					long notReadFollowRequest = this.userFollowDao.countNotReadFollow(userId,messageStatus.getLast_follow_me_time());
					hashMsgCount.put(key, RedisKey.not_read_follow, notReadFollowRequest);
				}else if(msgType.equals(RedisKey.not_read_chat)){
					// 未读聊天消息数
					long chatMsgCount = huanXinService.getOfflineMsgCount(userId);
					hashMsgCount.put(key, RedisKey.not_read_chat, chatMsgCount);
				}else if(msgType.equals(RedisKey.not_read_notice)){
					User user = this.userDao.get(userId);
					MessageStatus messageStatus = this.findMessageCheck(userId, user.getCourtyard_id());
					// 未读通知数量
					long notReadNotice = noticeDao.countNotReadNotice(userId, user.getCourtyard_id(), messageStatus.getLast_notice(),user.getRegister_time());
					hashMsgCount.put(key, RedisKey.not_read_notice, notReadNotice);
				}
			}
			long count = hashMsgCount.get(key, msgType);
			result = (int)count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	@Transactional(readOnly = false)
	public AtRelationsListResp getAtList(int current_page, int max_num,long userId){
	    AtRelationsListResp resp = new AtRelationsListResp();
	    List<AtRelations> atuserids = this.atRelationsDao.findForPage(current_page, max_num, "create_time", false,Restrictions.eq("at_target_id",userId));
	    if(CollectionUtils.isNotEmpty(atuserids)){
			MessageStatus ms = findMessageCheck(userId, 0);
			long lateTime = ms.getLast_readed_atTime();
			for(AtRelations atuser : atuserids){
			    User user = this.userDao.get(atuser.getUser_id());
			    AtRelationsListDto atRelationsListDto  = new AtRelationsListDto(user,atuser);
			    // 邀约(1),邻居帮帮(2),分享(3),活动(4),专题(5)
			    if(atuser.getScene()==1){
					Invitation invitation = this.invitationDao.get(atuser.getAppend());
					if(invitation==null){
						continue;
					}
					atRelationsListDto.setContent(invitation.getContent());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    else if(atuser.getScene()==2){
					UserPost userPost = this.userPostDao.get(atuser.getAppend());
					if(userPost==null){
						continue;
					}
					atRelationsListDto.setContent(userPost.getContent());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    else if(atuser.getScene()==3){
					UserPost userPost = this.userPostDao.get(atuser.getAppend());
					if(userPost==null){
						continue;
					}
					atRelationsListDto.setContent(userPost.getContent());
					atRelationsListDto.setImages(userPost.getImage_names());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    else if(atuser.getScene()==4){
					ActivityInfo act = this.activityInfoDao.get(atuser.getAppend());
					if(act==null){
						continue;
					}
					atRelationsListDto.setContent(act.getActivity_title());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    else if(atuser.getScene()==5){
					ContentEntity con = this.contentEntityDao.get(atuser.getAppend());
					if(con==null){
						continue;
					}
					atRelationsListDto.setContent(con.getTitle());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    else if(atuser.getScene()==6){
			    	PostReply pr = this.postReplyDao.get(atuser.getAppend());
			    	if(pr==null){
						continue;
					}
			    	UserPost userPost = this.userPostDao.get(pr.getPost_id());
					if(userPost==null){
						continue;
					}
					atRelationsListDto.setPostId(userPost.getId());
					atRelationsListDto.setPostType(userPost.getContent_type());
					atRelationsListDto.setContent(pr.getContent());
					atRelationsListDto.setImages(userPost.getImage_names());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    else if(atuser.getScene()==7){
			    	ActivityReply ar = this.activityReplyDao.get(atuser.getAppend());
			    	if(ar==null){
			    		continue;
			    	}
			    	ActivityInfo info = this.activityInfoDao.get(ar.getActivity_id());
			    	if(info==null){
			    		continue;
			    	}
			    	atRelationsListDto.setPostId(info.getId());
					atRelationsListDto.setPostType(ContentType.活动.getValue());
					atRelationsListDto.setContent(ar.getContent());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    else if(atuser.getScene()==8){
			    	ContentEntityReply cer = this.contentEntityReplyDao.get(atuser.getAppend());
			    	if(cer==null){
			    		continue;
			    	}
					ContentEntity con = this.contentEntityDao.get(cer.getContent_id());
					if(con==null){
						continue;
					}
					atRelationsListDto.setPostId(con.getId());
					atRelationsListDto.setPostType(ContentType.专题.getValue());
					atRelationsListDto.setContent(cer.getContent());
					atRelationsListDto.setId(atuser.getId());
					atRelationsListDto.setDesc("邻居提醒到了你");
			    }
			    resp.getDatas().add(atRelationsListDto);
			    if(atuser.getCreate_time()>lateTime){
			    	lateTime = atuser.getCreate_time();
			    }
			}
			if(lateTime>ms.getLast_readed_atTime()){
			    ms.setLast_readed_atTime(lateTime);
			    this.messageStatusDao.saveOrUpdate(ms);
			    PushMessageStatus pms = this.findPushMessageCheck(userId);
				pms.setLast_readed_atTime(lateTime);
			}
	    }
	    return resp;
	}
}
