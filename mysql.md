# DDL 操作数据库，表结构， 不能事务回滚
创建数据库，表，操作数据库
数据库编码 默认utf8mb4,1 个汉字 = 3 字节，1 个字母 / 数字 = 1 字节.
utf8最大3个字节，不支持emoji和生僻汉字，utf8mb4 最大4个字节，完美兼容。
## 数据类型
* char 定长，可指定字符个数，比如存性别，不是男就是女，可以用char(1)表示，注意括号里的数字表示字符个数，不是字节个数
* varchar variable char,可边长，也可指定字符个数，比如姓名，如果是最多10个字，就可以用varchar(10)表示
* tinyint 1 bytes, 可以指定signed, unsigned
* smallint 2 bytes
* mediumint 3 bytes
* int 4 bytes
* bigint 8 bytes
* float 4 bytes
* double 8 bytes
* decimal 依赖于M精度和D标度
### 创建数据库
```
show databases/schema;
create database if not exist 数据库;
use 数据库;
select database();
drop database 数据库；
```

### 创建表
```
show tables;

create table emp (
	id int,
    workNo varchar(10),
    name varchar(10),
	gender char(1),
	age tinyint unsigned ,
	idCardNo char(18),
	enterDate date
) comment '员工表';

desc 表名；
show create table 表名;

```   

### 修改表
alter table 表名 add/change/modify/drop/rename to ...;
```
// 增加字段
alter table emp add nickname varchar(20) ;

// 修改数据类型
alter table emp modify

// 修改字段名和字段类型
alter table emp change nickname username varchar(30);

```

### 删除字段
```
alter table emp drop username;
```

### 修改表名
```
alter table emp rename to employee;
```

### 表删除
```
//删除表结构 + 数据 + 索引 + 约束
drop table if exists employee;

// 只清数据，表结构、索引、约束都保留， 会受约束限制
truncate table employee;
```


# DML 操作数据增删改 支持回滚

## insert

```sql
// 给指定字段添加数据
insert into 表名（字段1， 字段2） values(值1，值2)；

// 给全部字段添加数据
insert into 表名 values(值1, 值2，...);

// 批量添加数据
insert into 表名（字段1，字段2）values(值1， 值2),（值1，值2，...);
insert into 表名 values(值1，值2, ...), (值1， 值2, ...);

-- 添加员工
# insert into emp values (1,'No1', '张无忌', '男', 18, '123456789012345678', '2025-01-01'),
#                        (2,'No2', '金毛狮王', '男', 48, '123456789012345679', '2024-01-01');
```
## update

update 表名 set 字段名1=值1，字段名2=值2... [where 条件]；

```sql
update emp set name="张无忌" where id=1;

-- 将id为1的员工名字改为张无忌
#  update emp set name='张无忌', gender='男' where id=1;

-- 将id为2的员工名字改为赵敏，性别修改为女;
#  update emp set name='赵敏', gender='女' where id=2;

-- 将所有员工入职时间改为2020-01-01
# update emp set enterDate='2020-01-01';

```

## delete

delete from 表名 [where 条件]；

```
delete from emp where gender = '女';
```

# DQL 数据查询语言

```sql
select
    字段
from
    表名
where
    条件列表
group by
    分组字段列表
having
    分组后条件列表
order by
    排序字段列表
limit
    分页参数
```

## 基本查询
```sql
select distinct address '员工地址' from emp;
```
## 条件查询（where)

### 比较运算符

#### > 大于 >= 大于等于

#### <小于 <= 小于等于

#### = 等于

#### <> 或!= 不等于

#### between... and ... 在某个范围之内（含最小，最大值）

#### in(...) 在in之后的列表中的值，多选一
```sql
-- 统计年龄等于18 或 28的员工
select * from emp where age in (18, 28);
```

#### like 占位符 模糊匹配（_ 匹配单个字符，%匹配任意个字符）
```sql
-- 查询姓名为两个字的员工
select * from emp where name like '__';

-- 查询身份证最后一位为X的员工
select  * from emp where idCardNo like '%X';
select  * from emp where idCardNo like '_________________X';
```

#### is null 是null

### 逻辑运算符

#### and 或 &&

#### or 或 ||
```sql
-- 统计年龄等于18 或 28的员工
select * from emp where age = 18 or age = 28;
```

#### not 或 !

## 聚合函数(count,max,min,avg,sum)
将一列数据作为一个整体，进行纵向计算
语法, 所有的null 值不参与聚合运算
```sql
select 聚合函数（字段列表）from 表名;
```
### count 统计数量
```sql
-- 统计员工数量
select count(*) from emp;

```

### max 最大值
```sql
-- 统计员工最大年龄
select max(age) from emp;
```
### min 最小值
```sql
-- 统计员工最小年龄
select min(age) from emp;
```
### avg 平均值
```sql
-- 统计员工的平均年龄
select avg(age) from emp;
```
### sum 求和
```sql
-- 统计所有女员工年龄之和
select sum(age) from emp where gender = '女';
```

## 分组查询(group by, having)

