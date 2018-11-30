package infrastructure.auth

import java.time.ZonedDateTime

import domain.entities.{GitHubForeignUserId, GitHubUser, User}
import domain.repositories.{GitHubUserRepository, UserRepository}
import javax.inject._
import play.api.libs.json._
import play.api.libs.ws._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util._

object GitHubAuthConfig {
  val authorizeUrl = "https://github.com/login/oauth/authorize"       // GET
  val accessTokenUrl = "https://github.com/login/oauth/access_token"  // POST

  def apply(clientId: String, clientSecret: String): OAuth2Config = {
    OAuth2Config(clientId, clientSecret, authorizeUrl, accessTokenUrl)
  }
}

@Singleton
class GitHubApi @Inject()(guRepo: GitHubUserRepository, userRepo: UserRepository, ws: WSClient, conf: OAuth2Config)
                         (implicit ec: ExecutionContext) extends OAuth2(conf, ws)(ec) {

  case class GitHubUserData(
                             id: Long,
                             node_id: String,
                             login: String,
                             name: String,
                             email: String,
                             created_at: ZonedDateTime,
                             updated_at: ZonedDateTime,
                           )

  val apiBase = "https://api.github.com"

  implicit val personReads = Json.reads[GitHubUserData]

  def userAsync(token: String): Future[Try[User]] = {
    apiGet(token, "/user")
      .map(_.json.validate[GitHubUserData])
      .map { jsResult =>
        jsResult.fold(
          e => Failure(new Exception(e.toString())),
          r => findCreateUser(r) match {
            case Some(user) => Success(user)
            case None => Failure(new Exception("user create failed"))
          }
        )
      }
  }

  def await[T](f: Future[Try[T]]): Try[T] = {
    Await.ready(f, Duration.Inf)
    f.value.get.flatten
  }

  private def findCreateUser(data: GitHubUserData): Option[User] = {
    val guId = GitHubForeignUserId(data.id)

    guRepo.findByForeignId(guId) match {
      case Some(ghu) =>
        userRepo.find(ghu.userId)
      case None =>
        userRepo.create(User(None, data.name)).map { user =>
          val gu = GitHubUser(None, user.id.get, guId, data.node_id,
            data.login, data.name, data.email,
            data.created_at, data.updated_at)

          guRepo.create(gu)
          user
        }
    }
  }

  private def apiGet(token: String, path: String) = wsRequest(token, path).get()

  private def wsRequest(token: String, path: String) = ws.url(apiBase + path).addHttpHeaders(authToken(token))

  private def authToken(token: String) = "authorization" -> s"token $token"
}
