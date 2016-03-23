package io.crate.plugin.commoncrawl

import java.io.{ByteArrayInputStream, InputStream}
import java.net.URI
import java.util

import com.google.common.base.Predicate
import io.crate.operation.collect.files.FileInput
import upickle.default._

import scala.collection.mutable.ListBuffer
import scala.io.Source



case class Page(ssl: Boolean, path: String, authority: String, date: String, ctype: String, clen: Long, content: String)

trait WETParser extends Iterator[Page] {
  val _NL = "\n"

  val source: Source

  lazy val linesIterator = source.getLines()

  val blockDelimiter = "WARC/1.0"

  val metaData = ""

  private def valueOf(src: String) = src.splitAt(src.indexOf(':') + 1)._2.trim

  private def nextUntil(f: String => Boolean): Option[String] = {
    while (linesIterator.hasNext) {
      val current = linesIterator.next()
      if (f(current)) return Option(current)
    }
    return None
  }

  private def getUntil(f: String => Boolean): String = {
    val result = new ListBuffer[String]()
    while (linesIterator.hasNext) {
      val current = linesIterator.next()
      if (f(current)) return result.mkString(_NL)
      result append current
    }
    return result.mkString(_NL)
  }


  private def parseOne(): Option[Page] = {
    while (hasNext) {
      val warcType = valueOf(nextUntil(_ != blockDelimiter).get)

      if (warcType.compareToIgnoreCase("conversion") == 0) {
        val uri = new URI(valueOf(nextUntil(_.startsWith("WARC-Target-URI:")).get))
        val zonedDate = valueOf(nextUntil(_.startsWith("WARC-Date:")).get)
        val contentType = valueOf(nextUntil(_.startsWith("Content-Type:")).get)
        val contentLength = valueOf(nextUntil(_.startsWith("Content-Length:")).get).toLong
        linesIterator.next()

        val content = getUntil(_ == blockDelimiter)
        val domain = uri.getHost

        val reverseDomain = if(uri.getPort > 0) s"${domain.split('.').reverse.mkString(".")}:${uri.getPort}" else s"${domain.split('.').reverse.mkString(".")}"
        val path = Option(uri.getPath).getOrElse("/")
        val queryStr = if(uri.getQuery != null) s"?${uri.getQuery}" else ""
        val fragmStr = if(uri.getFragment != null ) s"#${uri.getFragment}" else ""

        return Option(new Page(uri.getScheme == "https", s"$path$queryStr$fragmStr", reverseDomain, zonedDate, contentType, contentLength, content))
      }
    }
    return Option.empty
  }

  override def hasNext: Boolean = linesIterator.hasNext

  override def next(): Page = parseOne().getOrElse({
    source.close()
    throw new Exception("No more items")
  })
}

class WETFileInput extends FileInput {
  override def listUris(uri: URI, predicate: Predicate[URI]): util.List[URI] = util.Arrays.asList(uri)

  override def getStream(uri: URI): InputStream = {
    val p = new WETParser {
      override val source: Source = Source.fromURI(uri)
    }
    new ByteArrayInputStream(p.map(write(_).toByte).toArray)
  }

  override def sharedStorageDefault(): Boolean = true
}
