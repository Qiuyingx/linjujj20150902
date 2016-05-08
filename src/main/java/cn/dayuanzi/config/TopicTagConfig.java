/**
 * 
 */
package cn.dayuanzi.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.dayuanzi.exception.ConfigException;
import cn.dayuanzi.util.YardUtils;

/** 
 * @ClassName: TopicTagConfig 
 * @Description: TODO
 * @author qiuyingxiang
 * @date 2015年7月27日 下午8:43:48 
 *  
 */

public class TopicTagConfig implements IConfig{


	private static final Map<Integer, TopicTagInfo> TopicTag = new HashMap<Integer, TopicTagInfo>();
	
	/**
	 * @see cn.dayuanzi.config.TopicTagConfig#init()
	 */
	@Override
	public void init() {
		Document docXml;
		try {
			SAXReader reader = new SAXReader();
			docXml = reader.read(YardUtils.getResourcesFile("topicTag.xml"));
			Element root = docXml.getRootElement();
			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				Integer id = Integer.parseInt(element.attributeValue("id"));
				String name = element.attributeValue("name");
				TopicTag.put(id, new TopicTagInfo(id,name));
			}
		} catch (DocumentException ex) {
			ex.printStackTrace();
			throw new ConfigException("TopicTagInfo.xml信息错误！");
		}
	}

	/**
	 * @see cn.dayuanzi.config.IConfig#reload()
	 */
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		this.init();
	}

	public static Map<Integer, TopicTagInfo> getTopicTag() {
		return TopicTag;
	}
	
	

    
}
