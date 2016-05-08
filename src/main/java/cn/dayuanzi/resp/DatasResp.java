package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author : leihc
 * @date : 2015年6月8日
 * @version : 1.0
 */
public class DatasResp extends Resp {

	private List<Object> datas = new ArrayList<Object>();

	public List<Object> getDatas() {
		return datas;
	}

	public void setDatas(List<Object> datas) {
		this.datas = datas;
	}
	
	private boolean havesk;

	public boolean isHavesk() {
		return havesk;
	}

	public void setHavesk(boolean havesk) {
		this.havesk = havesk;
	}
	
	
}
