package spray.demo.server

import scala.util.Success
import scala.util.Try

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.PoisonPill
import akka.actor.Props
import spray.demo.Apple
import spray.demo.Fruits
import spray.http.MediaTypes
import spray.routing.RequestContext
import spray.routing.Route
import spray.routing.SimpleRoutingApp

object Server extends App with SimpleRoutingApp {

  implicit val system = ActorSystem("spray-server")

  var fruits = Fruits.someFruits

  def getJson(route: Route) = get {
    respondWithMediaType(MediaTypes.`application/json`) {
      route
    }
  }

  lazy val mainRoute =
    helloRoute ~ fruitRoute

  lazy val helloRoute =
    get {
      path("hello") {
        complete {
          "Hello"
        }
      } ~
        path("slow") { ctx =>
          system.actorOf(SlowActor.props) ! ctx
        }
    }

  lazy val fruitRoute =
    getJson {
      path("fruit") {
        complete {
          Fruits.toJson(fruits)
        }
      } ~
        path("fruit" / IntNumber) { index =>
          Try(fruits(index)) match {
            case Success(fruit) =>
              complete {
                Fruits.toJson(fruit)
              }
            case _ => reject
          }
        }
    } ~
      post {
        path("fruit" / "apple") {
          parameters("kind", "yearOfAppearance".as[Option[Int]]) { (kind, year) =>
            val newApple = Apple(kind = kind, yearOfAppearance = year)
            fruits = fruits :+ newApple
            complete {
              "OK"
            }
          }
        }
      }

  startServer(interface = "localhost", port = 9000) {
    mainRoute
  }

}

object SlowActor {
  def props = Props(new SlowActor())
}

class SlowActor extends Actor {
  override def receive = {
    case ctx: RequestContext =>
      ctx complete {
        Thread.sleep(3000)
        "SLOW"
      }
      // we stop this actor
      self ! PoisonPill
  }
}
