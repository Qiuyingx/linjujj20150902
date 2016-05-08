/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.DiscussGroupDto;

/**
 * 返回讨论组的信息
 *
 * @author : leihc
 * @date : 2015年5月2日
 * 
 */
public class DiscussGroupResp extends Resp {

	private List<DiscussGroupDto> datas = new ArrayList<DiscussGroupDto>();

	public List<DiscussGroupDto> getDatas() {
		return datas;
	}

	public void setDatas(List<DiscussGroupDto> datas) {
		this.datas = datas;
	} 
	
	
}
