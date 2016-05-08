package cn.dayuanzi.service;

import cn.dayuanzi.model.UserTrade;

/**
 * 
 * @author : leihc
 * @date : 2015年5月4日
 * @version : 1.0
 */
public interface IUserTradeService {

	/**
	 * 生成新的订单
	 * 
	 * @param courtyard
	 * @param userId
	 * @return
	 */
	public UserTrade generateOrder(long courtyard,long userId);
}
