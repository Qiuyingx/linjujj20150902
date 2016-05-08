package cn.dayuanzi.service;

import java.sql.Timestamp;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.dayuanzi.model.User;
import cn.dayuanzi.model.UserDaily;
import cn.dayuanzi.model.UserLinDou;
import cn.dayuanzi.model.UserSetting;
import cn.dayuanzi.pojo.ExternalType;
import cn.dayuanzi.resp.BlackNameListResp;
import cn.dayuanzi.resp.DatasResp;
import cn.dayuanzi.resp.LaudListResp;
import cn.dayuanzi.resp.MainPageResp;
import cn.dayuanzi.resp.MainResp;
import cn.dayuanzi.resp.MyHouseResp;
import cn.dayuanzi.resp.PersonalDataResp;
import cn.dayuanzi.resp.RecommendResp;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.resp.TaskResp;
import cn.dayuanzi.resp.VersionCheckeResp;

/**
 * 
 * @author : lhc
 * @date : 2015年4月10日 下午5:34:19
 * @version : 1.0
 */
public interface IUserService {

	/**
	 * 通过昵称查找
	 * @param nickName
	 * @return
	 */
	public User findUserByName(String nickName);
	/**
	 * 通过用户ID查找
	 * 
	 * @param userId
	 * @return
	 */
	public User findUserById(long userId);
	/**
	 * 通过手机号码查找用户
	 * 
	 * @param tel
	 * @return
	 */
	public User findUserByTel(String tel);
	
	/**
	 * 通过外部平台的ID查询用户
	 * @param openId
	 * @param externalType
	 * @return
	 */
	public User findUserByExternalId(String openId, ExternalType externalType);
	
	/**
	 * 保存用户
	 * 
	 * @param user
	 */
	public void saveUser(User user);
	
	/**
	 * 获取用户主页
	 * 
	 * @param userId
	 */
	public MainPageResp getMainPage(long userId);
	
	/**
	 * 获得我的数据
	 * @param userId
	 * @return
	 */
	public MainResp getMain(long userId);
	
	/**
	 * 修改密码
	 * @param userId
	 * @param type 1 修改密码 2 忘记密码通过验证码重置密码
	 * @param currentpw type=1 为当前密码 type=2 验证码
	 * @param newpw
	 */
	public void modifypw(long userId, int type, String currentpw, String newpw);
	
	/**
	 * 获取推荐邻居，根据兴趣匹配
	 * @param userId
	 * @param courtyardId
	 * @return
	 */
	public RecommendResp getRecommends(long userId, long courtyardId, int current_page, int max_num);
	
	/**
	 * 通过邻居昵称，模糊搜索邻居
	 * @param userId
	 * @param couryardId
	 * @param nickname
	 * @return
	 */
	public RecommendResp searchNeighbor(long userId, long couryardId, String nickname,int type);
	
	/**
	 * 登陆
	 * 
	 * @param userId
	 */
	public PersonalDataResp login(String tel, String password, String token, int platform);
	
	/**
	 * 第三方登陆
	 * @param externalType
	 * @param openId
	 * @param token
	 * @return
	 */
	public PersonalDataResp login(ExternalType externalType, String openId,  String token, int platform);
	
	/**
	 * 查询我的房屋信息
	 * 
	 * @param userId
	 * @param courtyardId
	 * @return
	 */
	public MyHouseResp getMyHouse(long userId);
	
	/**
	 * 完善资料
	 * 
	 * @param userId
	 * @param headIcons
	 * @param nickName
	 * @param gender
	 * @param birthday
	 * @param courtyardId
	 * @param careerId
	 * @param interests
	 */
	public PersonalDataResp addInfo(long userId,CommonsMultipartFile[] headIcons,String nickName,Integer gender,Timestamp birthday,int careerId,String interests,String signature,String skill);
	
	/**
	 * 修改资料
	 * @param userId
	 * @param headIcons
	 * @param nickName
	 * @param gender
	 * @param birthday
	 * @param courtyardId
	 * @param careerId
	 * @param interests
	 */
	
