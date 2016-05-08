package cn.dayuanzi.web.filter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.dayuanzi.pojo.UserSession;

/**
 * 
 * @author : lhc
 * @date : 2015年4月9日 上午11:28:39
 * @version : 1.0
 */
public class ShareInterceptor extends HandlerInterceptorAdapter{
	/**
	 * 忽略的请求
	 */
	private List<String> ignore = new ArrayList<String>();
	
	/**
	 * @return the ignore
	 */
	public List<String> getIgnore() {
		return ignore;
	}

	/**
	 * @param ignore the ignore to set
	 */
	public void setIgnore(List<String> ignore) {
		this.ignore = ignore;
	}
	
	/**
	 * 判断请求是否在忽略设置中
	 * 
	 * @param request
	 * @return
	 */
	private boolean isIgnore(HttpServletRequest request) {
		String target = request.getRequestURI();
		for (String url : this.ignore) {
			if (target.endsWith(url)) {
				return true;
			}
		}
		return false;
	}

	/** (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if(isIgnore(request)){
			return super.preHandle(request, response, handler);
		}
		StringBuffer url = request.getRequestURL();  
		String host = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
		HttpSession session = request.getSession();
		session.setAttribute("host", host);
		return super.preHandle(request, response, handler);
	}
	
	/** (non-Javadoc)
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserSession.clear();
		super.afterCompletion(request, response, handler, ex);
	}
}
