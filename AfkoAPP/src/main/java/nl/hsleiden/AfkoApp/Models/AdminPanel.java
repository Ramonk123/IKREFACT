package nl.hsleiden.AfkoApp.Models;

import nl.hsleiden.AfkoApp.Observables.AdminObservable;
import nl.hsleiden.AfkoApp.Observers.AdminObserver;
import nl.hsleiden.AfkoApp.Observers.HomeObserver;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Holds the reports for in the adminPanel.
 * @author Daniel Paans
 */
public class AdminPanel implements AdminObservable {

    private ArrayList<AdminObserver> observers = new ArrayList<>();
    private ArrayList<GameScoreReport> gameScoreReports;
    private ArrayList<AbbreviationReport> abbreviationReports;

    public AdminPanel() {
        gameScoreReports = new ArrayList<>();
        abbreviationReports = new ArrayList<>();
    }

    public ArrayList<GameScoreReport> getGameScoreReports() {
        return gameScoreReports;
    }

    public void setGameScoreReports(ArrayList<GameScoreReport> gameScoreReports) {
        this.gameScoreReports = gameScoreReports;
        notifyObservers();
    }

    public ArrayList<AbbreviationReport> getAbbreviationReports() {
        return abbreviationReports;
    }

    public void setAbbreviationReports(ArrayList<AbbreviationReport> abbreviationReports) {
        this.abbreviationReports = abbreviationReports;
        notifyObservers();
    }

    @Override
    public void registerObserver(AdminObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(AdminObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (AdminObserver observer : this.observers) {
            observer.update(this);
        }
    }
}
