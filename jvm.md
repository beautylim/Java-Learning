# JVM

# JVM里面有什么

## 类装载子系统
加载字节码文件

## 字节码执行引擎
* 执行代码
* 垃圾回收

## JVM 运行时内存

## 程序计数器
线程私有，每个线程有一块内存专门用来记录线程执行的位置或者行号，目的是为了多线程切换后能从上一次执行完的地方继续执行
## JVM 虚拟机栈
线程私有，每个线程在执行一个方法时会被分配一个栈帧，用来存储
* 局部变量表： 存局部变量
* 操作数栈：为了变量运算做存储
* 动态链接：Java字节码存在元空间，这里时将字节码中用到的符号引用替换成元空间中存储的地址
* 方法出口：记录调用该方法的位置，调用完知道上一个调用它的地方在哪

## 本地方法栈
java是由C++写的，所以这里存放C++的本地方法用到的 局部变量表，操作数栈等

## 堆
线程共享，存放对象的地方，在栈中的局部变量表中存的时堆的地址， 此外还有：
* 字符串常量池
* 静态变量

## 元空间（Java 8之前叫方法区）
线程共享，存放字节码的地方，运行时常量池（类常量池）等


## 线上代码排查工具arthas

```java
//下载
https://alibaba.github.io/arthas/arthas-boot.jar

//运行, 不要用gitbash， 因为暂停arthas 刷新 需要ctrl + C，git bash 会直接退出arthas
java -jar arthas-boot.jar

//1. 输入序列号 选择查看哪个进程

//2. 打完arthas banner后，输入dashboard，查看线程和memory情况，dashboard 会隔几秒就刷新
![dashboard](./image/image.png)

//3. Ctrl + C, 暂停刷新, 输入命令进行查看

// thread <thread - id> （查看某个线程当前情况）

// thread -b (查看有没有block的线程)

// jad classpath （反编译代码）

// ognl （查看或者修改线上系统的某个变量值）

```

## 内存调优

### JVM 参数设置

操作系统需要2-3G

一个对象一般不超过1KB

### 进入老年代判断标准

#### 长期存活的对象进入老年代
每次young GC， 都会给对象标年龄，当对象年龄超过一定值的时候就直接进入老年代了

### 对象动态年龄判断
新生代在做minor GC时， 如果存活的对象大小大于suvivor区规定的百分比（例如50%， -XX:TargetSuvivorRatio 可调） 会直接进入老年代，导致老年代最终被占满，引发full GC

### 老年代空间分配担保机制

## 生产环境一般把-Xms 和 -Xmx设为大小相同的值，防止动态扩容，动态扩容会引起full GC


## OOM 发生后程序一定会退出吗

不一定，
如果不是主线程发生的OOM，还有其他线程在跑，发送OOM的线程如果被try catch住了 线程不会结束，如果没有被catch住会线程结束，只要有其他线程在运行，JVM就不会停止运行。

如果程序用try catch 捕获了OOM就不会退出, 比如：
```java
    static void main(String[] args) {
        Thread thread = new Thread(() -> {
            try {
                ooM();
            }catch (OutOfMemoryError error) {
                System.out.println(error.getMessage() + " catched");
            }

            System.out.println("create OOM again");
            ooM();
            System.out.println("OOM happened");
        });

        Thread thread1 = new Thread(() -> {
           while (true) {
               System.out.println("looping");
               try {
                   Thread.sleep(2000);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        thread.start();
        thread1.start();
    }

    static void ooM() {
        List<Student> students = new ArrayList<>();
        AtomicInteger atomicInteger = new AtomicInteger();
        while (true) {
            Student student = new Student("Student" + atomicInteger.addAndGet(1));
            students.add(student);
        }
    }
}

// looping
// looping
// looping
// looping
// Java heap space catched
// create OOM again
// looping
// looping
// looping
// Exception in thread "Thread-0" java.lang.OutOfMemoryError: Java heap space
// 	at java.base/java.lang.StringConcatHelper$Concat1.concat(StringConcatHelper.java:111)
// 	at java.base/java.lang.invoke.DirectMethodHandle$Holder.invokeSpecial(DirectMethodHandle$Holder)
// 	at java.base/java.lang.invoke.LambdaForm$MH/0x0000000085044000.invoke(LambdaForm$MH)
// 	at java.base/java.lang.invoke.Invokers$Holder.linkToTargetMethod(Invokers$Holder)
// 	at org.example.jvm.DemoOOM.ooM(DemoOOM.java:43)
// 	at org.example.jvm.DemoOOM.lambda$main$0(DemoOOM.java:20)
// 	at org.example.jvm.DemoOOM$$Lambda/0x0000000085042a10.run(Unknown Source)
// 	at java.base/java.lang.Thread.runWith(Thread.java:1529)
// 	at java.base/java.lang.Thread.run(Thread.java:1516)
// looping
// looping
// looping
// looping
// looping

```

