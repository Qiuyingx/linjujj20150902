package cn.dayuanzi.resp;

import cn.dayuanzi.resp.dto.LevelExpDto;

/**
 * 
 * @author : leihc
 * @date : 2015年6月9日
 * @version : 1.0
 */
public class MainResp extends Resp {

	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像地址
	 */
	private String headIcon;
	
	private LevelExpDto levelExpDto;
	/**
	 * 帮帮数
	 */
	//private int helpAmount;
	/**
	 * 话题数+帮帮数量
	 */
	private int postAmount;
	/**
	 * 邀约数
	 */
	private int invitationAmount;
	/**
	 * 邻豆数
	 */
	private int lindouAmount;
	/**
	 * 粉丝数量
	 */
	private int followAmout;
	/**
	 * 新的关注自己的人数
	 */
	private long newFollowMeAmount;
	/**
	 * 回复数
	 */
	private long replyHelpAmount;
	/**
	 * 申请店铺状态
	 */
	private int pass=-1;
	/**
	 * 是否有店铺
	 */
	private boolean have;
	/**
	 * 技能是否认证成功
	 */
	private long skill;
	/**
	 * 技能是否申请
	 */
	private boolean havesk;
	/**
	 * 我的店的ID
	 */
	private long myShopId;
	
	

	
	public long getSkill() {
		return skill;
	}
	public void setSkill(long skill) {
		this.skill = skill;
	}
	public boolean isHavesk() {
		return havesk;
	}
	public void setHavesk(boolean havesk) {
		this.havesk = havesk;
	}
	
	public int getPass() {
		return pass;
	}
	public void setPass(int pass) {
		this.pass = pass;
	}
	public boolean isHave() {
		return have;
	}
	public void setHave(boolean have) {
		this.have = have;
	}
	public long getReplyHelpAmount() {
	    return replyHelpAmount;
	}
	public void setReplyHelpAmount(long replyHelpAmount) {
	    this.replyHelpAmount = replyHelpAmount;
	}
	public long getNewFollowMeAmount() {
	    return newFollowMeAmount;
	}
	public void setNewFollowMeAmount(long newFollowMeAmount) {
	    this.newFollowMeAmount = newFollowMeAmount;
	}
	public int getFollowAmout() {
	    	return followAmout;
	}
	public void setFollowAmout(int followAmout) {
	    	this.followAmout = followAmout;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadIcon() {
		return headIcon;
	}
	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}
	
	public LevelExpDto getLevelExpDto() {
		return levelExpDto;
	}
	public void setLevelExpDto(LevelExpDto levelExpDto) {
		this.levelExpDto = levelExpDto;
	}
	//public int getHelpAmount() {
	//	return helpAmount;
	//}
	//public void setHelpAmount(int helpAmount) {
	//	this.helpAmount = helpAmount;
	//}
	public int getPostAmount() {
		return postAmount;
	}
	public void setPostAmount(int postAmount) {
		this.postAmount = postAmount;
	}
	public int getInvitationAmount() {
		return invitationAmount;
	}
	public void setInvitationAmount(int invitationAmount) {
		this.invitationAmount = invitationAmount;
	}
	public int getLindouAmount() {
		return lindouAmount;
	}
	public void setLindouAmount(int lindouAmount) {
		this.lindouAmount = lindouAmount;
	}
	public long getMyShopId() {
		return myShopId;
	}
	public void setMyShopId(long myShopId) {
		this.myShopId = myShopId;
	}
	
	
}
