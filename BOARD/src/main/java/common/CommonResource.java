package common;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

public class CommonResource {
	//뷰경로
	public static final String viewPath = "/WEB-INF/views/";
		
	//HttpServletRequest의 파라미터들을 자바객체의 멤버변수들에 연결(이름으로 매핑)
	public static Object dataBind(Map<String, String[]> reqMap, Class<?> type) throws Exception {
		//생성자 찾기
		Constructor<?>[] con = type.getDeclaredConstructors(); 
		Object instance = null;
			
		if (con.length > 1) { //객채를 사용자 정의 생성자로 생성, 파라미터에 바인딩
			//파라미터 수가 많은 생성자에 우선순위
			Arrays.sort(con, (a2, a1) -> a1.getParameterCount() > a2.getParameterCount() ? 1 : 
										 a1.getParameterCount() < a2.getParameterCount() ? -1 : 0);
		
			Parameter[] paramArr = con[0].getParameters(); //생성자의 파라미터배열
			Object[] objArr = new Object[paramArr.length]; //생성자 호출시 넘길 오브젝트 배열
			for (int i = 0; i < paramArr.length; i++) {			
				String paramName = paramArr[i].getName();
				Class<?> paramType = paramArr[i].getType();
				Object paramObj = null; //오브젝트배열에 담길 객체
				if ((paramObj = reqMap.get(paramName)) != null) {
					if (paramType.getAnnotation(BindTarget.class) != null) //
						paramObj = dataBind(reqMap, paramType);
					else if (isConvertable(paramType))
						paramObj = castType((reqMap.get(paramName))[0], paramType); 						
				}
				objArr[i] = paramObj;
			}
			instance = con[0].newInstance(objArr);
	
		} else {
			//객체를 기본생성자로 생성, Setter메소드로 멤버변수에 바인딩
			instance = type.getDeclaredConstructor().newInstance(new Object[0]);
			
			Field[] fieldArr = type.getDeclaredFields();
			
			for (Field field : fieldArr) {
				String fieldName = field.getName();
				Class<?> fieldType = field.getType();
				Object fieldValue = null;
				
				if (reqMap.get(fieldName) != null) {
					if (fieldType.getAnnotation(BindTarget.class) != null)
						fieldValue = dataBind(reqMap, fieldType);
					else if (isConvertable(fieldType)) 
						fieldValue = castType((reqMap.get(fieldName))[0], fieldType);
					else
						continue;
				
					String fieldSetterName = "set" + 
											 fieldName.substring(0, 1).toUpperCase() +
											 fieldName.substring(1);
					try {
					  type.getDeclaredMethod(fieldSetterName, fieldType).invoke(instance, fieldValue);

					}catch(Exception e){
						
					}
				}
			}	
		}	
		return instance;
	}
	
	private static boolean isConvertable(Class<?> type){
		return  type == int.class 
				||type ==	double.class
				||type == char.class 
				||type == boolean.class
				||type == String.class
				||type == java.util.Date.class
				||type == java.sql.Date.class;				
	}
	
	public static Object castType(String value, Class<?> type) throws ParseException {
		if (value == null || value.trim().length() == 0)  //웹에선 다 문자열로 넘어와서..
			return value;
		if (type == int.class) {
			return Integer.valueOf(value);
		}else if (type == double.class) {
			return Double.valueOf(value);
		}else if (type == char.class) {
			return Character.valueOf(value.charAt(0));
		}else if ( type == boolean.class) {
			return Boolean.valueOf(value);
		}else if (type == java.util.Date.class) {
			return new SimpleDateFormat("yyyy-MM-dd").parse(value);
		}else if (type == java.sql.Date.class) {
			return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(value).getTime());
		}
		return value;
	}
	
	public static boolean isEmptyString(String arg) {
		return arg == null || arg.trim().length() == 0; 
	}
	
	public static void closeAutoCloseableResource(AutoCloseable...args) {
		for (AutoCloseable arg : args)
			if (arg != null)
				try {arg.close();} catch (Exception e) {} 
	}
	
	public static String toLowerFirstIdx(String s) {
		char[] arr = s.toCharArray();
		if (arr[0] >= 65 && arr[0] <= 90)
			arr[0] =  (char) (arr[0] + 32);
		return String.valueOf(arr);
	}
	
}
