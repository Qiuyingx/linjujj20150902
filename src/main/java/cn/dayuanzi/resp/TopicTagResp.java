/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;


import cn.dayuanzi.config.TopicTagInfo;

/** 
 * @ClassName: TopicTagResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月28日 上午9:35:08 
 *  
 */

public class TopicTagResp extends Resp {

    

	private List<TopicTagInfo> datas = new ArrayList<TopicTagInfo>();

	public List<TopicTagInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<TopicTagInfo> datas) {
		this.datas = datas;
	}
    
}
