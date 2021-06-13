package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;

import java.util.List;

public class Controller {


	public static final String APIKEY = "a3dc3f7d35a44956a7c27fd64f067322";  //TODO add your api key

	public void process(NewsApiBuilder newsApiBuilder) {
		System.out.println("Start process");

		//TODO implement Error handling
		//Welche Exceptions?
		//u
		NewsApi newsApi = newsApiBuilder.setApiKey(APIKEY).createNewsApi();

		//u von Example übernommen
		NewsResponse newsResponse = newsApi.getNews();
		if(newsResponse != null){
			List<Article> articles = newsResponse.getArticles();
			articles.stream().forEach(article -> System.out.println(article.toString()));
		}

		//TODO implement methods for analysis

		System.out.println("End process");
	}
	

	public Object getData() {
		
		return null;
	}
}
