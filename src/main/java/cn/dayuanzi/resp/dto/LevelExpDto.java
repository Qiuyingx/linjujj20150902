package cn.dayuanzi.resp.dto;

import cn.dayuanzi.config.LevelUpConfig;
import cn.dayuanzi.config.LevelUpInfo;
import cn.dayuanzi.model.User;

/**
 * 用户的等级经验数据
 * @author : leihc
 * @date : 2015年6月10日
 * @version : 1.0
 */
public class LevelExpDto {

	/**
	 * 当前等级
	 */
	private int currentLevel;
	/**
	 * 当前经验
	 */
	private int currentExp;
	/**
	 * 升到下一级需要的经验
	 */
	private int nextLevelExp;
	
	public LevelExpDto(User user){
		this.currentLevel = user.getLevel();
		this.currentExp = user.getExp();
		LevelUpInfo info = LevelUpConfig.getDatas().get(user.getLevel()+1);
		if(info!=null){
			this.nextLevelExp = info.getExp();
		}
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	public int getCurrentExp() {
		return currentExp;
	}
	public void setCurrentExp(int currentExp) {
		this.currentExp = currentExp;
	}
	public int getNextLevelExp() {
		return nextLevelExp;
	}
	public void setNextLevelExp(int nextLevelExp) {
		this.nextLevelExp = nextLevelExp;
	}
	
	
}
