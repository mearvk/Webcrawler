package webcrawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Webcrawler implements Runnable
{
    public Registrar registrar = new Registrar();
    
    public Initializer initializer = new Initializer();   
    
    //
    
    public static Map<String, Object> values = new HashMap();
    
    public static Map<String, String> visitedsitelinks = new HashMap();
    
    public static Map<String, String> visitedresourcelinks = new HashMap();
    
    //
    
    public static final Integer LOCAL_RECURSE_DEPTH = 4;
    
    public static final Integer GLOBAL_RECURSE_DEPTH = 4;
    
    //
    
    public static final String BASEDIR = "C:\\Users\\mearv\\OneDrive\\Desktop\\Output";
            
    //
    
    public static void main(String[] args)
    {
        Webcrawler webcrawler = new Webcrawler();
                
        //
        
        webcrawler.registrar.register(Preinitialization.class);
        
        webcrawler.registrar.register(ModuleOne.class);
        
        webcrawler.registrar.register(ModuleTwo.class);
        
        webcrawler.registrar.register(ModuleThree.class);
        
        
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
    
    WorkerThread wthread_001 = new WorkerThread(this);

    WorkerThread wthread_002 = new WorkerThread(this);

    WorkerThread wthread_003 = new WorkerThread(this);

    WorkerThread wthread_004 = new WorkerThread(this);

    //
   
    public void run()
    {        
        ArrayList<String> websites = (ArrayList<String>)((Initializer)Webcrawler.values.get("initializer")).variables.get("websites");

        //
                      
        for(int i=0; i<websites.size(); i++)
        {
            WebcrawlerParam param = new WebcrawlerParam();
            
            //
            
            param.baseURL = websites.get(i);                 
            
            param.href = websites.get(i);
            
            //
            if(param==null /*|| !param.baseURL.endsWith(".org") || !param.baseURL.endsWith(".com") || !param.baseURL.endsWith(".edu")*/)
            {
                System.out.println(param.baseURL+" is unsupported or such.");
            }
            else if(i%4==0)
            {
                wthread_004.queue.offer(param);
            }
            
            else if(i%4==1)
            {
                wthread_003.queue.offer(param);                
            }
            
            else if(i%4==2)
            {
                wthread_002.queue.offer(param);                
            }
            
            else if(i%4==3)
            {
                wthread_001.queue.offer(param);                
            }
        }
        
        //
        
        wthread_001.start();

        wthread_002.start();

        wthread_003.start();

        wthread_004.start();
    }
    
    public ArrayList<String> doparseanchors(WebcrawlerParam param) throws Exception
    {
        ArrayList<String> anchorlist = new ArrayList();
        
        //
        
        if(param==null || param.siteHTML==null) throw new Exception("Site HTML was not ready for site: "+param.baseURL);
        
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
            
            retval = retval.replace("https", "");           

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");
        }
        else //full path in HREF link
        {                  
            retval = param.href;
            
            retval = retval.replace("https", "");                       

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");
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
                        
            href = match;
        }              
        
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
    
    public String dorequestwithnopersistence(WebcrawlerParam param) throws Exception
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
            
        connection.setReadTimeout(2000);
            
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
                    
        //
        
        if(connection==null) return null;
        
        //
            
        StringBuilder builder = new StringBuilder();
        
        try
        {
            int responsecode = connection.getResponseCode();
            
            //
            
            if(responsecode!=HttpURLConnection.HTTP_OK)
            {
                if(responsecode == HttpURLConnection.HTTP_MOVED_TEMP || responsecode == HttpURLConnection.HTTP_MOVED_PERM || responsecode == HttpURLConnection.HTTP_SEE_OTHER)
                {                
                    String newurl = param.href = connection.getHeaderField("location");

                    String cookies = connection.getHeaderField("set-cookie"); 
                    
                    //
                    
                    connection = (HttpURLConnection) new URL(newurl).openConnection();
                    
                    connection.setRequestProperty("cookies", cookies);
                    
                    responsecode = connection.getResponseCode();     
                    
                    //
                    
                    if(responsecode!=HttpURLConnection.HTTP_OK)
                    {
                        System.out.println("Request for website ["+param.baseURL+"] failed with fatal code: "+responsecode);
                    }
                }
            }            
                        
            //           

            String string=null;

            //

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while((string=reader.readLine())!=null)
            {
                builder.append(string);
            }
            
            //

            param.siteHTML=builder.toString();
        }
        catch(FileNotFoundException fnfe)
        {
            //404 returned
        }
        catch(Exception e)
        {
            //
        }
        finally
        {
            System.gc();
        }

        if(builder==null) return null; 
        
        return builder.toString();       
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
            
        connection.setReadTimeout(10000);
            
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
                    
        //
        
        if(connection==null) return null;
            
        //
        
        StringBuilder builder = new StringBuilder();;
        
        // 
        
        try
        {
            int responsecode = connection.getResponseCode();
                        
            //
            
            if(responsecode!=HttpURLConnection.HTTP_OK)
            {
                if(responsecode == HttpURLConnection.HTTP_MOVED_TEMP || responsecode == HttpURLConnection.HTTP_MOVED_PERM || responsecode == HttpURLConnection.HTTP_SEE_OTHER)
                {                
                    String newurl = param.href = connection.getHeaderField("location");

                    String cookies = connection.getHeaderField("set-cookie"); 
                    
                    //
                    
                    connection = (HttpURLConnection) new URL(newurl).openConnection();
                    
                    connection.setRequestProperty("cookies", cookies);
                    
                    responsecode = connection.getResponseCode();     
                    
                    //                    
                    
                    if(responsecode!=HttpURLConnection.HTTP_OK)
                    {
                        System.out.println("Request for website ["+param.baseURL+"] failed with fatal code: "+responsecode);
                    }                    
                }
            }
     
            String string=null;

            //

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while((string=reader.readLine())!=null)
            {
                builder.append(string);
            }

            //

            param.siteHTML=builder.toString();

            param.unqualifiedURL = this.dodeterminefullpathforpersist(param);                

            //

            this.dopersist(param);

            //

            //return builder.toString();                      
        }
        catch(ConnectException ce)
        {
            //System.err.println("Resource \""+param.href+"\" could not be connected to; HTTP call fails.");
        }
        catch(FileNotFoundException fnfe)
        {
            //System.err.println("Resource "+param.href+" exists as a link but not actually a page reference. HTTP returned 404.");
        }
        catch(Exception e)
        {
            //
        }
        finally
        {            
            System.gc();            
        }
                       
        if(builder==null) throw new Exception("Unable to retrieve HTML for site: "+param.baseURL);
        
        return builder.toString();  
    }

    /**
     * Limit recursion to single HTTP based site, e.g. abc.google.com & google.com & www.google.com based on <a></a> tags
     *
     * @param param
     * @param depth
     * @return
     */
    public String dosinglesiterecurse(WebcrawlerParam param, Integer depth)
    {
        //
        
        ArrayList<String> anchors = param.siteAnchors;
        
        ArrayList<String> errors = null;
                                           
        //
            
        if(anchors==null || anchors.isEmpty())
        {
            System.out.println("Site "+param.baseURL+" reports no <a> links of any kind.");
            
            return null;
        }
        else
        {
            System.out.println("Site "+param.baseURL+" reports "+anchors.size()+" <a> links.");
        }
               
        //
        
        if(depth==Webcrawler.LOCAL_RECURSE_DEPTH)
        {
            //System.out.println("Recursive depth is "+depth+"; we expect no more traversals.");
        }
        else
        {
            //System.out.println("Recursive depth is "+depth+"; we expect "+(Webcrawler.LOCAL_RECURSE_DEPTH-depth)+" more traversals.");
        }
        
        //        
        
        for(int i=0; i<anchors.size(); i++)
        {                        
            WebcrawlerParam recursiveparam = new WebcrawlerParam();
            
            String anchor = anchors.get(i);
            
            //
            
            //System.out.println("Anchor #"+i+", \""+anchor+"\", for site "+param.baseURL+" is being run recursively.");           
                    
            //
            
            if(anchor==null || anchor.isEmpty()) continue;
            
            String href_quickref;
            
            href_quickref = this.parseanchorforhrefvalue(anchor).trim();
            
            if(href_quickref.endsWith("/") || href_quickref.endsWith("/\""))
            {            
                href_quickref = href_quickref.substring(href_quickref.length()-1);
            }
            
            if( href_quickref==null || !(href_quickref.startsWith("https://www."+param.baseURL) || href_quickref.startsWith("http://wwww."+param.baseURL) || href_quickref.startsWith("http://"+param.baseURL) || href_quickref.startsWith("https://"+param.baseURL) || href_quickref.startsWith(param.baseURL) || href_quickref.startsWith("/") || href_quickref.startsWith("./") || href_quickref.startsWith("//") || href_quickref.startsWith("..") || href_quickref.startsWith("#") || href_quickref.startsWith("?")) )
            {                 
                //also check xxx.yyy.root.com please before failing
                
                boolean match = false;
                
                Matcher matcher = Pattern.compile("([\\w]+\\.)*"+param.baseURL).matcher(href_quickref); //will match store.google.com  go.stop.google.com etc.

                //

                match = matcher.find();

                //
                
                if(!match)
                
                System.out.println("Anchor #"+i+", \""+anchor+"\", for "+param.baseURL+" is being skipped for being unintelligible or not properly a member site of "+param.baseURL);     
                
                continue;
            }
            else
            {
                // intelligible but possibly not local site i.e. wellsfargoadvisors.com off of wellsfargo.com
            }
            
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
                
                if(Webcrawler.visitedsitelinks.get(recursiveparam.href)==null || Webcrawler.visitedsitelinks.get(recursiveparam.href).isEmpty())
                {                
                    Webcrawler.visitedsitelinks.put(recursiveparam.href, "visited");
                    
                    this.dopersistsiteurlasvisited(recursiveparam.href);                    
                    
                    System.out.println("Local recursion has visited "+Webcrawler.visitedsitelinks.size()+" sites.");
                }                
                else continue;
                
                //
                                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.anchor = anchor;                

                //
                
                //System.out.println("ModuleOne:dosinglesiterecurse has href value: "+recursiveparam.href);
                
                //System.out.println("ModuleOne:dosinglesiterecurse has baseURL value: "+recursiveparam.baseURL);

                //
                
                if(depth>=Webcrawler.LOCAL_RECURSE_DEPTH)
                {
                    throw new StackDepthException("Local stack depth exceeded; returning.");
                }
                               
                recursiveparam.siteHTML = this.dorequest(recursiveparam);           
                
                recursiveparam.siteAnchors = this.doparseanchors(recursiveparam);
                
                recursiveparam.recurseMessage = this.dosinglesiterecurse(recursiveparam, (1+depth));
            }
            catch(NullPointerException npe)
            {
                //
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
            }
        }

        //
        
        System.gc();
        
        //
            
        return "success";                
    }

    /**
     * Do not limit recursion to single HTTP based site, e.g. abc.google.com & google.com & www.google.com based on <a></a> tags but rather accept and recurse all.
     *
     * @param param
     * @param depth
     * @return
     * @throws Exception
     */
    public String dorecurse(WebcrawlerParam param, Integer depth) throws Exception
    {       
        ArrayList<String> anchors = param.siteAnchors;
        
        ArrayList<String> errors = null;
                                           
        //
            
        if(anchors==null || anchors.isEmpty())
        {
            System.out.println("Site "+param.baseURL+" had no links of any kind!");
            
            return null;
        }
        else
        {
            for(int i=0; i<anchors.size(); i++)
            {
                String href = this.parseanchorforhrefvalue(anchors.get(i));                
            }
        }
               
        //        
        
        for(int i=0; i<anchors.size(); i++)
        {
            WebcrawlerParam recursiveparam = new WebcrawlerParam();
            
            String anchor = anchors.get(i);
                              
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
                
                if(Webcrawler.visitedsitelinks.get(recursiveparam.href)==null || Webcrawler.visitedsitelinks.get(recursiveparam.href).isEmpty())
                {                
                    Webcrawler.visitedsitelinks.put(recursiveparam.href, "visited");
                    
                    this.dopersistsiteurlasvisited(recursiveparam.href);
                    
                    System.out.println("Global recursion has visited "+Webcrawler.visitedsitelinks.size()+" sites.");
                    
                }                
                else continue;
                
                //
                                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.anchor = anchor;                

                //
                
                System.out.println("ModuleOne:dorecurse has href value: "+recursiveparam.href);
                
                System.out.println("ModuleOne:dorecurse has baseURL value: "+recursiveparam.baseURL);

                //
                
                if(depth>=Webcrawler.GLOBAL_RECURSE_DEPTH)
                {
                    throw new StackDepthException("Global stack depth exceeded; returning.");
                }

                //

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
        
        String scriptfileref = "/home/oem/Desktop/Webpages/storage/"+smonth+"-"+sday+"-"+syear+"/"+param.unqualifiedURL+"/javascript/";
        
        String cssfileref = "/home/oem/Desktop/Webpages/storage/"+smonth+"-"+sday+"-"+syear+"/"+param.unqualifiedURL+"/css/";
        
        
        //
               
        //System.out.println("-- -- -- -- --");
        
        //System.out.println("ModuleOne:dopersist has href value: "+param.href);
                
        //System.out.println("ModuleOne:dopersist has baseURL value: "+param.baseURL);       
        
        //System.out.println("ModuleOne:dopersist has unqualified value: "+param.unqualifiedURL); 
        
        //System.out.println("-- -- -- -- --");
    
        try
        {
            File dir = new File(fileref);
            
            if(!dir.exists())dir.mkdirs();
            
            //
            
            File file = new File(fileref);
            
            if(file.exists()) file.delete(); //no dupes 
            
            //
            
            File imagedir = new File(imagefileref);
            
            if(!imagedir.exists()) imagedir.mkdirs();   
                       
            //
            
            File scriptdir = new File(scriptfileref);
            
            if(!scriptdir.exists()) scriptdir.mkdirs();

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
                        this.persistfile(param, this.parsescriptforsrcvalue(param.siteScripts.get(i)), scriptfileref);
                    }
                    catch(Exception e)
                    {
                        //
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
        ArrayList<String> linklist = new ArrayList();
        
        String originalsiteHTML = new StringBuffer(param.siteHTML).toString();
        
        //
                       
        Matcher matcher = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.siteHTML); 
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();
            
            //;
            
            linklist.add(match);
        }      
        
        //
        
        WebcrawlerParam recursiveparam = null;
        
        //
        
        for(int i=0; i<linklist.size(); i++)
        {
            String linktag = linklist.get(i);
            
            String href = null;
            
            //
            
            recursiveparam = new WebcrawlerParam();
            
            recursiveparam.href = this.parselinkforhrefvalue(linktag);            
            
            //
            
            if(recursiveparam.href.startsWith("/") || recursiveparam.href.startsWith("./"))
            {
                recursiveparam.href = param.baseURL + recursiveparam.href;
            }
            
            if(recursiveparam.href.endsWith("/"))
            {
                recursiveparam.href = recursiveparam.href.substring(0,recursiveparam.href.length()-1);
            }
            
            if(param.baseURL.endsWith("/")) 
            {
                param.baseURL = param.baseURL.substring(0,param.baseURL.length()-1);
            }
            
            if( !(recursiveparam.href.trim().equals(param.baseURL.trim()) || recursiveparam.href.replace("www.","").trim().equals(param.baseURL.replace("www.","").trim())) && (Webcrawler.visitedresourcelinks.get(recursiveparam.href)==null || Webcrawler.visitedresourcelinks.size()==0) )
            {
                //
                
                Webcrawler.visitedresourcelinks.put(recursiveparam.href, "visited");
            }
            else 
            {
                //no infinite looping
                
                continue;
            } 
            
            //
            
            if(recursiveparam.href!=null && recursiveparam.href.trim().endsWith(".css")) 
            {
                continue; //
            }
            else //nested <link> references we care for here
            {            
                try
                {
                    this.dorequestwithnopersistence(recursiveparam);

                    //

                    if(recursiveparam.siteHTML==null) continue;

                    //

                    matcher = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(recursiveparam.siteHTML); //parse <img src=""></img> matches for now..

                    while(matcher.find())
                    {
                        String match = matcher.group();

                        //

                        linklist.add(match);
                    }                   
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }   
            }
        }
        
        //
        
        linklist = new ArrayList(new HashSet(linklist));

        //
        
        System.out.println("Site "+param.href+", at two levels of recursion, had "+linklist.size()+" <link> tag(s).");
         
        //
        
        for(int i=0; i<linklist.size(); i++)
        {
            String linktag = linklist.get(i);
            
            String href_quickref = this.parselinkforhrefvalue(linktag);
            
            String rel_quickref = this.parselinkforrelattr(linktag);
            
            String type_quickref = this.parselinkfortypeattr(linktag);

            //

            if(href_quickref==null) href_quickref = "";

            if(rel_quickref==null) rel_quickref = "";

            if(type_quickref==null) type_quickref = "";

            //
            
            if( ! (href_quickref.trim().endsWith(".css") || rel_quickref.contains("stylesheet") || type_quickref.contains("text/css")) )
            {
                linklist.remove(i);
            }
        }        
                               
        //
        
        param.siteStyleSheets = linklist;
        
        param.siteHTML = originalsiteHTML;
        
        //        
        
        return linklist;        
    }     
    
    public ArrayList<String> parsesiteimages(WebcrawlerParam param)
    {
        ArrayList<String> imagelist = new ArrayList();
        
        //
                       
        Matcher matcher = Pattern.compile("<img\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.siteHTML); 
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();

            imagelist.add(match);
        }
        
        //
        
        param.siteImages = imagelist;
        
        //
        
        return imagelist;
    } 
    
    public ArrayList<String> parsesitescripts(WebcrawlerParam param)
    {
        ArrayList<String> scriptlist = new ArrayList();
        
        //
                       
        Matcher matcher = Pattern.compile("<script\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.siteHTML);
        
        //
        
        while(matcher.find())
        {
            String match = matcher.group();
                       
            scriptlist.add(match);
        }
        
        //
                       
        Matcher matcher_link_tags = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.siteHTML); 
        
        //
        
        while(matcher_link_tags.find())
        {
            String match = matcher_link_tags.group();
                       
            String href = this.parselinkforhrefvalue(match);

            //

            if(href==null) href = "";

            //
            
            if(href.trim().endsWith(".js") || href.trim().endsWith(".js\"") || href.trim().endsWith(".vbs") || href.trim().endsWith(".vbs\""))
            {            
                scriptlist.add(match);
            }
        }        
        
        //
        
        param.siteScripts = scriptlist;
        
        //
        
        return scriptlist;
    }   
    
    public String parselinkforhrefvalue(String linktag)
    {
        Matcher matcher = Pattern.compile("(href=\"(.*?)\")").matcher(linktag); //parse <link src=""></link> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();        
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("href=\"", "").replace("\"", "");                
        
        return match;       
    }  
    
    public String parseimageforsrcvalue(String linktag)
    {
        Matcher matcher = Pattern.compile("(src=\"(.*?)\")").matcher(linktag); //parse <link src=""></link> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();        
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("src=\"", "").replace("\"", "");                
        
        return match;       
    }    
    
    public String parselinkforrelattr(String imagetag)
    {
        Matcher matcher = Pattern.compile("(\\brel=\"(.*?)\")").matcher(imagetag); //parse <img src=""></img> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();     
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("rel=\"", "").replace("\"", "");                
        
        return match;       
    }  
    
    public String parselinkfortypeattr(String imagetag)
    {
        Matcher matcher = Pattern.compile("(\\btype=\"(.*?)\")").matcher(imagetag); //parse <img src=""></img> matches for now..
        
        //
        
        String match=null;
        
        while(matcher.find())
        {
            match = matcher.group();     
        }
        
        //
        
        if(match==null) return null;
        
        match = match.replace("type=\"", "").replace("\"", "");                
        
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
            //
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.baseURL + "/" + inputURL;
        }        
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");
        
        //
        
        String filename = inputURL.substring(inputURL.lastIndexOf("/")+1);

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
    
    public String dopersistsiteurlasvisited(String url)
    {
        Date date = new Date();        
        
        Integer month = date.getMonth()+1;
        
        Integer day = date.getDate();
        
        Integer year = date.getYear();  
        
        String time = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
        
        //
        
        DecimalFormat monthformat = new DecimalFormat("##");
        
        DecimalFormat yearformat = new DecimalFormat("##");
        
        DecimalFormat dayformat = new DecimalFormat("##");               
        
        //
        
        String smonth = monthformat.format(month);
        
        String syear = "20"+yearformat.format(year).substring(1);
        
        String sday = dayformat.format(day);        
        
        //
        
        File visitedsitesdir = new File(Webcrawler.BASEDIR+"/"+smonth+"-"+sday+"-"+syear+"/meta/");
        
        File visitedsitesfile = new File(Webcrawler.BASEDIR+"/"+smonth+"-"+sday+"-"+syear+"/meta/visited.csv");
        
        //
        
        if(!visitedsitesdir.exists())
        {
            visitedsitesdir.mkdirs();
        }
                              
        //
        
        try
        {                        
            BufferedReader reader = new BufferedReader(new FileReader(visitedsitesfile));            
            
            // https;//google.com, 02-12-2018, 13:37 EST

            String sitename = null;
            
            String line = null;
            
            String[] tokens = null;
            
            while((line=reader.readLine())!=null)
            {
                tokens = line.split(",");
                
                if(tokens==null) continue;
                
                sitename = tokens[0];    
                
                //
                
                sitename = sitename.trim();
                
                url = url.trim();
                
                //
                
                while(sitename.endsWith("/"))
                {
                    sitename = sitename.substring(sitename.length()-1);
                }
                
                while(url.endsWith("/"))
                {
                    url = url.substring(url.length()-1);
                }                

                if(sitename.equals(url))
                {                    
                    reader.close();
                    
                    reader = null;
                    
                    return "preexisting";
                }
            }
            
            //

            reader.close();
                    
            reader = null;            
            
            //
            
            BufferedWriter writer = new BufferedWriter(new FileWriter(visitedsitesfile, true));          
            
            writer.write(url+","+date+","+time+"\n");
            
            writer.flush();
            
            writer.close();
            
            writer = null;
            
            //
        }
        catch(Exception e)
        {
            //
            
        }
        
        return "success";
    }
}