但是在Linux系统中，OOM Killer会杀死进程：
当物理内存用尽，Swap也用光，系统内核会选占用内存最大的进程直接kill -9


## 对象一定存在堆上吗

不一定，通过逃逸分析，通过确定对象作用范围，JVM确定该对象不会被该方法以外的代码访问，那么JVM有可能会在栈上分配对象， 这样做的好处是 可以减少垃圾回收压力，方法做完可以立马回收

标量替换，如果对象不会被方法以外访问，并且对象都是基础属性，就把对象的属性拆分成标量就是局部变量存在局部变量表，就不需要分配堆内存，效率会更高，这是JVM优化的结果。

## 内存泄露和内存溢出的区别

* 内存泄漏指的是对象已不再使用但是不能被释放出来 还是被引用着，所以回收不了。程序没有正确的清除，浪费内存资源。
  比如： 集合类

* 内存溢出，程序在申请内存时，没有足够内存可用，从而抛出OutOfMemory
  
## Class 文件常量池、运行时常量池
### 1. Class 文件常量池（静态常量池）
是.java 编译成 .class 文件后，存在 class 文件里的一张常量表。
存字符串字面量 类名、方法名、字段名
基本类型常量
符号引用（还没解析成内存地址）
#### 特点：
编译期就固定了，存在磁盘上的 class 文件里，此时都是符号引用，不是真实内存地址还没加载进 JVM
### 2. 运行时常量池
类被 JVM 加载进内存后，把 Class 文件常量池 加载到内存里，就变成了运行时常量池。
#### 存在哪 **（重点必考）** <br/>
JDK1.8 及以后：存放在 元空间 Metaspace
#### 做什么 <br/>
把编译期的符号引用 → 解析成直接引用（内存地址, 运行期间还可以动态加入新常量（比如 String.intern()）
#### 特点
每个类都有自己独立的运行时常量池, 属于方法区 / 元空间 逻辑一部分, 运行期动态可变，不是只读
### 3. 两者关系（一句话串起来）
```text
Java源码 → 编译 → Class文件常量池(磁盘)
               ↓
        类加载进JVM
               ↓
        运行时常量池(元空间内存)
```
#### Class 文件常量池
磁盘上的静态版本, 磁盘 class 文件里，编译期常量、符号引用
#### 运行时常量池
加载后放进 元空间，每个类独有，解析符号引用
#### 字符串常量池 StringPool
JDK1.7+ 移到 堆内存，全局共享，放字面量字符串

## 垃圾收集算法

### 标记清除
把垃圾直接清除
好处：效率高
坏处：碎片化


### 标记整理
把垃圾清除后，剩余对象往一端挪
好处：没有内存碎片，不需要像复制算法一样浪费一半内存
坏处：效率不高，速度慢，开销大，停顿时间长
适合老年代


### 复制
把存活的对象复制到另一块内存，把垃圾和原来的存活对象内存都清掉，来回复制
好处：没有内存碎片，速度快
坏处：浪费一半内存空间
适合新生代，因为新生代的对象大部分是要被回收掉的，所以要复制的内存少，速度快

## 垃圾回收器

### CMS
并发回收

三色标记 - 会产生错标- 增量更新 - 重新标记

### ParallelGC
新生代 复制算法
老年代 标记整理 算法



### G1 物理不分代，逻辑分代

分区回收， 动态调整新生代和老年代的比例
分区的好处是可以分区清理垃圾最多的区域，降低stop the world的时间

三色标记+SATB

