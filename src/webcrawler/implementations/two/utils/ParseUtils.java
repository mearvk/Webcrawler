package webcrawler.implementations.two.utils;

import jdk.jfr.Description;
import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.two.Webcrawler;

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
    @Description("Extension Safe")
    public static synchronized ArrayList<String> doparseanchors(WebcrawlerParam param) throws Exception
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
    public static synchronized ArrayList<String> doparseandsortanchors(WebcrawlerParam param) throws Exception
    {
        ArrayList<String> anchorlist = new ArrayList();

        //

        if(param==null || param.html ==null) throw new Exception("Site HTML was not ready for site: "+param.url);

        //

        Matcher matcher = Pattern.compile("<a\\s+.*?>(?:.*?)</a>").matcher(param.html); //parse <a href=""></a> matches for now..

        //

        while(matcher.find())
        {
            String anchor = ParseUtils.parselinkforhrefattributevalue(matcher.group());

            //

            if(anchor==null) continue;

            //

            anchor = anchor.startsWith("//") ? "https:"+anchor : anchor;

            anchor = anchor.startsWith("/") ? anchor.substring(1) : anchor;

            anchor = anchor.startsWith("http") ? anchor : param.url + "/" + anchor;

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
    public static synchronized String parseforbaseurl(String param) throws Exception
    {
        if(param==null || param.isEmpty()) throw new Exception();

        //

        if(param.startsWith("https://"))
        {
            param = param.substring(8);
        }

        if(param.startsWith("http://"))
        {
            param = param.substring(7);
        }

        if(param.endsWith("/"))
        {
            param = param.substring(0,param.length()-1);
        }

        if(param.startsWith("//"))
        {
            param = param.substring(2);
        }

        if(param.startsWith("/"))
        {
            param = param.substring(1);
        }

        if(param.startsWith("#"))
        {
            param = param.substring(1);
        }

        if(param.startsWith("./"))
        {
            param = param.substring(2);
        }

        //

        if( !(param==null || param.isEmpty()) )
        {
            try
            {
                param = param.substring(0, param.indexOf("/"));
            }
            catch(Exception e)
            {
                //System.out.println(e.getMessage());
            }
        }

        //

        if(!param.contains(".edu") && !param.contains(".com") && !param.contains(".net") && !param.contains(".org") && !param.contains(".uk"))
        {
            throw new Exception("Unsupported extension: "+param);
        }

        //

        String debug = param;

        //

        return param;
    }

    /**
     *
     * @param param
     * @return
     */
    public static synchronized ArrayList<String> parsesitescripts(WebcrawlerParam param)
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

        return scriptlist;
    }

    /**
     *
     * @param linktag
     * @return
     */
    public static synchronized String parselinkforhrefattributevalue(String linktag)
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

        return match;
    }

    /**
     *
     * @param imagetag
     * @return
     */
    public static synchronized String parselinkforrelattributevalue(String imagetag)
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
    public static synchronized String parselinkfortypeattributevalue(String imagetag)
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
    public static synchronized String parseanchorforhrefattributevalue(String hreftag)
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
    public static synchronized String parsescriptforsrcattributevalue(String scripttag)
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
     * @return
     */
    public static synchronized ArrayList<String> parsesitecss(WebcrawlerParam param)
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

            recursiveparam.href = ParseUtils.parselinkforhrefattributevalue(linktag);

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
                    NetUtils.dorequest(recursiveparam);

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
    public static synchronized ArrayList<String> parsesiteimages(WebcrawlerParam param)
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
}
