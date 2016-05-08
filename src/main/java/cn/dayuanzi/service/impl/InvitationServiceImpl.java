package cn.dayuanzi.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.config.ExpInfo;
import cn.dayuanzi.config.InvitationConfig;
import cn.dayuanzi.config.Settings;
import cn.dayuanzi.dao.AtRelationsDao;
import cn.dayuanzi.dao.BlackListDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.ExpDetailDao;
import cn.dayuanzi.dao.HouseOwnersDao;
import cn.dayuanzi.dao.InvitationDao;
import cn.dayuanzi.dao.InvitationDiscussGroupDao;
import cn.dayuanzi.dao.InvitationSignUpDao;
import cn.dayuanzi.dao.LikeInvitationMembersDao;
import cn.dayuanzi.dao.MessageStatusDao;
import cn.dayuanzi.dao.PostReplyDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserFollowDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.ExpDetail;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.InvitationDiscussGroupMember;
import cn.dayuanzi.model.InvitationSignUp;
import cn.dayuanzi.model.LikeInvitationMembers;
import cn.dayuanzi.model.MessageStatus;
import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.model.PushMessageStatus;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserDaily;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.pojo.ThingsAdder;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.DiscussGroupResp;
import cn.dayuanzi.resp.InvitationListResp;
import cn.dayuanzi.resp.InvitationResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.dto.DiscussGroupDto;
import cn.dayuanzi.resp.dto.InvitationDto;
import cn.dayuanzi.resp.dto.ReplyDto;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.IMessageCheckService;
import cn.dayuanzi.service.INoticeService;
import cn.dayuanzi.service.IUserCollectService;
import cn.dayuanzi.service.IUserPostService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.ApnsUtil;
import cn.dayuanzi.util.DateTimeUtil;
import cn.dayuanzi.util.ImageUtil;
import cn.dayuanzi.util.YardUtils;

/**
 * 
 * @author : leihc
 * @date : 2015年4月20日 下午4:57:53
 * @version : 1.0
 */
@Service
public class InvitationServiceImpl implements IInvitationService {

	@Autowired
	private InvitationDao invitationDao;
	@Autowired
	private LikeInvitationMembersDao likeInvitationMembersDao;
	@Autowired
	private InvitationDiscussGroupDao invitationDiscussGroupDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private HouseOwnersDao houseOwnersDao;
	@Autowired
	private PostReplyDao postReplyDao;
	@Autowired
	private IUserCollectService userCollectService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IUserPostService userPostService;
	@Autowired
	private ExpDetailDao expDetailDao;
	@Autowired
	private UserFollowDao userFollowDao;
	@Autowired
	private BlackListDao blackListDao;
	@Autowired
	private CourtYardDao courtyardDao;
	@Autowired
	private InvitationSignUpDao invitationSignUpDao;
	@Autowired
	private INoticeService noticeService;
	@Autowired
	private IMessageCheckService messageCheckService;
	@Autowired
	private MessageStatusDao messageStatusDao;
	@Autowired
	private AtRelationsDao atRelationsDao;
	
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#findInvitationById(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Invitation findInvitationById(long invitationId) {
		return invitationDao.get(invitationId);
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#findInvitations(long, int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public InvitationListResp findInvitationsUser(long user_id, int current_page, int max_num) {

		// 判断是否被对方拉黑
		if(user_id!=UserSession.get().getUserId()){
			if(blackListDao.isBlackList(user_id, UserSession.get().getUserId())){
				throw new GeneralLogicException("您不能查看此人的邀约哦。");
			}
		}

