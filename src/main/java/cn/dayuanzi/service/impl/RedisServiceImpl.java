package cn.dayuanzi.service.impl;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import cn.dayuanzi.service.IRedisService;
import cn.dayuanzi.util.RedisKey;

/**
 * 
 *
 * @author : leihc
 * @date : 2015年5月2日
 * 
 */
@Service
public class RedisServiceImpl implements IRedisService {

	@Autowired
	private RedisTemplate<String, Long> redisTemplate;
	
	/**
	 * @see cn.dayuanzi.service.IRedisService#saveUserByInterest(long, int)
	 */
	@Override
	public void saveUserByInterest(long courtyardId, long userId, int interestId) {
		// key = COURTYARD#INTEREST#2
		String key = RedisKey.getKey(courtyardId+"#"+RedisKey.family_interest, String.valueOf(interestId));
		try{
			redisTemplate.opsForSet().add(key, userId);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	
	/**
	 * @see cn.dayuanzi.service.IRedisService#removeUserFromInterest(long, int)
	 */
	@Override
	public void removeUserFromInterest(long courtyardId, long userId, int interestId) {
		try{
			String key = RedisKey.getKey(courtyardId+"#"+RedisKey.family_interest, String.valueOf(interestId));
			redisTemplate.opsForSet().remove(key, userId);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @see cn.dayuanzi.service.IRedisService#getRandomRecommend(long, java.util.List)
	 */
	@Override
	public Set<Long> getRandomRecommend(long courtyardId, long userId, List<Integer> interests, int recommendAmount) {
		if(recommendAmount < 6 || recommendAmount >= 30){
			recommendAmount = 6;
		}
		Set<Long> result = new HashSet<Long>();
		for(int i=0;i<recommendAmount;i++){
			Integer interestId = interests.get(RandomUtils.nextInt(interests.size()));
			String key = RedisKey.getKey(courtyardId+"#"+RedisKey.family_interest, String.valueOf(interestId));
			int tryCount = 15;
			while(tryCount>0){
				try{
					long target = this.redisTemplate.opsForSet().randomMember(key);
					if(target!=userId){
						result.add(target);
						break;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				tryCount--;
			}
		}
		// 去掉自己
		result.remove(userId);
		return result;
	}
	
	
	/**
	 * @see cn.dayuanzi.service.IRedisService#addValidateCodeGnerateAmount(java.lang.String)
	 */
	@Override
	public long addValidateCodeGnerateAmount(String tel) {
		Calendar calendar = Calendar.getInstance();
		final String key = RedisKey.getKey(RedisKey.tel, tel+"#"+calendar.get(Calendar.DAY_OF_YEAR));
		long count = 0;
		try {
			count = redisTemplate.opsForValue().increment(key, 1);
			// 设置过期时间
			if(redisTemplate.getExpire(key) == -1){
				calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				long diff = (calendar.getTimeInMillis()-System.currentTimeMillis())/1000l+60;
				redisTemplate.expire(key, diff, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	/**
	 * @see cn.dayuanzi.service.IRedisService#saveLastRange(long, long)
	 */
	@Override
	public void saveLastRange(long userId, long range) {
		if(userId<=0||range<=0){
			return;
		}
		try {
			String key = RedisKey.getKey(RedisKey.last_range, String.valueOf(userId));
			redisTemplate.opsForValue().set(key, range);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * @see cn.dayuanzi.service.IRedisService#getLastRange(long)
	 */
	@Override
	public long getLastRange(long userId) {
		Long range = 0l;
		try {
			String key = RedisKey.getKey(RedisKey.last_range, String.valueOf(userId));
			range = redisTemplate.opsForValue().get(key);
			if(range==null){
				range = 0l;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return range;
	}
}
