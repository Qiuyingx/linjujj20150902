/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.TrainDto;


/** 
 * @ClassName: TrainListResp 
 * @Description: 培训室列表
 * @author qiuyingxiang
 * @date 2015年8月12日 下午3:10:29 
 *  
 */

public class TrainListResp extends Resp{
	
	private List<TrainDto> datas = new ArrayList<TrainDto>();

	public List<TrainDto> getDatas() {
		return datas;
	}

	public void setDatas(List<TrainDto> datas) {
		this.datas = datas;
	}
	
}
