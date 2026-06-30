# Mybatis

## Maven
```xml
<dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>annotationProcessor</scope>
        </dependency>

        <!--        mybatis dependencies-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>4.0.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>4.0.0</version>
            <scope>compile</scope>
        </dependency>

    </dependencies>
```

## 配置文件
```yaml
spring:  
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/student_db?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&allowMultiQueries=true
    username: root
    password: 123456
    type: com.zaxxer.hikari.HikariDataSource

mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: org.example.mybatisdemo.model.entity
  configuration:
    map-underscore-to-camel-case: true
    lazy-loading-enabled: true

logging:
  level:
    org.example.mybatisdemo.mapper: DEBUG
```

## Mapper
```java
public interface StudentMapper {

    Student getStudentById(Long id); //多对一，查询学生以及对应的班级

    List<Student> getStudentsByClassId(Long cid);

    List<Student> selectAll();
}

```

## MapperScan
```java
@SpringBootApplication
@MapperScan("org.example.mybatisdemo.mapper")
@EnableTransactionManagement
public class MybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisApplication.class, args);
    }
}

```

## Mapper.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.example.mybatisdemo.mapper.StudentMapper">

    <resultMap id="studentResultMap" type="org.example.mybatisdemo.model.entity.Student">
        <id property="id" column="sid"></id>
        <result property="name" column="sname"></result>
        <result property="birthday" column="birthday"></result>
        <result property="status" column="sstatus"></result>
        <result property="createTime" column="screate_time"></result>
<!--        <association property="clazz" javaType="org.example.mybatisdemo.model.entity.Clazz">-->
<!--            <id property="id" column="cid"></id>-->
<!--            <result property="name" column="cname"></result>-->
<!--            <result property="status" column="cstatus"></result>-->
<!--            <result property="createTime" column="ccreate_time"></result>-->
<!--        </association>-->

        <association property="clazz" select="org.example.mybatisdemo.mapper.ClazzMapper.getClazzById" column="cid"/>
    </resultMap>
    <select id="getStudentById" resultMap="studentResultMap" parameterType="java.lang.Long">
        select id sid, name sname, birthday birthday, clazz_id cid,  status sstatus, create_time screate_time from student where id = #{id}
    </select>
    <select id="getStudentsByClassId" resultType="org.example.mybatisdemo.model.entity.Student"
            parameterType="java.lang.Long">
        select id, name, birthday, status, create_time from student where clazz_id = #{cid}
    </select>
    <select id="selectAll" resultType="org.example.mybatisdemo.model.entity.Student">
        select id, name, birthday, status, create_time from student
    </select>

    <!--    <select id="getStudentById" resultMap="studentResultMap" parameterType="java.lang.Long">-->
