package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.NewNoticeDto;

/**
 * 
 * @author : leihc
 * @date : 2015年5月5日
 * @version : 1.0
 */
public class NewNoticeResp extends Resp {

	private List<NewNoticeDto> datas = new ArrayList<NewNoticeDto>();

	public List<NewNoticeDto> getDatas() {
		return datas;
	}

	public void setDatas(List<NewNoticeDto> datas) {
		this.datas = datas;
	}
	
	
}