### 1. 语法
``` sql
select 字段列表 from 表名 [where 条件] group by 分组字段名 [having 分组后过滤条件]
```
### 2. where 与having区别
* **执行时机不同**：where是分组之前进行过滤， 不满足where条件，不参与分组; 而having 是分组之后对结果进行过滤。
* **判断条件不同**：where不能用聚合函数进行判断，而having可以。
  
```sql
-- 根据性别分组，分别统计数量
select gender, count(*) from emp group by gender;
```

```sql
-- 根据性别分组，统计男性员工和女性员工的平均年龄
select gender, avg(age) from emp group by gender;
```

```sql
-- 查询年龄小于50的员工，并根据工作地址进行分组，获取员工数量大于等于3的工作地址
select address, count(address) addressCount from emp where age < 50 group by address having addressCount >=3;
```

### 注意
* 执行顺序：where > 聚合函数 > having
* 分组之后，查询的字段一般为聚合函数和分组字段，查询其他字段无任何意义
## 排序查询(order by)
```sql
order by 字段名 排序方式(asc/desc), 字段名 排序方式；
```
## 分页查询(limit)
语法
```sql
select 字段列表 from 表名 limit 起始索引，查询记录
```

注意：
* 起始索引从0开始，起始索引 = （查询页码-1）* 每页显示记录数
* 分页查询是数据库的方言，不同数据库有不同实现，mysql中是limit
* 如果查询的是第一页数据，起始索引可以省略.(postgresql 和mysql几乎是一样的， Oracle不一样，Oracle是rownum)
* limit是在sql语句最后面的，order by 在limit 之前

```sql
-- 查询第1页员工数据，每页展示10条数据
select * from emp limit 10;

-- 查询第二页员工数据，每页展示10条记录
select * from emp limit 10, 10;
```

## 练习

```sql
-- 查询年龄为20，21，22，23的员工信息 (条件查询)
select * from emp where age in (20, 21, 22, 23);

-- 查询性别为男，并且年龄在20-40岁（含）以内的姓名为三个字的员工 (条件查询)
select * from emp where gender = '男' and age between 20 and 40 and name like '___';

-- 统计员工表中，年龄小于60岁的，男性员工和女性员工的人数 （分组查询）
select gender, count(*) from emp where age < 60 group by gender;

-- 查询所有年龄小于等于35岁员工的姓名和年龄，并对查询结果按年龄升序排序，如果年龄相同则按入职时间降序排序 （排序查询）
select name, age from emp where age <= 35 order by age, enterDate desc ;

-- 查询性别为男，且年轻在20-40（含）以内的前5个员工信息，对查询结果按年龄升序排序，再按入职时间升序排序 （分页查询）
select * from emp where age between 20 and 40 and gender='男' order by age, enterDate desc limit 5;   
```

## DQL 执行顺序

```sql
select
   字段列表           5
from
    表名              1
where
    条件列表           2
group by
    分组字段列表        3
having
    分组后条件列表      4
order by
    排序字段列表        6
limit
    分页参数            7

```

* 注意： group by之后是having，尽管having 能使用select里面的别名，但那是语法糖，真正的执行还是having先执行，然后是select
而且如果聚合函数如果被having计算过，在select就不会重复计算了

## DCL
用来管理数据库用户，控制数据库的访问权限


## 函数
### 字符串函数

#### concat(s1,s2,...sn) 字符串拼接，将s1,s2,...sn 拼接成一个字符串
```sql
-- concat
select concat('Hello ', 'world');
```
#### lower(str) 将字符串str全部转为小写
```sql
-- lower
select lower('Hello');
```
#### upper(str) 将字符串str全部转为大写
```sql
-- upper
select upper('Hello');
```
#### lpad(str, n, pad) 左填充，用字符串pad对str的左边进行填充，达到n个字符串长度
```sql
-- lpad
select lpad('01', 5, '0');
```
#### rpad(str, n, pad) 右填充, 用字符串pad对str的右边进行填充,达到n个字符串长度
```sql
-- rpad
select rpad('01', 5, '0');
```
#### trim(str)
```sql
-- trim
select trim(' 01 00 ');
```
#### substring(str, start, len) , 索引值是从1开始的
```sql
-- substring()
select substring('hello', 1, 1);
```

```sql
-- 将员工工号统一为5位数，目前不足5位数的全部在前面补0，比如：1号员工的工号应该为00001
update emp set workNo = lpad(workNo, 5, '0');
```

### 数值函数

#### ceil(x) 向上取整

#### floor(x) 向下取整

#### mod(x, y) 返回x%y的模， 即x 除以y的余数

#### rand() 返回0-1内的随机数

#### round(x,y) 求参数x的四舍五入的值，保留y位小数

```sql
-- ceil(x) 向上取整
select ceil(1.5);

-- floor(x) 向下取整
select floor(1.5);

-- mod(x, y) 返回x/y的模
select mod(3, 4);

-- rand() 返回0-1内的随机数
select rand();

-- round(x,y) 求参数x的四舍五入的值，保留y位小数
select round(2.345, 2);

-- 通过数据库函数，生成一个六位数的随机验证码
-- 不推荐，可能生成不足六位的数字，因为rand有可能是0.001234
select round(rand(), 6) * 1000000;

-- 先乘保证数字一定在0-999999之间，但还是会出现不足6位数字，需要补前面的0
select round(rand() * 1000000, 0);

-- 最终写法
select lpad(round(rand() * 1000000, 0), 6, '0');
```