G1控制GC 时间
```
-XX:MaxGCPauseMillis=200 //默认200毫秒
```

### ZGC 逻辑也不分代

## 调优

### JVM 参数
```shell
java -XX:+PrintFlagsInitial 
```
通过这行命令可以看到Java， 有多少个JVM参数（Java26 有829个参数)

## JVM命令

### jps

### jstack <pid>
查看堆栈信息

### jstack -gc <pid>
查看gc信息

### jinfo 查看JVM信息和参数

### jmap 
dump文件

## Object 对象有哪些构成

### 对象头：mark word  64bits 8bytes
记录锁的信息，GC 信息，hashcode，偏向锁Id，时间错

### 对象头：class pointer
指向所属的class地址， 被压缩成4个字节，所以是压缩指针， 不过机器内存超过32G，压缩指针会失效，会变成8字节

### 实例数据
存自己定义的成员变量（非 static）
基本类型：int、long、boolean、char 等
引用类型：String、自定义对象引用
注意：
static 静态变量不属于对象实例，存在堆 / 元空间，不占单个对象内存

### 对齐填充
HotSpot 要求：对象整体大小必须是 8 字节的整数倍
前面两部分凑不够 8 的倍数，就补空白字节，用来内存对齐、提升访问效率。


## 什么是进程


## 什么是线程

## CPU

CPU有多核
一核CPU 包括:
1. Register 寄存器，存放计算的变量，存计算的数据，临时结果，个数少，速度天花板
2. PC，程序计数器，存下一条要执行的指令地址，JVM里也有程序计数器和这个类似
3. ALU，算术逻辑单元，CPU计算干活的核心
4. Cache，缓存（L1/L2/L3）, CPU和内存之间的缓冲层，比内存快
5. CU， 控制单元，总指挥，翻译指令、调度 ALU、寄存器、内存、缓存，协调所有部件干活。

### 缓存行 cache line

缓存行是CPU与主内存直接传输数据的最小单位： 一块连续的64字节内存地址

#### 为什么要有缓存行
访问一个变量，大概率马上会访问它旁边的变量，所以CPU一次把附近64字节都拉进缓存， 后面访问相邻数据能直接命中缓存


每个CPU核都有自己的L1/L2缓存，多核心共享L3缓存+主内存， 靠MESI缓存一致性保持多核可见
#### 缓存行填充策略
先前提
主流 CPU 缓存行默认 64Byte
只要多个独立竞争变量落在同一条缓存行，就会伪共享，性能暴跌。
填充策略核心目的：让高并发竞争变量独占一整个缓存行。


## JMM解决了多线程并发的三大问题

### 原子性，一个或多个操作不可被中途打断，通过synchronized 实现， 或者ReetrantLock实现， volatile不能保证原子性

### 可见性，一个线程对共享变量做了修改，另一个线程能立马看到，通过锁或者volatile
例如：下面这个例子，当主线程对共享变量flag设为false的时候，thread1并没有停止，说明，主线程的修改，thread1看不到
```java
public class ThreadVisible {
    public static boolean flag = true;

    static void main() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("Start run");
            while (flag) {

            }
            System.out.println("Stop run");
        }, "Thread-flag");

        t1.start();
        Thread.sleep(2000);
        flag = false;
        System.out.println("Turn flag to false");
    }
}

```
解决方法，用volatile修饰flag, 这样主线程修改完以后一定能刷到主内存，而thread1也能立马去主内存读
```java
public static volatile boolean flag = true;
```
也可以用锁， 注意
```java
    public void println(int x) {
        if (getClass() == PrintStream.class) {
            writeln(String.valueOf(x));
        } else {
            synchronized (this) {
                print(x);
                newLine();
            }
        }
    }
```
所以如果在while 循环中加System.out.println() 也是能保证可见性的

```java
public class ThreadVisible {
    public static boolean flag = true;

    static void main() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            System.out.println("Start run");
            while (flag) {
                System.out.println(1); // System.out.println(), 带synchronized的锁
            }
            System.out.println("Stop run");
        }, "Thread-flag");

        t1.start();
        Thread.sleep(2000);
        flag = false;
        System.out.println("Turn flag to false");
    }
}
```

