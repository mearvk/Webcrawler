package webcrawler.implementations.three.utils;

import webcrawler.common.SiteSpecialization;
import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.three.modules.ModuleOne;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;

public class NetUtils
{
    /**
     *
     * @return
     */
    public static ArrayList<String> doenqueuelocalsites()
    {
        ArrayList<String> storedanchors = new ArrayList<String>();

        //

        for(SiteSpecialization site : WebcrawlerParam.sites.predefined)
        {
            WebcrawlerParam param = new WebcrawlerParam();

            try
            {
                param.HREF = new URI(site.SITENAME).normalize().toString();

                param.URL = new URI(site.SITENAME).normalize().toString();

                param.DOMAIN_NAME = ParseUtils.dogetbasedomainname(site.SITENAME);

                param.FULL_DOMAIN_NAME = ParseUtils.dogetfulldomainname(site.SITENAME);

                param.LDEPTH = site.LOCAL_DEPTH;

                param.GDEPTH = site.GLOBAL_DEPTH;

                //

                ModuleOne moduleone;

                moduleone = (ModuleOne) Webcrawler.modules.get("moduleone");

                moduleone.offer(param);


                Thread.sleep(20000);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                System.gc();
            }
        }

        return storedanchors;
    }

    /**
     *
     * @return
     */
    public static ArrayList<String> doenqueueremotesites()
    {
        ArrayList<String> storedanchors = new ArrayList<String>();

        //

        for(SiteSpecialization site : WebcrawlerParam.sites.predefined)
        {
            WebcrawlerParam param = new WebcrawlerParam();

            try
            {
                param.HREF = new URI(site.SITENAME).normalize().toString();

                param.URL = new URI(site.SITENAME).normalize().toString();

                param.DOMAIN_NAME = ParseUtils.dogetbasedomainname(site.SITENAME);

                param.FULL_DOMAIN_NAME = ParseUtils.dogetfulldomainname(site.SITENAME);

                param.LDEPTH = site.LOCAL_DEPTH;

                param.GDEPTH = site.GLOBAL_DEPTH;

                //

                ModuleOne moduleone;

                moduleone = (ModuleOne) Webcrawler.modules.get("moduleone");

                moduleone.offer(param);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                System.gc();
            }
        }

        return storedanchors;
    }

    /**
     *
     * @param CURRENT_DEPTH
     * @param MAX_DEPTH
     * @return
     */
    private static boolean exceedsmaximumdepth(final int CURRENT_DEPTH, final int MAX_DEPTH)
    {
        if(MAX_DEPTH<=0)
        {
            if (Webcrawler.LOCAL_RECURSE_DEPTH <= CURRENT_DEPTH) return true;
        }
        else
        {
            if(CURRENT_DEPTH >= MAX_DEPTH) return true;
        }

        return false;
    }

