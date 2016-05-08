package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.Career;
import cn.dayuanzi.config.Interest;

import cn.dayuanzi.resp.dto.InvitationsDto;
import cn.dayuanzi.resp.dto.PostDetailDto;

/**
 * 用户主页
 * 
 * @author : leihc
 * @date : 2015年4月24日 下午5:03:06
 * @version : 1.0
 */
public class MainPageResp extends Resp {

	/**
	 * 用户ID
	 */
	private long userId;
	/**
	 * 昵称
	 */
	private String nickname;
	/**
	 * 头像地址
	 */
	private String headIcon;
	/**
	 * 背景图片
	 */
	private String  mainPageImage;
	/**
	 * 生日
	 */
	private String birthday;
	/**
	 * 年龄
	 */
	private String age;
	/**
	 * 性别
	 */
	private int gender;
	/**
	 * 院子ID
	 */
	private long courtyardId;
	/**
	 * 社区名称
	 */
	private String courtyardName;
	/**
	 * 是否验证
	 */
	private boolean validate;
	/**
	 * 动态数
	 */
	private int dynamicAmount;
	/**
	 * 职业
	 */
	private Career career;
	
	/**
	 * 兴趣
	 */
	private List<Interest> interests = new ArrayList<Interest>();
	
	/**
	 * 最新内容的类型 0 没有最新内容 1 最新邀约 2 最新帖子
	 */
	private int latestType;
	/**
	 * 最新邀约
	 */
	private InvitationsDto invitationDto;
	/**
	 * 最新帖子
	 */
	private PostDetailDto postDetailDto;
	/**
	 * 当前用户与所见用户的好友关系状态
	 */
//	private  int  accept; 
	/**
	 *好友列表的请求方
	 */
//	private long requestuserid;
	/**
	 * 是否关注
	 */
	private boolean followed;
	/**
	 * 是否已拉黑
	 */
	private boolean backList;
	/**
	 * 回复数
	 */
	private long replyHelpAmount;
	/**
	 * 采纳数
	 */
	private long helpAcceptAmount;
	/**
	 * 话题数
	 */
	private long postAmount;
	/**
	 * 邀约数
	 */
	private long invitationAmount;
	/**
	 * 当前等级
	 */
	private int currentLevel;
	/**
	 * 粉丝数
	 */
	private long followedAmount;
	/**
	 * 是否是官方账号
	 */
	private boolean official;
	/**
	 * 个人签名
	 */
	private String signature;
	/**
	 * 新版本职业
	 */
	private String info;
	/**
	 * 技能是否认证成功
	 */
	private boolean skill;
	/**
	 * 学堂名
	 */
	private  String tarinName;
	/**
	 * 学堂Id
	 */
	private long tarinId;
	
	
	public String getTarinName() {
		return tarinName;
	}

	public void setTarinName(String tarinName) {
		this.tarinName = tarinName;
	}

	public long getTarinId() {
		return tarinId;
	}

	public void setTarinId(long tarinId) {
		this.tarinId = tarinId;
	}


	

	public boolean isSkill() {
		return skill;
	}

	public void setSkill(boolean skill) {
		this.skill = skill;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getMainPageImage() {
	    return mainPageImage;
	}

	public void setMainPageImage(String mainPageImage) {
	    this.mainPageImage = mainPageImage;
	}

	public String getSignature() {
	    return signature;
	}

	public void setSignature(String signature) {
	    this.signature = signature;
	}

	public long getFollowedAmount() {
	  return followedAmount;
	}

	public void setFollowedAmount(long followedAmount) {
	  this.followedAmount = followedAmount;
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

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getCourtyardName() {
		return courtyardName;
	}

	public void setCourtyardName(String courtyardName) {
		this.courtyardName = courtyardName;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public int getDynamicAmount() {
		return dynamicAmount;
	}

	public void setDynamicAmount(int dynamicAmount) {
		this.dynamicAmount = dynamicAmount;
	}
	public Career getCareer() {
		return career;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

	public List<Interest> getInterests() {
		return interests;
	}

	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public long getCourtyardId() {
		return courtyardId;
	}

	public void setCourtyardId(long courtyardId) {
		this.courtyardId = courtyardId;
	}

	public InvitationsDto getInvitationDto() {
		return invitationDto;
	}

	public void setInvitationDto(InvitationsDto invitationDto) {
		this.invitationDto = invitationDto;
	}

	public PostDetailDto getPostDetailDto() {
		return postDetailDto;
	}

	public void setPostDetailDto(PostDetailDto postDetailDto) {
		this.postDetailDto = postDetailDto;
	}

	public int getLatestType() {
		return latestType;
	}

	public void setLatestType(int latestType) {
		this.latestType = latestType;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}


	public long getReplyHelpAmount() {
		return replyHelpAmount;
	}

	public void setReplyHelpAmount(long replyHelpAmount) {
		this.replyHelpAmount = replyHelpAmount;
	}

	public long getPostAmount() {
		return postAmount;
	}

	public void setPostAmount(long postAmount) {
		this.postAmount = postAmount;
	}

	public long getInvitationAmount() {
		return invitationAmount;
	}

	public void setInvitationAmount(long invitationAmount) {
		this.invitationAmount = invitationAmount;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}

	public long getHelpAcceptAmount() {
		return helpAcceptAmount;
	}

	public void setHelpAcceptAmount(long helpAcceptAmount) {
		this.helpAcceptAmount = helpAcceptAmount;
	}

	public boolean isFollowed() {
		return followed;
	}

	public void setFollowed(boolean followed) {
		this.followed = followed;
	}

	public boolean isBackList() {
		return backList;
	}

	public void setBackList(boolean backList) {
		this.backList = backList;
	}

	public boolean isOfficial() {
		return official;
	}

	public void setOfficial(boolean official) {
		this.official = official;
	}

	
}
