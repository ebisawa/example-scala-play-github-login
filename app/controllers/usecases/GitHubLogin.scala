package controllers.usecases

import domain.entities.User
import infrastructure.auth.GitHubApi
import javax.inject._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class GitHubLogin @Inject()(github: GitHubApi)(implicit ec: ExecutionContext) {
  def loginUrl(redirectUrl: String): String = {
    github.signInUrl(redirectUrl)
  }

  def loggedInAsync(code: String, state: String): Future[Option[User]] = {
    // TODO: check state
    github.accessTokenAsync(code).flatMap { token =>
      github.userAsync(token).map(_.toOption)
    }
  }
}
