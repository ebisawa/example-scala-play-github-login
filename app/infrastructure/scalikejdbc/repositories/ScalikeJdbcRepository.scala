package infrastructure.scalikejdbc.repositories

import scalikejdbc.{AutoSession, DBSession}

trait ScalikeJdbcRepository {
  protected implicit val session: DBSession = AutoSession
}
