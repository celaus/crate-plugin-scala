package io.crate.plugin.commoncrawl

import java.io.IOException
import java.util

import io.crate.operation.collect.files.{FileInput, FileInputFactory}
import io.crate.plugin.AbstractPlugin
import org.elasticsearch.common.inject.multibindings.MapBinder
import org.elasticsearch.common.inject.{AbstractModule, Module}
import org.elasticsearch.common.settings.Settings

class CommonCrawlModule extends AbstractModule {
  override def configure(): Unit = {
    val b = MapBinder.newMapBinder(binder(), classOf[String], classOf[FileInputFactory])

    b.addBinding(CommonCrawlInputFactory.NAME).to(classOf[CommonCrawlInputFactory]).asEagerSingleton()
  }
}

object CommonCrawlInputFactory {
  val NAME: String = "ccrawl"
}

class CommonCrawlInputFactory extends FileInputFactory {
  @throws(classOf[IOException])
  def create: FileInput = {
    return new WETFileInput
  }
}

class CrateCommonCrawlPlugin extends AbstractPlugin {
  def this(settings: Settings) {
    this()
  }

  def name: String = {
    return "crate-commoncrawl"
  }

  def description: String = {
    return "Plugin to import common crawl data form http endpoints"
  }

  override def modules(settings: Settings): util.Collection[Module] = {
    val r = super.modules(settings)
    r.add(new CommonCrawlModule)
    return r
  }

}