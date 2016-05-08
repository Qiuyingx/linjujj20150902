package cn.dayuanzi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.config.InterestConf;
import cn.dayuanzi.dao.UserInterestDao;
import cn.dayuanzi.model.UserInterest;
import cn.dayuanzi.service.IUserInterestService;

/**
 * 
 * @author : leihc
 * @date : 2015年4月16日 下午6:33:42
 * @version : 1.0
 */
@Service
public class UserInterestServiceImpl implements IUserInterestService {

	@Autowired
	private UserInterestDao userInterestDao;
	
	/**
	 * @see cn.dayuanzi.service.IUserInterestService#saveUserInterest(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = false)
	public void saveUserInterest(long userId, String[] interests) {
		for(int i=0; i<interests.length; i++){
			int interestId = Integer.parseInt(interests[i]);
			if(InterestConf.getInterests().containsKey(interestId)){
				this.userInterestDao.saveOrUpdate(new UserInterest(userId,interestId));
			}
		}
	}
	

}
