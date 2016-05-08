/**
 * 
 */
package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.ActivityLaud;

/** 
 * @ClassName: ActivityLaudDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月7日 下午2:33:52 
 *  
 */
@Repository
public class ActivityLaudDao extends BaseDao<Long, ActivityLaud>{

	
	
	/**
	 *  查询点击该帖的人
	 */
	@SuppressWarnings("unchecked")
	public List<Long> laudActivityUser(long activityId){
	    	Criteria c = createCriteria(Restrictions.eq("activity_id", activityId));
		c.setProjection(Projections.distinct(Projections.property("user_id")));
		return c.list();
	}
}
