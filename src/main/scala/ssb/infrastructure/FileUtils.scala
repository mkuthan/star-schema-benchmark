package ssb.infrastructure

import scala.io.Source

object FileUtils {
  def content(filename: String): String = Source.fromFile(filename).mkString
}
