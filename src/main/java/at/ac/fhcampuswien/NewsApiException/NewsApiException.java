package at.ac.fhcampuswien.NewsApiException;


public class NewsApiException extends Exception{

    /*
    is thrown when another exception occurs
    should take the message of those exception as String argument (in most cases)
     */
    public NewsApiException (String msg) {
        super(msg);
    }

}
