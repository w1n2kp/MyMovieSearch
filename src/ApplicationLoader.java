import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Application Lifecycle Listener implementation class ApplicationLoader
 */
@WebListener
public class ApplicationLoader implements ServletContextListener {

    /**
     * Default constructor.
     */
    public ApplicationLoader() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }

    /**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        ServletContext ctx = arg0.getServletContext();
        String configFile = ctx.getRealPath("/") + "/config.xml";

        Hashtable<String, String> values = loadXmlConfig(configFile);
        Set<String> keySet = values.keySet();
        Iterator<String> it = keySet.iterator();

        while (it.hasNext()) {
            String key = it.next();
            String val = values.get(key);
            ctx.setAttribute(key, val);
        }

    }

    private Hashtable<String, String> loadXmlConfig(String filename) {

        Hashtable<String, String> values = new Hashtable<String, String>();

        Document doc = null;

        try {
            File file = new File(filename);
            SAXBuilder builder = new SAXBuilder();
            doc = builder.build(file);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Element root = doc.getRootElement();

        List<Element> settings = root.getChild("appSettings").getChildren("add");

        for (Element e : settings) {
            values.put(e.getAttributeValue("key"), e.getAttributeValue("value"));
        }

        return values;

    }

}
