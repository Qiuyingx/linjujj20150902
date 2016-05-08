/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.BlackNameListDto;

/** 
 * @ClassName: BlackNameList 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月9日 上午10:21:47 
 *  
 */

public class BlackNameListResp  extends Resp{

	private List<BlackNameListDto> datas = new ArrayList<BlackNameListDto>();

	public List<BlackNameListDto> getDatas() {
		return datas;
	}

	public void setDatas(List<BlackNameListDto> datas) {
		this.datas = datas;
	}
}
