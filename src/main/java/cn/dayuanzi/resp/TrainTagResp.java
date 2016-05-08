/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.TrainTagInfo;

/** 
 * @ClassName: TrainTagResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年8月18日 上午10:23:20 
 *  
 */

public class TrainTagResp extends Resp {

	private List<TrainTagInfo> datas = new ArrayList<TrainTagInfo>();

	public List<TrainTagInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<TrainTagInfo> datas) {
		this.datas = datas;
	}
}
