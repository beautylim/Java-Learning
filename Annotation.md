# 注解

## 格式
```java
public @interface 注解名称 {
    public 属性类型 属性名() default 默认值;
}
```

例如:
```java
public @interface MessageHandler {
    String routingKey() default "";
}



@MessageHandler(routingKey = "upgrade.start")
public class DemoAnno {

    static void main(String[] args) {

    }
}

```

## 注解原理
使用Xjad 反编译工具，将MessageHandler字节码文件.class， 反编译成Java文件
``` java
public interface MessageHandler extends Annotation
{

	public abstract String routingKey();
}
```
通过反编译好的代码可以看出，注解本质是一个接口，集成了Annotation接口，注解里面写的属性，其实是抽象方法

@注解，就是再实现类对象，实现了该注解以及Annotation接口


## 元注解
修饰注解的注解

### @Target
声明注解能够用在哪个位置
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Target {
    /**
     * Returns an array of the kinds of elements an annotation interface
     * can be applied to.
     * @return an array of the kinds of elements an annotation interface
     * can be applied to
     */
    ElementType[] value();
}

@Target(ElementType.TYPE， ElementType.METHOD) // value是数组，所以Target可以声明多个值

public enum ElementType {
    /** Class, interface (including annotation interface), enum, or record
     * declaration */
    // 类，接口（包括注释接口), 枚举，或者记录
    TYPE,

    /** Field declaration (includes enum constants) */
    // 成员变量（包括枚举常量)
    FIELD,

    /** Method declaration */
    // 方法
    METHOD,

    /** Formal parameter declaration */
    // 参数
    PARAMETER,

    /** Constructor declaration */
    // 构造函数
    CONSTRUCTOR,

    /** Local variable declaration */
    // 局部变量
    LOCAL_VARIABLE,

    /** Annotation interface declaration (Formerly known as an annotation type.) */
    // 注释接口
    ANNOTATION_TYPE,

    /** Package declaration */
    // 包
    PACKAGE,

    /**
     * Type parameter declaration
     *
     * @since 1.8
     */
    // 参数类型
    TYPE_PARAMETER,

    /**
     * Use of a type
     *
     * @since 1.8
     */
    TYPE_USE,

    /**
     * Module declaration.
     *
     * @since 9
     */
    // 模块
    MODULE,

    /**
     * Record component
     *
     * @jls 8.10.3 Record Members
     * @jls 9.7.4 Where Annotations May Appear
     *
     * @since 16
     */
    RECORD_COMPONENT;
}

```

### @Retention
声明注解的保留周期
```java
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Retention {
    /**
     * {@return the retention policy}
     */
    RetentionPolicy value(); // value只有一个值
}

public enum RetentionPolicy {
    /**
     * Annotations are to be discarded by the compiler.
     */
    // 编译器会抛弃这个注释
    SOURCE,

    /**
     * Annotations are to be recorded in the class file by the compiler
     * but need not be retained by the VM at run time.  This is the default
     * behavior.
     * 
     */
    // 注释会被编译器保留在字节码文件中， 默认是CLASS
    CLASS,

    /**
     * Annotations are to be recorded in the class file by the compiler and
     * retained by the VM at run time, so they may be read reflectively.
     *
     * @see java.lang.reflect.AnnotatedElement
     */
    // 注释会被编译器保留在字节码文件中， 并且保存到运作时
    RUNTIME
}

```

## 解析注解，使用注解

步骤： 要解析谁上面的注解，就先解析谁。比如：想解析类上的注解，先获取类对象，再通过类对象解析其上面的注解。
比如： 解析
``` java
@MessageHandler(routingKey = "order.post")
public class DemoAnno {

    static void main(String[] args) {
        Class<DemoAnno> demoClass = DemoAnno.class;

        Optional.of(demoClass.isAnnotationPresent(MessageHandler.class))
                .filter(Boolean::valueOf)
                .ifPresent(_ -> {
                    MessageHandler messageHandler = (MessageHandler) demoClass.getDeclaredAnnotation(MessageHandler.class); // 类对象上的注解，已经是注解类对象的实例了
                    System.out.println(messageHandler.routingKey()); //直接调用方法，注意注解是接口，属性其实是方法，所以属性名称后面一定要加（）
                });
    }
}
```

## 应用场景：做框架
设计两个简单的注解，模拟Junit 的Test，Order注解，即MyTest, MyOrder. 通过MyTest 判断方法是否是测试方法，如果是就执行，通过MyOrder判断该方法的执行顺序，并将所有的测试方法按照定义的顺序执行，注意，可能有多个方法的MyOrder是相同的
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyOrder {
    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MyTest {
}

public class DemoTest {

    @MyTest
    @MyOrder(1)
    public void test1() {
        System.out.println("test1");
    }

    public void test2() {
        System.out.println("test2");
    }

    @MyTest
    @MyOrder(2)
    public void test3() {
        System.out.println("test3");
    }

    @MyTest
    @MyOrder(4)
    public void test4() {
        System.out.println("test4");
    }

    @MyTest
    @MyOrder(4)
    public void test5() {
        System.out.println("test5");
    }

    static void main(String[] args) {
        DemoTest demoTest = new DemoTest();
        Class<? extends DemoTest> demoTestClass = demoTest.getClass();
        Map<Integer, List<Method>> collect = Arrays.stream(demoTestClass.getMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .sorted((m1, m2) -> {
                    int order1 = getOrder(m1);
                    int order2 = getOrder(m2);
                    return order1 - order2;
                })
                .peek(method -> System.out.println(method.getName()))
                .collect(Collectors.groupingBy(DemoTest::getOrder));
        collect.forEach((key, value) -> {
            System.out.println("Current order: " + key);
            value.forEach(method -> {
                try {
                    method.invoke(demoTest);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            });
        });

    }

    static int getOrder(Method method) {
        if (!method.isAnnotationPresent(MyOrder.class)) {
            return Integer.MAX_VALUE;
        }
        MyOrder myOrder = method.getDeclaredAnnotation(MyOrder.class);
        return myOrder.value();
    }
}
```
