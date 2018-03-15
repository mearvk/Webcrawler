package webcrawler.implementations.three.modules;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.threading.WorkerThread;
import webcrawler.implementations.three.utils.data.HTTPRequestFailurePercentage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ModuleOne extends webcrawler.common.ModuleOne implements Runnable
{
    public Integer count = 0;

    public final Integer threadcount = Runtime.getRuntime().availableProcessors();

    public ArrayList<WorkerThread> threads = new ArrayList<>(threadcount);

    public static Queue<WebcrawlerParam> recursiveparams = new ConcurrentLinkedQueue<>();

    public static HashMap<String, HTTPRequestFailurePercentage> percentage = new HashMap<>();

    /**
     *
     */
    public ModuleOne()
    {
        for(int i=0; i<threadcount; i++)
        {
            this.threads.add(new WorkerThread(this, "Thread " + i));
        }
    }

    /**
     *
     */
    public void run()
    {
        for(int i=0; i<threadcount; i++)
        {
            this.threads.get(i).start();
        }
    }

    /**
    *
    */
    private boolean entryexists(final WebcrawlerParam param)
    {
        for(int i=0; i<this.threads.size(); i++)
        {
            Queue copy = new ConcurrentLinkedQueue<WebcrawlerParam>(this.threads.get(i).site_queue);

            for(int j=0; j<copy.size(); j++)
            {
                WebcrawlerParam _param = (WebcrawlerParam)copy.poll();

                String aParam = param.HREF.trim();

                String bParam = _param.HREF.trim();

                if(aParam.equalsIgnoreCase(bParam))
                {
                    return true;
                }
            }

            copy = null;
        }

        System.gc();

        return false;
    }

    /**
     *
     * @param param
     */
    public void offer(WebcrawlerParam param)
    {
        ModuleOne.recursiveparams.add(param);
    }

    /**
     *
     * @param param
     */
    public void preoffer(WebcrawlerParam param)
    {
        if( this.entryexists(param) ) return;

        //

        switch(count%this.threadcount)
        {
            case 11:

                threads.get(11).site_queue.offer(param);

                break;

            case 10:

                threads.get(10).site_queue.offer(param);

                break;

            case 9:

                threads.get(9).site_queue.offer(param);

                break;

            case 8:

                threads.get(8).site_queue.offer(param);

                break;

            case 7:

                threads.get(7).site_queue.offer(param);

                break;

            case 6:

                threads.get(6).site_queue.offer(param);

                break;

            case 5:

                threads.get(5).site_queue.offer(param);

                break;

            case 4:

                threads.get(4).site_queue.offer(param);

                break;

            case 3:

                threads.get(3).site_queue.offer(param);

                break;

            case 2:

                threads.get(2).site_queue.offer(param);

                break;

            case 1:

                threads.get(1).site_queue.offer(param);

                break;

            case 0:

                threads.get(0).site_queue.offer(param);

                break;

            default: break;
        }

        count++;
    }
}
