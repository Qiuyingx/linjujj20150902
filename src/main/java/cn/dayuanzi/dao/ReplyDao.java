package cn.dayuanzi.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.PostReply;
import cn.dayuanzi.model.view.ReplyView;

/**
 * 
 * @author : leihc
 * @date : 2015年7月20日
 * @version : 1.0
 */
@Repository
public class ReplyDao extends BaseDao<Long, ReplyView> {
	
	@Autowired
	private PostReplyDao postReplyDao;
	@Autowired
	private UserFollowDao userFollowDao;

	/**
	 * 获取与该用户有关的回复
	 * 1,专题，活动@的回复
	 * 2,帖子中，用户发的帖或者参与的帖中关注的人的回复
	 * 
	 * @param userId
	 * @param current_page
	 * @param max_num
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ReplyView> getReplys(long userId,int current_page, int max_num){
		if(current_page<=0){
			current_page = 1;
		}
		if(max_num > 20){
			max_num = 20;
		}else if(max_num <= 0){
			max_num = 1;
		}
		StringBuilder hql = new StringBuilder();
		hql.append("from ReplyView where at_targetId=").append(userId);
		hql.append(" or (senderId=").append(userId).append(" and replyerId<>").append(userId).append(")");
		
		// 参与的帖子
		List<Object[]> r = postReplyDao.getRelatedPostIdAndType(userId);
		if(!r.isEmpty()){
			List<Long> follows = this.userFollowDao.findFollowIds(userId);
			if(!follows.isEmpty()){
				int index = 0;
				hql.append(" or ((");
				for(Object[] o : r){
					if(index!=0){
						hql.append(" or ");
					}
					PostReply firstReply = postReplyDao.getFirstReply(userId,(long)o[1],(int)o[0]);
					if(firstReply!=null){
						hql.append("(contentType=").append((int)o[0]);
						hql.append(" and targetId=").append((long)o[1]);
						hql.append(" and createTime>").append(firstReply.getCreate_time()).append(")");
					}
					index++;
				}
				hql.append(") and replyerId in(");
				index=0;
				for(long f : follows){
					if(index!=0){
						hql.append(",");
					}
					hql.append(f);
					index++;
				}
				hql.append(")");
				hql.append(")");
			}
		}
		hql.append(" order by createTime desc");
		Query query = this.getSession().createQuery(hql.toString());
		query.setFirstResult(((current_page-1)) * max_num);
		query.setMaxResults(max_num);
		return query.list();
	}
	
	public long countNotReadReply(long userId, long lastReadedReplyTime){
		StringBuilder hql = new StringBuilder();
		hql.append("select count(*) from ReplyView where (at_targetId=").append(userId);
		hql.append(" or (senderId=").append(userId).append(" and replyerId<>").append(userId).append(")");
		
		// 参与的帖子
		List<Object[]> r = postReplyDao.getRelatedPostIdAndType(userId);
		if(!r.isEmpty()){
			List<Long> follows = this.userFollowDao.findFollowIds(userId);
			if(!follows.isEmpty()){
				int index = 0;
				hql.append(" or ((");
				for(Object[] o : r){
					if(index!=0){
						hql.append(" or ");
					}
					PostReply firstReply = postReplyDao.getFirstReply(userId,(long)o[1],(int)o[0]);
					if(firstReply!=null){
						hql.append("(contentType=").append((int)o[0]);
						hql.append(" and targetId=").append((long)o[1]);
						hql.append(" and createTime>").append(firstReply.getCreate_time()).append(")");
					}
					index++;
				}
				hql.append(") and replyerId in(");
				index=0;
				for(long f : follows){
					if(index!=0){
						hql.append(",");
					}
					hql.append(f);
					index++;
				}
				hql.append(")");
				hql.append(")");
			}
		}
		hql.append(") and createTime>").append(lastReadedReplyTime);
		
		Query query = this.getSession().createQuery(hql.toString());
		return (long)query.uniqueResult();
	}
	/**
	 * 获取最后一条回复
	 * @param userId
	 * @return
	 */
	public ReplyView getLatestReply(long userId){
		List<ReplyView> replys = this.getReplys(userId, 1, 1);
		if(!replys.isEmpty()){
			return replys.get(0);
		}
		return null;
	}
}
