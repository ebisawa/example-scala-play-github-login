package infrastructure.auth

import play.api.libs.ws._

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Try

case class OAuth2Config(
  clientId: String,
  clientSecret: String,
  signInUrl: String,
  accessTokenUrl: String,
)

class OAuth2(conf: OAuth2Config, ws: WSClient)(implicit ec: ExecutionContext) {
  def signInUrl(redirectUrl: String): String = {
    conf.signInUrl +
      "?client_id=" + conf.clientId +
      "&redirect_uri=" + redirectUrl +
      "&state=12345"
  }

  def accessToken(code: String): Try[String] = {
    val f = accessTokenAsync(code)

    Await.ready(f, Duration.Inf)
    f.value.get
  }

  def accessTokenAsync(code: String): Future[String] = {
    val data = Map(
      "client_id" -> conf.clientId,
      "client_secret" -> conf.clientSecret,
      "code" -> code,
      "state" -> "abcde",
    )

    ws.url(conf.accessTokenUrl)
      .addHttpHeaders("accept" -> "application/json")
      .post(data)
      .map { res => (res.json \ "access_token").as[String] }
  }
}
