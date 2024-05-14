import net.javacoding.jspider.*;
import net.javacoding.jspider.api.event.JSpiderEvent;
import net.javacoding.jspider.api.event.JSpiderListener;
import net.javacoding.jspider.api.model.*;
import net.javacoding.jspider.core.util.config.ConfigurationFactory;

public class test {

    public static void main(String[] args) {
        try {
            JSpiderConfiguration config = ConfigurationFactory.getConfiguration("default");
            JSpider spider = new JSpider(config);
            
            spider.addJSpiderListener(new MyJSpiderListener());
            
            spider.addURL("http://example.com");
            
            spider.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class MyJSpiderListener implements JSpiderListener {
        
        public void handleJSpiderEvent(JSpiderEvent event) {
            if (event instanceof SpiderFoundURL) {
                SpiderFoundURL foundURL = (SpiderFoundURL) event;
                System.out.println("Found URL: " + foundURL.getURL());
            } else if (event instanceof SpiderVisitedURL) {
                SpiderVisitedURL visitedURL = (SpiderVisitedURL) event;
                System.out.println("Visited URL: " + visitedURL.getURL());
                
                SpiderHTTP http = visitedURL.getHTTP();
                String content = new String(http.getResponseContent());
                System.out.println("Content: " + content);
            }
        }
    }
}
