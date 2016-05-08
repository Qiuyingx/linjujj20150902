package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.resp.dto.PostDetailDto;

/**
 * 
 * @author : leihc
 * @date : 2015年4月23日 下午7:51:12
 * @version : 1.0
 */
public class UserPostListResp extends Resp {

	/**
	 * 邀约简要信息，获取全部的时候才传
	 */
	//主页不显示两条邀约
	//private List<InvitationInfo> invitationinfos = null;
	/**
	 * 置顶贴
	 */
	private List<PostDetailDto> tops = new ArrayList<PostDetailDto>();
	/**
	 * 帖子列表
	 */
	private List<PostDetailDto> datas = new ArrayList<PostDetailDto>();
	
	public List<PostDetailDto> getDatas() {
		return datas;
	}

	public void setDatas(List<PostDetailDto> datas) {
		this.datas = datas;
	}

	public List<PostDetailDto> getTops() {
		return tops;
	}

	public void setTops(List<PostDetailDto> tops) {
		this.tops = tops;
	}

	//public List<InvitationInfo> getInvitationinfos() {
	//	return invitationinfos;
//	}

	//public void setInvitationinfos(List<InvitationInfo> invitationinfos) {
	//	this.invitationinfos = invitationinfos;
	//}
	
	
}
