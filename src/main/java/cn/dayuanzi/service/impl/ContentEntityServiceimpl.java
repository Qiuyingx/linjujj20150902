/**
 * 
 */
package cn.dayuanzi.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.AtRelationsDao;
import cn.dayuanzi.dao.ContentEntityDao;
import cn.dayuanzi.dao.ContentEntityLaudDao;
import cn.dayuanzi.dao.ContentEntityReplyDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.MessageStatusDao;
import cn.dayuanzi.dao.PushMessageStatusDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.model.ContentEntity;
import cn.dayuanzi.model.ContentEntityLaud;
import cn.dayuanzi.model.ContentEntityReply;
import cn.dayuanzi.model.MessageStatus;
import cn.dayuanzi.model.PushMessageStatus;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.pojo.PushType;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.ContentEntityListResp;
import cn.dayuanzi.resp.ContentEntityResp;
import cn.dayuanzi.resp.dto.ContentEntityDto;
import cn.dayuanzi.resp.dto.ContentEntityReplyDto;
import cn.dayuanzi.resp.dto.LaudListDto;
import cn.dayuanzi.service.IContentEntityService;
import cn.dayuanzi.service.IUserService;
import cn.dayuanzi.util.ApnsUtil;
import cn.dayuanzi.util.YardUtils;


/** 
 * @ClassName: ContentEntityServiceimpl 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月15日 上午9:11:29 
 *  
 */
@Service
public class ContentEntityServiceimpl implements IContentEntityService{

    @Autowired
	private UserDao userDao;
    @Autowired
	private ContentEntityDao contentEntityDao;
    @Autowired
    private  ContentEntityReplyDao  contentEntityReplyDao;
    @Autowired
	private MessageStatusDao messageStatusDao;
	@Autowired
	private PushMessageStatusDao pushMessageStatusDao;
	@Autowired
	private ContentEntityLaudDao contentEntityLaudDao;
	@Autowired
	private IUserService userService;
	@Autowired
	private CourtYardDao courtYardDao;
	@Autowired
	private AtRelationsDao atRelationsDao;
	
