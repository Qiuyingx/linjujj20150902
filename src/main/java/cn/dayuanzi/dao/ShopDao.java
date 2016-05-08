package cn.dayuanzi.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.Shop;

/**
 * 
 * @author : leihc
 * @date : 2015年6月11日
 * @version : 1.0
 */
@Repository
public class ShopDao extends BaseDao<Long, Shop> {

	
	/**
	 * 按条件分页查询商品，按商品添加时间倒序排列
	 * @param current_page
	 * @param max_num
	 * @param criterions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Shop> findForPage(int current_page, int max_num, Criterion... criterions){
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
		c.addOrder(Order.desc("id"));
		return c.list();
	}
	
	
}
