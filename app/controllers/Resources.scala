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
import play.cache.Cache
import play.api.libs.iteratee.Enumerator

object Resources extends Controller {

  import java.io.File
  
  
  def image(fileName: String) = Action { request =>
    {               
      streamResponse(fileName, request).as("image/jpeg")      
    }
  }

  def binary(fileName: String) = Action { request =>
    {
     streamResponse(fileName, request).as("application/vnd.android.package-archive")
    }
  }
  
  private def streamResponse(fileName: String, request: play.api.mvc.Request[play.api.mvc.AnyContent]): play.api.mvc.ChunkedResult[Array[Byte]] = {      
    val sessionId = request.session.get("sessionId").getOrElse("")
    val parseResult: ParseResult = Cache.get(sessionId).asInstanceOf[ParseResult]     
    Ok.stream(Enumerator.fromStream(parseResult.getEntryStream(fileName)))
  }
}