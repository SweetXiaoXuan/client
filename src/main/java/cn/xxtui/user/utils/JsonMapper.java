package cn.xxtui.user.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.util.JSONPObject;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 * 
 * @author mahc
 */
public class JsonMapper {

	private static Logger logger = LoggerFactory.getLogger(JsonMapper.class);

	private static ObjectMapper mapper;
	
	
	public JsonMapper(Inclusion inclusion) {
		mapper = new ObjectMapper();
		//设置输出时包含属性的风格
		mapper.setSerializationInclusion(inclusion); 
		//设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//禁止使用int代表Enum的order()來反序列化Enum,非常危險
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
		
		mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 创建输出全部属性到Json字符串的Mapper.
	 */
	public static JsonMapper buildNormalMapper() {
		return new JsonMapper(Inclusion.ALWAYS);
	}

	/**
	 * 创建只输出非空属性到Json字符串的Mapper.
	 */
	public static JsonMapper buildNonNullMapper() {
		return new JsonMapper(Inclusion.NON_NULL);
	}

	/**
	 * 创建只输出初始值被改变的属性到Json字符串的Mapper.
	 */
	public static JsonMapper buildNonDefaultMapper() {
		return new JsonMapper(Inclusion.NON_DEFAULT);
	}

	/**
	 * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper.
	 */
	public static JsonMapper buildNonEmptyMapper() {
		return new JsonMapper(Inclusion.NON_EMPTY);
	}

	public static JsonMapper buildMapper(Inclusion inclusion){
		return new JsonMapper(inclusion);
	}
	/**
	 * 如果对象为Null, 返回"null".
	 * 如果集合为空集合, 返回"[]".
	 */
	public  String toJson(Object object) {
		try {
			StringWriter sw = new StringWriter();
			JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
			mapper.writeValue(gen, object);
			String json = sw.toString(); 
			return json;
		} catch (IOException e) {
			logger.warn("write to json string error:" + object, e);
			return null;
		}
	}

	/**
	 * 如果JSON字符串为Null或"null"字符串, 返回Null.
	 * 如果JSON字符串为"[]", 返回空集合.
	 * 
	 * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
	 * @see #constructParametricType(Class, Class...)
	 * @param jsonString json字符串
	 * @param clazz	转换的类型
	 * @return
	 */
	public static <T> T readJson(String jsonString, Class<T> clazz) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return mapper.readValue(jsonString, clazz);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 如果JSON字符串为Null或"null"字符串, 返回Null.
	 * 如果JSON字符串为"[]", 返回空集合.
	 * 
	 * 如需读取集合如List/Map, 且不是List<String>这种简单类型时,先使用函數constructParametricType构造类型.
	 * @see #constructParametricType(Class, Class...)
	 */
	@SuppressWarnings("unchecked")
	public <T> T readJson(String jsonString, JavaType javaType) {
		if (StringUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return (T) mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			logger.warn("parse json string error:" + jsonString, e);
			return null;
		}
	}

	/**
	 * 構造泛型的Type如List<MyBean>, 则调用constructParametricType(ArrayList.class,MyBean.class)
	 *             Map<String,MyBean>则调用(HashMap.class,String.class, MyBean.class)
	 */
	public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses) {
		return mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
	}

	/**
	 * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
	 */
	@SuppressWarnings("unchecked")
	public <T> T update(T object, String jsonString) {
		try {
			return (T) mapper.readerForUpdating(object).readValue(jsonString);
		} catch (JsonProcessingException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		} catch (IOException e) {
			logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
		}
		return null;
	}

	/**
	 * 輸出JSONP格式數據.
	 */
	public String toJsonP(String functionName, Object object) {
		return toJson(new JSONPObject(functionName, object));
	}

	/**
	 * 設定是否使用Enum的toString函數來讀寫Enum,
	 * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
	 * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
	 */
	public void setEnumUseToString(boolean value) {
		mapper.configure(SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING, value);
		mapper.configure(DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING, value);
	}

	/**
	 * 取出Mapper做进一步的设置或使用其他序列化API.
	 */
	public ObjectMapper getMapper() {
		return mapper;
	}
	public static  void isNull(){
		if(mapper==null){
			System.out.println("JsonMapper实例为空");
			buildNormalMapper();
		}
	}
	public void println(Object object){
		System.out.println(toJson(object));
	}
	public static void printlns(Object object,Inclusion inclusion){
		
		Print.println(object,buildMapper(inclusion));
	}
	public static void printlns(Object object){
		Print.println(object,buildNormalMapper());
	}
	private static class Print{
		public static void  println(Object object,JsonMapper jsonMapper){
			System.out.println(jsonMapper.toJson(object));
		}
	}

}
