package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircraftsCommand;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChangeCommand;
import akkaLabs.ExtremeAircrafts.position.Position;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class Main
{
	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(new ExtremeModule());
		//		ActorSystem sky = injector.getInstance(ActorSystem.class);
		ActorRef orchestrator = injector.getInstance(Key.get(ActorRef.class, Names.named("orchestrator")));
		orchestrator.tell(new ModifyAircraftsCommand(10), ActorRef.noSender());
		orchestrator.tell(new PositionChangeCommand(new Position(100, -75, 15)), ActorRef.noSender());
		//		sky.actorSelection("/user/*").fo
	}
}
