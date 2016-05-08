/**
 * 
 */
package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.TrainLaud;



/** 
 * @ClassName: TrainLaudDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年8月13日 下午7:19:52 
 *  
 */
@Repository
public class TrainLaudDao extends  BaseDao<Long, TrainLaud>{

	
	/**
	 *  查询点击该帖的人
	 */
	@SuppressWarnings("unchecked")
	public List<Long> laudTrainUser(long trainId){
	    	Criteria c = createCriteria(Restrictions.eq("train_id", trainId));
	    	c.setMaxResults(20);
		c.setProjection(Projections.distinct(Projections.property("user_id")));
		return c.list();
	}
}
