# Spring MVC 请求参数和返回参数的格式

## 1. 峰驼转下划线
在JAVA中,变量的命名方式一般为峰驼型(如userName),而在前端则是下划线方式(如user_name),springMVC提供了一种转换的适配

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
后端接收formdata的时候，可以通过重命名来确定接收的变量在后端的名称，如下，前端传过来的user_name会被放到userName变量

``` java
@RequestParam(value = "user_Name", required = true) String userName
```

请求数据   `formdata`:  user_name=xxxx

## 3. RequestBody接收json数据
除了formdata，一般的后台还接收json格式的数据

后端接收参数的方法如下(其中user有私有属性：id,userName,password,name,registerDate)

```java
@RequestBody User u
```


请求参数
```json
{
    "id": 1,
    "user_name": "dapan",
    "password": "123",
    "name": "dapan",
    "register_date": "1993-10-22"
  }
```

注意的是: 1) 在配置了峰驼转下划线后，请求体中的参数必须满足该规则(也就是user_name => userName)；2) 参数只能少，不能多，少的置成null，多则出400错误

# Spring MVC AOP 自定义注解
springMVC 提供AOP来拦截请求

在本文中，我们通过AOP来检查用户类型；用户类型分：普通用户和管理员

所以我们先定义一个annotation(如下)
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

然后定义该annotation对应的切片(如下)
```java
//before： 函数执行前所需要的操作
@Before("@annotation(com.demo.annotation.CheckUser) && (args(request,response,..) || args(..,request,response))") 
     public void beforeExec(JoinPoint joinPoint,HttpServletRequest request,HttpServletResponse response) {
         MethodSignature signature = (MethodSignature) joinPoint.getSignature();  
         Method method = signature.getMethod();  
         Class<?> classz = joinPoint.getTarget().getClass();
         
         CheckUser checkUser = getCheckUser(method,classz);//通过反射，获取注解里面UserType的值
         

         String token = request.getParameter("token");//获取请求体里面的令牌
         User user =  SessionManager.getSession(token);//系统中存储<令牌，用户>键值对的管理器
         if (user != null) {
             //在这里可以检查用户类型，是否为注解所要求的用户类型
         } else {
             //用户令牌无效的情况下，将response截断，并且返回{"code":1}(如果不截断，则会根据执行controller后，返回controller定义的类型)
             try {
                 response.reset();
                 response.setStatus(200);
                 response.setContentType("application/json;charset=UTF-8");
                 OutputStream out = response.getOutputStream();
                 out.write("{\"code\":1}".getBytes());
                 out.flush();
                 out.close();

             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }
```

对应的控制器如下，可以看出当调用getUserList接口的时候，首先会根据自定义的注解CheckUser，去执行相应的aspcet中的(beforeExec)，在aspect中检查用户的权限，根据用户权限来提前截断，或者继续执行
```java
    @CheckUser(type=UserType.Admin)
    @RequestMapping(value="getUserList", method = RequestMethod.GET)
    @ResponseBody
    public DataWrapper<list<User>> getUserList(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "token",required = true) String token
            ) {
        
        return  userService.getUserList(token);
    }
```

备注: 根据controller里面@CheckUser可知，在切片中，变量checkUser的值是UserType.Admin
