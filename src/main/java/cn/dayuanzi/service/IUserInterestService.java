package cn.dayuanzi.service;

/**
 * 
 * @author : leihc
 * @date : 2015年4月16日 下午6:32:07
 * @version : 1.0
 */
public interface IUserInterestService {

	/**
	 * 保存用户兴趣
	 * 
	 * @param userId
	 * @param interests
	 */
	public void saveUserInterest(long userId, String[] interests);
	
	
}
