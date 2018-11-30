name := """github-login-test"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.7"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// project settings
libraryDependencies ++= Seq(
  ws,
  "mysql" % "mysql-connector-java" % "8.0.+",
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.3.+",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.3.+",
  "org.scalikejdbc" %% "scalikejdbc-test"             % "3.3.+",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.6.0-scalikejdbc-3.3",
)

// scalikejdbc
enablePlugins(ScalikejdbcPlugin)

scalikejdbcGeneratorSettings in Compile ~= {
  c => c.copy(tableNameToClassName = x => c.tableNameToClassName(x).replaceAll("Github", "GitHub") + "Mapper")
}


// Flyway
import com.typesafe.config.ConfigFactory
val appConfig = ConfigFactory.parseFile(new File("./conf/application.conf"))

flywayUrl := appConfig.getString("db.default.url")
flywayUser := appConfig.getString("db.default.username")
flywayPassword := appConfig.getString("db.default.password")
flywayLocations := Seq("filesystem:conf/db/migrations")

enablePlugins(FlywayPlugin)
