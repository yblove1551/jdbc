package common;

import static common.CommonResource.toLowerFirstIdx;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;


public class AppContext {
	public Map<String, Object> map;
	private ServletContext context; //서블릿 컨텍스트랑 관계 맺어주기
	
	//빈을 담는 맵
	public AppContext(ServletContext context){
		map = new HashMap<>();
		
		this.context = context;
		context.setAttribute("appContext", this);
		
		//나중에 설정파일로 빼기.. 
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			DataSource dataSource = (DataSource) envContext.lookup("jdbc/oracle");
			map.put("dataSource", dataSource);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public Object getBean(String beanName) {
		return map.get(beanName);
	}
	
	public Object getBean(Class<?> clazz) throws Exception{
		List<Object> list = 
				map.values().stream()
				.filter(o -> clazz.isInstance(o))
				.collect(Collectors.toList());
		
		return list.get(0);
	}
	
	public void doAutowired() throws Exception {
		ConstructorAutowired();
		fieldAutowired();
		setterAutowired();		
	}


	//빈 스캔
	//현재 객체가 담긴 로더에서 패키지 리소스 정보를 불러오고
	public void beanScan() throws Exception  {
		System.out.println("beanScan");
		ClassLoader loader = this.getClass().getClassLoader();

		//로더에서 리소스 홈 경로를 얻어옴 classes경로..
		//classes/common, classes/controller 이런 구조
		File topLevelDir = new File(getClass().getClassLoader().getResource("").getFile());
		
		//classes 밑에있는 모든 파일
		List<String> fileList = Arrays.stream(topLevelDir.listFiles()) //Stream<File>
				.flatMap(f -> Arrays.stream(f.listFiles())) //Stream<Stream<File>> -> Stream<File> 로 풀기 
				.filter(f -> f.getName().lastIndexOf(".class") >= 0) //자바 class일만..
				.map(f -> f.getPath().replace(topLevelDir.getPath() + "\\", "") //상위 디렉토리 잘라서 패키지명.클래스명 만 가져온다.
						.replace(".class", "")
						.replaceAll("\\\\", ".")).
				collect(Collectors.toList());

		for (String s : fileList) {
			Class<?> scanClass = Class.forName(s); //class파일 가져오기
			if (scanClass.getAnnotation(Bean.class) != null) {		
				if (Arrays.stream(scanClass.getConstructors())
						.anyMatch(c -> c.getAnnotation(Autowired.class) != null)) {
					//의존성 주입이 생성자 주입일 땐 생성자 주입하면서 객체 생성
					map.put(toLowerFirstIdx(scanClass.getSimpleName()), scanClass); //key를 넣어놓고 나중에 value가 Class 객체인 애들로 key를 찾음
				}else {				
					Constructor<?> constructor = scanClass.getDeclaredConstructor();
					constructor.setAccessible(true);
					map.put(toLowerFirstIdx(scanClass.getSimpleName()), constructor.newInstance(new Object[0]));
				}
			}
		}
	}
	
	
	private void fieldAutowired() throws Exception{
		System.out.println("fieldAutowired");
		for (Object obj : map.values()) { //빈객체 전체를 루프돈다
			fieldAutowired(obj);
		}
	}
	
	private void fieldAutowired(Object obj) throws Exception{
		Class<?> beanClass = obj.getClass();
		for (Field field : beanClass.getDeclaredFields()) {
			if (field.getAnnotation(Autowired.class) != null &&
					getBean(field.getType()) != null) { //빈객체의 필드에 autowired 어노테이션이 있으면
				
				field.setAccessible(true);
				field.set(obj, getBean(field.getType()));
			}
		}
		
	}
	
	//2. 생성자 주입 -> 
	private void ConstructorAutowired() throws Exception{	
		System.out.println("ConstructorAutowired");
		Set<Map.Entry<String, Object>> entrySet = map.entrySet(); //빈 엔트리
		//엔트리 중 값이 Class객체인 것들은 생성자 호출..
		for (Map.Entry<String, Object> entry : entrySet) {
			if (!(entry.getValue() instanceof Class))
				continue;
	
			Class<?> clazz = (Class<?>)entry.getValue();
			//생성자 인자가 가장 많은 수의 생성자를 선택
			Optional<Constructor<?>> conOptional =  
					Arrays.stream(clazz.getDeclaredConstructors())
						.filter(c -> c.getDeclaredAnnotation(Autowired.class) != null)
						.collect(Collectors.maxBy(
						Comparator.comparing(c -> c.getParameterCount() * -1)
					));
			
			Constructor<?> con = conOptional.get();
			//생성자 수 만큼 오브젝트 배열 생성
			Object[] objArr = new Object[con.getParameterCount()];
			Parameter[] paramArr = con.getParameters();
			
			for (int i=0; i<con.getParameterCount(); i++) {
				Parameter param = paramArr[i];
				//주입할 빈이 없으면 에러 던지자
				Object bean = getBean(param.getType()); 
				if (bean == null)
					throw new Exception(clazz.getName() + " [" + param.getName() + "]" + " Bean Not Found");
				
				objArr[i] = bean;	
			}

			Object result = con.newInstance(objArr);
			
			map.put(entry.getKey(), result);		
		}
	}
	
	//3. 세터 주입
	public void setterAutowired() throws Exception{
		System.out.println("setterAutowired");
		for (Object obj : map.values()) {
			Class<?> beanClass = obj.getClass();
			List<Method> methodList = Arrays.stream(beanClass.getDeclaredMethods())
										.filter( m -> m.getAnnotation(Autowired.class) != null)
										.collect(Collectors.toList());
	
			for (Method method : methodList) {
				Parameter[] params = method.getParameters();
				Object[] paramObj = new Object[params.length];
				
				for (int i=0; i<params.length; i++) 
					paramObj[i] = getBean(params[i].getType());	
							
				method.invoke(obj, paramObj);
			}
		}
	}
	
	//서블릿 주입.. 서블릿은 내가 생성하는게 아니라?
	//내가 생성하고 직접 등록하면 어떨까?
	public void servletAutowired(HttpServlet servlet) throws Exception{
		map.put(toLowerFirstIdx(servlet.getClass().getSimpleName()), servlet);
		fieldAutowired(servlet);
	}
}
