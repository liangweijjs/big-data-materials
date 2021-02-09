package mycore.test

class Task extends Serializable {

  //Task 类似 spark 里的 RDD, 准备好数据结构和计算逻辑

  val datas = List(1, 2, 3, 4, 5)

  val logic: Int => Int = _ * 2

  def compute() = {
    datas.map(logic)
  }

}
