package at.ac.fhcampuswien.newsapi;

import at.ac.fhcampuswien.NewsApiException.NewsApiException;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;
import at.ac.fhcampuswien.newsapi.enums.Category;
import at.ac.fhcampuswien.newsapi.enums.Country;
import at.ac.fhcampuswien.newsapi.enums.Endpoint;
import at.ac.fhcampuswien.newsapi.enums.Language;

import java.util.List;

public class NewsAPIExample {

    public static final String APIKEY = "a3dc3f7d35a44956a7c27fd64f067322";    //TODO add your api key

    public static void main(String[] args){

        NewsApi newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY) //exc - key doesn't exist -> nullpointer? funktioniert nicht
                .setQ("") //exc - not a string -> Numberformat exception?
                .setEndPoint(Endpoint.TOP_HEADLINES)// example of how to use enums
                .setSourceCountry(Country.at)       // example of how to use enums
                .setSourceCategory(Category.science) // example of how to use enums
                .setLanguage(Language.en)
                .createNewsApi();

        NewsResponse newsResponse = null;
        try {
           newsResponse = newsApi.getNews();
            if(newsResponse != null){
                List<Article> articles = newsResponse.getArticles();
                articles.stream().forEach(article -> System.out.println(article.toString()));

            }
            } catch (NewsApiException e) {
            System.err.println(e.getMessage());
        }

        newsApi = new NewsApiBuilder()
                .setApiKey(APIKEY)
                .setQ("corona")
                .setEndPoint(Endpoint.EVERYTHING)
                .setFrom("2021-06-12")
                .setExcludeDomains("Lifehacker.com")
                .createNewsApi();

        try {
            newsResponse = newsApi.getNews();
        } catch (NewsApiException e) {
            System.err.println(e.getMessage());
        }

        if(newsResponse != null){
            List<Article> articles = newsResponse.getArticles();
            articles.stream().forEach(article -> System.out.println(article.toString()));
        }
    }
}
