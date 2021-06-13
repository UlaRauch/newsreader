package at.ac.fhcampuswien.newsanalyzer.ui;


import at.ac.fhcampuswien.newsanalyzer.ctrl.Controller;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.enums.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface 
{
	private Controller ctrl = new Controller();

	//u

	public void getDataFromCtrl1(){
		System.out.println("Today's Science Headlines in english\n");


		NewsApiBuilder newsApiBuilder = new NewsApiBuilder()
				.setQ("science")
				.setEndPoint(Endpoint.EVERYTHING)
				//.setSourceCategory(Category.science) // category not supported for endpoint EVERYTHING
				.setFrom(new java.sql.Date(System.currentTimeMillis()).toString()) //today
				.setLanguage(Language.en)
				.setSortBy(SortBy.POPULARITY);

		ctrl.process(newsApiBuilder);
	}

	public void getDataFromCtrl2(){
		System.out.println("Yesterday's news\n");

		NewsApiBuilder newsApiBuilder = new NewsApiBuilder()
				.setQ("")
				.setEndPoint(Endpoint.TOP_HEADLINES)// example of how to use enums
				.setSourceCategory(Category.entertainment) // example of how to use enums
				.setFrom("2021-06-12");

		ctrl.process(newsApiBuilder);
	}

	public void getDataFromCtrl3(){

		System.out.println("Penguins");

		NewsApiBuilder newsApiBuilder = new NewsApiBuilder()
				.setQInTitle("penguin")
				.setEndPoint(Endpoint.TOP_HEADLINES)
				.setSourceCategory(Category.science)
				.setSortBy(SortBy.RELEVANCY);

		ctrl.process(newsApiBuilder);

	}
	
	public void getDataForCustomInput() {
		NewsApiBuilder newsApiBuilder = new NewsApiBuilder();
		// TODO implement me
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitle("Wählen Sie aus:");
		menu.insert("a", "Today's science articles in english", this::getDataFromCtrl1);
		menu.insert("b", "Yesterday's news", this::getDataFromCtrl2);
		menu.insert("c", "Everything about penguins", this::getDataFromCtrl3);
		menu.insert("d", "Choice User Input:",this::getDataForCustomInput);
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

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
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
