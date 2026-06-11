# 反射

反射就是 加载类，并允许以编程的方式获取类的各种信息（成员变量，方法，构造器等）

## 反射的步骤
1. 加载类，获取类的字节码，生成类对象 Class
2. 获取类的构造器：Constructor 对象
3. 获取类的成员变量: Field对象
4. 获取类的成员方法：Method 对象

## 获取Class对象

```java
// 通过类名.class
Class studentClass = Student.class;
System.out.println(studentClass); // class org.example.reflection.Student
System.out.println(studentClass.getSimpleName()); // Student

// 通过Class.forName
Class studentClass1 = Class.forName("org.example.reflection.Student");
System.out.println(studentClass1);
System.out.println(studentClass == studentClass1); // true

// 通过对象
Student student = new Student();
Class studentClass2 = student.getClass();
System.out.println(studentClass2);
System.out.println(studentClass1 == studentClass2); // true
```

## 获取类的构造器

### 获取全部构造器: .getDeclaredConstructors
```java
@Data
@AllArgsConstructor
public class Student {
    private int id;
    private String name;
    private String grade;

    private Student() {}

    public Student(String name) {
        this.name = name;
    }
}

// 先获取类多谢
Class student = Student.class;
Constructor[] constructors = student.getConstructors(); // 只能拿public的构造器
Arrays.stream(constructors)
        .forEach(constructor -> System.out.println("构造器名称: " + constructor.getName() + ", 构造器参数: " + constructor.getParameterCount()));

Constructor[] constructorList = student.getDeclaredConstructors(); // 可以获取全部构造器
Arrays.stream(constructorList)
        .forEach(constructor -> System.out.println("构造器名称: " + constructor.getName() + ", 构造器参数: " + constructor.getParameterCount()));

```

### 根据构造器参数类型，精确获取某个构造器: .getDeclaredConstructor
```java
Class studentClass = Student.class;
Constructor constructor = studentClass.getDeclaredConstructor(); // 获取无参构造器
System.out.println("构造器名称: " + constructor.getName() + ", 构造器参数: " + constructor.getParameterCount());

Constructor constructor2 = studentClass.getDeclaredConstructor(String.class);
System.out.println("构造器名称: " + constructor2.getName() + ", 构造器参数: " + constructor2.getParameterCount());
```

### 拿构造器的作用：通过构造器创建对象: .newInstance
```java
Class studentClass = Student.class;
Constructor constructor = studentClass.getDeclaredConstructor(); // 获取无参构造器
constructor.setAccessible(true); // 暴力反射，禁止检查访问控制
Student student = (Student) constructor.newInstance();

Constructor constructor2 = studentClass.getDeclaredConstructor(String.class); // 获取只有一个String类型参数的构造函数
Student student2 = (Student) constructor2.newInstance("小强"); // 通过该构造函数生成对象
```

## 获取类的成员变量：.getDeclaredFields
```java
Class studentClass = Student.class; // 获取类对象

Field[] fields = studentClass.getDeclaredFields(); //获取全部成员变量
Arrays.stream(fields).forEach(field -> System.out.println(field.getName() + ", " + field.getType()));

Field nameField = studentClass.getDeclaredField("name"); // 根据成员变量的名字获取成员变量
System.out.println(nameField.getName() + ", " + nameField.getType());

Student student = new Student("小强");
nameField.setAccessible(true); //禁止检查访问控制
System.out.println(nameField.get(student)); // 通过成员变量获取值，结果应该是小强
nameField.set(student, "小花"); // 给成员变量赋值， 要带一个对象
System.out.println(student.getName()); // 结果是小花
```

## 获取类的方法
```java
Class studentClass = Student.class; //获取类对象
Method[] declaredMethods = studentClass.getDeclaredMethods(); //获取全部的方法
Arrays.stream(declaredMethods).forEach(method -> System.out.println(method.getName() + " " + method.getParameterCount()));

Method learn = studentClass.getDeclaredMethod("learn", String.class); // 根据方法名称和参数类型，获取方法
Student student = new Student("小明");
learn.setAccessible(true);  // 禁止检查访问控制
learn.invoke(student, "数学"); //执行learn方法，需要提供对象和参数
```

## 实战
写一个方法 用反射 将任意类型的对象的成员变量和值输出到一个文件中
```java

@Data
public class Teacher {
    int id;
    String name;
    String courses;

    public void teach(String course) {
        System.out.println("Teaching " + course);
    }

    private void learn(String course) {
        System.out.println("Learning " + course);
    }

}

public class DemoSaveObject {

    static void main(String[] args) throws FileNotFoundException {

        Student student = new Student("小明");
        student.setId(1);
        student.setGrade("一年级");

        Teacher teacher = new Teacher();
        teacher.setId(10);
        teacher.setName("老李");
        teacher.setCourses("数学");

        saveObject(student);
        saveObject(teacher);

    }

    static void saveObject(Object object) throws FileNotFoundException {
        String output = "data.txt";
        try (PrintStream printStream = new PrintStream(new FileOutputStream(output, true))) { //一定要设置 append 为true
            Class<?> objectClass = object.getClass();
            printStream.println("-----------" + objectClass.getSimpleName() + "-----------");

            Field[] fields = objectClass.getDeclaredFields();
            Arrays.stream(fields)
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            printStream.println(field.getName() + "=" + field.get(object));
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}

```
