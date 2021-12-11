package nl.hsleiden.AfkoApp.Observables;

import nl.hsleiden.AfkoApp.Observers.SubmitObserver;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;

public interface SubmitObservable {
    void registerObserver(SubmitObserver observer);

    void unregisterObserver(SubmitObserver observer);

    void notifyObservers() throws FileNotFoundException, URISyntaxException;
}
