package cn.general.test;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartException;

/**
 * 
 * @author : leihc
 * @date : 2015年4月15日 下午8:58:24
 * @version : 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application.xml" })
public class HttpTest {

	public void submitPost(){
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost("");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
