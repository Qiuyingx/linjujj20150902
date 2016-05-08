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
 * 
 * @author : leihc
 * @date : 2015年5月12日
 * @version : 1.0
 */
public class TagConfig implements IConfig {

	
	private static final Map<Integer, TagInfo> tags = new HashMap<Integer, TagInfo>();
	
	/**
	 * @see cn.dayuanzi.config.TagConfig#init()
	 */
	@Override
	public void init() {
		Document docXml;
		try {
			SAXReader reader = new SAXReader();
			docXml = reader.read(YardUtils.getResourcesFile("tags.xml"));
			Element root = docXml.getRootElement();
			for (Iterator<?> i = root.elementIterator(); i.hasNext();) {
				Element element = (Element) i.next();
				Integer id = Integer.parseInt(element.attributeValue("id"));
				String name = element.attributeValue("name");
				tags.put(id, new TagInfo(id,name));
			}
		} catch (DocumentException ex) {
			ex.printStackTrace();
			throw new ConfigException("tags.xml信息错误！");
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

	public static Map<Integer, TagInfo> getTags() {
		return tags;
	}
	
	

}
