package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.InvitationDto;

/**
 * 
 * @author : leihc
 * @date : 2015年4月29日
 * @version : 1.0
 */
public class NewInvitationResp extends Resp {

	private List<InvitationDto> datas = new ArrayList<InvitationDto>();

	public List<InvitationDto> getDatas() {
		return datas;
	}

	public void setDatas(List<InvitationDto> datas) {
		this.datas = datas;
	}
	
	
	
}
