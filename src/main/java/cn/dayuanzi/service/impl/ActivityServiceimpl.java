/**
 * 
 */
package cn.dayuanzi.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.ActivityInfoDao;
import cn.dayuanzi.dao.ActivityLaudDao;
import cn.dayuanzi.dao.ActivityReplyDao;
import cn.dayuanzi.dao.ActivitySignUpDao;
import cn.dayuanzi.dao.AtRelationsDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.CourtyardOfActivityDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.ActivityInfo;
import cn.dayuanzi.model.ActivityLaud;
import cn.dayuanzi.model.ActivityReply;
import cn.dayuanzi.model.ActivitySignUp;
import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.NoticeType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.ActivityListResp;
import cn.dayuanzi.resp.ActivityResp;
import cn.dayuanzi.resp.ContactsResp;
import cn.dayuanzi.resp.dto.ActivityDto;
import cn.dayuanzi.resp.dto.ActivityReplyDto;
import cn.dayuanzi.resp.dto.ContactsDto;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.service.IActivityService;
import cn.dayuanzi.service.INoticeService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.ApnsUtil;
import cn.dayuanzi.util.YardUtils;

/** 
 * @ClassName: ActivitySwrviceimpl 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年5月31日 下午4:29:37 
 *  
 */
@Service
public class ActivityServiceimpl implements IActivityService{

	@Autowired
	private ActivityInfoDao  activityInfoDao;
	@Autowired
	private CourtyardOfActivityDao courtyardOfActivityDao;
	@Autowired
	private ActivitySignUpDao  activitySignUpDao;
	@Autowired
	private ActivityLaudDao  activityLaudDao;
	@Autowired
	private ActivityReplyDao activityReplyDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private INoticeService noticeService;
	@Autowired
	private CourtYardDao courtyardDao;
	@Autowired
	private AtRelationsDao atRelationsDao;
	
	@Override
	@Transactional(readOnly = true  )
	public  ActivityInfo  getactivity(long activityId){
		return this.activityInfoDao.get(activityId);
	}
	
	
	@Override
	@Transactional(readOnly = true  )
	public  boolean  isJoinActivity(long activityId,long userId){
		return this.activitySignUpDao.getUserjoin(userId, activityId);
	}
	
	
	
	@Override
	@Transactional(readOnly = false)
	public void activitySignUp(long ateiviteyId,long userId,String name,String tel,String content){
		ActivityInfo actInfo = this.activityInfoDao.get(ateiviteyId);
		if(actInfo==null){
			throw new GeneralLogicException("该活动不存在");	
		}
		if(actInfo.getEndTime().getTime()<System.currentTimeMillis()){
			throw new GeneralLogicException("该活动已过期，无法报名");	
		}
		if(this.isJoinActivity(userId, ateiviteyId)!=false){
			throw new GeneralLogicException("您已经报名该次活动");	
		}
		if(actInfo.isSignDisable()){
			throw new GeneralLogicException("这个活动不能报名");
		}
		if(this.countSignedAmountForActivity(ateiviteyId)>=actInfo.getPeoplesLimit()){
			throw new GeneralLogicException("人数已满，报名失败");
		}
		User user = this.userDao.get(userId);
		List<Long> activityIds = courtyardOfActivityDao.findActivitysForCourtyardId(user.getCourtyard_id());
		if(activityIds.size()<=0 || !activityIds.contains(actInfo.getId())){
			throw new GeneralLogicException("您不能报名");	
		}
		ActivitySignUp act = new ActivitySignUp(userId,ateiviteyId,name,tel,content);
		this.activitySignUpDao.save(act);
		String notice = "恭喜你！“{0}”活动报名成功。";
		notice = MessageFormat.format(notice, actInfo.getActivity_title());
		noticeService.sendNoticeToUser(NoticeType.官方活动报名成功通知, user.getId(), NoticeType.官方活动报名成功通知.getTitle(),notice,ateiviteyId);
		
	}
	

	@Override
	@Transactional(readOnly = true)
	public int countLikeActivity(long activityId){
		return (int)this.activityLaudDao.count("activity_id",activityId);
	}
	
	@Override
	@Transactional(readOnly = true)
	public int  countReplyActivity(long activityId,long courtyardId){
		return (int)this.activityReplyDao.count(Restrictions.eq("activity_id", activityId));
	}
	
