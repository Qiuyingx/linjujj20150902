/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.RecommendDto;

/**
 * 返回推荐邻居的信息
 *
 * @author : leihc
 * @date : 2015年5月2日
 * 
 */
public class RecommendResp extends Resp {

	private List<RecommendDto> datas = new ArrayList<RecommendDto>();

	public List<RecommendDto> getDatas() {
		return datas;
	}

	public void setDatas(List<RecommendDto> datas) {
		this.datas = datas;
	}
	
	
}
