package mycore.test

import java.io.ObjectOutputStream
import java.net.{ServerSocket, Socket}

object Driver {

  def main(args: Array[String]): Unit = {
    val client1 = new Socket("localhost", 9090)
    val client2 = new Socket("localhost", 8080)

    val task = new Task

    val out1 = client1.getOutputStream
    val objOut1 = new ObjectOutputStream(out1)

    val subTask1 = new SubTask
    subTask1.logic = task.logic
    subTask1.datas = task.datas.take(2)

    objOut1.writeObject(subTask1)
    println("请求 1 已发送。")

    objOut1.flush()
    objOut1.close()
    client1.close()

    val out2 = client2.getOutputStream
    val objOut2 = new ObjectOutputStream(out2)

    val subTask2 = new SubTask
    subTask2.logic = task.logic
    subTask2.datas = task.datas.takeRight(2)

    objOut2.writeObject(subTask2)
    println("请求 2 已发送。")
    objOut2.flush()
    objOut2.close()

    client2.close()

  }

}
