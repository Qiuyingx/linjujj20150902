package cn.dayuanzi.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import cn.dayuanzi.config.SysMessages;
import cn.dayuanzi.exception.GeneralLogicException;
import cn.dayuanzi.pojo.ExternalType;
import cn.dayuanzi.pojo.SessionAttribute;
import cn.dayuanzi.pojo.UserSession;
import cn.dayuanzi.resp.Resp;
import cn.dayuanzi.service.impl.ServiceRegistry;
import cn.dayuanzi.util.JsonUtils;
import cn.dayuanzi.util.LogUtil;

/**
 * 
 * @author : lhc
 * @date : 2015年4月10日 下午3:04:56
 * @version : 1.0
 */
public class LoginFilter implements Filter{

	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		
	}

	/**
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		String externalType = req.getParameter("externalType");
		String courtyardId = req.getParameter("courtyardId");
		String pf = req.getParameter("pf");
		String deviceToken = req.getParameter("token");
		Resp respdata = null;
		try{
			UserSession userSession = (UserSession)session.getAttribute(SessionAttribute.SESSION_USER);
			//if(userSession!=null){
			//	return;
			//}
			int platform = Integer.parseInt(pf);
			if(StringUtils.isBlank(courtyardId)){
				if(StringUtils.isBlank(externalType) || StringUtils.isBlank(pf)){
					throw new GeneralLogicException("参数错误");
				}
				ExternalType external = ExternalType.values()[Integer.parseInt(externalType)];
				if(!external.isOpen()){
					throw new GeneralLogicException("该平台登录还未开放");
				}
				
				if(!pf.equals("1") && !pf.equals("2")){
					throw new GeneralLogicException("参数错误");
				}
				if(external==ExternalType.邻聚){
					String tel = req.getParameter("tel");
					String password = req.getParameter("password");
					if(StringUtils.isBlank(tel)){
						throw new GeneralLogicException(SysMessages.TEL_IS_NOT_BLANK);
					}
					if(StringUtils.isBlank(password)){
						throw new GeneralLogicException(SysMessages.PW_IS_NOT_BLANK);
					}
					respdata = ServiceRegistry.getUserService().login(tel, password, deviceToken,platform);
					userSession = UserSession.get();
					LogUtil.logLogin(userSession.getCourtyardId(), userSession.getUserId(),external, platform, deviceToken);
				}else if(external==ExternalType.QQ||external==ExternalType.微信||external==ExternalType.微博){
					// ^([0-9A-F]{32})$
					String openId = req.getParameter("openId");
					respdata = ServiceRegistry.getUserService().login(external,openId, deviceToken,platform);
					userSession = UserSession.get();
					LogUtil.logLogin(userSession.getCourtyardId(), userSession.getUserId(),external, platform, deviceToken);
				}
			}else{
				userSession = new UserSession(Long.parseLong(courtyardId));
				respdata = new Resp();
			}
			if(userSession!=null)
				session.setAttribute(SessionAttribute.SESSION_USER, userSession);
		}catch(Exception ex){
			respdata = new Resp(Resp.CODE_ERR_LOGIN, ex.getMessage());
			if(ex instanceof GeneralLogicException){
				int errorCode = ((GeneralLogicException)ex).getErrorCode();
				if(errorCode == Resp.CODE_ERR_USER_NOT_REGISTER){
					respdata.setErrorCode(errorCode);
				}
			}
			ex.printStackTrace();
		}
		String json = JsonUtils.toJson(respdata);
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.getWriter().println(json);
		response.flushBuffer();
	}

	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
	
}
