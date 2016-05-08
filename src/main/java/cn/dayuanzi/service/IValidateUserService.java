package cn.dayuanzi.service;

import cn.dayuanzi.model.ValidateUser;

/**
 * 
 * @author : leihc
 * @date : 2015年4月24日 下午6:32:53
 * @version : 1.0
 */
public interface IValidateUserService {

	/**
	 * 查找验证信息
	 * 
	 * @param userId
	 * @param houseownerId
	 * @return
	 */
	public ValidateUser findValidateUser(long userId, long houseownerId);
	
	/**
	 * 判断用户在该社区是否已验证
	 * @param userId
	 * @param courtyardId
	 * @return
	 */
	public boolean isValidate(long userId, long courtyardId);
}
