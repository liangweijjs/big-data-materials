## 02-Spark 运行环境
### Local 模式
所谓的 Local 模式，就是不需 要其他任何节点资源就可以在本地执行 Spark 代码的环境，一般用于教学，调试，演示等

```
tar -zxvf spark-3.0.0-bin-hadoop3.2.tgz -C /opt/module
cd /opt/module
mv spark-3.0.0-bin-hadoop3.2 spark-local

//启动环境
bin/spark-shell

//启动成功后，可以输入网址进行 Web UI 监控页面访问
http://虚拟机地址:4040

//在解压缩文件夹下的 data 目录中，添加 word.txt 文件。在命令行工具中执行如下代码指
令
sc.textFile("data/word.txt").flatMap(_.split("
")).map((_,1)).reduceByKey(_+_).collect

//提交应用
bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master local[2] \
./examples/jars/spark-examples_2.12-3.0.0.jar \
10
```

- --class 表示要执行程序的主类，此处可以更换为咱们自己写的应用程序
- --master local[2] 部署模式，默认为本地模式，数字表示分配的虚拟 CPU 核数量
- spark-examples_2.12-3.0.0.jar 运行的应用类所在的 jar 包，实际使用时，可以设定为咱
们自己打的 jar 包
- 数字 10 表示程序的入口参数，用于设定当前应用的任务数量

### Standalone 模式
```
// 将 spark-3.0.0-bin-hadoop3.2.tgz 文件上传到 Linux 并解压缩在指定位置
tar -zxvf spark-3.0.0-bin-hadoop3.2.tgz -C /opt/module
cd /opt/module
mv spark-3.0.0-bin-hadoop3.2 spark-standalone

// 进入解压缩后路径的 conf 目录，修改 slaves.template 文件名为 slaves
mv slaves.template slaves

//修改 slaves 文件，添加 work 节点
linux1
linux2
linux3
 
 //修改 spark-env.sh.template 文件名为 spark-env.sh
mv spark-env.sh.template spark-env.sh

//修改 spark-env.sh 文件，添加 JAVA_HOME 环境变量和集群对应的 master 节点
export JAVA_HOME=/opt/module/jdk1.8.0_144
SPARK_MASTER_HOST=linux1
SPARK_MASTER_PORT=7077
 
 // 分发 spark-standalone 目录
 xsync spark-standalone
 
 //启动集群
 sbin/start-all.sh
 
 //查看三台服务器运行进程
 ================linux1================
3330 Jps
3238 Worker
3163 Master
================linux2================
2966 Jps
2908 Worker
================linux3================
2978 Worker
3036 Jps

//3) 查看 Master 资源监控 Web UI 界面: http://linux1:8080
//提交应用
bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master spark://linux1:7077 \
./examples/jars/spark-examples_2.12-3.0.0.jar \
10

1) --class 表示要执行程序的主类
2) --master spark://linux1:7077 独立部署模式，连接到 Spark 集群
3) spark-examples_2.12-3.0.0.jar 运行类所在的 jar 包
4) 数字 10 表示程序的入口参数，用于设定当前应用的任务数量
```
**提交参数说明**

--class Spark
 > 程序中包含主函数的类

--master Spark 
> 程序运行的模式(环境) 模式: local[*]、spark://linux1:7077、 Yarn

--executor-memory 1G 
> 指定每个 executor 可用内存为 1G
符合集群内存配置即可，具体情况具体分
析。
 
 
--total-executor-cores 2
> 指定所有 executor 使用的 cpu 核数 为2个

--executor-cores
> 指定每个 executor 使用的 cpu 核数
application-jar
打包好的应用 jar，包含依赖。这 个 URL 在集群中全局可见。 比 如 hdfs:// 共享存储系统，如果是file:// path，那么所有的节点的 path 都包含同样的 jar

application-arguments
> 传给 main()方法的参数

### 配置历史服务

由于 spark-shell 停止掉后，集群监控 linux1:4040 页面就看不到历史任务的运行情况，所以 开发时都配置历史服务器记录任务运行情况。

1) 修改 spark-defaults.conf.template 文件名为 spark-defaults.conf
> mv spark-defaults.conf.template spark-defaults.conf

2) 修改 spark-default.conf 文件，配置日志存储路径:
```
  spark.eventLog.enabled true
  spark.eventLog.dir hdfs://linux1:8020/directory
  ```
  
注意:需要启动 hadoop 集群，HDFS 上的 directory 目录需要提前存在。
```
sbin/start-dfs.sh
haadoop fs -mkdir /directory
```
3) 修改 spark-env.sh 文件, 添加日志配置
```
 export SPARK_HISTORY_OPTS="
 -Dspark.history.ui.port=18080
 -Dspark.history.fs.logDirectory=hdfs://linux1:8020/directory
 -Dspark.history.retainedApplications=30"
```

⚫ 参数1含义:WEBUI访问的端口号为18080

⚫ 参数2含义:指定历史服务器日志存储路径

⚫ 参数3含义:指定保存Application历史记录的个数，如果超过这个值，旧的应用程序
信息将被删除，这个是内存中的应用数，而不是页面上显示的应用数。

4) 修改 spark-defaults.conf
```
spark.yarn.historyServer.address=linux1:18080
spark.history.ui.port=18080
```
5) 启动历史服务
```
sbin/start-history-server.sh
```
6) 重新执行任务
```
bin/spark-submit \
--class org.apache.spark.examples.SparkPi \
--master yarn \
--deploy-mode client \
./examples/jars/spark-examples_2.12-3.0.0.jar \
10
```
7) Web 页面查看日志:http://linux2:8088

### 端口号
➢ Spark查看当前Spark-shell运行任务情况端口号:4040(计算)

➢ Spark Master 内部通信服务端口号:7077

➢ Standalone模式下，SparkMasterWeb端口号:8080(资源)

➢ Spark历史服务器端口号:18080

➢ HadoopYARN任务运行情况查看端口号:8088