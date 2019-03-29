package modules;

import java.time.Clock;
import com.google.inject.AbstractModule;
import actors.GeneratorActor;

public class DefaultModule extends AbstractModule {
	
	@Override
    public void configure() {
    	
        bind(Clock.class).toInstance(Clock.systemDefaultZone());
        bind(GeneratorActor.class).asEagerSingleton();
    }

}
