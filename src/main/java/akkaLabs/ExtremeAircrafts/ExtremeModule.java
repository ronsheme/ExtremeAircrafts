package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

public class ExtremeModule extends AbstractModule
{
	private SpatialContextFactory spatialContextFactory;
	private SpatialContext spatialContext;
	private ActorSystem sky;

	public ExtremeModule()
	{
		this.spatialContextFactory = new SpatialContextFactory();
		this.spatialContext = this.spatialContextFactory.newSpatialContext();
		this.sky = ActorSystem.create("Sky");
	}

	@Override
	protected void configure()
	{
		bind(ActorSystem.class).toInstance(this.sky);
		bind(SpatialContextFactory.class).toInstance(this.spatialContextFactory);
		bind(SpatialContext.class).toInstance(this.spatialContext);
		bind(ActorRef.class).annotatedWith(Names.named("orchestrator"))
				.toProvider(() -> this.sky.actorOf(Props.create(Orchestrator.class, () -> new Orchestrator(this.spatialContext)), "orchestrator"));
	}

}
