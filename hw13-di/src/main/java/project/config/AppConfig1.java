package project.config;

import project.appcontainer.api.AppComponent;
import project.appcontainer.api.AppComponentsContainerConfig;
import project.services.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig1 {
    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceStreams(System.out, System.in);
    }
}
