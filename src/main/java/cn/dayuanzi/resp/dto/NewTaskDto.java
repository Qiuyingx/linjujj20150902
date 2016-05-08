/**
 * 
 */
package cn.dayuanzi.resp.dto;

/**
 * 
* @ClassName: NewTaskDto 
* @Description: 新手任务 dto
* @author qiuyingxiang
* @date 2015年6月15日 上午9:20:54 
*
 */

public class NewTaskDto {
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
	 * 图标
	 */
	private String image ;
	/**
	 * 类型号
	 */
	private int type;
	/**
	 * 每日任务情况描述
	 */
	private String dec;
	
	public NewTaskDto (){
		
	}
	
	public NewTaskDto(String taskName,int lindou, int exp,String image ,int type){
		this.taskName = taskName;
		this.lindou = lindou;
		this.exp = exp;
		this.image = image;
		this.type = type ;
		
		
	}


	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
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
