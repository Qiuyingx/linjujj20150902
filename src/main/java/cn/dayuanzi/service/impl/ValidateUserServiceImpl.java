package cn.dayuanzi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.ValidateUserDao;
import cn.dayuanzi.model.ValidateUser;
import cn.dayuanzi.service.IValidateUserService;

/**
 * 
 * @author : leihc
 * @date : 2015年4月24日 下午6:34:16
 * @version : 1.0
 */
@Service
public class ValidateUserServiceImpl implements IValidateUserService {

	@Autowired
	private ValidateUserDao validateUserDao;
	/**
	 * @see cn.dayuanzi.service.IValidateUserService#findValidateUser(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public ValidateUser findValidateUser(long userId, long houseownerId) {
		return this.validateUserDao.findValidateUser(userId, houseownerId);
	}

	/**
	 * @see cn.dayuanzi.service.IValidateUserService#isValidate(long, long)
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isValidate(long userId, long courtyardId) {
		return validateUserDao.isValidate(userId, courtyardId);
	}
}
