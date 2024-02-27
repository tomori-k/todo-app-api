package json.writes

import lib.model.TodoCategory
import play.api.libs.json.{Json, Writes}

case class JsValueTodoCategory(
    id:    Long,
    name:  String,
    color: String
)

object JsValueTodoCategory {
  implicit val writes: Writes[JsValueTodoCategory] =
    Json.writes[JsValueTodoCategory]

  def apply(todoCategory: TodoCategory.EmbeddedId): JsValueTodoCategory = {
    JsValueTodoCategory(
      id    = todoCategory.id,
      name  = todoCategory.v.name,
      color = todoCategory.v.color
    )
  }
}
