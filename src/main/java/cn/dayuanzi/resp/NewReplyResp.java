package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.NewReplyDto;

/**
 * 消息模块中获取新的评论
 * 
 * @author : leihc
 * @date : 2015年4月23日 下午1:28:26
 * @version : 1.0
 */
public class NewReplyResp extends Resp {

	/**
	 * 新评论列表
	 */
	List<NewReplyDto> datas = new ArrayList<NewReplyDto>();

	public List<NewReplyDto> getdatas() {
		return datas;
	}

	public void setNewReplys(List<NewReplyDto> newReplys) {
		this.datas = newReplys;
	}
	
	
	
}
