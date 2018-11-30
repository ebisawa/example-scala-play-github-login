import domain.entities.{User, UserId}
import domain.services.ObjectSerializer
import javax.inject._
import play.api.i18n.MessagesApi
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

package object controllers {
  val sessionKeyName = "uid"
  val timeStampKeyName = "ts"

  class AuthorizedRequest[A](val userId: UserId, request: MessagesRequest[A]) extends WrappedRequest[A](request) {
    def messagesApi = request.messagesApi
  }

  @Singleton
  class AuthorizedAction @Inject()(playBodyParsers: PlayBodyParsers, messagesApi: MessagesApi)(implicit val executionContext: ExecutionContext)
    extends ActionBuilder[AuthorizedRequest, AnyContent] with Results {

    override def parser: BodyParser[AnyContent] = playBodyParsers.anyContent

    override def invokeBlock[A](request: Request[A], block: AuthorizedRequest[A] => Future[Result]): Future[Result] = {
      request.session.get(sessionKeyName) match {
        case Some(encodedUserId) =>
          val userId = ObjectSerializer.deserialize[UserId](encodedUserId)
          val msgReq = new MessagesRequest(request, messagesApi)
          block(new AuthorizedRequest(userId, msgReq))
        case None =>
          Future(Unauthorized)
      }
    }
  }

  implicit class RichResult(self: Result) {
    def withUserSession(user: User): Result = {
      user.id.fold(self) { id =>
        self.withSession(
          sessionKeyName -> ObjectSerializer.serialize(id),
          timeStampKeyName -> unixTime().toString,
        )
      }
    }
  }

  private def unixTime(): Long = {
    System.currentTimeMillis() / 1000L
  }
}
