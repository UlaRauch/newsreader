package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.NewsApiException.NewsApiException;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;

import java.util.*;
import java.util.stream.Collectors;

public class Controller {


    public static final String APIKEY = "a3dc3f7d35a44956a7c27fd64f067322";  //TODO add your api key

    public void process(NewsApiBuilder newsApiBuilder) throws NewsApiException {
        System.out.println("Start process");

        NewsApi newsApi = newsApiBuilder
                .setApiKey(APIKEY)
                .setExcludeDomains("Lifehacker.com")
                .createNewsApi();

        NewsResponse newsResponse = null;
        List<Article> articles = null;
        try {
            newsResponse = newsApi.getNews();
            articles = newsResponse.getArticles();
        } catch (NullPointerException e) {
            throw new NewsApiException("Couldn't load any articles. Try different Parameters");
        } catch (NewsApiException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            throw new NewsApiException("There was an error: " + e.getMessage());
        }

        if (articles != null && !articles.isEmpty()) {
            articles.stream().forEach(article -> System.out.println(article.toString()));


            System.out.println("\n-------Analysis--------");

            //number of articles found:
            long numberOfArticles = articles.stream().count();
            System.out.println(numberOfArticles + " articles found");

            //provider with the most articles
            //create Map grouped by source name, matched with number of occurrence
            Map<String, Long> sourceFrequencyMap = articles.stream()
                    .collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()));

            //extract top entry from Map
            //flaw: gets only one name, does not give other providers with same number of articles
            Optional<Map.Entry<String, Long>> mostFrequentProvider = sourceFrequencyMap
                    .entrySet()
                    .stream()
                    .max(Comparator.comparingLong(name -> name.getValue()));
            //print result
            mostFrequentProvider.ifPresent(stringLongEntry -> System.out.println(
                    "Most prolific provider: " + stringLongEntry.getKey()
                            + " with " + stringLongEntry.getValue() + " articles"));

            /*
            short version:
            	String provider = articles.stream()
				.collect(Collectors.groupingBy(article -> article.getSource().getName(), Collectors.counting()))
				.entrySet()
				.stream()
				.max(Comparator.comparingInt(entry->entry.getValue().intValue()))
				.get()
				.getKey();
		        System.out.println("Most prolific provider: " + provider);

		        //to fix: get() does not check, if there is any result -> use ifPresent() or isPresent()

             */

            //Shortest author name
            Optional<String> author = articles.stream()
                    .map(Article::getAuthor)
                    .filter(Objects::nonNull)
                    .filter(name -> !name.equals(""))
                    .min(Comparator.comparing(name -> name.length()));
            System.out.println("Shortest Author name: " + author.orElse("no author name"));

/*
        System.out.println("All authors sorted by length: ");
        articles.stream()
                .map(Article::getAuthor)
                .filter(Objects::nonNull)
                .filter(name->!name.equals(""))
                .sorted(Comparator.comparing(String::length))
                .forEach(System.out::println);

 */

            //titles sorted
            System.out.println("Titles sorted by length in alphabetical order:");
            articles.stream()
                    .map(Article::getTitle)
                    .filter(Objects::nonNull)
                    .filter(title -> !title.equals(""))
                    .sorted(Comparator.comparing(String::length)
                            .reversed()
                            .thenComparing(String::length))
                    .forEachOrdered(System.out::println);
        }

        System.out.println("End process");
    }


    public Object getData() {

        return null;
    }

}
