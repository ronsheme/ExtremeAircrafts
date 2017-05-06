package akkaLabs.ExtremeAircrafts;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import akka.actor.ActorRef;

public class Main
{
	public static void main(String[] args)
	{
		Injector injector = Guice.createInjector(new ExtremeModule());
		ActorRef orchestrator = injector.getInstance(Key.get(ActorRef.class,Names.named("orchestrator")));
		
		orchestrator.tell(new Orchestrator.ModifyAircraftsMsg(10), ActorRef.noSender());
	}
}
