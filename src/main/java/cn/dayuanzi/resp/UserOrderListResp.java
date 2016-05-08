/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.resp.dto.UserOrderDto;


/** 
 * @ClassName: UserOrderListResp 
 * @Description: 我的订单
 * @author qiuyingxiang
 * @date 2015年6月11日 上午9:44:09 
 *  
 */

public class UserOrderListResp extends Resp{

	private List<UserOrderDto> datas = new ArrayList<UserOrderDto>();

	public List<UserOrderDto> getDatas() {
		return datas;
	}

	public void setDatas(List<UserOrderDto> datas) {
		this.datas = datas;
	}
	
	
}
