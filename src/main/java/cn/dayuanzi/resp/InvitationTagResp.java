/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.InvitationInfo;


/** 
 * @ClassName: InvitationTagResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月2日 下午11:16:35 
 *  
 */

public class InvitationTagResp extends Resp{


	private List<InvitationInfo> datas = new ArrayList<InvitationInfo>();

	public List<InvitationInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<InvitationInfo> datas) {
		this.datas = datas;
	}
}
