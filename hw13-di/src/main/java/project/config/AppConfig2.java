package project.config;

import project.appcontainer.api.AppComponent;
import project.appcontainer.api.Qualifier;
import project.services.*;
import project.appcontainer.api.AppComponentsContainerConfig;

@AppComponentsContainerConfig(order = 3)
public class AppConfig2 {
    @AppComponent(order = 2, name = "gameProcessorNew")
    public GameProcessor gameProcessor(@Qualifier(name = "ioServiceNew") IOService ioService,
                                       @Qualifier(name = "playerServiceNew") PlayerService playerService,
                                       @Qualifier(name = "equationPreparerNew") EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