	@Override
	@Transactional(readOnly = true)
	public boolean isLikeActibity(long userId,long activityId){
		return this.activityLaudDao.findUnique(Restrictions.eq("activity_id",activityId),Restrictions.eq("user_id", userId))!=null;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ActivityReply> findActivityReplys(long activityId, long courtyardId ){
		return this.activityReplyDao.findActivityReply("create_time",false,Restrictions.eq("activity_id", activityId),Restrictions.eq("reply_id", 0l));
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<ActivityReply> findActivityReply(long activityId, long rereplyId ){
		return this.activityReplyDao.findActivityReply("create_time", false,Restrictions.eq("activity_id", activityId),Restrictions.eq("reply_id", rereplyId));
		
	}
	
	
	@Override
	@Transactional(readOnly = false)
	public void likeActibity(long userId,long activityId,long courtyardId){
		ActivityInfo  activityInfo = this.getactivity(activityId);
		if(activityInfo == null){
			throw new GeneralLogicException("没有找到这条活动哦");
		}
		if(this.isLikeActibity(userId, activityId)){
			throw new GeneralLogicException("你已经赞过了哟");
		}
		
		ActivityLaud act = new ActivityLaud(userId,courtyardId,activityId);
		this.activityLaudDao.save(act);
	}
	
	
	
	@Override
	@Transactional(readOnly = true)
	public List<ActivityInfo> findActivityInfo(long courtyardId){
//		Disjunction d = Restrictions.disjunction();
//		d.add(Restrictions.eq("courtyard_id", 0l));
//		d.add(Restrictions.eq("courtyard_id", courtyardId));
//		CourtYard courtYard = this.courtyardDao.get(courtyardId);
//		List<CourtYard> courtyards = courtyardDao.getAll();
//		List<Long> courtyardIds = new ArrayList<Long>();
//		for(CourtYard yard : courtyards){
//			if(yard.getId()!=courtyardId){
//				// 查找五公里之内的社区
//				if(courtYard.atRange(5000, yard.getLongitude(), yard.getLatitude())){
//					courtyardIds.add(yard.getId());
//				}
//			}
//		}
//		List<Long> activityIds = null;
//		if(courtyardIds.isEmpty()){
//			activityIds = courtyardOfActivityDao.findActivitysForCourtyardId(courtyardId);
//		}else{
//			courtyardIds.add(courtyardId);
//			Disjunction d = Restrictions.disjunction();
//			d.add(Restrictions.eq("courtyard_id", 0l));
//			d.add(Restrictions.in("courtyard_id", courtyardIds));
//			Criteria c = courtyardOfActivityDao.createCriteria(d);
//			c.setProjection(Projections.property("activity_id"));
//			activityIds = c.list();
//		}
		List<Long> activityIds = courtyardOfActivityDao.findActivitysForCourtyardId(courtyardId);
		if(activityIds.isEmpty()){
			return null;
		}
		return this.activityInfoDao.getAll("createTime", false, Restrictions.in("id", activityIds));
	}
	
	
	
	@Override
	@Transactional(readOnly = false)
	public void replyActivity(long targetId,long userId, long replyType,String content,long atReplyerId){
		User user = this.userDao.get(userId);
		if(user.isBanned()){
			throw new GeneralLogicException("您已被禁言");
		}
		List<Long> activityIds = courtyardOfActivityDao.findActivitysForCourtyardId(user.getCourtyard_id());
		if(replyType==1){
			ActivityInfo info = this.activityInfoDao.get(targetId);
			//评论活动
			if(info==null){
				throw new GeneralLogicException("没有这个活动哦");
			}
			
			if(activityIds.size()==0||!activityIds.contains(info.getId())){
				throw new GeneralLogicException("您无法回复这个活动哦");
			}
			List<User> atUsers = YardUtils.findAt(content);
			ActivityReply actReply =new ActivityReply(user,targetId,content);
			this.activityReplyDao.save(actReply);
			if(atUsers!=null){
				for(User u : atUsers){
					AtRelations ar = new AtRelations(userId, u);
					ar.setScene(ContentType.活动评论.getValue());
					ar.setAppend(actReply.getId());
					this.atRelationsDao.save(ar);
				}
			}
		}else{
			//回复评论
			ActivityReply replyTarget = this.activityReplyDao.get(targetId);
			if(replyTarget==null){
				throw new GeneralLogicException("没有找到这个评论哟");
			}
			//注释原因专题需要不同院子能互评
			//if(user.getCourtyard_id()!=replyTarget.getCourtyard_id()){
			//	throw new GeneralLogicException("只能回复当前所在院子的帖子");
			//}
			if(replyTarget.getReply_id() > 0){
				throw new GeneralLogicException("不能回复");
			}
			User atReplyer = this.userDao.get(atReplyerId);
			if(atReplyer==null){
				throw new GeneralLogicException("出了点小问题，稍后试试");
			}
			ActivityInfo info = this.activityInfoDao.get(replyTarget.getActivity_id());
			//评论活动
			if(info==null){
				throw new GeneralLogicException("没有这个活动哦");
			}
			List<User> atUsers = YardUtils.findAt(content);
			ActivityReply reply  = new ActivityReply(user,replyTarget.getActivity_id(),content);
			reply.setAt_targetId(atReplyerId);
			reply.setReply_id(targetId);
			this.activityReplyDao.save(reply);
			if(atUsers!=null){
				for(User u : atUsers){
					AtRelations ar = new AtRelations(userId, u);
					ar.setScene(ContentType.活动评论.getValue());
					ar.setAppend(reply.getId());
					this.atRelationsDao.save(ar);
				}
			}
			
			int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadReply(atReplyer.getId(), 1);
			UserSetting setting = this.userService.getUserSetting(atReplyerId);
			if(setting.isReply()){
				ApnsUtil.getInstance().send(atReplyer, PushType.评论, "你收到一条新的评论", 0,count);
			}else{
				ApnsUtil.getInstance().send(atReplyer, count);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public ActivityResp getActivityDetail(long activityId){
		ActivityInfo  activityInfo = this.getactivity(activityId);
		if(activityInfo == null){
			throw new GeneralLogicException("没有找到这条活动哦");
		}
		UserSession userSession = UserSession.get();
		ActivityResp resp = new ActivityResp(activityInfo);
		if(StringUtils.isNotBlank(activityInfo.getContent())&&activityInfo.getContent().contains("@")){
			resp.setAts(this.atRelationsDao.findAtTargets(ContentType.活动, activityInfo.getId()));
		}
		resp.setJoin(this.isJoinActivity(activityId, userSession.getUserId()));
		resp.setLaudAmount(this.countLikeActivity(activityId));
		resp.setReplyAmount(this.countReplyActivity(activityId, userSession.getCourtyardId()));
		resp.setLauded(this.isLikeActibity(userSession.getUserId(), activityId));
		
		//返回点赞的人
		List<Long> userIds = this.activityLaudDao.laudActivityUser(activityId);
		if(CollectionUtils.isNotEmpty(userIds)){
			List<LaudListDto> laudLists = new ArrayList<LaudListDto>();
			 List<User> users = this.userDao.get(userIds);
			    for(User user : users){
			    	LaudListDto dto = new LaudListDto(user);
			    	laudLists.add(dto);
			    }
			resp.setLaudList(laudLists);
		}
		
		activityInfo.setViews(activityInfo.getViews()+1);
		this.activityInfoDao.saveOrUpdate(activityInfo);
		// 可以报名，但人数已满
		boolean peoplesFull= !activityInfo.isSignDisable()&&this.countSignedAmountForActivity(activityId)>=activityInfo.getPeoplesLimit();
		resp.setPeoplesFull(peoplesFull);
		// 评论
		List<ActivityReply> replys = this.findActivityReplys(activityId,userSession.getCourtyardId());
		if(CollectionUtils.isNotEmpty(replys)){
			List<ActivityReplyDto> replyDtos = new ArrayList<ActivityReplyDto>();
			for(ActivityReply reply : replys){
				ActivityReplyDto dto = new ActivityReplyDto(reply);
				User replyer = this.userService.findUserById(reply.getReplyer_id());
				dto.setSenderHeadIcon(replyer.getHead_icon());
				dto.setSenderName(replyer.getNickname());
				if(StringUtils.isNotBlank(reply.getContent())&&reply.getContent().contains("@")){
					dto.setAts(this.atRelationsDao.findAtTargets(ContentType.活动评论, reply.getId()));
				}
				//回复
				List<ActivityReply> replyAts = this.findActivityReply(activityId,reply.getId());
				if(CollectionUtils.isNotEmpty(replyAts)){
					List<ActivityReplyDto> replyAtDtos = new ArrayList<ActivityReplyDto>();
					for(ActivityReply activityReply : replyAts){
						ActivityReplyDto atdto = new ActivityReplyDto(activityReply);
						if(StringUtils.isNotBlank(activityReply.getContent())&&activityReply.getContent().contains("@")){
							atdto.setAts(this.atRelationsDao.findAtTargets(ContentType.活动评论, activityReply.getId()));
						}
						User user = this.userService.findUserById(activityReply.getReplyer_id());
						atdto.setSenderName(user.getNickname());
						User atUser = this.userService.findUserById(activityReply.getAt_targetId());
						atdto.setAtTargetId(activityReply.getAt_targetId());
						atdto.setAtTargetName(atUser.getNickname());
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
	public ActivityResp shareActivity(long activityId,long courtyardId){
		ActivityInfo  activityInfo = this.getactivity(activityId);
		if(activityInfo == null){
			throw new GeneralLogicException("没有找到这条活动哦");
		}
		ActivityResp resp = new ActivityResp(activityInfo);
		// 可以报名，但人数已满
//		boolean peoplesFull= !activityInfo.isSignDisable()&&this.countSignedAmountForActivity(activityId)>=activityInfo.getPeoplesLimit();
//		resp.setPeoplesFull(peoplesFull);
		// 评论
		List<ActivityReply> replys = this.findActivityReplys(activityId,courtyardId);
		if(CollectionUtils.isNotEmpty(replys)){
			List<ActivityReplyDto> replyDtos = new ArrayList<ActivityReplyDto>();
			for(ActivityReply reply : replys){
				ActivityReplyDto dto = new ActivityReplyDto(reply);
				User replyer = this.userService.findUserById(reply.getReplyer_id());
				dto.setSenderHeadIcon(replyer.getHead_icon());
				dto.setSenderName(replyer.getNickname());
				//回复
//				List<ActivityReply> replyAts = this.findActivityReply(activityId,reply.getId());
//				if(CollectionUtils.isNotEmpty(replyAts)){
//					List<ActivityReplyDto> replyAtDtos = new ArrayList<ActivityReplyDto>();
//					for(ActivityReply activityReply : replyAts){
//						ActivityReplyDto atdto = new ActivityReplyDto(activityReply);
//						User user = this.userService.findUserById(activityReply.getReplyer_id());
//						atdto.setSenderName(user.getNickname());
//						User atUser = this.userService.findUserById(activityReply.getAt_targetId());
//						atdto.setAtTargetId(activityReply.getAt_targetId());
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
	
	@Override
	@Transactional(readOnly = true)
	public  ActivityListResp  getActivityList(){
		UserSession userSession = UserSession.get();
		List<ActivityInfo> activityInfos = this.findActivityInfo(userSession.getCourtyardId());
		ActivityListResp resp =	new ActivityListResp();
		if(CollectionUtils.isNotEmpty(activityInfos)){
			for(ActivityInfo activityInfo : activityInfos){
				if(activityInfo.getEndTime().getTime()<System.currentTimeMillis()){
					continue;
				}
				if(activityInfo.getStartTime().getTime()>System.currentTimeMillis()){
				    	continue;
				}
				ActivityDto activityDto  = new ActivityDto(activityInfo);
				if(!userSession.isVisitor()){
					activityDto.setJoin(this.isJoinActivity(activityInfo.getId(), userSession.getUserId()));
					activityDto.setLauded(this.isLikeActibity(userSession.getUserId(), activityInfo.getId()));
				}else{
					activityDto.setJoin(false);
					activityDto.setLauded(false);
				}
				resp.getDatas().add(activityDto);
			}	
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IActivityService#countSignedAmountForActivity(long)
	 */
	@Override
	@Transactional(readOnly = true)
	public int countSignedAmountForActivity(long activityId) {
		return (int)activitySignUpDao.count(Restrictions.eq("activity_id", activityId));
	}
	
	/**
	 * @see cn.dayuanzi.service.IActivityService#getSignUpUsers(long)
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public ContactsResp getSignUpUsers(long activityId) {
		ContactsResp resp = new ContactsResp();
		List<Long> userIds = activitySignUpDao.getSignUpUsers(activityId);
		if(!userIds.isEmpty()){
			List<User> follows = this.userDao.get(userIds);
			for(User user : follows){
				resp.getDatas().add(new ContactsDto(user));
			}
		}
		return resp;
	}
}
