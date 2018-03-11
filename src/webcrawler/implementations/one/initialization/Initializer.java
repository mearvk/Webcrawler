package webcrawler.implementations.one.initialization;

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
        
        variables.put("time_accrued", 1000L);
        
        //
        
        ArrayList<String> websites = new ArrayList();
        
        //predefined.add("https://washingtonpost.com");
        
        //predefined.add("https://google.com");
        
        //predefined.add("https://microsoft.com");
        
        //predefined.add("https://mozilla.com");
        
        //predefined.add("https://oracle.com");
        
        //predefined.add("https://ebay.com");
        
        //predefined.add("https://adobe.com");
        
        //
        
        variables.put("predefined", websites);
    }
}
