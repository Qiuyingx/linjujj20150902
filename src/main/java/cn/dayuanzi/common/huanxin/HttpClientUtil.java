package cn.dayuanzi.common.huanxin;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dayuanzi.model.ImToken;
import cn.dayuanzi.util.JsonUtils;



/**
 * 
 * @author : leihc
 * @date : 2015年4月26日 下午5:45:13
 * @version : 1.0
 */
public class HttpClientUtil {
	
	private Logger logger = LoggerFactory.getLogger("dayuanzi.huanxinLog");
	
	private final static HttpClientUtil instance = new HttpClientUtil();
	
	private HttpClient httpClient;
	
	private ClientConnectionManager connManager;
	
	private long lastEvictTime = System.currentTimeMillis();
	private static final long EVICT_INTERVAL = 5 * 1000;
	
	
	private HttpClientUtil(){
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		params.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		//设置连接最大等待时间
		ConnManagerParams.setTimeout(params, 2000);
		//设置最大连接数
		ConnManagerParams.setMaxTotalConnections(params, 100);
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
		//设置每个路由最大连接数
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
        
		try{
			SSLContext ctx = SSLContext.getInstance("TLS"); 
	        ctx.init(null, new TrustManager[]{new MyTrustManager()},new java.security.SecureRandom());   
	        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx); 
	        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        SchemeRegistry schemeRegistry = new SchemeRegistry();
	        schemeRegistry.register(new Scheme("https", socketFactory, 443));
	        this.connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
	        this.httpClient = new DefaultHttpClient(this.connManager, params);
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static HttpClientUtil getInstance(){
		return instance;
	}

	public ResponseAndStatus sendUrl(String uri, String dataBody, String method, String token){
		evict();
		ResponseAndStatus resp = new ResponseAndStatus();
		try {
			logger.debug("huanxin api:"+uri+" dataBody:"+dataBody+" method:"+method+" token:"+token);
			HttpResponse response = null;
//			dataBody = URLEncoder.encode(dataBody, "utf-8");
			if (method.equals(HTTPMethod.METHOD_POST)) {
				HttpPost httpPost = new HttpPost(uri);
				addHeaders(httpPost, token);
				httpPost.setEntity(new StringEntity(dataBody, "UTF-8"));

				response = httpClient.execute(httpPost);
			} else if (method.equals(HTTPMethod.METHOD_PUT)) {
				HttpPut httpPut = new HttpPut(uri);
				addHeaders(httpPut, token);
				httpPut.setEntity(new StringEntity(dataBody, "UTF-8"));

				response = httpClient.execute(httpPut);
			} else if (method.equals(HTTPMethod.METHOD_GET)) {
				HttpGet httpGet = new HttpGet(uri);
				addHeaders(httpGet, token);
				response = httpClient.execute(httpGet);

			} else if (method.equals(HTTPMethod.METHOD_DELETE)) {
				HttpDelete httpDelete = new HttpDelete(uri);
				addHeaders(httpDelete, token);

				response = httpClient.execute(httpDelete);
			}
			HttpEntity entity = response.getEntity();
			try{
				if (null != entity) {
					String responseContent = EntityUtils.toString(entity, "UTF-8");
					resp.setContent(responseContent);
					resp.setStatusCode(response.getStatusLine().getStatusCode());
				}
			}catch(Exception e){
				resp.setContent(e.getMessage());
				e.printStackTrace();
			}finally{
				entity.consumeContent();
			}
		} catch (Exception e) {
			resp.setContent(e.getMessage());
			e.printStackTrace();
		}
		logger.debug("返回内容： "+resp.getContent());
		return resp;
	}
	
	public ResponseAndStatus sendFile(String uri, String dataBody, String token){
		evict();
		ResponseAndStatus resp = new ResponseAndStatus();
		try {
			HttpResponse response = null;
			HttpPost httpPost = new HttpPost(uri);
//			addHeaders(httpPost, token);
			httpPost.addHeader("restrict-access", "false");
			if(StringUtils.isNotBlank(token)){
				httpPost.addHeader("Authorization", "Bearer "+token);
			}
			httpPost.setEntity(new StringEntity(dataBody, "UTF-8"));

			response = httpClient.execute(httpPost);
			resp.setStatusCode(response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			try{
				if (null != entity) {
					String responseContent = EntityUtils.toString(entity, "UTF-8");
					resp.setContent(responseContent);
				}
			}catch(Exception e){
				resp.setContent(e.getMessage());
				e.printStackTrace();
			}finally{
				entity.consumeContent();
			}
		} catch (Exception e) {
			resp.setContent(e.getMessage());
			e.printStackTrace();
		}
		return resp;
	}
	
	private void addHeaders(HttpRequestBase request, String token){
		request.addHeader("Content-Type", "application/json");
		if(StringUtils.isNotBlank(token)){
			request.addHeader("Authorization", "Bearer "+token);
		}
	}
	
	private synchronized void evict() {
		if (System.currentTimeMillis() - this.lastEvictTime > EVICT_INTERVAL) {
			this.lastEvictTime = System.currentTimeMillis();
			this.connManager.closeExpiredConnections();
			this.connManager.closeIdleConnections(60, TimeUnit.SECONDS);
		}
	}
	
	class MyTrustManager implements X509TrustManager{

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
		
	}
	
	public static void main(String[] args) {
//		service.doSimplePostJson("https://a1.easemob.com/dayuanzi/courtyard/token","{\"grant_type\":\"client_credentials\",\"client_id\":\"YXA6KDsPgOpQEeS_Fvn0gfLxYg\",\"client_secret\":\"YXA6oDBcchIcbIihAYNZDi9l3i0ZH3E\"}", "utf-8");
		ResponseAndStatus resp = HttpClientUtil.getInstance().sendUrl("https://a1.easemob.com/dayuanzi/courtyard/token", "{\"grant_type\":\"client_credentials\",\"client_id\":\"YXA6KDsPgOpQEeS_Fvn0gfLxYg\",\"client_secret\":\"YXA6oDBcchIcbIihAYNZDi9l3i0ZH3E\"}", HTTPMethod.METHOD_POST, null);
		
		ImToken token = JsonUtils.fromJson(resp.getContent(), ImToken.class);
		System.out.println(token.getAccess_token());
		System.out.println(token.getExpires_in());
		System.out.println(token.getApplication());
	}
}
