package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.HelpPostDto;

/**
 * 
 * @author : leihc
 * @date : 2015年4月23日 下午3:53:36
 * @version : 1.0
 */
public class HelpPostListResp extends Resp {

	private List<HelpPostDto> lists = new ArrayList<HelpPostDto>();

	public List<HelpPostDto> getLists() {
		return lists;
	}

	public void setLists(List<HelpPostDto> lists) {
		this.lists = lists;
	}
	
	
}
