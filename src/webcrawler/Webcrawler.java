package webcrawler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Webcrawler implements Runnable
{
    public Registrar registrar = new Registrar();
    
    public Initializer initializer = new Initializer();   
    
    //
    
    public static Map<String, Object> values = new HashMap();
    
    public static Map<String, String> visitedlinks = new HashMap();
    
    //
    
    public static final Integer localrecursedepth = 8;
    
    public static final Integer globalrecursedepth = 4;
    
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
                //
                
                param.baseURL = websiteURL;
                
                param.href = websiteURL;

                param.siteHTML = this.dorequest(param);

                param.siteAnchors = this.doparseanchors(param);

                param.recurseMessage = this.dosinglesiterecurse(param, 0);            
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
                       
        Matcher matcher = Pattern.compile("<a\\s+.*?>(?:.*?)</a>").matcher(param.siteHTML); //parse <a href=""></a> matches for now..
        
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
                       
        Matcher matcher = Pattern.compile("<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"").matcher(param.siteHTML); //parse <a href=""></a> matches for now..
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();
            
            //if(match.startsWith("<a")) continue; //glitch in regex fix
            
            href = match;
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
            
            /*retval = retval.replace(":", ".colon.");*/ retval = retval.replace("https", "");           

            /*retval = retval.replace(":", ".colon.");*/ retval = retval.replace(":", "");

            /*retval = retval.replace("//", ".fsfs.");*/ retval = retval.replace("//", "");

            /*retval = retval.replace("..", ".");*/ retval = retval.replace("..", "");

            //System.out.println("UNQUALIFIED URL LOOKS LIKE "+retval);            
        }
        else //full path in HREF link
        {                  
            retval = param.href;
            
            /*retval = retval.replace(":", ".colon.");*/ retval = retval.replace("https", "");                       

            /*retval = retval.replace(":", ".colon.");*/ retval = retval.replace(":", "");

            /*retval = retval.replace("//", ".fsfs.");*/ retval = retval.replace("//", "");

            /*retval = retval.replace("..", ".");*/ retval = retval.replace("..", "");

            //System.out.println("UNQUALIFIED URL LOOKS LIKE "+retval);
        }
        
        return retval;
    }
    
    public String dodeterminefullpathforhttpreference(WebcrawlerParam param, String inputURL) throws Exception
    {
        if(inputURL==null || inputURL.isEmpty()) throw new Exception();
        
        //
        
        String href = null;
        
        //
                       
        Matcher matcher = Pattern.compile("<a\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(inputURL); //parse <img src=""></img> matches for now..
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();
            
            //if(match.startsWith("<img")) continue;
            
            href = match;
        }
        
        //
        
        //param.siteImages = href;
        
        //
        
        //System.err.println("Anchor  "+inputURL+" had href value: "+href);
        
        //
        
        //return href;        
        
        //
        
        if(inputURL.startsWith("//") || inputURL.startsWith("/") || inputURL.startsWith("./"))
        {
            //move to absolute case for simplification
            
            if(inputURL.startsWith("//"))
            {
                inputURL = "https:"+inputURL;
            }
            
            if(inputURL.startsWith("/"))
            {
                inputURL = param.baseURL + inputURL;
            }
            
            if(inputURL.startsWith("./"))
            {
                inputURL = param.baseURL + inputURL;
            }                       
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.baseURL + "/" + inputURL;
        }
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");  
        
        return inputURL;
    }
    
    public String dosimplerequest(WebcrawlerParam param) throws Exception
    {
        URL url=null;
        
        HttpURLConnection connection=null;
        
        //

        //System.out.println("ModuleOne::dorequest sees for href the following value: "+param.href);
        
        //
        
        url = new URL(param.href);            

        
        //
        
        if(url==null) return null;
            
        //
            
        connection = (HttpURLConnection)url.openConnection();
            
        connection.setRequestMethod("GET");
            
        connection.setReadTimeout(10000);
            
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
                    
        //
        
        if(connection==null) return null;
            
        try
        {
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

            //System.out.println("ModuleOne:dopersist working with "+param.href);

            //

            param.siteHTML=builder.toString();

            //param.unqualifiedURL = this.dodeterminefullpathforpersist(param);                

            //

            //this.dopersist(param);        

            //

            return builder.toString();             
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("Resource "+param.href+" exists as a link but not actually a page reference. HTTP returned 404.");
        }
        catch(Exception e)
        {
            //
        }
        finally
        {
            System.gc();
        }
                       
        return null;        
    }
    
    public String dorequest(WebcrawlerParam param) throws Exception
    {
        URL url=null;
        
        HttpURLConnection connection=null;
        
        //

        //System.out.println("ModuleOne::dorequest sees for href the following value: "+param.href);
        
        //
        
        url = new URL(param.href);            

        
        //
        
        if(url==null) return null;
            
        //
            
        connection = (HttpURLConnection)url.openConnection();
            
        connection.setRequestMethod("GET");
            
        connection.setReadTimeout(10000);
            
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
                    
        //
        
        if(connection==null) return null;
            
        try
        {
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

            System.out.println("ModuleOne:dorequest working with "+param.href);

            //

            param.siteHTML=builder.toString();

            param.unqualifiedURL = this.dodeterminefullpathforpersist(param);                

            //

            this.dopersist(param);        

            //

            return builder.toString();             
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("Resource "+param.href+" exists as a link but not actually a page reference. HTTP returned 404.");
        }
        catch(Exception e)
        {
            //
        }
        finally
        {
            System.gc();
        }
                       
        return null;
    }
    
    public String dosinglesiterecurse(WebcrawlerParam param, Integer depth)
    {
        //
        
        ArrayList<String> anchors = param.siteAnchors;
        
        ArrayList<String> errors = null;
                                           
        //
            
        if(anchors==null || anchors.isEmpty())
        {
            System.out.println("Wouldn't ya believe it?  Site "+param.baseURL+" reports no links of any kind.");
            
            return null;
        }
        else
        {
            for(int i=0; i<anchors.size(); i++)
            {
                String href = this.parseanchorforhrefvalue(anchors.get(i));
                
                System.out.println("Anchor #"+i+" for site "+param.baseURL+" is : "+href);
            }
        }
               
        //        
        
        for(int i=0; i<anchors.size(); i++)
        {
            WebcrawlerParam recursiveparam = new WebcrawlerParam();
            
            String anchor = anchors.get(i);
                    
            //
            
            if(anchor==null || anchor.isEmpty()) continue;
            
            String href_quickref = this.parseanchorforhrefvalue(anchor);
            
            if( !(href_quickref.startsWith("https://www."+param.baseURL) || href_quickref.startsWith("http://wwww."+param.baseURL) || href_quickref.startsWith("http://"+param.baseURL) || href_quickref.startsWith("https://"+param.baseURL) || href_quickref.startsWith(param.baseURL) || href_quickref.startsWith("/") || href_quickref.startsWith("./") || href_quickref.startsWith("//") || href_quickref.startsWith("..") || href_quickref.startsWith("#")) ) continue;
            
            //
                
            try
            {                                                               
                //
                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.href = this.parseanchorforhrefvalue(anchor);
                
                if(recursiveparam.href==null || recursiveparam.href.isEmpty()) continue;
                
                recursiveparam.href = this.dodeterminefullpathforhttpreference(param, recursiveparam.href);
                
                //
                
                if(recursiveparam.href==null) continue;
                
                if(!recursiveparam.href.contains("http")) continue;                                        
                
                //
                
                //System.out.println("ModuleOne:dorecurse has href value: "+recursiveparam.href);
                
                //System.out.println("ModuleOne:dorecurse has baseURL value: "+recursiveparam.baseURL);

                
                //
                
                if(Webcrawler.visitedlinks.get(recursiveparam.href)==null || Webcrawler.visitedlinks.get(recursiveparam.href).isEmpty())
                {                
                    Webcrawler.visitedlinks.put(recursiveparam.href, "visited");
                }                
                else continue;
                
                //
                                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.anchor = anchor;                

                //
                
                System.out.println("ModuleOne:dosinglesiterecurse has href value: "+recursiveparam.href);
                
                System.out.println("ModuleOne:dosinglesiterecurse has baseURL value: "+recursiveparam.baseURL);

                //
                
                if(depth>=Webcrawler.localrecursedepth)
                {
                    throw new StackDepthException("Local stack depth exceeded; returning.");
                }
                               
                recursiveparam.siteHTML = this.dorequest(recursiveparam);           
                
                recursiveparam.siteAnchors = this.doparseanchors(recursiveparam);
                
                recursiveparam.recurseMessage = this.dosinglesiterecurse(recursiveparam, (1+depth));
            }
            catch(NullPointerException npe)
            {
                // check there will be an error if 404 returned by dorequest
            }
            catch(StackDepthException vdsde)
            {
                //
                
                System.out.println("-- -- -- -- --");
                
                System.out.println(vdsde.getMessage());
                
                System.out.println("-- -- -- -- --");
                
                return null;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
                //System.err.println("Error with anchor tag: "+anchor);
            }
        }

        //
        
        System.gc();
        
        //
            
        return "success";                
    }
    
    public String dorecurse(WebcrawlerParam param, Integer depth) throws Exception
    {       
        ArrayList<String> anchors = param.siteAnchors;
        
        ArrayList<String> errors = null;
                                           
        //
            
        if(anchors==null || anchors.isEmpty())
        {
            System.out.println("Wouldn't ya believe it?  Site "+param.baseURL+" had no links of any kind!");
            
            return null;
        }
        else
        {
            for(int i=0; i<anchors.size(); i++)
            {
                String href = this.parseanchorforhrefvalue(anchors.get(i));
                
                //System.out.println("Anchor #"+i+" for site "+param.baseURL+" is : "+href);
            }
        }
               
        //        
        
        for(int i=0; i<anchors.size(); i++)
        {
            WebcrawlerParam recursiveparam = new WebcrawlerParam();
            
            String anchor = anchors.get(i);
                    
            //System.out.println("ANCHOR: "+anchor);

            
            if(anchor==null || anchor.isEmpty()) continue;
                
            try
            {                                                               
                //
                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.href = this.parseanchorforhrefvalue(anchor);
                
                if(recursiveparam.href==null || recursiveparam.href.isEmpty()) continue;
                
                recursiveparam.href = this.dodeterminefullpathforhttpreference(param, recursiveparam.href);
                
                //
                
                if(recursiveparam.href==null) continue;
                
                if(!recursiveparam.href.contains("http")) continue;                                        
                
                //
                
                //System.out.println("ModuleOne:dorecurse has href value: "+recursiveparam.href);
                
                //System.out.println("ModuleOne:dorecurse has baseURL value: "+recursiveparam.baseURL);

                
                //
                
                if(Webcrawler.visitedlinks.get(recursiveparam.href)==null || Webcrawler.visitedlinks.get(recursiveparam.href).isEmpty())
                {                
                    Webcrawler.visitedlinks.put(recursiveparam.href, "visited");
                }                
                else continue;
                
                //
                                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.anchor = anchor;                

                //
                
                System.out.println("ModuleOne:dorecurse has href value: "+recursiveparam.href);
                
                System.out.println("ModuleOne:dorecurse has baseURL value: "+recursiveparam.baseURL);

                //
                
                if(depth>=Webcrawler.globalrecursedepth)
                {
                    throw new StackDepthException("Global stack depth exceeded; returning.");
                }
                               
                recursiveparam.siteHTML = this.dorequest(recursiveparam);           
                
                recursiveparam.siteAnchors = this.doparseanchors(recursiveparam);
                
                recursiveparam.recurseMessage = this.dorecurse(recursiveparam, (1+depth));
            }
            catch(NullPointerException npe)
            {
                // check there will be an error if 404 returned by dorequest
            }
            catch(StackDepthException vdsde)
            {
                //
                
                System.out.println("-- -- -- -- --");
                
                System.out.println(vdsde.getMessage());
                
                System.out.println("-- -- -- -- --");
                
                return null;
            }
            catch(Exception e)
            {
                e.printStackTrace();
                
                //System.err.println("Error with anchor tag: "+anchor);
            }
        }

        //
        
        System.gc();
        
        //
            
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
        
        String imagefileref = "/home/oem/Desktop/Webpages/storage/"+smonth+"-"+sday+"-"+syear+"/"+param.unqualifiedURL+"/images/";
        
        String javascriptfileref = "/home/oem/Desktop/Webpages/storage/"+smonth+"-"+sday+"-"+syear+"/"+param.unqualifiedURL+"/javascript/";
        
        String cssfileref = "/home/oem/Desktop/Webpages/storage/"+smonth+"-"+sday+"-"+syear+"/"+param.unqualifiedURL+"/css/";
        
        
        //
        
        //System.out.println("TRYING TO PERSIST : "+fileref);
        
        System.out.println("-- -- -- -- --");
        
        System.out.println("ModuleOne:dopersist has href value: "+param.href);
                
        System.out.println("ModuleOne:dopersist has baseURL value: "+param.baseURL);       
        
        System.out.println("ModuleOne:dopersist has unqualified value: "+param.unqualifiedURL); 
        
        System.out.println("-- -- -- -- --");
    
        try
        {
            File dir = new File(fileref);
            
            if(!dir.exists())dir.mkdirs();
            
            //
            
            File file = new File(fileref);
            
            if(file.exists()) file.delete(); //write duplicate workaround (file.txt becomes file_0001.text etc)
            
            //
            
            File imagedir = new File(imagefileref);
            
            if(!imagedir.exists()) imagedir.mkdirs();   
                       
            //
            
            File javascriptdir = new File(javascriptfileref);
            
            if(!javascriptdir.exists()) javascriptdir.mkdirs();

            //
            
            File cssdir = new File(cssfileref);
            
            if(!cssdir.exists()) cssdir.mkdirs();

            //
            
            this.parsesiteimages(param);
            
            if(param.siteImages!=null)
            {
                for(int i=0; i<param.siteImages.size(); i++)
                {
                    try
                    {
                        this.persistimage(param, this.parseimageforsrcvalue(param.siteImages.get(i)), imagefileref);
                    }
                    catch(Exception e)
                    {
                        //
                    }
                }
            }
            
            //
            
           this.parsesitecss(param);
            
            if(param.siteStyleSheets!=null)
            {
                for(int i=0; i<param.siteStyleSheets.size(); i++)
                {
                    try
                    {
                        this.persistfile(param, this.parselinkforhrefvalue(param.siteStyleSheets.get(i)), cssfileref);
                    }
                    catch(Exception e)
                    {
                        //
                    }
                }
            }

            //            
            
            this.parsesitescripts(param);
                       
            if(param.siteScripts!=null)
            {
                for(int i=0; i<param.siteScripts.size(); i++)
                {
                    try
                    {
                        this.persistfile(param, this.parsescriptforsrcvalue(param.siteScripts.get(i)), javascriptfileref);
                    }
                    catch(Exception e)
                    {
//                      //
                    }
                }        
            }

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
        finally
        {
            System.gc();
        }
        
        return "success";
    }
    
    public ArrayList<String> parsesitecss(WebcrawlerParam param)
    {
        ArrayList<String> anchorlist = new ArrayList();
        
        String originalsiteHTML = new StringBuffer(param.siteHTML).toString();
        
        //
                       
        Matcher matcher = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.siteHTML); //parse <img src=""></img> matches for now..
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();
            
            //;
            
            anchorlist.add(match);
        }      
        
        //
        
        for(int i=0; i<anchorlist.size(); i++)
        {
            String anchor = anchorlist.get(i);
            
            String href = null;
            
            //
            
            href = this.parselinkforhrefvalue(anchor);
            
            if(href.endsWith(".css")) 
            {
                continue;
            }
            
            try
            {
                this.dorequest(param);
                
                matcher = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.siteHTML); //parse <img src=""></img> matches for now..

                //

                while(matcher.find())
                {
                    String match = matcher.group();

                    //

                    anchorlist.add(match);
                }                   
            }
            catch(Exception e)
            {
                //
            }                                
        }
        
        //
        
        for(int i=0; i<anchorlist.size(); i++)
        {
            String anchor = anchorlist.get(i);
            
            String href = this.parselinkforhrefvalue(anchor);
            
            //
            
            if( !href.endsWith(".css") )
            {
                anchorlist.remove(i);
            }
        }        
        
        //
        
        param.siteStyleSheets = anchorlist;
        
        param.siteHTML = originalsiteHTML;
        
        //
        
        //System.err.println("Site "+param.baseURL+" had "+param.siteStyleSheets.size()+" css link(s).");
        
        //
        
        return anchorlist;        
    }     
    
    public ArrayList<String> parsesiteimages(WebcrawlerParam param)
    {
        ArrayList<String> anchorlist = new ArrayList();
        
        //
                       
        Matcher matcher = Pattern.compile("<img\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.siteHTML); //parse <img src=""></img> matches for now..
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();
            
            //if(match.startsWith("<img")) continue;
            
            anchorlist.add(match);
        }
        
        //
        
        param.siteImages = anchorlist;
        
        //
        
        //System.err.println("Site "+param.baseURL+" had "+param.siteImages.size()+" image tag(s).");
        
        //
        
        return anchorlist;        
    } 
    
    public ArrayList<String> parsesitescripts(WebcrawlerParam param)
    {
        ArrayList<String> anchorlist = new ArrayList();
        
        //
                       
        Matcher matcher = Pattern.compile("<script\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.siteHTML); //parse <script src=""></script> matches for now..
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();
            
            //if(match.startsWith("<img")) continue;
            
            anchorlist.add(match);
        }
        
        //
        
        param.siteScripts = anchorlist;
        
        //
        
        //System.err.println("Site "+param.baseURL+" had "+param.siteScripts.size()+" scripts tag(s).");
        
        //
        
        return anchorlist;        
    }   
    
    public String parselinkforhrefvalue(String linktag)
    {
        Matcher matcher = Pattern.compile("(\\href=\"(.*?)\")").matcher(linktag); //parse <img src=""></img> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();
            
            //if(match.startsWith("src")) continue;
            
            //System.err.println("img src tag has value: "+match);           
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("href=\"", "").replace("\"", "");                
        
        return match;       
    }  
    
    public String parseimageforsrcvalue(String imagetag)
    {
        Matcher matcher = Pattern.compile("(\\bsrc=\"(.*?)\")").matcher(imagetag); //parse <img src=""></img> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();
            
            //if(match.startsWith("src")) continue;
            
            //System.err.println("img src tag has value: "+match);           
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("src=\"", "").replace("\"", "");                
        
        return match;       
    }  
    
    public String parseanchorforhrefvalue(String hreftag)
    {
        Matcher matcher = Pattern.compile("(\\bhref=\"(.*?)\")").matcher(hreftag); //parse <img src=""></img> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();
            
            //if(match.startsWith("src")) continue;
            
            //System.err.println("img src tag has value: "+match);           
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("href=\"", "").replace("\"", "");                
        
        return match;       
    }    
    
    public String parsescriptforsrcvalue(String scripttag)
    {                                       
        Matcher matcher = Pattern.compile("(\\bsrc=\"(.*?)\")").matcher(scripttag); //parse <img src=""></img> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();
            
            //if(match.startsWith("src")) continue;
            
            //System.err.println("script src tag has value: "+match);           
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("src=\"", "").replace("\"", "");                
        
        return match;     
    }    
    
    public void persistimage(WebcrawlerParam param, String inputURL, String outputURL) throws Exception 
    {                
        if(inputURL.startsWith("//") || inputURL.startsWith("/") || inputURL.startsWith("./"))
        {
            //move to absolute case for simplification
            
            if(inputURL.startsWith("//"))
            {
                inputURL = "https:"+inputURL;
            }
            
            if(inputURL.startsWith("/"))
            {
                inputURL = param.baseURL + inputURL;
            }
            
            if(inputURL.startsWith("./"))
            {
                inputURL = param.baseURL + inputURL;
            }                        
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.baseURL + "/" + inputURL;
        }
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");
                
        //
        
        String filename = inputURL.substring(inputURL.lastIndexOf("/")+1);
        
        //System.out.println("FULL URL: "+inputURL);
        
        //System.out.println("FILENAME TO PERSIST: "+filename);
       
        //
       
        InputStream is = null;
        
        OutputStream os = null;
        
        try
        {
            URL url = new URL(inputURL);
        
            is = url.openStream();
            
            os = new FileOutputStream(outputURL+"/"+filename);
                    
            byte[] b = new byte[1024*1024*50]; //file size up to 50 MB

            int length;

            while ((length = is.read(b)) != -1) 
            {
                os.write(b, 0, length);
            }

            is.close();

            os.close();

            //

            b = null;        
            
            is = null;
            
            os = null;
        }
        catch(Exception e)
        {
            //
            
        }        	
        
        //
        
        System.gc();
    }  
    
    public void persistfile(WebcrawlerParam param, String inputURL, String outputURL) throws Exception 
    {               
        if(inputURL==null || inputURL.isEmpty())
        {
            return;
        }
        else if(inputURL.startsWith("//") || inputURL.startsWith("/") || inputURL.startsWith("./"))
        {
            //move to absolute case for simplification
            
            if(inputURL.startsWith("//"))
            {
                inputURL = "https:"+inputURL;
            }
            
            if(inputURL.startsWith("/"))
            {
                inputURL = param.baseURL + inputURL;
            }
            
            if(inputURL.startsWith("./"))
            {
                inputURL = param.baseURL + inputURL;
            }                       
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.baseURL + "/" + inputURL;
        }        
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");
        
        //
        
        String filename = inputURL.substring(inputURL.lastIndexOf("/")+1);
        
        //System.out.println("FULL URL: "+inputURL);
        
        //System.out.println("FILENAME TO PERSIST: "+filename);

        //
        
        try
        {
        
            URL url = new URL(inputURL);

            InputStream is = url.openStream();

            OutputStream os = new FileOutputStream(outputURL+"/"+filename);

            byte[] b = new byte[1024*1024*50]; //file size up to 50 MB

            int length;

            while ((length = is.read(b)) != -1) 
            {
                os.write(b, 0, length);
            }

            is.close();

            os.close();

            //

            b = null;
            
            is = null;
            
            os = null;
        
        }
        catch(Exception e)
        {
            //
        }
        
        //
        
        System.gc();
    }     
}

class ModuleTwo implements Runnable
{    
    public void run()
    {
        //rewrite links to face inward for local reference
    }
}

class ModuleThree implements Runnable
{    
    public void run()
    {
        //compress files, delete redundant files
    }
}

class WebcrawlerParam
{
    public String siteHTML;
    
    public ArrayList<String> siteStyleSheets;
            
    public ArrayList<String> siteAnchors;
    
    public ArrayList<String> siteImages;
    
    public ArrayList<String> siteScripts;
    
    public String href;
           
    public String recurseMessage;
            
    public String persistMessage;
    
    public String baseURL;
    
    public String unqualifiedURL;
    
    public String anchor;
}