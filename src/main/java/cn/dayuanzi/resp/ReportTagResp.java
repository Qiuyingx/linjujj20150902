package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.ReportInfo;
/**
 * 
* @ClassName: ReportTagResp 
* @Description: 举报标签返回
* @author qiuyingxiang
* @date 2015年5月25日 上午12:15:09 
*
 */

public class ReportTagResp extends Resp {

	private List<ReportInfo> datas = new ArrayList<ReportInfo>();

	public List<ReportInfo> getDatas() {
		return datas;
	}

	public void setDatas(List<ReportInfo> datas) {
		this.datas = datas;
	}
}
