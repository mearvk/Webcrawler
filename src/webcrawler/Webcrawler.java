package webcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Webcrawler implements Runnable
{
    public Registrar registrar = new Registrar();
    
    public Initializer initializer = new Initializer();   
    
    public static Map<String, Object> values = new HashMap();
    
    //
    
    public static void main(String[] args)
    {
        Webcrawler webcrawler = new Webcrawler();
        
        //
        
        webcrawler.registrar.register(Preinitialization.class);
        
        webcrawler.registrar.register(ModuleOne.class);
        
        webcrawler.registrar.register(ModuleTwo.class);
        
        //
        
        webcrawler.initializer.initialize();              
        
        //
        
        values.put("webcrawler", webcrawler);
        
        values.put("initializer", webcrawler.initializer);
        
        values.put("registrar", webcrawler.registrar);
        
        //
        
        webcrawler.run();
    }
    
    public void run()
    {
        for(Class _class : registrar.classes)
        {
            Object object=null; 
            
            Runnable runner=null;
            
            try
            {
                object = _class.newInstance();
            }
            catch(Exception e)
            {
                e.printStackTrace(); return;
            }
            
            if(object instanceof Runnable)
            {
                runner = (Runnable)object;
            }
            
            if(runner!=null)
            {
                runner.run();
            }
            else
            {
                System.out.println("No runner object was found for class "+_class.getName());
            }            
        }
    }
}

class ModuleOne implements Runnable
{
    //pull the website recursively 
   
    public void run()
    {        
        ArrayList<String> websites = (ArrayList<String>)((Initializer)Webcrawler.values.get("initializer")).variables.get("websites");
        
        for(String websiteURL : websites)
        {
            WebcrawlerParam param = new WebcrawlerParam();
            
            //
            
            try                
            {            
                param.baseURL = websiteURL;
                
                param.href = websiteURL;

                param.siteHTML = this.dorequest(param);

                param.siteAnchors = this.doparseanchors(param);

                param.recurseMessage = this.dorecurse(param);

                //param.persistMessage = this.dopersist(param);
            
            }
            catch(Exception e)
            {
                //e.printStackTrace();
            }           
        }
    }
    
    public ArrayList<String> doparseanchors(WebcrawlerParam param) throws Exception
    {
        ArrayList<String> anchorlist = new ArrayList();
        
        //
                       
        Matcher matcher = Pattern.compile("<a.*?>(?:.*?)</a>").matcher(param.siteHTML); //parse <a href=""></a> matches for now..
        
        //
        
        while(matcher.find())
        {
            anchorlist.add(matcher.group());
        }
        
        //
        
        return anchorlist;
    }
        
    public String doparsehref(WebcrawlerParam param) 
    {
        //<a\s+(?:[^>]*?\s+)?href=(["'])(.*?)\1
     
        String href = null;
        
        //
                       
        Matcher matcher = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=([\"'])(.*?)\\1").matcher(param.siteHTML); //parse <a href=""></a> matches for now..
        
        //
        
        while(matcher.find())
        {
            href = matcher.group();
        }
            
        //
            
        return href;
    }
    
    public String dodeterminefullpathforpersist(WebcrawlerParam param) throws Exception
    {
        if(param==null) throw new NullPointerException();
        
        if(param.baseURL==null) throw new NullPointerException();
        
        if(param.href==null) throw new NullPointerException();
        
        //
        
        String retval=null;
        
        //
        
        if(param.href.startsWith("/")) //relative path
        {
            retval = param.baseURL+System.getProperty("file.separator")+param.href;

            retval = retval.replace(":", ".colon.");

            retval = retval.replace("//", ".fsfs.");

            retval = retval.replace("..", ".");

            //System.out.println("UNQUALIFIED URL LOOKS LIKE "+retval);            
        }
        else //full path in HREF link
        {                  
            retval = param.href;

            retval = retval.replace(":", ".colon.");

            retval = retval.replace("//", ".fsfs.");

            retval = retval.replace("..", ".");

            //System.out.println("UNQUALIFIED URL LOOKS LIKE "+retval);
        }
        
        return retval;
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
            
        connection.setReadTimeout(1000);
            
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
            
        //System.out.println("RETURNING VALUE FOR "+param.href);
        
        //
        
        param.siteHTML=builder.toString();
        
        param.unqualifiedURL = this.dodeterminefullpathforpersist(param);                

        //
        
        this.dopersist(param);
        
        //
            
        return builder.toString();                        
    }
    
    public String dorecurse(WebcrawlerParam param) throws Exception
    {       
        ArrayList<String> anchors = param.siteAnchors;
        
        ArrayList<String> errors = null;
                                           
        //
            
        if(anchors==null) return null;
        
        if(anchors.size()==0) return null;
        
        //
        
        
        for(int i=0; i<anchors.size(); i++)
        {
            WebcrawlerParam recursiveparam = new WebcrawlerParam();
            
            String anchor = anchors.get(i);
                    
            //System.out.println("ANCHOR: "+anchor);

            
            if(anchor==null) continue;
                
            try
            {                               
                
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        
                Document document = builder.parse(new InputSource(new StringReader(anchor)));
                
                //
                
                recursiveparam.href = document.getDocumentElement().getAttribute("href");
                
                //
                
                if(recursiveparam.href==null) continue;
                
                if(!recursiveparam.href.contains("http")) continue;                                        
                
                //
                
                //System.out.println("HREF: "+recursiveparam.href);
                
                //
                                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.anchor = anchor;                                
                               
                recursiveparam.siteHTML = this.dorequest(recursiveparam);                             
                
                //
                
                this.dorecurse(recursiveparam);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

            
        return "success";
    }
    
    public String dopersist(WebcrawlerParam param) throws Exception
    {
        Date date = new Date();
        
        Integer month = date.getMonth()+1;
        
        Integer day = date.getDate();
        
        Integer year = date.getYear();  
        
        //
        
        DecimalFormat monthformat = new DecimalFormat("##");
        
        DecimalFormat yearformat = new DecimalFormat("##");
        
        DecimalFormat dayformat = new DecimalFormat("##");
        
        //
        
        String smonth = monthformat.format(month);
        
        String syear = "20"+yearformat.format(year).substring(1);
        
        String sday = dayformat.format(day);
        
        //                
        
        String fileref = "/home/oem/Desktop/Webpages/storage/"+smonth+"-"+sday+"-"+syear+"/"+param.unqualifiedURL+"/index.html";
        
        //
        
        System.out.println("TRYING TO PERSIST : "+fileref);
    
        try
        {
            File dir = new File(fileref);
            
            if(!dir.exists())dir.mkdirs();
            
            //
            
            File file = new File(fileref);
            
            if(file.exists()) file.delete();
            
            //
            
            FileWriter writer = new FileWriter(file);
            
            //
            
            writer.write(param.siteHTML);
            
            writer.flush();
            
            writer.close();
        }
        catch(Exception e)
        {
            
            e.printStackTrace();
            
            
            return e.getMessage();
        }
        
        return "success";
    }
}

class ModuleTwo implements Runnable
{    
    public void run()
    {
        //
    }
}

class WebcrawlerParam
{
    public String siteHTML;
            
    public ArrayList<String> siteAnchors;
    
    public String href;
           
    public String recurseMessage;
            
    public String persistMessage;
    
    public String baseURL;
    
    public String unqualifiedURL;
    
    public String anchor;
}