/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Max Rupplin (sr scientologist)
 */
public class Preinitialization implements Runnable
{
    public void run()
    {
        WebcrawlerParam param = new WebcrawlerParam();
        
        param.baseURL = "https://www.quantcast.com/top-sites/";
        
        param.href = "https://www.quantcast.com/top-sites/";
        
        //
        
        try
        {
            this.dorequest(param);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        //
        
        Initializer initializer = null;
        
        initializer = (Initializer)Webcrawler.values.get("initializer");
        
        //
        
        ArrayList<String> websites;
        
        websites = (ArrayList<String>)initializer.variables.get("websites");
        
        //
        
        websites.add("https://en.wikipedia.org/wiki/Computer_science");
        
        websites.add("https://en.wikipedia.org/wiki/Mathematics");
        
        websites.add("https://en.wikipedia.org/wiki/Logic");

        websites.add("https://en.wikipedia.org/wiki/Constitution");

        websites.add("https://en.wikipedia.org/wiki/Church");
        
        websites.add("https://en.wikipedia.org/wiki/Xenu");
        
        websites.add("https://en.wikipedia.org/wiki/Religion");

        websites.add("https://en.wikipedia.org/wiki/Spirituality");

        websites.add("https://en.wikipedia.org/wiki/Philosophy");

        websites.add("https://en.wikipedia.org/wiki/Network_analysis_(electrical_circuits)");

        websites.add("https://en.wikipedia.org/wiki/Calculus");
        
        websites.add("https://en.wikipedia.org/wiki/Trigonometry");
        
        websites.add("https://en.wikipedia.org/wiki/Psychology");

        websites.add("https://en.wikipedia.org/wiki/Psychiatry");
        
        websites.add("https://en.wikipedia.org/wiki/Pharmacology");

        websites.add("https://en.wikipedia.org/wiki/Algebra");
        
        websites.add("https://en.wikipedia.org/wiki/Linear_algebra");
        
        websites.add("https://en.wikipedia.org/wiki/Science");
        
        websites.add("https://en.wikipedia.org/wiki/Chemistry");        
        
        websites.add("https://en.wikipedia.org/wiki/Intelligence_quotient");
        
        websites.add("https://en.wikipedia.org/wiki/President_of_the_United_States");

        websites.add("https://en.wikipedia.org/wiki/Vice_President_of_the_United_States");
        
        websites.add("https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_by_population");

        websites.add("https://en.wikipedia.org/wiki/Wikipedia");

        websites.add("https://en.wikipedia.org/wiki/Pope");

        websites.add("https://en.wikipedia.org/wiki/Pope_Francis");
                
        websites.add("https://en.wikipedia.org/wiki/L._Ron_Hubbard");

        websites.add("https://en.wikipedia.org/wiki/Truth");
        
        websites.add("https://en.wikipedia.org/wiki/Java_(programming_language)");        
               
        websites.add("https://en.wikipedia.org/wiki/C_(programming_language)");
        
        websites.add("https://en.wikipedia.org/wiki/C%2B%2B");
        
        websites.add("https://en.wikipedia.org/wiki/Music");
        
        //
        
        
    }
    
    public String dorequest(WebcrawlerParam param) throws Exception
    {
        URL url=null;
        
        HttpURLConnection connection=null;
        
        //

        url = new URL(param.href);            

        
        //
        
        if(url==null) return null;
            
        //
            
        connection = (HttpURLConnection)url.openConnection();
            
        connection.setRequestMethod("GET");
            
        connection.setReadTimeout(5000);
            
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
                    
        //
        
        if(connection==null) return null;
            
        int responsecode = connection.getResponseCode();
            
        //
            
        StringBuilder builder = new StringBuilder();
            
        String string=null;
            
        //
            
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            
        while((string=reader.readLine())!=null)
        {
            builder.append(string);
        }
                                      
        //
        
        param.siteHTML=builder.toString();
        
        ArrayList<String> websites = this.doparseHTMLlinks(param);         
        
        //
        
        Initializer initializer = null;
        
        initializer = (Initializer)Webcrawler.values.get("initializer");
        
        initializer.variables.put("websites", websites);
        
        //
            
        return builder.toString();                        
    }    
    
    public ArrayList<String> doparseHTMLlinks(WebcrawlerParam param)
    {        
        ArrayList<String> links = new ArrayList<String>();
        
        //
                       
        Matcher matcher = Pattern.compile("(name=\".*?\").*?", Pattern.DOTALL).matcher(param.siteHTML); //parse <a href=""></a> matches for now..
        
        //
        
        while(matcher.find())
        {
            String s = matcher.group();
            
            if(s.startsWith("name"))
            {
                s = s.replace("name=", "https://").replace("\"", "");
                
                System.out.println(s);
            }
            else 
            {
                System.out.println("No match");
                
                continue;
            }            
            
            links.add(s);
        }
        
        return links;
    }
}
            