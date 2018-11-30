package infrastructure.scalikejdbc.mappers

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class UsersMapperSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val u = UsersMapper.syntax("u")

  behavior of "UsersMapper"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = UsersMapper.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = UsersMapper.findBy(sqls.eq(u.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = UsersMapper.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = UsersMapper.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = UsersMapper.findAllBy(sqls.eq(u.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = UsersMapper.countBy(sqls.eq(u.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = UsersMapper.create(name = "MyString", createdAt = null, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = UsersMapper.findAll().head
    // TODO modify something
    val modified = entity
    val updated = UsersMapper.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = UsersMapper.findAll().head
    val deleted = UsersMapper.destroy(entity)
    deleted should be(1)
    val shouldBeNone = UsersMapper.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = UsersMapper.findAll()
    entities.foreach(e => UsersMapper.destroy(e))
    val batchInserted = UsersMapper.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
