# SpringBoot
https://spring.io/projects/spring-boot

## Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications that you can "just run".
Spring Boot 能轻松创建独立可部署、生产级的 Spring 应用，直接运行即可。

We take an opinionated view of the Spring platform and third-party libraries so you can get started with minimum fuss. Most Spring Boot applications need minimal Spring configuration.
Spring Boot 对 Spring 平台及第三方库采用约定优于配置的设计理念，让你以最少的繁琐配置快速上手。绝大多数 Spring Boot 应用只需极少的 Spring 手动配置。

If you’re looking for information about a specific version, or instructions about how to upgrade from an earlier release, check out the project release notes section on our wiki.

Features
核心特性

* Create stand-alone Spring applications 创建独立运行的 Spring 应用
* Embed Tomcat, Jetty or Undertow directly (no need to deploy WAR files) 内置 Tomcat、Jetty、Undertow 服务器，无需打包部署 WAR 包
* Provide opinionated 'starter' dependencies to simplify your build configuration 提供场景化 starter 起步依赖，简化项目构建配置
* Automatically configure Spring and 3rd party libraries whenever possible 
尽可能自动配置 Spring 及第三方组件
* Provide production-ready features such as metrics, health checks, and externalized configuration 提供可直接用于生产环境的能力：应用指标监控、健康检查、外部化配置
* Absolutely no code generation and no requirement for XML configuration 完全无代码生成，也无需编写 XML 配置文件

## Maven

```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.6</version>
</parent>

 <packaging>jar</packaging>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

## Main 类

```java
@SpringBootApplication
public class SpringDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDemoApplication.class, args);
    }

}
```

## 配置

### properties

### yaml文件

### 命令行参数

### 环境变量

### 查找配置信息逻辑
先从命令行配置的spring.config.location找

再从工作目录的./config文件下找application.yaml文件和application.properties文件

再到根目录找

再从classpath找


### 多配置文件切换
比如有两个配置文件application-dev.properties 和 application-prod.properties

* 通过命令行参数指定启动哪个配置文件 --spring.profiles.active=prod
* 通过再application.properties指定spring.profiles.active=prod

### 指定配置文件来源
```java
@ConfigurationProperties(prefix = "person")
@Configuration
@PropertySource("classpath:myapplication.properties")
@Data
public class PersonBean {

    String name;
    Integer age;
}

//myapplication.properties"
person.name= Min
person.age= 18

```

### Environment
时SpringBoot提供的环境对象


## API注解

### RestController
表示类时controller类

### RestMapping 用来将url和控制器或控制器的方法绑定
* value是数组，可以指定多个值，支持ant风格（模糊匹配，占位符匹配）
```java
// 模糊匹配：?通配符只匹配一个字符，*0或多个匹配
@RequestMapping("/x?z/send")  //只能匹配/xyz这种中间有一个字符的
@RequestMapping("/x*z/send") //xz,xyz,xabcz都可以
public ResponseEntity<String> greet(@RequestParam(value = "name", defaultValue = "world") String name) {
        return ResponseEntity.status(HttpStatus.OK).body("Hello, " + name);
    }

// 占位符：在url中设置占位符，与@PathVariable相结合获取值，专门给RESTFUL编程风格准备的，form表单不支持restful 风格的
@RequestMapping("/{name}/send")  // {name}是占位符
public ResponseEntity<String> send(@PathVariable String name) {
        return ResponseEntity.status(HttpStatus.OK).body("Hello, path variable " + name);
    }

// RequestMapping param属性，要求url必须带某些参数
// RequestMapping headers属性，要求headers必须带某些参数
```

### Restful常见搭配

@PathVariable + @RequestBody 使用Java bean获取requestbody + @RequestHeader 获取header上的值
```java
    @PostMapping("/{id}/send")
    public ResponseEntity<String> send(@PathVariable String id, @RequestBody MyMessage myMessage, @RequestHeader("Authorization")  String authorization) { // 意味着request header上必须有Authorization
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Hello, path variable " + id + ", " + myMessage + ", " + authorization);
    }
```

## HttpMessageConverter
将HTTP请求和响应数据与Java对象直接的双向转换

## RequestEntity<T>

## ResponseEntity<T>

## 全局错误

```java
@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}

