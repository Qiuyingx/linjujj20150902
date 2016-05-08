package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.resp.dto.UnitDto;

public class UnitResp extends Resp{

	private List<UnitDto> datas = new ArrayList<UnitDto>();

	public List<UnitDto> getDatas() {
		return datas;
	}

	public void setDatas(List<UnitDto> datas) {
		this.datas = datas;
	}
}
