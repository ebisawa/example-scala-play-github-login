package domain.entities

import java.time.ZonedDateTime

trait GitHubUserId extends EntityId

case class GitHubForeignUserId(value: Long)

case class GitHubUser(
  id: Option[GitHubUserId],
  userId: UserId,
  ghUserId: GitHubForeignUserId,
  ghNodeId: String,
  ghLogin: String,
  ghName: String,
  ghEmail: String,
  ghCreatedAt: ZonedDateTime,
  ghUpdatedAt: ZonedDateTime,
)
