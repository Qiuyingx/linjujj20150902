package cn.dayuanzi.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.User;

/**
 * 
 * @author : lhc
 * @date : 2015年4月10日 下午5:20:12
 * @version : 1.0
 */
@Repository
public class UserDao extends BaseDao<Long, User> {
	
	/**
	 * 查询官方账号
	 * 
	 * @return
	 */
	public List<User> getOfficial(){
		int official = 1;
		return this.find(Restrictions.eq("official", official));
	}
	
	/**
	 * 查询官方账号Id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getOfficialId(){
		int official = 1;
		Criteria c = this.createCriteria(Restrictions.eq("official", official));
		c.setProjection(Projections.property("id"));
		return c.list();
	}
	/**
	 * At用户列表
	 */
	@SuppressWarnings("unchecked")
	public  List<User> getAtUserId(int current_page, int max_num,long courtyardId,List<Long> f){
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		Disjunction d = Restrictions.disjunction();
		d.add(Restrictions.eq("courtyard_id", courtyardId));
		if(CollectionUtils.isNotEmpty(f)){
			d.add(Restrictions.in("id",f));
		}
		Criteria c = this.createCriteria(d);
		c.setFirstResult(((current_page-1)) * max_num);
		c.setMaxResults(max_num);
		c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		return c.list();
	
	}
}
