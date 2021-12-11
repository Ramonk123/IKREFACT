package nl.hsleiden.AfkoApp.Models;

import nl.hsleiden.AfkoApp.Observables.SubmitObservable;
import nl.hsleiden.AfkoApp.Observers.SubmitObserver;
import java.util.ArrayList;

public class Submit implements SubmitObservable {


    ArrayList<Abbreviation> abbreviationResults;

    /**
     * Constructor for Submit
     * @author InsectByte
     */
    public Submit() {
        abbreviationResults = new ArrayList<>();
    }
    // -------------------------------------------
    // Getters & Setters
    // -------------------------------------------

    /**
     * Returns resulting Abbreviations
     * @author InsectByte
     * @return ArrayList<Abbreviation>
     */
    public ArrayList<Abbreviation> getAbbreviationResults() {
        return abbreviationResults;
    }

    /**
     * Sets resulting abbreviations
     * @author InsectByte
     * @param abbreviationResults
     */
    public void setAbbreviationResults(ArrayList<Abbreviation> abbreviationResults) {
        this.abbreviationResults = abbreviationResults;
        try{
            notifyObservers();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    // -------------------------------------------
    // Observer Pattern
    // -------------------------------------------

    ArrayList<SubmitObserver> observers = new ArrayList<>();

    /**
     * Registers SubmitObserver
     * @author InsectByte
     * @param observer
     */
    @Override
    public void registerObserver(SubmitObserver observer) {
        this.observers.add(observer);
    }

    /**
     * Unregisters SubmitObserver
     * @author InsectByte
     * @param observer
     */
    @Override
    public void unregisterObserver(SubmitObserver observer) {
        this.observers.remove(observer);
    }

    /**
     * Notifies all Submit Observers
     */
    @Override
    public void notifyObservers() {
        for (SubmitObserver observer : this.observers) {
            observer.update(this);
        }
    }
}
