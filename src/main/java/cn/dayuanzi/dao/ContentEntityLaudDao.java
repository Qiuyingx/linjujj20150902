/**
 * 
 */
package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;



import cn.dayuanzi.model.ContentEntityLaud;

/** 
 * @ClassName: ContentEntityLaudDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月15日 上午11:02:53 
 *  
 */
@Repository
public class ContentEntityLaudDao extends BaseDao<Long, ContentEntityLaud>  {

	
	/**
	 *  查询点击该帖的人
	 */
	@SuppressWarnings("unchecked")
	public List<Long> laudContentEntityUser(long contentId){
	    	Criteria c = createCriteria(Restrictions.eq("content_id", contentId));
		c.setProjection(Projections.distinct(Projections.property("user_id")));
		return c.list();
	}
}
