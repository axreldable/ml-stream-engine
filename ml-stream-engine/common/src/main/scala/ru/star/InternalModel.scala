package ru.star

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}

import io.radicalbit.flink.pmml.scala.models.control.AddMessage
import org.apache.flink.api.common.serialization.{DeserializationSchema, SerializationSchema}
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.createTypeInformation

final case class AddModelMessage(name: String, version: Long, path: String, occurredOn: Long)

final case class InternalModel(id: String, addMessage: AddModelMessage) {
  def toAddMessage: AddMessage = {
    val name = addMessage.name
    val version = addMessage.version
    val path = addMessage.path
    val occurredOn = addMessage.occurredOn

    AddMessage(name, version, path, occurredOn)
  }
}

object InternalModel {
  def fromString(model: String): InternalModel = {
    val ar = model.split(" ")

    InternalModel(
      id = "1",
      addMessage = AddModelMessage(ar(0), ar(1).toLong, ar(2), System.currentTimeMillis())
    )
  }
}

class InternalModelDeserializer() extends DeserializationSchema[InternalModel] {
  override def deserialize(message: Array[Byte]): InternalModel = {
    val ois = new ObjectInputStream(new ByteArrayInputStream(message))
    val value = ois.readObject
    ois.close()
    value.asInstanceOf[InternalModel]
  }

  override def isEndOfStream(nextElement: InternalModel): Boolean = false

  override def getProducedType: TypeInformation[InternalModel] = {
    createTypeInformation[InternalModel]
  }
}


class InternalModelSerializer extends SerializationSchema[InternalModel] {
  override def serialize(element: InternalModel): Array[Byte] = {
    val stream: ByteArrayOutputStream = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(stream)
    oos.writeObject(element)
    oos.close()
    stream.toByteArray
  }
}
