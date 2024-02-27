package json.writes

import lib.model.Todo
import play.api.libs.json.{Json, Writes}

case class JsValueTodo(
    id:       Long,
    title:    String,
    body:     String,
    state:    Short,
    category: Option[String] // todo: category も JsValue を定義する
)

object JsValueTodo {
  implicit val writes: Writes[JsValueTodo] = Json.writes[JsValueTodo]

  def apply(todo: Todo.EmbeddedId): JsValueTodo =
    JsValueTodo(
      id       = todo.id,
      title    = todo.v.title,
      body     = todo.v.body,
      state    = todo.v.state.code,
      category = todo.v.category.map(_.name)
    )
}
