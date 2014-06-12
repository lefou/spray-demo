package spray.demo.client

import scala.concurrent.Future
import scala.util.Failure
import scala.util.Success

import akka.actor.ActorSystem
import spray.client.pipelining._
import spray.demo.Fruits
import spray.http.HttpResponse

object Client extends App {

  implicit val system = ActorSystem("spray-client")
  import system.dispatcher

  val pipeline = sendReceive

  val fruits: Future[HttpResponse] = pipeline(Get("http://localhost:9000/fruit"))

  def shutdown() {
    system.shutdown()
    system.awaitTermination()
  }

  // multiple callbacks on futures are unordered, so we hook the shutdown in each case
  fruits onComplete {
    case Success(response) =>
      val fruitsAsString = response.entity.asString
      val fruits = Fruits.fromJson(fruitsAsString)
      println(s"Status: ${response.status} Content:\n ${fruitsAsString}\nFruits:\n${fruits}")
      shutdown()
    case Failure(e) =>
      println(s"Could not complete request. Error: ${e.getMessage}")
      shutdown()
  }

}