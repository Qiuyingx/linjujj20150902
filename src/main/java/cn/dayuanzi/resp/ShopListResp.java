/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.resp.dto.ShopDto;

/** 
 * @ClassName: ShowListResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月11日 下午1:40:41 
 *  
 */

public class ShopListResp extends Resp{

	private List<ShopDto> datas = new ArrayList<ShopDto>();

	public List<ShopDto> getDatas() {
		return datas;
	}

	public void setDatas(List<ShopDto> datas) {
		this.datas = datas;
	}
}
