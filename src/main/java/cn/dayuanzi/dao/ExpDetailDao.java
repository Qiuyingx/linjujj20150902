/**
 * 
 */
package cn.dayuanzi.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.ExpDetail;


/** 
 * @ClassName: ExpDetailDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月13日 下午4:20:47 
 *  
 */
@Repository
public class ExpDetailDao extends BaseDao<Long, ExpDetail>{

	
	
	/**
	 * 查找指定用户经验详细获取列表
	 * @param userId
	 * @return
	 */
	public List<ExpDetail> findDetails(long userId,int current_page,int max_num){
		Map<String, Object> conditions = new HashMap<String, Object>();
		conditions.put("user_id", userId);
		return this.findForPage(current_page, max_num, conditions, "create_time", false);
		//Criteria c = this.createCriteria(Restrictions.eq(, userId));
		//c.addOrder(Order.desc("create_time"));
		//return c.list();
	}
}
