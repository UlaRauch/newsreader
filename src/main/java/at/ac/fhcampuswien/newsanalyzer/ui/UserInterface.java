package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.NewsApiException.NewsApiException;
import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class UserInterface {
    private Controller ctrl = new Controller();


    public void getDataFromCtrl1() {
        System.out.println("Science Headlines\n");

        NewsApiBuilder newsApiBuilder = new NewsApiBuilder()
                .setQ("")
                .setEndPoint(Endpoint.TOP_HEADLINES)
                .setSourceCategory(Category.science) // category not supported for endpoint EVERYTHING
                .setLanguage(Language.en)
                .setFrom(new java.sql.Date(System.currentTimeMillis()).toString()) //today
                .setSortBy(SortBy.POPULARITY);

        try {
            ctrl.process(newsApiBuilder);
        } catch (NewsApiException e) {
            System.err.println(e.getMessage());
        }
    }

    public void getDataFromCtrl2() {
        System.out.println("Österreichische Politik\n");

        NewsApiBuilder newsApiBuilder = new NewsApiBuilder()
                .setQ("politik")
                .setEndPoint(Endpoint.TOP_HEADLINES)
                .setSourceCountry(Country.at)
                //.setFrom("2021-06-12")
                .setSortBy(SortBy.RELEVANCY);

        try {
            ctrl.process(newsApiBuilder);
        } catch (NewsApiException e) {
            System.err.println(e.getMessage());
        }
    }

    public void getDataFromCtrl3() {

        System.out.println("Climate change");

        NewsApiBuilder newsApiBuilder = new NewsApiBuilder()
                .setQ("climate")
                .setEndPoint(Endpoint.TOP_HEADLINES) //Everything is not currently supported with Country param
                .setSortBy(SortBy.RELEVANCY);

        try {
            ctrl.process(newsApiBuilder);
        } catch (NewsApiException e) {
            System.err.println(e.getMessage());
        }
    }

    public void getDataForCustomInput() {
        NewsApiBuilder newsApiBuilder = new NewsApiBuilder();
        //parameter nicht setzen, nur an process weitergeben
        //get Keyword
        System.out.println("type a keyword: ");
        Scanner scanner = new Scanner(System.in);
        String q = scanner.nextLine();
        newsApiBuilder.setQ(q)
                .setEndPoint(Endpoint.TOP_HEADLINES);
        try {
            ctrl.process(newsApiBuilder);
        } catch (NewsApiException e) {
            System.err.println(e.getMessage());
        }
    }


    public void start() {
        Menu<Runnable> menu = new Menu<>("User Interface");
        menu.setTitle("Wählen Sie aus:");
        menu.insert("a", "Science Headlines", this::getDataFromCtrl1);
        menu.insert("b", "Österreichische Politik", this::getDataFromCtrl2);
        menu.insert("c", "Climate", this::getDataFromCtrl3);
        menu.insert("d", "Choice User Input:", this::getDataForCustomInput);
        menu.insert("q", "Quit", null);
        Runnable choice;
        while ((choice = menu.exec()) != null) {
            choice.run();
        }
        System.out.println("Program finished");
    }


    protected String readLine() {
        String value = "\0";
        BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            value = inReader.readLine();
        } catch (IOException ignored) {
        }
        return value.trim();
    }

    protected Double readDouble(int lowerlimit, int upperlimit) {
        Double number = null;
        while (number == null) {
            String str = this.readLine();
            try {
                number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
                System.out.println("Please enter a valid number:");
                continue;
            }
            if (number < lowerlimit) {
                System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
                System.out.println("Please enter a lower number:");
                number = null;
            }
        }
        return number;
    }
}
