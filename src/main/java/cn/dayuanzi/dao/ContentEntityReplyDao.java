/**
 * 
 */
package cn.dayuanzi.dao;


import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;




import cn.dayuanzi.model.ContentEntityReply;

/** 
 * @ClassName: ContentEntityReplyDao 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月15日 上午10:33:58 
 *  
 */
@Repository
public class ContentEntityReplyDao extends BaseDao<Long, ContentEntityReply>{

    @SuppressWarnings("unchecked")
	public List<ContentEntityReply> findContentEntityReply(String orderByProperty, boolean isAsc, final Criterion... criterions){
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
    
    public long countNotReadReply(long userId, long lastReadedReplyId){
    	return this.count(Restrictions.eq("at_targetId", userId),Restrictions.gt("id", lastReadedReplyId));
    }
    
    @SuppressWarnings("unchecked")
	public ContentEntityReply getLatestReply(long userId){
		Criteria criteria = this.createCriteria(Restrictions.eq("at_targetId", userId));
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("create_time"));
		List<ContentEntityReply> r = criteria.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
}
