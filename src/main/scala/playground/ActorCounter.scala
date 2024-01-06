package playground

import akka.actor.Actor
import akka.actor.{Actor, ActorRef, ActorSystem, Props}


object ActorCounter extends App {

  val system = ActorSystem("CounterActor")

  //Actor Domain
  object Counter {
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter extends Actor {
    import Counter._

    var count = 0

    def receive = {
      case Increment => count += 1
      case Decrement => count -= 1
      case Print => println(s"[counter] My current count is: $count")
    }
  }

  val countActor = system.actorOf(Props[Counter],"myActorCounter")

  import Counter._

  (1 to 6).foreach(_ => countActor ! Increment)
  (1 to 3).foreach(_ => countActor ! Decrement)
  countActor ! Print


}
