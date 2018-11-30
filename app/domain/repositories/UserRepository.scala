package domain.repositories

import domain.entities.{User, UserId}

trait UserRepository {
  def find(id: UserId): Option[User]
  def create(user: User): Option[User]
  def save(user: User): Boolean
}
