package webcrawler.implementations.two.threading;

import webcrawler.common.WebcrawlerParam;
import webcrawler.common.ModuleOne;
import webcrawler.implementations.two.utils.FileUtils;
import webcrawler.implementations.two.utils.NetUtils;
import webcrawler.implementations.two.utils.ParseUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

//
public class WorkerThread extends Thread
{
    public Queue<WebcrawlerParam> queue = new ArrayBlockingQueue(100);

    public Boolean running = true;

    public ModuleOne module = null;

    public Integer original_size = null;

    //

    public WorkerThread(ModuleOne runner, String threadname)
    {
        this.module = runner;

        this.setName(threadname);
    }

    public WorkerThread(ModuleOne runner)
    {
        this.module = runner;
    }

    public WorkerThread() {}

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
                    synchronized (this)
                    {
                        this.wait(250L);
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

                    WebcrawlerParam param = queue.poll();

                    //

                    String websiteURL = param.url;

                    //

                    System.out.println("Thread \""+this.getName()+"\" now working with \""+param.url +"\" - "+queue.size()+" out of "+this.original_size+" sites remain.");

                    //

                    param.url = websiteURL;

                    param.href = websiteURL;

                    //

                    this.module.setrunner(module);

                    //

                    NetUtils.dorequest(param);

                    ParseUtils.doparseanchors(param);

                    this.module.functions.run(module, "dolocalsiterecurse", param.recurseMessage, String.class, param, 0);

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
