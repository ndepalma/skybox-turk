package utils;



import java.util.HashMap;
import java.util.Map;

public class MakeSingleton {
	public static Map<Class, Object> allInstances = new HashMap<Class, Object>();
	public static <T> T getInstance(Class<T> classType) {
		if(allInstances.containsKey(classType))
			return classType.cast(allInstances.get(classType));
		try {
			T inst = classType.newInstance();
			allInstances.put(classType, inst);
			return inst;
		} catch(Exception e) {
			return null;
		}
		
	}
}
