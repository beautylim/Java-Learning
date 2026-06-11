# 动态代理

## JDK动态代理，代理类是通过目标类的接口生成的

通过InvocationHandler 反射调用目标类的方法
```java
public interface Star {

    void sing();

    void dance();
}

public class Star1 implements Star{

    @Override
    public void sing() {
        System.out.println("唱歌");
        dance();
    }

    @Override
    public void dance() {
        System.out.println("跳舞");
    }
}


public class DynamicProxyDemo {

    static void main(String[] args) {
        Star1 star1 = new Star1();
        Star agent = (Star) Proxy.newProxyInstance(star1.getClass().getClassLoader(), new Class[]{Star.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getName().equals("sing")) {
                    System.out.println("中介收钱20万");
                } else if (method.getName().equals("dance")) {
                    System.out.println("中介收起30万");
                }
                return method.invoke(star1, args);
            }
        });

        agent.sing();
        agent.dance();
    }
}

//中介收钱20万
//唱歌
//跳舞
//中介收起30万
//跳舞
```
可以看到通过JDK 动态代理，如果在目标类中调用自方法，是不走代理类的，因为代理类是通过接口实现的，导致明星在唱歌过程中跳舞没有收钱


## CGLib 动态代理，可以没有接口，代理类直接继承被代理类
```java
public class CglibProxyDemo {

    public static void main(String[] args) {
        Star1 star1 = new Star1();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(Star1.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                if (method.getName().equals("sing")) {
                    System.out.println("CGLib: 收钱20W");
                } else if (method.getName().equals("dance")) {
                    System.out.println("CGLib: 收钱30W");
                }
                return proxy.invokeSuper(obj, args);
            }
        });

        Star1 proxy = (Star1) enhancer.create();
        proxy.sing();
        proxy.dance();
    }
}

//CGLib: 收钱20W
//唱歌
//CGLib: 收钱30W
//跳舞
//CGLib: 收钱30W
//跳舞
```

通过下面一个例子，感受一下子类继承父类和类实现接口的区别
```java
// 接口： 明星
public interface Star {

    void sing();

    void dance();
}

// 实现类：某个明星
// 会在执行时打印this到底是谁
public class Star1 implements Star{

    @Override
    public void sing() {
        System.out.println("唱歌");
        System.out.println(this);
        dance();
    }

    @Override
    public void dance() {
        System.out.println(this);
        System.out.println("跳舞");
    }
}

// 通过接口实现明星接口，持有某个明星，达到代理目的
public class Agent implements Star {

    private Star star;

    public Agent(Star star) {
        this.star = star;
    }

    @Override
    public void sing() {
        System.out.println("静态代理: 接口: 收钱20W");
        star.sing();
    }

    @Override
    public void dance() {
        System.out.println("静态代理: 接口: 收钱30W");
        star.dance();
    }
}

// 继承某明星类 成为子类
public class AgentEx extends Star1 {

    @Override
    public void sing() {
        System.out.println("静态代理: 继承: 收钱20w");
        super.sing();
    }

    @Override
    public void dance() {
        System.out.println("静态代理: 继承: 收钱30w");
        super.dance();
    }
}

// 测试
public class TestStaticProxy {

    static void main(String[] args) {
        Star star = new Star1();
        Star agent = new Agent(star);

        agent.sing();
        agent.dance();

        System.out.println("----------------------------------");
        Star agentExt = new AgentEx();

        agentExt.sing();
        agentExt.dance();
    }
}

// 静态代理: 接口: 收钱20W
// 唱歌
// org.example.dynamicproxy.Star1@65b54208
// org.example.dynamicproxy.Star1@65b54208
// 跳舞
// 静态代理: 接口: 收钱30W
// org.example.dynamicproxy.Star1@65b54208
// 跳舞
// ----------------------------------
// 静态代理: 继承: 收钱20w
// 唱歌
// org.example.dynamicproxy.AgentEx@6b884d57
// 静态代理: 继承: 收钱30w
// org.example.dynamicproxy.AgentEx@6b884d57
// 跳舞
// 静态代理: 继承: 收钱30w
// org.example.dynamicproxy.AgentEx@6b884d57
// 跳舞
```
可以看到对子类调用父类方法时，父类的this还是子类，所以调的还是子类方法