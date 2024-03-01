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

@Singleton
class TodoController @Inject() (val controllerComponents: ControllerComponents)(
    implicit executionContext: ExecutionContext
) extends BaseController {

  def getAll: Action[AnyContent] = Action async { implicit req =>
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

  def get(id: Long): Action[AnyContent] = Action async { implicit req =>
    for {
      todoItem     <- TodoRepository.get(Todo.Id(id))
      todoCategory <- todoItem match {
                        case Some(todoItem) =>
                          TodoCategoryRepository.get(
                            TodoCategory.Id(todoItem.v.categoryId)
                          )
                        case None           => Future.successful(None)
                      }
    } yield todoItem match {
      case Some(todoItem) => {
        val todoWithCategory =
          todoItem.v.copy(category = todoCategory.map(_.v)).toEmbeddedId
        Ok(
          Json.toJson(
            JsValueTodo(todoWithCategory)
          )
        )
      }
      case None           => NotFound
    }
  }

  def update(): Action[JsValue] = Action(parse.json).async { implicit req =>
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
                            .map(_ => Ok)
                        case None    =>
                          Future.successful(
                            NotFound("Not a such ID")
                          )
                      }
          } yield result
      )
  }

  def delete(id: Long): Action[AnyContent] = Action async { implicit req =>
    TodoRepository
      .remove(Todo.Id(id))
      .map(_ => Ok)
  }
}
