package org.normal.function;

import java.sql.Timestamp;
import java.util.Date;

import org.normal.function.DateTransformer;
import org.normal.function.StringUtil;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JSON {
	public static String Encode(Object obj) {
		if(obj == null || obj.toString().equals("null")) return null;
		if(obj != null && obj.getClass() == String.class){
			return obj.toString();
		}
		JSONSerializer serializer = new JSONSerializer();
		serializer.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Date.class);
		serializer.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss"), Timestamp.class);
		return serializer.deepSerialize(obj);
	}
	public static Object Decode(String json) {
		if (StringUtil.isNullOrEmpty(json)) return "";
		JSONDeserializer deserializer = new JSONDeserializer();
		deserializer.use(String.class, new DateTransformer("yyyy-MM-dd HH:mm:ss"));		
		Object obj = deserializer.deserialize(json);
		if(obj != null && obj.getClass() == String.class){
			return Decode(obj.toString());
		}		
		return obj;
	}

}
