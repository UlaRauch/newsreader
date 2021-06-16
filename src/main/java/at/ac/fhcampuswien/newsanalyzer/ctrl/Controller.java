package at.ac.fhcampuswien.newsanalyzer.ctrl;

import at.ac.fhcampuswien.NewsApiException.NewsApiException;
import at.ac.fhcampuswien.newsapi.NewsApi;
import at.ac.fhcampuswien.newsapi.NewsApiBuilder;
import at.ac.fhcampuswien.newsapi.beans.Article;
import at.ac.fhcampuswien.newsapi.beans.NewsResponse;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
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
            e.printStackTrace();
            throw new NewsApiException("Couldn't load any articles. Try different Parameters. " + e.getMessage());
        } catch (NewsApiException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        if (articles != null && !articles.isEmpty()) {
            articles.stream().forEach(article -> System.out.println(article.toString()));


            System.out.println("\n-------Analysis--------");

            //number of articles found:
            printNumberofArticles(articles);

            //provider with the most articles
            printMostProlificProvider(articles);

            //titles sorted
            printTitlesSorted(articles);

            //load original articles
            System.out.println("Do you want to download these articles? type y for yes, or any other key to get back to the menu: ");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine();
            if (answer.equals("y")) {
                try {
                    downLoadArticles(articles);
                } catch (NewsApiException e) {
                    System.err.println(e.getMessage());
                }
            }


        } else {
            System.out.println("Sorry, no articles in your chosen category today");
        }

        System.out.println("End process");
    }


    public Object getData() {

        return null;
    }

    public void printNumberofArticles(List<Article> articles) {
        long numberOfArticles = articles.stream().count();
        System.out.println(numberOfArticles + " articles found");
    }

    public void printMostProlificProvider(List<Article> articles) {
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
    }

    public void printShortestAuthorName(List<Article> articles) {
        Optional<String> author = articles.stream()
                .map(Article::getAuthor)
                .filter(Objects::nonNull)
                .filter(name -> !name.equals(""))
                .min(Comparator.comparing(name -> name.length()));
        System.out.println("Shortest Author name: " + author.orElse("no author name"));
    }

    public void printAllAuthorNamesSorted(List<Article> articles) {
        //Shortest author name
        printShortestAuthorName(articles);
        System.out.println("All authors sorted by length: ");
        articles.stream()
                .map(Article::getAuthor)
                .filter(Objects::nonNull)
                .filter(name -> !name.equals(""))
                .sorted(Comparator.comparing(String::length))
                .forEach(System.out::println);
    }

    public void printTitlesSorted(List<Article> articles) {
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

    public void downLoadArticles(List<Article> articles) throws NewsApiException {
        for (Article article :
                articles) {
            try {
                URL url = new URL(article.getUrl()); //create URL object
                InputStream inputstream = url.openStream(); //create Inputstream
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream)); //create reader object to read from inputstream
                //target filename and directory
                String filename = article.getTitle().substring(0, 15).replaceAll(" ", "_") + ".html";
                File directory = new File("C:\\Users\\urauc\\Documents\\Ausbildung\\FH\\Programmieren\\PROG2\\Exercises\\prog2-uebung3-newsreader-api\\downloads");
                File filePath = new File(directory, filename);
                //write file from stream
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                }
                reader.close();
                writer.close();
                System.out.println("downloaded file " + filename + " to directory: " + directory);
            } catch (MalformedURLException e) {
                throw new NewsApiException("Unvalid URL format for Article: " + article.getTitle() + " " + article.getUrl() + " " + e.getMessage());
            } catch (IOException e) {
                throw new NewsApiException("Problem while trying to read article from stream: " + article.getTitle() + " " + article.getUrl() + " " + e.getMessage());
            } catch (Exception e) {
                throw new NewsApiException("An error has occurred while trying to download article: " + article.getTitle() + " " + article.getUrl() + " " + e.getMessage());
            }
        }
    }

}
