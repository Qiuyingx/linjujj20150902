package cn.dayuanzi.service.impl;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.config.CareerConfig;
import cn.dayuanzi.config.ExpInfo;
import cn.dayuanzi.config.InterestConf;
import cn.dayuanzi.config.PostTemplateConfig;
import cn.dayuanzi.config.Settings;
import cn.dayuanzi.config.LindouInfo;
import cn.dayuanzi.config.SysMessages;
import cn.dayuanzi.dao.BlackListDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.ExpDetailDao;
import cn.dayuanzi.dao.FriendsDao;
import cn.dayuanzi.dao.HouseOwnersDao;
import cn.dayuanzi.dao.InvitationDao;
import cn.dayuanzi.dao.LindouDetailDao;
import cn.dayuanzi.dao.MessageStatusDao;
import cn.dayuanzi.dao.NoticeDao;
import cn.dayuanzi.dao.OpenTrainDao;
import cn.dayuanzi.dao.PostReplyDao;
import cn.dayuanzi.dao.ThankReplyDao;
import cn.dayuanzi.dao.TrainInfoDao;
import cn.dayuanzi.dao.UserDailyDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserErrorDao;
import cn.dayuanzi.dao.UserFeedbakcDao;
import cn.dayuanzi.dao.UserFollowDao;
import cn.dayuanzi.dao.UserInterestDao;
import cn.dayuanzi.dao.UserLinDouDao;
import cn.dayuanzi.dao.UserPostDao;
import cn.dayuanzi.dao.UserSettingDao;
import cn.dayuanzi.dao.UserStarDao;
import cn.dayuanzi.dao.ValidateUserDao;
import cn.dayuanzi.dao.VersionCheckeDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.BlackList;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.ExpDetail;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.LindouDetail;
import cn.dayuanzi.model.MessageStatus;
import cn.dayuanzi.model.OpenTrain;
import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.model.ThankReply;
import cn.dayuanzi.model.TrainInfo;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserDaily;
import cn.dayuanzi.model.UserError;
import cn.dayuanzi.model.UserFeedback;
import cn.dayuanzi.model.UserFollow;
import cn.dayuanzi.model.UserInterest;
import cn.dayuanzi.model.UserLinDou;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.model.UserStar;
import cn.dayuanzi.model.ValidateUser;
import cn.dayuanzi.model.VersionChecke;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.pojo.ThingsAdder;
import cn.dayuanzi.pojo.ExternalType;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.BlackNameListResp;
import cn.dayuanzi.resp.DatasResp;
import cn.dayuanzi.resp.LaudListResp;
import cn.dayuanzi.resp.MainPageResp;
import cn.dayuanzi.resp.MainResp;
import cn.dayuanzi.resp.MyHouseResp;
import cn.dayuanzi.resp.PersonalDataResp;
import cn.dayuanzi.resp.RecommendResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.TaskResp;
import cn.dayuanzi.resp.VersionCheckeResp;
import cn.dayuanzi.resp.dto.BlackNameListDto;
import cn.dayuanzi.resp.dto.EveryTaskDto;
import cn.dayuanzi.resp.dto.ExpDetailDto;
import cn.dayuanzi.resp.dto.InvitationsDto;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.resp.dto.LevelExpDto;
import cn.dayuanzi.resp.dto.LindouDetailDto;
import cn.dayuanzi.resp.dto.NewTaskDto;
import cn.dayuanzi.resp.dto.PostDetailDto;
import cn.dayuanzi.resp.dto.RecommendDto;
import cn.dayuanzi.resp.dto.UserStarDto;
import cn.dayuanzi.service.IMessageCheckService;
import cn.dayuanzi.service.INoticeService;
import cn.dayuanzi.service.IRedisService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.ApnsUtil;
import cn.dayuanzi.util.DateTimeUtil;
import cn.dayuanzi.util.ImageUtil;


/**
 * 
 * @author : lhc
 * @date : 2015年4月10日 下午5:35:58
 * @version : 1.0
 */


