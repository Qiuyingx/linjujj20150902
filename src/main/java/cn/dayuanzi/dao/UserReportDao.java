package cn.dayuanzi.dao;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.dayuanzi.model.UserReport;

/**
 * 
 * @author : leihc
 * @date : 2015年5月21日
 * @version : 1.0
 */
@Repository
public class UserReportDao extends BaseDao<Long, UserReport> {

	/**
	 * 查询该用户是否举报
	 * 
	 */
	
	public boolean haveReport(long postId,int postType,long userId){
		UserReport userReport = this.findUnique(Restrictions.eq("target_id", postId),Restrictions.eq("content_type", postType),Restrictions.eq("user_id",userId));
		return userReport!=null;
	}
}
