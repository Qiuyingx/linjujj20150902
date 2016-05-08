package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.UserPost;

/**
 * 
 * @author : leihc
 * @date : 2015年4月16日 下午9:09:53
 * @version : 1.0
 */
@Repository
public class UserPostDao extends BaseDao<Long, UserPost> {
	
	/**
	 * 查询最新帖子
	 * 
	 * @param userId
	 * @param courtyardId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public UserPost getLatestUserPost(long userId, long courtyardId){
		Criteria criteria = this.createCriteria(Restrictions.eq("user_id", userId));
//		ProjectionList list = Projections.projectionList();
//		list.add(Projections.max("id"));
//		criteria.setProjection(list);
		criteria.setMaxResults(1);
		criteria.addOrder(Order.desc("create_time"));
		List<UserPost> r = criteria.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
//		Long maxUserPostId = (Long)criteria.uniqueResult();
//		if(maxUserPostId!=null){
//			return this.get(maxUserPostId);
//		}
		// id = (select max)
		return null;
	}
	/**
	 * 删除用户帖子
	 * @param user_id
	 * @param postId
	 * @param postType
	 */
//	public void removePost(long user_id,long postId ,int postType){
//		if(postType==2||postType==3){
//			String hql = "delete from UserPost where user_id = ? and id = ? and content_type = ?";
//			this.batchExecute(hql, user_id,postId,postType);
//		}else{
//			String hql = "delete from Invitation where user_id = ? and id = ?";
//			this.batchExecute(hql, user_id,postId);
//		}
//	}
	/**
	 * 查询是否已经自我介绍
	 */
	public boolean findziwojieshao(long userId){
		return this.findUnique(Restrictions.eq("user_id",userId),Restrictions.eq("myself",true),Restrictions.eq("content_type",3))==null;
	}
	
	
}
