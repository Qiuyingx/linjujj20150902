/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.resp.dto.ContentEntityDto;

/** 
 * @ClassName: ContentEntityResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月8日 下午9:11:10 
 *  
 */

public class ContentEntityListResp extends Resp {


	private List<ContentEntityDto> datas = new ArrayList<ContentEntityDto>();

	public List<ContentEntityDto> getDatas() {
		return datas;
	}

	public void setDatas(List<ContentEntityDto> datas) {
		this.datas = datas;
	}
	
	
    
    
}