## synchronized
synchronized 内存语义（JMM 规定死的）
1. 解锁时（退出 synchronized 代码块）
把当前线程工作内存中，所有**共享变量**的修改，全部刷新到主内存
👉 全域刷，不局限 synchronized 代码块内部变量
2. 加锁时（进入 synchronized 代码块）
清空当前线程工作内存里所有共享变量的缓存副本
后面用到共享变量，必须重新从主内存加载最新值
👉 全域作废本地缓存，也不局限代码块里的变量

## Lock锁

本质是通过volatile修饰的变量state, 保证多线程的可见性，有序性

当线程来抢锁时，尝试用CAS来更改state的状态从0改为1，如果已经不是0了说明lock锁已经被别人抢了，就把线程挂起，会被 AQS（AbstractQueuedSynchronizer）放入一个双向阻塞队列，然后调用 LockSupport.park() 把线程挂起阻塞


### 有序性： 指令重排

#### 谁把指令重排了
1. 编译器（javac, JIT)
2. CPU乱序执行
   
关于指令重排有个经典案例，就是单例模式的创建：

#### 经典的懒汉模式，DCL 双重校验锁
```java
public class LazyMode {

    private static volatile LazyMode instance; // volatile的目的是禁止指令重排

    private LazyMode() {

    }

    public static LazyMode getInstance() {
        if (instance == null) {
            synchronized (LazyMode.class) {
                if (instance == null) {
                    instance = new LazyMode(); // 创建对象有三个步骤：1. 分配内存空间；2. 初始化对象（给成员变量赋值）；3.instance 指向内存地址. 如果指令重排了。将1和3 先做完，
                    // 其他线程拿到的只是成员变量还是空的对象，在后面的方法调用或其他操作中可能会出现空指针异常
                    // 所以 instance一定要加volatile 禁止指令重排
                }
                return instance;
            }
        } else {
            return instance;
        }
    }
}

```

饿汉模式的好处是，程序不一定会用到这个对象，可以节省空间

#### 饿汉模式，饿了先吃，管它用不用先创建了再说， 缺点是占空间

```java
public class HungryMode {

    private static final HungryMode instance = new HungryMode(); // final 变量保证instance不会被外界修改， static 保证在类加载的时候只初始化一次

    private HungryMode() {

    }

    public static HungryMode getInstance() {
        return instance;
    }
}

```

### 类加载

### 什么时候类加载
主动使用就会触发类加载机制
#### 1. new 实例对象
```java
new User()
```
#### 2. 调用类的静态方法
```java
User.test()
```
#### 3. 访问类的静态常量以外的静态变量
```java
User.age
```
#### 4. 反射
```java
Class.forName("xxx.User")
```
#### 5. 初始化子类，一定会先加载父类
子类一初始化，父类必先完成加载初始化

### 类加载过程
加载 → 验证 → 准备 → 初始化

成员变量（非 static）：创建对象 new 的时候才初始化
静态变量（static）：类加载初始化阶段就初始化，跟有没有 new 对象无关。

## Java锁

### 锁的分类

#### 悲观锁 & 乐观锁

悲观锁：拿不到锁 就挂起(blocked, waiting, timed_waiting). 比如：synchronized, ReentrantLock

乐观锁：拿不到锁 不挂起，一直尝试 比如：CAS

如果尝试几次就能成功就用乐观锁，如果获取锁需要一定时间，就用悲观锁

#### 可重入锁 & 不可重入锁

#### 公平锁和非公平锁

公平锁，等待的线程越久越容易拿到锁
非公平锁，不根据等待时间判断是否拿到锁， synchronized， ReentrantLock支持公平和非公平

#### 互斥锁 & 共享锁
互斥锁就是写锁，线程得拿到锁才能操作，同一个时间只有一个线程能拿到锁， Java中synchronized和ReentrantLock都是互斥锁

如果业务需要加锁，但是业务时读多写少，允许多个线程同时持有锁，如果有写操作，所有得读操作都不能持有锁

写线程要等所有读线程做完，才能拿锁 去写

Java 中ReentrantReadWrite就是一个共享锁，或者说是一个共享锁

### CAS
CAS CPU层面保证了同一层面只有一个线程在做Comapre ans swap，比较和交换时CPU的同一条并发原语

