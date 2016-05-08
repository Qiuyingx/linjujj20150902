package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.view.LaudView;

/**
 * 
 * @author : leihc
 * @date : 2015年8月19日
 * @version : 1.0
 */
@Repository
public class LaudViewDao extends BaseDao<Long, LaudView> {

	@Autowired
	private UserFollowDao userFollowDao;
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getRelatedIdAndType(long userId){
		Criteria criteria = this.createCriteria(Restrictions.ne("beLauder_id", userId), Restrictions.eq("lauder_id", userId));
//		Criterion c = Restrictions.or(Restrictions.eq("beLauder_id", userId), Restrictions.eq("lauder_id", userId));
//		criteria.add(c);
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("content_id"));
		pl.add(Projections.property("create_time"));
		pl.add(Projections.property("laud_type"));
		criteria.setProjection(Projections.distinct(pl));
		
		return criteria.list();
	}
	/**
	 * 
	 * @param userId
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<LaudView> getLauds(long userId,int current_page, int max_num){
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		StringBuilder hql = new StringBuilder();
		hql.append("from LaudView where (");

		hql.append("beLauder_id="+userId);
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		if(!follows.isEmpty()){
			List<Object[]> postIds = this.getRelatedIdAndType(userId);
			if(!postIds.isEmpty()){
				// (post_send_id=1 or (((post_id=1 and create_time>1) or()) and user_id in(1,1)))
				hql.append(" or (");
				int index = 0;
				hql.append("(");
				for(Object[] postId : postIds){
					if(index!=0){
						hql.append(" or ");
					}
					hql.append("(content_id=").append((long)postId[0]);
					hql.append(" and laud_type="+(int)postId[2]);
					hql.append(" and create_time>").append((long)postId[1]).append(")");
					index++;
				}
				hql.append(")");
				index=0;
				hql.append(" and lauder_id in(");
				for(long follow : follows){
					if(index!=0){
						hql.append(",");
					}
					hql.append(follow);
					index++;
				}
				hql.append(")");
				hql.append(")");
			}
		}
		hql.append(") and lauder_id <> "+userId+" order by create_time desc");
		Query query = this.getSession().createQuery(hql.toString());
		query.setFirstResult(((current_page-1)) * max_num);
		query.setMaxResults(max_num);
		return query.list();
	}
	
	public long countNotReadLaud(long userId, long lastReadedLaudTime){
		StringBuilder hql = new StringBuilder();
		hql.append("select count(*) from LaudView where (");

		hql.append("beLauder_id="+userId);
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		if(!follows.isEmpty()){
			List<Object[]> postIds = this.getRelatedIdAndType(userId);
			if(!postIds.isEmpty()){
				// (post_send_id=1 or (((post_id=1 and create_time>1) or()) and user_id in(1,1)))
				hql.append(" or (");
				int index = 0;
				hql.append("(");
				for(Object[] postId : postIds){
					if(index!=0){
						hql.append(" or ");
					}
					hql.append("(content_id=").append((long)postId[0]);
					hql.append(" and laud_type="+(int)postId[2]);
					hql.append(" and create_time>").append((long)postId[1]).append(")");
					index++;
				}
				hql.append(")");
				index=0;
				hql.append(" and lauder_id in(");
				for(long follow : follows){
					if(index!=0){
						hql.append(",");
					}
					hql.append(follow);
					index++;
				}
				hql.append(")");
				hql.append(")");
			}
		}
		hql.append(") and lauder_id <> "+userId+" and create_time>"+lastReadedLaudTime);
		Query query = this.getSession().createQuery(hql.toString());
		return (long)query.uniqueResult();
	}
	
	/**
	 * 获取最后一条感谢
	 * @param userId
	 * @return
	 */
	public LaudView getLatestLaud(long userId){
		List<LaudView> lauds = this.getLauds(userId, 1, 1);
		if(!lauds.isEmpty()){
			return lauds.get(0);
		}
		return null;
	}
}
