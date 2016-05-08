package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.Career;

/**
 * 返回系统中设置的职业
 * 
 * @author : leihc
 * @date : 2015年5月6日
 * @version : 1.0
 */
public class CareersResp extends Resp {

	private List<Career> datas = new ArrayList<Career>();

	public List<Career> getDatas() {
		return datas;
	}

	public void setDatas(List<Career> datas) {
		this.datas = datas;
	}
	
	
}
