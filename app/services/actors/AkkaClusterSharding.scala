package services.actors

import akka.actor._
import akka.cluster.sharding._
import play.api.Configuration

/**
 * Created by peixiaobin on 2020/11/25.
 */
trait AkkaClusterSharding {

  def shardName: String

  def props: Props

  def startRegion(configuration: Configuration, actorSystem: ActorSystem): ActorRef = {
    //Based on Akka Docs, the number of shards should be a factor
    //ten greater than the planned maximum number of cluster nodes.
    val numberOfShards = configuration
      .getOptional[Int]("akka.cluster.number_of_nodes")
      .getOrElse(100) * 12

    ClusterSharding(actorSystem).start(
      typeName = shardName,
      entityProps = props,
      settings = ClusterShardingSettings(actorSystem),
      extractEntityId = extractEntityId,
      extractShardId = extractShardId(numberOfShards)
    )
  }

  def getRegion(system: ActorSystem): ActorRef = {
    ClusterSharding(system).shardRegion(shardName)
  }

  def extractEntityId: ShardRegion.ExtractEntityId = {
    case env@Envelope(id, _) => (id.toString, env)
  }

  def extractShardId(numberOfShards: Int): ShardRegion.ExtractShardId = {
    case Envelope(id, _) => (math.abs(id.hashCode) % numberOfShards).toString
  }
}
