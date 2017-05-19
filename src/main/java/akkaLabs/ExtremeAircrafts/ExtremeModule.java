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
	public static final String ORCHESTRATOR = "orchestrator";

	public static final double TOP_LEFT_LONGITUDE = 31.7995855;
	public static final double TOP_LEFT_LATITUDE = 34.673663;
	public static final double BOTTOM_RIGHT_LONGITUDE = 31.6940723;
	public static final double BOTTOM_RIGHT_LATITUDE = 34.8884191;

	public static final int UPDATE_RATE = 1;

	protected SpatialContext spatialContext;
	protected ActorSystem sky;
	private SpatialContextFactory spatialContextFactory;

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
		bind(ActorRef.class).annotatedWith(Names.named(ORCHESTRATOR)).toProvider(() -> this.sky.actorOf(Props.create(Orchestrator.class, () -> new Orchestrator(this.spatialContext)), ORCHESTRATOR));
	}

}
