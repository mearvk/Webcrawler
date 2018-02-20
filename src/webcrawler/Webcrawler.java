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
import java.net.SocketTimeoutException;
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
    
    public static final Integer LOCAL_RECURSE_DEPTH = 2;
    
    public static final Integer GLOBAL_RECURSE_DEPTH = 4;
    
    //
    
    public static final String BASEDIR = Utils.dofileseparatornormalization("C:\\Users\\mearv\\OneDrive\\Desktop\\Websites\\storage");
            
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
    
    WorkerThread wthread_001 = new WorkerThread(this,"thread_01");

    WorkerThread wthread_002 = new WorkerThread(this,"thread_02");

    WorkerThread wthread_003 = new WorkerThread(this,"thread_03");

    WorkerThread wthread_004 = new WorkerThread(this,"thread_04");

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
            if(param==null)
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

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
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

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
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

            retval = retval.replace("http", "");

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");
        }
        else //full path in HREF link
        {                  
            retval = param.href;
            
            retval = retval.replace("https", "");

            retval = retval.replace("http", "");

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");
        }
        
        return retval;
    }

    /**
     *
     * @param param
     * @param inputURL
     * @return
     * @throws Exception
     */
    public String dodeterminefullpathforhttpreference(WebcrawlerParam param, String inputURL) throws Exception
    {
        if(inputURL==null || inputURL.isEmpty()) throw new Exception();
        
        //
        
        if(inputURL.startsWith("//") || inputURL.startsWith("/") || inputURL.startsWith("./"))
        {
            //move to absolute case for simplification
            
            if(inputURL.startsWith("//"))
            {
                inputURL = "https:"+inputURL;
            }
            else if(inputURL.startsWith("/"))
            {
                inputURL = param.baseURL + inputURL;
            }
            else if(inputURL.startsWith("./"))
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

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public String dorequestwithnopersistence(WebcrawlerParam param) throws Exception
    {
        String threadname = Thread.currentThread().getName();

        //

        URL url=null;
        
        HttpURLConnection connection=null;
        
        //
        
        url = new URL(param.href);            
        
        //
        
        if(url==null) return null;
            
        //
            
        connection = (HttpURLConnection)url.openConnection();
            
        connection.setRequestMethod("GET");

        connection.setConnectTimeout(30000);
            
        connection.setReadTimeout(10000);
            
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
                        System.out.println("    >> Thread \""+threadname+"\" :: request for website ["+param.href+"] failed with fatal code: "+responsecode);
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
        catch(SocketTimeoutException stoe)
        {
            System.out.println("    >> "+stoe.getMessage()+" for "+param.href);
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

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public String dorequest(WebcrawlerParam param) throws Exception
    {
        String threadname = Thread.currentThread().getName();

        //

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

        connection.setConnectTimeout(30000);
            
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
                        System.out.println("    >> Thread \""+threadname+"\" :: request for website ["+param.href+"] failed with fatal code: "+responsecode);
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
        catch(SocketTimeoutException stoe)
        {
            System.out.println("    >> "+stoe.getMessage()+" for "+param.href);
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
            //e.printStackTrace();
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
        String threadname = Thread.currentThread().getName();

        //
        
        ArrayList<String> anchors = new ArrayList(new HashSet(param.siteAnchors));
        
        ArrayList<String> errors = null;
                                           
        //
            
        if(anchors==null || anchors.isEmpty())
        {
            System.out.println("    >> Thread \""+threadname+"\" :: "+param.baseURL+" reports no <a> links of any kind.");
            
            return null;
        }
        else
        {
            //System.out.println("    >> Thread \""+threadname+"\" :: "+param.baseURL+" reports "+anchors.size()+" <a> links.");
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

            //
            
            String href_quickref;

            String baseurl_quickref;

            //

            baseurl_quickref = new StringBuffer(param.baseURL).toString();

            //

            if(baseurl_quickref.startsWith("https://"))
            {
                baseurl_quickref = baseurl_quickref.substring(8);
            }
            else if(baseurl_quickref.startsWith("http://"))
            {
                baseurl_quickref = baseurl_quickref.substring(7);
            }

            //
            
            href_quickref = this.parseanchorforhrefattributevalue(anchor);

            //

            if(href_quickref==null) continue;

            //
            
            if(href_quickref.endsWith("/"))
            {            
                href_quickref = href_quickref.substring(href_quickref.length()-1);
            }

            //

            if( !(href_quickref.startsWith("https://www."+baseurl_quickref) || href_quickref.startsWith("http://wwww."+baseurl_quickref) || href_quickref.startsWith("http://"+baseurl_quickref) || href_quickref.startsWith("https://"+baseurl_quickref) || href_quickref.startsWith(baseurl_quickref) || href_quickref.startsWith("/") || href_quickref.startsWith("./") || href_quickref.startsWith("//") || href_quickref.startsWith("..") || href_quickref.startsWith("#") || href_quickref.startsWith("?") || href_quickref.endsWith(".html")) )
            {                 
                //also check xxx.yyy.root.com please before failing
                
                boolean match = false;

                //

                if(href_quickref.startsWith("https://"))
                {
                    href_quickref = href_quickref.substring(8);
                }
                else if(href_quickref.startsWith("http://"))
                {
                    href_quickref = href_quickref.substring(7);
                }

                //

                if(baseurl_quickref.startsWith("https://"))
                {
                    baseurl_quickref = baseurl_quickref.substring(8);
                }
                else if(baseurl_quickref.startsWith("http://"))
                {
                    baseurl_quickref = baseurl_quickref.substring(7);
                }

                //
                
                Matcher matcher = Pattern.compile("([\\w]+\\.)*"+baseurl_quickref).matcher(href_quickref); //will match store.google.com  go.stop.google.com etc.

                //

                match = matcher.find();

                //
                
                if(!match)
                {
                    System.out.println("    >> Thread \""+threadname+"\" :: anchor #" + i + ", \"" + this.parseanchorforhrefattributevalue(anchor) + "\", for " + param.baseURL + " is being skipped for being unintelligible or not properly a member site of " + param.baseURL);

                    continue;
                }
            }
            else
            {
                // intelligible but possibly not local site i.e. wellsfargoadvisors.com off of wellsfargo.com

                if(Webcrawler.visitedsitelinks.get(this.parseanchorforhrefattributevalue(anchor))==null || Webcrawler.visitedsitelinks.get(this.parseanchorforhrefattributevalue(anchor)).isEmpty())
                {
                    //System.out.println("    >> Thread \"" + threadname + "\" :: anchor #" + i + ", \"" + this.parseanchorforhrefattributevalue(anchor) + "\", for " + param.baseURL + " is being included for single site recursion.");
                }
            }
            
            //
                
            try
            {                                                               
                //
                
                recursiveparam.baseURL = param.baseURL;
                
                recursiveparam.href = this.parseanchorforhrefattributevalue(anchor);
                
                if(recursiveparam.href==null || recursiveparam.href.isEmpty()) continue;
                
                recursiveparam.href = this.dodeterminefullpathforhttpreference(param, recursiveparam.href);
                
                //
                
                if(recursiveparam.href==null) continue;
                
                if(!recursiveparam.href.contains("http")) continue;                                        
                
                //
                
                if(Webcrawler.visitedsitelinks.get(recursiveparam.href)==null || Webcrawler.visitedsitelinks.get(recursiveparam.href).isEmpty())
                {                
                    Webcrawler.visitedsitelinks.put(recursiveparam.href, "visited");
                    
                    //this.dopersistsiteurlasvisited(recursiveparam.href);
                    
                    System.out.println("Local recursion has visited and downloaded images, css and HTML from "+Webcrawler.visitedsitelinks.size()+" site(s) and/or link(s).");
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
                String href = this.parseanchorforhrefattributevalue(anchors.get(i));
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
                
                recursiveparam.href = this.parseanchorforhrefattributevalue(anchor);
                
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
                    
                    System.out.println("Global recursion has visited "+Webcrawler.visitedsitelinks.size()+" site(s).");
                    
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

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
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

        String file_separator = System.getProperty("file.separator");

        //

        String dirref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL)+"\\");

        String fileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+Utils.dofileseparatornormalization("\\"+"index.html");
        
        String imagefileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+Utils.dofileseparatornormalization("\\"+"images");
        
        String scriptfileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+Utils.dofileseparatornormalization("\\"+"javascript");
        
        String cssfileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+Utils.dofileseparatornormalization("\\"+"css");

        //
    
        try
        {
            File dir = new File(dirref);
            
            if(!dir.exists()) dir.mkdirs();

            //

            File file = new File(dir.getAbsolutePath(), "index.html");

            if(file.exists()) file.delete();

            file.createNewFile();

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
                        this.persistimage(param, this.parseimageforsrcattributevalue(param.siteImages.get(i)), imagefileref);
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
                        this.persistfile(param, this.parselinkforhrefattributevalue(param.siteStyleSheets.get(i)), cssfileref);
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
                        this.persistfile(param, this.parsescriptforsrcattributevalue(param.siteScripts.get(i)), scriptfileref);
                    }
                    catch(Exception e)
                    {
                        //
                    }
                }        
            }

            //

            FileWriter writer = new FileWriter(file);
            
            writer.write(param.siteHTML);
            
            writer.flush();
            
            writer.close();
        }
        catch(Exception e)
        {
            System.err.println(fileref);

            e.printStackTrace();

            //return e.getMessage();
        }
        finally
        {
            System.gc();
        }
        
        return "success";
    }

    /**
     *
     * @param param
     * @return
     */
    public ArrayList<String> parsesitecss(WebcrawlerParam param)
    {
        String threadname = Thread.currentThread().getName();

        //

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
            
            recursiveparam.href = this.parselinkforhrefattributevalue(linktag);
            
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
        
        //System.out.println("    >> Thread \""+threadname+"\" :: "+param.href+", at two levels of recursion, had "+linklist.size()+" <link> tag(s).");
         
        //
        
        for(int i=0; i<linklist.size(); i++)
        {
            String linktag = linklist.get(i);
            
            String href_quickref = this.parselinkforhrefattributevalue(linktag);
            
            String rel_quickref = this.parselinkforrelattributevalue(linktag);
            
            String type_quickref = this.parselinkfortypeattributevalue(linktag);

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

    /**
     *
     * @param param
     * @return
     */
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

    /**
     *
     * @param param
     * @return
     */
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
                       
            String href = this.parselinkforhrefattributevalue(match);

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

    /**
     *
     * @param linktag
     * @return
     */
    public String parselinkforhrefattributevalue(String linktag)
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

    /**
     *
     * @param linktag
     * @return
     */
    public String parseimageforsrcattributevalue(String linktag)
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

    /**
     *
     * @param imagetag
     * @return
     */
    public String parselinkforrelattributevalue(String imagetag)
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

    /**
     *
     * @param imagetag
     * @return
     */
    public String parselinkfortypeattributevalue(String imagetag)
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

    /**
     *
     * @param hreftag
     * @return
     */
    public String parseanchorforhrefattributevalue(String hreftag)
    {
        //System.out.println("HREFTAG: "+hreftag);

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

    /**
     *
     * @param scripttag
     * @return
     */
    public String parsescriptforsrcattributevalue(String scripttag)
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

    /**
     *
     * @param param
     * @param inputURL
     * @param outputURL
     * @throws Exception
     */
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

    /**
     *
     * @param param
     * @param inputURL
     * @param outputURL
     * @throws Exception
     */
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

    /**
     *
     * @param url
     * @return
     */
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
        
        File visitedsitesdir = new File(Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\"+"meta"));
        
        File visitedsitesfile = new File(Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\"+"meta"+"\\"+"visited.csv"));
        
        //
        
        if(!visitedsitesdir.exists())
        {
            visitedsitesdir.mkdirs();
        }
                              
        //
        
        try
        {
            if(!visitedsitesfile.exists()) throw new Exception(">>Index file ["+visitedsitesfile.getName()+"] is missing.");

            BufferedReader reader = new BufferedReader(new FileReader(visitedsitesfile));

            // https;//google.com, 02-12-2018, 13:37 EST

            String sitename = null;

            String line = null;

            String[] tokens = null;

            while ((line = reader.readLine()) != null)
            {
                tokens = line.split(",");

                if (tokens == null) continue;

                sitename = tokens[0];

                //

                sitename = sitename.trim();

                url = url.trim();

                //

                while (sitename.endsWith("/")) {
                    sitename = sitename.substring(sitename.length() - 1);
                }

                while (url.endsWith("/")) {
                    url = url.substring(url.length() - 1);
                }

                if (sitename.equals(url)) {
                    reader.close();

                    reader = null;

                    return "preexisting";
                }
            }

            //

            reader.close();

            reader = null;
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());    //for now let's listen
        }

        try
        {
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
            System.err.println(e.getMessage());    //for now let's listen
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

/**
 *
 */
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

/**
 *
 */
class WorkerThread extends Thread
{
    public Queue<WebcrawlerParam> queue = new ArrayBlockingQueue(100);
    
    public Boolean running = true;
    
    public ModuleOne runner = null;

    //

    public WorkerThread(ModuleOne runner, String threadname)
    {
        this.runner = runner;

        this.setName(threadname);
    }

    public WorkerThread(ModuleOne runner)
    {
        this.runner = runner;
    }

    public WorkerThread()
    {
        //
    }

    public WorkerThread(String threadname)
    {
        super(threadname);
    }

    //
    
    public void run()
    {
        while(running)
        {
            if(queue.isEmpty())
            {
                try
                {
                    this.wait(500L);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
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

                    System.out.println("Thread \""+this.getName()+"\" now working with site: "+param.baseURL);

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
                    e.printStackTrace();
                }
            }
        }
    }
}

class Utils
{
    public final static String OS_SENSITIVE_SLASH = System.getProperty("file.separator");

    /**
     * Please send here to clear file strings (C:/programs/data/text.xml) against particular Operating System file separator implementations (System.getProperty("file.separator");.
     *
     * @param basedir
     * @return
     */
    public static String dofileseparatornormalization(String basedir)
    {
        String retval = "";

        if(System.getProperty("os.name").contains("indows"))
        {
            retval = basedir.replace("/", Utils.OS_SENSITIVE_SLASH);
        }
        else
        {
            retval = basedir.replace("\\", Utils.OS_SENSITIVE_SLASH);
        }

        return retval;
    }

    /**
     * Please send only HTTP URL portion here for escapement into Windows safe file string(s); will removed unsafe characters (<, >, |, ", *, etc.)
     *
     * @param rawURL
     * @return
     */
    public static String doURLnormalization(String rawURL)
    {
        String website_name = "";

        String url_portion = "";

        Integer break_index = 0;

        //

        if(rawURL==null || rawURL.isEmpty()) return null;

        //

        if(rawURL.endsWith("/")) rawURL = rawURL.substring(0,rawURL.length()-1);

        //

        if(rawURL.startsWith("https://"))
        {
            website_name = rawURL.substring(8);
        }
        else if(rawURL.startsWith("http://"))
        {
            website_name = rawURL.substring(7);
        }

        //

        website_name = rawURL;

        //

        break_index = website_name.indexOf("/");

        //

        if(break_index>0)
        {
            website_name = rawURL.substring(0, break_index);         //trim for www.google.com or google.com
        }

        if(break_index>0)
        {
            url_portion = rawURL.substring(break_index);                       //trim for ?q=fantasy_football
        }

        //

        url_portion = url_portion.replace(".LT.", "..LT..");    // less than escaping ~ ? ~ .LT. --> ..LT..

        url_portion = url_portion.replace(".GT.", "..GT..");    // greater than escaping ~ ? ~ .GT. --> ..GT..

        url_portion = url_portion.replace(".CO.", "..CO..");    // colon escaping ~ ? ~ .CO. --> ..CO..

        url_portion = url_portion.replace(".DQ.", "..DQ..");    // double quote escaping ~ ? ~ .DQ. --> ..DQ..

        url_portion = url_portion.replace(".FS.", "..FS..");    // forward slash escaping ~ ? ~ .FS. --> ..FS..

        url_portion = url_portion.replace(".BS.", "..BS..");    // back slash escaping ~ ? ~ .BS. --> ..BS..

        url_portion = url_portion.replace(".PI.", "..PI..");    // pipe escaping ~ ? ~ .PI. --> ..PI..

        url_portion = url_portion.replace(".AS.", "..AS..");    // asterix escaping ~ ? ~ .AS. --> ..AS..

        url_portion = url_portion.replace(".QM.", "..QM..");    // question mark escaping ~ ? ~ .QM. --> ..QM..

        // .QM..QM. => ..QM....QM.. or ..QM...QM.. check please

        url_portion = url_portion.replace("<",".LT.");

        url_portion = url_portion.replace(">",".GT.");

        url_portion = url_portion.replace(":",".CO.");

        url_portion = url_portion.replace("\"",".DQ.");

        url_portion = url_portion.replace("/",".FS.");

        url_portion = url_portion.replace("\\",".BS.");

        url_portion = url_portion.replace("|",".PI.");

        url_portion = url_portion.replace("*",".AS.");

        url_portion = url_portion.replace("?", ".QM.");

        //

        //url_portion = url_portion+System.getProperty("file.separator");

        //

        return (website_name+System.getProperty("file.separator")+url_portion).replace(".FS.", "\\");
    }
}