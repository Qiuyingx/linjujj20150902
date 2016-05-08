package cn.dayuanzi.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import cn.dayuanzi.common.huanxin.HTTPMethod;
import cn.dayuanzi.common.huanxin.HttpClientUtil;
import cn.dayuanzi.common.huanxin.ResponseAndStatus;
import cn.dayuanzi.config.HuanXinConf;
import cn.dayuanzi.dao.ImTokenDao;
import cn.dayuanzi.model.Friends;
import cn.dayuanzi.model.ImToken;
import cn.dayuanzi.model.Invitation;
import cn.dayuanzi.service.IHuanXinService;
import cn.dayuanzi.util.JsonUtils;
import cn.dayuanzi.util.RedisKey;

/**
 * 与环信聊天相关接口
 * 
 * @author : leihc
 * @date : 2015年4月26日 上午11:47:33
 * @version : 1.0
 */
@Service
public class HuanXinServiceImpl implements IHuanXinService{

	private Logger logger = LoggerFactory.getLogger("dayuanzi.huanxinLog");
	
	@Autowired
	private ImTokenDao imTokenDao;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#getToken()
	 */
	@Override
	public synchronized ImToken getToken() {
		String value = stringRedisTemplate.opsForValue().get(RedisKey.getKey("HX", "TOKEN"));
		ImToken token = null;
		try{
			if(StringUtils.isNotBlank(value)){
				token = JsonUtils.fromJson(value, ImToken.class);
				if(token!=null && !token.isExpired()){
					return token;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
//		ImToken imToken = this.imTokenDao.getLatestImToken();
//		if(imToken!=null && !imToken.isExpired()){
//			return imToken;
//		}
		String uri = HuanXinConf.getUrl("token");
		JsonObject json = new JsonObject();
		json.addProperty("grant_type", "client_credentials");
		json.addProperty("client_id", HuanXinConf.CLIENT_ID);
		json.addProperty("client_secret", HuanXinConf.CLIENT_SECRET);
		long now = System.currentTimeMillis();
		logger.debug("开始请求token "+uri);
		ResponseAndStatus result = HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_POST, null);
		logger.debug("结束请求token "+(System.currentTimeMillis()-now)+" "+result.getContent());
		try{
			token = JsonUtils.fromJson(result.getContent(), ImToken.class);
			if(token!=null){
				stringRedisTemplate.opsForValue().set(RedisKey.getKey("HX", "TOKEN"), result.getContent());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
//		this.imTokenDao.save(imToken);
		return token;
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#registerUser(cn.dayuanzi.model.User)
	 */
	@Override
	public boolean registerUser(long userId, String password) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("users");
		JsonObject json = new JsonObject();
		json.addProperty("username", userId);
		json.addProperty("password", password);
//		json.addProperty("nickname", nickname);
		ResponseAndStatus result = HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_POST, token);
		return result.getStatusCode() == HttpStatus.SC_OK;
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#addFriend(cn.dayuanzi.model.Friends)
	 */
	@Override
	public boolean addFriend(Friends friends) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("users")+"/"+friends.getUser_id()+"/contacts/users/"+friends.getFriend_id();
		JsonObject json = new JsonObject();
		ResponseAndStatus result = HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_POST, token);
		return result.getStatusCode() == HttpStatus.SC_OK;
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#createDiscussGroup(cn.dayuanzi.model.Invitation)
	 */
	@Override
	public void createDiscussGroup(Invitation invitation,String group_name) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("chatgroups");
		JsonObject json = new JsonObject();
		json.addProperty("groupname", group_name);//群组名称, 此属性为必须的
		json.addProperty("desc", "邀约群组");//群组描述, 此属性为必须的
		json.addProperty("public", false);//是否是公开群, 此属性为必须的,为false时为私有群
		json.addProperty("maxusers", 200);//群组成员最大数(包括群主), 值为数值类型,默认值200,此属性为可选的
		json.addProperty("approval", false);//加入公开群是否需要批准, 默认值是true（加群需要群主批准）, 此属性为可选的,只作用于公开群
		json.addProperty("owner", invitation.getUser_id());//群组的管理员, 此属性为必须的
//		json.addProperty("members", userId);//群组成员,此属性为可选的,但是如果加了此项,数组元素至少一个（注：群主jma1不需要写入到members里面）
		ResponseAndStatus result = HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_POST, token);
		JsonParser parser = new JsonParser();
		JsonObject resultObject = parser.parse(result.getContent()).getAsJsonObject();
		String groupId = resultObject.get("data").getAsJsonObject().get("groupid").getAsString();
		invitation.setGroup_id(groupId);
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#dismissDiscussGroup(cn.dayuanzi.model.Invitation)
	 */
	@Override
	public void dismissDiscussGroup(Invitation invitation) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("chatgroups")+"/"+invitation.getGroup_id();
		JsonObject json = new JsonObject();
		HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_DELETE, token);
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#joinDiscussGroup(cn.dayuanzi.model.User, cn.dayuanzi.model.Invitation)
	 */
	@Override
	public void joinDiscussGroup(long userId, String groupId) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("chatgroups")+"/"+groupId+"/users/"+userId;
		JsonObject json = new JsonObject();
		HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_POST, token);
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#exitDiscussGroup(long, java.lang.String)
	 */
	@Override
	public void exitDiscussGroup(long userId, String groupId) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("chatgroups")+"/"+groupId+"/users/"+userId;
		JsonObject json = new JsonObject();
		HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_DELETE, token);
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#modifypassword(long, java.lang.String)
	 */
	@Override
	public void modifypassword(long userId, String newpw) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("users")+"/"+userId+"/password";
		JsonObject json = new JsonObject();
		json.addProperty("newpassword", newpw);
		HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_PUT, token);
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#modifyNickName(long, java.lang.String)
	 */
	@Override
	public void modifyNickName(long userId, String nickname) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("users")+"/"+userId;
		JsonObject json = new JsonObject();
		json.addProperty("nickname", nickname);
		HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_PUT, token);
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#sendTextToGroup(long, java.lang.String, cn.dayuanzi.model.Invitation)
	 */
	@Override
	public void sendTextToGroup(long userId, String content,
			Invitation invitation) {
		ImToken imToken = this.getToken();
		String token = imToken.getAccess_token();
		String uri = HuanXinConf.getUrl("messages");
		JsonObject json = new JsonObject();
		json.addProperty("target_type", "chatgroups");// 群组聊天
		JsonArray target = new JsonArray();
		target.add(new JsonPrimitive(invitation.getGroup_id()));// 向该群组发送信息
		json.add("target", target);
		JsonObject msg = new JsonObject();
		msg.addProperty("type", "txt");
		msg.addProperty("msg", content);
		json.add("msg", msg);
		json.addProperty("from", String.valueOf(userId));
		HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_POST, token);
	}
	
	/**
	 * @see cn.dayuanzi.service.IHuanXinService#getOfflineMsgCount(long)
	 */
	@Override
	public long getOfflineMsgCount(long userId) {
		long count = 0;
		try{
			ImToken imToken = this.getToken();
			String token = imToken.getAccess_token();
			String uri = HuanXinConf.getUrl("users")+"/"+userId+"/offline_msg_count";
			JsonObject json = new JsonObject();
			ResponseAndStatus result = HttpClientUtil.getInstance().sendUrl(uri, json.toString(), HTTPMethod.METHOD_GET, token);
			JsonParser parser = new JsonParser();
			JsonObject resultObject = parser.parse(result.getContent()).getAsJsonObject();
			count = resultObject.get("data").getAsJsonObject().get(String.valueOf(userId)).getAsLong();
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
}
