package project;

import project.appcontainer.AppComponentsContainerImpl;
import project.appcontainer.api.AppComponentsContainer;
import project.config.AppConfig1;
import project.config.AppConfig2;
import project.services.GameProcessor;
import project.services.GameProcessorImpl;

public class App {

    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
        //AppComponentsContainer container = new AppComponentsContainerImpl("project.config");
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessorNew");
        
        // Обязательный вариант
       // AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
