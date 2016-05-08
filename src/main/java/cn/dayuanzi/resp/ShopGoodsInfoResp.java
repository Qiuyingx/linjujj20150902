/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.GoodsInfo;


/** 
 * @ClassName: ShopGoodsInfoResp 
 * @Description: 放弃该实现方式。
 * @author qiuyingxiang
 * @date 2015年6月10日 下午9:33:20 
 *  
 */

public class ShopGoodsInfoResp extends Resp{

	private List<GoodsInfo> datas = new ArrayList<GoodsInfo>();

	public List<GoodsInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<GoodsInfo> datas) {
		this.datas = datas;
	}
	
}
