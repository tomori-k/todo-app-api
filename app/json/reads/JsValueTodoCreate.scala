package json.reads

import lib.model.TodoCategory
import play.api.libs.json.{Json, Reads}

case class JsValueTodoCreate(
    title:      String,
    body:       String,
    categoryId: TodoCategory.Id
)

object JsValueTodoCreate {
  implicit val reads: Reads[JsValueTodoCreate] = Json.reads[JsValueTodoCreate]
}
