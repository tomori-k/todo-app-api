package json.reads

import lib.model.Todo.TodoState
import lib.model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Reads}

case class JsValueTodoUpdate(
    id:         Todo.Id,
    title:      String,
    body:       String,
    categoryId: TodoCategory.Id,
    state:      TodoState
)

object JsValueTodoUpdate {
  implicit val reads: Reads[JsValueTodoUpdate] = Json.reads[JsValueTodoUpdate]
}