	/**
	 * 专题列表
	 */
    @Override
	@Transactional
	public ContentEntityListResp getContentEntityList(int current_page, int max_num,long courtyardId){
	    ContentEntityListResp resp = new ContentEntityListResp();
	    int cityid = this.courtYardDao.get(courtyardId).getCity_id();
	    List<ContentEntity> contentEntitys = this.contentEntityDao.findForPage(current_page, max_num, "create_time", false,Restrictions.or(Restrictions.eq("cityId",0),Restrictions.eq("cityId",cityid)),Restrictions.eq("status", 1));;
		if(CollectionUtils.isNotEmpty(contentEntitys)){
			boolean logined = UserSession.get()!=null;
			if(logined){
				MessageStatus ms = ServiceRegistry.getMessageCheckService().findMessageCheck(UserSession.get().getUserId(), 0);
				long latestTime = ms.getLast_linju_subject_time();
				for(ContentEntity contentEntity : contentEntitys){
				    ContentEntityDto contentEntityDto  = new ContentEntityDto(contentEntity);
				    resp.getDatas().add(contentEntityDto);
					
					if(contentEntity.getCreate_time()>latestTime){
						latestTime = contentEntity.getCreate_time();
					}
				}
				if(latestTime!=ms.getLast_linju_subject_time()){
					ms.setLast_linju_subject_time(latestTime);
					this.messageStatusDao.saveOrUpdate(ms);
					PushMessageStatus pms = ServiceRegistry.getMessageCheckService().findPushMessageCheck(UserSession.get().getUserId());
					pms.setLast_linju_subject_time(latestTime);
					this.pushMessageStatusDao.saveOrUpdate(pms);
				}
			}else{
				for(ContentEntity contentEntity : contentEntitys){
				    ContentEntityDto contentEntityDto  = new ContentEntityDto(contentEntity);
				    resp.getDatas().add(contentEntityDto);
				}
			}
			
		}
	    return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IContentEntityService#searchContentEntity(long, java.lang.String)
	 */
	@Override
	@Transactional(readOnly=true,propagation=Propagation.NOT_SUPPORTED)
	public ContentEntityListResp searchContentEntity(long courtyardId,String keys) {
		ContentEntityListResp resp = new ContentEntityListResp();
		int cityid = this.courtYardDao.get(courtyardId).getCity_id();
		List<ContentEntity> contentEntitys = this.contentEntityDao.find(Restrictions.or(Restrictions.eq("cityId",0),Restrictions.eq("cityId",cityid)),Restrictions.eq("status", 1),Restrictions.like("title", "%"+keys+"%"));
		for(ContentEntity contentEntity : contentEntitys){
		    ContentEntityDto contentEntityDto  = new ContentEntityDto(contentEntity);
		    resp.getDatas().add(contentEntityDto);
		}
		return resp;
	}
	
    	/**
	 * 专题详情
	 */
	@Override
	@Transactional(readOnly = false)
	public ContentEntityResp getContentEntityDetail (long contentId){
	    ContentEntity contentEntity = this.contentEntityDao.get(contentId);
	    UserSession userSession = UserSession.get();
	    if(contentEntity == null||contentEntity.getStatus()!=1){
	    	throw new GeneralLogicException("没有找到这条专题哦");
	    }
	    ContentEntityResp resp = new ContentEntityResp(contentEntity);
	    if(StringUtils.isNotBlank(contentEntity.getContent())&&contentEntity.getContent().contains("@")){
			resp.setAts(this.atRelationsDao.findAtTargets(ContentType.专题, contentEntity.getId()));
		}
	    resp.setLaudAmount(this.countLaud(contentId,0));
	    resp.setReplyAmount(this.countReply(contentId,0));
	    resp.setLauded(this.isContentEntity(userSession.getUserId(), contentId));
	 
	    //返回点赞的人
	  	List<Long> userIds = this.contentEntityLaudDao.laudContentEntityUser(contentId);
	  	if(CollectionUtils.isNotEmpty(userIds)){
	  		List<LaudListDto> laudLists = new ArrayList<LaudListDto>();
	  		List<User> users = this.userDao.get(userIds);
			   for(User user : users){
			    	LaudListDto dto = new LaudListDto(user);
			    	laudLists.add(dto);
			   }
	  		resp.setLaudList(laudLists);
	  	}

	    //评论
	    List<ContentEntityReply> replys = this.findContentEntityReplys(contentId,userSession.getCourtyardId());
		if(CollectionUtils.isNotEmpty(replys)){
			List<ContentEntityReplyDto> replyDtos = new ArrayList<ContentEntityReplyDto>();
			for(ContentEntityReply reply : replys){
			    ContentEntityReplyDto dto = new ContentEntityReplyDto(reply);
				User replyer = this.userService.findUserById(reply.getReplyer_id());
				dto.setSenderHeadIcon(replyer.getHead_icon());
				dto.setSenderName(replyer.getNickname());
				if(StringUtils.isNotBlank(reply.getContent())&&reply.getContent().contains("@")){
					dto.setAts(this.atRelationsDao.findAtTargets(ContentType.专题评论, reply.getId()));
				}
				//回复
				List<ContentEntityReply> replyAts = this.findContentEntityReply(contentId,reply.getId());
				if(CollectionUtils.isNotEmpty(replyAts)){
					List<ContentEntityReplyDto> replyAtDtos = new ArrayList<ContentEntityReplyDto>();
					for(ContentEntityReply contentEntityReply : replyAts){
					    ContentEntityReplyDto atdto = new ContentEntityReplyDto(contentEntityReply);
						User user = this.userService.findUserById(contentEntityReply.getReplyer_id());
						atdto.setSenderName(user.getNickname());
						User atUser = this.userService.findUserById(contentEntityReply.getAt_targetId());
						atdto.setAtTargetId(contentEntityReply.getAt_targetId());
						atdto.setAtTargetName(atUser.getNickname());
						if(StringUtils.isNotBlank(contentEntityReply.getContent())&&contentEntityReply.getContent().contains("@")){
							atdto.setAts(this.atRelationsDao.findAtTargets(ContentType.专题评论, contentEntityReply.getId()));
						}
						replyAtDtos.add(atdto);
					}
					dto.setReplys(replyAtDtos);
				}
				replyDtos.add(dto);
			}
			resp.setReplys(replyDtos);
		}
	    //浏览计数
	    contentEntity.setViews(contentEntity.getViews()+1);
	    this.contentEntityDao.saveOrUpdate(contentEntity);
	    MessageStatus ms = ServiceRegistry.getMessageCheckService().findMessageCheck(UserSession.get().getUserId(), 0);
	    PushMessageStatus pms = ServiceRegistry.getMessageCheckService().findPushMessageCheck(UserSession.get().getUserId());
	    ContentEntity ce = this.contentEntityDao.getLatestContentEntity(contentEntity.getCityId());
	    if(ce!=null&&ms.getLast_linju_subject_time()!=ce.getCreate_time()){
	    	ms.setLast_linju_subject_time(ce.getCreate_time());
	    	pms.setLast_linju_subject_time(ce.getCreate_time());
	    }
	    return resp;
	}
	
	
	/**
	 * 专题分享
	 */
	@Override
	@Transactional(readOnly = true)
	public  ContentEntityResp shareContentEntityDetail(long contentId){
	    ContentEntity contentEntity = this.contentEntityDao.get(contentId);
	    if(contentEntity == null||contentEntity.getStatus()!=1){
		throw new GeneralLogicException("没有找到这条专题哦");
	    }
	    ContentEntityResp resp = new ContentEntityResp(contentEntity);
	    return resp;
	
	}
	/**
	 * 专题评论
	 * @param targetId
	 * @param userId
	 * @param replyType
	 * @param content
	 * @param atReplyerId
	 */
	@Override
	@Transactional(readOnly = false)
	public void replycontentEntity(long targetId,long userId, long replyType,String content,long atReplyerId){
		User user = this.userDao.get(userId);
		if(user.isBanned()){
			throw new GeneralLogicException("您已被禁言");
		}
		if(replyType==1){
		    ContentEntity info = this.contentEntityDao.get(targetId);
			//评论专题
			if(info==null){
				throw new GeneralLogicException("没有这个专题哦");
			}
			List<User> atUsers = YardUtils.findAt(content);
			ContentEntityReply actReply = new ContentEntityReply(user,targetId,content);
			this.contentEntityReplyDao.save(actReply);
			if(atUsers!=null){
				for(User u : atUsers){
					AtRelations ar = new AtRelations(userId, u);
					ar.setScene(ContentType.专题评论.getValue());
					ar.setAppend(actReply.getId());
					this.atRelationsDao.save(ar);
				}
			}
		}else{
			//回复评论
		    ContentEntityReply replyTarget = this.contentEntityReplyDao.get(targetId);
			if(replyTarget==null){
				throw new GeneralLogicException("没有找到这个评论哟");
			}
			//注释原因专题不能互评
			//if(user.getCourtyard_id()!=replyTarget.getCourtyard_id()){
				//throw new GeneralLogicException("只能回复当前所在院子的帖子");
			//}
			if(replyTarget.getReply_id() > 0){
				throw new GeneralLogicException("不能回复");
			}
			User atReplyer = this.userDao.get(atReplyerId);
			if(atReplyer==null){
				throw new GeneralLogicException("出了点小问题，稍后试试");
			}
			ContentEntity info = this.contentEntityDao.get(replyTarget.getContent_id());
			//评论专题
			if(info==null){
				throw new GeneralLogicException("没有这个专题哦");
			}
			List<User> atUsers = YardUtils.findAt(content);
			ContentEntityReply reply  = new ContentEntityReply(user,replyTarget.getContent_id(),content);
			reply.setAt_targetId(atReplyerId);
			reply.setReply_id(targetId);
			this.contentEntityReplyDao.save(reply);
			if(atUsers!=null){
				for(User u : atUsers){
					AtRelations ar = new AtRelations(userId, u);
					ar.setScene(ContentType.专题评论.getValue());
					ar.setAppend(reply.getId());
					this.atRelationsDao.save(ar);
				}
			}
			//
			int count = ServiceRegistry.getMessageCheckService().getAllAndUpdateNotReadReply(atReplyer.getId(), 1);
			UserSetting setting = this.userService.getUserSetting(atReplyerId);
			if(setting.isReply()){
				ApnsUtil.getInstance().send(atReplyer, PushType.评论, "你收到一条新的评论", 0,count);
			}else{
				ApnsUtil.getInstance().send(atReplyer, count);
			}
		}
	}
	/**
	 * 专题喜欢
	 */
	@Override
	@Transactional(readOnly = false)
	public void likecontentEntity(long userId,long contentId,long courtyardId){
	    	ContentEntity  contentEntity = this.contentEntityDao.get(contentId);
		if(contentEntity == null){
			throw new GeneralLogicException("没有找到这条专题哦");
		}
		System.out.println(this.isContentEntity(userId, contentId));
		if(this.isContentEntity(userId, contentId)){
			throw new GeneralLogicException("你已经赞过了哟");
		}
		
		ContentEntityLaud act = new ContentEntityLaud(userId,courtyardId,contentId);
		this.contentEntityLaudDao.save(act);
	}
	/**
	 * 专题是否喜欢
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isContentEntity(long userId,long cotentId){
		return this.contentEntityLaudDao.findUnique(Restrictions.eq("content_id",cotentId),Restrictions.eq("user_id", userId))!=null;
	}
	/**
	 * 查询评论
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ContentEntityReply> findContentEntityReplys(long cotentId, long courtyardId ){
		return this.contentEntityReplyDao.findContentEntityReply("create_time",false,Restrictions.eq("content_id", cotentId),Restrictions.eq("reply_id", 0l));
	}
	/**
	 * 查询回复
	 * @param activityId
	 * @param rereplyId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<ContentEntityReply> findContentEntityReply(long cotentId, long rereplyId ){
		return this.contentEntityReplyDao.findContentEntityReply("create_time", false,Restrictions.eq("content_id", cotentId),Restrictions.eq("reply_id", rereplyId));
		
	}
	/**
	 * 专题点赞数量
	 */
	@Override
	@Transactional(readOnly = true)
	public long countLaud(long contentId,long courtyardId) {
		return this.contentEntityLaudDao.count(Restrictions.eq("content_id", contentId));
	}
	/**
	 * 专题评论数量
	 */
	@Override
	@Transactional(readOnly = true)
	public long countReply(long contentId,long courtyardId){
	    return this.contentEntityReplyDao.count(Restrictions.eq("content_id", contentId));
	}
}
