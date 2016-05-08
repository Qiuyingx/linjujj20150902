package cn.dayuanzi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.CourtYardDao;
import cn.dayuanzi.dao.PropertyCompanyDao;
import cn.dayuanzi.dao.UserTradeDao;
import cn.dayuanzi.model.CourtYard;
import cn.dayuanzi.model.PropertyCompany;
import cn.dayuanzi.model.UserTrade;
import cn.dayuanzi.service.IUserTradeService;

/**
 * 
 * @author : leihc
 * @date : 2015年5月4日
 * @version : 1.0
 */
@Service
public class UserTradeServiceImpl implements IUserTradeService {

	@Autowired
	private PropertyCompanyDao propertyCompanyDao;
	@Autowired
	private UserTradeDao userTradeDao;
	@Autowired
	private CourtYardDao courtYardDao;
	
	/**
	 * @see cn.dayuanzi.service.IUserTradeService#generateOrder(long, long)
	 */
	@Override
	@Transactional(readOnly = false)
	public UserTrade generateOrder(long courtyardId, long userId) {
		CourtYard courtyard = this.courtYardDao.get(courtyardId);
		PropertyCompany company = this.propertyCompanyDao.get(courtyard.getCompany_id());
		UserTrade trade = new UserTrade(userId,courtyardId,company.getAlipay_acount());
		this.userTradeDao.save(trade);
		return null;
	}

}
