package webcrawler.implementations.one.initialization;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.one.Webcrawler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class WebsiteListLoader implements Runnable
{
    public void run()
    {
        WebcrawlerParam param = new WebcrawlerParam();

        param.URL = "https://www.quantcast.com/top-sites/";

        param.HREF = "https://www.quantcast.com/top-sites/";

        //

        try
        {
            this.dorequest(param);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //

        Initializer initializer = null;

        initializer = (Initializer) Webcrawler.values.get("initializer");

        //

        ArrayList<String> websites;

        websites = (ArrayList<String>)initializer.variables.get("predefined");

        //

        websites.add("https://en.wikipedia.org/wiki/Computer_science");

        websites.add("https://en.wikipedia.org/wiki/Mathematics");

        websites.add("https://en.wikipedia.org/wiki/Logic");

        websites.add("https://en.wikipedia.org/wiki/Constitution");

        websites.add("https://en.wikipedia.org/wiki/Church");

        websites.add("https://en.wikipedia.org/wiki/Xenu");

        websites.add("https://en.wikipedia.org/wiki/Religion");

        websites.add("https://en.wikipedia.org/wiki/Spirituality");

        websites.add("https://en.wikipedia.org/wiki/Philosophy");

        websites.add("https://en.wikipedia.org/wiki/Network_analysis_(electrical_circuits)");

        websites.add("https://en.wikipedia.org/wiki/Calculus");

        websites.add("https://en.wikipedia.org/wiki/Trigonometry");

        websites.add("https://en.wikipedia.org/wiki/Psychology");

        websites.add("https://en.wikipedia.org/wiki/Psychiatry");

        websites.add("https://en.wikipedia.org/wiki/Pharmacology");

        websites.add("https://en.wikipedia.org/wiki/Algebra");

        websites.add("https://en.wikipedia.org/wiki/Linear_algebra");

        websites.add("https://en.wikipedia.org/wiki/Science");

        websites.add("https://en.wikipedia.org/wiki/Chemistry");

        websites.add("https://en.wikipedia.org/wiki/Intelligence_quotient");

        websites.add("https://en.wikipedia.org/wiki/President_of_the_United_States");

        websites.add("https://en.wikipedia.org/wiki/Vice_President_of_the_United_States");

        websites.add("https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_by_population");

        websites.add("https://en.wikipedia.org/wiki/Wikipedia");

        websites.add("https://en.wikipedia.org/wiki/Pope");

        websites.add("https://en.wikipedia.org/wiki/Pope_Francis");

        websites.add("https://en.wikipedia.org/wiki/L._Ron_Hubbard");

        websites.add("https://en.wikipedia.org/wiki/Truth");

        websites.add("https://en.wikipedia.org/wiki/Java_(programming_language)");

        websites.add("https://en.wikipedia.org/wiki/C_(programming_language)");

        websites.add("https://en.wikipedia.org/wiki/C%2B%2B");

        websites.add("https://en.wikipedia.org/wiki/Music");

        websites.add("https://www.google.com/search?source=hp&q=computer+software&oq=computer+software");

        websites.add("https://www.google.com/search?source=hp&q=system+models&oq=computer+software");

        websites.add("https://www.google.com/search?source=hp&q=reality&oq=reality");

        websites.add("https://www.google.com/search?source=hp&q=teaching&oq=teaching");

        websites.add("https://www.google.com/search?source=hp&q=organization&oq=organization");

        websites.add("https://www.google.com/search?source=hp&q=philosophy&oq=philosophy");

        websites.add("https://www.google.com/search?source=hp&q=honor&oq=honor");

        //

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=church&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=life&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=java+programming&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=c+programming&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=religion&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=scientology&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=president%20trump&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=united%20states%20constitution&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=presidential%20signing%20orders&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=state%20of%20the%20union&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=north%20carolina%20law&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=federalism&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=germany&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=latin%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=italian%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=spanish%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=portugese%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=hindi%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=bengali%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=javanese%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=punjabi%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=landha%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=arabic%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=german%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=french%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=afrikaans%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=dutch%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=chinese%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=mandarin%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=cantonese%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=japanese%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=swahili%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=russian%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=icelandic%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=polynesian%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=inuit%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=korean%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=tagalog%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=tsimshian%20dictionary&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=shakespeare&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=existentialism&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=engineering&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=calculus&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=calculus&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=linear%20algebra&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=trigonometry%20algebra&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=trigonometry&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=fourier&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=chess&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=crime&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=children&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=psychology&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=psychiatry&tbs=,bkv:f&num=100");

        websites.add("https://www.google.com/search?tbo=p&tbm=bks&q=chemistry&tbs=,bkv:f&num=100");

        //

        websites.add("https://news.google.com/news/?ned=us&gl=US&hl=en");                   //usa

        websites.add("https://news.google.com/news/?ned=uk&gl=GB&hl=en-GB");                //uk

        websites.add("https://news.google.com/news/headlines?ned=ar_me&hl=ar&gl=ME");       //arab

        websites.add("https://news.google.com/news/headlines?ned=en_zw&hl=en&gl=ZW");       //zimbabwe

        websites.add("https://news.google.com/news/headlines?ned=es_co&hl=es-419&gl=CO");   //columbia

        websites.add("https://news.google.com/news/headlines?ned=en_bw&hl=en&gl=BW");       //botswana

        websites.add("https://news.google.com/news/headlines?ned=cn&hl=zh-CN&gl=CN");       //china

        websites.add("https://news.google.com/news/headlines?ned=it&hl=it&gl=IT");          //italy

        websites.add("https://news.google.com/news/headlines?ned=hk&hl=zh-HK&gl=HK");       //hong kong

        websites.add("https://news.google.com/news/headlines?ned=nl_nl&hl=nl&gl=NL");       //netherlands

        websites.add("https://news.google.com/news/headlines?ned=en_il&hl=en&gl=IL");       //israel

        websites.add("https://news.google.com/news/headlines?ned=pt-PT_pt&hl=pt-PT&gl=PT"); //portugal
    }

    public String dorequest(WebcrawlerParam param) throws Exception
    {
        URL url=null;

        HttpURLConnection connection=null;

        //

        url = new URL(param.HREF);


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

        param.HTML =builder.toString();

        ArrayList<String> websites = this.doparseHTMLlinks(param);

        //

        Initializer initializer = null;

        initializer = (Initializer)Webcrawler.values.get("initializer");

        initializer.variables.put("predefined", websites);

        //

        return builder.toString();
    }

    public ArrayList<String> doparseHTMLlinks(WebcrawlerParam param)
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
