package project.config;

import project.appcontainer.api.AppComponent;
import project.appcontainer.api.AppComponentsContainerConfig;
import project.appcontainer.api.Qualifier;
import project.services.*;

@AppComponentsContainerConfig(order = 2)
public class AppConfig1 {
    @AppComponent(order = 0, name = "equationPreparerNew")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerServiceNew")
    public PlayerService playerService(@Qualifier(name = "ioServiceNew") IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 0, name = "ioServiceNew")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
