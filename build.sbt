name := "ccpp"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test",
  "com.lihaoyi" %% "upickle" % "0.3.8"
)

val excluded = Seq(
  "antlr-2.7.7.jar",
  "antlr-runtime-3.5.jar",
  "apache-log4j-extras-1.2.17.jar",
  "asm-4.1.jar",
  "asm-commons-4.1.jar",
  "asm-tree-4.1.jar",
  "aws-java-sdk-core-1.10.12.jar",
  "aws-java-sdk-ec2-1.10.12.jar",
  "aws-java-sdk-kms-1.10.12.jar",
  "aws-java-sdk-s3-1.10.12.jar",
  "commons-cli-1.2.jar",
  "commons-codec-1.9.jar",
  "commons-lang3-3.3.2.jar",
  "commons-logging-1.1.3.jar",
  "commons-math3-3.4.1.jar",
  "compiler-0.8.13.jar",
  "compress-lzf-1.0.2.jar",
  "crate-app-0.55.0-SNAPSHOT-bc2b86c.jar",
  "dnsjava-2.1.7.jar",
  "groovy-all-2.4.4.jar",
  "guava-18.0.jar",
  "hppc-0.6.0.jar",
  "httpclient-4.3.6.jar",
  "httpcore-4.3.3.jar",
  "jackson-annotations-2.5.0.jar",
  "jackson-core-2.5.3.jar",
  "jackson-databind-2.5.3.jar",
  "jackson-dataformat-cbor-2.5.3.jar",
  "jackson-dataformat-smile-2.5.3.jar",
  "jackson-dataformat-yaml-2.5.3.jar",
  "jakarta-regexp-1.4.jar",
  "jna-4.1.0.jar",
  "joda-convert-1.2.jar",
  "joda-time-2.8.1.jar",
  "jsr305-1.3.9.jar",
  "jts-1.13.jar",
  "log4j-1.2.17.jar",
  "lucene-analyzers-common-4.10.4.jar",
  "lucene-core-4.10.4.jar",
  "lucene-expressions-4.10.4.jar",
  "lucene-grouping-4.10.4.jar",
  "lucene-highlighter-4.10.4.jar",
  "lucene-join-4.10.4.jar",
  "lucene-memory-4.10.4.jar",
  "lucene-misc-4.10.4.jar",
  "lucene-queries-4.10.4.jar",
  "lucene-queryparser-4.10.4.jar",
  "lucene-sandbox-4.10.4.jar",
  "lucene-spatial-4.10.4.jar",
  "lucene-suggest-4.10.4.jar",
  "netty-3.10.5.Final.jar",
  "slf4j-api-1.6.2.jar",
  "snakeyaml-1.12.jar",
  "spatial4j-0.4.1.jar",
  "stringtemplate-3.2.1.jar",
  "t-digest-3.0.jar",
  "xbean-bundleutils-4.2.jar",
  "xbean-finder-4.2.jar"
)

assemblyExcludedJars in assembly := {
  val cp = (fullClasspath in assembly).value
  cp filter (c => excluded.contains(c.data.getName))
}