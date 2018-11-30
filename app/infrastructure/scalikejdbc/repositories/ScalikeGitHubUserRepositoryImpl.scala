package infrastructure.scalikejdbc.repositories

import java.time.ZonedDateTime

import domain.entities.{GitHubForeignUserId, GitHubUser, GitHubUserId}
import domain.repositories.GitHubUserRepository
import infrastructure.scalikejdbc.mappers._
import scalikejdbc._

import scala.util.Try

object ScalikeGitHubUserRepositoryImpl extends GitHubUserRepository with ScalikeJdbcRepository {
  import ScalikeGitHubUserRepositoryHelper._
  import ScalikeUserRepositoryHelper._

  private val gu = GitHubUsersMapper.syntax("gu")

  def findByForeignId(ghUserId: GitHubForeignUserId): Option[GitHubUser] = {
    GitHubUsersMapper.findBy(sqls.eq(gu.ghUserId, ghUserId.value)).map(_.toEntity)
  }

  def create(ghUser: GitHubUser): Option[GitHubUser] = {
    Try {
      val now = ZonedDateTime.now()

      GitHubUsersMapper.create(
        ghUser.userId.toInt,
        ghUser.ghUserId.value,
        ghUser.ghNodeId,
        ghUser.ghLogin,
        ghUser.ghName,
        ghUser.ghEmail,
        ghUser.ghCreatedAt,
        ghUser.ghUpdatedAt,
        now,
        now
      )
    }.toOption.map(_.toEntity)
  }

  def save(ghUser: GitHubUser): Boolean = {
    Try {
      val now = ZonedDateTime.now()
      val column = GitHubUsersMapper.column

      withSQL {
        update(GitHubUsersMapper).set(
          column.ghUserId -> ghUser.ghUserId.value,
          column.ghNodeId -> ghUser.ghNodeId,
          column.ghLogin -> ghUser.ghLogin,
          column.ghName -> ghUser.ghName,
          column.ghEmail -> ghUser.ghEmail,
          column.ghCreatedAt -> ghUser.ghCreatedAt,
          column.ghUpdatedAt -> ghUser.ghUpdatedAt,
          column.updatedAt -> now,
        ).where.eq(column.id, ghUser.id.get.toInt)
      }.update.apply()
    }.isSuccess
  }
}

object ScalikeGitHubUserRepositoryHelper {
  import ScalikeUserRepositoryHelper._

  case class GitHubUserIdImpl(int: Int) extends GitHubUserId

  implicit class RichGitHubUserId(self: GitHubUserId) {
    def toInt: Int = {
      self match {
        case GitHubUserIdImpl(value) => value
        case _ => throw new Exception("id is not a GitHubUserIdImpl")
      }
    }
  }

  implicit class RichGitHubUsersModel(self: GitHubUsersMapper) {
    def toEntity: GitHubUser = {
      GitHubUser(
        Some(GitHubUserIdImpl(self.id)),
        UserIdImpl(self.userId),
        GitHubForeignUserId(self.ghUserId),
        self.ghNodeId,
        self.ghLogin,
        self.ghName,
        self.ghEmail,
        self.ghCreatedAt,
        self.ghUpdatedAt,
      )
    }
  }
}
