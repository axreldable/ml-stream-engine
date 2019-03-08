package ru.star

import io.radicalbit.flink.pmml.scala.models.control.{AddMessage, ServingMessage}
import org.apache.flink.streaming.api.functions.source.SourceFunction

class FiniteSource(mappingIdPath: Map[String, String], maxInterval: Long) extends SourceFunction[ServingMessage] {


  // todo: example with control stream
  override def cancel(): Unit = {}

  override def run(ctx: SourceFunction.SourceContext[ServingMessage]): Unit =
    mappingIdPath.foreach { idPath =>
      val (id, path) = idPath
      ctx.getCheckpointLock.synchronized {
        ctx.collect(AddMessage(id, 1, path, System.currentTimeMillis()))
      }

    }

}