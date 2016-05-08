package cn.dayuanzi.resp;


import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.resp.dto.EveryTaskDto;
import cn.dayuanzi.resp.dto.NewTaskDto;


/**
 * 
 * @author : leihc
 * @date : 2015年6月10日
 * @version : 1.0
 */
public class TaskResp extends Resp {

	private List<NewTaskDto> newUserTasks = new ArrayList<NewTaskDto>();
	
	private List<EveryTaskDto> dailyTasks = new ArrayList<EveryTaskDto>();
	/**
	 * 资料完善
	 */
	private NewTaskDto ziliao;
	/**
	 * 社区认证
	 */
	private NewTaskDto shequ;
	/**
	 * 介绍自己
	 */
	//private NewTaskDto jieshao;
	/**
	 * 邀请邻居
	 */
	private NewTaskDto yaoqing;
	/**
	 * 发话题
	 */
	private EveryTaskDto fahuati;
	/**
	 * 回帖
	 */
	private EveryTaskDto huitie;
	/**
	 * 被感谢
	 */
	private EveryTaskDto bieganxie;
	/**
	 * 采纳
	 */
	private EveryTaskDto caina;
	
	
	public NewTaskDto getZiliao() {
		return ziliao;
	}

	public void setZiliao(NewTaskDto ziliao) {
		this.ziliao = ziliao;
	}

	public NewTaskDto getShequ() {
		return shequ;
	}

	public void setShequ(NewTaskDto shequ) {
		this.shequ = shequ;
	}

	//public NewTaskDto getJieshao() {
	//	return jieshao;
	//}

	//public void setJieshao(NewTaskDto jieshao) {
	//	this.jieshao = jieshao;
	//}

	public NewTaskDto getYaoqing() {
		return yaoqing;
	}

	public void setYaoqing(NewTaskDto yaoqing) {
		this.yaoqing = yaoqing;
	}

	public EveryTaskDto getFahuati() {
		return fahuati;
	}

	public void setFahuati(EveryTaskDto fahuati) {
		this.fahuati = fahuati;
	}

	public EveryTaskDto getHuitie() {
		return huitie;
	}

	public void setHuitie(EveryTaskDto huitie) {
		this.huitie = huitie;
	}

	public EveryTaskDto getBieganxie() {
		return bieganxie;
	}

	public void setBieganxie(EveryTaskDto bieganxie) {
		this.bieganxie = bieganxie;
	}

	public EveryTaskDto getCaina() {
		return caina;
	}

	public void setCaina(EveryTaskDto caina) {
		this.caina = caina;
	}

	public List<NewTaskDto> getNewUserTasks() {
		return newUserTasks;
	}

	public void setNewUserTasks(List<NewTaskDto> newUserTasks) {
		this.newUserTasks = newUserTasks;
	}

	public List<EveryTaskDto> getDailyTasks() {
		return dailyTasks;
	}

	public void setDailyTasks(List<EveryTaskDto> dailyTasks) {
		this.dailyTasks = dailyTasks;
	}

	
	
}
