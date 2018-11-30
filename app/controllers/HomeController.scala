package controllers

import controllers.usecases.GitHubLogin
import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, github: GitHubLogin, authAction: AuthorizedAction)
                              (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index() = Action { implicit request =>
    Ok(views.html.index.index())
  }

  def login() = Action { implicit request =>
    Redirect(github.loginUrl("http://localhost:9000/loggedIn"))
  }

  def loggedIn(code: String, state: String) = Action.async { implicit request =>
    github.loggedInAsync(code, state).map {
      case Some(user) =>
        Redirect(routes.HomeController.home).withUserSession(user)
      case None =>
        Redirect(routes.HomeController.index).withNewSession
    }
  }

  def logout() = Action { implicit request =>
    Redirect(routes.HomeController.index).withNewSession
  }

  def home() = authAction { implicit request =>
    println(s"XXX userId=${request.userId}")

    Ok(views.html.home())
  }
}
