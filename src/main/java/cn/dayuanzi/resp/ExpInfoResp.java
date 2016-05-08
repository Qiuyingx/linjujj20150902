/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.ExpInfo;


/** 
 * @ClassName: ExpInfoResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月10日 下午9:03:04 
 *  
 */

public class ExpInfoResp extends Resp{

	
	private List<ExpInfo> datas = new ArrayList<ExpInfo>();
	private String content;

	public List<ExpInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<ExpInfo> datas) {
		this.datas = datas;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
