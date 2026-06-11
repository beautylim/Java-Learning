# 1. 内部类

## 成员内部类
<details>
<summary>点击查看代码</summary>

```java
package org.example.innerclass;

import java.util.Random;

public class InnerClass01 {

    static String name;

    private int age;

    public void setName(String _name) {
        name = _name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    IdCard idCard = new IdCard();

    public IdCard getIdCard() {
        return idCard;
    }

    // 成员内部类: 无法从外界初始化该类
    class IdCard{
        int id = new Random().nextInt(10);
        public void innerFunction() {
            System.out.println("name: " + name + ", age: " + age + ", id: " + id);
        }
    }

    static void main(String[] args) {
        InnerClass01 innerClass01 = new InnerClass01();
        innerClass01.setName("成员内部类");
        innerClass01.setAge(18);
        innerClass01.getIdCard().innerFunction();

        //name: 成员内部类, age: 18, id: 0
    }
}

```
</details>


## 局部内部类

<details>
<summary>点击查看代码</summary>

```java

package org.example.innerclass;

import java.util.Random;

public class InnerClass02 {

    static String name;

    int age;

    public void setName(String _name) {
        name = _name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    static void main(String[] args) {

        class IdCard{
            int id = new Random().nextInt(10);
            public void innerFunction() {
                System.out.println("name: " + name + ", id: " + id ); // 不能访问 非static 成员变量 age
            }
        }

        InnerClass02 innerClass02 = new InnerClass02();
        innerClass02.setName("test");
        innerClass02.setAge(18);
        IdCard idCard = new IdCard();
        idCard.innerFunction();

        InnerClass02 innerClass02_01 = new InnerClass02();
        innerClass02_01.setName("测试");
        innerClass02_01.setAge(18);
        IdCard idCard2 = new IdCard();
        idCard2.innerFunction();

//        name: test, id: 1
//        name: 测试, id: 7

    }


}

```
</details>

## 静态内部类
<details>
<summary>点击查看代码</summary>

```java
package org.example.innerclass;

import java.util.Random;

public class InnerClass02 {

    static String name;

    int age;

    public void setName(String _name) {
        name = _name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    static void main(String[] args) {

        class IdCard{
            int id = new Random().nextInt(10);
            public void innerFunction() {
                System.out.println("name: " + name + ", id: " + id ); // 不能访问 非static 成员变量 age
            }
        }

        InnerClass02 innerClass02 = new InnerClass02();
        innerClass02.setName("test");
        innerClass02.setAge(18);
        IdCard idCard = new IdCard();
        idCard.innerFunction();

        InnerClass02 innerClass02_01 = new InnerClass02();
        innerClass02_01.setName("测试");
        innerClass02_01.setAge(18);
        IdCard idCard2 = new IdCard();
        idCard2.innerFunction();

//        name: test, id: 1
//        name: 测试, id: 7

    }


}

```
</details>
   
## 匿名内部类 <重点>
<details>
<summary>点击查看代码</summary>

```java
package org.example.innerclass;

public class InnerClass04 {

    static void main(String[] args) {

        MyCard myCard = new MyCard() { // 匿名内部类，不需要指定接口的实现类名称
            @Override
            public void innerFunction(String name, int age) {
                System.out.println("name: " + name + ", age: " + age);
            }
        };
        
        myCard.innerFunction("test", 18); // name: test, age: 18
        
    }
}

```
</details>



# 2. lambda

关注 参数和具体操作，不关注对象名称和方法名称

匿名内部类可以用lambda代替
<details>
<summary>点击查看代码</summary>

```java
package org.example.stream;

public class Demo00 {

    static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("匿名内部类调用结束");
            }
        }
        ).start();  //匿名内部类调用结束

        new Thread(() -> System.out.println("lambda调用结束")
        ).start();  //lambda调用结束
    }
}

```
</details>

# 3. Stream

位于java.util
利用了lambda
对集合有很多方便操作，省去大量嵌套和冗余代码



## 创建流
### 串行流
默认是串行流，对流元素的操作是串行执行的
### 并行流
执行时将流元素分割成几个部分，并行执行，提高效率

```java
Arrays.stream(new int[] {1,2,3,4});
list.stream()
list.parallelStream()
Stream.of(1,2,3,4);

```

