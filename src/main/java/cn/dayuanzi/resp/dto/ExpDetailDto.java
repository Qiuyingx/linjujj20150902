/**
 * 
 */
package cn.dayuanzi.resp.dto;

import cn.dayuanzi.model.ExpDetail;
import cn.dayuanzi.util.DateTimeUtil;

/** 
 * @ClassName: ExpDetailDto 
 * @Description:获取用户经验值详情
 * @author qiuyingxiang
 * @date 2015年6月13日 下午4:25:53 
 *  
 */

public class ExpDetailDto {

	private long id;
	/**
	 * 产生时间
	 */
	private String createTime;
	/**
	 * 操作场景
	 */
	private String remark;
	/**
	 * 变化数量
	 */
	private int change;
	
	public ExpDetailDto (){
		
	}
	
	public ExpDetailDto(ExpDetail detail){
		this.id = detail.getId();
		this.createTime = DateTimeUtil.formatDateTime(detail.getCreate_time());
		this.remark = detail.getRemark();
		this.change = detail.getChangeAmount();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getChange() {
		return change;
	}

	public void setChange(int change) {
		this.change = change;
	}
}