### 日期函数

#### curdate() 当前日期

#### curtime() 当前时间

#### now()     当前日期和时间

#### year(date) 获取指定date的年份

#### month(date) 获取指定date的月份

#### day(date) 获取指定date的日期

#### date_add(date, interval expr type) 返回一个日期/时间值加上一个时间间隔expr后的时间值

#### datediff(date1, date2) 返回起始时间date1 和结束时间date2之间的天数

```sql
#### curdate() 当前日期

select  curdate();

#### curtime() 当前时间
select curtime();

#### now()     当前日期和时间
select now();

#### year(date) 获取指定date的年份
select year(now());

#### month(date) 获取指定date的月份
select month(now());

#### day(date) 获取指定date的日期
select day(now());

#### date_add(date, interval expr type) 返回一个日期/时间值加上一个时间间隔expr后的时间值
select date_add(now(), INTERVAL  70 DAY);

#### datediff(date1, date2) 返回起始时间date1 和结束时间date2之间的天数

select datediff('2021-12-01', '2021-11-01');

-- 查询所有员工的入职天数，并根据入职天数倒序排序
select name, datediff(curdate(), enterDate) as entryDays from emp order by enterDate desc;
```

### 流程函数

#### if(value, t, f) 如果value为true， 则返回t, 否则返回if

#### ifnull(value1, value2) 如果value1 为空，则返回value2

#### case when val1 then res1 ... else default end 如果val1 为true，则返回res1, 都不匹配则返回default值 结束

#### case  expr when val1 then res1 ... else default end 如果表达式等于val1 则返回res1, ... 都不匹配则返回default值
```sql
-- 查询员工工作地址，如果是北京上海则输出一线城市，如果是其他的就输出二线城市
select emp.address, case address when '北京' then '一线城市' when '上海' then '一线城市' else '二线城市' end from emp;
```


## 约束
* not null 非空约束
* unique 唯一约束
* primary key 主键约束
* default 默认约束
* check 检查约束
* foreign key 外键约束

```sql
create table student (
    id int not null primary key auto_increment comment '学生id',
    name varchar(50) not null,
    birth_date date not null,
    gender char(1) not null,
    grade tinyint not null,
    math tinyint,
    chinese tinyint,
    english tinyint
);

insert into student (name, birth_date, gender, grade, math, chinese, english) VALUES ('小明', '2022-08-10', '男', 3, 100, 70, 80),
                                                                                     ('小红','2022-05-01', '女', 3, 90, 96, 99 );
-- 统计三年级的学习成绩，按总成绩 倒序排名
select name, math+chinese+english sumScore from student where grade = 3 order by sumScore desc;

-- 统计三年级的学习成绩，如果成绩大于250 分算优秀，否则算一般， 按总成绩 倒序排名
select name, math+chinese+english sumScore, case when math+chinese+english > 250 then '优秀' else '一般' end 评分 from student where grade = 3 order by sumScore desc;

```

## 外键

### 添加外键
```sql
create table 表名 （
    字段名 类型,
    [constraint] [外键名称] foreign key (外键字段名) references 主表(主表列名)
);

alter table 表名 add constraint 外键名称 foreign key (外键字段名) reference 主表(主表列名);

-- 添加外键
alter table emp add constraint fk_emp_dept_id foreign key (dept_id) references depart(id);
```

### 删除外键

```sql
alter table 表名 drop foregin key 外键名称;

-- 删除外键
alter table emp drop foreign key fk_emp_dept_id;
```

### 外键约束 对父表中删除/更新的影响

* no action: 当要删除/更新某个记录时，先检查，该记录是否有对应外键，如果有则不允许删除/更新
* restrict: 与no action一致
* cascade：当要删除/更新某个记录时，先检查，该记录是否有对应外键，如果有则也删除/更新
* set null，当要删除某个记录时，先检查，该记录是否有对应外键，如果有则设置该外键值为null (要求外键值允许为null)
* set default, 父表记录有更新时，子表将外键设置成一个默认值(innodb不支持)

```sql
alter table emp add constraint fk_emp_dept_id foreign key (dept_id) references depart(id) on update cascade on delete cascade;
```

## 多表查询

### 连接查询

#### 内连接： 查询A,B表交集的部分数据， 如果A，B在连接字段是空 不返回结果集

* 隐式内连接
```sql
select 字段列表 
from t1, t2 
where t1.字段 = t2.字段; 
```

* 显式内连接
```sql
select 字段列表 
from t1 (inner) 
join t2 
on t1.字段 = t2.字段；


-- 返回所有分配好部门的员工
select t1.*, t2.name from emp t1
join depart t2
on t1.dept_id = t2.id;

```


#### 外连接 - 左外连： 查询左表所有数据，以及两张表交集部分数据， 即使左连接表的连接字段是空，也返回在结果集
```sql
select 字段列表 from t1
left join t2
on 条件;


-- 显示所有的员工信息以及他们所在的部门名称
select t1.*, t2.name from emp t1 left join depart t2 on t1.dept_id = t2.id;
```

