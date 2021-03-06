package mycore.test

import java.io.ObjectInputStream
import java.net.ServerSocket

object Executor2 {

  def main(args: Array[String]): Unit = {
    //启动服务，接收数据
    val server = new ServerSocket(8080)

    println("服务器启动，等待请求...")

    //等待客户连接
    val client = server.accept()
    val in = client.getInputStream
    val objInput = new ObjectInputStream(in)

    val task = objInput.readObject().asInstanceOf[SubTask]
    val res = task.compute()
    println("2 计算结果：" + res)

    in.close()
    client.close()
    server.close()

  }

}
