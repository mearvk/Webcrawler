package webcrawler.implementations.one.threading;

import webcrawler.common.ModuleOne;
import webcrawler.common.WebcrawlerParam;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 */
public class WorkerThread extends Thread
{
    public Queue<WebcrawlerParam> queue = new ArrayBlockingQueue(100);

    public Boolean running = true;

    public ModuleOne runner = null;

    public Integer original_size = null;

    //

    public WorkerThread(ModuleOne runner, String threadname)
    {
        this.runner = runner;

        this.setName(threadname);
    }

    public WorkerThread(ModuleOne runner)
    {
        this.runner = runner;
    }

    public WorkerThread()
    {
        //
    }

    public WorkerThread(String threadname)
    {
        super(threadname);
    }

    //

    public void run()
    {
        this.original_size = this.queue.size();

        //

        while(running)
        {
            if(queue.isEmpty())
            {
                try
                {
                    this.wait(250L);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {

                    WebcrawlerParam param = queue.poll();

                    //

                    String websiteURL = param.url;

                    //

                    System.out.println("Thread \""+this.getName()+"\" now working with \""+param.url +"\" - "+queue.size()+" out of "+this.original_size+" sites remain.");

                    //

                    param.url = websiteURL;

                    param.href = websiteURL;

                    param.html = (String)runner.functions.run(runner,"dorequestandstoresite", param.html, String.class, param);                  //dorequestandstoresite(param);

                    param.siteAnchors = (ArrayList<String>)runner.functions.run(runner,"doparseanchors", param.siteAnchors, ArrayList.class, param);    //

                    param.recurseMessage = (String)runner.functions.run(runner,"doglobalsiterecurse", param.recurseMessage, String.class, param, 0);            //

                    //
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
