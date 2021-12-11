package nl.hsleiden.AfkoApp.Models;

import nl.hsleiden.AfkoApp.Observables.HomeObservable;
import nl.hsleiden.AfkoApp.Observers.HomeObserver;
import java.util.ArrayList;

public class Home implements HomeObservable {

    ArrayList<HomeObserver> observers = new ArrayList<>();

    ArrayList<Abbreviation> recentAbbreviations;
    ArrayList<Abbreviation> resultAbbreviations;

    /**
     * Constructor for Home.
     * @author InsectByte
     */
    public Home() {
        recentAbbreviations = new ArrayList<>();
        resultAbbreviations = new ArrayList<>();
    }

    /**
     * Returns 10 most recent Abbreviations.
     * @return ArrayList<Abbreviation>
     */
    public ArrayList<Abbreviation> getRecentAbbreviations() {
        return recentAbbreviations;
    }

    /**
     * Sets 10 most recent Abbreviations.
     * @author InsectByte
     * @param recentAbbreviations
     */
    public void setRecentAbbreviations(ArrayList<Abbreviation> recentAbbreviations) {
        this.recentAbbreviations = recentAbbreviations;
        notifyObservers();
    }

    /**
     * Returns the abbreviation results.
     * @author InsectByte
     * @return ArrayList<Abbreviation>
     */
    public ArrayList<Abbreviation> getResultAbbreviations() {
        return resultAbbreviations;
    }

    /**
     * Sets the resulting abbreviations.
     * @author InsectByte
     * @param resultAbbreviations
     */
    public void setResultAbbreviations(ArrayList<Abbreviation> resultAbbreviations) {
        this.resultAbbreviations = resultAbbreviations;
        notifyObservers();
    }

    /**
     * Registers HomeObserver
     * @author InsectByte
     * @param observer
     */
    @Override
    public void registerObserver(HomeObserver observer) {
        observers.add(observer);
    }

    /**
     * Unregisters HomeObserver
     * @author InsectByte
     * @param observer
     */
    @Override
    public void unregisterObserver(HomeObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers.
     * @author InsectByte
     */
    @Override
    public void notifyObservers() {
        for (HomeObserver observer : this.observers) {
            observer.update(this);
        }
    }
}
