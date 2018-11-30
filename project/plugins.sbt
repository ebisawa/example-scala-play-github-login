addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.18")

resolvers += "Flyway" at "https://davidmweber.github.io/flyway-sbt.repo"
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "5.2.0")

libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.+"
//libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.+"
addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "3.3.1")