/**
 * Rewrite links for local, offline browsing.
 * 
 * @author Max Rupplin
 */
class ModuleTwo implements Runnable
{    
    public void run()
    {
        //todo
    }
}

/**
 * Compress files, delete redundant files
 * 
 * @author Max Rupplin
 */
class ModuleThree implements Runnable
{    
    public void run()
    {
        //todo
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

class WorkerThread extends Thread
{
    public WorkerThread(ModuleOne runner)
    {
        this.runner = runner;
    }
    
    public Queue<WebcrawlerParam> queue = new ArrayBlockingQueue(100);
    
    public Boolean running = true;
    
    public ModuleOne runner = null;
    
    public void run()
    {
        while(running)
        {
            if(queue.isEmpty())
            {
                try
                {
                    this.wait(1000L);
                }
                catch(Exception e)
                {
                    //
                }
            }
            else
            {            
                try
                {

                    WebcrawlerParam param = queue.poll();

                    //

                    String websiteURL = param.baseURL;

                    //

                    param.baseURL = websiteURL;

                    param.href = websiteURL;

                    param.siteHTML = runner.dorequest(param);

                    param.siteAnchors = runner.doparseanchors(param);

                    param.recurseMessage = runner.dosinglesiterecurse(param, 0);   
                    
                    //
                }
                catch(Exception e)
                {
                    //
                }
            }
        }
    }
}