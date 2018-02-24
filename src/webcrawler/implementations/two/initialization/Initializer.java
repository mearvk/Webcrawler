package webcrawler.implementations.two.initialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Max Rupplin
 */
public class Initializer extends webcrawler.intialization.Initializer
{    
    public String baseURL;
    
    public Map<String, Object> variables = new HashMap();
    
    public Initializer()
    {
        
    }
    
    public void initialize()
    {
        //
        
        variables.put("initializer", this);
                
        //        
                
        variables.put("method", "GET");
        
        variables.put("user-agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:56.0) Gecko/20100101 Firefox/56.0");
        
        variables.put("timeout", 1000L);
        
        //
        
        ArrayList<String> websites = new ArrayList();
        
        //websites.add("https://washingtonpost.com");
        
        //websites.add("https://google.com");
        
        //websites.add("https://microsoft.com");
        
        //websites.add("https://mozilla.com");
        
        //websites.add("https://oracle.com");
        
        //websites.add("https://ebay.com");
        
        //websites.add("https://adobe.com");
        
        //
        
        variables.put("websites", websites);
    }
}
