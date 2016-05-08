/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.LevelUpInfo;


/** 
 * @ClassName: LevelUpInfoResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年6月13日 下午2:35:57 
 *  
 */

public class LevelUpInfoResp  extends Resp {

	private List<LevelUpInfo> datas = new ArrayList<LevelUpInfo>();
	private String content;

	public List<LevelUpInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<LevelUpInfo> datas) {
		this.datas = datas;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
