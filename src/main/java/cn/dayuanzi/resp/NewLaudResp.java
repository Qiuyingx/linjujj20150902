package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.NewLaudDto;

/**
 * 返回新消息提醒中的新的点赞列表
 * 
 * @author : leihc
 * @date : 2015年4月23日 下午2:25:06
 * @version : 1.0
 */
public class NewLaudResp extends Resp {

	private List<NewLaudDto> datas = new ArrayList<NewLaudDto>();

	public List<NewLaudDto> getDatas() {
		return datas;
	}

	public void setDatas(List<NewLaudDto> datas) {
		this.datas = datas;
	}
	
	
}
