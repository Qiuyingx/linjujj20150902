package cn.dayuanzi.service;

import java.util.List;
import java.util.Set;

/**
 * 
 *
 * @author : leihc
 * @date : 2015年5月2日
 * 
 */
public interface IRedisService {

	/**
	 * 保存用户到指定社区的指定兴趣组下
	 * @param courtyardId
	 * @param userId
	 * @param interestId
	 */
	public void saveUserByInterest(long courtyardId, long userId, int interestId);
	/**
	 * 将用户从指定社区的指定兴趣组移除
	 * @param courtyardId
	 * @param userId
	 * @param interestId
	 */
	public void removeUserFromInterest( long courtyardId,long userId, int interestId);
	
	/**
	 * 推荐指定社区相同兴趣的人
	 * @param courtyardId
	 * @param userId
	 * @param interests
	 * @param recommendAmount
	 * @return
	 */
	public Set<Long> getRandomRecommend(long courtyardId, long userId, List<Integer> interests, int recommendAmount);
	
	/**
	 * 手机验证码当天生成次数
	 * @param tel
	 * @return
	 */
	public long addValidateCodeGnerateAmount(String tel);
	
	/**
	 * 
	 * @param userId
	 */
	public void saveLastRange(long userId, long range);
	
	/**
	 * 获取最后一次范围，显示该范围内的帖子
	 * @param userId
	 * @return
	 */
	public long getLastRange(long userId);
}