@Service
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private HouseOwnersDao houseOwnersDao;
	@Autowired
	private CourtYardDao courtYardDao;
	@Autowired
	private ValidateUserDao validateUserDao;
	@Autowired
	private UserPostDao userPostDao;
	@Autowired
	private InvitationDao invitationDao;
	@Autowired
	private UserInterestDao userInterestDao;
	@Autowired
	private IRedisService redisService;
	@Autowired
	private MessageStatusDao messageStatusDao;
	@Autowired
	private UserFeedbakcDao userFeedbackDao;
	@Autowired 
	private UserErrorDao userErrorDao;
	@Autowired 
	private VersionCheckeDao versionCheckeDao;
	@Autowired
	private FriendsDao friendsDao;
	@Autowired
	private UserSettingDao userSettingDao;
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private UserLinDouDao userLinDouDao;
	@Autowired
	private PostReplyDao postReplyDao;
	@Autowired
	private UserDailyDao userDailyDao;
	@Autowired
	private LindouDetailDao lindouDetailDao;
	@Autowired
	private ThankReplyDao thankReplyDao;
	@Autowired
	private ExpDetailDao expDetailDao;
	@Autowired
	private IMessageCheckService messageCheckService;
	@Autowired
	private BlackListDao blackListDao;
	@Autowired
	private UserFollowDao userFollowDao;
	@Autowired
	private INoticeService noticeService;
	@Autowired
	private UserStarDao userStarDao;
	@Autowired
	private OpenTrainDao openTrainDao;
	@Autowired
	private TrainInfoDao trainInfoDao;
	
	
	/**
	 * @see cn.dayuanzi.service.IUserService#findUserByName(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public User findUserByName(String nickName) {
		return this.userDao.findUniqueBy("nickname", nickName);
	}
	/**
	 * @see cn.dayuanzi.service.IUserService#findUserById(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public User findUserById(long userId) {
		return this.userDao.get(userId);
	}
	/**
	 * @see cn.dayuanzi.service.IUserService#findUserByTel(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public User findUserByTel(String tel) {
		return this.userDao.findUniqueBy("tel", tel);
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#findUserByExternalId(java.lang.String, cn.dayuanzi.pojo.ExternalType)
	 */
	@Override
	@Transactional(readOnly = true)
	public User findUserByExternalId(String openId, ExternalType externalType) {
		if(externalType==ExternalType.邻聚){
			throw new GeneralLogicException("请求错误");
		}
		return this.userDao.findUnique(Restrictions.eq("openId", openId),Restrictions.eq("externalType", externalType.ordinal()));
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#saveUser(cn.dayuanzi.model.User)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveUser(User user) {
		this.userDao.saveOrUpdate(user);
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getPersonalData(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public PersonalDataResp getPersonalData(long userId) {
		PersonalDataResp resp = new PersonalDataResp();
		User user = this.userDao.get(userId);
		resp.setUserId(userId);
		resp.setTel(user.getTel());
		resp.setLingdou(this.getUserLinDou(userId).getAmount());
		resp.setTel(user.getTel());
		if(user.getBirthday()!=null){
			resp.setAge(DateTimeUtil.getAgeData(user.getBirthday()));
			resp.setBirthday(DateTimeUtil.formatDateTime(user.getBirthday(), "yyyy-MM-dd"));
		}
		UserSetting setting = this.userSettingDao.get(userId);
		if(setting!=null ){
			resp.setLaud(setting.isLaud());
			resp.setMessage(setting.isMessage());
			resp.setReply(setting.isReply());
			resp.setSystem(setting.isSystem());
		}else{
			resp.setLaud(true);
			resp.setMessage(true);
			resp.setReply(true);
			resp.setSystem(true);
		}
		resp.setSignature(user.getSignature());
		resp.setNickname(user.getNickname());
		resp.setGender(user.getGender());
		resp.setHeadIcon(user.getHead_icon());
		resp.setCareer(CareerConfig.getCareers().get(user.getCareerId()));
		if(user.getCourtyard_id()>0){
			CourtYard courtyard = courtYardDao.get(user.getCourtyard_id());
			resp.setCourtyardName(courtyard.getCourtyard_name());
			resp.setAlly(courtyard.getAlly());//返回当前用户所在社区是否合作 
		}else{
			resp.setCourtyardName("0");
			resp.setAlly(0);//返回当前用户所在社区是否合作 
		}
	
//		resp.setValidate(validateUserDao.isValidate(userId, user.getCourtyard_id()));
		resp.setCourtyardId(user.getCourtyard_id());
	
		//返回技能
		if(user.getSkill()!=null){
			resp.setInfo(user.getSkill());
		}else{
			if(user.getCareerId()!=0){
				resp.setInfo(CareerConfig.getCareers().get(user.getCareerId()).getDomain());
			}
		}
		
		ValidateUser validateUser = this.validateUserDao.findValidateUser(userId, user.getCourtyard_id());
		if(validateUser!=null){
			resp.setValidate(validateUser.getValidate_status()==1);
			resp.setValidateStatus(validateUser.getValidate_status());
		}else{
			resp.setValidateStatus(-1);
		}
	
		// 查询用户的兴趣
		List<Integer> interests = userInterestDao.findInterests(userId);
		for(Integer interestId : interests){
			if(InterestConf.getInterests().get(interestId)!=null)
				resp.getInterests().add(InterestConf.getInterests().get(interestId));
		}
		UserDaily userDaily = this.getUserDaily(userId);
		resp.setDay(userDaily.getCheck_continue_days());
		// 查询最后一次选择的范围
		resp.setLastRange(this.redisService.getLastRange(userId));
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getMainPage(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public MainPageResp getMainPage(long userId) {
		MainPageResp resp = new MainPageResp();
		User user = this.userDao.get(userId);
//		if(UserSession.get().getCourtyardId()!=user.getCourtyard_id()){
//			throw new GeneralLogicException("只能查看同社区人的资料");
//		}
		resp.setUserId(userId);
		resp.setNickname(user.getNickname());
		resp.setHeadIcon(user.getHead_icon());
		resp.setOfficial(user.isOfficialAccount());
		if(!user.isOfficialAccount()){
		    resp.setMainPageImage(user.getPageImage());
			resp.setGender(user.getGender());
			resp.setCurrentLevel(user.getLevel());
			UserStar us = this.userStarDao.findUnique(Restrictions.eq("user_id", user.getId()),Restrictions.eq("status", 1));
			if(us!=null){
				resp.setInfo(us.getSkill());
			}else{
				if(user.getSkill()!=null){
					resp.setInfo(user.getSkill());
				}else{
					if(user.getCareerId()!=0){
						resp.setInfo(CareerConfig.getCareers().get(user.getCareerId()).getDomain());
					}
					
				}
			}
			TrainInfo tr = this.trainInfoDao.findUnique(Restrictions.eq("userId", userId));
			if(tr!=null){
				resp.setTarinName(tr.getTitle());
				resp.setTarinId(tr.getId());
			}
			resp.setSkill(userStarDao.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("status", 1))!=null);
			resp.setSignature(user.getSignature());
			if(UserSession.get().getUserId()!=userId){
				resp.setBackList(this.blackListDao.isBlackList(UserSession.get().getUserId(), userId));
				resp.setFollowed(userFollowDao.isFollowed(UserSession.get().getUserId(), userId));
			}
			resp.setAge(DateTimeUtil.getAgeData(user.getBirthday()));
			resp.setCareer(CareerConfig.getCareers().get(user.getCareerId()));
			CourtYard courtyard = courtYardDao.get(user.getCourtyard_id());
			if(courtyard!=null){
				resp.setCourtyardName(courtyard.getCourtyard_name());
			}else{
				resp.setCourtyardName(null);
			}
		
			if(user.getBirthday()!=null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				resp.setBirthday(format.format(user.getBirthday()));
			}
			// 查询用户的兴趣
			List<Integer> interests = userInterestDao.findInterests(userId);
			for(Integer interestId : interests){
				resp.getInterests().add(InterestConf.getInterests().get(interestId));
			}
		}
		resp.setValidate(validateUserDao.isValidate(userId, user.getCourtyard_id()));
		resp.setCourtyardId(user.getCourtyard_id());
		long postAmount = userPostDao.count(Restrictions.eq("user_id", userId),Restrictions.eq("content_type", ContentType.分享.getValue()));
		long invitationAmount = this.invitationDao.count(Restrictions.eq("user_id", userId));
		long helpAmount = this.userPostDao.count(Restrictions.eq("user_id", userId),Restrictions.eq("content_type", ContentType.邻居帮帮.getValue()));
		resp.setDynamicAmount((int)(postAmount+invitationAmount+helpAmount));
		resp.setPostAmount((int)postAmount);
		resp.setInvitationAmount((int)invitationAmount);
		// 最新发帖
		UserPost latestPost = this.userPostDao.getLatestUserPost(userId, user.getCourtyard_id());
		if(latestPost!=null){
			User sender = this.userDao.get(userId);
			resp.setPostDetailDto(new PostDetailDto(sender, latestPost));
			resp.setLatestType(2);
		}
		if(!user.isOfficialAccount()){
			// 最新发的邀约
			Invitation latestInvitation = this.invitationDao.getLatestInvitation(user.getCourtyard_id(), userId);
			if(latestInvitation!=null){
				resp.setInvitationDto(new InvitationsDto(latestInvitation));
			}
			
			long replyAmount = this.postReplyDao.count(Restrictions.eq("content_type", ContentType.邻居帮帮.getValue()),Restrictions.eq("replyer_id", userId))+this.postReplyDao.count(Restrictions.eq("content_type", ContentType.分享.getValue()),Restrictions.eq("replyer_id", userId));
			long acceptAmount = this.postReplyDao.count(Restrictions.eq("content_type", ContentType.邻居帮帮.getValue()),Restrictions.eq("replyer_id", userId),Restrictions.eq("accepted", true));
			long followAmout = this.userFollowDao.count(Restrictions.eq("target_id",userId));
			resp.setReplyHelpAmount(replyAmount);
			resp.setHelpAcceptAmount(acceptAmount);
			resp.setFollowedAmount(followAmout);
		}
		return resp;
	}

	/**
	 * @see cn.dayuanzi.service.IUserService#getMain(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public MainResp getMain(long userId) {
		MainResp resp = new MainResp();
		User user = this.userDao.get(userId);
		resp.setNickname(user.getNickname());
		resp.setHeadIcon(user.getHead_icon());
		resp.setLevelExpDto(new LevelExpDto(user));
		//技能是否认证成功
		
		UserStar us = this.userStarDao.findUnique(Restrictions.eq("user_id", userId));
		if(us==null){
			resp.setHavesk(false);
		}else{
			resp.setHavesk(true);
			resp.setSkill(us.getStatus());
		}
		long postAmount = userPostDao.count(Restrictions.eq("courtyard_id", user.getCourtyard_id()),Restrictions.eq("user_id", userId),Restrictions.eq("content_type", ContentType.分享.getValue()))+this.userPostDao.count(Restrictions.eq("courtyard_id", user.getCourtyard_id()),Restrictions.eq("user_id", userId),Restrictions.eq("content_type", ContentType.邻居帮帮.getValue()));
		long invitationAmount = this.invitationDao.count(Restrictions.eq("courtyard_id", user.getCourtyard_id()),Restrictions.eq("user_id", userId));
		//long helpAmount = this.userPostDao.count(Restrictions.eq("courtyard_id", user.getCourtyard_id()),Restrictions.eq("user_id", userId),Restrictions.eq("content_type", ContentType.邻居帮帮.getValue()));
		long followAmout = this.userFollowDao.count(Restrictions.eq("target_id",userId));
		resp.setPostAmount((int)postAmount);
		resp.setInvitationAmount((int)invitationAmount);
		resp.setFollowAmout((int)followAmout);
		UserLinDou lindou = this.getUserLinDou(userId);
		resp.setLindouAmount(lindou.getAmount());
		//在我的 显示回复数
		long replyAmount = this.postReplyDao.count(Restrictions.eq("content_type", ContentType.邻居帮帮.getValue()),Restrictions.eq("replyer_id", userId))+this.postReplyDao.count(Restrictions.eq("content_type", ContentType.分享.getValue()),Restrictions.eq("replyer_id", userId));
		resp.setReplyHelpAmount(replyAmount);
		//返回新关注人数
		MessageStatus messageStatus = this.messageCheckService.findMessageCheck(userId, user.getCourtyard_id());
		long newFollows = this.userFollowDao.countNotReadFollow(userId,messageStatus.getLast_follow_me_time());
		resp.setNewFollowMeAmount(newFollows);
		OpenTrain op = this.openTrainDao.findUnique(Restrictions.eq("userId", userId));
		resp.setHave(false);
		if(op!=null){
			resp.setHave(true);
			resp.setPass(op.getPassed());
			TrainInfo info = this.trainInfoDao.findUnique(Restrictions.eq("userId", userId));
			if(info!=null&&info.getStatus()==1){
				resp.setMyShopId(info.getId());
			}
		}
		return resp;
	}
	
	
	/**
	 * @see cn.dayuanzi.service.IUserService#modifypw(long, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false  )
	public void modifypw(long userId, int type, String currentpw, String newpw) {
		User user = this.userDao.get(userId);
		if(type==1 && !user.getPassword().equals(DigestUtils.md5Hex(currentpw))){
			throw new GeneralLogicException("输入的密码不正确，修改密码失败。");
		}
		user.setPassword(DigestUtils.md5Hex(newpw));
		this.userDao.saveOrUpdate(user);
		ServiceRegistry.getHuanXinService().modifypassword(userId, newpw);
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getRecommends(long, long)
	 */
	@Override
	@Transactional
	public RecommendResp getRecommends(long userId, long courtyardId,int current_page, int max_num) {
//		List<Integer> interests = this.userInterestDao.findInterests(userId);
//		Set<Long> recommends = this.redisService.getRandomRecommend(courtyardId,userId, interests, 6);
		RecommendResp resp = new RecommendResp();
//		if(recommends==null || recommends.isEmpty()){
//			return resp;
//		}
//		List<Friends> friends = this.friendsDao.findFriends(userId);
//		List<Long> friendIds = new ArrayList<Long>();
//		for(Friends friend : friends){
//			if(friend.getUser_id()==userId){
//				friendIds.add(friend.getFriend_id());
//			}else{
//				friendIds.add(friend.getUser_id());
//			}
//		}
		List<Long> recommends = this.validateUserDao.findUserId(courtyardId,current_page,max_num);
		recommends.remove(userId);
		if(recommends.isEmpty()){
			return resp;
		}
		
		List<User> users = this.userDao.get(recommends);
		for(User user : users){
			RecommendDto dto = new RecommendDto(user);
			//返回用户标签
			UserStar userStar = userStarDao.get(userId);
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
		MessageStatus messageStatus = messageCheckService.findMessageCheck(userId, courtyardId);
		long latestValidatedTime = validateUserDao.getLatestValidatedTime(courtyardId, userId);
		if(latestValidatedTime!=messageStatus.getLast_neighbor_validate_time()){
			messageStatus.setLast_neighbor_validate_time(latestValidatedTime);
			messageStatusDao.saveOrUpdate(messageStatus);
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#searchNeighbor(long, long, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public RecommendResp searchNeighbor(long userId, long couryardId, String nickname,int type) {
		RecommendResp resp = new RecommendResp();
		//1是当前院子搜索  2是默认所有社区搜索
	    Criteria c = this.userDao.createCriteria(Restrictions.or(Restrictions.like("nickname", "%"+nickname+"%"),Restrictions.like("skill", "%"+nickname+"%")));
	    if(type==1){ 
	    	c.add(Restrictions.eq("courtyard_id", couryardId));
	    }
	    List<User> users = c.list();
	    if(!users.isEmpty()){
			Iterator<User> i = users.iterator();
			while(i.hasNext()){
				User u = i.next();
				if(u.getId()==userId){
					i.remove();
				}else if(!this.validateUserDao.isValidate(u.getId(), u.getCourtyard_id())){
					i.remove();
				}
			}
			if(!users.isEmpty()){
				for(User user : users){
					List<Integer> interestsForRecommend = this.userInterestDao.findInterests(user.getId());
					RecommendDto dto = new RecommendDto(user);
					UserStar userStar = userStarDao.get(user.getId());
					String r = "";
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
								    	r = r+"/"+InterestConf.getInterests().get(interestid).getInterest();
								    }
								} 
						    }else{
						    	r = user.getSignature();
						    }
						}	
				    }
				    dto.setInfo(r);
					for(Integer interestid:interestsForRecommend){
						if(InterestConf.getInterests().get(interestid)!=null)
							dto.getInterests().add(InterestConf.getInterests().get(interestid));
					}
					resp.getDatas().add(dto);
				}
			}
	    }
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#login(String, String)
	 */
	@Override
	@Transactional
	public PersonalDataResp login(String tel, String password, String token,int platform) {
		User user = this.findUserByTel(tel);
		if(user == null){
			throw new GeneralLogicException(SysMessages.USER_NOT_EXIST);
		}
//		if(user.isBanned()){
//			throw new GeneralLogicException("您已被禁止登录");
//		}
		if(user.getExternalType()!=ExternalType.邻聚.ordinal()){
			throw new GeneralLogicException("请通过"+ExternalType.values()[user.getExternalType()].name()+"登陆");
		}
		if(!user.getPassword().equals(DigestUtils.md5Hex(password))){
			throw new GeneralLogicException(SysMessages.PW_WRONG);
		}
		if(StringUtils.isNotBlank(token)){
			// 如果有其他账号在该设备上登陆过，则需要将该账号的设备标示擦除
			List<User> others = this.userDao.find(Restrictions.eq("last_login_platform", new Integer(platform)),Restrictions.eq("last_login_token", token));
			if(!others.isEmpty()){
				for(User other : others){
					if(other.getId()!=user.getId()){
						other.setLast_login_token("");
						this.userDao.saveOrUpdate(other);
					}
				}
			}
		}
		user.setLast_login_time(System.currentTimeMillis());
		user.setLast_login_platform(platform);
		user.setLast_login_token(token);
		// 登陆时同步兴趣缓存
//		if(user.getCourtyard_id() > 0){
//			List<Integer> interestIds = userInterestDao.findInterests(user.getId());
//			for(Integer interestId : interestIds){
//				ServiceRegistry.getRedisService().saveUserByInterest(user.getCourtyard_id(), user.getId(), interestId);
//			}
//		}
		UserDaily userDaily = this.getUserDaily(user.getId());
		// 上次签到时间
		long lastCheckTime = userDaily.getLast_check_time();
		this.checkDaily(user.getId());
		this.userDao.save(user);
		UserSession userSession = new UserSession(user);
		UserSession.set(userSession);
		PersonalDataResp resp = this.getPersonalData(user.getId());
		resp.setCheck(DateTimeUtil.isToday(new Date(lastCheckTime)));
		if(resp.getDay()==7){
			resp.setGainExp(ThingsAdder.连续七日签到.getExpInfo().getExp());
		}else{
			resp.setGainExp(ThingsAdder.每日签到.getExpInfo().getExp());
		}
		
		UserStar us = this.userStarDao.findUnique(Restrictions.eq("user_id", user.getId()),Restrictions.eq("status", 1));
		if(us!=null){
			resp.setInfo(us.getSkill());
		}else{
			if(user.getSkill()!=null){
				resp.setInfo(user.getSkill());
			}else{
				resp.setInfo(CareerConfig.getCareers().get(user.getCareerId()).getDomain());
			}
		}
		if(userSession.isValidatedUser()){
			if(!userDaily.isSend_introduce()&&user.getRegister_time()>Settings.AFTER_REGISTER_TIME){
				CourtYard courtyard = this.courtYardDao.get(user.getCourtyard_id());
				List<Integer> interestIds = userInterestDao.findInterests(user.getId());
				String content = PostTemplateConfig.getContent(user.getNickname(), courtyard.getCourtyard_name(), interestIds);
				UserPost userPost = new UserPost(user.getCourtyard_id(),user.getId(), null, content);
				userPost.setContent_type(ContentType.分享.getValue());
				userPost.setShow_around(false);
				this.userPostDao.save(userPost);
				userDaily.setSend_introduce(true);
			}
			List<Long> followIds = this.userFollowDao.findFollowIds(user.getId());
			List<Long> officialIds = this.userDao.getOfficialId();
			if(!officialIds.isEmpty()){
				for(long officialId : officialIds){
					if(officialId==user.getId()){
						continue;
					}
					if(!followIds.contains(officialId)){
						this.userFollowDao.save(new UserFollow(user.getId(), officialId));
					}
				}
			}
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#login(cn.dayuanzi.pojo.ExternalType, java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public PersonalDataResp login(ExternalType externalType, String openId, String token,int platform) {
		User user = this.findUserByExternalId(openId, externalType);
		if(user==null){
			List<User> users = userDao.find(Restrictions.eq("openId", openId));
			if(users.isEmpty()){
				throw new GeneralLogicException(Resp.CODE_ERR_USER_NOT_REGISTER,SysMessages.USER_NOT_EXIST);
			}
			user = users.get(0);
		}
//		if(user.isBanned()){
//			throw new GeneralLogicException("您已被禁止登录");
//		}
		if(StringUtils.isNotBlank(token)){
			List<User> others = this.userDao.find(Restrictions.eq("last_login_platform", platform),Restrictions.eq("last_login_token", token));
			if(!others.isEmpty()){
				for(User other : others){
					if(other.getId()!=user.getId()){
						other.setLast_login_token("");
						this.userDao.saveOrUpdate(other);
					}
				}
			}
		}
		user.setLast_login_time(System.currentTimeMillis());
		user.setLast_login_platform(platform);
		user.setLast_login_token(token);
		// 登陆时同步兴趣缓存
//		if(user.getCourtyard_id() > 0){
//			List<Integer> interestIds = userInterestDao.findInterests(user.getId());
//			for(Integer interestId : interestIds){
//				ServiceRegistry.getRedisService().saveUserByInterest(user.getCourtyard_id(), user.getId(), interestId);
//			}
//		}
		UserDaily userDaily = this.getUserDaily(user.getId());
		// 上次签到时间
		long lastCheckTime = userDaily.getLast_check_time();
		this.checkDaily(user.getId());
		this.userDao.save(user);
		UserSession userSession = new UserSession(user);
		UserSession.set(userSession);
		PersonalDataResp resp = this.getPersonalData(user.getId());
		resp.setCheck(DateTimeUtil.isToday(new Date(lastCheckTime)));
		if(resp.getDay()==7){
			resp.setGainExp(ThingsAdder.连续七日签到.getExpInfo().getExp());
		}else{
			resp.setGainExp(ThingsAdder.每日签到.getExpInfo().getExp());
		}
		UserStar us = this.userStarDao.findUnique(Restrictions.eq("user_id", user.getId()),Restrictions.eq("status", 1));
		if(us!=null){
			resp.setInfo(us.getSkill());
		}else{
			if(user.getSkill()!=null){
				resp.setInfo(user.getSkill());
			}else{
				if(user.getCareerId()!=0){
					resp.setInfo(CareerConfig.getCareers().get(user.getCareerId()).getDomain());
				}
			}
		}
		if(userSession.isValidatedUser()){
			if(!userDaily.isSend_introduce()&&user.getRegister_time()>Settings.AFTER_REGISTER_TIME){
				CourtYard courtyard = this.courtYardDao.get(user.getCourtyard_id());
				List<Integer> interestIds = userInterestDao.findInterests(user.getId());
				String content = PostTemplateConfig.getContent(user.getNickname(), courtyard.getCourtyard_name(), interestIds);
				UserPost userPost = new UserPost(user.getCourtyard_id(),user.getId(), null, content);
				userPost.setContent_type(ContentType.分享.getValue());
				userPost.setShow_around(false);
				this.userPostDao.save(userPost);
				userDaily.setSend_introduce(true);
			}
			List<Long> followIds = this.userFollowDao.findFollowIds(user.getId());
			List<Long> officialIds = this.userDao.getOfficialId();
			if(!officialIds.isEmpty()){
				for(long officialId : officialIds){
					if(officialId==user.getId()){
						continue;
					}
					if(!followIds.contains(officialId)){
						this.userFollowDao.save(new UserFollow(user.getId(), officialId));
					}
				}
			}
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getMyHouse(long, long)
	 */
	@Override
	@Transactional(readOnly = false)
	public MyHouseResp getMyHouse(long userId) {
		User user = this.userDao.get(userId);
		MyHouseResp resp = new MyHouseResp();
		resp.setInviteCode(user.getInviteCode());
		CourtYard courtyard = this.courtYardDao.get(user.getCourtyard_id());
//		HouseOwners owner = this.houseOwnersDao.get(user.getHouseowner_id());
		// 没验证过
//		if(owner==null){
//			if(validateUserDao.isValidate(userId, user.getCourtyard_id())){
//				resp.setValidated(true);
//				resp.setValidateType(3);// 邀请码验证
//				resp.setHouseAddress(courtyard.getCourtyard_name());
//			}else{
//				resp.setValidated(false);
//			}
//			return resp;
//		}
		String address = courtyard==null?"":courtyard.getCourtyard_name();
//		List<ValidateUser> validateUsers = this.validateUserDao.findBy("houseowner_id", user.getHouseowner_id());
		ValidateUser me = this.validateUserDao.findValidateUser(userId, user.getCourtyard_id());
//		for(ValidateUser validateUser : validateUsers){
//			if(validateUser.getUser_id()==userId){
//				me = validateUser;
//				break;
//			}
//		}
		// 没验证过
		if(me==null){
			resp.setValidated(false);
			return resp;
		}else{
			resp.setValidated(true);
		}
		resp.setValidateType(me.getValidate_type());
		resp.setHouseAddress(address);
		resp.setValidateStatus(me.getValidate_status());
		resp.setImage(me.getAppend());
		if(me.getValidate_status()==1){
			UserSession userSession = UserSession.get();
			userSession.setValidate(true);
			ValidateUser lastVu = this.validateUserDao.getLastValidate(userId);
			if(lastVu!=null&&DateTimeUtil.countDays(lastVu.getCreate_time(), System.currentTimeMillis())>Settings.CHANGE_COURTYARD_CYCLE){
				resp.setChangeCourtyard(true);
			}
		}
//		for(ValidateUser validateUser : validateUsers){
//			User member = this.userDao.get(validateUser.getUser_id());
//			resp.getMembers().add(new MemberDto(member.getId(),member.getHead_icon(),member.getNickname()));
//		}
	//	ValidateUser lastVu = this.validateUserDao.getLastValidate(userId);
		
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#addInfo(long, org.springframework.web.multipart.commons.CommonsMultipartFile[], java.lang.String, java.lang.Integer, java.lang.String, java.lang.Long, int, java.lang.String)
	 */
	@Override
	@Transactional
	public PersonalDataResp addInfo(long userId, CommonsMultipartFile[] headIcons,
			String nickName, Integer gender, Timestamp birthday,
			int careerId, String interests,String signature,String skill) {
		User user = this.userDao.get(userId);
		User u = this.userDao.findUniqueBy("nickname", nickName);
		if(u!=null&&u.getId()!=userId){
			throw new GeneralLogicException("这个昵称有人用了，另外选一个吧");
		}
		user.setGender(gender);
		user.setNickname(nickName);
		user.setBirthday(birthday);
		user.setCareerId(careerId);
		if(signature!=null){
		    user.setSignature(signature);
		}
		//保存技能名称
		boolean haveStar = false;
		if(StringUtils.isNotBlank(skill)){
			user.setSkill(skill);
		}
		ServiceRegistry.getHuanXinService().modifyNickName(userId, nickName);
		boolean haveInterests = false;
		if(StringUtils.isNotBlank(interests)){
			String[] tokens = interests.split("[/]");
//			ServiceRegistry.getUserInterestService().saveUserInterest(user.getId(), tokens);
			for(int i=0; i<tokens.length; i++){
				int interestId = Integer.parseInt(tokens[i]);
				if(InterestConf.getInterests().containsKey(interestId)){
					this.userInterestDao.saveOrUpdate(new UserInterest(userId,interestId));
					haveInterests = true;
				}
			}
		}
		if(headIcons.length > 1){
			throw new GeneralLogicException("只能上传一张头像");
		}
		if(!headIcons[0].isEmpty()){
			// 头像目录结构：系统设置图片目录/头像目录名/用户ID/头像图片
			String originalName = headIcons[0].getOriginalFilename();
			String fileName = String.valueOf(System.currentTimeMillis())+originalName.substring(originalName.indexOf('.'));
			user.setHead_icon(ImageUtil.saveImageInHeadDire(userId, fileName, headIcons[0]));
		}
//		boolean haveTel = StringUtils.isNotBlank(user.getTel());
		boolean haveCareer = CareerConfig.getCareers().get(careerId)!=null;
		boolean isComplete = (haveCareer&&haveInterests&&StringUtils.isNotBlank(signature))||(haveStar&&haveInterests&&StringUtils.isNotBlank(signature));
		// 判断资料是否完善
		if(isComplete){
			LindouInfo info = ThingsAdder.资料完善.getLindouInfo();
			this.addLindou(user.getId(), info.getLindou(), info.getRemark());
			ExpInfo expInfo = ThingsAdder.资料完善.getExpInfo();
			user.addExp(expInfo.getExp());
			ExpDetail exp = new ExpDetail(userId,expInfo.getExp(),expInfo.getRemark());
			this.expDetailDao.save(exp);
			String content = "恭喜你！完善资料成功，获得{0}个经验值，{1}个邻豆";
			content = MessageFormat.format(content, expInfo.getExp(),info.getLindou());
			noticeService.sendNoticeToUser(NoticeType.系统通知, userId, NoticeType.系统通知.getTitle(),content,0);
		}
		this.userDao.saveOrUpdate(user);
		
		UserDaily userDaily = this.getUserDaily(user.getId());
		// 上次签到时间
		long lastCheckTime = userDaily.getLast_check_time();
		this.checkDaily(user.getId());
		PersonalDataResp resp = this.getPersonalData(user.getId());
		resp.setCheck(DateTimeUtil.isToday(new Date(lastCheckTime)));
		resp.setGainExp(ThingsAdder.每日签到.getExpInfo().getExp());
		
		return resp;
	}
	
	
	@Override
	@Transactional
	public PersonalDataResp modify(long userId,CommonsMultipartFile[] headIcons,String nickName,
			Integer gender,Timestamp birthday,Long courtyardId,int careerId,
			String interests,String signature,String skill){
		User user = this.userDao.get(userId);
		if(nickName != null){
			User other = this.userDao.findUniqueBy("nickname", nickName);
			if(other!=null&&other.getId()!=user.getId()){
				throw new GeneralLogicException("这个昵称有人用了，另外选一个吧");
			}
			user.setNickname(nickName);
			ServiceRegistry.getHuanXinService().modifyNickName(userId, nickName);
		}
		// 老版本已完善资料并获取了经验邻豆奖励
		boolean reward = user.getCareerId()>0&&StringUtils.isBlank(user.getSignature());
		// 老版本修改资料前是否有未完成的
		boolean haveBlank = user.getCareerId()==0||StringUtils.isBlank(user.getSignature());
		// 新版本修改资料前是否有未完成的
		boolean newHaveBlank = StringUtils.isBlank(user.getSignature());
		//签名
		if(signature != null){
		    user.setSignature(signature);
		}
		
		if(gender != null){
			user.setGender(gender);
		}
		if(birthday != null){
			user.setBirthday(birthday);
		}
		// 修改资料后是否有职业
		boolean haveCareer = false;
		if(CareerConfig.getCareers().get(careerId)!=null){
			user.setCareerId(careerId);
			haveCareer = true;
		}
		//if(courtyardId !=null &&courtyardId>0){
		//	user.setCourtyard_id(courtyardId);
		//}
		//修改资料后是否有兴趣
		boolean haveInterests = false;
		List<Integer> interestIds = userInterestDao.findInterests(userId);
		reward = reward&&!interestIds.isEmpty();
		if(StringUtils.isNotBlank(interests)){
			if(interestIds.isEmpty()){
				haveBlank = true;
				newHaveBlank = true;
			}
			userInterestDao.removeInterests(userId);
			// 移除缓存中的
			for(Integer interestId : interestIds){
				ServiceRegistry.getRedisService().removeUserFromInterest(courtyardId, userId, interestId);
			}
			String[] tokens = interests.split("[/]");
		//	ServiceRegistry.getUserInterestService().saveUserInterest(user.getId(), tokens);
			for(int i=0; i<tokens.length; i++){
				int interestId = Integer.parseInt(tokens[i]);
				if(InterestConf.getInterests().containsKey(interestId)){
					haveInterests = true;
					this.userInterestDao.saveOrUpdate(new UserInterest(userId,interestId));
//					ServiceRegistry.getRedisService().saveUserByInterest(courtyardId, userId, interestId);
				}
			}
		}
		//保存技能名称
		boolean haveSkill = false;
		if(skill!=null){
			UserStar us = this.userStarDao.findUnique(Restrictions.eq("user_id", userId));
			if(us!=null){
				if(us.getStatus()!=1){
					user.setSkill(skill);
					haveSkill = true ;
				}else{
					skill = null;
				}
			}else{
				newHaveBlank = true;
				user.setSkill(skill);
				haveSkill = true ;
			}
		}
		if(headIcons!=null && headIcons.length>0){
			if(headIcons.length > 1){
				throw new GeneralLogicException("只能上传一张头像");
			}
			if(!headIcons[0].isEmpty()){
				// 头像目录结构：系统设置图片目录/头像目录名/用户ID/头像图片
				// 用户目录
				String originalName = headIcons[0].getOriginalFilename();
				String fileName = String.valueOf(System.currentTimeMillis())+originalName.substring(originalName.lastIndexOf('.'));
				user.setHead_icon(ImageUtil.saveImageInHeadDire(userId, fileName, headIcons[0]));
			}
		}
		boolean nowComplete = haveBlank && haveCareer && haveInterests && StringUtils.isNotBlank(user.getSignature());
		// 老版本没有完善资料获取奖励，新版本可以通过完善资料获取
		if(!reward&&!nowComplete){
			nowComplete = newHaveBlank&&StringUtils.isNotBlank(user.getSignature())&&haveSkill&&haveInterests;
		}
		if(nowComplete){
			LindouInfo info = ThingsAdder.资料完善.getLindouInfo();
			this.addLindou(user.getId(), info.getLindou(), info.getRemark());
			ExpInfo expInfo = ThingsAdder.资料完善.getExpInfo();
			user.addExp(expInfo.getExp());
			ExpDetail exp = new ExpDetail(user.getId(),expInfo.getExp(),expInfo.getRemark());
			this.expDetailDao.save(exp);
			String content = "恭喜你！完善资料成功，获得{0}个经验值，{1}个邻豆";
			content = MessageFormat.format(content, expInfo.getExp(),info.getLindou());
			noticeService.sendNoticeToUser(NoticeType.系统通知, userId, NoticeType.系统通知.getTitle(),content,0);
		}
		
		
		this.userDao.saveOrUpdate(user);
		PersonalDataResp resp = this.getPersonalData(userId);
		UserDaily userDaily = this.getUserDaily(user.getId());
		// 上次签到时间
		long lastCheckTime = userDaily.getLast_check_time();
		resp.setCheck(DateTimeUtil.isToday(new Date(lastCheckTime)));
		return resp;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void setUserFeedback(long userId,Long courtyardId,String content,CommonsMultipartFile[] images){
		User user = this.userDao.get(userId);
	    	UserFeedback  userback = new UserFeedback(userId,courtyardId,content);
	    	if(user.getTel()!=null){
	    	    userback.setUser_tel(user.getTel());
	    	}else{
	    	    userback.setUser_tel("0");
	    	}//避免第三方登陆时无手机号
		this.userFeedbackDao.save(userback);
		if(images!=null && images.length>0){
			if(images.length >3){
				throw new GeneralLogicException("最多只能上传三张照片");
			}
			userback.setFeedBackImages(ImageUtil.savePostImage(userback.getId(), Settings.BACK_IMAGES, images));
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void setUserError(long userId, String name,String code,String address,String tel,String content){
		UserError  usererror =new UserError(userId,name,code,address,tel,content);
		this.userErrorDao.save(usererror);
	}
	
	@Override
	@Transactional(readOnly = false)
	public void selectCommunity(long courtyardId,long userId){
		CourtYard courtyard = this.courtYardDao.get(courtyardId);
		if(courtyard==null){
			throw new GeneralLogicException("您选择的社区不存在");
		}
		User user = this.userDao.get(userId);
		if(courtyard.getId()==user.getCourtyard_id()){
			throw new GeneralLogicException("您已经在这个社区了");
		}
		user.setCourtyard_id(courtyardId);
		this.userDao.save(user);
	}
	
	@Override
	@Transactional(readOnly = true)
	public VersionCheckeResp versionChecking(){
		VersionCheckeResp resp =new VersionCheckeResp();
		VersionChecke ver = this.versionCheckeDao.get((long)1);
		if(ver!=null){
			 resp =new VersionCheckeResp(ver);
			return resp;
		}
		return resp;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void userSetting(boolean reply ,boolean laud,boolean message,boolean system){
		UserSession userSession = UserSession.get();
		UserSetting setting = this.getUserSetting(userSession.getUserId());
		setting.setLaud(laud);
		setting.setMessage(message);
		setting.setReply(reply);
		setting.setSystem(system);
		this.userSettingDao.save(setting);
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#register(java.lang.String, int, java.lang.String, int, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public User register(int externalType, String openId,
			int platform, String token,Long inviteCode) {
		User user = this.findUserByExternalId(openId, ExternalType.values()[externalType]);
		if(user!=null){
			throw new GeneralLogicException("已经注册了");
		}
//		user = findUserByTel(tel);
//		if(user != null){
//			throw new GeneralLogicException("这个手机号码已被注册过了");
//		}
		List<User> others = this.userDao.find(Restrictions.eq("last_login_platform", platform),Restrictions.eq("last_login_token", token));
		if(!others.isEmpty()){
			for(User other : others){
				other.setLast_login_token("");
				this.userDao.saveOrUpdate(other);
			}
		}
		
		user = new User(externalType, openId);
		user.setPlatform(platform);
		user.setToken(token);
		this.userDao.save(user);
		
		this.register(user,inviteCode);
		try{
			String pw = openId.substring(0, 6);
			// 外部平台注册时，注册环信账户，密码为手机号码，昵称为第三方平台昵称
			boolean success = ServiceRegistry.getHuanXinService().registerUser(user.getId(), pw);
			if(success){
				logger.info("新用户注册环信用户成功。");
			}else{
				logger.info("新用户注册环信用户失败。");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#register(java.lang.String, java.lang.String, int, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public User register(String tel, String password, int platform, String token, Long inviteCode) {
		User user = findUserByTel(tel);
		if(user != null){
			throw new GeneralLogicException("这个手机号码已被注册过了");
		}
		
		List<User> others = this.userDao.find(Restrictions.eq("last_login_platform", platform),Restrictions.eq("last_login_token", token));
		if(!others.isEmpty()){
			for(User other : others){
				other.setLast_login_token("");
				this.userDao.saveOrUpdate(other);
			}
		}
		
		user = new User(tel, DigestUtils.md5Hex(password));
		user.setPlatform(platform);
		user.setToken(token);
		this.userDao.save(user);
		this.register(user,inviteCode);
		try{
			boolean success = ServiceRegistry.getHuanXinService().registerUser(user.getId(), password);
			if(success){
				logger.info("新用户注册环信用户成功");
			}else{
				logger.info("新用户注册环信用户失败");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return user;
	}
	/**
	 * 注册时调用，加经验邻豆等
	 * @param newUser
	 * @param inviteCode
	 */
	private void register(User newUser,Long inviteCode){
		if(inviteCode!=null){
			User inviter = this.userDao.get(inviteCode);
			if(inviter==null)
				throw new GeneralLogicException("对不起，邀请码无效");
			if(!validateUserDao.isValidate(inviter.getId(), inviter.getCourtyard_id())){
				throw new GeneralLogicException("对不起，邀请码无效");
			}
//			newUser.setCourtyard_id(inviter.getCourtyard_id());
			newUser.setInviteCode(inviteCode);
//			ValidateUser validateUser = new ValidateUser(newUser.getId(), newUser.getCourtyard_id());
//			validateUser.setValidate_status(1);
//			validateUser.setValidate_type(3);
//			validateUserDao.save(validateUser);
//			Notice notice = new Notice(NoticeType.系统通知.ordinal(),validateUser.getCourtyard_id(),newUser.getId(),"恭喜你，注册并邀请验证成功");
//			this.noticeDao.save(notice);
//			LindouInfo info = ThingsAdder.社区认证.getLindouInfo();
//			this.addLindou(newUser.getId(), info.getLindou(), info.getRemark());
//			ExpInfo expInfo = ThingsAdder.社区认证.getExpInfo();
//			newUser.addExp(expInfo.getExp());
		}
		LindouInfo info = ThingsAdder.注册.getLindouInfo();
		this.addLindou(newUser.getId(), info.getLindou(), info.getRemark());
		ExpInfo expInfo = ThingsAdder.注册.getExpInfo();
		newUser.addExp(expInfo.getExp());
		ExpDetail exp= new ExpDetail(newUser.getId(),expInfo.getExp(),expInfo.getRemark());
		this.expDetailDao.save(exp);
		String content = "恭喜你！注册成功，获得{0}个经验值，{1}个邻豆";
		content = MessageFormat.format(content, expInfo.getExp(),info.getLindou());
		noticeService.sendNoticeToUser(NoticeType.注册通知, newUser.getId(), NoticeType.注册通知.getTitle(),content,0);
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getUserSetting(long)
	 */
	@Override
	@Transactional
	public UserSetting getUserSetting(long userId) {
		UserSetting userSetting = this.userSettingDao.get(userId);
		if(userSetting==null){
			userSetting = new UserSetting();
			userSetting.setId(userId);
			this.userSettingDao.save(userSetting);
		}
		return userSetting;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getUserLinDou(long)
	 */
	@Override
	@Transactional
	public UserLinDou getUserLinDou(long userId) {
		UserLinDou lindou = this.userLinDouDao.get(userId);
		if(lindou==null){
			lindou = new UserLinDou();
			lindou.setId(userId);
			this.userLinDouDao.save(lindou);
		}
		return lindou;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getUserDaily(long)
	 */
	@Override
	@Transactional
	public UserDaily getUserDaily(long userId) {
		UserDaily userDaily = this.userDailyDao.get(userId);
		if(userDaily==null){
			userDaily = new UserDaily();
			userDaily.setId(userId);
			this.userDailyDao.save(userDaily);
		}
		userDaily.doUpdate();
		return userDaily;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#checkDaily(long)
	 */
	@Override
	@Transactional
	public void checkDaily(long userId) {
		User user = this.userDao.get(userId);
		UserDaily userDaily = this.getUserDaily(userId);
		// 签到成功
		if(userDaily.checkedToday()){
			if(userDaily.getCheck_continue_days()==7){
//				userDaily.setCheck_continue_days(0);
				ExpInfo info = ThingsAdder.连续七日签到.getExpInfo();
				user.addExp(info.getExp());
				ExpDetail exp= new ExpDetail(userId,info.getExp(),info.getRemark());
				this.expDetailDao.save(exp);
				LindouInfo lindouinfo = ThingsAdder.连续七日签到.getLindouInfo();
				this.addLindou(userId, lindouinfo.getLindou(), lindouinfo.getRemark());
			}else{
				ExpInfo info = ThingsAdder.每日签到.getExpInfo();
				user.addExp(info.getExp());
				ExpDetail exp= new ExpDetail(userId,info.getExp(),info.getRemark());
				this.expDetailDao.save(exp);
			}
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#addLindou(long, java.lang.String)
	 */
	@Override
	@Transactional
	public void addLindou(long userId, int amount, String from) {
		if(amount <=0 ){
			return;
		}
		UserLinDou userLinDou = getUserLinDou(userId);
		userLinDou.setAmount(userLinDou.getAmount()+amount);
		this.lindouDetailDao.save(new LindouDetail(userId, amount, from));
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getLindouDetail(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public DatasResp getLindouDetail(long userId,int current_page,int max_num) {
		List<LindouDetail> results = lindouDetailDao.findDetails(userId,current_page,max_num);
		DatasResp resp = new DatasResp();
		for(LindouDetail detail : results){
			resp.getDatas().add(new LindouDetailDto(detail));
		}
		return resp; 
	}
	/**
	 * 
	 * @see cn.dayuanzi.service.IUserService#getExpDetail(long)
	 */
	
	@Override
	@Transactional(readOnly = true)
	public DatasResp  getExpDetail(long userId,int current_page,int max_num){
		List<ExpDetail> results = expDetailDao.findDetails(userId,current_page,max_num);
		DatasResp resp = new DatasResp();
		for(ExpDetail detail : results){
			resp.getDatas().add(new ExpDetailDto(detail));
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#thankReply(long, long, long)
	 */
	@Override
	@Transactional
	public void thankReply(long userId, long courtyardId, long replyId) {
		PostReply postReply = this.postReplyDao.get(replyId);
		if(userId == postReply.getReplyer_id()){
			throw new GeneralLogicException("您不能发出感谢");
		}
		if(postReply.getReply_id()!=0){
			throw new GeneralLogicException("无法操作感谢");
		}
		thankReplyDao.save(new ThankReply(userId, postReply));
		UserDaily userDaily = ServiceRegistry.getUserService().getUserDaily(postReply.getReplyer_id());
		ExpInfo info = ThingsAdder.被感谢.getExpInfo();
		if(userDaily.getBe_thank_count() < info.getLimitDaily()){
			User replyer = this.userDao.get(postReply.getReplyer_id());
			replyer.addExp(info.getExp());
			ExpDetail exp = new ExpDetail(replyer.getId(),info.getExp(),info.getRemark());
			this.expDetailDao.save(exp);
			userDaily.setBe_thank_count(userDaily.getBe_thank_count()+1);
		}
		User replyer = this.userDao.get(postReply.getReplyer_id());
		int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadLaud(replyer.getId(), 1);
		UserSetting setting = this.getUserSetting(replyer.getId());
		if(setting.isLaud()){
			ApnsUtil.getInstance().send(replyer, PushType.感谢, "你收到一条新的感谢",0, count);
		}else{
			ApnsUtil.getInstance().send(replyer, count);
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#getMyTask(long)
	 */
	//@Override
	//@Transactional
	//public Resp getMyTask(long userId) {
	//	User user = this.userDao.get(userId);
	//	TaskResp resp = new TaskResp();
	//	if(!this.validateUserDao.isValidate(userId, user.getCourtyard_id())){
	//		TaskDto newDto = new TaskDto(ThingsAdder.社区认证.name(),ThingsAdder.社区认证.getLindouInfo().getLindou(),ThingsAdder.社区认证.getExpInfo().getExp());
	//		resp.getNewUserTasks().add(newDto);
	//	}
	//	boolean haveBlank = user.getCareerId()==0;
	//	if(!haveBlank){
	//		List<Integer> interestIds = userInterestDao.findInterests(userId);
	//		haveBlank = interestIds.isEmpty();
	//	}
	//	if(haveBlank){
	//		TaskDto newDto = new TaskDto(ThingsAdder.资料完善.name(),ThingsAdder.资料完善.getLindouInfo().getLindou(),ThingsAdder.资料完善.getExpInfo().getExp());
	//		resp.getNewUserTasks().add(newDto);
	//	}
	//	
	//	UserDaily userDaily = this.getUserDaily(userId);
	//	// 每日任务发话题
	//	ExpInfo info = ThingsAdder.发话题.getExpInfo();
	//	TaskDto dto = new TaskDto(ThingsAdder.发话题.name(),info.getExp(),userDaily.getSend_post_count(),info.getLimitDaily());
	//	resp.getDailyTasks().add(dto);
	//	// 每日任务帮帮评论
	//	info = ThingsAdder.帮帮评论.getExpInfo();
	//	dto = new TaskDto(ThingsAdder.帮帮评论.name(),info.getExp(),userDaily.getHelp_reply_count(),info.getLimitDaily());
	//	resp.getDailyTasks().add(dto);
	//	// 每日任务被感谢
	//	info = ThingsAdder.被感谢.getExpInfo();
	//	dto = new TaskDto(ThingsAdder.被感谢.name(),info.getExp(),userDaily.getBe_thank_count(),info.getLimitDaily());
	//	resp.getDailyTasks().add(dto);
	//	// 每日任务回复被采纳
	//	info = ThingsAdder.意见被采纳.getExpInfo();
	//	dto = new TaskDto(ThingsAdder.意见被采纳.name(),info.getExp(),userDaily.getBe_thank_count(),info.getLimitDaily());
	//	resp.getDailyTasks().add(dto);
	//	return resp;
	//}
	@Override
	@Transactional
	public TaskResp getMyTask(long userId) {
		User user = this.userDao.get(userId);
		TaskResp resp = new TaskResp();
		if(!this.validateUserDao.isValidate(userId, user.getCourtyard_id())){
			NewTaskDto newDto = new NewTaskDto(ThingsAdder.社区认证.name(),ThingsAdder.社区认证.getLindouInfo().getLindou(),ThingsAdder.社区认证.getExpInfo().getExp(),ThingsAdder.社区认证.getExpInfo().getImage(),ThingsAdder.社区认证.getExpInfo().getId());
			newDto.setLimitTimes(ThingsAdder.社区认证.getExpInfo().getLimitDaily());
			resp.getNewUserTasks().add(newDto);
			//resp.setShequ(newDto);
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
			NewTaskDto newDtoziliao = new NewTaskDto(ThingsAdder.资料完善.name(),ThingsAdder.资料完善.getLindouInfo().getLindou(),ThingsAdder.资料完善.getExpInfo().getExp(),ThingsAdder.资料完善.getExpInfo().getImage(),ThingsAdder.资料完善.getExpInfo().getId());
			newDtoziliao.setLimitTimes(ThingsAdder.资料完善.getExpInfo().getLimitDaily());
			resp.getNewUserTasks().add(newDtoziliao);
			//resp.setZiliao(newDto);
		}
//		if(this.userPostDao.findziwojieshao(userId)){
			//NewTaskDto  newDtojieshao = new  NewTaskDto(ThingsAdder.介绍自己.name(),ThingsAdder.介绍自己.getLindouInfo().getLindou(),ThingsAdder.介绍自己.getExpInfo().getExp(),ThingsAdder.介绍自己.getExpInfo().getImage(),ThingsAdder.介绍自己.getExpInfo().getId());
			//newDtojieshao.setLimitTimes(ThingsAdder.介绍自己.getExpInfo().getLimitDaily());
			//resp.getNewUserTasks().add(newDtojieshao);
			//resp.setJieshao(newDtojieshao);
//		}
		if(this.userDao.find(Restrictions.eq("inviteCode",userId)).size()==0){
			NewTaskDto  newDtoyaoqing = new NewTaskDto(ThingsAdder.邀请邻居.name(),ThingsAdder.邀请邻居.getLindouInfo().getLindou(),ThingsAdder.邀请邻居.getExpInfo().getExp(),ThingsAdder.邀请邻居.getExpInfo().getImage(),ThingsAdder.邀请邻居.getExpInfo().getId());
			newDtoyaoqing.setLimitTimes(1);
			resp.getNewUserTasks().add(newDtoyaoqing);	
			//resp.setYaoqing(newDtoyaoqing);
		}
		
		UserDaily userDaily = this.getUserDaily(userId);
		// 每日任务发话题
		ExpInfo info = ThingsAdder.发话题.getExpInfo();
		EveryTaskDto dto = new EveryTaskDto(ThingsAdder.发话题.name(),info.getExp(),userDaily.getSend_post_count(),info.getLimitDaily(),ThingsAdder.发话题.getExpInfo().getImage(),ThingsAdder.发话题.getExpInfo().getId());
		resp.getDailyTasks().add(dto);
		//resp.setFahuati(dto);;
		// 每日任务帮帮评论
		info = ThingsAdder.帮帮评论.getExpInfo();
		EveryTaskDto dtobang = new EveryTaskDto(ThingsAdder.帮帮评论.name(),info.getExp(),userDaily.getHelp_reply_count(),info.getLimitDaily(),ThingsAdder.帮帮评论.getExpInfo().getImage(),ThingsAdder.帮帮评论.getExpInfo().getId());
		resp.getDailyTasks().add(dtobang);		//resp.setHuitie(dtobang);
		// 每日任务被感谢
		info = ThingsAdder.被感谢.getExpInfo();
		EveryTaskDto dtoganxie = new EveryTaskDto(ThingsAdder.被感谢.name(),info.getExp(),userDaily.getBe_thank_count(),info.getLimitDaily(),ThingsAdder.被感谢.getExpInfo().getImage(),ThingsAdder.被感谢.getExpInfo().getId());
		resp.getDailyTasks().add(dtoganxie);
		//resp.setBieganxie(dtoganxie);
		// 每日任务回复被采纳
		info = ThingsAdder.意见被采纳.getExpInfo();
		EveryTaskDto dtobeicaina = new EveryTaskDto(ThingsAdder.意见被采纳.name(),info.getExp(),userDaily.getAccepted_count(),info.getLimitDaily(),info.getImage(),info.getId());
		dtobeicaina.setLindou(ThingsAdder.意见被采纳.getLindouInfo().getLindou()*info.getLimitDaily());
		resp.getDailyTasks().add(dtobeicaina);
		//resp.setCaina(dtobeicaina);
		return resp;
	}
	
	
	/**
	 * @see cn.dayuanzi.service.IUserService#putUserBlackList(long, long)
	 */
	@Override
	@Transactional
	public void putUserBlackList(long userId, long targetId,boolean concel) {
		User target  = this.userDao.get(targetId);
		if(target.isOfficialAccount()){
			return;
		}
		if(concel){
			this.blackListDao.delete(userId, targetId);
		}else{
			if(userId==targetId){
				throw new GeneralLogicException("操作错误");
			}
			this.blackListDao.save(new BlackList(userId, targetId));
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#followUser(long, long)
	 */
	@Override
	@Transactional
	public void followUser(long userId, long targetId,boolean concel) {
		if(concel){
			User target = this.userDao.get(targetId);
			//  官方账号不能取消关注
			if(target.isOfficialAccount()){
				return;
			}
			userFollowDao.delete(userId, targetId);
		}else{
			if(userId==targetId){
				throw new GeneralLogicException("操作错误");
			}
			User target = this.userDao.get(targetId);
			if(target==null){
				throw new GeneralLogicException("关注的对象不存在");
			}
			if(blackListDao.isBlackList(userId, targetId)){
				throw new GeneralLogicException("您已拉黑，不能关注哦");
			}
			if(blackListDao.isBlackList(targetId, userId)){
				throw new GeneralLogicException("您已被拉黑，不能关注哦");
			}
			userFollowDao.save(new UserFollow(userId, targetId));
			int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadFollow(targetId, 1);
			UserSetting userSetting = ServiceRegistry.getUserService().getUserSetting(targetId);
			if(userSetting.isSystem()){
				ApnsUtil.getInstance().send(target, "有邻居关注了你");
				ApnsUtil.getInstance().send(target, count);
			}else{
				ApnsUtil.getInstance().send(target, count);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public BlackNameListResp BlackNameList(int current_page, int max_num){
	    UserSession userSession = UserSession.get();
	    BlackNameListResp resp = new BlackNameListResp();
	    List<BlackList> blackLists = this.blackListDao.findForPage(current_page, max_num, "create_time", false, Restrictions.eq("user_id",userSession.getUserId()));;
		if(CollectionUtils.isNotEmpty(blackLists)){
			for(BlackList blackList : blackLists){
			    User user = this.userDao.get(blackList.getTarget_id());
			    BlackNameListDto blackNameListDto  = new BlackNameListDto(blackList,user.getHead_icon(),user.getNickname());
			    resp.getDatas().add(blackNameListDto);
			}	
		}
	    return resp;
	}
	
	@Override
	public Resp shareUser(long userId){
	    PersonalDataResp resp = new PersonalDataResp();
	    resp.setUserId(userId);
	    return  resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#applyStar(long, java.lang.String, java.lang.String, org.springframework.web.multipart.commons.CommonsMultipartFile[])
	 */
	@Override
	@Transactional
	public void applyStar(long userId, String tel, String skill, String content, CommonsMultipartFile[] images) {
		UserStar us = this.userStarDao.findUniqueBy("user_id", userId);
		if(us!=null){
			if(us.getStatus()!=2){
				throw new GeneralLogicException("您已经申请技能认证了，不能再次申请");
			}
			us.setStatus(0);
		}else{
			us = new UserStar(userId,tel,content);
		}
		User user = this.userDao.get(userId);
		us.setSkill(skill);
		user.setSkill(skill);
		if(images!=null){
			us.setImage_names(ImageUtil.saveImage(images));
		}
		this.userStarDao.saveOrUpdate(us);
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserService#findUserStars(long)
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public DatasResp findUserStars(long userId,Integer current_page, Integer max_num) {
		DatasResp resp = new DatasResp();
		List<Object[]> starIdTags = this.userStarDao.findStarIdAndTagName(userId,current_page,max_num);
		if(!starIdTags.isEmpty()){
			List<Long> starIds = new ArrayList<Long>();
			for(Object[] o : starIdTags){
				starIds.add((Long)o[0]);
			}
			List<User> users = this.userDao.get(starIds);
			Map<Long, User> temp = new HashMap<Long, User>();
			for(User u : users){
				temp.put(u.getId(), u);
			}
			int index = 0;
			for(Long l : starIds){
				if(l == userId){
					
					continue;
				}
				User u = temp.get(l);
				CourtYard c = this.courtYardDao.get(u.getCourtyard_id());
				UserStarDto dto = new UserStarDto();
				dto.setUserId(l);
				dto.setNickName(u.getNickname());
				dto.setHeadIcon(u.getHead_icon());
				dto.setTagName((String)starIdTags.get(index)[1]);
				dto.setCourtyardName(c.getCourtyard_name());
				index++;
				resp.getDatas().add(dto);
			}
			temp = null;
		}
		UserStar us = this.userStarDao.findUnique(Restrictions.eq("user_id", userId));
		if(us==null){
			resp.setHavesk(false);
		}else{
			resp.setHavesk(true);
		}
		return resp;
	}
	
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public LaudListResp findUserfollow(int current_page, int max_num,long userId,long courtyardId){
	    LaudListResp resp = new LaudListResp();
	    List<Long> followIds = this.userFollowDao.findFollowIds(userId);
	    List<User> users = this.userDao.getAtUserId(current_page,max_num,courtyardId,followIds);
	    if(CollectionUtils.isNotEmpty(users)){
			for(User user:users){
			    LaudListDto dto = new LaudListDto(user);
			    UserStar userStar = userStarDao.get(userId);
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
	    }
	    return resp ;
	}
	
	@Override
	@Transactional(readOnly = false)
	public void setMainPageImage(CommonsMultipartFile[] image,long userId){
	    User user = this.userDao.get(userId);
	    if(!image[0].isEmpty()){
		// 头像目录结构：系统设置图片目录/头像目录名/用户ID/主页图片
		String originalName = image[0].getOriginalFilename();
		String fileName = String.valueOf(System.currentTimeMillis())+originalName.substring(originalName.lastIndexOf('.'));
		user.setPageImage(ImageUtil.saveImageInHeadDire(userId, fileName, image[0]));
	    }
	}
	
	@Override
	@Transactional(readOnly = false)
	public void validateTel(String tel,long userId){
		User us = this.findUserByTel(tel);
		if(us!=null){
			throw new GeneralLogicException("该手机号已经被认证,请重新输入");
		}
		User user  = this.userDao.get(userId);
		if(user==null){
			throw new GeneralLogicException("用户不存在无法认证");
		}
		user.setTel(tel);
		this.userDao.saveOrUpdate(user);
	}
}
	
