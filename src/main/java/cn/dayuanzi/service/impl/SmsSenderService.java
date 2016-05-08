package cn.dayuanzi.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.dayuanzi.common.tools.HttpClientService;
import cn.dayuanzi.config.ExternalInterfaceConfig;
import cn.dayuanzi.config.Settings;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.util.YardUtils;

/**
 * 发送验证码
 * 
 * @author : leihc
 * @date : 2015年4月14日 下午12:11:02
 * @version : 1.0
 */
@Service
public class SmsSenderService {

	private static final Logger logger = LoggerFactory.getLogger(SmsSenderService.class);
	
	public String sendMsg(String tel){
		String code = null;
		try{
			code = YardUtils.generateCode(6);
			logger.info("generate code : "+code);
			String url = this.getUrlForLmobile(tel, code);
			logger.info(url);
			String result = HttpClientService.getInstance().doSimplePost(url);
			logger.info(result);
			if(StringUtils.isNotBlank(result)){
				SAXReader reader = new SAXReader();
				Document document = reader.read(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result.getBytes()), "UTF-8")));
				//Document document = reader.read(new ByteArrayInputStream(result.getBytes()));
				Element root = document.getRootElement();
				this.validateStatusForLmobile(root, result);
			}else{
				throw new GeneralLogicException("短信验证方法错误。"+result);
			}
		}catch(Exception ex){
			code = null;
			ex.printStackTrace();
		}
		return code;
	}
	
	/**
	 * 万客会使用短信接口
	 * @param tel
	 * @param code
	 * @return
	 */
	public String getUrlForSmsapiC123Cn(String tel, String code){
		StringBuilder buffer = new StringBuilder();
		buffer.append(ExternalInterfaceConfig.SMS_URL).append("?");
		buffer.append("action=").append(ExternalInterfaceConfig.ACTION);
		buffer.append("&ac=").append(ExternalInterfaceConfig.AC);
		buffer.append("&authkey=").append(ExternalInterfaceConfig.AUTH_KEY);
		buffer.append("&cgid=").append(ExternalInterfaceConfig.CG_ID);
		buffer.append("&csid=").append(ExternalInterfaceConfig.CS_ID);
		buffer.append("&c=").append(code);
		buffer.append("&m=").append(tel);
		return buffer.toString();
	}
	
	/**
	 * 解析返回结果
	 * 
	 * @param root
	 */
	public void validateStatusForSmsapiC123Cn(Element root, String result) throws GeneralLogicException{
		String actionName = root.attributeValue("name");
		String postResult = root.attributeValue("result");
		if(!"sendOnce".equals(actionName) || !"1".equals(postResult)){
			throw new GeneralLogicException("短信验证方法错误。"+result);
		}
	}
	
	/**
	 * 
	 * @param tel
	 * @param code
	 * @return
	 */
	public String getUrlForLmobile(String tel, String code){
		StringBuilder buffer = new StringBuilder();
		buffer.append("http://cf.lmobile.cn/submitdata/Service.asmx/g_Submit").append("?");
		buffer.append("sname=").append(Settings.SMS_NAME);
		buffer.append("&spwd=").append(Settings.SMS_PW);
		buffer.append("&scorpid=").append(" ");
		buffer.append("&sprdid=").append("1012818");
		buffer.append("&sdst=").append(tel);
		buffer.append("&smsg=").append("您验证码是"+code+"，请在30分钟内完成输入。加入邻聚，发现更多有趣的邻居。【邻聚】");
		return buffer.toString();
	}
	
	public void validateStatusForLmobile(Element root, String result) throws GeneralLogicException{
		String state = root.elementText("State");
		if(!"0".equals(state)){
			throw new GeneralLogicException("短信验证方法错误。"+result);
		}
	}
	
	public static void main(String[] args) {
		SmsSenderService serive = new SmsSenderService();
		String code = serive.sendMsg("13880105993");
		System.out.println("发送短信验证码："+code);
	}
}
