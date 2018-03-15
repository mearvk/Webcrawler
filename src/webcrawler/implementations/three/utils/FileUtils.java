package webcrawler.implementations.three.utils;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.utils.Utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;

public class FileUtils
{
    public static final Integer WINDOWS_MAX_FILEPATH = 260;

    public static final Integer WINDOWS_MAX_FILENAME = 255;

    //

    public static final Integer LINUX_MAX_FILEPATH = 4096;

    public static final Integer LINUX_MAX_FILENAME = 255;

    //

    /**
     *
     * @param file
     * @return
     */
    public static File doguardagainstlongfilenames(File file)
    {
        String SLASH = System.getProperty("file.separator");

        String FILENAME = file.getAbsolutePath().trim();

        //

        if(FILENAME.length()<=255) return file;

        //

        Integer DIVISIONS = FILENAME.length() / 255;

        String NEWFILENAME = null;

        //

        if(DIVISIONS==1)
            NEWFILENAME = FILENAME.substring(0,254) +SLASH+ FILENAME.substring(255);

        if(DIVISIONS==2)
            NEWFILENAME = FILENAME.substring(0,254) +SLASH+ FILENAME.substring(255,509) +SLASH+ FILENAME.substring(510);

        if(DIVISIONS==3)
            NEWFILENAME = FILENAME.substring(0,254) +SLASH+ FILENAME.substring(255,509) +SLASH+ FILENAME.substring(510,764) +SLASH+ FILENAME.substring(765);

        if(DIVISIONS==4)
            NEWFILENAME = FILENAME.substring(0,254) +SLASH+ FILENAME.substring(255,509) +SLASH+ FILENAME.substring(510,764) +SLASH+ FILENAME.substring(765,1019) +SLASH+ FILENAME.substring(1020);

        if(DIVISIONS==5)
            NEWFILENAME = FILENAME.substring(0,254) +SLASH+ FILENAME.substring(255,509) +SLASH+ FILENAME.substring(510,764) +SLASH+ FILENAME.substring(765,1019) +SLASH+ FILENAME.substring(1020, 1274) +SLASH+ FILENAME.substring(1275);

        if(DIVISIONS>5)
        {
            try
            {
                throw new Exception("Unsupported file length");
            }
            catch (Exception e)
            {
                System.err.println(e.getMessage());
            }
        }

        if(NEWFILENAME==null) return file;

        //

        NEWFILENAME = Paths.get(NEWFILENAME).normalize().toString();

        //

        return new File(NEWFILENAME);
    }