#### 外连接 - 右外连: 查询右表所有数据，以及两张表交集部分数据， 即使右连接表的连接字段是空也返回结果集
```sql
select 字段列表 from t1
right join t2
on 条件;
```

#### 自连接: 当前表与自身的连接查询，自连接必须使用表别名

```sql
- 查询员工 及其所属领导的名字
select t1.name, t2.name from emp t1 join emp t2 on t1.manager = t2.id;

-- 查询员工 及其所属领导的名字, 如果没有领导 也要查询出来
select t1.name, t2.name from emp t1 left join emp t2 on t1.manager = t2.id;
```

#### 联合查询 把多次查询的结果合并起来，形成一个新的查询结果集
```sql
-- 查询工资小于5000 以及年龄大于50的员工
select * from emp where salary < 5000
union all
select * from emp where age > 50;

-- 将结果去重
select * from emp where salary < 5000
union 
select * from emp where age > 50;
```
* 多张表的列数保持一致
* union 会对合并的结果去重
* union all 不会去重

### 子查询 又称嵌套查询
```sql
select * from t1 where colum1 = (select colum1 from t2);
```
子查询外部语句可以是insert/update/delete/select的任何一个

#### 标量子查询（子查询结果为单个值）
```sql
-- 查询研发部所有员工信息
select * from emp where dept_id = (select id from depart where name = '研发');
```

#### 列子查询（子查询结果为一列）
常用的操作符:
* in
```sql
-- 查询研发部和财务部所有员工信息
select * from emp where dept_id in (select id from depart where name = '研发' or name = '财务');
```
* not in
* any
* some 与any相同
* all
```sql
-- 查询比财务部所有人工资都高的员工
select * from emp where salary > all (select salary from emp where dept_id = (select id from depart where name = '财务')) ;
```

#### 行子查询（子查询结果为一行）
```sql
-- 查询和张无忌的薪资相同以及和张无忌有相同领导的员工信息
select * from emp where (salary, manager) = (select salary, manager from emp where name = '张无忌');
```

#### 表子查询（子查询结果为多行多列）

#### where之后

#### from 之后

#### select之后



## 事务

### 四大特性 ACID
* Atomic 原子性： 一个一组操作 要么都执行成功 要么都不执行
* Consistency 一致性：事务提交完后 数据是一致的， 比如A给B转账100， 事务执行完和执行前两个人的余额加一起应该是一样的
* Isolation 隔离性，在事务之内的查询 不能查询到未提交的数据
* Durable 持久性，事务提交完，数据会落盘在磁盘，即使机器重启，数据还被查询得到

### 并发事务问题

* 脏读， 读到 事务未提交得数据
* 不可重复读，在同一个事务中，多次读取同一行数据，数据不一样
* 幻读，在同一个事务中，上一个查询，没有查询到数据，但是插入数据时，又发现数据已经存在了

### 隔离级别 解决并发事务问题
            脏读      不可重复读     幻读
* 读未提交， O          O            O
* 读已提交， X          O            O
* 可重复度   X          X            O
* 串行执行   X          X            X

```mysql
--  查看mysql 默认隔离级别
select @@transaction_isolation;  -- REPEATABLE-READ

-- 设置事务隔离级别
set session transaction isolation level repeatable read;

-- 开启事务
start transaction;

-- 提交事务
commit/rollback;
```

## 索引

### B+Tree 索引
数据都会出现在叶子节点，叶子节点是个双向链表，矮胖的特点，能存储更多的数据

### Hash 索引

### R-tree 空间索引

### Full-text 全文索引 倒排索引

### 索引分类
* 主键索引 主键创建得索引 关键字：primary
* 唯一索引 避免同一个表中某数据列中得值重复 关键字:unique
* 常规索引 快速定位特定数据 
* 全文索引 查找的是文本中的关键词，而不是比较索引中的值  关键字 fulltext


* 聚集索引  将数据存储和索引放在了一块，索引结构的叶子节点保存了行数据， 默认主键索引是聚集索引， 如果没有主键，选择唯一索引作为主键，如果没有唯一索引使用rowid

* 二级索引 又叫辅助索引，将数据与索引分开存储，索引结构的叶子节点关联的是对应的主键， 通过二级索引找到主键，再根据主键从聚集索引中找到行数据，这个操作叫回表查询

### 索引语法

#### 创建索引
```sql
create [unique | fulltext] index index_name on table_name (col_name,...);
```

#### 查看索引

```sql
show index from table_name;
```

#### 删除索引
```sql
drop index index_name on table_name;
```

### 查看sql执行频次
```sql
-- 七个下划线
show global status like 'com_______' 
```

### 性能分析 - 开启慢查询日志
慢查询日志记录了所有执行时间超过指定参数的所有SQL语句日志
```sql
-- 查看慢查询日志是否开启
show variables like 'slow_query_log';
```
MySQL的慢查询日志默认没有开启，需要再MySQL的配置文件(/etc/my.cnf)中配置
```txt
slow_query_log=1
long_query_time=2
```
### 性能分析 - profile详情

