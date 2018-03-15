package webcrawler.implementations.three.initialization;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.three.utils.NetUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RemoteLoader implements Runnable
{
    /**
     *
     */
    public RemoteLoader()
    {

    }

    /**
     *
     */
    public void run()
    {
        NetUtils.doenqueueautomaticsites();
    }
}
