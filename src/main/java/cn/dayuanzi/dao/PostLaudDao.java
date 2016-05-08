package cn.dayuanzi.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.PostLaud;


/**
 * 
 * @author : leihc
 * @date : 2015年4月21日 上午11:44:14
 * @version : 1.0
 */
@Repository
public class PostLaudDao extends BaseDao<Long, PostLaud> {

	@Autowired
	private UserFollowDao userFollowDao;
	
	@SuppressWarnings("unchecked")
	public List<PostLaud> findPostLaud(String orderByProperty, boolean isAsc, final Criterion... criterions){
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
	 * 查询与此帖子相关的人
	 * @param postId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getRelatedUsers(long postId){
		Criteria c = createCriteria(Restrictions.eq("post_id", postId));
		c.setProjection(Projections.property("user_id"));
		return c.list();
	}
	
	/**
	 * 获取相关的帖子
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getRelatedPosts(long userId){
		Criteria c = createCriteria(Restrictions.or(Restrictions.eq("user_id", userId), Restrictions.eq("post_sender_id", userId)));
		c.setProjection(Projections.distinct(Projections.property("post_id")));
		return c.list();
	}
	
	/**
	 * 获取最新的感谢
	 * 
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PostLaud getLatestLaud(long userId){
//		List<Long> postIds = this.getRelatedPosts(userId);
//		if(!postIds.isEmpty()){
			Criteria criteria = null;
			List<Long> follows = this.userFollowDao.findFollowIds(userId);
			if(!follows.isEmpty()){
				List<Long> postIds = this.getRelatedPosts(userId);
				if(postIds.isEmpty()){
					return null;
				}
				criteria = this.createCriteria(Restrictions.in("post_id", postIds),Restrictions.ne("user_id", userId),Restrictions.in("user_id", follows));
			}else{
				criteria = this.createCriteria(Restrictions.eq("post_sender_id", userId),Restrictions.ne("user_id", userId));
			}
			criteria.setMaxResults(1);
			criteria.addOrder(Order.desc("create_time"));
			List<PostLaud> result = criteria.list();
			if(!result.isEmpty()){
				return result.get(0);
			}
//		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getRelatedPostIdAndType(long userId){
		Criteria criteria = this.createCriteria(Restrictions.ne("post_sender_id", userId), Restrictions.eq("user_id", userId));
//		Criterion c = Restrictions.or(Restrictions.eq("post_sender_id", userId), Restrictions.eq("user_id", userId));
//		criteria.add(c);
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("post_id"));
		pl.add(Projections.property("create_time"));
		criteria.setProjection(Projections.distinct(pl));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PostLaud> getLauds(long userId){

		StringBuilder hql = new StringBuilder();
		hql.append("from PostLaud where (");

		hql.append("post_sender_id="+userId);
		List<Long> follows = this.userFollowDao.findFollowIds(userId);
		if(!follows.isEmpty()){
			List<Object[]> postIds = this.getRelatedPostIdAndType(userId);
			if(!postIds.isEmpty()){
				// (post_send_id=1 or (((post_id=1 and create_time>1) or()) and user_id in(1,1)))
				hql.append(" or (");
				int index = 0;
				hql.append("(");
				for(Object[] postId : postIds){
					if(index!=0){
						hql.append(" or ");
					}
					hql.append("(post_id=").append((long)postId[0]);
					hql.append(" and create_time>").append((long)postId[1]).append(")");
					index++;
				}
				hql.append(")");
				index=0;
				hql.append(" and user_id in(");
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
		hql.append(") and user_id <> "+userId+" order by create_time desc");
		Query query = this.getSession().createQuery(hql.toString());
		return query.list();
	}
	
	/**
	 * 删除该帖子的回复
	 * @param postId
	 */
	public void remove(long postId){
		String hql = "delete from PostLaud where post_id = ?";
		this.batchExecute(hql, postId);
	}
	
	
	/**
	 *  查询点击该帖的人
	 */
	@SuppressWarnings("unchecked")
	public List<Long> laudPostUser(long postId){
	    	Criteria c = createCriteria(Restrictions.eq("post_id", postId));
		c.setProjection(Projections.distinct(Projections.property("user_id")));
		return c.list();
	}
	
	/**
	 * 查询点击该帖的人(一定个数)
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Long> laudPostUsertoo(long postId){
	    	Criteria c = createCriteria(Restrictions.eq("post_id", postId));
		c.setProjection(Projections.distinct(Projections.property("user_id")));
		return c.list();
	}
	
}
