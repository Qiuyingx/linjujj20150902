package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.config.TagInfo;


/**
 * 
 * @author qiuyingxiang
 *
 */
public class TagResp extends Resp {

	private List<TagInfo> datas = new ArrayList<TagInfo>();

	public List<TagInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<TagInfo> datas) {
		this.datas = datas;
	}
	
}
