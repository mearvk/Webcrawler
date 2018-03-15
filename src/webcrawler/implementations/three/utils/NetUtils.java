package webcrawler.implementations.three.utils;

import webcrawler.common.SiteSpecialization;
import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.three.initialization.Initializer;
import webcrawler.implementations.three.modules.ModuleOne;
import webcrawler.implementations.three.utils.data.HTTPRequestFailurePercentage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtils
{
    /**
     *
     * @return
     */
    public static ArrayList<String> doenqueuemanualsites()
    {
        ArrayList<String> manually_entered_sites = new ArrayList<String>();

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

                moduleone.preoffer(param);
            }
            catch(Exception e)
            {
                //e.printStackTrace();
            }
            finally
            {
                System.gc();
            }
        }

        return manually_entered_sites;
    }

    /**
     *
     * @return
     */
    public static ArrayList<String> doenqueueautomaticsites()
    {
        ArrayList<String> topsites = new ArrayList<>();

        //

        WebcrawlerParam param = new WebcrawlerParam();

        param.HREF = "https://quantcast.com/top-sites";

        //

        try
        {
            URL url = new URL(new URI(param.HREF).normalize().toString());

            if (url == null) return null;

            //

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            if (connection == null) return null;

            //

            connection.setRequestMethod("GET");

            connection.setReadTimeout(600);

            connection.setConnectTimeout(600);

            connection.setInstanceFollowRedirects(true);

            connection.setDoOutput(true);

            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");

            //

            int responsecode = connection.getResponseCode();

            //

            StringBuilder builder = new StringBuilder();

            String string = null;

            //

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((string = reader.readLine()) != null)
            {
                builder.append(string);
            }

            //

            param.HTML = builder.toString();

            //

            System.err.println("❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊ ❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊");

            //

            Matcher matcher = Pattern.compile("(name=\".*?\").*?", Pattern.DOTALL).matcher(param.HTML); //parse <a HREF=""></a> matches for now..

            //

            while(matcher.find())
            {
                String s = matcher.group();

                if(s.startsWith("name"))
                {
                    s = s.replace("name=", "https://").replace("\"", "");
                }
                else
                {
                    continue;
                }

                topsites.add(s);
            }

            //

            System.err.println("❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊ ❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊");

            //

            Initializer initializer = (Initializer) Webcrawler.modules.get("initializer");

            initializer.variables.put("predefined", topsites);

            //

            reader = null;

            builder = null;

            string = null;

            matcher = null;

            param = null;

            connection = null;
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }

        return topsites;
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

                        recursiveparam.LDEPTH = 2;

                        recursiveparam.GDEPTH = 2;

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

                        anchor = null;

                        moduleone = null;
                    }
                    catch (Exception e)
                    {
                        System.err.println("NetUtils.dorequestandstoreanchors :: "+e);
                    }
                    finally
                    {
                        System.gc();
                    }
                }
                catch(Exception e)
                {
                    System.err.println("NetUtils.dorequestandstoreanchors :: "+e.getMessage());
                }
                finally
                {
                    System.gc();
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

        if(ModuleOne.percentage.get(ParseUtils.dogetfulldomainname(param.HREF))==null)
        {
            ModuleOne.percentage.put(ParseUtils.dogetfulldomainname(param.HREF), new HTTPRequestFailurePercentage());
        }

        if(ModuleOne.percentage.get(ParseUtils.dogetfulldomainname(param.HREF)).tries > 10)
        if(ModuleOne.percentage.get(ParseUtils.dogetfulldomainname(param.HREF)).percentage > 0.50)
        {
            System.err.println("Closing connection; site deemed unreliable.");

            return "Closing connection; site deemed unreliable.";
        }

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
            //
        }

        //

        if(url==null) return null;

        //

        param.URL = url.toString();

        param.HREF = url.toString();

        param.PATH = ParseUtils.dogetpath(param.HREF);

        param.FULL_DOMAIN_NAME = ParseUtils.dogetfulldomainname(param.HREF);

        param.DOMAIN_NAME = ParseUtils.dogetbasedomainname(param.HREF);

        //

        connection = (HttpURLConnection)url.openConnection();

        connection.setInstanceFollowRedirects(true);

        connection.setChunkedStreamingMode(8192);

        connection.setDoOutput(true);

        connection.setRequestProperty("Keep-Alive", "false");

        connection.setReadTimeout(600);

        connection.setConnectTimeout(600);

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
                        HTTPRequestFailurePercentage var = ModuleOne.percentage.get(ParseUtils.dogetfulldomainname(param.HREF));

                        //

                        var.tries++;

                        var.failures++;

                        //

                        var.percentage = (var.failures)/(var.tries);

                        ModuleOne.percentage.put(ParseUtils.dogetfulldomainname(param.HREF), var);

                        System.out.println("    >> Thread \""+threadname+"\" :: request for website ["+param.HREF +"] failed with fatal code: "+responsecode);
                    }
                }
            }

            //

            HTTPRequestFailurePercentage var;

            var = ModuleOne.percentage.get(ParseUtils.dogetfulldomainname(param.HREF));

            var.successes++;

            var.tries++;

            ModuleOne.percentage.put(ParseUtils.dogetfulldomainname(param.HREF), var);

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

            var = null;

            //

            param.HTML = builder.toString();
        }
        catch(SocketTimeoutException stoe)
        {
            //stoe.printStackTrace();
        }
        catch(FileNotFoundException fnfe)
        {
            //fnfe.printStackTrace();
        }
        catch(Exception e)
        {
            //e.printStackTrace();
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
            //
        }
        catch(ConnectException ce)
        {
            //
        }
        catch(FileNotFoundException fnfe)
        {
            //
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
