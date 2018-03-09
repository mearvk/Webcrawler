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

    public SiteSpecialization(String sitename, int ldepth, int gdepth, boolean images, String username, String password, String loginstring)
    {
        this.SITENAME = sitename;

        this.LOCAL_DEPTH = ldepth;

        this.GLOBAL_DEPTH = gdepth;

        this.IMAGES = images;

        this.USERNAME = username;

        this.PASSWORD = password;

        this.LOGINSTRING = loginstring;
    }

    //

    private String generateloginstring(String username, String password, String loginstring)
    {
        return ""; //return printf(?username=%s&password=%s, username, password)
    }

    //

    public Integer LOCAL_DEPTH;

    public Integer GLOBAL_DEPTH;

    public String SITENAME;

    public Boolean IMAGES = true;

    public String USERNAME = null;

    public String PASSWORD = null;

    public String LOGINSTRING = null;
}