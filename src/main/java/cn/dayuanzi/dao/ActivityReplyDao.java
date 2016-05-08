/**
 * 
 */
package cn.dayuanzi.dao;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.ActivityReply;

/** 
 * @ClassName: Activity 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月8日 下午7:11:08 
 *  
 */
@Repository
public class ActivityReplyDao extends BaseDao<Long, ActivityReply>{

	/**
	 * 查询评论
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityReply> findActivityReply(String orderByProperty, boolean isAsc, final Criterion... criterions){
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
	/**
	 * 统计未读的活动回复数
	 * 
	 * @param userId
	 * @param courtyardId
	 * @param lastReadedReplyId
	 * @return
	 */
	public long countNotReadReply(long userId, long courtyardId, long lastReadedReplyId){
		// where at_targetId = userId and courtyard_id=courtyardId and id > lastReadedReplyId
		long amount = this.count(Restrictions.eq("at_targetId", userId),Restrictions.gt("id", lastReadedReplyId));
		return amount;
	}
	
	@SuppressWarnings("unchecked")
	public ActivityReply getLatestReply(long userId){
		Criteria criteria = this.createCriteria(Restrictions.eq("at_targetId", userId));
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("create_time"));
		List<ActivityReply> r = criteria.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
}
