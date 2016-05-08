package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.Interest;

/**
 * 
 * @author : leihc
 * @date : 2015年5月6日
 * @version : 1.0
 */
public class InterestsResp extends Resp {

	private List<Interest> datas = new ArrayList<Interest>();

	public List<Interest> getDatas() {
		return datas;
	}

	public void setDatas(List<Interest> datas) {
		this.datas = datas;
	}
	
}
