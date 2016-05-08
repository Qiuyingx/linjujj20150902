package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.Invitation;

/**
 * 
 * @author : leihc
 * @date : 2015年4月20日 下午4:54:45
 * @version : 1.0
 */
@Repository
public class InvitationDao extends BaseDao<Long, Invitation> {

	/**
	 * 查找与自己兴趣相同的邀约
	 * 
	 * @param interests
	 * @return
	 */
	public List<Invitation> findInvitations(long courtyard_id, List<Integer> interests){
		return this.find(Restrictions.eq("courtyard_id", courtyard_id),Restrictions.in("invitation_type", interests));
	}
	
	/**
	 * 计算当前院子中与自己兴趣相同的未读邀约数
	 * 
	 * @param interests
	 * @return
	 */
	public long countInvitations(long courtyard_id, long lastReadInvitation){
		Criterion sameYard = Restrictions.eq("courtyard_id", courtyard_id);
		Criterion gtId = Restrictions.gt("id", lastReadInvitation);
		return this.count(sameYard,gtId);
	}
	
	/**
	 * 查询用户发出的最新一条邀约
	 * @param courtyardId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Invitation getLatestInvitation(long courtyardId, long userId){
		Criteria criteria = this.createCriteria(Restrictions.eq("user_id", userId));
//		ProjectionList list = Projections.projectionList();
//		list.add(Projections.max("id"));
//		criteria.setProjection(list);
//		Long maxId = (Long)criteria.uniqueResult();
//		if(maxId!=null){
//			return this.get(maxId);
//		}
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("create_time"));
		List<Invitation> r = criteria.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
	/**
	 * 查询最新的一条邀约
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Invitation getLatestInvitations(final Criterion... criterions ){
		Criteria c = this.createCriteria( criterions);
		c.setMaxResults(1);
		c.addOrder(Order.desc("create_time"));
		List<Invitation> r = c.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
	/**
	 * 查询最新的一条邀约
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Invitation getLatestInvitations(long courtyardId){
		Criteria c = this.createCriteria(Restrictions.eq("courtyard_id", courtyardId));
		c.setMaxResults(1);
		c.addOrder(Order.desc("create_time"));
		List<Invitation> r = c.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
	
	
}
