package json.reads

import play.api.libs.json.{Json, Reads}

case class JsValueTodoUpdate(
    id:         Long,
    title:      String,
    body:       String,
    categoryId: Long,
    state:      Short
)

object JsValueTodoUpdate {
  implicit val reads: Reads[JsValueTodoUpdate] = Json.reads[JsValueTodoUpdate]
}
