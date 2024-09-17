package ssb

import java.io.File

import scala.io.Source

object FileUtils {
  def files(dir: String, fileSuffix: String = ".sql"): Seq[(String, String)] =
    new File(dir)
      .listFiles
      .toSeq
      .filter(_.isFile)
      .filter(_.getName.endsWith(fileSuffix))
      .map { file =>
        val scenario = file.getName.stripSuffix(fileSuffix)
        val sql = Source.fromFile(file).mkString
        scenario -> sql
      }
      .sortBy(_._1)
}
