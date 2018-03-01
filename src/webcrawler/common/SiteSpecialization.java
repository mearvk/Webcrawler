package webcrawler.common;

public class SiteSpecialization
{
    public SiteSpecialization(String sitename)
    {
        this.SITENAME = sitename;

        this.LOCAL_DEPTH = 1;

        this.GLOBAL_DEPTH = 1;

        this.IMAGES = true;
    }

    public SiteSpecialization(String sitename, int ldepth, int gdepth)
    {
        this.SITENAME = sitename;

        this.LOCAL_DEPTH = ldepth;

        this.GLOBAL_DEPTH = gdepth;
    }

    public SiteSpecialization(String sitename, int ldepth, int gdepth, boolean images)
    {
        this.SITENAME = sitename;

        this.LOCAL_DEPTH = ldepth;

        this.GLOBAL_DEPTH = gdepth;

        this.IMAGES = images;
    }

    public Integer LOCAL_DEPTH;

    public Integer GLOBAL_DEPTH;

    public String SITENAME;

    public Boolean IMAGES = true;
}