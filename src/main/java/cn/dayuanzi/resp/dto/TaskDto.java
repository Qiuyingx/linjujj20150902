package cn.dayuanzi.resp.dto;



/**
 * 
 * @author : leihc
 * @date : 2015年6月10日
 * @version : 1.0
 */
public class TaskDto {

	/**
	 * 任务名
	 */
	private String taskName;
	/**
	 * 可得邻豆数
	 */
	private int lindou;
	/**
	 * 可得经验数
	 */
	private int exp;
	/**
	 * 每日任务当前完成次数
	 */
	private int currentTimes;
	/**
	 * 每日任务每天次数
	 */
	private int limitTimes;

	/**
	 * 新手任务
	 * @param taskName
	 * @param lindou
	 * @param exp
	 */
	public TaskDto(String taskName,int lindou, int exp){
		this.taskName = taskName;
		this.lindou = lindou;
		this.exp = exp;
	}
	/**
	 * 每日任务
	 * @param taskName
	 * @param exp
	 * @param currentTime
	 * @param limitTimes
	 */
	public TaskDto(String taskName, int exp, int currentTime, int limitTimes){
		this.taskName = taskName;
		this.exp = exp;
		this.currentTimes = currentTime;
		this.limitTimes = limitTimes;
	}
	
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getLindou() {
		return lindou;
	}

	public void setLindou(int lindou) {
		this.lindou = lindou;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getCurrentTimes() {
		return currentTimes;
	}

	public void setCurrentTimes(int currentTimes) {
		this.currentTimes = currentTimes;
	}

	public int getLimitTimes() {
		return limitTimes;
	}

	public void setLimitTimes(int limitTimes) {
		this.limitTimes = limitTimes;
	}

}
