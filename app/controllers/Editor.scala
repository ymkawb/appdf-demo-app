package controllers

import play.api._
import play.api.mvc._

/**
 * Controller serving AppDF editor requests for js/data/css
 */
object Editor extends Controller {
   
  def editor() = Action {    
    Redirect("http://www.onepf.org/editor/")
  }
}