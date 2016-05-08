package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.ContactsDto;

public class ContactsResp extends Resp{

	private List<ContactsDto> datas = new ArrayList<ContactsDto>();

	public List<ContactsDto> getDatas() {
		return datas;
	}

	public void setDatas(List<ContactsDto> datas) {
		this.datas = datas;
	}
	
	
}
