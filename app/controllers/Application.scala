package controllers

import play.api._
import play.api.mvc._
import model.FileNamer
import play.api.Play.current
import play.cache.Cache

object Application extends Controller {

  def index = Action {
    Ok(views.html.welcome())
  }

  def upload = Action(parse.multipartFormData) { request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      val contentType = picture.contentType
      val is = util.IOUtil.writeFileToByteStream(picture.ref.file)
      val sessionId = java.util.UUID.randomUUID().toString()
      Cache.set(sessionId, is, 10 * 60)
      Redirect(routes.AppdfPage.apppage).withSession(
        ("sessionId" -> sessionId))
    }.getOrElse {
      Redirect(routes.Application.index).flashing(
        "error" -> "Missing file")
    }
  }

  def useDefault = Action {
    request =>
      {
        val sessionId = "demo"
        Logger.info("Action")
        Redirect(routes.AppdfPage.apppage).withSession(
            ("sessionId" -> sessionId)
           )
      }
  }

}