package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.ContentEntity;

/**
 * 专题
 * @author : leihc
 * @date : 2015年7月3日
 * @version : 1.0
 */
@Repository
public class ContentEntityDao extends BaseDao<Long, ContentEntity> {

	
	/**
	 * 获取最新的专题
	 * @param cityId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ContentEntity getLatestContentEntity(int cityId){
		Criteria c = this.createCriteria(Restrictions.or(Restrictions.eq("cityId", cityId), Restrictions.eq("cityId", 0)),Restrictions.eq("status", 1));
		c.setMaxResults(1);
		c.addOrder(Order.desc("create_time"));
		List<ContentEntity> r = c.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
	
}
