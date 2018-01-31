/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
            