		List<Invitation> invitations = this.invitationDao.findForPage(current_page, max_num, "create_time", false, Restrictions.eq("user_id", user_id));
		InvitationListResp resp = new InvitationListResp();
		if(CollectionUtils.isNotEmpty(invitations)){
			for(Invitation invitation : invitations){
				InvitationDto invitationDto = new InvitationDto(invitation);
				if(StringUtils.isNotBlank(invitation.getContent())&&invitation.getContent().contains("@")){
					invitationDto.setAts(this.atRelationsDao.findAtTargets(ContentType.邀约, invitation.getId()));
				}
				if(!UserSession.get().isVisitor()){
					invitationDto.setJoined(false);
					invitationDto.setCollected(userCollectService.findUserCollect(UserSession.get().getUserId(), ContentType.邀约.getValue(), invitation.getId())!=null);
					invitationDto.setClickLike(this.findLikeMember(UserSession.get().getUserId(), invitation.getId())!=null);
				}else{
					invitationDto.setJoined(false);
					invitationDto.setCollected(false);
					invitationDto.setClickLike(false);
				}
				resp.getDatas().add(invitationDto);
			}
		}
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#findInvitationsforCourtyard(long, int, int)
	 */
	@Override
	@Transactional(readOnly = false)
	public InvitationListResp findInvitationsforCourtyard(long courtyard_id, int current_page, int max_num) {
		CourtYard courtyard = this.courtyardDao.get(courtyard_id);
		int range = 5;
		if(!UserSession.get().getCourtyardsPerDistance().containsKey(range)){
			List<CourtYard> courtyards = courtyardDao.find(Restrictions.eq("city_id", courtyard.getCity_id()));
			List<Long> courtyardsIds = new ArrayList<Long>();
			for(CourtYard c : courtyards){
				if(c.getId()==courtyard_id){
					continue;
				}
				if(YardUtils.getDistance(courtyard.getLongitude(), courtyard.getLatitude(), c.getLongitude(), c.getLatitude()) < range*1000){
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
		List<Invitation> invitations = null;
		if(courtyardIds == null|| courtyardIds.isEmpty()){
			invitations = this.invitationDao.findForPage(current_page, max_num,"create_time", false,Restrictions.eq("courtyard_id", courtyard_id), Restrictions.gt("activity_time", current));
		}else{
			// where courtyard_id=courtyardId or courtyard_id in courtyardIds and show_around=true;
			Disjunction dis = Restrictions.disjunction();
			dis.add(Restrictions.eq("courtyard_id", courtyard_id));
			Conjunction con = Restrictions.conjunction();
			con.add(Restrictions.in("courtyard_id", courtyardIds));
			con.add(Restrictions.eq("show_around", true));
			dis.add(con);
			invitations = invitationDao.findForPage(current_page, max_num, "create_time", false, dis, Restrictions.gt("activity_time", current));
		}
		InvitationListResp resp = new InvitationListResp();
		if(CollectionUtils.isNotEmpty(invitations)){
			boolean needSort = false;
			MessageStatus messageStatus = this.messageCheckService.findMessageCheck(UserSession.get().getUserId(), courtyard_id);
			long maxinvitationtime = messageStatus.getLast_invitation();
			for(Invitation invitation : invitations){
				InvitationDto invitationDto = new InvitationDto(invitation);
//				if(StringUtils.isNotBlank(invitation.getContent())&&invitation.getContent().contains("@")){
//					List<String> r = YardUtils.displayAt(invitation.getContent()); 
//					for(String at : r){
//						User u = this.userDao.get(Long.parseLong(at.substring(at.lastIndexOf('{')+1, at.lastIndexOf('}'))));
//						if(u!=null){
//							invitationDto.getAts().add(new AtDto(u));
//						}
//					}
//				}
				if(StringUtils.isNotBlank(invitation.getContent())&&invitation.getContent().contains("@")){
					invitationDto.setAts(this.atRelationsDao.findAtTargets(ContentType.邀约, invitation.getId()));
				}
				if(!UserSession.get().isVisitor()){
					invitationDto.setJoined(this.findDiscussGroupMember(invitation.getId(), UserSession.get().getUserId())!=null);
					invitationDto.setCollected(userCollectService.findUserCollect(UserSession.get().getUserId(), ContentType.邀约.getValue(), invitation.getId())!=null);
					invitationDto.setClickLike(this.findLikeMember(UserSession.get().getUserId(), invitation.getId())!=null);
				}else{
					invitationDto.setJoined(false);
					invitationDto.setCollected(false);
					invitationDto.setClickLike(false);
				}
				if(invitation.getCourtyard_id()!=courtyard_id){
					CourtYard cy = this.courtyardDao.get(invitation.getCourtyard_id());
					invitationDto.setDistance(YardUtils.getDistance(courtyard.getLongitude(),courtyard.getLatitude(),cy.getLongitude(),cy.getLatitude()));
					needSort = true;
				}
				resp.getDatas().add(invitationDto);
				//把已经看过的最大的邀约创建时间 放到max
				if(invitation.getCreate_time()>maxinvitationtime){
				    maxinvitationtime = invitation.getCreate_time();
				}
			}
			//如果max和数据库存的不一致  则更新
			if(maxinvitationtime!=messageStatus.getLast_invitation()){
			    messageStatus.setLast_invitation(maxinvitationtime);
			}
			this.messageStatusDao.save(messageStatus);
			if(needSort){
				Collections.sort(resp.getDatas());
			}
			
		}
		
		return resp;
	}

	/**
	 * @see cn.dayuanzi.service.IInvitationService#countInterestForInvitation(cn.dayuanzi.model.Invitation)
	 */
	@Override
	@Transactional(readOnly = true)
	public int countLikeInvitation(Invitation invitation) {
		long result = likeInvitationMembersDao.count("invitation_id",invitation.getId());
		return (int)result;
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#addInterestForInvitation(long, long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void addLikePeopleForInvitation(long courtyardId,long userId, long targetId) {
		Invitation invitation = this.invitationDao.get(targetId);
		if(invitation == null){
			throw new GeneralLogicException("这条邀约不存在");
		}
//		if(courtyardId!=invitation.getCourtyard_id()){
//			throw new GeneralLogicException("您不是这个院子的业主，不能操作哦");
//		}
		LikeInvitationMembers exist = this.likeInvitationMembersDao.findUnique(Restrictions.eq("invitation_id", targetId),Restrictions.eq("user_id", userId));
		if(exist!=null){
			throw new GeneralLogicException("已经点过赞了");
		}
		LikeInvitationMembers lim = new LikeInvitationMembers(courtyardId, userId, targetId,invitation.getUser_id());
		likeInvitationMembersDao.save(lim);
		
		UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(invitation.getUser_id());
		ExpInfo info = ThingsAdder.被感谢.getExpInfo();
		if(userDaily.getBe_thank_count() < info.getLimitDaily()){
			User replyer = this.userDao.get(invitation.getUser_id());
			replyer.addExp(info.getExp());
			ExpDetail exp = new ExpDetail(replyer.getId(),info.getExp(),info.getRemark());
			this.expDetailDao.save(exp);
			userDaily.setBe_thank_count(userDaily.getBe_thank_count()+1);
		}
		
		MessageStatus ms = this.messageCheckService.findMessageCheck(userId, 0);
//		ms.setLast_readed_invitation_like_id(lim.getId());
		ms.setLast_readed_laud_time(lim.getCreate_time());
		PushMessageStatus pms = this.messageCheckService.findPushMessageCheck(userId);
//		pms.setLast_readed_invitation_like_id(lim.getId());
		pms.setLast_readed_laud_time(lim.getCreate_time());
		
		List<Long> temp = likeInvitationMembersDao.getRelatedUsers(targetId);
		temp.add(invitation.getUser_id());
		Set<Long> lauderIds=new HashSet<Long>();
		lauderIds.addAll(temp);
		lauderIds.remove(userId);
		if(!lauderIds.isEmpty()){
			for(long replateId : lauderIds){
				User relateUser = this.userDao.get(replateId);
				int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadLaud(replateId, 1);
				UserSetting setting = this.userService.getUserSetting(replateId);
				if(setting.isLaud()){
					ApnsUtil.getInstance().send(relateUser, PushType.感谢, "你收到一条新的感谢",0, count);
				}else{
					ApnsUtil.getInstance().send(relateUser, count);
				}
			}
		}
		
//		User postSender = this.userDao.get(invitation.getUser_id());
//		int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadLaud(invitation.getUser_id(), 1);
//		UserSetting setting = this.userService.getUserSetting(invitation.getUser_id());
//		if(setting.isLaud()){
//			ApnsUtil.getInstance().send(postSender, count);
//		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#sendInvitation(long, long, int, long, java.lang.String, java.lang.String, org.springframework.web.multipart.commons.CommonsMultipartFile[])
	 */
	@Override
	@Transactional(readOnly = false)
	public Invitation sendInvitation(long courtyardId, long userId,
			int invitation_type, long activityTime, String activityPlace,
			String content, CommonsMultipartFile[] images,boolean showAround) {
		User user = this.userDao.get(userId);
		if(user.isBanned()){
			throw new GeneralLogicException("您已被禁言");
		}
		List<User> atUsers = YardUtils.findAt(content);
		
		Invitation invitation = new Invitation();
		invitation.setCourtyard_id(courtyardId);
		invitation.setUser_id(userId);
		invitation.setInvitation_type(invitation_type);
		invitation.setActivity_time(activityTime);
		invitation.setActivity_place(activityPlace);
		invitation.setContent(content);
		invitation.setCreate_time(System.currentTimeMillis());
		invitation.setShow_around(showAround);
//		String group_name = InvitationConfig.getInterests().get(invitation_type).getInvitation()+"@"+user.getNickname();
//		invitation.setGroup_name(group_name);
		this.invitationDao.save(invitation);
//		InvitationDiscussGroupMember meber =new InvitationDiscussGroupMember(invitation.getId(),courtyardId,userId);
//		this.invitationDiscussGroupDao.save(meber);
		if(images!=null){
			invitation.setImage_names(ImageUtil.savePostImage(invitation.getId(), Settings.INVITATION_IMAGE_DIRE, images));
		}
		invitationSignUpDao.save(new InvitationSignUp(userId, invitation.getId()));

		UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(userId);
		ExpInfo info = ThingsAdder.发邀约.getExpInfo();
		if(userDaily.getSend_invitation_count() < info.getLimitDaily()){
			user.addExp(info.getExp());
			this.expDetailDao.save(new ExpDetail(userId,info.getExp(),info.getRemark()));
			userDaily.setSend_invitation_count(userDaily.getSend_invitation_count()+1);
		}
		if(atUsers!=null){
			for(User u : atUsers){
				AtRelations ar = new AtRelations(userId, u);
				ar.setScene(ContentType.邀约.getValue());
				ar.setAppend(invitation.getId());
				this.atRelationsDao.save(ar);
			}
		}
		return invitation;
	}
	
	@Override
	@Transactional
	public void replyInvitation(long user_id , int replyType, long targetId, long atReplyerId, String content) {
		User user = this.userDao.get(user_id);
		if(user.isBanned()){
			throw new GeneralLogicException("您已被禁言");
		}
		Invitation invitation = null;
		// 评论帖子
		if(replyType==1){
			invitation = this.invitationDao.get(targetId);
			if(invitation == null){
				throw new GeneralLogicException("回复的邀约不存在哦");
			}
			List<User> atUsers = YardUtils.findAt(content);
			
			PostReply reply = new PostReply(user, targetId,ContentType.邀约.getValue(), content);
			reply.setPost_sender_id(invitation.getUser_id());
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
			
		}else{
			// 回复评论
			PostReply replyTarget = this.postReplyDao.get(targetId);
			if(replyTarget == null){
				throw new GeneralLogicException("回复的评论不存在哦");
			}
//			if(user.getCourtyard_id()!=replyTarget.getCourtyard_id()){
//				throw new GeneralLogicException("只能回复当前所在院子的邀约");
//			}
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
			
			invitation = this.invitationDao.get(replyTarget.getPost_id());
			PostReply reply = new PostReply(user, invitation.getId(), ContentType.邀约.getValue(),content);
			reply.setPost_sender_id(invitation.getUser_id());
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
//			User invitation_sender = this.userDao.get(invitation.getUser_id());
//			if(invitation_sender.isValidIOSUser()){
//				int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadReply(invitation.getUser_id(), 1);
//				ApnsUtil.getInstance().send(invitation_sender, count);
//			}
//			if(atReplyer.isValidIOSUser()){
//				int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadReply(atReplyer.getId(), 1);
//				ApnsUtil.getInstance().send(atReplyer, count);
//			}
		}
		
		List<Long> temp = this.postReplyDao.getReplyers(invitation.getId(), ContentType.邀约.getValue());
		temp.add(invitation.getUser_id());
		Set<Long> replyerIds = new HashSet<Long>();
		replyerIds.addAll(temp);
		replyerIds.remove(user_id);
		if(!replyerIds.isEmpty()){
			List<User> replyers = this.userDao.get(replyerIds);
			for(User replyer : replyers){
				int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadReply(replyer.getId(), 1);
				UserSetting setting = this.userService.getUserSetting(replyer.getId());
				if(setting.isReply()){
					ApnsUtil.getInstance().send(replyer, PushType.评论, "你收到一条新的评论", 0,count);
				}else{
					ApnsUtil.getInstance().send(replyer, count);
				}
			}
		}
	}
	/**
	 * @see cn.dayuanzi.service.IInvitationService#joinDiscussGroup(long, long)
	 */
	@Override
	@Transactional
	public InvitationDiscussGroupMember joinDiscussGroup(long courtyardId, long userId, long invitaionId) {
		Invitation invitation = this.invitationDao.get(invitaionId);
		if(invitation == null){
			throw new GeneralLogicException("讨论组不存在");
		}
		if(invitation.getCourtyard_id()!=courtyardId){
			throw new GeneralLogicException("只能加入您当前所在院子的讨论组里哦");
		}
		InvitationDiscussGroupMember group = this.invitationDiscussGroupDao.findUnique(Restrictions.eq("invitation_id", invitaionId), Restrictions.eq("user_id", userId));
		if(group!=null){
			throw new GeneralLogicException("您已经加入讨论组了");
		}
		group = new InvitationDiscussGroupMember();
		group.setInvitation_id(invitaionId);
		group.setUser_id(userId);
		this.invitationDiscussGroupDao.save(group);
		if(Settings.USE_INTERNAL_IM){
			ServiceRegistry.getHuanXinService().joinDiscussGroup(userId, invitation.getGroup_id());
		}
		return group;
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#exitDiscussGroup(long, long, long)
	 */
	@Override
	@Transactional
	public void exitDiscussGroup(long courtyardId, long userId, long invitaionId) {
		// TODO Auto-generated method stub
		Invitation invitation = this.invitationDao.get(invitaionId);
		if(invitation == null){
			throw new GeneralLogicException("讨论组不存在");
		}
		if(invitation.getCourtyard_id()!=courtyardId){
			throw new GeneralLogicException("只能退出您当前所在院子的讨论组哦");
		}
		InvitationDiscussGroupMember group = this.invitationDiscussGroupDao.findUnique(Restrictions.eq("invitation_id", invitaionId), Restrictions.eq("user_id", userId));
		if(group==null){
			throw new GeneralLogicException("您不在这个讨论组哦。");
		}
		this.invitationDiscussGroupDao.delete(group);
		if(Settings.USE_INTERNAL_IM){
			ServiceRegistry.getHuanXinService().exitDiscussGroup(userId, invitation.getGroup_id());
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#getDiscussGroup(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public DiscussGroupResp getDiscussGroup(long userId, long courtyardId) {
		DiscussGroupResp resp = new DiscussGroupResp();
		List<InvitationDiscussGroupMember> groups = this.invitationDiscussGroupDao.find(Restrictions.eq("courtyard_id", courtyardId),Restrictions.eq("user_id", userId));
		for(InvitationDiscussGroupMember group : groups){
			int memberAmount = (int)this.invitationDiscussGroupDao.count("invitation_id", group.getInvitation_id());
			Invitation invitation = this.invitationDao.get(group.getInvitation_id());
			DiscussGroupDto dto =new DiscussGroupDto(invitation,memberAmount);
			for(int i=0;i<4;i++){
				User user = this.userDao.get(group.getUser_id());
				dto.setHeadicon(user.getHead_icon());
			}
			resp.getDatas().add(dto);
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#countDiscussGroupMembers(cn.dayuanzi.model.Invitation)
	 */
	@Override
	@Transactional(readOnly = true)
	public int countDiscussGroupMembers(Invitation invitation) {
		int memberAmount = (int)this.invitationDiscussGroupDao.count("invitation_id", invitation.getId());
		return memberAmount;
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#findDiscussGroupMember(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public InvitationDiscussGroupMember findDiscussGroupMember(
			long invitationId, long userId) {
		InvitationDiscussGroupMember group = this.invitationDiscussGroupDao.findUnique(Restrictions.eq("invitation_id", invitationId), Restrictions.eq("user_id", userId));
		return group;
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#findLikeMember(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public LikeInvitationMembers findLikeMember(long user_id, long invitationId) {
		return this.likeInvitationMembersDao.findUnique(Restrictions.eq("invitation_id", invitationId),Restrictions.eq("user_id", user_id));
	}
	
	@Transactional(readOnly = true)
	public List<PostReply> findReplys(long invitationId) {
		return postReplyDao.findPostReplys("create_time",false,Restrictions.eq("post_id", invitationId),Restrictions.eq("content_type", ContentType.邀约.getValue()),Restrictions.eq("reply_id", 0l));
	}
	
	@Transactional(readOnly = true)
	public List<PostReply> findReplys(long invitationId, long reply_id) {
		return postReplyDao.findPostReplys("create_time",true,Restrictions.eq("post_id", invitationId),Restrictions.eq("content_type", ContentType.邀约.getValue()),Restrictions.eq("reply_id", reply_id));
	}
	
	@Override
	@Transactional(readOnly = true)
	public long countReply(long invitationId) {
		return this.postReplyDao.count(Restrictions.eq("post_id", invitationId),Restrictions.eq("content_type", ContentType.邀约.getValue()));
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#getInvitationDetails(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public Resp getInvitationDetails(long invitationId) {
		Invitation invitation = findInvitationById(invitationId);
		if(invitation == null){
			throw new GeneralLogicException("该话题不存在或已被删除了哦");
		}
		UserSession userSession = UserSession.get();
		InvitationResp resp = new InvitationResp(invitation);
		resp.setReplyAmount(this.countReply(invitationId));
//		if(StringUtils.isNotBlank(invitation.getContent())&&invitation.getContent().contains("@")){
//			List<String> r = YardUtils.displayAt(invitation.getContent()); 
//			for(String at : r){
//				User u = this.userDao.get(Long.parseLong(at.substring(at.lastIndexOf('{')+1, at.lastIndexOf('}'))));
//				if(u!=null){
//					resp.getAts().add(new AtDto(u));
//				}
//			}
//		}
		if(StringUtils.isNotBlank(invitation.getContent())&&invitation.getContent().contains("@")){
			resp.setAts(this.atRelationsDao.findAtTargets(ContentType.邀约, invitation.getId()));
		}
		if(userSession.isVisitor()){
			resp.setJoined(false);
			resp.setCollected(false);
			resp.setClickLike(false);
		}else{
			resp.setJoined(this.findDiscussGroupMember(invitation.getId(), userSession.getUserId())!=null);
			resp.setJoined(false);
			resp.setCollected(userCollectService.findUserCollect(userSession.getUserId(), ContentType.邀约.getValue(), invitation.getId())!=null);
			resp.setClickLike(this.findLikeMember(userSession.getUserId(), invitation.getId())!=null);
		}
		
		// 帖子的评论
		List<PostReply> replys = this.findReplys(invitationId);
		if(CollectionUtils.isNotEmpty(replys)){
			List<ReplyDto> replyDtos = new ArrayList<ReplyDto>();
			for(PostReply reply : replys){
				ReplyDto dto = new ReplyDto(reply);
				User replyer = this.userService.findUserById(reply.getReplyer_id());
				dto.setSenderHeadIcon(replyer.getHead_icon());
				dto.setSenderName(replyer.getNickname());
				dto.setThanked(userPostService.isThankForReply(userSession.getUserId(), reply.getId()));
				dto.setThankAmount(userPostService.countThankAmountForReply(reply.getId()));
				if(StringUtils.isNotBlank(reply.getContent())&&reply.getContent().contains("@")){
					dto.setAts(this.atRelationsDao.findAtTargets(ContentType.帖子评论, reply.getId()));
				}
				// 评论下的回复
				List<PostReply> replyAts = this.findReplys(invitationId, reply.getId());
				if(CollectionUtils.isNotEmpty(replyAts)){
					List<ReplyDto> replyAtDtos = new ArrayList<ReplyDto>();
					for(PostReply postReply : replyAts){
						ReplyDto atdto = new ReplyDto(postReply);
						User user = this.userService.findUserById(postReply.getReplyer_id());
						atdto.setSenderName(user.getNickname());
						User atUser = this.userService.findUserById(postReply.getAt_targetId());
						atdto.setAtTargetId(postReply.getAt_targetId());
						atdto.setAtTargetName(atUser.getNickname());
						replyAtDtos.add(atdto);
						if(StringUtils.isNotBlank(postReply.getContent())&&postReply.getContent().contains("@")){
							atdto.setAts(this.atRelationsDao.findAtTargets(ContentType.帖子评论, postReply.getId()));
						}
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
	public Resp shareInvitation(long invitationId) {
		Invitation invitation = findInvitationById(invitationId);
		if(invitation == null){
			throw new GeneralLogicException("该邀约不存在或已被删除了哦");
		}
		InvitationResp resp = new InvitationResp(invitation);
//		resp.setReplyAmount(this.countReply(invitationId));
		
		// 帖子的评论
		List<PostReply> replys = this.findReplys(invitationId);
		if(CollectionUtils.isNotEmpty(replys)){
			List<ReplyDto> replyDtos = new ArrayList<ReplyDto>();
			for(PostReply reply : replys){
				ReplyDto dto = new ReplyDto(reply);
				User replyer = this.userService.findUserById(reply.getReplyer_id());
				dto.setSenderHeadIcon(replyer.getHead_icon());
				dto.setSenderName(replyer.getNickname());
				replyDtos.add(dto);
			}
			resp.setReplys(replyDtos);
		}
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IInvitationService#invitationSignUp(long, long)
	 */
	@Override
	@Transactional
	public void invitationSignUp(long userId, long invitationId) {
		Invitation invitation = findInvitationById(invitationId);
		if(invitation == null){
			throw new GeneralLogicException("该邀约不存在或已被删除了哦");
		}
		invitationSignUpDao.save(new InvitationSignUp(userId, invitationId));
		String content = "你成功报名了“{0}{1}{2}”邀约，你将获得{3}个经验值";
		int interestId = invitation.getInvitation_type();
		String activity_type = InvitationConfig.getInterests().get(interestId).getInvitation();
		String activity_time =DateTimeUtil.formatDateTime(invitation.getActivity_time(),"MM月dd日 HH:mm");
		content = MessageFormat.format(content, activity_type,activity_time,invitation.getActivity_place());
		noticeService.sendNoticeToUser(NoticeType.邀约通知,userId, "你成功报名了一个邀约", content,invitationId);
		User user = this.userDao.get(userId);
		content = "{0}报名了“{1}{2}{3}”的邀约";
		content = MessageFormat.format(content, user.getNickname(),activity_type,activity_time,invitation.getActivity_place());
		noticeService.sendNoticeToUser(NoticeType.邀约通知,invitation.getUser_id(), "你发起的邀约有人报名",content,invitationId);
	}
}
