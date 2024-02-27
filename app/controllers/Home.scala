/** to do sample project
  */

package controllers

import json.reads.{JsValueTodoCreate, JsValueTodoUpdate}
import json.writes.JsValueTodo
import lib.model.Todo.TodoState
import lib.model.{Todo, TodoCategory}
import lib.persistence.default._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

case class CreateFormData(
    title:      String,
    body:       String,
    categoryId: Long
)

case class UpdateFormData(
    title:      String,
    body:       String,
    categoryId: Long,
    stateValue: Short
)

@Singleton
class HomeController @Inject() (val controllerComponents: ControllerComponents)(
    implicit executionContext: ExecutionContext
) extends BaseController {

  def list(): Action[AnyContent] = Action async { implicit req =>
    for {
      todoItems <- TodoRepository.getAllWithCategory()
    } yield Ok(
      Json.toJson(todoItems.map(x => JsValueTodo(x)))
    )
  }

  def create(): Action[JsValue] = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueTodoCreate]
      .fold(
        _ => {
          Future.successful(BadRequest("Invalid body."))
        },
        todoCreate => {
          TodoRepository
            .add(
              new Todo(
                id         = None,
                categoryId = TodoCategory.Id(todoCreate.categoryId),
                title      = todoCreate.title,
                body       = todoCreate.body,
                state      = TodoState.Todo
              ).toWithNoId
            )
            .map(_ => Ok)
        }
      )
  }

  def todoa(id: Long): Action[AnyContent] = Action async { implicit req =>
    for {
      todoItem <- TodoRepository.get(Todo.Id(id))
    } yield todoItem match {
      case Some(todoItem) => Ok(Json.toJson(JsValueTodo(todoItem)))
      case None           => NotFound
    }
  }

  def updatea(): Action[JsValue] = Action(parse.json).async { implicit req =>
    req.body
      .validate[JsValueTodoUpdate]
      .fold(
        _ => Future.successful(BadRequest("Invalid body")),
        todoUpdate =>
          for {
            todo   <- TodoRepository.get(Todo.Id(todoUpdate.id))
            result <- todo match {
                        case Some(x) =>
                          TodoRepository
                            .update(
                              x.map(
                                _.copy(
                                  title      = todoUpdate.title,
                                  body       = todoUpdate.body,
                                  categoryId =
                                    TodoCategory.Id(todoUpdate.categoryId),
                                  state      = TodoState(todoUpdate.state)
                                )
                              )
                            )
                            .map(_ => Redirect(routes.HomeController.list()))
                        case None    =>
                          Future.successful(
                            NotFound("Not a such ID")
                          )
                      }
          } yield result
      )
  }

  def deletea(id: Long): Action[AnyContent] = Action async { implicit req =>
    TodoRepository
      .remove(Todo.Id(id))
      .map(_ => Ok)
  }
}
