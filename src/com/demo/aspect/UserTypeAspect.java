package com.demo.aspect;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.demo.annotation.CheckUser;
import com.demo.models.User;
import com.demo.utils.SessionManager;

@Aspect
@Component
public class UserTypeAspect {
	 ThreadLocal<Long> time = new ThreadLocal<Long>();  
	 ThreadLocal<String> tag = new ThreadLocal<String>();   
	    
//	 @Autowired
//	 UserDao userDao;
	 @Before("@annotation(com.demo.annotation.CheckUser) && (args(request,response,..) || args(..,request,response))") 
	 public void beforeExec(JoinPoint joinPoint,HttpServletRequest request,HttpServletResponse response) {
		 time.set(System.currentTimeMillis());  
		 tag.set(UUID.randomUUID().toString());  
		 
		 
//		 Object args[] = joinPoint.getArgs();  
		 MethodSignature signature = (MethodSignature) joinPoint.getSignature();  
		 Method method = signature.getMethod();  
		 Class<?> classz = joinPoint.getTarget().getClass();
		 
		 CheckUser checkUser = getCheckUser(method,classz);
		 
		 //检查autowired
//		 String userName = request.getParameter("userName");
//		 User user = userDao.getByUserName(userName);
//		 System.out.println(user.getName());
		 
		 
		 
		 String token = request.getParameter("token");
		 User user =  SessionManager.getSession(token);
		 if (user != null) {
			 //可以通过检查用户类型来进行对应的操作
			 System.out.println(checkUser.type());
		 } else {
			 
			 try {
	
				 
				 response.reset();
				 
				 response.setStatus(200);
				 response.setContentType("application/json;charset=UTF-8");
				 OutputStream out = response.getOutputStream();
//				 out.write(1);

				 out.write("{\"code\":1}".getBytes());
				 out.flush();
				 out.close();

			 } catch (Exception e) {
				// TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		 }
	 }
	
//	 @After("@annotation(com.demo.annotation.CheckUser) && (args(request,..) || args(..,request))")  
//	 public void afterExec(JoinPoint joinPoint,HttpServletRequest request){  
//		 //这里可以记录日志
//		 MethodSignature ms = (MethodSignature) joinPoint.getSignature();  
//		 Method method = ms.getMethod();  
//		 System.out.println("标记为"+tag.get()+"的方法"+method.getName()+"运行消耗"+(System.currentTimeMillis()-time.get())+"ms");  
//	 }
	 
	 public CheckUser getCheckUser(Method method,Class<?> classz) {
		 boolean isClzAnnotation = classz.isAnnotationPresent(CheckUser.class);
         boolean isMethondAnnotation = method.isAnnotationPresent(CheckUser.class);
         CheckUser checkUser = null;
         //如果方法和类声明中同时存在这个注解，那么方法中的会覆盖类中的设定。
         if(isMethondAnnotation){
        	 checkUser = method.getAnnotation(CheckUser.class);
         }else if(isClzAnnotation){
        	 checkUser = classz.getAnnotation(CheckUser.class);
         }
         return checkUser;
		 
	 }

}
