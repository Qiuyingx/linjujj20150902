package cn.dayuanzi.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.Notice;
import cn.dayuanzi.pojo.NoticeType;



/**
 * 
 * @author : leihc
 * @date : 2015年4月22日 下午5:53:14
 * @version : 1.0
 */
@Repository
public class NoticeDao extends BaseDao<Long, Notice> {


	/**
	 * 根据通知类型和院子ID查询通知列表
	 */
	public List<Notice> findNotices(int notice_type, long yard_id){
		// 不是系统通知还跟那个院子有关
		if(notice_type == 0){
			return this.find(Restrictions.eq("notice_type", notice_type));
		}else{
			return this.find(Restrictions.eq("notice_type", notice_type),Restrictions.eq("courtyard_id", yard_id));
		}
	}
	
	/**
	 * 未读通知条数
	 * 
	 * @param yard_id
	 * @param last_readed_notice
	 * @return
	 */
	public long countNotReadNotice(long user_id, long yard_id, long last_readed_notice, long register_time){
		// where id>last_readed_notice and (user_id=user_id or (courtyard_id=yard_id and user_id=0) or (user_id=0 and courtyard_id=0))
		Criterion afterRigister = Restrictions.gt("create_time", register_time);
		
		Criterion lastReaded = Restrictions.gt("id", last_readed_notice);
		
		Criterion c_userId = Restrictions.eq("user_id", user_id);
		Criterion sameYard = Restrictions.eq("courtyard_id", yard_id);
		Criterion allUser = Restrictions.eq("user_id", 0l);
		Criterion c_all = Restrictions.and(Restrictions.eq("courtyard_id", 0l),Restrictions.eq("user_id", 0l),afterRigister);
		
		Criterion or = Restrictions.or(c_userId, Restrictions.and(sameYard, allUser,afterRigister), c_all);
		
		return this.count(lastReaded, or);
	}
	/**
	 * 查找通知 
	 * @param orderByProperty
	 * @param isAsc
	 * @param criterions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Notice> findNotice(String orderByProperty, boolean isAsc, final Criterion... criterions){
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
	 * 查找当前院子最新的通知
	 * @param courtyardId
	 * @return
	 */
	public Notice getLatestNotice(long courtyardId, long userId, NoticeType noticeType, long registerTime){
		
		Criterion afterRigister = Restrictions.gt("create_time", registerTime);
		
		Criterion c_userId = Restrictions.eq("user_id", userId);
		Criterion sameYard = Restrictions.eq("courtyard_id", courtyardId);
		Criterion allUser = Restrictions.eq("user_id", 0l);
		Criterion c_all = Restrictions.and(Restrictions.eq("courtyard_id", 0l),Restrictions.eq("user_id", 0l),afterRigister);
		
		Criterion or = Restrictions.or(c_userId, Restrictions.and(sameYard, allUser,afterRigister), c_all);
		
		Criteria criteria = null;
		if(noticeType!=null){
			criteria = this.createCriteria(Restrictions.and(or, Restrictions.eq("notice_type", noticeType.ordinal())));
		}else{
			criteria = this.createCriteria(or);
		}
		ProjectionList list = Projections.projectionList();
		list.add(Projections.max("id"));
		criteria.setProjection(list);
		Long maxUserPostId = (Long)criteria.uniqueResult();
		if(maxUserPostId!=null){
			return this.get(maxUserPostId);
		}
		// id = (select max)
		return null;
	}
	
}