```

## 过滤器
Tomcat 里支持的过滤器，请求先进到ApplicationFilterChain 里，调用过滤器，过滤器执行完，再调用DispatcherServlet执行拦截器
```java
@Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        // Call the next filter if there is one
        if (pos < n) {
            ApplicationFilterConfig filterConfig = filters[pos++];
            try {
                Filter filter = filterConfig.getFilter();

                if (request.isAsyncSupported() && !(filterConfig.getFilterDef().getAsyncSupportedBoolean())) {
                    request.setAttribute(Globals.ASYNC_SUPPORTED_ATTR, Boolean.FALSE);
                }
                filter.doFilter(request, response, this);
            } catch (IOException | ServletException | RuntimeException e) {
                throw e;
            } catch (Throwable t) {
                ExceptionUtils.handleThrowable(t);
                throw new ServletException(sm.getString("filterChain.filter"), t);
            }
            return;
        }

        // We fell off the end of the chain -- call the servlet instance
        try {
            if (dispatcherWrapsSameObject) {
                lastServicedRequest.set(request);
                lastServicedResponse.set(response);
            }

            if (request.isAsyncSupported() && !servletSupportsAsync) {
                request.setAttribute(Globals.ASYNC_SUPPORTED_ATTR, Boolean.FALSE);
            }
            // Use potentially wrapped request from this point
            servlet.service(request, response);
        } catch (IOException | ServletException | RuntimeException e) {
            throw e;
        } catch (Throwable t) {
            ExceptionUtils.handleThrowable(t);
            throw new ServletException(sm.getString("filterChain.servlet"), t);
        } finally {
            if (dispatcherWrapsSameObject) {
                lastServicedRequest.set(null);
                lastServicedResponse.set(null);
            }
        }
    }

```
## 拦截器
在HandlerExecutionChain 类中,专门拦截DispatcherServlet请求的
```java
	boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		for (int i = 0; i < this.interceptorList.size(); i++) {
			HandlerInterceptor interceptor = this.interceptorList.get(i);
			if (!interceptor.preHandle(request, response, this.handler)) {
				triggerAfterCompletion(request, response, null);
				return false;
			}
			this.interceptorIndex = i;
		}
		return true;
	}

	/**
	 * Apply postHandle methods of registered interceptors.
	 */
	void applyPostHandle(HttpServletRequest request, HttpServletResponse response, @Nullable ModelAndView mv)
			throws Exception {

		for (int i = this.interceptorList.size() - 1; i >= 0; i--) {
			HandlerInterceptor interceptor = this.interceptorList.get(i);
			interceptor.postHandle(request, response, this.handler, mv);
		}
	}

```

## 请求执行流程

请求到达tomcat -> 执行pre过滤器 -> DispatcherServlet -> 执行pre拦截器 -> Controller -> 执行post 拦截器 -> 执行post过滤器

结论： Filter的拦截范围更大，它是tomcat里 servlet带的

### 设置Filter

```java
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        System.out.println("Filter 前置处理：" + req.getRequestURI());
        chain.doFilter(request, response);
        System.out.println("Filter 后置处理");
    }
}

// FilterRegistrationBean 可以配置filter 拦截路径和拦截顺序
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter> myFilter() {
        FilterRegistrationBean<MyFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new MyFilter());
        bean.addUrlPatterns("/*"); //拦截路径
        bean.setOrder(1); //拦截顺序
        return bean;
    }
}
```

### 设置Interceptor
```java
public class MyInterceptor implements HandlerInterceptor {


    // Controller 执行之前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        System.out.println("Interceptor 前置：拦截请求");
        // true 放行；false 拦截不放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("Interceptor 后置：拦截请求");
    }

    // 整个请求完毕
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        System.out.println("Interceptor 请求完毕");
    }
}

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**")       // 拦截所有接口
                .excludePathPatterns("/static/**"); // 放行路径
    }
}
```

### RequestMappingHandlerMapping 
做url和方法的映射,存方法的lambda

父类AbstractHandlerMethodMapping 的initHandlerMethods 用来初始化map 
