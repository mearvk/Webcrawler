package webcrawler.implementations.three.threading;

import webcrawler.common.ModuleOne;
import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.utils.NetUtils;
import webcrawler.implementations.three.utils.ParseUtils;

import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

//
public class WorkerThread extends Thread
{
    public volatile Queue<WebcrawlerParam> queue = new ConcurrentLinkedQueue();

    public Boolean running = true;

    public ModuleOne module = null;

    public Integer original_size = null;

    //

    public WorkerThread(ModuleOne runner, String threadname)
    {
        this.module = runner;

        this.setName(threadname);
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
                    synchronized (this)
                    {
                        this.wait(1000L);
                    }
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
                    WebcrawlerParam  param = queue.poll();

                    //

                    System.err.println("* "+this.getName()+" has link \""+param.href+"\" for site \"" +param.url+"\"");

                    //

                    NetUtils.dorequestandstoresite(param);

                    //
                }
                catch(Exception e)
                {
                    if(e instanceof FileNotFoundException)
                    {
                        System.err.println(e.getMessage());
                    }
                    if(e instanceof IllegalArgumentException)
                    {
                        System.err.println(e.getMessage());
                    }
                    else
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
