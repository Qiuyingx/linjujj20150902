package cn.dayuanzi.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.ValidateUser;

/**
 * 
 * @author : leihc
 * @date : 2015年4月19日 下午12:25:59
 * @version : 1.0
 */
@Repository
public class ValidateUserDao extends BaseDao<Long, ValidateUser> {

	public ValidateUser findValidateUser(long userId, long courtyardId){
		return this.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("courtyard_id", courtyardId));
	}
	
	public boolean isValidate(long userId, long courtyardId){
		ValidateUser v = findValidateUser(userId, courtyardId);
		return v!=null&&v.getValidate_status()==1;
	}
	
	/**
	 * 查找属于这个社区的用户ID
	 * @param courtyardId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findUserId(long courtyardId,int current_page, int max_num){
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		Criteria criteria = this.createCriteria(Restrictions.eq("courtyard_id", courtyardId),Restrictions.eq("validate_status", 1));
//		if(!friendIds.isEmpty()){
//			criteria.add(Restrictions.not(Restrictions.in("user_id", friendIds)));
//		}
		criteria.setFirstResult(((current_page-1)) * max_num);
		criteria.setMaxResults(max_num);
		criteria.addOrder(Order.desc("create_time"));
		
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("user_id"));
		criteria.setProjection(list);
		return criteria.list();
	}
	
	/**
	 * 查询最新通过验证的时间，不包括好友和自己
	 * 
	 * @param courtyardId
	 * @param userId
	 * @param friendIds
	 * @return
	 */
	public long getLatestValidatedTime(long courtyardId,long userId){
		Criteria criteria = this.createCriteria(Restrictions.eq("courtyard_id", courtyardId),Restrictions.eq("validate_status", 1),Restrictions.ne("user_id", userId));
//		if(!friendIds.isEmpty()){
//			criteria.add(Restrictions.not(Restrictions.in("user_id", friendIds)));
//		}
		criteria.setProjection(Projections.max("create_time"));
		Long latestTime = (Long)criteria.uniqueResult();
		if(latestTime==null){
			latestTime = 0l;
		}
		return latestTime;
	}
	
	/**
	 * 统计邻居数，不包括好友和自己
	 * 
	 * @param courtyardId
	 * @param userId
	 * @param friendIds
	 * @return
	 */
	public long count(long courtyardId,long userId, long lastValidateTime,List<Long> friendIds){
		Criteria criteria = this.createCriteria(Restrictions.eq("courtyard_id", courtyardId),Restrictions.eq("validate_status", 1),Restrictions.ne("user_id", userId));
		criteria.add(Restrictions.gt("create_time", lastValidateTime));
		if(friendIds!=null&&!friendIds.isEmpty()){
			criteria.add(Restrictions.not(Restrictions.in("user_id", friendIds)));
		}
		
		criteria.setProjection(Projections.rowCount());
		
		Long count = (Long)criteria.uniqueResult();
		if(count==null){
			count = 0l;
		}
		return count;
	}
	
	
	@SuppressWarnings("unchecked")
	public ValidateUser getLastValidate(long userId){
		Criteria c = this.createCriteria(Restrictions.eq("user_id", userId));
		c.setMaxResults(1);
		c.addOrder(Order.desc("create_time"));
		List<ValidateUser> r = c.list();
		if(r.isEmpty()){
			return null;
		}
		return r.get(0);
	}
}
