package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.UserStar;

/**
 * 
 * @author : leihc
 * @date : 2015年7月28日
 * @version : 1.0
 */
@Repository
public class UserStarDao extends BaseDao<Long, UserStar> {

	@Autowired
	private UserFollowDao userFollowDao;
	
	/**
	 * 查找可以推荐的达人，不包括自己关注的人
	 * 
	 * @param userId
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> findStarIdAndTagName(long userId,int current_page, int max_num){
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		Criteria c = this.createCriteria(Restrictions.eq("status", 1),Restrictions.ne("user_id", userId));
		if(!follows.isEmpty()){
			c.add(Restrictions.not(Restrictions.in("user_id", follows)));
		}
		ProjectionList p = Projections.projectionList();
		p.add(Projections.property("user_id"));
		p.add(Projections.property("skill"));
		c.setProjection(p);
		c.addOrder(Order.desc("create_time"));
		return c.list();
	}
}
