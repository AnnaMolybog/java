package project.config;

import project.appcontainer.api.AppComponent;
import project.appcontainer.api.AppComponentsContainerConfig;
import project.services.*;

@AppComponentsContainerConfig(order = 3)
public class AppConfig2 {
    @AppComponent(order = 2, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
