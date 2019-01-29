package i14013.engine;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.validator.routines.UrlValidator;

/**
 *
 * @author Gabriella
 */
public class UrlChecker {

    public static boolean isValid(String url) {

        boolean isValid = false;
        try {
            String[] schemes = {"http", "https"};
            boolean isValidUrl = (new UrlValidator(schemes)).isValid(url);
            boolean isAReference = (new URL(url).getRef()) != null;

            isValid = isValidUrl && !isAReference;
        } catch (MalformedURLException ex) {

        } finally {
            return isValid;
        }
    }

}