## 中间操作
### filter
用来过滤集合中的元素，lambda是Predicate

### map
用来转换集合中的元素，转换成另一种类型
### distinct
去除集合中重复元素，通过元素类型的equals方法判断是否相等

### sorted
如果元素类型不实现Comaprable接口，用sorted加lambda，lambda是Comparator<T>, 升序是left - right, 降序是right-left
如果元素类型实现了Comparable,直接用sorted()


### limit
限制流的长度

### skip
跳过几个元素，通常配合sorted使用，先排序，再跳过排名最前的某几个元素，比如：跳过年龄最大的作者，就是先作者集合按年龄递减排序，然后skip(1)

### flatMap
将集合中的元素，转化成一个一个stream 再合并， 比如：查询所有作者下的所有书籍

### peek
作为中间操作可以消费流元素，而且还会返回流，不会终止流，适合中间打印log使用
```java
List<String> pathList = List.of("/root/test.txt", "/home/admin/admin.txt", "/data01/data/dummy/pkg");
List<File> fileList = pathList.stream().peek(System.out::println).map(File::new).toList();
```

## 终结操作

### foreach
遍历流中的所有元素， lambda是Consumer

### count
计算流元素的个数， 比如统计一下所有作家一共写了多少本书

### min & max
分别统计作家集合中作家能写书的最大数量和最小数量， 需要传入lambda, lambda 是Comparator\<T>, 结果是Optional\<T>

### toArray
将流转成数组
```java
authors.stream().map(Author::getName).toArray(String[]::new);
```

### toList
将流转成一个list集合
```java
.toList() // from java 16
```
### collect
将当前流转换成一个集合

#### 转成set
```java
.collect(Collectors.toSet());
```
#### 转成map
将作家集合转成 key为作家名称，value为作家所写书的数量
```java
Map<String, Integer> map = authors.stream()
                .collect(Collectors.toMap(Author::getName, author -> author.getBooks().size()));

```

#### 转成字符串
将所有作家名字返回并以逗号作为分割符隔开
```java
authors.stream().map(Author::getName).collect(Collectors.joining(","))
```
### reduce
将流中的数据，按照指定计算方式得出一个结果， 我们可以传入一个初始值，它会按照我们给的计算方式依次拿流中的元素和在初始值的基础上计算，最终得出一个结果
比如计算所有作家的年龄加一起一共多少岁
```java
authors.stream().map(author -> author.age)
                .reduce(0, Integer::sum);
```
统计作家集合中最大年龄
```java
uthors.stream().map(author -> author.getAge())
                .reduce(0, ((integer, integer2) -> integer < integer2? integer2 : integer));
```
如果不给reduce 设初始值的话，reduce 会默认将集合中的第一个元素设为初始值，如果一个元素都没有将返回空值，所以最终reduce不带初始值的返回结果是Optional类型的


## 终结操作之查找与匹配

### anyMatch

### allMatch

### findAny

### findFirst

## 高级用法
### IntSummaryStatistics/LongSummaryStatistics/DoubleSummaryStatistics
统计所有作家年龄的信息:
```java
IntSummaryStatistics intSummaryStatistics = authors.stream()
        .collect(Collectors.summarizingInt(Author::getAge));
//最大年龄
System.out.println(intSummaryStatistics.getMax());
//最小年龄
System.out.println(intSummaryStatistics.getMin());
//平均年龄
System.out.println(intSummaryStatistics.getAverage());
//年龄总和
System.out.println(intSummaryStatistics.getSum());
//年龄数目
System.out.println(intSummaryStatistics.getCount());
```

### 分组和分区
#### groupBy 分组
将相同特性的流数据聚集在一起分成组, 组内元素组成一个列表，可以对组内列表进行各种操作比如统计数量，计算平均值，过滤，转成String,转成Set等等。
统计一个数字集合中，每个数字出现的频率
``` java
List<Integer> list = Arrays.asList(1,2,3,2,3,3);
Map<Integer, Long> map2 = list.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
```

