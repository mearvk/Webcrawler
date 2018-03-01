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

class WebsiteListLoader implements Runnable
{
    public void run()
    {
        WebcrawlerParam param = new WebcrawlerParam();

        param.url = "https://www.quantcast.com/top-sites/";

        param.href = "https://www.quantcast.com/top-sites/";

        //

        try
        {
            this.dopreload(param);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //

        Initializer initializer = null;

        initializer = (Initializer) Webcrawler.modules.get("initializer");

        //

        ArrayList<String> websites = null;

        websites = (ArrayList<String>)initializer.variables.get("websites");
    }

    public String dopreload(WebcrawlerParam param) throws Exception
    {
        URL url=null;

        HttpURLConnection connection=null;

        //

        url = new URL(new URI(param.href).normalize().toString());


        //

        if(url==null) return null;

        //

        connection = (HttpURLConnection)url.openConnection();

        connection.setRequestMethod("GET");

        connection.setReadTimeout(10000);

        connection.setConnectTimeout(30000);

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

        param.html = builder.toString();

        //

        System.out.println("-- -- -- -- -- -- -- -- -- -- -- -- -- -- --");

        ArrayList<String> websites = this.doparseQUANTCASTlinks(param);

        System.out.println("-- -- -- -- -- -- -- -- -- -- -- -- -- -- --");

        //

        Initializer initializer = null;

        initializer = (Initializer) Webcrawler.modules.get("initializer");

        initializer.variables.put("websites", websites);

        //

        return builder.toString();
    }

    public ArrayList<String> doparseQUANTCASTlinks(WebcrawlerParam param)
    {
        ArrayList<String> links = new ArrayList<String>();

        //

        Matcher matcher = Pattern.compile("(name=\".*?\").*?", Pattern.DOTALL).matcher(param.html); //parse <a href=""></a> matches for now..

        //

        while(matcher.find())
        {
            String s = matcher.group();

            if(s.startsWith("name"))
            {
                s = s.replace("name=", "https://").replace("\"", "");

                System.out.println(s);
            }
            else
            {
                System.out.println("No match");

                continue;
            }

            links.add(s);
        }

        return links;
    }
}
