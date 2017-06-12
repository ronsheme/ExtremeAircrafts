package akkaLabs.ExtremeAircrafts;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.event.japi.LookupEventBus;
import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEvelope;

import akkaLabs.ExtremeAircrafts.eventbus.PositionChangedEventBus;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;

public class ExtremeModule extends AbstractModule
{
	public static final String ORCHESTRATOR = "orchestrator";
	public static final String AIRCRAFT_SERVER_ADDRESS = "http://localhost:8080/api/aircrafts/update";
	public static final String EVENTBUS_NAME = "positionChangedEventBus";

	public static final double MAX_LONGITUDE = 180.0;
	public static final double MAX_LATITUDE = 90.0;
	public static final double MIN_LONGITUDE = -180.0;
	public static final double MIN_LATITUDE = -90.0;

	public static final double MIN_SPEED = 5000;//meter/second
	public static final double MAX_SPEED = 6000;//meter/second

	public static final int UPDATE_RATE = 1;

	private SpatialContext spatialContext;
	private ActorSystem sky;
	private SpatialContextFactory spatialContextFactory;
	private LookupEventBus<PositionChangedEvelope, ActorRef, String> eventBus;

	public ExtremeModule()
	{
		this.spatialContextFactory = new SpatialContextFactory();
		this.spatialContext = this.spatialContextFactory.newSpatialContext();
		this.sky = ActorSystem.create("Sky");
		this.eventBus = new PositionChangedEventBus();
	}

	@Override
	protected void configure()
	{
		bind(ActorSystem.class).toInstance(this.sky);
		bind(SpatialContextFactory.class).toInstance(this.spatialContextFactory);
		bind(SpatialContext.class).toInstance(this.spatialContext);
		bind(ActorRef.class).annotatedWith(Names.named(ORCHESTRATOR)).toProvider(() -> this.sky.actorOf(Props.create(Orchestrator.class, () -> new Orchestrator(this.spatialContext,this.eventBus)), ORCHESTRATOR));
		bind(LookupEventBus.class).annotatedWith(Names.named("positionChangedEventBus")).toInstance(eventBus);
	}

}
