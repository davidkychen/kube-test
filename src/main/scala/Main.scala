import skuber._
import skuber.json.format._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.util.{Success, Failure}

object Main extends App {
  implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()
implicit val dispatcher = system.dispatcher

val k8s = k8sInit
val listPodsRequest = k8s.listInNamespace[PodList]("kube-system")
listPodsRequest.onComplete {
  case Success(pods) => pods.items.foreach { p => println(p.name) }
  case Failure(e) => throw(e)
}
  println("Hello, World!")
}