#### 基于CAS的工具类
AtomicInteger, 内部有一个volatile修饰的value值， 记录当前值的大小，保证了多线程并发的可见性和有序性，内部用CAS修改值，保证了在CPU层面只有一个线程执行这个比较和交换操作，保证了原子性

#### CAS三个问题
1. ABA问题
   用AtomicStam
2. 自旋次数过多
3. 只能保证一个变量的原子性

### 类锁和对象锁

static 方法是基于类锁

非static 方法是基于对象锁

### javap 反编译命令

### synchronized 基于对象实现的锁
### synchronized锁优化

#### 锁消除
编译器在编译时发现加synchronized没有意义，就消除锁的操作

#### 锁膨胀
编译JIT做的一个优化，用于扩大锁的范围，从而避免频繁加锁操作，比如在循环里加synchronized锁资源，编译器会把synchronized锁膨胀到for循环之外，避免了频繁加锁操作，大大提高了效率

#### 锁升级
有点类似ReentrantLock, 但不一样，ReentrantLock内部会基于CAS尝试那所，拿不到所，再去排队，排队过程中可能将线程挂起

synchronized在jdk1.5 比较巴普里，那所成功，操作，拿锁失败，挂起，性能较差

锁升级优化，会有一定的CAS尝试

synchronized锁升级有几个阶段：无锁/匿名偏向，偏向锁，轻量级锁，重量级锁

1. 无锁/匿名偏向： 当前对象没有作为锁资源
2. 偏向锁：当前锁资源，只有一个线程来索取，这个线程反复来拿锁，就不需要竞争了
   * 如果偏向的线程不是当前要获取锁的线程
        * * 如果偏向锁被持有，导致获取锁的线程拿锁失败，做锁升级
        * * 如果偏向锁没有被持有，将偏向线程改为要获取锁的线程
3. 轻量级锁：会采用自旋锁 多次执行CAS，获取锁资源（自适应锁）
   * 如果CAS失败，达到自旋阙值，做锁升级
4. 重量级锁：传统syn锁方式，拿锁成功，操作，拿锁失败，挂起
#### synchronized 锁升级的核心目的：
在不丧失线程安全的前提下，尽量少用重量级互斥阻塞，先从轻量级、无锁、自旋过渡，减少内核态阻塞、减少上下文切换，提升性能。
不是单纯给拿不到锁做调整，是 JVM 根据竞争激烈程度，自动适配「最优加锁策略」。

#### MarkWord 随着锁升级存哪些内容

```
无锁状态：内部正常存储对象信息，hashcode,分代年龄，锁标记位：001
匿名偏向锁： 没有指向线程，但锁标记为是101
偏向锁：内部没有地方存hashcode了，存储线程的标识， 锁标记位: 101
轻量级锁：内部直接存储了Lock Record 地址， Lock Record存储了对象信息，锁标记位:00
重量级锁: 内部直接存储了ObjectMonitor地址， ObjectMonitor存储了对象信息，锁标记位：10
```

#### 偏向锁
偏向锁在jdk8-jdk14默认开启，且默认延迟4秒，-XX:BiasedLockingStartupDelay=4000
JDK15-JDK19 偏向锁默认关闭 -XX:-UseBiasedLocking （注意-代表关闭）
jdk20-jdk21 偏向锁彻底删除
锁升级路径简化：
无锁 -> 轻量级锁 -> 重量级锁

删除原因：高并发下，偏向锁撤销代价很大，现在业务都是多线程竞争，单线程的很少，简化synchronized锁，偏向锁维护成本太高，收益低

#### 锁升级时机（无锁(01) → 轻量级(00) → 重量级(10)）
1. 无锁 → 轻量级锁（第一次抢锁）<br/>
对象刚创建：无锁（01)
线程 T1 第一次进入 synchronized (obj)：在当前线程栈帧创建 Lock Record；
用 CAS 把对象头 Mark Word 改成指向该 Lock Record 的指针；
成功 → 轻量级锁（00），进入临界区。
触发时机：第一次有线程加锁， 多个线程交替加锁，不在同一时间抢锁

