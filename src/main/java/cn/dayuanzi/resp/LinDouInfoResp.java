/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;






import cn.dayuanzi.config.LindouInfo;



/** 
 * @ClassName: LinDouInfoResp 
 * @Description: 领豆规则
 * @author qiuyingxiang
 * @date 2015年6月10日 下午9:14:56 
 *  
 */

public class LinDouInfoResp extends Resp{
	
	private List<LindouInfo> datas = new ArrayList<LindouInfo>();
	private String content;

	public List<LindouInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<LindouInfo> datas) {
		this.datas = datas;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
