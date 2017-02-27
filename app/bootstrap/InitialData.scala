package bootstrap

import com.google.inject.Inject
import models.{Employee, Work}
import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import repo.{EmployeeRepository, WorkRepository}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class InitialData @Inject() (employeeRepo: EmployeeRepository, workRepo: WorkRepository) {

  def insert = for {
    emps <- employeeRepo.getAll() if (emps.length == 0)
    _ <- employeeRepo.insertAll(Data.employees)
  } yield {}

  def insertWork = for {
    work <- workRepo.getAll() if (work.length == 0)
    _ <- workRepo.insertAll(Data.works)
  } yield {}

  try {
    Logger.info("DB initialization.................")
    Await.result(insert, Duration.Inf)
    Await.result(insertWork, Duration.Inf)
  } catch {
    case ex: Exception =>
      Logger.error("Error in database initialization ", ex)
  }

}

object Data {
  val employees = List(
    Employee("Satendra", "satendra@knoldus.com", "Knoldus","Senior Consultant"),
    Employee("Mayank", "mayank@knoldus.com",  "knoldus","Senior Consultant"),
    Employee("Sushil", "sushil@knoldus.com",  "knoldus","Consultant"),
    Employee("Narayan", "narayan@knoldus.com",  "knoldus","Consultant"),
    Employee("Himanshu", "himanshu@knoldus.com",  "knoldus","Senior Consultant"))

  val works = List(
    Work("work1", 10000, "company1"),
    Work("work2", 10000, "company2"),
    Work("work3", 10000, "company3")

  )
}
