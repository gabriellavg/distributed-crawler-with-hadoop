package i14013.engine;

/**
 *
 * @author Gabriella
 */
public class CrawlingTask {

    private String url, mode, root;
    private int startDepth, maxDepth;

    public CrawlingTask(String url, String mode, int startDepth, int maxDepth, String root) {
        this.url = url;
        this.mode = mode;
        this.startDepth = startDepth;
        this.maxDepth = maxDepth;
        this.root = root;
    }

    public String getUrl() {
        return url;
    }

    public String getMode() {
        return mode;
    }

    public String getRoot() {
        return root;
    }

    public int getStartDepth() {
        return startDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

}
