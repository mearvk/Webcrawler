package webcrawler.common;

import java.util.ArrayList;

/**
 *
 */
public class WebcrawlerParam
{
    public WebcrawlerParam()
    {

    }

    public WebcrawlerParam(WebcrawlerParam param, String href)
    {
        this.href = href;

        this.url = param.url;
    }

    public String html;

    public ArrayList<String> siteStyleSheets;

    public ArrayList<String> siteAnchors;

    public ArrayList<String> siteImages;

    public ArrayList<String> siteScripts;

    public String href;

    public String recurseMessage;

    public String persistMessage;

    public String baseurl;

    public String url;

    public String unqualifiedURL;

    public String anchor;
}
