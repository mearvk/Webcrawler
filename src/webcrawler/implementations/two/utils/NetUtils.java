package webcrawler.implementations.two.utils;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.two.Webcrawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;

public class NetUtils
{
    /**
     *
     * @param param
     * @param storedanchors
     * @param depth
     * @return
     */
    public static synchronized ArrayList<String> dorequestandstoreanchors(WebcrawlerParam param, ArrayList<String> storedanchors, Integer depth)
    {
        String threadname = Thread.currentThread().getName();

        //

        if(Webcrawler.LOCAL_RECURSE_DEPTH<=depth) return null;

        if(storedanchors==null) storedanchors = new ArrayList<String>();

        //

        try
        {
            storedanchors.addAll(new HashSet<>(ParseUtils.doparseandsortanchors(param)));

            //

            outer:
            for(int i=0; i<storedanchors.size(); i++)
            {
                if(storedanchors.get(i)==null) continue;

                //

                inner:
                for(int j=0; j<Webcrawler.readyanchorlinks.size(); j++)
                {
                    if (Webcrawler.readyanchorlinks.get(j)==null || Webcrawler.readyanchorlinks.get(j).isEmpty() )
                    {
                        continue;
                    }
                    else
                    {
                        String storedanchor = storedanchors.get(i);

                        String visitedanchor = Webcrawler.readyanchorlinks.get(j);

                        //

                        if(storedanchor==null || visitedanchor==null) continue;

                        //

                        if(storedanchor.trim().equalsIgnoreCase(visitedanchor.trim()))
                        {
                            System.out.println("Site or link \""+storedanchor+"\" has already been visited; skipping.");

                            continue outer;
                        }
                    }
                }

                //

                try
                {
                    String anchor = storedanchors.get(i);

                    if(anchor == null) continue;    //skip for now

                    anchor = anchor.startsWith("//") ? anchor.substring(2) : anchor;

                    anchor = anchor.startsWith("http") ? anchor : "https://" + anchor;

                    //

                    WebcrawlerParam recursisveparam = null;

                    try
                    {
                        recursisveparam = new WebcrawlerParam(param, anchor);

                        System.out.println(threadname+" :: visiting "+anchor+".");

                        recursisveparam.html = NetUtils.dorequest(recursisveparam);
                    }
                    catch(FileNotFoundException fnfe)
                    {
                        System.err.println("NetUtils.dorequestandstoreanchors :: Site or link not found: "+param.href);
                    }
                    catch (Exception e)
                    {
                        System.err.println("NetUtils.dorequestandstoreanchors :: "+e);
                    }

                    //

                    dorequestandstoreanchors(recursisveparam, storedanchors, depth + 1);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("NetUtils.dorequestandstoreanchors :: Site or link not found: "+param.href);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Precurse for site \""+param.url+"\" completed.");
        }

        return storedanchors;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static synchronized String dorequest(WebcrawlerParam param) throws Exception
    {
        String threadname = Thread.currentThread().getName();

        //

        URL url=null;

        URI uri=null;

        HttpURLConnection connection=null;

        //

        if(param.href==null || param.href.isEmpty()) return null;

        //

        if( param.href.startsWith("http://tel:") || param.href.startsWith("https://tel:") ) throw new Exception("Unhandled protocol ["+param.href+"] : skipping in request.");

        if( param.href.startsWith("android-app://") || param.href.startsWith("android-app") ) throw new Exception("Unhandled protocol ["+param.href+"] : skipping in request.");

        if( param.href.startsWith("#") ) throw new Exception("Unhandled protocol ["+param.href+"] : skipping in request.");

        if( param.href.startsWith("data") ) throw new Exception("Unhandled protocol ["+param.href+"] : skipping in request.");

        //

        try
        {
            url = new URL(new URI(param.href).normalize().toString());
        }
        catch (Exception e)
        {
            System.err.println("NetUtils.dopreload :: unable to setup URL: \""+param.href+"\"");
        }

        //

        if(url==null) return null;

        //

        connection = (HttpURLConnection)url.openConnection();

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

            param.html = builder.toString();
        }
        catch(SocketTimeoutException stoe)
        {
            System.out.println("    >> "+stoe.getMessage()+" for "+param.href);
        }
        catch(FileNotFoundException fnfe)
        {
            System.err.println("NetUtils.dopreload :: Site or link not found: "+param.href);
        }
        catch(Exception e)
        {
            //
        }
        finally
        {
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
    public static synchronized String dorequestandstoresite(WebcrawlerParam param) throws Exception
    {
        String threadname = Thread.currentThread().getName();

        //

        URL url=null;

        HttpURLConnection connection=null;

        //

        try
        {
            url = new URL(new URI(param.href).normalize().toString());
        }
        catch(Exception e)
        {
            System.err.println("NetUtils.dorequestandstoresite :: unable to setup URL: "+param.href);
        }

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

        StringBuilder builder = new StringBuilder();

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

            param.html = builder.toString();

            //param.href = ParseUtils.dogetbaseURL(param.href);

            //

            FileUtils.dopersist(param);

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

        //

        if(builder==null) throw new Exception("Unable to retrieve HTML for site: "+param.url);

        //

        return builder.toString();
    }
}
