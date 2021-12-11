package nl.hsleiden.AfkoApp.Observables;

import nl.hsleiden.AfkoApp.Observers.AdminObserver;
import nl.hsleiden.AfkoApp.Observers.SubmitObserver;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/**
 * Observable for the admin.
 * @author Daniel Paans
 */
public interface AdminObservable {
    void registerObserver(AdminObserver observer);

    void unregisterObserver(AdminObserver observer);

    void notifyObservers() throws FileNotFoundException, URISyntaxException;
}
