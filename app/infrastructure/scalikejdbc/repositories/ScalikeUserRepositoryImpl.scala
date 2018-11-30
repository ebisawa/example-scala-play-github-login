package infrastructure.scalikejdbc.repositories

import java.time.ZonedDateTime

import domain.entities._
import domain.repositories.UserRepository
import infrastructure.scalikejdbc.mappers._
import scalikejdbc._

import scala.util.Try

object ScalikeUserRepositoryImpl extends UserRepository with ScalikeJdbcRepository {
  import ScalikeUserRepositoryHelper._

  private val u = UsersMapper.syntax("u")

  def find(id: UserId): Option[User] = {
    UsersMapper.findBy(sqls.eq(u.id, id.toInt)).map(_.toEntity)
  }

  def create(user: User): Option[User] = {
    Try {
      val now = ZonedDateTime.now()

      UsersMapper.create(
        user.name,
        now,
        now
      )
    }.toOption.map(_.toEntity)
  }

  def save(user: User): Boolean = {
    Try {
      val now = ZonedDateTime.now()
      val column = UsersMapper.column

      withSQL {
        update(UsersMapper).set(
          column.name -> user.name,
          column.updatedAt -> now,
        ).where.eq(column.id, user.id.get.toInt)
      }.update.apply()
    }.isSuccess
  }
}

object ScalikeUserRepositoryHelper {
  case class UserIdImpl(int: Int) extends UserId

  implicit class RichUserId(self: UserId) {
    def toInt: Int = {
      self match {
        case UserIdImpl(value) => value
        case _ => throw new Exception("id is not a UserIdImpl")
      }
    }
  }

  implicit class RichUsersModel(self: UsersMapper) {
    def toEntity: User = {
      User(
        Some(UserIdImpl(self.id)),
        self.name,
      )
    }
  }
}
