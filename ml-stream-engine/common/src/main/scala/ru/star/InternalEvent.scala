package ru.star

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.createTypeInformation

/**
  * Inner representation of Event.
  * All messages transform into this representation in input-adapter.
  * And core of the system works with this events only.
  *
  * @param id      - unique message identifier
  * @param message - message context
  */
// todo: replace message with event representation
final case class InternalEvent(id: String,
                               messageTimestamp: Long,
                               createTimestamp: Long,
                               message: Option[String]) extends Serializable

object InternalEvent {
  def fromString(event: String): InternalEvent = {
    InternalEvent(
      id = "1",
      messageTimestamp = 1,
      createTimestamp = 1,
      message = Option(event)
    )
  }

  def toString(event: InternalEvent): String = {
    event.message.getOrElse("empty message")
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
