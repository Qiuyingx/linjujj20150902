/**
 * 
 */
package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.ActivityInfo;

/** 
 * @ClassName: ActivityInfoDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年5月28日 下午3:42:33 
 *  
 */
@Repository
public class ActivityInfoDao extends BaseDao<Long, ActivityInfo>{

	
	
	/**
	 * 获取最新的活动
	 * @param activityIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActivityInfo getLatestActivity(List<Long> activityIds){
		Criteria c = this.createCriteria(Restrictions.in("id", activityIds));
		c.add(Restrictions.eq("signDisable", true));
		c.setMaxResults(1);
		c.addOrder(Order.desc("createTime"));
		List<ActivityInfo> infos = c.list();
		if(!infos.isEmpty()){
			return infos.get(0);
		}
		return null;
	}
	
	/**
	 * 获取最新资讯
	 * @param activityIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ActivityInfo getLatestNews(List<Long> activityIds){
		Criteria c = this.createCriteria(Restrictions.in("id", activityIds));
		c.add(Restrictions.eq("signDisable", false));
		c.setMaxResults(1);
		c.addOrder(Order.desc("createTime"));
		List<ActivityInfo> infos = c.list();
		if(!infos.isEmpty()){
			return infos.get(0);
		}
		return null;
	}
}
