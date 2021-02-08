# 01-Spark 概述
Spark 是一种基于内存的快速、通用、可扩展的大数据分析计算引擎。

### Spark and Hadoop
- MapReduce是一种编程模型，Hadoop根据Google的MapReduce论文将其实现， 作为 Hadoop 的分布式计算模型，是 Hadoop 的核心。基于这个框架，分布式并行 程序的编写变得异常简单。综合了 HDFS 的分布式存储和 MapReduce 的分布式计 算，Hadoop 在处理海量数据时，性能横向扩展变得非常容易。
- Hadoop MapReduce 由于其设计初衷并不是为了满足循环迭代式数据流处理，因此在多 并行运行的数据可复用场景(如:机器学习、图挖掘算法、交互式数据挖掘算法)中存 在诸多计算效率等问题。所以 Spark 应运而生，Spark 就是在传统的 MapReduce 计算框 架的基础上，利用其计算过程的优化，从而大大加快了数据分析、挖掘的运行和读写速
度，并将计算单元缩小到更适合并行计算和重复使用的 RDD 计算模型。
- 机器学习中 ALS、凸优化梯度下降等。这些都需要基于数据集或者数据集的衍生数据 反复查询反复操作。MR 这种模式不太合适，即使多 MR 串行处理，性能和时间也是一 个问题。数据的共享依赖于磁盘。另外一种是交互式数据挖掘，MR 显然不擅长。而 Spark 所基于的 scala 语言恰恰擅长函数的处理。
- spark 是一个分布式数据快速分析项目。它的核心技术是弹性分布式数据集(Resilient Distributed Datasets)，提供了比 MapReduce 丰富的模型，可以快速在内存中对数据集 进行多次迭代，来支持复杂的数据挖掘算法和图形计算算法。
- Spark 和 Hadoop 的根本差异是多个作业之间的数据通信问题 : Spark 多个作业之间数据 通信是基于内存，而 Hadoop 是基于磁盘。
- Spark Task的启动时间快。Spark采用fork线程的方式，而Hadoop采用创建新的进程 的方式。
- spark只有在shuffle的时候将数据写入磁盘，而Hadoop中多个MR作业之间的数据交 互都要依赖于磁盘交互
- Spark的缓存机制比HDFS的缓存机制高效。 

经过上面的比较，我们可以看出在绝大多数的数据计算场景中，Spark 确实会比 MapReduce 更有优势。但是 Spark 是基于内存的，所以在实际的生产环境中，由于内存的限制，可能会 由于内存资源不够导致 Job 执行失败，此时，MapReduce 其实是一个更好的选择，所以 Spark 并不能完全替代 MR。

### Spark 核心模块
- Spark Core

Spark Core 中提供了 Spark 最基础与最核心的功能，Spark 其他的功能如:Spark SQL，Spark Streaming，GraphX, MLlib 都是在 Spark Core 的基础上进行扩展的
- Spark SQL

Spark SQL 是 Spark 用来操作结构化数据的组件。通过 Spark SQL，用户可以使用 SQL 或者 Apache Hive 版本的 SQL 方言(HQL)来查询数据。
- Spark Streaming

Spark Streaming 是 Spark 平台上针对实时数据进行流式计算的组件，提供了丰富的处理 数据流的 API。
- Spark MLlib

MLlib 是 Spark 提供的一个机器学习算法库。MLlib 不仅提供了模型评估、数据导入等
额外的功能，还提供了一些更底层的机器学习原语。
- Spark GraphX

GraphX 是 Spark 面向图计算提供的框架与算法库。
