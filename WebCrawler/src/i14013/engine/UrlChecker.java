package i14013.engine;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author Gabriella
 */
public class UrlChecker {

    public static String getUrlInfo(String url) {
        String info = "Invalid URL";
        try {
            if (!isValidUrl(url)) {
                info = "Invalid URL";
            } else {
                if (!isValidProtocol(url)) {
                    info = "URL protocol is not http/https";
                } else {
                    if (isAReference(url)) {
                        info = "URL is a reference";
                    } else {
                        info = "Valid URL";
                    }
                }
            }
        } catch (MalformedURLException ex) {

        } finally {
            return info;
        }
    }

    private static boolean isValidUrl(String url) {
        return (new UrlValidator()).isValid(url);
    }

    private static boolean isValidProtocol(String url) throws MalformedURLException {
        return (new URL(url).getProtocol()).equals("http")
                || (new URL(url).getProtocol()).equals("https");
    }

    private static boolean isAReference(String url) throws MalformedURLException {
        return (new URL(url).getRef()) != null;
    }

}
