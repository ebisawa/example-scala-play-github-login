package infrastructure.scalikejdbc.mappers

import scalikejdbc._
import java.time.{ZonedDateTime}

case class GitHubUsersMapper(
  id: Int,
  userId: Int,
  ghUserId: Long,
  ghNodeId: String,
  ghLogin: String,
  ghName: String,
  ghEmail: String,
  ghCreatedAt: ZonedDateTime,
  ghUpdatedAt: ZonedDateTime,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = GitHubUsersMapper.autoSession): GitHubUsersMapper = GitHubUsersMapper.save(this)(session)

  def destroy()(implicit session: DBSession = GitHubUsersMapper.autoSession): Int = GitHubUsersMapper.destroy(this)(session)

}


object GitHubUsersMapper extends SQLSyntaxSupport[GitHubUsersMapper] {

  override val tableName = "github_users"

  override val columns = Seq("id", "user_id", "gh_user_id", "gh_node_id", "gh_login", "gh_name", "gh_email", "gh_created_at", "gh_updated_at", "created_at", "updated_at")

  def apply(gu: SyntaxProvider[GitHubUsersMapper])(rs: WrappedResultSet): GitHubUsersMapper = apply(gu.resultName)(rs)
  def apply(gu: ResultName[GitHubUsersMapper])(rs: WrappedResultSet): GitHubUsersMapper = new GitHubUsersMapper(
    id = rs.get(gu.id),
    userId = rs.get(gu.userId),
    ghUserId = rs.get(gu.ghUserId),
    ghNodeId = rs.get(gu.ghNodeId),
    ghLogin = rs.get(gu.ghLogin),
    ghName = rs.get(gu.ghName),
    ghEmail = rs.get(gu.ghEmail),
    ghCreatedAt = rs.get(gu.ghCreatedAt),
    ghUpdatedAt = rs.get(gu.ghUpdatedAt),
    createdAt = rs.get(gu.createdAt),
    updatedAt = rs.get(gu.updatedAt)
  )

  val gu = GitHubUsersMapper.syntax("gu")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[GitHubUsersMapper] = {
    withSQL {
      select.from(GitHubUsersMapper as gu).where.eq(gu.id, id)
    }.map(GitHubUsersMapper(gu.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[GitHubUsersMapper] = {
    withSQL(select.from(GitHubUsersMapper as gu)).map(GitHubUsersMapper(gu.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(GitHubUsersMapper as gu)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[GitHubUsersMapper] = {
    withSQL {
      select.from(GitHubUsersMapper as gu).where.append(where)
    }.map(GitHubUsersMapper(gu.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[GitHubUsersMapper] = {
    withSQL {
      select.from(GitHubUsersMapper as gu).where.append(where)
    }.map(GitHubUsersMapper(gu.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(GitHubUsersMapper as gu).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    userId: Int,
    ghUserId: Long,
    ghNodeId: String,
    ghLogin: String,
    ghName: String,
    ghEmail: String,
    ghCreatedAt: ZonedDateTime,
    ghUpdatedAt: ZonedDateTime,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): GitHubUsersMapper = {
    val generatedKey = withSQL {
      insert.into(GitHubUsersMapper).namedValues(
        column.userId -> userId,
        column.ghUserId -> ghUserId,
        column.ghNodeId -> ghNodeId,
        column.ghLogin -> ghLogin,
        column.ghName -> ghName,
        column.ghEmail -> ghEmail,
        column.ghCreatedAt -> ghCreatedAt,
        column.ghUpdatedAt -> ghUpdatedAt,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    GitHubUsersMapper(
      id = generatedKey.toInt,
      userId = userId,
      ghUserId = ghUserId,
      ghNodeId = ghNodeId,
      ghLogin = ghLogin,
      ghName = ghName,
      ghEmail = ghEmail,
      ghCreatedAt = ghCreatedAt,
      ghUpdatedAt = ghUpdatedAt,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[GitHubUsersMapper])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'userId -> entity.userId,
        'ghUserId -> entity.ghUserId,
        'ghNodeId -> entity.ghNodeId,
        'ghLogin -> entity.ghLogin,
        'ghName -> entity.ghName,
        'ghEmail -> entity.ghEmail,
        'ghCreatedAt -> entity.ghCreatedAt,
        'ghUpdatedAt -> entity.ghUpdatedAt,
        'createdAt -> entity.createdAt,
        'updatedAt -> entity.updatedAt))
    SQL("""insert into github_users(
      user_id,
      gh_user_id,
      gh_node_id,
      gh_login,
      gh_name,
      gh_email,
      gh_created_at,
      gh_updated_at,
      created_at,
      updated_at
    ) values (
      {userId},
      {ghUserId},
      {ghNodeId},
      {ghLogin},
      {ghName},
      {ghEmail},
      {ghCreatedAt},
      {ghUpdatedAt},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: GitHubUsersMapper)(implicit session: DBSession = autoSession): GitHubUsersMapper = {
    withSQL {
      update(GitHubUsersMapper).set(
        column.id -> entity.id,
        column.userId -> entity.userId,
        column.ghUserId -> entity.ghUserId,
        column.ghNodeId -> entity.ghNodeId,
        column.ghLogin -> entity.ghLogin,
        column.ghName -> entity.ghName,
        column.ghEmail -> entity.ghEmail,
        column.ghCreatedAt -> entity.ghCreatedAt,
        column.ghUpdatedAt -> entity.ghUpdatedAt,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: GitHubUsersMapper)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(GitHubUsersMapper).where.eq(column.id, entity.id) }.update.apply()
  }

}
