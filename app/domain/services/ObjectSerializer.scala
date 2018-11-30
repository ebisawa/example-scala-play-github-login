package domain.services

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.Base64

object ObjectSerializer {
  def serialize[T](obj: T): String = {
    val buf = new ByteArrayOutputStream()
    val oos = new ObjectOutputStream(buf)

    oos.writeObject(obj)
    oos.close()

    Base64.getEncoder.encodeToString(buf.toByteArray())
  }

  def deserialize[T](str: String): T = {
    val bytes = Base64.getDecoder().decode(str)
    val bis = new ByteArrayInputStream(bytes)
    val ois = new ObjectInputStream(bis)

    ois.readObject().asInstanceOf[T]
  }
}
