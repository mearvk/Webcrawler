package webcrawler.implementations.utils;

//import java.nio.file.Paths;

import java.nio.file.Paths;

public class Utils
{
    public final static String OS_SENSITIVE_SLASH = System.getProperty("file.separator");

    /**
     * Please send here to clear file strings (C:/programs/data/text.xml) against particular Operating System file separator implementations (System.getProperty("file.separator");.
     *
     * @param basedir
     * @return
     */
    public static String dofileseparatornormalization(String basedir)
    {
        if(basedir==null) return null;

        String retval = "";

        if(System.getProperty("os.name").contains("indows"))
        {
            retval = basedir.replace("/", Utils.OS_SENSITIVE_SLASH);
        }
        else
        {
            retval = basedir.replace("\\", Utils.OS_SENSITIVE_SLASH);
        }

        //

        retval = Paths.get(retval).normalize().toString();

        //

        return retval;
    }

    /**
     * Please send only HTTP URL portion here for escapement into Windows safe file string(s); will removed unsafe characters (<, >, |, ", *, etc.)
     *
     * @param rawURL
     * @return
     */
    public static String doURLnormalization(final String rawURL)
    {
        String base = new String(rawURL);

        String website_name = "";

        String url_portion = "";

        Integer break_index = 0;

        //

        if(base==null || base.isEmpty()) return null;

        //

        if(base.endsWith("/")) base = base.substring(0,base.length()-1);

        //

        if(base.startsWith("https://"))
        {
            website_name = base.substring(8);
        }
        else if(base.startsWith("http://"))
        {
            website_name = base.substring(7);
        }

        //

        website_name = base; //?

        //

        break_index = website_name.indexOf("/");

        //

        if(break_index>0)
        {
            website_name = base.substring(0, break_index);         //trim for www.google.com or google.com
        }

        if(break_index>0)
        {
            url_portion = base.substring(break_index);                       //trim for ?q=fantasy_football
        }

        //

        if(break_index==-1)
        {
            url_portion = base;
        }

        //

        url_portion = url_portion.replace(".LT.", "..LT..");    // less than escaping ~ ? ~ .LT. --> ..LT..

        url_portion = url_portion.replace(".GT.", "..GT..");    // greater than escaping ~ ? ~ .GT. --> ..GT..

        url_portion = url_portion.replace(".CO.", "..CO..");    // colon escaping ~ ? ~ .CO. --> ..CO..

        url_portion = url_portion.replace(".DQ.", "..DQ..");    // double quote escaping ~ ? ~ .DQ. --> ..DQ..

        url_portion = url_portion.replace(".FS.", "..FS..");    // forward slash escaping ~ ? ~ .FS. --> ..FS..

        url_portion = url_portion.replace(".BS.", "..BS..");    // back slash escaping ~ ? ~ .BS. --> ..BS..

        url_portion = url_portion.replace(".PI.", "..PI..");    // pipe escaping ~ ? ~ .PI. --> ..PI..

        url_portion = url_portion.replace(".AS.", "..AS..");    // asterix escaping ~ ? ~ .AS. --> ..AS..

        url_portion = url_portion.replace(".QM.", "..QM..");    // question mark escaping ~ ? ~ .QM. --> ..QM..

        // .QM..QM. => ..QM....QM.. or ..QM...QM.. check please

        url_portion = url_portion.replaceAll("<",".LT.");

        url_portion = url_portion.replaceAll(">",".GT.");

        url_portion = url_portion.replaceAll(":",".CO.");

        url_portion = url_portion.replaceAll("\"",".DQ.");

        url_portion = url_portion.replaceAll("/",".FS.");

        url_portion = url_portion.replaceAll("\\\\",".BS.");

        url_portion = url_portion.replaceAll("\\|",".PI.");

        url_portion = url_portion.replaceAll("\\*",".AS.");

        url_portion = url_portion.replaceAll("\\?", ".QM.");

        //

        if(break_index==-1) //only a HTTP URL, not a file URL
        {
            return (url_portion).replace(".FS.", "\\");
        }
        else return (website_name+System.getProperty("file.separator")+url_portion).replace(".FS.", "\\");
    }
}
