
# Spring MVC

## 将pom.xml里的packaging改为war
```xml
    <packaging>war</packaging>
```
## DispatcherServlet
是SpringMVC框架为我们提供的最核心的类，它是整个SpringMVC框架的前端控制器，负责接收HTTP请求，将请求路由到处理程序。是web应用程序的主要入口之一，它的职责包括：
1. 接受HTTP请求
2. 通过请求路径URL找到Controller
3. 渲染视图
4. 返回响应给客户端

核心方法：doDispatch(request, response);

## web.xml
在src/main目录下的webapp/WEB-INF/web.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">
    <!-- 配置前端控制器，所有的请求过来，都要经过前端控制器，除非是JSP请求路径-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <!-- 官方推荐写法，/*会拦截所有路径，包括jsp, jsp应该交由web容器内置的JspServlet自动处理, springMVC 不应该拦截，因此不建议写/* -->
        <!-- /拦截的是所有其他servlet不要的请求，不过在spring mvc开发中我们只编写一个DispatchServlet, 不需要编写其他Servket, 等同的认为/拦住了除jsp之外的所有请求 -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```
## Configuration类
```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.example.springmvc")
public class MyConfig {

    // ========== Thymeleaf 配置 三个Bean ==========

    // 1. 模板解析器：指定页面位置、后缀、编码
    @Bean
    public ClassLoaderTemplateResolver templateResolver(){
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        // 视图前缀：resources/templates/
        resolver.setPrefix("/WEB-INF/templates/");
        // 视图后缀：.html
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode("HTML5");
        return resolver;
    }

    // 2. 模板引擎
    @Bean
    public SpringTemplateEngine templateEngine(ClassLoaderTemplateResolver templateResolver){
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver);
        return engine;
    }

    // 3. Thymeleaf 视图解析器 替代SpringMVC默认视图解析器
    @Bean
    public ViewResolver viewResolver(SpringTemplateEngine templateEngine){
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);
        return resolver;
    }
}

```