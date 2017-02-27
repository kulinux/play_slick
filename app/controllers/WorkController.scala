package controllers

import javax.inject.Inject

import models.Work
import play.api.libs.json.Json._
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.{Action, Controller}
import repo.WorkRepository
import utils.Constants
import utils.JsonFormat._

import scala.concurrent.Future

/**
  * Created by fbenitez on 26/02/2017.
  */
class WorkController @Inject() (workRepository: WorkRepository) extends Controller {

  def list() = Action.async {
    workRepository.getAll().map( res =>
     Ok(successResponse(Json.toJson(res), "List ok"))
    )
  }

  def create() = Action.async(parse.json) { request =>
    request.body.validate[Work].fold(error => Future.successful(BadRequest(JsError.toJson(error))), { emp =>
      workRepository.insert(emp).map { id =>
        Ok(successResponse(Json.toJson(Map("id" -> id)), "Work Created"))
      }
    })

  }

  private def successResponse(data: JsValue, message: String) = {
    obj("status" -> Constants.SUCCESS, "data" -> data, "msg" -> message)
  }


}
