package playground

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import playground.ChangingActorBehavior.Children.{KidAccepted, KidRejected}

object ChangingActorBehavior extends App{


  object Children {
    val HAPPY = "happy"
    val SAD = "sad"
    object KidAccepted
    object KidRejected
  }


  //gambiarra
  class Children extends Actor {
    import Children._
    import Mom._

    //internal state of kid
    var state = HAPPY

    def receive:Receive = {
      case Food(VEGETABLES) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_) => if (state == HAPPY) {
        sender() ! KidAccepted
      } else {
        sender() ! KidRejected
      }
    }
  }

  //stateless children refactored
  class StatelessChildren extends Actor {
    import Mom._

    def receive: Receive = happyReceive //happy because is default behavior
    def happyReceive: Receive = {
      case Food(VEGETABLES) => context.become(sadReceive)
      case Food(CHOCOLATE) => //do nothing because state is happy
      case Ask(_) => sender() ! KidAccepted
    }
    def sadReceive: Receive = {
      case Food(VEGETABLES) => //do nothing because state is sad
      case Food(CHOCOLATE) => context.become(happyReceive) //change the behavior to happy because he receive a chocolate
      case Ask(_) => sender() ! KidRejected //when then receive a ask and the last food of he receive is vegetable he rejected the ask
    }
  }

  object Mom {
    val VEGETABLES = "veggies"
    val CHOCOLATE = "chocolate"
    case class Ask(message:String)
    case class Food(food:String)
    case class MomStart(kidRef: ActorRef)
  }

  class Mom extends Actor {
    import Mom._
    import Children._

    def receive: Receive = {
      case MomStart(kidRef) =>
        kidRef ! Food(VEGETABLES)
        kidRef ! Ask("Do you wanna play?")
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("Do you wanna play?")

      case KidAccepted => println("Yay, my kid is happy!")
      case KidRejected => println("My kid is sad but as he healthy!")
    }
  }

  val system = ActorSystem("ChagingActorBehaviorDemo")

  val mom = system.actorOf(Props[Mom], "momActor")
  val statelessKid = system.actorOf(Props[StatelessChildren], "statelessChilderActor")
  //val children = system.actorOf(Props[Children], "childrenActor") //not use this

  import Mom._
  mom ! MomStart(statelessKid)

  /*
  Mom receives MomStart
  Children receives vegetable -> children will change the handler to sadReceive
  Children do you play? -> children reject

  Children receives chocolate -> children not change because the default handler is happyReceive
  Children do you play? -> children accept
  */

}
