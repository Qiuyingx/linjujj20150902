package cn.dayuanzi.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.pojo.ContentType;

/**
 * 
 * @author : leihc
 * @date : 2015年4月21日 上午11:45:27
 * @version : 1.0
 */
@Repository
public class PostReplyDao extends BaseDao<Long, PostReply> {

	/**
	 * 获取回复指定评论的用户ID
	 * @param replyId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getReplyer(long replyId){
		Criteria criteria = this.createCriteria(Restrictions.eq("reply_id", replyId));
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("replyer_id"));
		criteria.setProjection(list);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PostReply> findPostReplys(String orderByProperty, boolean isAsc, final Criterion... criterions){
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
	 * 移除相应帖子的评论
	 * @param postId
	 * @param contentType
	 */
	@SuppressWarnings("unchecked")
	public List<Long> removeReply(long postId, int contentType){
		Criteria criteria = this.createCriteria(Restrictions.eq("post_id", postId),Restrictions.eq("content_type", contentType));
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("id"));
		criteria.setProjection(list);
		List<Long> ids = criteria.list();
		String removeReplyHql = "delete from PostReply where post_id = ? and content_type = ?";
		this.batchExecute(removeReplyHql, postId, contentType);
		return ids;
	}
	
	/**
	 * 查询与用户相关的帖子ID
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getRelatedPostIds(long userId){
		Criteria criteria = this.createCriteria();
		Criterion c = Restrictions.or(Restrictions.eq("post_sender_id", userId), Restrictions.eq("replyer_id", userId));
		criteria.add(c);
		
		criteria.setProjection(Projections.distinct(Projections.property("post_id")));
		
		return criteria.list();
	}
	
	/**
	 * 查询指定帖子的回复人列表
	 * @param postId
	 * @param contentType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> getReplyers(long postId, int contentType){
		Criteria criteria = this.createCriteria(Restrictions.eq("post_id", postId),Restrictions.eq("content_type", contentType));
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("replyer_id"));
		criteria.setProjection(list);
		return criteria.list();
	}
	
	/**
	 * 查询最后一条与用户有关的评论
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PostReply getLatestReply(long userId){
//		List<Long> postIds = getRelatedPostIds(userId);
		
		Criterion c = Restrictions.or(Restrictions.eq("post_sender_id", userId), Restrictions.eq("replyer_id", userId));
		Criteria cInvitation = this.createCriteria();
		cInvitation.add(c);
		cInvitation.add(Restrictions.eq("content_type", ContentType.邀约.getValue()));
		cInvitation.setProjection(Projections.distinct(Projections.property("post_id")));
		List<Long> invitationIds = cInvitation.list();
		Criteria cPost = this.createCriteria();
		cPost.add(c);
		cPost.add(Restrictions.ne("content_type", ContentType.邀约.getValue()));
		cPost.setProjection(Projections.distinct(Projections.property("post_id")));
		List<Long> postIds = cPost.list();
		if(!postIds.isEmpty() || !invitationIds.isEmpty()){
			Criteria criteria = this.createCriteria();
			Disjunction d = Restrictions.or();
			if(!postIds.isEmpty()){
				Conjunction conjunction = Restrictions.and();
				conjunction.add(Restrictions.ne("content_type", ContentType.邀约.getValue()));
				conjunction.add(Restrictions.in("post_id", postIds));
				conjunction.add(Restrictions.ne("replyer_id", userId));
				d.add(conjunction);
			}
			if(!invitationIds.isEmpty()){
				Conjunction conjunction = Restrictions.and();
				conjunction.add(Restrictions.eq("content_type", ContentType.邀约.getValue()));
				conjunction.add(Restrictions.in("post_id", invitationIds));
				conjunction.add(Restrictions.ne("replyer_id", userId));
				d.add(conjunction);
			}
			criteria.add(d);
			criteria.setMaxResults(1);
			criteria.addOrder(Order.desc("create_time"));
			List<PostReply> result = criteria.list();
			if(!result.isEmpty()){
				return result.get(0);
			}
		}
		
//		if(!postIds.isEmpty()){
//			Criteria criteria = this.createCriteria(Restrictions.in("post_id", postIds),Restrictions.ne("replyer_id", userId));
//			criteria.setMaxResults(1);
//			criteria.addOrder(Order.desc("create_time"));
//			List<PostReply> result = criteria.list();
//			if(!result.isEmpty()){
//				return result.get(0);
//			}
//		}
		return null;
	}
	
	/**
	 * 查询用户在该帖子下的第一条回复
	 * @param userId
	 * @param postId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PostReply getFirstReply(long userId, long postId,int contentType){
		Criteria c = this.createCriteria(Restrictions.eq("post_id", postId),Restrictions.eq("content_type", contentType),Restrictions.eq("replyer_id", userId));
		c.setMaxResults(1);
		c.addOrder(Order.asc("create_time"));
		List<PostReply> result = c.list();
		if(!result.isEmpty()){
			return result.get(0);
		}
		return null;
	}
	
	/**
	 * 投影字段 content_type,post_id,post_sender_id
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> getRelatedPostIdAndType(long userId){
		Criteria criteria = this.createCriteria(Restrictions.ne("post_sender_id", userId), Restrictions.eq("replyer_id", userId));
//		Criterion c = Restrictions.or(Restrictions.eq("post_sender_id", userId), Restrictions.eq("replyer_id", userId));
//		criteria.add(c);
		
		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("content_type"));
		pl.add(Projections.property("post_id"));
//		pl.add(Projections.property("post_sender_id"));
		criteria.setProjection(Projections.distinct(pl));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<PostReply> getReplys(long userId){
		List<Object[]> r = this.getRelatedPostIdAndType(userId);
		if(r.isEmpty()){
			return null;
		}
		// 查询条件，帖子类型，帖子ID，回复者userId第一个回复的时间
		List<Object[]> conditions = new ArrayList<Object[]>();
		for(Object[] o : r){
			// 如果是发帖者，第一个回复时间设置为0
			if((Long)o[2]==userId){
				conditions.add(new Object[]{o[0],o[1],0l});
			}else{
				PostReply firstReply = getFirstReply(userId,(long)o[1],(int)o[0]);
				if(firstReply!=null){
					conditions.add(new Object[]{o[0],o[1],firstReply.getCreate_time()});
				}
			}
		}
		if(conditions.isEmpty()){
			return null;
		}
		StringBuilder hql = new StringBuilder();
		hql.append("from PostReply where (");
		int index = 0;
		for(Object[] o : conditions){
			if(index!=0){
				hql.append(" or ");
			}
			hql.append("(content_type=").append((int)o[0]);
			hql.append(" and post_id=").append((long)o[1]);
			hql.append(" and create_time>").append((long)o[2]).append(")");
			index++;
		}
		hql.append(") and replyer_id <> "+userId+" order by create_time desc");
		Query query = this.getSession().createQuery(hql.toString());
		return query.list();
	}
}
