package webcrawler.implementations.three.threading;

import webcrawler.common.ModuleOne;
import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.utils.NetUtils;

import java.io.FileNotFoundException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//
public class WorkerThread extends Thread implements ShutdownThread
{
    public volatile Queue<WebcrawlerParam> queue = new ConcurrentLinkedQueue();

    public Boolean running = true;

    public ModuleOne module = null;

    public Integer timeout_threshold = 2*6000000; //2 hours

    public Long time_accrued = 0L;

    public Long wait_millis = 1000L;

    //

    public WorkerThread(ModuleOne runner, String threadname)
    {
        this.module = runner;

        this.setName(threadname);
    }

    //

    public void run()
    {
        while(running)
        {
            if(queue.isEmpty())
            {
                try
                {
                    synchronized (this)
                    {
                        this.wait(this.wait_millis);

                        this.doisinactive(this.wait_millis);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                this.time_accrued = 0L;

                try
                {
                    WebcrawlerParam  param = queue.poll();

                    //

                    System.err.println("* "+this.getName()+" has link \""+param.href+"\" for site \"" +param.url+"\"");

                    //

                    NetUtils.dorequestandstoresite(param);
                }
                catch(Exception e)
                {
                    if(e instanceof FileNotFoundException | e instanceof IllegalArgumentException)
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

    @Override
    public void doisinactive(long millis)
    {
        if(this.running)
        {
            try
            {
                this.time_accrued += millis;

                if(this.time_accrued >this.timeout_threshold)
                {
                    this.shutdown();
                }
            }
            catch(Exception e)
            {

            }
        }
    }

    @Override
    public void shutdown()
    {
        if(this.isAlive())
        {
            try
            {
                this.interrupt();
            }
            catch(Exception e)
            {
                //
            }

            try
            {
                this.running = false;
            }
            catch(Exception e)
            {
                //
            }
        }

        System.err.println("-- -- -- -- -- -- -- -- -- -- -- --");

        System.err.println("Thread "+this.getName()+" timed out.");

        System.err.println("-- -- -- -- -- -- -- -- -- -- -- --");
    }
}
