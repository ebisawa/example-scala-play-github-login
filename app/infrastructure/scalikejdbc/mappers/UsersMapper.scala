package infrastructure.scalikejdbc.mappers

import scalikejdbc._
import java.time.{ZonedDateTime}

case class UsersMapper(
  id: Int,
  name: String,
  createdAt: ZonedDateTime,
  updatedAt: ZonedDateTime) {

  def save()(implicit session: DBSession = UsersMapper.autoSession): UsersMapper = UsersMapper.save(this)(session)

  def destroy()(implicit session: DBSession = UsersMapper.autoSession): Int = UsersMapper.destroy(this)(session)

}


object UsersMapper extends SQLSyntaxSupport[UsersMapper] {

  override val tableName = "users"

  override val columns = Seq("id", "name", "created_at", "updated_at")

  def apply(u: SyntaxProvider[UsersMapper])(rs: WrappedResultSet): UsersMapper = apply(u.resultName)(rs)
  def apply(u: ResultName[UsersMapper])(rs: WrappedResultSet): UsersMapper = new UsersMapper(
    id = rs.get(u.id),
    name = rs.get(u.name),
    createdAt = rs.get(u.createdAt),
    updatedAt = rs.get(u.updatedAt)
  )

  val u = UsersMapper.syntax("u")

  override val autoSession = AutoSession

  def find(id: Int)(implicit session: DBSession = autoSession): Option[UsersMapper] = {
    withSQL {
      select.from(UsersMapper as u).where.eq(u.id, id)
    }.map(UsersMapper(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[UsersMapper] = {
    withSQL(select.from(UsersMapper as u)).map(UsersMapper(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(UsersMapper as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[UsersMapper] = {
    withSQL {
      select.from(UsersMapper as u).where.append(where)
    }.map(UsersMapper(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[UsersMapper] = {
    withSQL {
      select.from(UsersMapper as u).where.append(where)
    }.map(UsersMapper(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(UsersMapper as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    name: String,
    createdAt: ZonedDateTime,
    updatedAt: ZonedDateTime)(implicit session: DBSession = autoSession): UsersMapper = {
    val generatedKey = withSQL {
      insert.into(UsersMapper).namedValues(
        column.name -> name,
        column.createdAt -> createdAt,
        column.updatedAt -> updatedAt
      )
    }.updateAndReturnGeneratedKey.apply()

    UsersMapper(
      id = generatedKey.toInt,
      name = name,
      createdAt = createdAt,
      updatedAt = updatedAt)
  }

  def batchInsert(entities: collection.Seq[UsersMapper])(implicit session: DBSession = autoSession): List[Int] = {
    val params: collection.Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'name -> entity.name,
        'createdAt -> entity.createdAt,
        'updatedAt -> entity.updatedAt))
    SQL("""insert into users(
      name,
      created_at,
      updated_at
    ) values (
      {name},
      {createdAt},
      {updatedAt}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: UsersMapper)(implicit session: DBSession = autoSession): UsersMapper = {
    withSQL {
      update(UsersMapper).set(
        column.id -> entity.id,
        column.name -> entity.name,
        column.createdAt -> entity.createdAt,
        column.updatedAt -> entity.updatedAt
      ).where.eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: UsersMapper)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(UsersMapper).where.eq(column.id, entity.id) }.update.apply()
  }

}
