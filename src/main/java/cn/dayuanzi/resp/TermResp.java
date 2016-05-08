package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.resp.dto.TermDto;



/**
 * 返回指定城市的社区信息，名称和ID
 * @author qiuyingxiang
 *
 */
public class TermResp extends Resp {

	private List<TermDto> datas = new ArrayList<TermDto>();

	public List<TermDto> getDatas() {
		return datas;
	}

	public void setDatas(List<TermDto> datas) {
		this.datas = datas;
	}
}