<!--        select s.id sid, s.name sname, s.birthday birthday,  s.status sstatus, s.create_time screate_time, c.id cid, c.name cname, c.status cstatus, c.create_time ccreate_time-->
<!--        from student s-->
<!--        left join clazz c-->
<!--        on s.clazz_id = c.id-->
<!--        where s.id = #{id}-->
<!--    </select>-->
</mapper>
```

## 动态SQL

### if 可以执行多个分支
```xml
<select id="findShops" resultType="org.example.shop.model.entity.Shop">
        select id, shopName, address,logoPath,coverPath,tags, score from shop
        <where>
            <if test="name != null and name != ''">
                shopName = #{name}
            </if>
            <if test = "address != null and address !=''">
                and address like concat('%', #{address}, '%')
            </if>
        </where>
    </select>
```

### where 可以动态处理多个分支的and连接
```xml
<select id="findShops" resultType="org.example.shop.model.entity.Shop">
        select id, shopName, address,logoPath,coverPath,tags, score from shop
        <where>
            <if test="name != null and name != ''">
                shopName = #{name}
            </if>
            <if test = "address != null and address !=''">
                and address like concat('%', #{address}, '%')
            </if>
        </where>
    </select>
```
### set 可以在update时，不更新null值
```xml
<update id="updateShopIgnoreNull" parameterType="org.example.shop.model.entity.Shop">
        update shop
        <set>
            <if test="shopName != null and shopName != ''">
                shopName = #{shopName},
            </if>
            <if test="address != null and address != ''">
                address = #{address},
            </if>
            <if test="logoPath != null and logoPath != ''">
                logoPath = #{logoPath},
            </if>
            <if test="coverPath != null and coverPath != ''">
                coverPath = #{coverPath},
            </if>
            <if test="tags != null and tags != ''">
                tags = #{tags},
            </if>
            <if test="score != null and score != ''">
                score = #{score},
            </if>
        </set>
        where id = #{id}
    </update>
```

### foreach 可以在插入 查询 删除中动态拼接集合里的元素
```xml
    <insert id="insertShops" >
    insert into shop (shopName, address, logoPath, coverPath, tags, score)
    values 
        <foreach collection="shops" item="shop" separator=",">
            (#{shop.shopName}, #{shop.address}, #{shop.logoPath}, #{shop.coverPath}, #{shop.tags}, #{shop.score})
        </foreach>
    </insert>
    <select id="findShopsByIds" resultType="org.example.shop.model.entity.Shop">
        select id, shopName, address,logoPath,coverPath,tags, score from shop
        where id in
        <foreach collection="ids" item="id" open="(" close=")" separator=",">
            #{id}
        </foreach>
    </select>
```

### trim 动态添加prefix，suffix 以及动态删除preifix 和suffix，同样可以用在where 条件中
```xml
    <select id="findShopsComplex" resultType="org.example.shop.model.entity.Shop">
        select id, shopName, address,logoPath,coverPath,tags, score from shop
        <trim prefix="where" suffixOverrides="and|or">
            <if test="name != null and name != ''">
                shopName like concat('%', #{name}, '%')  and
            </if>
            <if test = "address != null and address !=''">
                address like concat('%', #{address}, '%') and
            </if>
            <if test="tags != null and tags !=''">
                tags like concat('%', #{tags}, '%') and
            </if>
        </trim>
    </select>
```
### case when otherwise 特点是只能走一个分支
```xml
    <select id="findShopsOnlyFromOneCondition" resultType="org.example.shop.model.entity.Shop">
        select id, shopName, address,logoPath,coverPath,tags, score from shop
        <where>
            <choose>
                <when test="name != null and name != ''">
                    shopName like concat('%', #{name}, '%')
                </when>
                <when test="address != null and address != ''">
                    address like concat('%', #{address}, '%')
                </when>
                <otherwise>
                    tags like concat('%', #{tags}, '%')
                </otherwise>
            </choose>
        </where>
    </select>
```

# 多对一的数据结果集
例如学生班级，查询一个学生以及所在的班级信息，在Model中就是
```java
@Data
public class Student {
    Long id;
    String name;
    Date birthday;
    Clazz clazz; //一个学生对应一个班级
    int status; //0 正在上学,1 已经毕业, 2休学肄业
    Date createTime;
}

```

如何将班级信息赋值给clazz属性呢，定义resultMap, 通过association select 实现延迟加载和数据映射

```xml
<resultMap id="studentResultMap" type="org.example.mybatisdemo.model.entity.Student">
        <id property="id" column="sid"></id>
        <result property="name" column="sname"></result>
        <result property="birthday" column="birthday"></result>
        <result property="status" column="sstatus"></result>
        <result property="createTime" column="screate_time"></result>
<!--        <association property="clazz" javaType="org.example.mybatisdemo.model.entity.Clazz">-->
<!--            <id property="id" column="cid"></id>-->
<!--            <result property="name" column="cname"></result>-->
<!--            <result property="status" column="cstatus"></result>-->
<!--            <result property="createTime" column="ccreate_time"></result>-->
<!--        </association>-->

        <association property="clazz" select="org.example.mybatisdemo.mapper.ClazzMapper.getClazzById" column="cid"/>
    </resultMap>
    <select id="getStudentById" resultMap="studentResultMap" parameterType="java.lang.Long">
        select id sid, name sname, birthday birthday, clazz_id cid,  status sstatus, create_time screate_time from student where id = #{id}
    </select>
```

# 一对多的数据结果集
例如查询一个班里的所有学生， model:
```java
@Data
public class Clazz {
    Long id;
    String name;
    int status; // 0 正在上学, 1已毕业
    Date createTime;
    List<Student> students; // 一对多，一个班级对应多个学生
}
```
也是定义一个resultMap, 使用collection select，实现延迟加载以及数据映射，把学生数据映射成List
```xml
<resultMap id="clazzResultMap" type="org.example.mybatisdemo.model.entity.Clazz">
        <id property="id" column="cid"></id>
        <result property="name" column="cname"></result>
        <result property="status" column="cstatus"></result>
        <result property="createTime" column="ccreate_time"></result>
<!--        <collection property="students" ofType="org.example.mybatisdemo.model.entity.Student">-->
<!--            <id property="id" column="sid"></id>-->
<!--            <result property="name" column="sname"></result>-->
<!--            <result property="birthday" column="birthday"></result>-->
<!--            <result property="status" column="sstatus"></result>-->
<!--            <result property="createTime" column="screate_time"></result>-->
<!--        </collection>-->
        <collection property="students" select="org.example.mybatisdemo.mapper.StudentMapper.getStudentsByClassId" column="cid">

        </collection>
    </resultMap>
    <select id="getClazzAndStudentById" resultMap="clazzResultMap" parameterType="java.lang.Long">
--         select c.id cid, c.name cname, c.status cstatus, c.create_time ccreate_time, s.id sid, s.name sname, s.birthday, s.status sstatus, s.create_time screate_time
--         from clazz c
--                  join student s
--                       on c.id = s.clazz_id
--         where c.id = 1
        select c.id cid, c.name cname, c.status cstatus, c.create_time ccreate_time from clazz c where id = #{id}
    </select>
```

# 延迟加载
开启
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: org.example.mybatisdemo.model.entity
  configuration:
    lazy-loading-enabled: true
```
搭配association select 或者 collection select 这样就能实现访问属性的时候才执行select语句

# Json映射
如果字段存的是json，要映射成一个对象：
## 新增typehandler 类
```java
package org.example.shop.typehandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.example.shop.model.dto.Cover;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes({Cover.class, Object.class})
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Class<T> type;

    public JsonTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
        try {
            ps.setString(i, mapper.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("JSON序列化失败", e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private T parseJson(String json) throws SQLException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (Exception e) {
            throw new SQLException("JSON反序列化失败", e);
        }
    }
}
```
## 在application.yml中增加配置
```yaml
mybatis:
  type-handlers-package: org.example.shop.typehandler  # 扫描包路径
```



# 缓存

* 一级缓存： 将查询到的数据存储到SqlSession中
* 二级缓存： 将数据存储到SqlSessionFactory中 现在很少用mybatis的缓存

## 一级缓存

默认开启，只要使用同一个SqlSession对象执行同一条SQL语句，就会走缓存，将方法加上Transaction注解， 就能用缓存，因为在事务中 只用一个SqlSession, 如果不开启事务，每一次Mapper的调用都是新的SqlSession
```java
    @Test
    @Transactional
    void getClazzAndStudentById() {
        Clazz clazz = clazzMapper.getClazzAndStudentById(1L);
        System.out.println(clazz.getName());
        clazz = clazzMapper.getClazzAndStudentById(1L);
        System.out.println(clazz.getStudents().size());
    }
```

## 二级缓存时SqlSessionFactory的缓存
要开启的话 要在config xml中加上Cache标签或者在yaml文件中配置mybatis:configuration:cache-enabled:true

类似于Redis, Redis更灵活 所以现在都用Redis缓存了

# 分页查询

直接上PageHelper 一款国人开发的插件非常好用，支持分页以及排序
```java
@Test
    public void testFindStudentByName() {
        PageHelper.startPage(2, 3).setOrderBy("name asc");
        List<Student> students = studentMapper.selectAll();
        PageInfo<Student> pageInfo = new PageInfo<>(students);
        System.out.println(pageInfo);
    }
```

