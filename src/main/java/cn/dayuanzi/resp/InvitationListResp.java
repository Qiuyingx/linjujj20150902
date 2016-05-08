package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.InvitationDto;

/**
 * 
 * @author : leihc
 * @date : 2015年4月20日 下午4:09:26
 * @version : 1.0
 */
public class InvitationListResp extends Resp {

	private List<InvitationDto> datas = new ArrayList<InvitationDto>();
	
	public InvitationListResp(){
		
	}

	/**
	 * @return the datas
	 */
	public List<InvitationDto> getDatas() {
		return datas;
	}

	/**
	 * @param datas the datas to set
	 */
	public void setDatas(List<InvitationDto> datas) {
		this.datas = datas;
	}
	
	
}
