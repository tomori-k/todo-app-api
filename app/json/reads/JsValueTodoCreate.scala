package json.reads

import play.api.libs.json.{Json, Reads}

case class JsValueTodoCreate(
    title:      String,
    body:       String,
    categoryId: Long
)

object JsValueTodoCreate {
  implicit val reads: Reads[JsValueTodoCreate] = Json.reads[JsValueTodoCreate]
}
