# Spring MVC 请求参数和返回参数的格式
## 1. 峰驼转下划线

对dispacther的jackson进行配置
```xml
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
              <property name="messageConverters">
                     <list>
                            <bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
                            	<!-- 驼峰转下划线 -->
                            	<property name="objectMapper">
                    				<bean class="com.fasterxml.jackson.databind.ObjectMapper">
                        				<property name="propertyNamingStrategy">
                           					 <bean class="com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy" />
                        				</property>
                   					 </bean>
                				</property>
                				<property name="supportedMediaTypes">
                    				<list>
                        				<!-- <value>text/html;charset=UTF-8</value> -->
                        				<value>application/json; charset=UTF-8</value>
                    				</list>
                				</property>
                            	<!-- 驼峰转下划线 -->
                            
                            </bean>
                            
                            <bean class="org.springframework.http.converter.FormHttpMessageConverter"/>
                     </list>
              </property>
       </bean>
```

对于返回的结果，会将后端的userName，转化为前端的user_name

## 2. formdata
后端接收formdata
``` java
@RequestParam(value = "user_Name", required = true) String userName
````
请求数据   formdata: user_name=xxxx

## 3. RequestBody接收json数据

服务器接收参数如下，具体见usercontroller内的testJsonInput接口
``` java
@RequestBody User u
```

请求数据
```json
{
    "id": 1,
    "user_name": "dapan",
    "password": "123",
    "name": "dapan",
    "register_date": "1993-10-22"
  }
```

注意的是，在配置了峰驼转下划线后，请求体重的参数也要符合；第二个是参数只能少，不能多，少的置成null，多则出400错误

# Spring MVC AOP 自定义注解
springMVC 提供AOP来拦截请求
在本文中，我们通过AOP来检查用户类型；用户类型分：普通用户和管理员

所以我们先定义一个annotation
```java
@Retention(RetentionPolicy.RUNTIME)  
@Target({ElementType.METHOD,ElementType.TYPE})  
@Documented  
//最高优先级  
@Order(Ordered.HIGHEST_PRECEDENCE)  
public @interface CheckUser {
	UserType type() default UserType.User; //UserType 是一个含有User和Admin的枚举类型

}
```



