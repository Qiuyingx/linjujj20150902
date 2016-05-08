package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.HouseDto;


public class HouseResp extends Resp{

	private List<HouseDto> datas = new ArrayList<HouseDto>();

	public List<HouseDto> getDatas() {
		return datas;
	}

	public void setDatas(List<HouseDto> datas) {
		this.datas = datas;
	}
}
