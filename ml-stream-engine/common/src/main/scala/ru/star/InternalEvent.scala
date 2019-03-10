package ru.star

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.UUID

import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.createTypeInformation

final case class InternalEvent(id: String, configName: String, message: String) extends Serializable

object InternalEvent {
  def fromString(event: String): InternalEvent = {
    val ar = event.split(" ")
    InternalEvent(
      id = UUID.randomUUID().toString,
      configName = ar(0),
      message = ar(1)
    )
  }
}

class InternalEventDeserializer() extends DeserializationSchema[InternalEvent] {
  override def deserialize(message: Array[Byte]): InternalEvent = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(message))
    val value = ois.readObject
    ois.close()
    value.asInstanceOf[InternalEvent]
  }

  override def isEndOfStream(nextElement: InternalEvent): Boolean = false

  override def getProducedType: TypeInformation[InternalEvent] = {
    createTypeInformation[InternalEvent]
  }
}


class InternalEventSerializer extends SerializationSchema[InternalEvent] {
  override def serialize(element: InternalEvent): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(element)
    oos.close()
    stream.toByteArray
  }
}
