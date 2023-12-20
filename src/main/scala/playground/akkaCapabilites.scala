package playground

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object akkaCapabilites extends App{

  val system = ActorSystem("ActorCapabilitiesDemo")
  //actors can receive any type of message
  class SimpleActor extends Actor {
    override def receive = {
      case "Hi" => sender() ! "Hello, there!"
      case message:String => println(s"[$self] I have received a STRING: $message")
      case number: Integer => println(s"[simple actor] I have received a NUMBER: $number")
      case SpecialType(content) => println(s"[simple actor] I have received a SPECIAL TYPE: $content")

      //context.self == this in OOP
      case SendMessageToYourSelf(content) => self ! content

      case SayHiTo(ref) => ref ! "Hi" //pedro is being passed as the sender

      case WirelessPhoneMessage(content, ref) => ref forward content //forward methods keep the original sender
    }
  }

  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  simpleActor ! 42

  //different types
  case class SpecialType(content:String)
  simpleActor ! SpecialType("Some special content")

  //sending message to yourself
  case class SendMessageToYourSelf(content: String)
  simpleActor ! SendMessageToYourSelf("I am an actor and I am proud of it")

  case class SayHiTo(ref: ActorRef)

  val pedro = system.actorOf(Props[SimpleActor], "pedro")
  val taliyah = system.actorOf(Props[SimpleActor], "taliyah")

  pedro ! SayHiTo(taliyah) //reply to "me"

  //dead letters
  pedro ! "Hi" //how is the sender?

  case class WirelessPhoneMessage(content:String, ref: ActorRef)
  pedro ! WirelessPhoneMessage("Hi", taliyah)
}
