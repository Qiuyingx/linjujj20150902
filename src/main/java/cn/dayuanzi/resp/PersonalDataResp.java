package cn.dayuanzi.resp;

import java.util.ArrayList;
import java.util.List;

import cn.dayuanzi.config.Career;
import cn.dayuanzi.config.Interest;


/**
 * 个人资料
 * 
 * @author : leihc
 * @date : 2015年5月7日
 * @version : 1.0
 */
public class PersonalDataResp extends Resp {

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
	 * 生日 yyyy-MM-dd
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
	 * 社区是否合作
	 */
	private  int ally;
	/**
	 * 社区名称
	 */
	private String courtyardName;
	/**
	 * 是否验证
	 */
	private boolean validate;
	/**
	 * 验证状态，0未验证 1已通过验证
	 */
	private int validateStatus;
	/**
	 * 职业
	 */
	private Career career;
	
	/**
	 * 兴趣
	 */
	private List<Interest> interests = new ArrayList<Interest>();
	/**
	 * 登陆返回手机号
	 */
	private String tel;
	
	/**
	 * 用户评论提醒
	 */
	private boolean reply ;
	/**
	 * 用户点赞提醒
	 */
	private boolean laud ;
	/**
	 * 用户私信提醒
	 */
	private boolean message;
	/**
	 * 用户系统提醒
	 */
	private  boolean system ;
	/**
	 * 邻豆
	 */
	private long lingdou;
	/**
	 * 签到天数
	 */
	private int day;
	/**
	 * 是否签到
	 */
	private boolean check;
	/**
	 * 签到获得的经验
	 */
	private int gainExp;
	/**
	 * 最后一次查看帖子选择的范围，单位公里
	 */
	private long lastRange;
	/**
	 * 签名
	 */
	private String signature;
	/**
	 * 新版本职业
	 */
	private String info;
	
	
	

	

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSignature() {
	    return signature;
	}

	public void setSignature(String signature) {
	    this.signature = signature;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public boolean isReply() {
		return reply;
	}

	public void setReply(boolean reply) {
		this.reply = reply;
	}

	public boolean isLaud() {
		return laud;
	}

	public void setLaud(boolean laud) {
		this.laud = laud;
	}

	public boolean isMessage() {
		return message;
	}

	public void setMessage(boolean message) {
		this.message = message;
	}

	public boolean isSystem() {
		return system;
	}

	public void setSystem(boolean system) {
		this.system = system;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
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
	public Career getCareer() {
		return career;
	}

	public void setCareer(Career career) {
		this.career = career;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public long getLingdou() {
		return lingdou;
	}

	public void setLingdou(long lingdou) {
		this.lingdou = lingdou;
	}

	public List<Interest> getInterests() {
		return interests;
	}

	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}

	public long getCourtyardId() {
		return courtyardId;
	}

	public void setCourtyardId(long courtyardId) {
		this.courtyardId = courtyardId;
	}
	public int getAlly() {
		return ally;
	}

	public void setAlly(int ally) {
		this.ally = ally;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public int getGainExp() {
		return gainExp;
	}

	public void setGainExp(int gainExp) {
		this.gainExp = gainExp;
	}

	public long getLastRange() {
		return lastRange;
	}

	public void setLastRange(long lastRange) {
		this.lastRange = lastRange;
	}

	public int getValidateStatus() {
		return validateStatus;
	}

	public void setValidateStatus(int validateStatus) {
		this.validateStatus = validateStatus;
	}
	
}
