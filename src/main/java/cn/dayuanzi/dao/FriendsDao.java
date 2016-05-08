package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.Friends;


/**
 * 
 * @author : leihc
 * @date : 2015年4月22日 下午9:11:42
 * @version : 1.0
 */
@Repository
public class FriendsDao extends BaseDao<Long, Friends> {

	/**
	 * 查找好友
	 * 
	 * @param usre_id
	 * @param courtyard_id
	 * @return
	 */
	public List<Friends> findFriends(long user_id){
		Disjunction disjunction =  Restrictions.disjunction();
		disjunction.add(Restrictions.eq("user_id", user_id));
		disjunction.add(Restrictions.eq("friend_id", user_id));
		return this.find(disjunction,Restrictions.eq("accept", 2));
	}
	/**
	 * 查找最后一条好友请求，未处理的
	 * @param user_id
	 * @param courtyard_id
	 */
	@SuppressWarnings("unchecked")
	public Friends getLastestFriendRequest(long user_id, long courtyard_id){
		Criteria c = createCriteria(Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("friend_id", user_id),Restrictions.eq("accept", 1));
		c.setMaxResults(1);
		c.addOrder(Order.desc("request_time"));
		List<Friends> r = c.list();
		if(!r.isEmpty()){
			return r.get(0);
		}
		return null;
	}
	
	/**
	 * 统计未读的好友请求条数
	 * 
	 * @param courtyard_id
	 * @param user_id
	 * @param last_readed_request
	 * @return
	 */
	public long countNotReadFriendRequest(long courtyard_id, long user_id, long last_readed_request){
		return this.count(Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("friend_id", user_id),Restrictions.eq("accept", 1),Restrictions.gt("request_time", last_readed_request));
	}
	/**
	 * 查找未同意好友请求列表
	 * @param user_id
	 * @param courtyard_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Friends> findQuestFriend(long user_id, long courtyard_id){
		Criteria c = createCriteria(Restrictions.eq("courtyard_id", courtyard_id),Restrictions.eq("friend_id", user_id),Restrictions.eq("accept", 1));
		c.addOrder(Order.desc("request_time"));
		return  c.list();
	}
	
	/**
	 * 查找用户与好友用户状态
	 */
	public Friends findFriendsStatus(long userId, long friendId){
		Friends friends = this.findUnique(Restrictions.eq("user_id", userId),Restrictions.eq("friend_id", friendId));
		if(friends!=null){
			return friends;
		}
		return this.findUnique(Restrictions.eq("user_id", friendId),Restrictions.eq("friend_id", userId));
	}
	/**
	 * 删除好友
	 * @param userId
	 * @param friendId
	 */
	public void deleteFriends(long userId ,long friendId){
			String hql = "delete from Friends where user_id = ? and friend_id = ?  and accept = 2";
			this.batchExecute(hql, userId,friendId);
			this.batchExecute(hql, friendId,userId);
		}
	
	
}
