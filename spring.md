# Spring

## 五大开发原则
SOLID

* 单一职责： 一个类应该只干一件事，只负责一个业务职责
* 开闭原则：对修改关闭，对扩展开发，用接口，抽象类实现扩展
* 里氏替换原则：子类可以完全替换父类，程序员不报错，逻辑不变，好处多态，替换无副作用
* 接口隔离原则：接口要细粒度，不要实现没有用到的接口
* 依赖倒置原则：应该依赖抽象或者接口，不应该依赖具体实现，减少耦合


## IOC
控制反转是一种spring思想，Spring IOC容器接管对象的创建和依赖配置，遵循了依赖倒置原则

## DI
依赖注入，是实现spring IOC的一种方式

## Spring框架，有Spring context, Spring aop, Spring data， spring-core


## bean的生命周期

1. 实例化
2. 给参数赋值
4. Aware接口回调
    * BeanNameAware：获取自己在容器中的 beanName
    * BeanFactoryAware：获取 BeanFactory 工厂
    * ApplicationContextAware：获取 ApplicationContext 容器
5. BeanPostProccessor前置
    * 执行 BeanPostProcessor.beforeInitialization() AOP 代理很多在这里生成
6. 初始化调用：@PostConstruct注解方法，实现InitializingBean#afterPropertiesSet()，或者@Bean指定的init-method
7. Bean后置处理：执行 BeanPostProcessor.afterInitialization()
8. 就绪，放到单例池中使用
9. 使用
10. 销毁

## 三层缓存

一级缓存：
singletonObjects， 存放，已经创建好的单例对象

二级缓存：earlySingletonObjects
放早期bean, 半成品bean

三级缓存：singletonFactories, 放bean factory

如果有B依赖A，A依赖B 用setter字段注入的话
1. Spring 构造器实例化A
2. 把A包装成ObjectFactory 放入三级缓存
3. 开始给A依赖注入，发现依赖B
4. 创建B，发现又依赖A
5. B先从一级缓存找，没有
6. B又从二级缓存找，没有
7. B又从三级缓存找，有，同A的ObjeFactory拿到A，如果需要AOP，在这里提前创建和代理对象
8. 把拿到的A放入二级缓存，删掉三级缓存工厂
9. B完成，放入以及缓存
10. A继续走完属性填充，初始化，也放入以及缓存


## 注解
生成对象的注解

@Controller
@Service
@Repository
@Component

其实@Controller
@Service
@Repository 只是语义上有区别，行为上没啥区别都是@Component的别名

给属性赋值的注解

@Value（简单类型注解）
@Autowired 自动装配，复杂对象注解，byType找bean
@Qualifier("名称"),与@Autowired一起使用，通过名称找bean，默认required=true，找不到就报错

@Resource() JDK自带的不是spring的，默认通过属性名称找bean，没有这个名字的bean再通过类型

@Configuration，配置bean的类，自身也是bean，加@ComponentScan指定IOC容器创建哪些包下的bean, 也可以通过申明@Bean在方法中，自定义bean

## AOP
切面
通过spring-aspects

### 七大术语
1. 连接点（Joinpoint)： 调用方法前，后，环绕，抛出异常，返回方法
2. 切点（Pointcut): 表达了在什么类什么方法上切入
3. 增强（Advice）：加强功能
4. 切面（Aspect）：切点加增强
5. 目标对象（Target）：被代理类
6. Aop代理：生成的代理类
7. 织入（Weaving）： 将增强添加到目标类的连接点上的过程

```java
@Component
@Aspect
public class MyAspect {
    
    @Pointcut("execution(* org.demo.service.*.call(..))")
    public void pointcut() {
        
    }

    // 前置增强
    @Before("pointcut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("前置增强");
    }


    // 后置增强
    @After("pointcut()")
    public void afterAdvice(JoinPoint joinPoint) { // 只要方法调用，就会响应后置增强

        System.out.println("后置增强");
    }

    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) {
        System.out.println("环绕前");
        Object o = null;
        try {
            o = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("环绕后");
            return o;
        }
    }

    @AfterReturning("pointcut()") // 只有正常返回，才会执行
    public void afterReturning() {
        System.out.println("返回增强");
    }

    @AfterThrowing("execution(* org.demo.service.*.call(..))")
    public void afterThrowing() {
        System.out.println("异常增强");
    }
}


// 环绕前
// 前置增强
// MyService call
// 返回增强
// 后置增强
// 环绕后
```
