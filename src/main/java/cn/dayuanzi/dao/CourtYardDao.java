package cn.dayuanzi.dao;

import java.util.List;

import cn.dayuanzi.model.CourtYard;



import cn.dayuanzi.util.GeoRange;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author : leihc
 * @date : 2015年4月24日 下午6:21:32
 * @version : 1.0
 */
@Repository
public class CourtYardDao extends BaseDao<Long, CourtYard> {

	public CourtYard findCourtYard(long courtyard_id){
		return this.findUnique(Restrictions.eq("id", courtyard_id));
	}
	
	/**
	 * 查找指定城市某个范围内的社区
	 * 
	 * @param cityId
	 * @param gr
	 * @return
	 */
	public List<CourtYard> findCourtyards(int cityId, GeoRange gr){
		Criterion lgtBetween = Restrictions.between("longitude", gr.getMinLgt(), gr.getMaxLgt());
		Criterion latBetween = Restrictions.between("latitude", gr.getMinLat(), gr.getMaxLat());
		return this.find(Restrictions.eq("city_id", cityId), lgtBetween, latBetween);
	}
	
	/**
	 * 查找指定范围的社区ID
	 * @param cityId
	 * @param gr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findCourtyardIds(int cityId, GeoRange gr){
		Criterion lgtBetween = Restrictions.between("longitude", gr.getMinLgt(), gr.getMaxLgt());
		Criterion latBetween = Restrictions.between("latitude", gr.getMinLat(), gr.getMaxLat());
		Criteria c = this.createCriteria(Restrictions.eq("city_id", cityId), lgtBetween, latBetween);
		c.setProjection(Projections.property("id"));
		return c.list();
	}
}
