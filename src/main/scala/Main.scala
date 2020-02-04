import skuber._
import skuber.json.format._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.apache.spark.launcher.SparkLauncher

import scala.util.{Failure, Success}

object Test {
  def main(args: Array[String]): Unit = {
    if(args.length < 3){
      println("Usage: java -jar this.jar <k8s api server> <jar path> <spark image>")
    } else {
//      implicit val system = ActorSystem()
//      implicit val materializer = ActorMaterializer()
//      implicit val dispatcher = system.dispatcher
//
//      val k8sConfig = K8SConfiguration.parseKubeconfigFile()
//      val k8s = k8sInit
//      val listPodsRequest = k8s.listInNamespace[PodList](args(0))
//      listPodsRequest.onComplete {
//        case Success(pods) => pods.items.foreach { p => println(p.name) }
//        case Failure(e) => throw(e)
//      }
      val launcher = new SparkLauncher().setDeployMode("cluster")
                    .setAppName("spark-kube-test")
                    .setMainClass("ValueZones")
//                    .setVerbose(true)
                    .setMaster(args(0))
                    .setAppResource(args(1))
                    .setConf("spark.kubernetes.container.image", args(2))
                    .setConf("spark.kubernetes.authenticate.driver.serviceAccountName","spark")
                    .setConf("spark.hadoop.fs.AbstractFileSystem.gs.impl","com.google.cloud.hadoop.fs.gcs.GoogleHadoopFS")
                    .setConf("spark.kubernetes.container.image.pullPolicy","Always")
      launcher.addAppArgs("gs://tinderbox/user/david/yellow_tripdata_2018*.csv")
      launcher.addAppArgs("gs://tinderbox/user/david/green_tripdata_2018*.csv")
      launcher.addAppArgs("gs://tinderbox/user/david/taxi _zone_lookup.csv")
      launcher.addAppArgs("gs://tinderbox/user/david/kube-test/result")
      launcher.startApplication()

    }
  }
}
