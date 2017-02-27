package models

case class Employee(name: String, email: String, companyName: String,position:String, id: Option[Int]=None)
case class Work(name: String, salary : Double, company : Option[String] = None, id : Option[Int] = None)