	public PersonalDataResp modify(long userId,CommonsMultipartFile[] headIcons,String nickName,Integer gender,Timestamp birthday,Long courtyardId,int careerId,String interests,String signature,String skill);
	/**
	 * 获取个人资料
	 * @param userId
	 * @return
	 */
	public PersonalDataResp getPersonalData(long userId);
	/**
	 * 用户反馈信息
	 * @param userId
	 * @param courtyardId
	 * @param content
	 */
	public void setUserFeedback(long userId,Long courtyardId,String content,CommonsMultipartFile[] images);
	/**
	 * 用户认证报错
	 * @param userId
	 * @param neme
	 * @param code
	 * @param address
	 * @param tel
	 * @param content
	 */
	public void setUserError(long userId, String neme,String code,String address,String tel,String  content);
	/**
	 * 选择社区
	 * @param courtyardId
	 * @param userId
	 */
	public void selectCommunity(long courtyardId,long userId);
	/**
	 * 检查版本
	 */
	public VersionCheckeResp versionChecking();
	/**
	 * 用户推送设置
	 */
	public void userSetting(boolean reply ,boolean laud,boolean message,boolean system);
	/**
	 * 手机号密码注册
	 * @param tel
	 * @param password
	 * @param platform 注册时的平台
	 * @param token 注册时的设备token
	 * @return
	 */
	public User register(String tel, String password, int platform, String token, Long inviteCode);
	
	/**
	 * 外部平台注册
	 * @param tel
	 * @param externalType
	 * @param openId
	 * @param platform
	 * @param token
	 * @return
	 */
	public User register(int externalType ,String openId, int platform, String token, Long inviteCode);
	
	/**
	 * 获取用户系统推送设置
	 * @param userId
	 */
	public UserSetting getUserSetting(long userId);
	
	/**
	 * 获取用户的邻豆
	 * @param userId
	 * @return
	 */
	public UserLinDou getUserLinDou(long userId);
	
	/**
	 * 获取用户每日数据
	 * 
	 * @param userId
	 * @return
	 */
	public UserDaily getUserDaily(long userId);
	
	/**
	 * 每日签到
	 * @param userId
	 */
	public void checkDaily(long userId);
	
	/**
	 * 添加邻豆
	 * @param userId
	 * @param from
	 */
	public void addLindou(long userId, int amount, String from);
	
	/**
	 * 获取指定用户邻豆的变化列表详情
	 * @param userId
	 * @return
	 */
	public DatasResp getLindouDetail(long userId,int current_page,int max_num);
	/**
	 * 获取指定用户经验的变化详情
	 */
	public DatasResp  getExpDetail(long userId,int current_page,int max_num);
	/**
	 * 感谢
	 * @param userId
	 * @param courtyardId
	 * @param replyId
	 */
	public void thankReply(long userId, long courtyardId, long replyId);
	
	/**
	 * 查询我的任务
	 * 
	 * @param userId
	 * @return
	 */
	public TaskResp getMyTask(long userId);
	/**
	 * 拉黑用户
	 * @param userId
	 * @param targetId
	 */
	public void putUserBlackList(long userId, long targetId,boolean concel);
	
	/**
	 * 关注某人
	 * 
	 * @param userId
	 * @param targetId
	 */
	public void followUser(long userId, long targetId,boolean concel);
	/**
	 * 
	 * 黑名单
	 */
	public BlackNameListResp BlackNameList(int current_page, int max_num);
	/**
	 * 分享邀请码
	 * @param userId
	 * @return
	 */
	public Resp shareUser(long userId);

	/**
	 * 申请达人
	 * @param userId
	 * @param tel
	 * @param tagName
	 * @param images
	 */
	public void applyStar(long userId, String tel, String skillTag,String content,CommonsMultipartFile[] images);
	
	/**
	 * 达人列表
	 * @param userId
	 * @return
	 */
	public DatasResp findUserStars(long userId,Integer current_page, Integer max_num);
	/**
	 * 查找本小区用户和关注的人
	 */
	public LaudListResp findUserfollow(int current_page, int max_num,long userId,long courtyardId);
	/**
	 * 主页背景
	 */
	public void setMainPageImage(CommonsMultipartFile[] image,long userId);
	/**
	 * 手机号验证
	 */
	public void validateTel(String tel,long userId);
}
