package playground

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import playground.ActorBank.Person.LiveTheLife

object ActorBank extends App {

  val system = ActorSystem("Bank")

  object BankAccount {
    case class Deposit(amount: Int)

    case class WithDraw(amount: Int)

    case class TransactionSuccess(message: String)

    case class TransactionFailure(message: String)

    object Statement
  }

  class BankAccount extends Actor {

    import BankAccount._

    var found = 0

    def receive = {
      case Deposit(amount) => if (amount < 0){
        sender() ! TransactionFailure("Invalid amount value.")
      }
      else {
        found += amount
        sender() ! TransactionSuccess(s"Deposited $amount.")
      }

      case WithDraw(amount) => if (amount < 0)
        sender() ! TransactionFailure("Invalid amount value.")
      else if (amount > found) {
        sender() ! TransactionFailure("Amount is greather than founds.")
      } else if (amount < found) {
        found -= amount
        sender() ! TransactionSuccess(s"Drawee $amount.")
      }

      case Statement => println(s"[bank] The value of your founds is: $found")
    }
  }

  object Person {
    case class LiveTheLife(account: ActorRef)
  }

  class Person extends Actor {

    import BankAccount._
    import Person._

    def receive: Receive = {
      case LiveTheLife(account) =>
        account ! Deposit(1000)
        account ! WithDraw(2000)
        account ! WithDraw(500)
        account ! Statement

      case message => println(message.toString)
    }
  }

  val account = system.actorOf(Props[BankAccount], "bankActor")
  val person = system.actorOf(Props[Person], "personActor")

  person ! LiveTheLife(account)
}
