/**
 * 
 */
package cn.dayuanzi.dao;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;






import cn.dayuanzi.config.Interest;
import cn.dayuanzi.config.InterestConf;
import cn.dayuanzi.model.TrainInfo;
import cn.dayuanzi.util.GeoRange;


/** 
 * @ClassName: TrainInfoDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年8月12日 下午7:02:07 
 *  
 */
@Repository
public class TrainInfoDao extends BaseDao<Long, TrainInfo>{

	
	@SuppressWarnings("unchecked")
	public List<TrainInfo>  findTrain(int current_page, int max_num,final Criterion... criterions){
		List<TrainInfo> result = null;
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		Criteria c = createCriteria(criterions);
		c.setFirstResult(((current_page-1)) * max_num);
		c.setMaxResults(max_num);
		
		result =  c.list();
		return result;
	}
	
	/**
	 * 查找指定城市某个范围内的店铺
	 * 
	 * @param cityId
	 * @param gr
	 * @return
	 */
	public List<TrainInfo> findTrainInfo(int cityId, GeoRange gr){
		Criterion lgtBetween = Restrictions.between("longitude", gr.getMinLgt(), gr.getMaxLgt());
		Criterion latBetween = Restrictions.between("latitude", gr.getMinLat(), gr.getMaxLat());
		return this.find(Restrictions.eq("city_id", cityId), lgtBetween, latBetween);
	}
	/**
	 * 模糊查询相同类型的店铺
	 * @param tag
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TrainInfo findtrain(String categoryName,long userId){
		Criteria c = this.createCriteria(Restrictions.like("categoryName", "%"+categoryName+"%"),Restrictions.ne("userId", userId));
		c.setMaxResults(1);
		List <TrainInfo> tr = c.list();
		if(!tr.isEmpty()){
			return tr.get(0);
		}
		return null;	
	}
	/**
	 * 模糊查询用户爱好的店铺
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public TrainInfo findtrains(List<Integer> interests){
		Integer interestId = interests.get(RandomUtils.nextInt(interests.size()));
		Interest in =InterestConf.getInterests().get(interestId);
		Criteria c = this.createCriteria(Restrictions.like("categoryName", "%"+in.getInterest()+"%"));
		c.setMaxResults(1);
		List <TrainInfo> tr = c.list();
		if(!tr.isEmpty()){
			return tr.get(0);
		}
		return null;	
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]>  findTrainPlace(int cityId){
		Criteria c = createCriteria(Restrictions.eq("cityId", cityId));
		ProjectionList p = Projections.projectionList();
		p.add(Projections.property("id"));
		p.add(Projections.property("longitude"));
		p.add(Projections.property("latitude"));
		
		return c.list();
	}
}
