package cn.dayuanzi.resp.dto;

import cn.dayuanzi.model.LindouDetail;
import cn.dayuanzi.util.DateTimeUtil;

/**
 * 获取邻豆的详细列表中的项目
 * @author : leihc
 * @date : 2015年6月8日
 * @version : 1.0
 */
public class LindouDetailDto {

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
	
	public LindouDetailDto(LindouDetail detail){
		this.id = detail.getId();
		this.createTime = DateTimeUtil.formatDateTime(detail.getCreate_time());
		this.remark = detail.getRemark();
		this.change = detail.getChangeAmount();
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
}
