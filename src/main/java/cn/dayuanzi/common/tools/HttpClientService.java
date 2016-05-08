package cn.dayuanzi.common.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientService {
	private final Logger logger = LoggerFactory.getLogger(HttpClientService.class);
	private static final HttpClientService instance = new HttpClientService();
	private HttpClient httpClient;
	private ClientConnectionManager cm;
	private static final long EVICT_INTERVAL = 5 * 1000;
	private long lastEvictTime = System.currentTimeMillis();

	public static HttpClientService getInstance() {
		return instance;
	}

	private HttpClientService() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		params.setParameter(CoreConnectionPNames.TCP_NODELAY, true);
		
		ConnManagerParams.setTimeout(params, 2000);
		ConnManagerParams.setMaxTotalConnections(params, 100);
		ConnPerRouteBean connPerRoute = new ConnPerRouteBean(20);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		this.cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		this.httpClient = new DefaultHttpClient(this.cm, params);
	}

	public HttpClient getHttpClient() {
		evict();
		return this.httpClient;
	}

	public void destroy() {
		this.httpClient.getConnectionManager().shutdown();
	}

	public String doSimpleGet(String url) {
		return doSimpleGet(url, "UTF-8");
	}

	public String doSimplePost(String url) {
		return doSimplePost(url, "UTF-8");
	}

	public String doSimpleGet(String url, String encode) {
		evict();
		long startTime = System.currentTimeMillis();
		String content = "";
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = this.httpClient.execute(get);
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
					get.abort();
				} finally {
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			content = e.getMessage();
			if (this.logger.isDebugEnabled()) {
				long elapse = System.currentTimeMillis() - startTime;
				StringBuilder buffer = new StringBuilder();
				buffer.append(elapse).append(" ").append(url);
				buffer.append(" ").append(content);
				this.logger.debug(buffer.toString());
			}
		}
		return content;
	}

	public String doSimplePost(String uri, String encode) {
		evict();
		long startTime = System.currentTimeMillis();
		String content = "";
		String url = "";
		String paramStr = "";
		String[] paramPair = null;
		String[] valuePair = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
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
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			if (params != null && params.size() > 0) {
				post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			HttpResponse response = this.httpClient.execute(post);
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
				buffer.append(elapse).append(" ").append(uri);
				buffer.append(" ").append(content);
				this.logger.debug(buffer.toString());
			}
		}
		return content;
	}
	
	public String doSimplePostJson(String url, String json, String encode) {
		evict();
		long startTime = System.currentTimeMillis();
		String content = "";
		try {
			
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/json");
			StringEntity reqentity = new StringEntity(json);
			reqentity.setContentType("text/json");
			post.setEntity(reqentity);
			HttpResponse response = this.httpClient.execute(post);
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
		return content;
	}

	public String doAndroidSdkPost(String url, String body, String encode) {
		evict();
		long startTime = System.currentTimeMillis();
		String content = "";
		try {
			StringEntity stringEntity = new StringEntity(body, encode);
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-Type", "application/x-www-form-urlencoded");
			post.setEntity(stringEntity);
			HttpResponse response = this.httpClient.execute(post);
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
		return content;
	}

	private synchronized void evict() {
		if (System.currentTimeMillis() - this.lastEvictTime > EVICT_INTERVAL) {
			this.lastEvictTime = System.currentTimeMillis();
			this.cm.closeExpiredConnections();
			this.cm.closeIdleConnections(60, TimeUnit.SECONDS);
		}
	}

	public static void main(String[] args) {
		HttpClientService service = new HttpClientService();
		String content = service.doSimplePostJson("http://a1.easemob.com/dayuanzi/courtyard/token","{\"grant_type\":\"client_credentials\",\"client_id\":\"YXA6KDsPgOpQEeS_Fvn0gfLxYg\",\"client_secret\":\"YXA6oDBcchIcbIihAYNZDi9l3i0ZH3E\"}", "utf-8");
		System.out.println(content);
	}
}
