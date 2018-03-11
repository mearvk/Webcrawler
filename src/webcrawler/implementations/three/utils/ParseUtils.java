package webcrawler.implementations.three.utils;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.Webcrawler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseUtils
{
    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static ArrayList<String> doparseanchors(WebcrawlerParam param) throws Exception
    {
        ArrayList<String> anchorlist = new ArrayList();

        //

        if(param==null || param.HTML ==null) throw new Exception("Site HTML was not ready for site: "+param.URL);

        //

        Matcher matcher = Pattern.compile("<a\\s+.*?>(?:.*?)</a>").matcher(param.HTML); //parse <a HREF=""></a> matches for now..

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
    public static ArrayList<String> doparseandsortanchors(WebcrawlerParam param) throws Exception
    {
        ArrayList<String> anchorlist = new ArrayList();

        //

        if(param==null || param.HTML ==null) throw new Exception("Site HTML was not ready for site: "+param.URL);

        //

        Matcher matcher = Pattern.compile("<a\\s+.*?>(?:.*?)</a>").matcher(param.HTML); //parse <a HREF=""></a> matches for now..

        //

        while(matcher.find())
        {
            String anchor = ParseUtils.parselinkforhrefattributevalue(matcher.group());

            //

            if(anchor==null) continue;

            //

            anchor = anchor.startsWith("//") ? "https:"+anchor : anchor;

            anchor = anchor.startsWith("/") ? anchor.substring(1) : anchor;

            anchor = anchor.startsWith("#") ? param.URL + "/" + anchor : anchor;

            anchor = anchor.startsWith("http") ? anchor : "https://"+param.FULL_DOMAIN_NAME + "/" + anchor;

            //

            anchorlist.add(anchor);
        }

        //

        anchorlist = new ArrayList<>(new HashSet<>(anchorlist));

        //

        Webcrawler.readyanchorlinks.addAll(anchorlist);

        //

        return anchorlist;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static String dogetbasedomainname(final String param) throws Exception
    {
        if(param==null) throw new NullPointerException();

        //

        String retval=param;

        //https://store.google.com --> google.com ... ok

        if(param.startsWith("/")) //relative path
        {
            retval = retval.replace("https", "");

            retval = retval.replace("http", "");

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");

            if(retval.indexOf("/")!=-1)
            {
                retval = retval.substring(0, retval.indexOf("/"));
            }
        }
        else //full path in HREF link
        {
            retval = retval.replace("https", "");

            retval = retval.replace("http", "");

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");

            if(retval.indexOf("/")!=-1)
            {
                retval = retval.substring(0, retval.indexOf("/"));
            }
        }

        //

        retval = retval.trim();

        //

        int periods = retval.split("\\.").length;

        //

        if(periods<=1) throw new Exception("Unusual domain name: "+retval);

        //

        if(periods==2) return retval;

        //

        String suffix, base;

        suffix = base = null;

        if(periods>2)
        {
            suffix = retval.split("\\.")[periods - 1];

            base = retval.split("\\.")[periods - 2];
        }

        //

        String debug = base+"."+suffix;

        //

        return base+"."+suffix;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static String dogetfulldomainname(String param)
    {
        if(param==null || param.isEmpty()) return null;

        //

        //https://store.google.com --> store.google.com ... ok

        //

        if(param.startsWith("https://"))                        //remove https://
        {
            param = param.substring(8);
        }

        if(param.startsWith("http://"))                         //remove http://
        {
            param = param.substring(7);
        }

        if(param.endsWith("/"))                                 //remove final / ... eg. hoogle.com/
        {
            param = param.substring(0,param.length()-1);
        }

        if(param.startsWith("//"))                              //remove //hoogle.com --> hoogle.com
        {
            param = param.substring(2);
        }

        if(param.startsWith("/"))                               //remove /hoogle.com --> hoogle.com
        {
            param = param.substring(1);
        }

        if(param.startsWith("#"))                               //remove #hoogle.com --> hoogle.com
        {
            param = param.substring(1);
        }

        if(param.startsWith("./"))                              //remove ./hoogle.com --> hoogle.com
        {
            param = param.substring(2);
        }

        //

        if( !(param==null || param.isEmpty()) )
        {
            try
            {
                param = param.substring(0, param.indexOf("/")); //grab only DNS name eg.  hoogle.com/?serach=123 --> hoogle.com
            }
            catch(Exception e)
            {
                //
            }
        }

        //

        //grade extensions?

        //

        String debug = param;   //here for debug look

        //

        return param;
    }

    /**
     *
     * @param param
     * @return
     */
    public static ArrayList<String> parsesitescripts(WebcrawlerParam param)
    {
        ArrayList<String> scriptlist = new ArrayList();

        //

        Matcher matcher = Pattern.compile("<script\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.HTML);

        //

        while(matcher.find())
        {
            String match = matcher.group();

            scriptlist.add(match);
        }

        //

        Matcher matcher_link_tags = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.HTML);

        //

        while(matcher_link_tags.find())
        {
            String match = matcher_link_tags.group();

            String href = ParseUtils.parselinkforhrefattributevalue(match);

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

        matcher = null;

        //

        return scriptlist;
    }

    /**
     *
     * @param linktag
     * @return
     */
    public static String parselinkforhrefattributevalue(String linktag)
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

        //

        matcher = null;

        //

        return match;
    }

    /**
     *
     * @param linktag
     * @return
     */
    public static String parseimageforsrcattributevalue(String linktag)
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

        //

        matcher = null;

        //

        return match;
    }

    /**
     *
     * @param imagetag
     * @return
     */
    public static String parselinkforrelattributevalue(String imagetag)
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

        //

        matcher = null;

        //

        return match;
    }

    /**
     *
     * @param imagetag
     * @return
     */
    public static String parselinkfortypeattributevalue(String imagetag)
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

        //

        matcher = null;

        //

        return match;
    }

    /**
     *
     * @param hreftag
     * @return
     */
    public static String parseanchorforhrefattributevalue(String hreftag)
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

        match = match.replace("HREF=\"", "").replace("\"", "");

        //

        matcher = null;

        //

        return match;
    }

    /**
     *
     * @param scripttag
     * @return
     */
    public static String parsescriptforsrcattributevalue(String scripttag)
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

        //

        matcher = null;

        //

        return match;
    }

    /**
     *
     * @param param
     * @return
     */
    public static ArrayList<String> parsesitecss(WebcrawlerParam param)
    {
        ArrayList<String> linkedlist = new ArrayList();

        //

        Matcher matcher = Pattern.compile("<link\\s+(?:.*?)(href=\".*?\")(?:.*?)>").matcher(param.HTML);

        //

        while(matcher.find())
        {
            linkedlist.add(matcher.group());
        }

        //

        linkedlist = new ArrayList(new HashSet(linkedlist));

        //

        for(int i=0; i<linkedlist.size(); i++)
        {
            String linktag = linkedlist.get(i);

            String href_quickref = ParseUtils.parselinkforhrefattributevalue(linktag);

            String rel_quickref = ParseUtils.parselinkforrelattributevalue(linktag);

            String type_quickref = ParseUtils.parselinkfortypeattributevalue(linktag);

            //

            if(href_quickref==null) href_quickref = "";

            if(rel_quickref==null) rel_quickref = "";

            if(type_quickref==null) type_quickref = "";

            //

            if( ! (href_quickref.trim().endsWith(".css") || rel_quickref.contains("stylesheet") || type_quickref.contains("text/css")) )
            {
                linkedlist.remove(i);
            }
        }

        matcher = null;

        //

        return param.siteStyleSheets = linkedlist;
    }

    /**
     *
     * @param param
     * @return
     */
    public static ArrayList<String> parsesiteimages(WebcrawlerParam param)
    {
        ArrayList<String> imagelist = new ArrayList();

        //

        Matcher matcher = Pattern.compile("<img\\s+(?:.*?)(src=\".*?\")(?:.*?)>").matcher(param.HTML);

        //

        while(matcher.find())
        {
            String match = matcher.group();

            imagelist.add(match);
        }

        //

        param.siteImages = imagelist;

        //

        matcher = null;

        //

        return imagelist;
    }
}