show profiles, 通过have_profiling 参数，看到mysql是否支持
```sql

show variables like 'have_profiling';

select @@have_profiling;

select @@profiling;

set profiling =1;

show profiles;

show profile for query [query id];
```

### 性能分析 - explain执行计划

explain 或者desc命令获取mysql 如何执行select 语句的信息，包括在select 语句执行过程中表如何连接和连接顺序

```sql
-- 直接在select语句之前加上关键字explain/desc
explain select 字段列表 from 表名 where 条件；
```

各字段含义:
* id： select查询的序列号，表示查询中执行select子句或者是操作表的顺序（id相同，执行顺序从上到下，id不同，值越大越先执行）
* select_type: 查询的类型，simple 简单表，不使用表连接或者子查询， primary主查询即外层查询，union union中的第二个或者后面的查询语句，subquery select/where之后包含了子查询等

* type: 连接类型，性能由好到差的连接类型为null(不访问任何表), system(访问系统表), const(根据主键或者唯一索引访问), eq_ref, ref(如果使用非唯一性的索引查询), range, index,all
  
* possible_key：可能用到的索引
  
* key: 实际用到的索引

## 索引使用原则

### 最左前缀法则： 如果索引了多列，要遵守最左前缀法则。查询从索引的最左列开始，并且不跳过索引中的列， 如果跳跃某一列，索引将部分失效（后面的字段索引失效）

联合索引依然是一颗B+树，b+树索引的存储是先从左往右排序，找数据也是先从左往右找，只能根据左边的值找右边

## 索引失效 

* 在索引列上进行函数运算
* 字符串类型不加单引号
* 模糊匹配 如果仅仅是尾部模糊匹配，索引不会失效，如果是头部模糊匹配， 索引失效
* or 连接的条件 用or分割开的条件，如果or前面有索引，后面没索引，那么都不会用索引
* 数据分布影响 如果Mysql 评估使用索引比全表更慢，则不使用索引， 比如is null 和 is not null 走不走索引是取决于表中数据分布的，如果null 多，查is null就不走索引，查 is not null 会走索引，反之一样。


## SQL 提示
告诉数据库 用哪个index

use index: 数据库会评估一下是否使用
```sql
select * from emp use index(idx_XX) where XX;
```
ignore index: 忽略index
```sql
select * from emp ignore index(idx_XX) where XX;
```
force index: 强制数据库使用
```sql
select * from emp force index(idx_XX) where XX;
```

## 覆盖索引

尽量使用覆盖索引，查询使用了索引，并且需要返回的列，在该索引中已经全部能够找到，减少select *
如果查询的内容在联合索引中不存在，还需要从主键索引中回表，增加了时间，在所有的二级索引或者联合索引叶子节点都存的是主键值，如果查询的内容超过了联合索引的内容就需要根据主键值去主键索引中回表。

## 前缀索引

对于长的字符串（varchar, text)时，索引值会很长，浪费io, 影响查询效率。此时，可以只对字符串的前几个字符建立索引

```sql
create index idx_xxx on table_name(column(n));
```

n值前缀长度取值
```sql
select count (distinct email) / count(*) from emp ;

select count (distinct substring(email, 1, 5)) / count(*) from emp;
```
这是获取索引的选择性，选择性是指不重复的索引值和数据表中的数据比值，值越高查询效率越高，唯一索引的选择性是1

## 索引设计原则

* 数据量较大，查询比较频繁
* 针对常作为查询条件(where), 排序(order by), 分组（group by)的字段建立索引
* 尽量选择区分度高的列作为索引 比如 用户表中的身份证号，手机号，邮箱，用户名，区分度越高，索引也高, 相反的是比如性别，状态
* 如果是字符串类型的字段，字段的长度较长，可以针对字段的特点，建立前缀索引
* 尽量使用联合索引，减少单例索引，查询时，联合索引很多时候可以覆盖索引，节省空间，避免回表，提高查询效率
* 控制索引数量，索引并不是多多益善，索引越多，维护索引结构的代价越大，会影响增删改的效率
* 如果索引不能存null值，请在创建表时使用not null约束


## SQL 优化

### 插入数据
* 批量插入
```sql
insert into table_name values,(,),(,),(,),(,),(,),(,),(,),(,);
insert into table_name values,(,),(,),(,),(,),(,),(,),(,),(,);
```

* 手动提交事务
```sql
start transaction; / begin;
insert into table_name values,(,),(,),(,),(,),(,),(,),(,),(,);
insert into table_name values,(,),(,),(,),(,),(,),(,),(,),(,);
...
commit;

```

* 大批量插入 如果一次性需要插入大批量书库，可以使用mysql提供的load指令进行插入

### 主键优化
在InnoDB存储引擎中，表数据都是根据主键顺序组织存放的
Tablespace: 表空间 （ibd）
Segment：段,数据段（leaf node segment),索引段（non-leaf node segment), 回滚段（rollback segment) , InnoDB是索引组织表，数据段就是B+树的叶子节点，索引段即为B+树的非叶子节点
Extent：区 默认1M，一个区中一共有64个连续的页
Page: 页 默认16k， 为了保证页的连续性，InnoDB存储引擎每次从磁盘申请4-5个区
Row: 行, 数据是按行进行存放的
Trx_id, 每次对某条记录进行改动时，都会把对应事务的id赋值给trx_id隐藏列
Roll_pointer,每次对某条记录进行改动时，都会把旧的版本写入到undo日志中，然后这个隐藏列就相当于一个指针，可以通过它来找到该记录修改前的信息


