package domain.repositories

import domain.entities.{GitHubForeignUserId, GitHubUser}

trait GitHubUserRepository {
  def findByForeignId(gitHubUserId: GitHubForeignUserId): Option[GitHubUser]
  def create(user: GitHubUser): Option[GitHubUser]
  def save(user: GitHubUser): Boolean
}
