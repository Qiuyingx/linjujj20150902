package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.UserFollow;

/**
 * 
 * @author : leihc
 * @date : 2015年7月1日
 * @version : 1.0
 */
@Repository
public class UserFollowDao extends BaseDao<Long, UserFollow> {

	/**
	 * 查询关注的对象ID列表
	 * 
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findFollowIds(long user_id){
		Criteria c = this.createCriteria(Restrictions.eq("user_id", user_id));
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("target_id"));
		c.setProjection(list);
		return  c.list();
	}
	
	/**
	 * 获取关注自己的人
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findFollowerIds(long user_id,int current_page, int max_num){
		List<Long> result = null;
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		Criteria c = this.createCriteria(Restrictions.eq("target_id", user_id));
		ProjectionList list = Projections.projectionList();
		c.addOrder(Order.desc("create_time"));
		list.add(Projections.property("user_id"));
		c.setFirstResult(((current_page-1)) * max_num);
		c.setMaxResults(max_num);
		c.setProjection(list);
		result =  c.list();
		return result;
	}
	/**
	 * 是否关注对方
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public boolean isFollowed(long userId, long targetId){
		return this.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("target_id", targetId))!=null;
	}
	
	/**
	 * 取消关注
	 * @param userId
	 * @param targetId
	 */
	public void delete(long userId,long targetId){
		String hql = "delete from UserFollow where user_id = ? and target_id = ?";
		this.batchExecute(hql, userId, targetId);
	}
	
	/**
	 * 查询最新的关注请求时间
	 * @param userId
	 * @return
	 */
	public UserFollow getLatestFollowTime(long userId){
		Criteria c = this.createCriteria(Restrictions.eq("target_id", userId));
		c.setMaxResults(1);
		c.addOrder(Order.desc("create_time"));
//		c.setProjection(Projections.max("create_time"));
		return (UserFollow)c.uniqueResult();
	}
	
	/**
	 * 统计未读的关注请求数
	 * @param userId
	 * @param lastReadedFollowTime
	 * @return
	 */
	public long countNotReadFollow(long userId, long lastReadedFollowTime){
		return count(Restrictions.eq("target_id", userId),Restrictions.gt("create_time", lastReadedFollowTime));
	}
	
	/**
	 * 查询关注的对象ID列表  (分页)
	 * 
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findFollowIds(long user_id ,int current_page, int max_num){
		List<Long> result = null;
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		Criteria c = this.createCriteria(Restrictions.eq("user_id", user_id));
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("target_id"));
		c.setFirstResult(((current_page-1)) * max_num);
		c.setMaxResults(max_num);
		c.setProjection(list);
		result =  c.list();
		return result;
	}
}
