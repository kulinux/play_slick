package repo

import javax.inject.Singleton

import com.google.inject.Inject
import models.Work
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

/**
  * Created by fbenitez on 26/02/2017.
  */
@Singleton()
class WorkRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends WorkTable with HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  def insert(w: Work) : Future[Int] = db.run{
    workTableQueryInc += w
  }


  def insertAll(works: List[Work]): Future[Seq[Int]] = db.run {
    workTableQueryInc ++= works
  }


  def getAll() : Future[List[Work]] = db.run {
    workTableQuery.to[List].result
  }

}

private[repo] trait WorkTable {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import driver.api._

  lazy protected val workTableQuery = TableQuery[WorkTable]
  lazy protected val workTableQueryInc = workTableQuery returning workTableQuery.map(_.id)

  private[WorkTable] class WorkTable(tag : Tag) extends Table[Work](tag, "work") {
    val id: Rep[Int] = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val name: Rep[String] = column[String]("name", O.SqlType("VARCHAR(200)"))
    val salary: Rep[Int] = column[Int]("salary", O.SqlType("NUMERIC"))
    val company: Rep[String] = column[String]("company", O.SqlType("VARCHAR(200)"))

    def * = (name, salary, company, id.?) <> (Work.tupled, Work.unapply)

  }



}
