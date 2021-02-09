package mycore.test

class SubTask extends Serializable {

  var datas: List[Int] = _

  var logic: (Int) => Int = _

  def compute() = {
    datas.map(logic)
  }

}
