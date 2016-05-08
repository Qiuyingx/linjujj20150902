package cn.dayuanzi.common.tools;

import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dayuanzi.common.huanxin.ResponseAndStatus;
import cn.dayuanzi.model.ImToken;
import cn.dayuanzi.util.JsonUtils;

/**
 * @author leihc
 */
public class HttpsClientService {
	
	private final Logger logger = LoggerFactory.getLogger(HttpsClientService.class);
	private final static HttpsClientService httsClientService = new HttpsClientService();
	private HttpClient httpsClient;
	private ClientConnectionManager connManager;
	private long lastEvictTime = System.currentTimeMillis();
	private static final long EVICT_INTERVAL = 5 * 1000;
	
	public static HttpsClientService getInstance(){
		return httsClientService;
	}
	
	private HttpsClientService(){
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
		//设置连接超时时间  
//        HttpConnectionParams.setConnectionTimeout(params, 10000);  
        //设置读取超时时间  
//        HttpConnectionParams.setSoTimeout(params, 1000);
        
		try{
			SSLContext ctx = SSLContext.getInstance("TLS"); 
	        ctx.init(null, new TrustManager[]{new MyTrustManager()},new java.security.SecureRandom());   
	        SSLSocketFactory socketFactory = new SSLSocketFactory(ctx); 
	        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
	        SchemeRegistry schemeRegistry = new SchemeRegistry();
	        schemeRegistry.register(new Scheme("https", socketFactory, 443));
	        this.connManager = new ThreadSafeClientConnManager(params, schemeRegistry);
	        this.httpsClient = new DefaultHttpClient(this.connManager, params);
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String doPost(String url) {
		return doPost(url, "UTF-8");
	}
	
	public String doPost(String uri, String encode){ 
		evict();
        String responseContent = null;
        try { 
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            String url = "";
    		String paramStr = "";
    		String[] paramPair = null;
    		String[] valuePair = null;
			int index = uri.indexOf("?");
			if (index != -1) {
				url = uri.substring(0, index);
				paramStr = uri.substring(index + 1);
				if (!StringUtils.isBlank(paramStr)) {
					if (paramStr.indexOf("&") == -1) {
						valuePair = paramStr.split("[=]");
						params.add(new BasicNameValuePair(valuePair[0], valuePair[1]));
					} else {
						paramPair = paramStr.split("[&]");
						for (String pp : paramPair) {
							valuePair = pp.split("[=]");
							params.add(new BasicNameValuePair(valuePair[0], valuePair[1]));
						}
					}
				}
			}
            
            HttpPost httpPost = new HttpPost(url); 
            HttpEntity re = new UrlEncodedFormEntity(params);
            httpPost.setEntity(re); 
            HttpResponse response = httpsClient.execute(httpPost); //执行POST请求 
            HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					if (encode == null) {
						String entityCharset = EntityUtils.getContentCharSet(entity);
						if (entityCharset != null)
							encode = entityCharset;
						else
							encode = "UTF-8";
					}
					responseContent = EntityUtils.toString(entity, encode);
				} catch (Exception e) {
					responseContent = e.getMessage();
					httpPost.abort();
				} finally {
					entity.consumeContent();
				}
			}
        } catch(Exception e){
        	responseContent = e.getMessage();
        	e.printStackTrace();
        }
        return responseContent; 
    } 
	
	public ResponseAndStatus doSimplePostJson(String url, String json, String encode) {
		evict();
		long startTime = System.currentTimeMillis();
		ResponseAndStatus resp = new ResponseAndStatus();
		String content = "";
		try {
			
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/json");
			post.setHeader("Authorization", "Bearer YWMt0-cSYuvGEeSgB0_zyEPCcgAAAU4o0vlqe_IuBrmAhdGdJPCuPG6ag6l0PGg");
			json = URLEncoder.encode(json, "utf-8");
			StringEntity reqentity = new StringEntity(json);
			reqentity.setContentType("text/json");
			post.setEntity(reqentity);
			HttpResponse response = this.httpsClient.execute(post);
			resp.setStatusCode(response.getStatusLine().getStatusCode());
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					if (encode == null) {
						String entityCharset = EntityUtils.getContentCharSet(entity);
						if (entityCharset != null)
							encode = entityCharset;
						else
							encode = "UTF-8";
					}
					content = EntityUtils.toString(entity, encode);
				} catch (Exception e) {
					content = e.getMessage();
					e.printStackTrace();
					post.abort();
				} finally {
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			content = e.getMessage();
			if (this.logger.isDebugEnabled()) {
				long elapse = System.currentTimeMillis() - startTime;
				StringBuilder buffer = new StringBuilder();
				buffer.append(elapse).append(" ").append(url);
				buffer.append(" ").append(content);
				this.logger.debug(buffer.toString());
			}
		}
		resp.setContent(content);
		return resp;
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
		HttpsClientService service = new HttpsClientService();
		ResponseAndStatus resp = service.doSimplePostJson("https://a1.easemob.com/dayuanzi/courtyard/token","{\"grant_type\":\"client_credentials\",\"client_id\":\"YXA6KDsPgOpQEeS_Fvn0gfLxYg\",\"client_secret\":\"YXA6oDBcchIcbIihAYNZDi9l3i0ZH3E\"}", "utf-8");
		ImToken token = JsonUtils.fromJson(resp.getContent(), ImToken.class);
		System.out.println(token.getAccess_token());
		System.out.println(token.getExpires_in());
		System.out.println(token.getApplication());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(format.format(token.getExpires_in()));
//		String result = JsonUtils.toJson(new RequestEntity("YXA6KDsPgOpQEeS_Fvn0gfLxYg", "YXA6oDBcchIcbIihAYNZDi9l3i0ZH3E"));
//		System.out.println(result);
	}
}

