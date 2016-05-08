/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.resp.dto.LaudListDto;

/** 
 * @ClassName: LaudListResp 
 * @Description: 点赞列表
 * @author qiuyingxiang
 * @date 2015年7月27日 下午7:23:43 
 *  
 */

public class LaudListResp extends Resp{

    private List<LaudListDto> datas = new ArrayList<LaudListDto>();

	public List<LaudListDto> getDatas() {
		return datas;
	}

	public void setDatas(List<LaudListDto> datas) {
		this.datas = datas;
	}
}
