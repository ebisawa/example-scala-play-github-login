package infrastructure.scalikejdbc.mappers

import org.scalatest._
import scalikejdbc.scalatest.AutoRollback
import scalikejdbc._
import java.time.{ZonedDateTime}


class GitHubUsersMapperSpec extends fixture.FlatSpec with Matchers with AutoRollback {
  val gu = GitHubUsersMapper.syntax("gu")

  behavior of "GitHubUsersMapper"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = GitHubUsersMapper.find(123)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = GitHubUsersMapper.findBy(sqls.eq(gu.id, 123))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = GitHubUsersMapper.findAll()
    allResults.size should be >(0)
  }
  it should "count all records" in { implicit session =>
    val count = GitHubUsersMapper.countAll()
    count should be >(0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = GitHubUsersMapper.findAllBy(sqls.eq(gu.id, 123))
    results.size should be >(0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = GitHubUsersMapper.countBy(sqls.eq(gu.id, 123))
    count should be >(0L)
  }
  it should "create new record" in { implicit session =>
    val created = GitHubUsersMapper.create(userId = 123, ghUserId = 1L, ghNodeId = "MyString", ghLogin = "MyString", ghName = "MyString", ghEmail = "MyString", ghCreatedAt = null, ghUpdatedAt = null, createdAt = null, updatedAt = null)
    created should not be(null)
  }
  it should "save a record" in { implicit session =>
    val entity = GitHubUsersMapper.findAll().head
    // TODO modify something
    val modified = entity
    val updated = GitHubUsersMapper.save(modified)
    updated should not equal(entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = GitHubUsersMapper.findAll().head
    val deleted = GitHubUsersMapper.destroy(entity)
    deleted should be(1)
    val shouldBeNone = GitHubUsersMapper.find(123)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = GitHubUsersMapper.findAll()
    entities.foreach(e => GitHubUsersMapper.destroy(e))
    val batchInserted = GitHubUsersMapper.batchInsert(entities)
    batchInserted.size should be >(0)
  }
}