    /**
     *
     * @param param
     * @param inputURL
     * @return
     * @throws Exception
     */
    public static String dodeterminefullpathforhttpreference(WebcrawlerParam param, String inputURL) throws Exception
    {
        if(inputURL==null || inputURL.isEmpty()) throw new Exception();

        //

        if(inputURL.startsWith("//") || inputURL.startsWith("/") || inputURL.startsWith("./"))
        {
            //move to absolute case for simplification

            if(inputURL.startsWith("//"))
            {
                inputURL = "https:"+inputURL;
            }
            else if(inputURL.startsWith("/"))
            {
                inputURL = param.URL + inputURL;
            }
            else if(inputURL.startsWith("./"))
            {
                inputURL = param.URL + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.URL + "/" + inputURL;
        }
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");

        return inputURL;
    }

    /**
     *
     * @param baseURL
     * @param value
     * @return
     * @throws Exception
     */
    public static String doquickpersist(String baseURL, String value) throws Exception
    {
        Date date = new Date();

        Integer month = date.getMonth()+1;

        Integer day = date.getDate();

        Integer year = date.getYear();

        //

        String smonth = String.format("%02d", month);

        String syear = "20"+String.format("%02d", year).substring(1);

        String sday = String.format("%02d", day);

        //

        String SLASH = System.getProperty("file.separator");

        //

        String dirref = Webcrawler.BASEDIR +SLASH+ smonth+"-"+sday+"-"+syear +SLASH+ baseURL;

        //

        try
        {
            File dir = new File(dirref);

            if (!dir.exists()) dir.mkdirs();

            //

            File file = new File(dir.getAbsolutePath(), "index.html");

            if (file.exists()) return "skipping";

            file = FileUtils.doguardagainstlongfilenames(file);

            file.createNewFile();

            //

            BufferedWriter writer;

            writer = new BufferedWriter(new FileWriter(file));

            writer.write(value);

            writer.flush();

            writer.close();

            //

            writer = null;

            dir = null;

            file = null;

            //

            System.gc();
        }
        catch(Exception e)
        {
            //
        }

        return null;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static String dofullsitepersist(WebcrawlerParam param) throws Exception
    {
        Date date = new Date();

        Integer month = date.getMonth()+1;

        Integer day = date.getDate();

        Integer year = date.getYear();

        //

        String smonth = String.format("%02d", month);

        String syear = "20"+String.format("%02d", year).substring(1);

        String sday = String.format("%02d", day);

        //

        String SLASH = System.getProperty("file.separator");

        //

        String basedir = Webcrawler.BASEDIR +SLASH+ smonth+"-"+sday+"-"+syear +SLASH+ Utils.doURLnormalization(param.FULL_DOMAIN_NAME) + (param.PATH.equals("") ? "" : SLASH + Utils.dofileseparatornormalization(Utils.doURLnormalization(param.PATH)));

        //

        String imagefileref = basedir +SLASH+ "images";

        String scriptfileref = basedir +SLASH+ "javascript";

        String cssfileref = basedir +SLASH+ "css";

        //

        try
        {
            File dir = new File(basedir);

            if(!dir.exists()) dir.mkdirs();

            //

            File file = new File(dir.getAbsolutePath(), "index.html");

            if(file.exists())
            {
                System.out.println("Skipped persist event for : "+file.getAbsolutePath());

                return "skipping";
            }

            System.out.println("File persist event for : "+file.getAbsolutePath());

            file = FileUtils.doguardagainstlongfilenames(file);

            file.createNewFile();

            //

            File imagedir = new File(imagefileref);

            if(!imagedir.exists()) imagedir.mkdirs();

            //

            File scriptdir = new File(scriptfileref);

            if(!scriptdir.exists()) scriptdir.mkdirs();

            //

            File cssdir = new File(cssfileref);

            if(!cssdir.exists()) cssdir.mkdirs();

            //

            ParseUtils.parsesiteimages(param);

            if(param.siteImages!=null)
            {
                for(int i=0; i<param.siteImages.size(); i++)
                {
                    try
                    {
                        FileUtils.persistfile(param, ParseUtils.parseimageforsrcattributevalue(param.siteImages.get(i)), imagefileref);
                    }
                    catch(Exception e)
                    {
                        System.err.println("FileUtils.dofullsitepersist :: "+e.getMessage());
                    }
                }
            }

            //

            ParseUtils.parsesitecss(param);

            if(param.siteStyleSheets!=null)
            {
                for(int i=0; i<param.siteStyleSheets.size(); i++)
                {
                    try
                    {
                        FileUtils.persistfile(param, ParseUtils.parselinkforhrefattributevalue(param.siteStyleSheets.get(i)), cssfileref);
                    }
                    catch(Exception e)
                    {
                        System.err.println("FileUtils.dofullsitepersist :: "+e.getMessage());
                    }
                }
            }

            //

            ParseUtils.parsesitescripts(param);

            if(param.siteScripts!=null)
            {
                for(int i=0; i<param.siteScripts.size(); i++)
                {
                    try
                    {
                        FileUtils.persistfile(param, ParseUtils.parsescriptforsrcattributevalue(param.siteScripts.get(i)), scriptfileref);
                    }
                    catch(Exception e)
                    {
                        System.err.println("FileUtils.dofullsitepersist :: "+e.getMessage());
                    }
                }
            }

            //

            FileWriter writer = new FileWriter(file);

            writer.write(param.HTML);

            writer.flush();

            writer.close();

            //

            writer = null;

            dir = null;

            file = null;

            cssdir = null;

            imagedir = null;

            scriptdir = null;
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            System.gc();
        }

        return "success";
    }

    /**
     *
     * @param url
     * @return
     */
    public static String dopersistsiteurlasvisited(String url)
    {
        Date date = new Date();

        Integer month = date.getMonth()+1;

        Integer day = date.getDate();

        Integer year = date.getYear();

        String time = date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();

        //

        String smonth = String.format("%02d",month);

        String syear = "20"+String.format("%02d",year).substring(1);

        String sday = String.format("%02d",day);

        //

        String SLASH = System.getProperty("file.separator");

        //

        File metadir = new File(Webcrawler.BASEDIR +SLASH+ smonth+"-"+sday+"-"+syear +SLASH+ "meta");

        File linksfile = new File(metadir.getAbsolutePath(), "links.csv");

        //

        BufferedWriter writer;

        //

        if(!linksfile.exists())
        {
            try
            {
                linksfile = FileUtils.doguardagainstlongfilenames(linksfile);

                linksfile.createNewFile();
            }
            catch (Exception e)
            {
                System.err.println(e);
            }
        }

        if (!metadir.exists())
        {
            metadir.mkdirs();
        }

        //

        try
        {
            //

            writer = new BufferedWriter(new FileWriter(linksfile, true));

            writer.write(url+","+date+","+time+"\n");

            writer.flush();

            writer.close();

            //
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());    //for now let's listen
        }

        //

        writer = null;

        metadir = null;

        linksfile = null;

        //

        return "success";
    }

    /**
     *
     * @param param
     * @param inputURL
     * @param outputURL
     * @throws Exception
     */
    public static void persistfile(WebcrawlerParam param, String inputURL, String outputURL) throws Exception
    {
        //

        if(inputURL==null || inputURL.isEmpty())
        {
            return;
        }
        else if(inputURL.startsWith("//") || inputURL.startsWith("/") || inputURL.startsWith("./"))
        {
            //move to absolute case for simplification

            if(inputURL.startsWith("//"))
            {
                inputURL = "https:"+inputURL;
            }

            if(inputURL.startsWith("/"))
            {
                inputURL = param.URL + inputURL;
            }

            if(inputURL.startsWith("./"))
            {
                inputURL = param.URL + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.URL + "/" + inputURL;
        }
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");

        //

        if(inputURL.endsWith("\\\\"))
        {
            inputURL =  inputURL.substring(0,inputURL.length()-2);
        }

        if(inputURL.endsWith("\\"))
        {
            inputURL =  inputURL.substring(0,inputURL.length()-1);
        }

        if(outputURL.contains("\\"))
        {
            outputURL = outputURL.replace("\\\\", "\\");
        }

        //

        String filename = inputURL.substring(inputURL.lastIndexOf("/")+1);

        //

        if(filename==null || filename.isEmpty()) return;

        //

        String SLASH = System.getProperty("file.separator");

        //

        InputStream is = null;

        OutputStream os = null;

        //

        try
        {
            URL url = new URL(inputURL);

            is = url.openStream();

            //

            String correctedfilename = Paths.get(outputURL +SLASH+ Utils.doURLnormalization(filename)).normalize().toString();

            //System.out.println("FileUtils.persistfile :: corrected file name: "+correctedfilename);

            //

            File file = new File(correctedfilename);

            if( !file.exists() ) file.createNewFile();

            //

            os = new FileOutputStream(correctedfilename);

            byte[] b = new byte[1024*1024*50]; //file size up to 50 MB

            int length;

            while ((length = is.read(b)) != -1)
            {
                os.write(b, 0, length);
            }

            is.close();

            os.close();

            //

            b = null;

            is = null;

            os = null;
        }
        catch(FileNotFoundException fnfe)
        {
            //System.err.println("FileUtils.persistfile :: Site or link not found: \""+fnfe.getMessage()+"\"");
        }
        catch(Exception e)
        {
            //System.err.println("FileUtils.persistfile :: Exception: "+e.getMessage());
        }

        //

        System.gc();
    }

    /**
     *
     * @param param
     * @param inputURL
     * @param outputURL
     * @throws Exception
     */
    public static void persistimage(WebcrawlerParam param, String inputURL, String outputURL) throws Exception
    {
        if(inputURL.startsWith("//") || inputURL.startsWith("/") || inputURL.startsWith("./"))
        {
            //move to absolute case for simplification

            if(inputURL.startsWith("//"))
            {
                inputURL = "https:"+inputURL;
            }

            if(inputURL.startsWith("/"))
            {
                inputURL = param.URL + inputURL;
            }

            if(inputURL.startsWith("./"))
            {
                inputURL = param.URL + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.URL + "/" + inputURL;
        }
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");

        //

        String filename = inputURL.substring(inputURL.lastIndexOf("/")+1);

        //

        if(filename==null || filename.isEmpty()) return;

        //

        String SLASH = System.getProperty("file.separator");

        //

        InputStream is = null;

        OutputStream os = null;

        try
        {
            URL url = new URL(inputURL);

            is = url.openStream();

            //

            String correctedfilename = Paths.get(outputURL +SLASH+ Utils.doURLnormalization(filename)).normalize().toString();

            //System.out.println("FileUtils.persistimage :: corrected file name: "+correctedfilename);

            //

            os = new FileOutputStream(correctedfilename);

            byte[] b = new byte[1024*1024*50]; //file size up to 50 MB

            int length;

            while ((length = is.read(b)) != -1)
            {
                os.write(b, 0, length);
            }

            is.close();

            os.close();

            //

            b = null;

            is = null;

            os = null;
        }
        catch (FileNotFoundException fnfe)
        {
            if(fnfe.getMessage().contains("403"))
            {
                new String("403 error thrown");
            }

            //System.err.println("FileUtils.persistimage :: File not found: \""+fnfe.getMessage()+"\"");
        }
        catch(Exception e)
        {
            //System.err.println("FileUtils.persistimage :: Exception: "+e.getMessage());
        }

        //

        System.gc();
    }
}