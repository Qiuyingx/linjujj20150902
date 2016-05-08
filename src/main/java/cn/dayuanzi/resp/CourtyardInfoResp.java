package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.CourtyardDto;

/**
 * 返回指定城市的社区信息，名称和ID
 * @author : leihc
 * @date : 2015年4月29日
 * @version : 1.0
 */
public class CourtyardInfoResp extends Resp {

	private List<CourtyardDto> datas = new ArrayList<CourtyardDto>();

	public List<CourtyardDto> getDatas() {
		return datas;
	}

	public void setDatas(List<CourtyardDto> datas) {
		this.datas = datas;
	}
	
}
