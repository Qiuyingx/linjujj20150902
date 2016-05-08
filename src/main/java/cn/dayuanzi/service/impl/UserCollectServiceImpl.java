package cn.dayuanzi.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;




import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.AtRelationsDao;
import cn.dayuanzi.dao.ContentEntityDao;
import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.InvitationDao;
import cn.dayuanzi.dao.UserCollectDao;
import cn.dayuanzi.dao.UserDao;
import cn.dayuanzi.dao.UserPostDao;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserCollect;
import cn.dayuanzi.model.UserPost;
import cn.dayuanzi.pojo.ContentType;
import cn.dayuanzi.resp.InvitationListResp;
import cn.dayuanzi.resp.UserPostListResp;
import cn.dayuanzi.resp.dto.InvitationDto;
import cn.dayuanzi.resp.dto.PostDetailDto;
import cn.dayuanzi.service.IInvitationService;
import cn.dayuanzi.service.IUserCollectService;
import cn.dayuanzi.service.IUserPostService;

/**
 * 
 * @author : leihc
 * @date : 2015年4月27日
 * @version : 1.0
 */
@Service
public class UserCollectServiceImpl implements IUserCollectService {

	@Autowired
	private UserCollectDao userCollectDao;
	@Autowired
	private UserPostDao userPostDao;
	@Autowired
	private InvitationDao invitationDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IUserPostService userPostService;
	@Autowired
	private IInvitationService invitationService;
	@Autowired
	private CourtYardDao courtYardDao;
	@Autowired
	private ContentEntityDao contentEntityDao;
	@Autowired
	private AtRelationsDao atRelationsDao;
	
	
	/**
	 * @see cn.dayuanzi.service.IUserCollectService#collect(long, long, int, long)
	 */
	@Override
	@Transactional(readOnly = false)
	public void collect(long courtyardId, long userId, int collectType,
			long targetId) {
		if(collectType==1){
			Invitation invitation = this.invitationDao.get(targetId);
			if(invitation == null){
				throw new GeneralLogicException("您要收藏的邀约不存在或已删除");
			}
//			if(invitation.getCourtyard_id()!=courtyardId){
//				throw new GeneralLogicException("您只能收藏您院子的邀约哦");
//			}
			UserCollect userCollect = this.userCollectDao.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("collect_type", collectType),Restrictions.eq("target_id", targetId));
			if(userCollect!=null){
				throw new GeneralLogicException("已经收藏了这条邀约");
			}
		}else if(collectType==2 || collectType==3){
			UserPost userPost = this.userPostDao.get(targetId);
			if(userPost==null){
				throw new GeneralLogicException("您要收藏的话题不存在或已删除");
			}
//			if(userPost.getCourtyard_id()!=courtyardId){
//				throw new GeneralLogicException("您只能收藏您院子的话题哦");
//			}
			UserCollect userCollect = this.userCollectDao.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("collect_type", collectType),Restrictions.eq("target_id", targetId));
			if(userCollect!=null){
				throw new GeneralLogicException("已经收藏了这条话题");
			}
		}else{
			throw new GeneralLogicException("参数错误");
		}
		userCollectDao.save(new UserCollect(courtyardId,userId,collectType,targetId));
	}
	
	@Transactional(readOnly = false)
	public void cancelcollect(long courtyardId, long userId, int collectType, long targetId){
		UserCollect userCollect = this.userCollectDao.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("collect_type", collectType),Restrictions.eq("target_id", targetId));
		if(userCollect==null){
			throw new GeneralLogicException("您没有这条收藏");
		}
		this.userCollectDao.delete(userCollect);
