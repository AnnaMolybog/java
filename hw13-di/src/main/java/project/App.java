package project;

import project.appcontainer.AppComponentsContainerImpl;
import project.appcontainer.api.AppComponentsContainer;
import project.config.AppConfig;
import project.services.GameProcessor;

public class App {

    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        // AppComponentsContainer container = new AppComponentsContainerImplNew(AppConfig1.class, AppConfig2.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
        //AppComponentsContainer container = new AppComponentsContainerImplNew("project.config");
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessorNew");
        
        // Обязательный вариант
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        // GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
