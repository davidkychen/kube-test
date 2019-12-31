import skuber._
import skuber.json.format._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import scala.util.{Success, Failure}

object Test {
  def main(args: Array[String]): Unit = {
    if(args.length < 1){
      println("Usage: java -jar this.jar <namespace>")
    } else {
      implicit val system = ActorSystem()
      implicit val materializer = ActorMaterializer()
      implicit val dispatcher = system.dispatcher

      val k8sConfig = K8SConfiguration.parseKubeconfigFile()
      val k8s = k8sInit
      val listPodsRequest = k8s.listInNamespace[PodList](args(0))
      listPodsRequest.onComplete {
        case Success(pods) => pods.items.foreach { p => println(p.name) }
        case Failure(e) => throw(e)
      }
    }
  }
}
