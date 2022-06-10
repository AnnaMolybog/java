package project.config;

import project.appcontainer.api.AppComponent;
import project.appcontainer.api.AppComponentsContainerConfig;
import project.appcontainer.api.Qualifier;
import project.services.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig {

    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(@Qualifier(name = "ioService") IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(@Qualifier(name = "ioService") IOService ioService,
                                       @Qualifier(name = "playerService") PlayerService playerService,
                                       @Qualifier(name = "equationPreparer") EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