    /**
     *
     * @param param
     * @param anchors
     * @param CURRENT_DEPTH
     * @param MAX_DEPTH
     * @return
     */
    public static ArrayList<String> dorequestandstoreanchors(WebcrawlerParam param, ArrayList<String> anchors, final Integer CURRENT_DEPTH, final Integer MAX_DEPTH)
    {
        if(anchors==null) throw new InvalidParameterException();

        if(param==null) throw new InvalidParameterException();

        //

        if( NetUtils.exceedsmaximumdepth(CURRENT_DEPTH, MAX_DEPTH) ) return anchors;

        //

        try
        {
            anchors.addAll(new HashSet<>(ParseUtils.doparseandsortanchors(param)));

            //

            for(int i=0; i<anchors.size(); i++)
            {
                if(Webcrawler.visitedsitelinks.contains(anchors.get(i))) continue;

                try
                {
                    String anchor = anchors.get(i);

                    //

                    if(anchor == null) continue;    //skip for now

                    //

                    anchor = anchor.startsWith("//") ? anchor.substring(2) : anchor;

                    anchor = anchor.startsWith("http") ? anchor : "https://" + anchor;

                    //

                    WebcrawlerParam recursiveparam = null;

                    try
                    {
                        recursiveparam = new WebcrawlerParam();

                        recursiveparam.URL = param.URL;

                        recursiveparam.HREF = anchor;

                        recursiveparam.HTML = NetUtils.dositerequest(recursiveparam);

                        recursiveparam.LDEPTH = 4;

                        recursiveparam.GDEPTH = 4;

                        //

                        if(recursiveparam.HTML==null || recursiveparam.HTML.isEmpty()) continue;


                        //

                        ModuleOne moduleone = (ModuleOne)Webcrawler.modules.get("moduleone");

                        moduleone.offer(recursiveparam);

                        //

                        Webcrawler.visitedsitelinks.add(anchor);

                        //

                        NetUtils.dorequestandstoreanchors(recursiveparam, anchors, CURRENT_DEPTH + 1, MAX_DEPTH);

                        //

                        recursiveparam.HTML = null;

                        //System.out.println(">> "+param.URL+" branch "+anchor+" precursed.");
                    }
                    catch (Exception e)
                    {
                        System.err.println("NetUtils.dorequestandstoreanchors :: "+e);
                    }

                }
                catch(Exception e)
                {
                    System.err.println("NetUtils.dorequestandstoreanchors :: "+e.getMessage());
                }
            }
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("NetUtils.dorequestandstoreanchors :: Site or link not found: "+param.HREF);
        }
        catch(Exception e)
        {
            System.err.println("NetUtils.dorequestandstoreanchors :: "+e);
        }
        finally
        {
            System.gc();
        }

        return anchors;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static String dositerequest(WebcrawlerParam param) throws Exception
    {
        String threadname = Thread.currentThread().getName();

        //

        URL url=null;

        URI uri=null;

        HttpURLConnection connection=null;

        //

        if(param.HREF ==null || param.HREF.isEmpty()) return null;

        //

        if( param.HREF.startsWith("http://tel:") || param.HREF.startsWith("https://tel:") ) throw new Exception("Unhandled protocol ["+param.HREF +"] : skipping in request.");

        if( param.HREF.startsWith("android-app://") || param.HREF.startsWith("android-app") ) throw new Exception("Unhandled protocol ["+param.HREF +"] : skipping in request.");

        if( param.HREF.startsWith("#") ) throw new Exception("Unhandled protocol ["+param.HREF +"] : skipping in request.");

        if( param.HREF.startsWith("data") ) throw new Exception("Unhandled protocol ["+param.HREF +"] : skipping in request.");

        //

        try
        {
            url = new URL(new URI(param.HREF).normalize().toString());
        }
        catch (Exception e)
        {
            //System.err.println("NetUtils.dopreload :: unable to setup URL: \""+param.HREF+"\"");
        }

        //

        if(url==null) return null;

        //

        //

        param.URL = url.toString();

        param.HREF = url.toString();

        param.FULL_DOMAIN_NAME = ParseUtils.dogetfulldomainname(param.HREF);

        param.DOMAIN_NAME = ParseUtils.dogetbasedomainname(param.HREF);

        //

        connection = (HttpURLConnection)url.openConnection();

        connection.setReadTimeout(10000);

        connection.setConnectTimeout(10000);

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
                    String newurl = param.HREF = connection.getHeaderField("location");

                    String cookies = connection.getHeaderField("set-cookie");

                    //

                    connection = (HttpURLConnection) new URL(newurl).openConnection();

                    connection.setRequestProperty("cookies", cookies);

                    responsecode = connection.getResponseCode();

                    //

                    if(responsecode!=HttpURLConnection.HTTP_OK)
                    {
                        System.out.println("    >> Thread \""+threadname+"\" :: request for website ["+param.HREF +"] failed with fatal code: "+responsecode);
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

            reader.close();

            reader = null;

            //

            param.HTML = builder.toString();
        }
        catch(SocketTimeoutException stoe)
        {
            System.out.println("    >> "+stoe.getMessage()+" for "+param.HREF);
        }
        catch(FileNotFoundException fnfe)
        {
            //System.err.println("NetUtils.dopreload :: Site or link not found: "+param.HREF);
        }
        catch(Exception e)
        {
            //
        }
        finally
        {
            connection = null;

            System.gc();
        }

        //

        if(builder==null) return null;

        //

        return builder.toString();
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    //@Description("Extension Safe")
    public static String dorequestandstoresite(WebcrawlerParam param) throws Exception
    {
        String threadname = Thread.currentThread().getName();

        //

        try
        {
            NetUtils.dositerequest(param);

            FileUtils.dofullsitepersist(param);
        }
        catch(SocketTimeoutException stoe)
        {
            //System.out.println("    >> "+stoe.getMessage()+" for "+param.HREF);
        }
        catch(ConnectException ce)
        {
            //System.err.println("Resource \""+param.HREF+"\" could not be connected to; HTTP call fails.");
        }
        catch(FileNotFoundException fnfe)
        {
            //System.err.println("Resource "+param.HREF+" exists as a link but not actually a page reference. HTTP returned 404.");
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            System.gc();
        }

        //

        if(param.HTML ==null) throw new Exception("Unable to retrieve HTML for site: "+param.URL);

        //

        return param.HTML;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static String dorequestandstorepage(WebcrawlerParam param) throws Exception
    {
        try
        {
            NetUtils.dositerequest(param);

            FileUtils.doquickpersist(param.FULL_DOMAIN_NAME, param.HTML);

            //

            return "success";
        }
        catch(Exception e)
        {
            System.err.println(e);
        }

        return "failure";
    }
}
