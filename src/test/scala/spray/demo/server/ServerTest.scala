package spray.demo.server

import org.scalatest.FreeSpec
import org.scalatest.Matchers

import spray.http.StatusCodes
import spray.testkit.ScalatestRouteTest

class ServerTest extends FreeSpec with Matchers with ScalatestRouteTest {
  
  "GET /hello should work" in {
    Get("/hello") ~> Server.mainRoute ~> check {
      handled should be (true)
      status should be (StatusCodes.OK)
      responseAs[String] should equal ("Hello")
    }
  }

}