package cn.dayuanzi.config;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

/**
 * 帖子模板配置
 * 
 * @author : leihc
 * @date : 2015年8月3日
 * @version : 1.0
 */
public class PostTemplateConfig implements IConfig {

	private static List<String> templates = new ArrayList<String>();
	/**
	 * 
	 */
	private static List<String> noInterests = new ArrayList<String>();
	
	
	/**
	 * @see cn.dayuanzi.config.IConfig#init()
	 */
	@Override
	public void init() {
		templates.add("{0}的邻居们，大家好！我叫{1}，很高兴认识大家！");
		templates.add("大家好~我叫{1}，住在{0}，很高兴认识大家！");
		templates.add("邻居们好，我叫{1}，住在{0}，第一次来大家多多照顾啦！");
		templates.add("第一次使用邻聚，我是住在{0}的{1}，以后大家多多联系~");
		templates.add("{0}的邻居们，我叫{1}，第一次加入邻聚，很高兴认识大家。");
		templates.add("第一次发帖，我是来自{0}的{1}，很高兴认识大家。");
		templates.add("{0}的邻居，在的举个手啦，我叫{1}，第一次加入，大家多多照顾。");
		templates.add("邻聚的邻居们大家好，我是{0}的 {1}，很高兴在邻聚和大家相聚。");
		{
			noInterests.add("{0}的邻居们，大家好！我叫{1}，很高兴认识大家！");
			noInterests.add("大家好~我叫{1}，住在{0}，很高兴认识大家！");
			noInterests.add("邻居们好，我叫{1}，住在{0}，第一次来大家多多照顾啦！");
			noInterests.add("第一次使用邻聚，我是住在{0}的{1}，以后大家多多联系~");
			noInterests.add("{0}的邻居们，我叫{1}，第一次加入邻聚，很高兴认识大家。");
			noInterests.add("第一次发帖，我是来自{0}的{1}，很高兴认识大家。");
			noInterests.add("{0}的邻居，在的举个手啦，我叫{1}，第一次加入，大家多多照顾。");
			noInterests.add("邻聚的邻居们大家好，我是{0}的 {1}，很高兴在邻聚和大家相聚。");
		}
		
		templates.add("我叫{1}，爱好{2}的邻居欢迎随时联系交流。");
		templates.add("爱{2}的邻居欢迎大家随时联系我，我叫{1}住在{0}。");
		templates.add("喜欢{2}的邻居，我是{0}的{1}，欢迎大家联系我。");
		templates.add("有没有喜欢{2}的邻居，我在{0}，我叫{1}，希望有机会和大家一起交流。");
		templates.add("{0}的邻居，{1}来啦，很高兴和大家认识，喜欢{2}的请联系我。");
	}

	/**
	 * @see cn.dayuanzi.config.IConfig#reload()
	 */
	@Override
	public void reload() {
		this.init();
	}
	
	public static String getContent(String nickname, String counrtyardName, List<Integer> interests){
		if(CollectionUtils.isEmpty(interests)){
			return MessageFormat.format(noInterests.get(RandomUtils.nextInt(noInterests.size())), counrtyardName,nickname);
		}else{
			String interest = "";
			for(Integer interestId : interests){
				if(InterestConf.getInterests().get(interestId)!=null){
					interest = interest +InterestConf.getInterests().get(interestId).getInterest()+"/";
				}
			}
			if(StringUtils.isNotBlank(interest)){
				return MessageFormat.format(templates.get(RandomUtils.nextInt(templates.size())), counrtyardName,nickname,interest);
			}else{
				return MessageFormat.format(noInterests.get(RandomUtils.nextInt(noInterests.size())), counrtyardName,nickname);
			}
		}
	}

}
