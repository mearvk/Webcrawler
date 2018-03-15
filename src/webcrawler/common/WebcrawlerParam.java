package webcrawler.common;

import webcrawler.implementations.three.mapping.SiteSpecializationMap;

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
        this.HREF = href;

        this.URL = param.URL;
    }

    public String HTML;

    public ArrayList<String> siteStyleSheets;

    public ArrayList<String> siteAnchors;

    public ArrayList<String> siteImages;

    public ArrayList<String> siteScripts;

    public String HREF;

    public String recurseMessage;

    public String persistMessage;

    public String DOMAIN_NAME;

    public String FULL_DOMAIN_NAME;

    public String URL;

    public String PATH;

    public String anchor;

    public int LDEPTH = 0;

    public int GDEPTH = 0;

    //

    public static SiteSpecializationMap sites = new SiteSpecializationMap();
}