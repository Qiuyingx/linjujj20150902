package cn.dayuanzi.dao;

import java.util.List;





import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;




import cn.dayuanzi.model.ThankReply;

/**
 * 
 * @author : leihc
 * @date : 2015年6月9日
 * @version : 1.0
 */
@Repository
public class ThankReplyDao extends BaseDao<Long, ThankReply> {

	public void removeAll(List<Long> ids){
		if(!ids.isEmpty()){
			StringBuilder hql = new StringBuilder();
			hql.append("delete from ThankReply where reply_id in (");
			for(int i=0;i<ids.size();i++){
				if(i!=ids.size()-1){
					hql.append(ids.get(i)).append(",");
				}else{
					hql.append(ids.get(i)).append(")");
				}
			}
			
			this.batchExecute(hql.toString());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public  List<ThankReply> findThank(String orderByProperty, boolean isAsc, final Criterion... criterions){
		Criteria c = createCriteria(criterions);
		if(StringUtils.isNotBlank(orderByProperty)){
			if (isAsc) {
				c.addOrder(Order.asc(orderByProperty));
			} else {
				c.addOrder(Order.desc(orderByProperty));
			}
		}
		return c.list();
	}
	/**
	 * 统计未读评论感谢
	 * @param user_id
	 * @param last_think_reply
	 * @return
	 */
	public long countNotReadThink(long user_id,long last_think_reply){
		return this.count(Restrictions.eq("replyer_id", user_id),Restrictions.gt("id", last_think_reply));
	}
	
	/**
	 * 查询最后一条感谢
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ThankReply getLatestThankReply(long userId){
		Criteria criteria = this.createCriteria(Restrictions.eq("replyer_id", userId));
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("create_time"));
		List<ThankReply> r = criteria.list();
		if(r.size()>0){
			return r.get(0);
		}
		return null;
	}
}
