package modules

import com.google.inject.AbstractModule
import infrastructure.auth.{GitHubAuthConfig, OAuth2Config}

class GitHubAppKeyModule extends AbstractModule {
  override def configure(): Unit = {
    val authConfig = GitHubAuthConfig("xxxxxxxx", "xxxxxxxx")
    bind(classOf[OAuth2Config]).toInstance(authConfig)
  }
}
