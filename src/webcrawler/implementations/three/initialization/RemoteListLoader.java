package webcrawler.implementations.three.initialization;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.Webcrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoteListLoader implements Runnable
{
    /**
     *
     */
    public RemoteListLoader()
    {

    }

    /**
     *
     */
    public void run()
    {
        try
        {
            WebcrawlerParam param = new WebcrawlerParam();

            param.URL = "https://www.quantcast.com/top-sites/";

            param.HREF = "https://www.quantcast.com/top-sites/";

            //

            this.dopreload(param);

            //

            Initializer initializer = null;

            initializer = (Initializer) Webcrawler.modules.get("initializer");

            //

            ArrayList<String> websites = (ArrayList<String>)initializer.variables.get("predefined");

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param param
     * @return
     * @throws Exception
     */
    public String dopreload(WebcrawlerParam param) throws Exception
    {
        URL url=null;

        HttpURLConnection connection=null;

        //

        url = new URL(new URI(param.HREF).normalize().toString());


        //

        if(url==null) return null;

        //

        connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("GET");

        connection.setReadTimeout(10000);

        connection.setConnectTimeout(10000);

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");

        //

        if(connection==null) return null;

        int responsecode = connection.getResponseCode();

        //

        StringBuilder builder = new StringBuilder();

        String string=null;

        //

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        while((string=reader.readLine())!=null)
        {
            builder.append(string);
        }

        //

        param.HTML = builder.toString();

        //

        System.err.println("❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊ ❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊");

        ArrayList<String> websites = this.doparsequantcastlinks(param);

        System.err.println("Initialized Top 100 Websites...");

        System.err.println("❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊ ❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊❊");

        //

        Initializer initializer = (Initializer) Webcrawler.modules.get("initializer");

        initializer.variables.put("predefined", websites);

        //

        return builder.toString();
    }

    /**
     *
     * @param param
     * @return
     */
    public ArrayList<String> doparsequantcastlinks(WebcrawlerParam param)
    {
        ArrayList<String> links = new ArrayList<String>();

        //

        Matcher matcher = Pattern.compile("(name=\".*?\").*?", Pattern.DOTALL).matcher(param.HTML); //parse <a HREF=""></a> matches for now..

        //

        while(matcher.find())
        {
            String s = matcher.group();

            if(s.startsWith("name"))
            {
                s = s.replace("name=", "https://").replace("\"", "");
            }
            else
            {
                continue;
            }

            links.add(s);
        }

        return links;
    }
}