1. 轻量级锁 → 重量级锁（竞争加剧）<br/>
轻量级锁是：用户态 CAS + 自适应自旋，不进内核、不阻塞。
升级到重量级锁（10，ObjectMonitor）的典型触发条件：
* 自旋次数超限 / 自适应自旋失败, JDK 21 用自适应自旋：根据历史锁等待时长动态调整自旋次数（默认几十次）；自旋完还抢不到 → 升级。
* 有两个个及以上线程同时来并发竞争 轻量级适合少量线程交替；竞争线程变多，自旋风暴浪费 CPU → 升级。
* 持锁线程在临界区内阻塞 / 休眠 如 sleep()、wait()、IO 阻塞等；其他线程自旋等不到，必须阻塞 → 升级。
* 调用 wait() / notify(), 只有 ObjectMonitor（重量级锁） 才能管理等待队列；一旦 wait()，直接膨胀为重量级锁。

### ReetrantLock
基于AQS（AbstractQueuedSynchronizer）抽象类实现的锁机制

#### state属性
```java
    private volatile int state;
//当state为0的时候，说明没有线程持有当前资源，如果有线程持有，state必然是大于0的
```


#### 双向链表
Node类 实现双向链表
```java
  abstract static class Node {
        volatile Node prev;       // initially attached via casTail
        volatile Node next;       // visibly nonnull when signallable
        Thread waiter;            // visibly nonnull when enqueued
        volatile int status;      // written by owner, atomic bit ops by others

        // methods for atomic operations
        final boolean casPrev(Node c, Node v) {  // for cleanQueue
            return U.weakCompareAndSetReference(this, PREV, c, v);
        }
        final boolean casNext(Node c, Node v) {  // for cleanQueue
            return U.weakCompareAndSetReference(this, NEXT, c, v);
        }
        final int getAndUnsetStatus(int v) {     // for signalling
            return U.getAndBitwiseAndInt(this, STATUS, ~v);
        }
        final void setPrevRelaxed(Node p) {      // for off-queue assignment
            U.putReference(this, PREV, p);
        }
        final void setStatusRelaxed(int s) {     // for off-queue assignment
            U.putInt(this, STATUS, s);
        }
        final void clearStatus() {               // for reducing unneeded signals
            U.putIntOpaque(this, STATUS, 0);
        }

        private static final long STATUS
            = U.objectFieldOffset(Node.class, "status");
        private static final long NEXT
            = U.objectFieldOffset(Node.class, "next");
        private static final long PREV
            = U.objectFieldOffset(Node.class, "prev");
    }
```
#### 单向链表
ConditionNode 通过nextWaiter
```java
static final class ConditionNode extends Node
        implements ForkJoinPool.ManagedBlocker {
        ConditionNode nextWaiter;            // link to next waiting node

        /**
         * Allows Conditions to be used in ForkJoinPools without
         * risking fixed pool exhaustion. This is usable only for
         * untimed Condition waits, not timed versions.
         */
        public final boolean isReleasable() {
            return status <= 1 || Thread.currentThread().isInterrupted();
        }

        public final boolean block() {
            while (!isReleasable()) LockSupport.park();
            return true;
        }
    }
```

#### ReentrantLock加锁流程

非公平锁：
* 场景： 线程A，线程B尝试获取lock资源；
* 线程A先执行了加锁，执行CAS, 尝试将ReentrantLock中的state从0改为1， 并且成功拿到锁资源，lock记录持锁线程；
```java
public abstract class AbstractOwnableSynchronizer
    implements java.io.Serializable {
            private transient Thread exclusiveOwnerThread;

}
```
* 线程B尝试加锁，执行CAS，尝试将Lock中的state 从0改为1，失败了；
* 线程B再次尝试拿锁，这次不会尝试修改state，先看state是否为0；
* 如果是0，再次尝试将state从0改为1，如果成功拿锁走人，如果失败，排队；
* 如果不为0，持有锁的线程是否是自己，如果是走可重入流程，如果不是拿锁失败，准备排队；
* 将线程B封装为NodeB，添加到AQS双向链表中,如果没有head 就创建一个
* 线程B进入到排队队列后，如果自己就是head.next,再次走抢锁流程， 如果不是，挂起线程
* 线程A释放锁资源后，会让head查看有没有需要唤醒的Node
* 如果有，唤醒head.next的Node
* 如果没有，啥事不做

公平锁：