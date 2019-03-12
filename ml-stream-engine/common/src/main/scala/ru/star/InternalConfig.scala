package ru.star

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.UUID

import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.createTypeInformation

final case class ModelConfig(path: String,
                             name: String,
                             version: Long)

final case class TransformConfig(funcName: String)

final case class EventConfig(triggerValue: Double,
                             modelConfig: ModelConfig,
                             transformConfig: TransformConfig)

final case class InternalConfig(version: String, eventConfigs: Map[String, EventConfig], key: String = "1") extends Serializable

object InternalConfig {
  def fromString(config: String): InternalConfig = {
    InternalConfig(
      version = UUID.randomUUID().toString,
      eventConfigs = getEventConfigs(config)
    )
  }

  def getEventConfigs(config: String): Map[String, EventConfig] = {
    Map(
      "train_event" ->
        EventConfig(
          0.8,
          ModelConfig("./pmml_source/simple.pmml", "c056b84c-6c58-42b7-b98e-90da537995a2", 1),
          TransformConfig("fraudTransform")
        )
    )
  }

  def default: InternalConfig = {
    InternalConfig(
      version = "default_version",
      eventConfigs = Map.empty[String, EventConfig]
    )
  }
}

class InternalConfigDeserializer() extends DeserializationSchema[InternalConfig] {
  override def deserialize(message: Array[Byte]): InternalConfig = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(message))
    val value = ois.readObject
    ois.close()
    value.asInstanceOf[InternalConfig]
  }

  override def isEndOfStream(nextElement: InternalConfig): Boolean = false

  override def getProducedType: TypeInformation[InternalConfig] = {
    createTypeInformation[InternalConfig]
  }
}


class InternalConfigSerializer extends SerializationSchema[InternalConfig] {
  override def serialize(element: InternalConfig): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(element)
    oos.close()
    stream.toByteArray
  }
}
