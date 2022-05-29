package project.appcontainer.helpers;

import project.appcontainer.wrapper.AppComponentWrapper;

import java.util.Comparator;

public class AppComponentOrderComparator implements Comparator<AppComponentWrapper> {
    @Override
    public int compare(AppComponentWrapper appComponentWrapper1, AppComponentWrapper appComponentWrapper2) {
        return Integer.compare(
            appComponentWrapper1.getOrder(),
            appComponentWrapper2.getOrder()
        );
    }
}
