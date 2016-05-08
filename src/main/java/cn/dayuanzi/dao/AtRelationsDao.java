package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.AtRelations;
import cn.dayuanzi.pojo.ContentType;

/**
 * 
 * @author : leihc
 * @date : 2015年7月26日
 * @version : 1.0
 */
@Repository
public class AtRelationsDao extends BaseDao<Long, AtRelations> {

	
	@SuppressWarnings("unchecked")
	public List<Object[]> findAtTargets(ContentType contentType, long append){
		Criteria criteria = this.createCriteria(Restrictions.eq("scene", contentType.getValue()),Restrictions.eq("append", append));
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("at_target_id"));
		pl.add(Projections.property("at_nickname"));
		criteria.setProjection(Projections.distinct(pl));
		return criteria.list();
	}
	
	/**
	 * 
	 * @param userId
	 * @param lastReadAtTime
	 * @return
	 */
	public long countNotRead(long userId, long lastReadAtTime){
		return this.count(Restrictions.eq("at_target_id", userId),Restrictions.gt("create_time", lastReadAtTime));
	}
	
	/**
	 * 获取最后一条艾特
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public AtRelations getLatestAtRelations(long userId){
		Criteria c = this.createCriteria(Restrictions.eq("at_target_id", userId));
		c.setMaxResults(1);
		c.addOrder(Order.desc("create_time"));
		List<AtRelations> r = c.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
}
