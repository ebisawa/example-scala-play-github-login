package domain.entities

trait UserId extends EntityId

case class User(
  id: Option[UserId],
  name: String,
)