* 页分裂
页可以为空，也可以填充一半，也可以填充100%， 每个也包含了2-N行数据（如果一行数据太大，会行溢出），根据主键排列
主键乱序插入，会发送页分裂现象
* 页合并
删除会造成页合并现象

#### 主键设计原则
* 满足业务需求的情况下， 尽量降低主键的长度
* 插入数据时，尽量选择顺序插入，选择使用auto_increment自增主键
* 尽量不要使用UUID做主键或者时其他自然主键，如身份证号，因为这些是无序的，会出现页分裂现象

### order by 优化
1. Using filesort: 通过表的索引或全部扫描，读取满足条件的数据行，然后在排序缓冲区sort buffer中完成排序，所有不是通过索引直接返回排序结构的都叫filesort排序
2. Using index: 通过有序索引顺序扫描直接返回有序数据，这种情况为 using index, 不需要额外排序，效率高

可以在建索引时指定 字段排序

### group by优化
* 在分组操作时，可以通过索引来提高效率
* 分组操作时，索引的使用也是满足最左前缀法则的

### limit优化
一个常见头疼的问题就是limit 2000000,10,起始点越大耗时越久

优化方案：通过覆盖索引和子查询
```sql
-- 原来
select * from emp limit 2000000, 10;

-- 优化
select s.* from emp s, (select id from emp limit 2000000, 10) d where s.id = d.id;
```

### count 优化
如果数据比较多，count(*)比较久
在InnoDB中，执行count(*), 需要把数据一行一行地读出，累计计数

优化思路：可以自己计数，比如用redis维护

count几种用法：
* count(*) 并不会把全部字段取出来，专门做了优化，不取值，服务层直接进行累加
* count(主键) 遍历整张表，把每一行的主键id值取出来，不用判断是否为0，因为主键不为空
* count(字段) 遍历整张表，把每一行的字段值取出来， 如果没有not null 约束，会检查是否为null
* count(1) 遍历整张表，但不取值，每返回一行，放一个数字1进入，按行进行累加，这个数字只是占位符，只要不是null，设的值不会影响最终结果值

效率排序： count(字段)<count(主键 id) < count(1) 约等于 count(*), 所以尽量使用count(*)

### update优化

更新字段时，一定要根据索引字段进行更新，并且索引不能失效，否则由行锁升级为表锁， 并发性能就会降低


## 锁

### 锁的分类

#### 根据锁的粒度分
1. 全局锁： 锁定数据库中所有表， 备份的时候要加全局锁，保证备份的数据是一致的,加了锁后，只能读不能写，写的话会阻塞
```sql
flush tables with read lock;
```
```shell
## 备份语句不需要进入mysql 中，加锁和解锁需要进入mysql中
mysqldump -uroot -p123456 emp > emp.sql
```
```sql
unlock tables;
```
数据库加全局锁是一个比较中的操作， 我们可以在innoDB引擎中备份时加上参数--single-transaction完成不加锁的一致性数据备份, 通过快照
```shell
mysqldump --single-transaction -uroot -p123456 emp > emp.sql
```

2. 表级锁：锁住整张表
* 表锁
    * 表共享读锁（read lock) 其他客户端能读不能写
    ```sql
    lock tables 表名 read；
    unlock tables;
    ```
    * 表独占写锁（write lock) 其他客户端读写都不能
    ```sql
    lock tables 表名 write;
    unlock tables;
    ```
* 元数据锁 meta data lock, MDL
  MDL加锁过程是系统自动控制，无需显示使用，在访问一张表的时候会自动加上。MDL锁主要作用是维护元数据一致性，在表上由活动事务的时候，不可以对元数据进行写入操作。为了避免DML与DDL冲突，保证读写正确性
```sql
alter 操作会给元数据加一把exclusive 锁 这把锁与sharde_read, shared_write互斥

当在事务中执行 select时， 会自动给元数据加shared_read锁，此时在另外窗口中执行alter操作 会受到阻塞


```


* 意向锁
在进行DML语句时，会自动加行锁，此时如果另一个线程或窗口要加表锁，得一行一行去检查有没有行锁，这样效率极低，为了提高效率，引进意向锁，当行锁加得时候，意向锁会显示锁的类型，加表锁时只需要检查意向锁是否能和表锁兼容就可以了

* 意向共享锁（IS）: select ... lock in share mode， 与表锁的共享锁（read)兼容， 与表锁的写锁互斥
* 意向排他锁（IX）：由insert，update，delete，select...for update 添加, 与表锁共享锁和排他锁都互斥，意向锁之间不会互斥



3. 行级锁：锁住对应的行数据

锁住对应的行数据，针对索引项加的锁，而不是对记录加的锁

InnoDB 事务外界行级锁

* 行锁（record lock): 锁定单个行记录的锁，防止其他事务对此进行update或delete，在RC，RR隔离级别下都支持
* 共享锁（S），运行别的事务读该行，和别的事务排他锁互斥
* 排他锁（X），不允许别的事务获取读锁和排他锁
  加锁时机和意向锁一致

