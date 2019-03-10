package ru.star

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.createTypeInformation

final case class ConfiguredEventWithPrediction(id: String, configuredEvent: ConfiguredEvent, predictedValue: Double)

class ConfiguredEventWithPredictionDeserializer() extends DeserializationSchema[ConfiguredEventWithPrediction] {
  override def deserialize(message: Array[Byte]): ConfiguredEventWithPrediction = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(message))
    val value = ois.readObject
    ois.close()
    value.asInstanceOf[ConfiguredEventWithPrediction]
  }

  override def isEndOfStream(nextElement: ConfiguredEventWithPrediction): Boolean = false

  override def getProducedType: TypeInformation[ConfiguredEventWithPrediction] = {
    createTypeInformation[ConfiguredEventWithPrediction]
  }
}


class ConfiguredEventWithPredictionSerializer extends SerializationSchema[ConfiguredEventWithPrediction] {
  override def serialize(element: ConfiguredEventWithPrediction): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(element)
    oos.close()
    stream.toByteArray
  }
}