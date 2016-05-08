package cn.dayuanzi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.dayuanzi.dao.TelValidateDao;
import cn.dayuanzi.model.TelValidate;
import cn.dayuanzi.service.ITelValidateService;

/**
 * 
 * @author : leihc
 * @date : 2015年4月15日 上午11:57:37
 * @version : 1.0
 */
@Service
public class TelValidateServiceImpl implements ITelValidateService {

	@Autowired
	private TelValidateDao telValidateDao;
	
	/**
	 * @see cn.dayuanzi.service.ITelValidateService#findTelValidate(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public TelValidate findTelValidate(String tel) {
		return this.telValidateDao.findUniqueBy("tel", tel);
	}
	
	/**
	 * @see cn.dayuanzi.service.ITelValidateService#saveTelValidate(cn.dayuanzi.model.TelValidate)
	 */
	@Override
	@Transactional(readOnly = true)
	public void saveTelValidate(TelValidate telValidate) {
		this.telValidateDao.saveOrUpdate(telValidate);
	}

}
