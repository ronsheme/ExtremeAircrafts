package server;

import akkaLabs.ExtremeAircrafts.http.PositionChangedHttpEntity;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import serialize.PositionChangedHttpEntityDeserializer;

/**
 * Created by ron-lenovo on 5/28/2017.
 */
public class AircraftsApplication extends Application<AircraftsConfiguration> {
    @Override
    public void run(AircraftsConfiguration aircraftsConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new AircraftsResource());
    }

    @Override
    public void initialize(Bootstrap<AircraftsConfiguration> bootstrap) {
        SimpleModule httpEntityModule = new SimpleModule();
        httpEntityModule.addDeserializer(PositionChangedHttpEntity.class,new PositionChangedHttpEntityDeserializer());
        bootstrap.getObjectMapper().registerModule(httpEntityModule);
        bootstrap.addBundle(new AssetsBundle("/assets/", "/","index.html"));
        bootstrap.addBundle(new AssetsBundle("/assets/js/","/js",null,"js"));
        bootstrap.addBundle(new AssetsBundle("/assets/css/","/css",null,"css"));

    }
}
