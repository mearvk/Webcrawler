package webcrawler.implementations.one.modules;

import webcrawler.common.WebcrawlerParam;
import webcrawler.exceptions.StackDepthException;
import webcrawler.implementations.two.Webcrawler;
import webcrawler.implementations.two.initialization.Initializer;
import webcrawler.implementations.two.threading.WorkerThread;
import webcrawler.implementations.utils.Utils;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ModuleOne extends webcrawler.common.ModuleOne implements Runnable
{
    //pull the website recursively

    WorkerThread wthread_000 = new WorkerThread(this,"thread_000");

    WorkerThread wthread_001 = new WorkerThread(this,"thread_001");

    WorkerThread wthread_002 = new WorkerThread(this,"thread_002");

    WorkerThread wthread_003 = new WorkerThread(this,"thread_003");

    WorkerThread wthread_004 = new WorkerThread(this,"thread_004");

    WorkerThread wthread_005 = new WorkerThread(this,"thread_005");

    WorkerThread wthread_006 = new WorkerThread(this,"thread_006");

    WorkerThread wthread_007 = new WorkerThread(this,"thread_007");

    //

    public void run()
    {
        ArrayList<String> websites = (ArrayList<String>)((Initializer) Webcrawler.values.get("initializer")).variables.get("websites");

        //

        for(int i=0; i<websites.size(); i++)
        {
            WebcrawlerParam param = new WebcrawlerParam();

            //

            param.url = websites.get(i);

            param.href = websites.get(i);

            //
            if(param==null)
            {
                System.out.println(param.url +" is unsupported or such.");
            }
            else if(i%8==7)
            {
                wthread_007.queue.offer(param);
            }
            else if(i%8==6)
            {
                wthread_006.queue.offer(param);
            }
            else if(i%8==5)
            {
                wthread_005.queue.offer(param);
            }
            else if(i%8==4)
            {
                wthread_004.queue.offer(param);
            }
            else if(i%8==3)
            {
                wthread_003.queue.offer(param);
            }
            else if(i%8==2)
            {
                wthread_002.queue.offer(param);
            }
            else if(i%8==1)
            {
                wthread_001.queue.offer(param);
            }
            else if(i%8==0)
            {
                wthread_000.queue.offer(param);
            }
        }

        //

        wthread_000.start();

        wthread_001.start();

        wthread_002.start();

        wthread_003.start();

        wthread_004.start();

        wthread_005.start();

        wthread_006.start();

        wthread_007.start();
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

        if(param==null || param.html ==null) throw new Exception("Site HTML was not ready for site: "+param.url);

        //

        Matcher matcher = Pattern.compile("<a\\s+.*?>(?:.*?)</a>").matcher(param.html); //parse <a href=""></a> matches for now..

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

        if(param.url ==null) throw new NullPointerException();

        if(param.href==null) throw new NullPointerException();

        //

        String retval=null;

        //

        if(param.href.startsWith("/")) //relative path
        {
            retval = param.url +System.getProperty("file.separator")+param.href;

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
                inputURL = param.url + inputURL;
            }
            else if(inputURL.startsWith("./"))
            {
                inputURL = param.url + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.url + "/" + inputURL;
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

            param.html =builder.toString();
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

            param.html =builder.toString();

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

        if(builder==null) throw new Exception("Unable to retrieve HTML for site: "+param.url);

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
            System.out.println("    >> Thread \""+threadname+"\" :: "+param.url +" reports no <a> links of any kind.");

            return null;
        }
        else
        {
            //System.out.println("    >> Thread \""+threadname+"\" :: "+param.url+" reports "+anchors.size()+" <a> links.");
        }

        //

        if(depth== Webcrawler.LOCAL_RECURSE_DEPTH)
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

            //System.out.println("Anchor #"+i+", \""+anchor+"\", for site "+param.url+" is being run recursively.");

            //

            if(anchor==null || anchor.isEmpty()) continue;

            //

            String href_quickref;

            String baseurl_quickref;

            //

            baseurl_quickref = new StringBuffer(param.url).toString();

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
                    System.out.println("    >> Thread \""+threadname+"\" :: anchor #" + i + ", \"" + this.parseanchorforhrefattributevalue(anchor) + "\", for " + param.url + " is being skipped for being unintelligible or not properly a member site of " + param.url);

                    continue;
                }
            }
            else
            {
                // intelligible but possibly not local site i.e. wellsfargoadvisors.com off of wellsfargo.com

                if(Webcrawler.visitedsitelinks.get(this.parseanchorforhrefattributevalue(anchor))==null || Webcrawler.visitedsitelinks.get(this.parseanchorforhrefattributevalue(anchor)).isEmpty())
                {
                    //System.out.println("    >> Thread \"" + threadname + "\" :: anchor #" + i + ", \"" + this.parseanchorforhrefattributevalue(anchor) + "\", for " + param.url + " is being included for single site recursion.");
                }
            }

            //

            try
            {
                //

                recursiveparam.url = param.url;

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

                    System.out.println("Local recursion has visited and downloaded images, css and HTML from "+ Webcrawler.visitedsitelinks.size()+" site(s) and/or link(s).");
                }
                else continue;

                //

                recursiveparam.url = param.url;

                recursiveparam.anchor = anchor;

                //

                //System.out.println("ModuleOne:doglobalsiterecurse has href value: "+recursiveparam.href);

                //System.out.println("ModuleOne:doglobalsiterecurse has url value: "+recursiveparam.url);

                //

                if(depth>= Webcrawler.LOCAL_RECURSE_DEPTH)
                {
                    throw new StackDepthException("Local stack depth exceeded; returning.");
                }

                recursiveparam.html = this.dorequest(recursiveparam);

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
            System.out.println("Site "+param.url +" had no links of any kind!");

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

                recursiveparam.url = param.url;

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

                    System.out.println("Global recursion has visited "+ Webcrawler.visitedsitelinks.size()+" site(s).");

                }
                else continue;

                //

                recursiveparam.url = param.url;

                recursiveparam.anchor = anchor;

                //

                System.out.println("ModuleOne:dorecurse has href value: "+recursiveparam.href);

                System.out.println("ModuleOne:dorecurse has url value: "+recursiveparam.url);

                //

                if(depth>= Webcrawler.GLOBAL_RECURSE_DEPTH)
                {
                    throw new StackDepthException("Global stack depth exceeded; returning.");
                }

                //

                recursiveparam.html = this.dorequest(recursiveparam);

                recursiveparam.siteAnchors = this.doparseanchors(recursiveparam);

                recursiveparam.recurseMessage = this.dorecurse(recursiveparam, (1+depth));
            }
            catch(NullPointerException npe)
            {
                // check there will be an error if 404 returned by dorequestandstoresite
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

        String smonth = String.format("%02d", month);

        String syear = "20"+String.format("%02d", year).substring(1);

        String sday = String.format("%02d", day);

        //

        String file_separator = System.getProperty("file.separator");

        //

        String dirref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL)+"\\");

        String fileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"index.html");

        String imagefileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"images");

        String scriptfileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"javascript");

        String cssfileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"css");

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

            writer.write(param.html);

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

        String originalsiteHTML = new StringBuffer(param.html).toString();

        //

        Matcher matcher = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.html);

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
                recursiveparam.href = param.url + recursiveparam.href;
            }

            if(recursiveparam.href.endsWith("/"))
            {
                recursiveparam.href = recursiveparam.href.substring(0,recursiveparam.href.length()-1);
            }

            if(param.url.endsWith("/"))
            {
                param.url = param.url.substring(0,param.url.length()-1);
            }

            if( !(recursiveparam.href.trim().equals(param.url.trim()) || recursiveparam.href.replace("www.","").trim().equals(param.url.replace("www.","").trim())) && (Webcrawler.visitedresourcelinks.get(recursiveparam.href)==null || Webcrawler.visitedresourcelinks.size()==0) )
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

                    if(recursiveparam.html ==null) continue;

                    //

                    matcher = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(recursiveparam.html); //parse <img src=""></img> matches for now..

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

        param.html = originalsiteHTML;

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

        Matcher matcher = Pattern.compile("<img\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.html);

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

        Matcher matcher = Pattern.compile("<script\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.html);

        //

        while(matcher.find())
        {
            String match = matcher.group();

            scriptlist.add(match);
        }

        //

        Matcher matcher_link_tags = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.html);

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
                inputURL = param.url + inputURL;
            }

            if(inputURL.startsWith("./"))
            {
                inputURL = param.url + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.url + "/" + inputURL;
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
                inputURL = param.url + inputURL;
            }

            if(inputURL.startsWith("./"))
            {
                inputURL = param.url + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.url + "/" + inputURL;
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

        String smonth = String.format("%02d",month);

        String syear = "20"+String.format("%02d",year).substring(1);

        String sday = String.format("%02d",day);

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
