/**
 * 
 */
package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.ActivityDto;

/** 
 * @ClassName: ActivityListResp 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年5月30日 下午5:56:31 
 *  
 */

public class ActivityListResp extends Resp{

	private List<ActivityDto> datas = new ArrayList<ActivityDto>();

	public List<ActivityDto> getDatas() {
		return datas;
	}

	public void setDatas(List<ActivityDto> datas) {
		this.datas = datas;
	}
}
