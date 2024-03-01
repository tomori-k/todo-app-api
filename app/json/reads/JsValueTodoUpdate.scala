package json.reads

import lib.model.{Todo, TodoCategory}
import play.api.libs.json.{Json, Reads}

case class JsValueTodoUpdate(
    id:         Todo.Id,
    title:      String,
    body:       String,
    categoryId: TodoCategory.Id,
    state:      Short
)

object JsValueTodoUpdate {
  implicit val reads: Reads[JsValueTodoUpdate] = Json.reads[JsValueTodoUpdate]
}
