package cn.dayuanzi.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.LikeInvitationMembers;

/**
 * 
 * @author : leihc
 * @date : 2015年4月20日 下午6:01:30
 * @version : 1.0
 */
@Repository
public class LikeInvitationMembersDao extends BaseDao<Long, LikeInvitationMembers> {

	@Autowired
	private UserFollowDao userFollowDao;
	
	public void remove(long invitationId){
		String hql = "delete from LikeInvitationMembers where invitation_id = ?";
		this.batchExecute(hql, invitationId);
	}
	/**
	 * 获取用户相关邀约,用户发表的邀约或者点赞过的邀约
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getRelatedInvitation(long userId){
		Criteria c = this.createCriteria(Restrictions.or(Restrictions.eq("invitation_sender_id", userId), Restrictions.eq("user_id", userId)));
		c.setProjection(Projections.property("invitation_id"));
		return c.list();
	}
	
	public long countNotReadLike(long userId, long courtyardId, long lastReadLikeId){
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		long amount = 0;
		if(follows.isEmpty()){
			amount = this.count(Restrictions.gt("invitation_id", lastReadLikeId),Restrictions.eq("invitation_sender_id", userId),Restrictions.ne("user_id", userId));
		}else{
			List<Long> invitationIds = this.getRelatedInvitation(userId);
			if(invitationIds.isEmpty()){
				return amount;
			}
			Disjunction followOrAtTarget = Restrictions.disjunction();
			followOrAtTarget.add(Restrictions.eq("invitation_sender_id", userId));
			followOrAtTarget.add(Restrictions.in("user_id", follows));
			amount = this.count(Restrictions.gt("invitation_id", lastReadLikeId),Restrictions.in("invitation_id", invitationIds),Restrictions.ne("user_id", userId),followOrAtTarget);
		}
		return amount;
	}
	
	@SuppressWarnings("unchecked")
	public List<LikeInvitationMembers> findLikes(String orderByProperty, boolean isAsc, final Criterion... criterions){
		Criteria c = createCriteria(criterions);
		if(StringUtils.isNotBlank(orderByProperty)){
			if (isAsc) {
				c.addOrder(Order.asc(orderByProperty));
			} else {
				c.addOrder(Order.desc(orderByProperty));
			}
		}
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Long> getRelatedUsers(long invitationId){
		Criteria c = createCriteria(Restrictions.eq("invitation_id", invitationId));
		c.setProjection(Projections.property("user_id"));
		return c.list();
	}
	
	@SuppressWarnings("unchecked")
	public LikeInvitationMembers getLatestInvitationLaud(long userId){
//		List<Long> invitationIds = getRelatedInvitation(userId);
//		if(!invitationIds.isEmpty()){
//			Criteria c = this.createCriteria(Restrictions.in("invitation_id", invitationIds),Restrictions.ne("user_id", userId));
//			c.setMaxResults(1);
//			c.addOrder(Order.desc("create_time"));
//			List<LikeInvitationMembers> r = c.list();
//			if(!r.isEmpty()){
//				return r.get(0);
//			}
//		}
		
		Criteria criteria = null;
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		if(!follows.isEmpty()){
			List<Long> invitationIds = getRelatedInvitation(userId);
			if(invitationIds.isEmpty()){
				return null;
			}
			criteria = this.createCriteria(Restrictions.in("invitation_id", invitationIds),Restrictions.ne("user_id", userId),Restrictions.in("user_id", follows));
		}else{
			criteria = this.createCriteria(Restrictions.eq("invitation_sender_id", userId),Restrictions.ne("user_id", userId));
		}
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("create_time"));
		List<LikeInvitationMembers> result = criteria.list();
		if(!result.isEmpty()){
			return result.get(0);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getRelatedPostIdAndType(long userId){
		Criteria criteria = this.createCriteria(Restrictions.ne("invitation_sender_id", userId), Restrictions.eq("user_id", userId));
//		Criterion c = Restrictions.or(Restrictions.eq("invitation_sender_id", userId), Restrictions.eq("user_id", userId));
//		criteria.add(c);
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("invitation_id"));
		pl.add(Projections.property("create_time"));
		criteria.setProjection(Projections.distinct(pl));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<LikeInvitationMembers> getInvitationMembers(long userId){
		StringBuilder hql = new StringBuilder();
		hql.append("from LikeInvitationMembers where (");
		hql.append("invitation_sender_id="+userId);
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		if(!follows.isEmpty()){
			List<Object[]> postIds = this.getRelatedPostIdAndType(userId);
			if(!postIds.isEmpty()){
				// (post_send_id=1 or (((post_id=1 and create_time>1) or()) and user_id in(1,1)))
				hql.append(" or (");
				int index = 0;
				hql.append("(");
				for(Object[] postId : postIds){
					if(index!=0){
						hql.append(" or ");
					}
					hql.append("(invitation_id=").append((long)postId[0]);
					hql.append(" and create_time>").append((long)postId[1]).append(")");
					index++;
				}
				hql.append(")");
				index=0;
				hql.append(" and user_id in(");
				for(long follow : follows){
					if(index!=0){
						hql.append(",");
					}
					hql.append(follow);
					index++;
				}
				hql.append(")");
				hql.append(")");
			}
		}
		hql.append(")");
		Query query = this.getSession().createQuery(hql.toString());
		return query.list();
	}
	/**
	 *  查询点击该帖的人
	 */
	@SuppressWarnings("unchecked")
	public List<Long> laudInvitationUser(long postId){
	    	Criteria c = createCriteria(Restrictions.eq("invitation_id", postId));
		c.setProjection(Projections.distinct(Projections.property("invitation_sender_id")));
		return c.list();
	}
}
