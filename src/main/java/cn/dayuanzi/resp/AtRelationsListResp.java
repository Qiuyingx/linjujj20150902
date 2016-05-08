/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.AtRelationsListDto;



/** 
 * @ClassName: AtRelationsListResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年8月10日 上午10:26:44 
 *  
 */

public class AtRelationsListResp extends Resp{

    	private List<AtRelationsListDto> datas = new ArrayList<AtRelationsListDto>();

	public List<AtRelationsListDto> getDatas() {
		return datas;
	}

	public void setDatas(List<AtRelationsListDto> datas) {
		this.datas = datas;
	}
    
}
