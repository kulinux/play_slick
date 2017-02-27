package models

case class Employee(name: String, email: String, companyName: String,position:String, id: Option[Int]=None)
case class Work(name: String, salary : Int, company : String, id : Option[Int] = None)

