import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object AndExample extends App {

  final case class Message(x: String)

  def freshTestData = Seq(
    Message("ABC"),
    Message("BCD"),
    Message("CDE")
  )

  final class MessageTable(tag: Tag) extends Table[Message](tag, "message") {
    def x  = column[String]("x")
    def * = x.mapTo[Message]
  }

  lazy val messages = TableQuery[MessageTable]

  val db = Database.forConfig("example")

  def exec[T](program: DBIO[T]): T = Await.result(db.run(program), 2 seconds)

  exec(messages.schema.create)
  exec(messages ++= freshTestData)

  val patterns = List("%BC%", "%DE%")
  val q = messages.filter( m =>
      patterns.map(p => m.x like p).reduceLeft(_ && _)
  )

  println(q.result.statements)
}
