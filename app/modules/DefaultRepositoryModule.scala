package modules

import com.google.inject.AbstractModule
import domain.repositories._
import infrastructure.scalikejdbc.repositories._

class DefaultRepositoryModule extends AbstractModule {
  override def configure(): Unit = {
    // repositories
    bind(classOf[UserRepository]).toInstance(ScalikeUserRepositoryImpl)
    bind(classOf[GitHubUserRepository]).toInstance(ScalikeGitHubUserRepositoryImpl)
  }
}
