package cn.dayuanzi.service;

import cn.dayuanzi.model.TelValidate;

/**
 * 
 * @author : leihc
 * @date : 2015年4月15日 上午11:56:30
 * @version : 1.0
 */
public interface ITelValidateService {

	/**
	 * 查找手机验证信息
	 * 
	 * @param tel
	 */
	public TelValidate findTelValidate(String tel);
	
	/**
	 * @param telValidate
	 */
	public void saveTelValidate(TelValidate telValidate);
}