* 间隙锁（Gap Lock): 锁定索引记录间隙（不包含该记录），防止其他事务在这个间隙进行insert，产生幻读，在RR隔离级别下都支持
* 临键锁（next-key lock): 行锁和间隙锁的组合，同时锁住数据
  * 索引上的等值查询（唯一索引），给不存在的记录加锁，优化为间隙锁
  * 索引上的等值查询（普通索引），向右遍历时最后一个值不满足查询需求时，next-key lock退化为间隙锁
  * 索引上的范围查询（唯一索引）--会访问到不满足条件的第一个值上
查看锁
```sql
select object_schema, object_name,index_name,lock_type,lock_mode,lock_data from performance_schema.data_locks;
```
当我在一个事务中做范围查询并加上lock in share mode时， 其他事务要对该范围内的数据修改是不允许的 会一直阻塞直到我的事务提交

## InnoDB存储引擎
从Mysql5.5开始，使用InnoDB存储引擎
在InnoDB存储引擎中，表数据都是根据主键顺序组织存放的
Tablespace: 表空间 （ibd）
Segment：段,数据段（leaf node segment),索引段（non-leaf node segment), 回滚段（rollback segment) , InnoDB是索引组织表，数据段就是B+树的叶子节点，索引段即为B+树的非叶子节点
Extent：区 默认1M，一个区中一共有64个连续的页
Page: 页 默认16k， 为了保证页的连续性，InnoDB存储引擎每次从磁盘申请4-5个区
Row: 行, 数据是按行进行存放的
Trx_id, 每次对某条记录进行改动时，都会把对应事务的id赋值给trx_id隐藏列
Roll_pointer,每次对某条记录进行改动时，都会把旧的版本写入到undo日志中，然后这个隐藏列就相当于一个指针，可以通过它来找到该记录修改前的信息

### 内存结构
#### Buffer pool
缓冲池，缓存磁盘上经常操作的真实数据，在执行增删改时，先操作缓冲池中的数据，然后再以一定的频率刷新到磁盘，减少IO，加速处理速度

缓冲池以页为单位，分为三种类型（和Linux的内存页一致）：
free page: 空闲page
clean page: 被使用的page, 数据没有被修改过
dirty page: 被使用过，被修改过

#### Change Buffer
更改缓冲区 针对非唯一的二级索引页，在执行DML语句时，如果数据page没有在buffer pool中，不会直接操作磁盘，而是将数据变更存在更改缓存区中Change Buffer,在未来数据被读取时，再将数据合并恢复到Buffer Pool中，再将合并后的数据刷新到磁盘中

#### Log Buffer
日志缓冲区，保存要写入磁盘log日志数据（redo log, undo log), 默认大小为16 MB， 日志缓冲区会定期刷到磁盘中，如果需要更新，插入或删除许多行的事务，增加日志缓冲区的大小可以节省磁盘I/O；
innodb_log_buffer_size:缓冲区大小
innodb_flush_log_at_trx_commit: 日志刷新到磁盘时机。 1 表示日志在每次事务提交后写入并刷新到磁盘；0 表示每秒将日志写入并刷新到磁盘一次；2 日志在每次事务提交后写入，并每秒刷新到磁盘一次

如果有一台单独的服务器给MySQL，那么80%的内存都会分配给mysql的内存缓存区


### 磁盘结构 
#### 系统表空间 system tablespace 是更改缓冲区的存储区域
参数 innodb_data_file_path
```sql
show variables like '%data_file_path%';
```
#### File-Per-Table tablespace: 每个表的文件表空间包括单个InnoDB表的数据和索引，并存储在文件系统上的单个数据文件中
参数：innodb_file_per_table

#### General Tablespaces: 通用表空间

创建通用表空间
```sql
create tablespace XXXX add datafile 'file_name' engine=engin_name;
```

使用通用表空间
```sql
create table xxxx tablespace ts_name;
```

#### undo tablespaces：撤销表空间，主要存储undo log日志， MySQL实例在初始化时会自动创建两个默认的undo表空间（初始大小为16M)

#### Doublewrite Buffer Files: 双写缓冲区，InnoDB引擎将数据页从Buffer Pool刷新到磁盘前，先将数据也写入双鞋缓冲区文件中， 便于系统异常时恢复数据 .dblwr

#### Redo log: 重做日志，当事务提交后 会把所有修改信息都会存到该日志中，用于在刷新脏页到磁盘时，发生错误时，进行数据恢复使用
以循环方式写入重做日志文件，涉及到两个文件ib_logfile0, ib_logfile1


### 后台线程

```sql
show engine innodb status; 
```
1. master thread
核心后台线程，负责调度其他线程，负责将缓冲池中的数据刷新到磁盘，保存数据一致性，还包括脏页的刷新，合并插入缓存，undo页的回收

2. IO thread
AIO处理io请求，
Read thread 4
Write thread 4
Log thread 1
Insert buffer thread 1

3. Purge Thread 回收事务已经提交了的undo log, 事务提交后，undo log可能不用了，用它来回收
4. Page Cleaner Thread 协助master thread刷新脏页到磁盘，减轻master thread的压力