//		if(collectType==1){
//			Invitation invitation = this.invitationDao.get(targetId);
//			if(invitation == null){
//				throw new GeneralLogicException("您要取消收藏的邀约不存在。");
//			}
//			if(invitation.getCourtyard_id()!=courtyardId){
//				throw new GeneralLogicException("您只能收藏您院子的邀约哦。");
//			}
//			
//			this.userCollectDao.removecollect(userId, targetId,collectType,courtyardId);
//		}else if(collectType==2 || collectType==3){
//			UserPost userPost = this.userPostDao.get(targetId);
//			if(userPost==null){
//				throw new GeneralLogicException("您要取消收藏的分享不存在。");
//			}
//			if(userPost.getCourtyard_id()!=courtyardId){
//				throw new GeneralLogicException("您只能取消收藏您院子的分享哦。");
//			}
//			this.userCollectDao.removecollect(userId, targetId,collectType,courtyardId);
//		}else{
//			throw new GeneralLogicException("参数错误。");
//		}
	}
	
	
	/**
	 * @see cn.dayuanzi.service.IUserCollectService#getInvitationCollect(long, int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public InvitationListResp getInvitationCollect(long courtyard_id, long userId,
			int current_page, int max_num) {
		List<Long> invitationIds = this.userCollectDao.getCollectTargetIds(courtyard_id, userId, current_page, max_num);
		InvitationListResp resp = new InvitationListResp();
		if(!invitationIds.isEmpty()){
			List<Invitation> invitations = this.invitationDao.get(invitationIds);
			for(Invitation invitation : invitations){
				InvitationDto invitationDto = new InvitationDto(invitation);
				invitationDto.setJoined(invitationService.findDiscussGroupMember(invitation.getId(), userId)!=null);
				invitationDto.setCollected(true);
				invitationDto.setClickLike(invitationService.findLikeMember(userId, invitation.getId())!=null);
				resp.getDatas().add(invitationDto);
			}
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserCollectService#getPostCollect(long, int, int)
	 */
	@Override
	@Transactional(readOnly = true,propagation=Propagation.NOT_SUPPORTED)
	public UserPostListResp getPostCollect(long courtyard_id, long userId, int current_page,int max_num) {
		List<Long> postIds = this.userCollectDao.getCollectTargetIds(courtyard_id, userId, current_page, max_num);
		UserPostListResp resp = new UserPostListResp();
		if(!postIds.isEmpty()){
			List<UserPost> userPosts = this.userPostDao.get(postIds);
			Map<Long, UserPost> temp = new HashMap<Long, UserPost>();
			for(UserPost userPost : userPosts){
				temp.put(userPost.getId(), userPost);
			}
			for(Long postId : postIds){
				UserPost userPost = temp.get(postId);
				if(userPost!=null){
					User sender = this.userDao.get(userPost.getUser_id());
					PostDetailDto postDetail = new PostDetailDto(sender, userPost);
					CourtYard yard = courtYardDao.get(sender.getCourtyard_id());
					postDetail.setCourtyardName(yard.getCourtyard_name());
					postDetail.setLaudAmount(this.userPostService.countLaud(userPost.getId()));
					postDetail.setReplyAmount(this.userPostService.countReply(userPost.getId(),userPost.getContent_type()));
					postDetail.setCollected(true);
					postDetail.setLauded(userPostService.findLaud(userId, userPost.getId())!=null);
					if(StringUtils.isNotBlank(userPost.getContent())&&userPost.getContent().contains("@")){
						postDetail.setAts(this.atRelationsDao.findAtTargets(ContentType.getInstance(userPost.getContent_type()), userPost.getId()));
					}
					resp.getDatas().add(postDetail);
				}
			}
//			for(UserPost userPost : userPosts){
//				User sender = this.userDao.get(userPost.getUser_id());
//				PostDetailDto postDetail = new PostDetailDto(sender, userPost);
//				CourtYard yard = courtYardDao.get(sender.getCourtyard_id());
//				postDetail.setCourtyardName(yard.getCourtyard_name());
//				postDetail.setLaudAmount(this.userPostService.countLaud(userPost.getId()));
//				postDetail.setReplyAmount(this.userPostService.countReply(userPost.getId(),userPost.getContent_type()));
//				postDetail.setCollected(true);
//				postDetail.setLauded(userPostService.findLaud(userId, userPost.getId())!=null);
//				resp.getDatas().add(postDetail);
//			}
		}
		return resp;
	}
	
	/**
	 * @see cn.dayuanzi.service.IUserCollectService#findUserCollect(long, int, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserCollect findUserCollect(long userId, int collect_type, long target_id) {
		UserCollect userCollect = this.userCollectDao.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("collect_type", collect_type),Restrictions.eq("target_id", target_id));
		return userCollect;
	}
	
}
