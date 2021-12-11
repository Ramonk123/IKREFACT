package nl.hsleiden.AfkoApp.Observables;

import nl.hsleiden.AfkoApp.Observers.HomeObserver;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public interface HomeObservable {
    void registerObserver(HomeObserver observer);

    void unregisterObserver(HomeObserver observer);

    void notifyObservers() throws FileNotFoundException, URISyntaxException;
}
