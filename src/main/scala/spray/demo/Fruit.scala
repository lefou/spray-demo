package spray.demo

import org.json4s.native.Serialization
import org.json4s.ShortTypeHints
import org.json4s.DefaultFormats

trait Fruit

case class Apple(kind: String, yearOfAppearance: Option[Int] = None) extends Fruit
case class Banana(name: String, lengthCm: Int) extends Fruit

object Fruits {

  val someFruits = List[Fruit](
    Apple(kind = "Boskoop", yearOfAppearance = None),
    Apple(kind = "McIntosh", yearOfAppearance = Some(1796)),
    Apple(kind = "Golden Delicious", yearOfAppearance = Some(1890)),
    Banana(name = "Chiquita", lengthCm = 20))

  private[this] implicit val formats = Serialization.formats(ShortTypeHints(List(classOf[Apple], classOf[Banana])))
  def toJson(fruit: Fruit): String = Serialization.writePretty(fruit)
  def toJson(fruits: List[Fruit]): String = Serialization.writePretty(fruits)

  def fromJson(json: String): List[Fruit] = Serialization.read[List[Fruit]](json)

}