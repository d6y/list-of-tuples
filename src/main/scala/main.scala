import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object Example extends App {

  final case class Message(
    x : Int,
    y : Int
   )

  def freshTestData = Seq(
    Message(1,2),
    Message(1,3),
    Message(2,3)
  )

  final class MessageTable(tag: Tag)
      extends Table[Message](tag, "message") {

    def x  = column[Int]("x")
    def y  = column[Int]("y")

    def * = (x, y) <> (Message.tupled, Message.unapply)
  }

  lazy val messages = TableQuery[MessageTable]

  val db = Database.forConfig("example")

  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  exec(messages.schema.create)
  exec(messages ++= freshTestData)


  def or(values: List[(Int,Int)]): MessageTable => Rep[Boolean] =
    m => values match {
      case (a,b) :: rest => (m.x === a && m.y === b) || or(rest)(m)
      case Nil => false
    }

  val data = List( (9,8), (1,2) )
  val q = messages.filter( or(data) )

  println(q.result.statements)

}
