package lib.model

import ixias.model._
import ixias.util.json.JsonEnvReads
import lib.model.TodoCategory.Id
import play.api.libs.json.Reads

import java.time.LocalDateTime

case class TodoCategory(
    id:        Option[Id],
    name:      String,
    color:     String,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

object TodoCategory extends JsonEnvReads {
  val Id = the[Identity[Id]]
  type Id         = Long @@ TodoCategory
  type EmbeddedId = Entity.EmbeddedId[Id, TodoCategory]

  implicit val todoCategoryIdReads: Reads[TodoCategory.Id] = idAsNumberReads
}
