package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.view.TrainView;

/**
 * 
 * @author : leihc
 * @date : 2015年8月17日
 * @version : 1.0
 */
@Repository
public class TrainViewDao extends BaseDao<Long, TrainView> {

	
	@SuppressWarnings("unchecked")
	public List<TrainView> getAllByCity(long cityId){
		Criteria c = this.createCriteria(Restrictions.eq("cityId", cityId));
		return c.list();	
	}
}
