package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.BuildingsDto;

/**
 * 返回指定院子的楼栋信息
 * 
 * @author : leihc
 * @date : 2015年4月29日
 * @version : 1.0
 */
public class BuildingsResp extends Resp {

	private List<BuildingsDto> datas = new ArrayList<BuildingsDto>();

	public List<BuildingsDto> getDatas() {
		return datas;
	}

	public void setDatas(List<BuildingsDto> datas) {
		this.datas = datas;
	}
	
	
}
