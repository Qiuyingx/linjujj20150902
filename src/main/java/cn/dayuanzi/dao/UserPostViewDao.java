package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.config.Settings;
import cn.dayuanzi.model.view.UserPostView;

/**
 * 
 * @author : leihc
 * @date : 2015年8月4日
 * @version : 1.0
 */
@Repository
public class UserPostViewDao extends BaseDao<Long, UserPostView> {

	/**
	 * 获取最新热帖
	 * @param criterions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UserPostView getHotPost(final Criterion... criterions){
		Criteria c = createCriteria(criterions);
		c.add(Restrictions.gt("replys",Settings.HOT_POST_CONDITION));
		c.setMaxResults(1);
		c.addOrder(Order.desc("replys"));
		c.addOrder(Order.desc("create_time"));
		List<UserPostView> r = c.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
}
