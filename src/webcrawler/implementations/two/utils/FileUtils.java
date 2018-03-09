package webcrawler.implementations.two.utils;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.two.Webcrawler;
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

    public static synchronized File doclearfileurl(File file)
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
     * @return
     * @throws Exception
     */
    public static synchronized String dodeterminefullpathforpersist(WebcrawlerParam param) throws Exception
    {
        if(param==null) throw new NullPointerException();

        if(param.url ==null) throw new NullPointerException();

        if(param.href==null) throw new NullPointerException();

        //

        String retval=null;

        //

        if(param.href.startsWith("/")) //relative path
        {
            retval = param.url +System.getProperty("file.separator")+param.href;

            retval = retval.replace("https", "");

            retval = retval.replace("http", "");

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");
        }
        else //full path in HREF link
        {
            retval = param.href;

            retval = retval.replace("https", "");

            retval = retval.replace("http", "");

            retval = retval.replace(":", "");

            retval = retval.replace("//", "");

            retval = retval.replace("..", "");
        }

        return retval;
    }

    /**
     *
     * @param param
     * @param inputURL
     * @return
     * @throws Exception
     */
    public static synchronized String dodeterminefullpathforhttpreference(WebcrawlerParam param, String inputURL) throws Exception
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
                inputURL = param.url + inputURL;
            }
            else if(inputURL.startsWith("./"))
            {
                inputURL = param.url + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.url + "/" + inputURL;
        }
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");

        return inputURL;
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public static synchronized String dopersist(WebcrawlerParam param) throws Exception
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

        String dirref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\") +SLASH+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL));

        String fileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+SLASH+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"index.html");

        String imagefileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+SLASH+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"images");

        String scriptfileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+SLASH+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"javascript");

        String cssfileref = Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\")+SLASH+ Utils.dofileseparatornormalization(Utils.doURLnormalization(param.unqualifiedURL))+ Utils.dofileseparatornormalization("\\"+"css");

        //

        try
        {
            File dir = new File(dirref);

            if(!dir.exists()) dir.mkdirs();

            //

            File file = new File(dir.getAbsolutePath(), "index.html");

            if(file.exists()) file.delete(); //weird case where new directory is made for URL and followed by ./index.html and here we do not treat second directory creation step.

            file = FileUtils.doclearfileurl(file);

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
                        FileUtils.persistimage(param, ParseUtils.parseimageforsrcattributevalue(param.siteImages.get(i)), imagefileref);
                    }
                    catch(Exception e)
                    {
                        System.err.println("FileUtils.dofullpersist :: "+e.getMessage());
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
                        System.err.println("FileUtils.dofullpersist :: "+e.getMessage());
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
                        System.err.println("FileUtils.dofullpersist :: "+e.getMessage());
                    }
                }
            }

            //

            FileWriter writer = new FileWriter(file);

            writer.write(param.html);

            writer.flush();

            writer.close();
        }
        catch(Exception e)
        {
            System.err.println("FileUtils.dofullpersist :: "+e.getMessage());
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
    public static synchronized String dopersistsiteurlasvisited(String url)
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

        File visitedsitesdir = new File(Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\"+"meta"));

        File visitedsitesfile = new File(Utils.dofileseparatornormalization(Webcrawler.BASEDIR+"\\"+smonth+"-"+sday+"-"+syear+"\\"+"meta"+"\\"+"visited.csv"));

        //

        if(!visitedsitesdir.exists())
        {
            visitedsitesdir.mkdirs();
        }

        //

        try
        {
            if(!visitedsitesfile.exists()) throw new Exception(">>Index file ["+visitedsitesfile.getName()+"] is missing.");

            BufferedReader reader = new BufferedReader(new FileReader(visitedsitesfile));

            // https;//google.com, 02-12-2018, 13:37 EST

            String sitename = null;

            String line = null;

            String[] tokens = null;

            while ((line = reader.readLine()) != null)
            {
                tokens = line.split(",");

                if (tokens == null) continue;

                sitename = tokens[0];

                //

                sitename = sitename.trim();

                url = url.trim();

                //

                while (sitename.endsWith("/")) {
                    sitename = sitename.substring(sitename.length() - 1);
                }

                while (url.endsWith("/")) {
                    url = url.substring(url.length() - 1);
                }

                if (sitename.equals(url)) {
                    reader.close();

                    reader = null;

                    return "preexisting";
                }
            }

            //

            reader.close();

            reader = null;
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());    //for now let's listen
        }

        try
        {
            //

            BufferedWriter writer = new BufferedWriter(new FileWriter(visitedsitesfile, true));

            writer.write(url+","+date+","+time+"\n");

            writer.flush();

            writer.close();

            writer = null;

            //
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());    //for now let's listen
        }

        return "success";
    }

    /**
     *
     * @param param
     * @param inputURL
     * @param outputURL
     * @throws Exception
     */
    public static synchronized void persistfile(WebcrawlerParam param, String inputURL, String outputURL) throws Exception
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
                inputURL = param.url + inputURL;
            }

            if(inputURL.startsWith("./"))
            {
                inputURL = param.url + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.url + "/" + inputURL;
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
            System.err.println("FileUtils.persistfile :: Site or link not found: \""+fnfe.getMessage()+"\"");
        }
        catch(Exception e)
        {
            System.err.println("FileUtils.persistfile :: Exception: "+e.getMessage());
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
    public static synchronized void persistimage(WebcrawlerParam param, String inputURL, String outputURL) throws Exception
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
                inputURL = param.url + inputURL;
            }

            if(inputURL.startsWith("./"))
            {
                inputURL = param.url + inputURL;
            }
        }
        else if(inputURL.startsWith("http"))
        {
            //do nothing should be absolute URL
        }
        else if(!inputURL.isEmpty() && inputURL.charAt(0)!='/')
        {
            inputURL = param.url + "/" + inputURL;
        }
        else throw new Exception("Neither relative nor absolute URL found for file: \""+inputURL+"\"");

        //

        String filename = inputURL.substring(inputURL.lastIndexOf("/")+1);

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
            System.err.println("FileUtils.persistimage :: File not found: \""+fnfe.getMessage()+"\"");
        }
        catch(Exception e)
        {
            System.err.println("FileUtils.persistimage :: Exception: "+e.getMessage());
        }

        //

        System.gc();
    }
}