#### partioningBy 分区
分区只能根据布尔值分成两组，true或者false,分完区 也会有一个List列表，可以对区内列表进行操作
统计含有武侠和不含有武侠小说的书本数量
```java
Map<Boolean, Long> collect = authors.stream()
        .flatMap(author -> author.getBooks().stream())
        .map(book -> book.getTypes())
        .collect(Collectors.partitioningBy(type -> type.contains("武侠"), Collectors.counting()))
```


## 注意事项
* 流是惰性求值，没有终结操作，只有中间操作时不会执行的
* 流是一次性的，一旦流经过一个终结操作后，这个流就不能再被使用
* 不会影响原数据

# 4. Optional
方便判断null值，减少NPE异常

## 创建Optional

### Optional.ofNullable()
可以传空值，当不确定值是否为空时 用ofNullable()方法

```java
Optional<String> optional = Optional.ofNullable("123");
Optional<String> optional2 = Optional.ofNullable(null);
```

### Optional.of()
不可以传空值, 否则会抛出NPE异常

### Optional.empty()
确定是空，可以使用Optional.empty()

```java
private static Optional<String> testExisting(String name) {
    return name == null ? Optional.empty() : Optional.of(name);
}
```

## 使用Optional

### orElse
如果Optional有值返回值，如果没有，设置一个default值

### orElseGet
如果Optional有值返回值，如果没有，通过一个Supplier lambda表达式赋值

### ifPresent
如果Optional有值则操作这个值，lambda是Consumer,无返回值

### map
如果Optional有值则操作这个值，有返回值，返回值也是Optional, 注意这个值是普通参数，不是Optional类型的值

### flatMap
这是专门为解决嵌套Optional自动判断Optional值是否为空准备的， 层层拆箱，自动判断Optioanl值是否为空。

比如: 一个Optional对象与它的成员变量层层嵌套Optional, 就可以用flatMap方便层层拆套
```java
public class User {
    Optional<Address> address;
}

public class Address {
    Optional<String> city;
}

Optional<String> city = Optional.of("shanghai");
Address address = new Address();
address.city = city;
Optional<Address> optionalAddress = Optional.of(address);
User user = new User();
user.address = optionalAddress;
Optional<User> optionalUser = Optional.of(user);
optionalUser.flatMap(user1 -> user1.address)
        .flatMap(address1 -> address1.city)
        .ifPresent(System.out::println);
```

### 使用Optional 优雅判断true, false
```java
boolean existing = false;
Optional.ofNullable(existing).filter(Boolean::valueOf).ifPresent(flag -> System.out.println("存在"));
```

### filter
如果Optional有值，对值做filter判断，过滤出需要的值

```java
Optional<Integer> name = Optional.of(3);
        name.filter(integer -> integer < 5 ).ifPresent(System.out::println);
```

# 5. 函数式接口
用 @FunctionalInterface 修饰的接口
比如Consumer，Supplier，Runnable, Predicate, Function

函数式接口有一些默认方法，比如Predicate的or方法和and方法
```java
authors.stream()
        .filter(new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.getAge() > 17;
            }
        }.or(new Predicate<Author>() {
            @Override
            public boolean test(Author author) {
                return author.getBooks().size() > 5;
            }
        })).forEach(System.out::println);
```
可以看到这些默认方法只对匿名类有效，对lambda是访问不到的


# 6. 方法引用
在用lambda表达式定义操作的时候，如果操作只有一行方法调用，可以使用语法糖方法引用来进一步简化代码

## 基本格式
```
类名或者对象名::方法名
```

## 引用类的静态方法
比如：将一个内容为数字的字符串列表 转换成int类型的列表
```java
List<String> list = List.of("1","2","3","4","5");
List<Integer> list1 = list.stream().map(Integer::parseInt).toList();
```

## 引用对象的实例方法
比如：给每个作家颁奖
```java
Award award = new Award();
authors.stream().forEach(award::give);
```


## 引用类的实例方法
比如：将作家列表，转换成只有作家名字的列表

```java
authors.stream().map(Author::getName).toList();
```

## 构造器引用
格式：
```
类名::new
```
比如在文件路径集合中，根据文件路径创建文件
```java
List<String> pathList = List.of("/root/test.txt", "/home/admin/admin.txt", "/data01/data/dummy/pkg");
List<File> fileList = pathList.stream().map(File::new).toList();
```