package cn.dayuanzi.pojo;

import java.nio.charset.Charset;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 
 * @author : leihc
 * @date : 2015年7月8日
 * @version : 1.0
 */
public class LongRedisSerializer implements RedisSerializer<Long> {

	private final Charset charset;
	
	public LongRedisSerializer(){
		this(Charset.forName("UTF8"));
	}
	
	public LongRedisSerializer(Charset charset){
		this.charset = charset;
	}
	/**
	 * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
	 */
	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		return (bytes == null ? null : Long.parseLong(new String(bytes, charset)));
	}
	
	/**
	 * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
	 */
	@Override
	public byte[] serialize(Long t) throws SerializationException {
		return (t == null ? null : t.toString().getBytes(charset));
	}
}