## 事务原理

原子性，一致性，持久性 由redo log和undo log保证
隔离性 由锁和mvcc保证 

### Redo log
重做日志，记录的时事务提交时数据页的物理修改，时用来实现事务的持久性
该日志文件由两部分组成：重做日志缓存已经重做日志文件
当事务提交之后会把所有修改信息都存到该日志文件中，用于在刷新脏页到磁盘ibd文件时，如果发生错误，可以通过redo log进行数据恢复

### undo log 
回滚日志，用于记录数据被修改前的信息，作用包含两个：提供回滚和MVCC

undo log 和redo log记录物理日志不一样，它时逻辑日志，可以认为当delete一条记录时，undo log中会记录一条对应的insert记录，反之亦然，当update一条记录时，它记录一条对应相反的update记录，当执行rollback时，可以从undo log的逻辑记录中读取到相应的内容并进行回滚

undo log销毁： undo log在执行事务时产生，事务提交时，并不会立即删除undo log, 因为这些日志可能还用于mvcc
undo log存储：undo log采用端的方式进行管理和记录，

## MVCC多版本并发控制
#### 当前读
永远读的是记录的最新版本，可以通过共享锁或者排他锁保证

#### 快照读
简单的select 不加锁的就是快照都，读取的是记录数据的可见版本，有可能是历史数据
rc: 每次select 都生成一个快照读
rr:开启事务后第一个select语句才是快照读的地方
serializable: 快照读会退化为当前读

#### MVCC
多版本并发控制，通过维护一个数据的多个版本，使得读写操作没有冲突，快照读为MySQL实现MVCC提供了一个非阻塞读功能。MVCC的具体实现，还需要依赖于数据库记录中的三个隐式字段，undo log日志，readview

##### 记录中的隐藏字段
DB_TRX_ID, 最近修改事务ID， 记录插入这条记录或最后一次修改该记录的事务ID
DB_ROLL_PTR, 回滚指针，指向这条记录的上一个版本，用于配合undo log
DB_ROW_ID 隐藏主键

##### undo log
当insert的时候，，产生的undo log日志只在回滚时需要，在事务提交后，可被立即删除
而update,delete的时候，产生的undo log日志不仅在回滚的时候需要，在快照读的时候也需要 不会被立即删除

##### undo log版本链
记录：id  age    DB_TRX_ID， DB_ROLL_PTR
      1   30       1
      1   3        2           0x0001
      1   4        3           0x0002
      1   5        4           0x0003   

undo log:            DB_TRX_ID，DB_ROLL_PTR
             
        0x0003  1 4     3       0x0002     
        0x0002  1 3     2       0x0001          
        0x0001  1 30    1      
##### Readview
读视图 是快照读SQL执行时MVCC提供数据的依据，记录并维护系统当前活跃的事务id
四个核心字段：
m_ids 当前活跃的事务ID集合
min_trx_id 最小活跃事务ID
max_trx_id 预分配事务ID，当前最大事务ID+1
creator_trx_id ReadView创建者的事务ID

版本链数据访问规则：
1. 记录中存的trx_id == creator_trx_id？ 可以访问
2. trx_id < min_trx_id? 可以访问 说明事务开启前已提交
3. trx_id > max_trx_id? 不可以访问  说明这条数据已经被后面的事务修改了
4. min_trx_id <= trx_id <=max_trx_id? 如果trx_id不在m_ids中是可以访问该版本的。说明数据已提交
如果记录不满足可以访问的要求，就从undo log里从版本连中找，直到找到合适的版本

对于RR：仅在事务中第一次执行快照读时生成readview, 后续复用该readview
RC: 在事务中每一次执行快照读时都生成readview


# MySQL运维

## 日志

### 错误日志
```
-- /var/log/mysqld.log
show variables like '%log_error%';
```
### 二进制日志
binlog 记录了所有的DDL语句和DML语句

作用
* 灾难恢复
* 主从复制
```
-- /var/log/mysql/binlog
show variables like '%log_bin%';
```  

日志格式
* statement 记录的是SQL语句
row 行
mixed 
```
-- /var/log/mysql/binlog
show variables like '%binlog_format%';
```  
日志查看
```
mysqlbinlog 参数选项 logfilename
```
删除binlog

查看log 过期时间
```sql
show variables like '%binlog_expire%';
 
```
#### 查询日志
记录了客户端的所有操作语句，默认是未开启

```sql
show variables like '%general%';
 
```
#### 慢查询日志

## 主从复制
是指将主数据库的二进制日志传到从库服务器中，从库对这些日志重新执行也叫重做，从而使得从库和主库的数据保持同步

优点：
1. 主库出现问题，可以快速切换到从库提供服务
2. 实现读写分离，降低主库的访问压力
3. 可以在从库中执行备份，以避免备份期间影响主库读物

流程或者原理
1. 主库发生DML或者DDL语句
2. 从库的IOthread发起请求向主库同步binlog日志
3. IO thread将binlog 同步到relaylog
4. 从库的SQLthread 将执行relaylog（中继日志） 将SQL语句执行到数据库中