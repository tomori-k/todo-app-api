package controllers

import json.writes.JsValueTodoCategory
import lib.persistence.onMySQL.TodoCategoryRepository
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class TodoCategoryController @Inject() (
    val controllerComponents: ControllerComponents
)(implicit executionContext: ExecutionContext)
    extends BaseController {
  
  def getAll(): Action[AnyContent] = Action async { implicit req =>
    TodoCategoryRepository
      .getAll()
      .map(x => Ok(Json.toJson(x.map(y => JsValueTodoCategory(y)))))
  }
}
