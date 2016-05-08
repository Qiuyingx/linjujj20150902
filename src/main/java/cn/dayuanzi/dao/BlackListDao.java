package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.BlackList;

/**
 * 
 * @author : leihc
 * @date : 2015年7月2日
 * @version : 1.0
 */
@Repository
public class BlackListDao extends BaseDao<Long, BlackList> {

	/**
	 * 取消拉黑
	 * @param userId
	 * @param targetId
	 */
	public void delete(long userId,long targetId){
		String hql = "delete from BlackList where user_id = ? and target_id = ?";
		this.batchExecute(hql, userId, targetId);
	}
	
	/**
	 * 获取拉黑的用户名单
	 * 
	 * @param user_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Long> findBlackIds(long user_id){
		Criteria c = this.createCriteria(Restrictions.eq("user_id", user_id));
		ProjectionList list = Projections.projectionList();
		list.add(Projections.property("target_id"));
		c.setProjection(list);
		return c.list();
	}
	/**
	 * 判断是否在黑名单中
	 * 
	 * @param userId
	 * @param targetId
	 * @return
	 */
	public boolean isBlackList(long userId, long targetId){
		return this.findUnique(Restrictions.eq("user_id", userId), Restrictions.eq("target_id", targetId))!=null;
	}
}
