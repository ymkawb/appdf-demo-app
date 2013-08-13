package controllers

import play.api.mvc.Controller
import play.api.libs.concurrent.Promise
import play.api.libs.concurrent.Akka
import play.api.Play.current
import play.api._
import play.api.mvc._
import org.onepf.appdf.parser.AppdfFileParser
import org.onepf.appdf.model.{ Application => AppDfApp }
import org.onepf.appdf.parser.ParseResult
import model._
import util._
import java.io.File
import java.io.FileOutputStream
import scala.collection.JavaConversions._
import org.onepf.appdf.parser.AppdfStreamParser
import play.cache.Cache

object AppdfPage extends Controller {

  def apppage = Action { request =>
    {
      val sessionIdOption = request.session.get("sessionId")
      if (sessionIdOption.isEmpty || sessionIdOption.get == "demo")
        getStorePage("./demo/life.appdf")
      else
        getStorePage(sessionIdOption.get, true)
    }
  }

  private def getStorePage(fileOrId: String, isSessionId: Boolean = false) = {

    val parseResult = if (isSessionId) {
      val is = Cache.get(fileOrId).asInstanceOf[java.io.InputStream]
      new AppdfStreamParser(is).parse
    } else {
      new AppdfFileParser(fileOrId).parse
    }

    Logger.info(">>compute")
    val app = parseResult.getApplication()
    if (app != null) {
      val sessionId = if (isSessionId) fileOrId else "demo"
      Cache.set(sessionId, parseResult, 10 * 60) //Storing for ten minutes seems good enough              
      Ok(views.html.app_page(app)).withSession(
        ("sessionId" -> sessionId))
    } else {
      PreconditionFailed // think of better 
    }

    //    val promiseOfHeader: Promise[ParseResult] = Akka.future {
    //      Logger.info(">>promise")
    //      if (isSessionId) {
    //        val is = Cache.get(fileOrId).asInstanceOf[java.io.InputStream]        
    //        new AppdfStreamParser(is).parse
    //      } else {
    //        new AppdfFileParser(fileOrId).parse
    //      }
    //    }
    //    Async {
    //      promiseOfHeader map {
    //        parseResult =>
    //          {           
    //            Logger.info(">>compute")
    //            val app = parseResult.getApplication()
    //            if (app != null) {
    //              val sessionId = if (isSessionId) fileOrId else "demo"              
    //              Cache.set(sessionId, parseResult, 10 * 60) //Storing for ten minutes seems good enough              
    //              Ok(views.html.app_page(app)).withSession(
    //                ("sessionId" -> sessionId))
    //            } else {
    //              PreconditionFailed // think of better 
    //            }
    //          }
    //      }
    //    }
  }
}