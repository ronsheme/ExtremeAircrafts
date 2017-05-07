package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akkaLabs.ExtremeAircrafts.commands.aircraft.ModifyAircrafts;
import akkaLabs.ExtremeAircrafts.commands.aircraft.PositionChange;
import akkaLabs.ExtremeAircrafts.position.Position;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class Main
{
	
	private static final int AUTO_UPDATE = 3000;
	private static final String ORCHESTRATOR = "orchestrator";
	private static final String ALL_AIRCRAFTS = "/user/"+ORCHESTRATOR+"/*";
	
	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(new ExtremeModule());
		ActorSystem sky = injector.getInstance(ActorSystem.class);
		ActorRef orchestrator = injector.getInstance(Key.get(ActorRef.class, Names.named(ORCHESTRATOR)));
		orchestrator.tell(new ModifyAircrafts(10), ActorRef.noSender());
		orchestrator.tell(new PositionChange(new Position(100, -75, 15)), ActorRef.noSender());
		
		sky.scheduler().schedule(Duration.Zero(),
				Duration.create(AUTO_UPDATE, TimeUnit.MILLISECONDS),()->{
					sky.actorSelection(ALL_AIRCRAFTS).tell(new PositionChange(new Position()), ActorRef.noSender());
				},sky.dispatcher());
		
	}
}
