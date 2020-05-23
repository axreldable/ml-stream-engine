package ru.star

object Helpers {
  def resourceAbsolutePath(resource: String): String = {
    try {
      this.getClass.getResource(resource).getPath
    } catch {
      case _: NullPointerException => throw new NullPointerException(s"Perhaps resource $resource not found.")
    }
  }
}
