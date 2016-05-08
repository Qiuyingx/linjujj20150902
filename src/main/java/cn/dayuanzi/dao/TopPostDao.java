package cn.dayuanzi.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.TopPost;

/**
 * 
 * @author : leihc
 * @date : 2015年8月4日
 * @version : 1.0
 */
@Repository
public class TopPostDao extends BaseDao<Long, TopPost> {

	/**
	 * 获取置顶贴
	 * @param currentCourtyard_id 当前用户的社区ID
	 * @param courtyardIds 当前用户指定范围的周边社区
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TopPost> getTop(long currentCourtyard_id,List<Long> courtyardIds){
		Criteria c = this.createCriteria();
		if(CollectionUtils.isEmpty(courtyardIds)){
			c.add(Restrictions.eq("courtyard_id", currentCourtyard_id));
		}else{
			Disjunction dis = Restrictions.disjunction();
			dis.add(Restrictions.eq("courtyard_id", currentCourtyard_id));
			Conjunction con = Restrictions.conjunction();
			con.add(Restrictions.in("courtyard_id", courtyardIds));
			con.add(Restrictions.eq("topType", 1));
			dis.add(con);
			c.add(dis);
		}
		c.setMaxResults(2);
		c.addOrder(Order.desc("priority"));
		return c.list();
	}
	
	/**
	 * 同城置顶
	 * 
	 * @param currentCourtyard_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TopPost> getTop(long currentCourtyard_id){
		Criteria c = this.createCriteria();
		Disjunction dis = Restrictions.disjunction();
		dis.add(Restrictions.eq("courtyard_id", currentCourtyard_id));
		dis.add(Restrictions.eq("topType", 1));
		c.add(dis);
		c.setMaxResults(2);
		c.addOrder(Order.desc("priority"));
		return c.list();
	}
}
