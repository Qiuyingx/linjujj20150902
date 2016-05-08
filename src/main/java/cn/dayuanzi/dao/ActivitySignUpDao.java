/**
 * 
 */
package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;






import cn.dayuanzi.model.ActivitySignUp;


/** 
 * @ClassName: ActivitySignUpDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年5月28日 下午3:40:37 
 *  
 */
@Repository
public class ActivitySignUpDao extends BaseDao<Long, ActivitySignUp>{

	
	/**
	 * 判断该用户是否参加该活动
	 */
	public boolean getUserjoin (long userId,long activityId){
		return  this.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("activity_id", activityId))!=null;
	}
	
	/**
	 * 获取报名该活动的用户
	 * @param activityId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getSignUpUsers(long activityId){
		Criteria c = this.createCriteria(Restrictions.eq("activity_id", activityId));
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("user_id"));
		c.setProjection(list);
		return c.list();
	}
}
