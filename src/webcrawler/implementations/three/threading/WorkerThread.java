package webcrawler.implementations.three.threading;

import webcrawler.implementations.three.modules.ModuleOne;
import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.utils.NetUtils;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

//
public class WorkerThread extends Thread implements ShutdownThread
{
    public volatile Queue<WebcrawlerParam> site_queue = new ConcurrentLinkedQueue();

    public Object lock = new Object();

    public Boolean running = true;

    public ModuleOne module = null;

    public Integer timeout_threshold = 2*6000000; //2 hours

    public Long time_accrued = 0L;

    public Long wait_millis = 200L;

    /**
     *
     * @param runner
     * @param threadname
     */
    public WorkerThread(ModuleOne runner, String threadname)
    {
        this.module = runner;

        this.setName(threadname);
    }

    /**
     *
     */
    public void run()
    {
        while(running)
        {
            try
            {
                this.sleepily().processqueue();
            }
            catch(Exception e)
            {
                //
            }
        }
    }

    /**
     *
     */
    public void processqueue()
    {
        if (this.site_queue != null && !this.site_queue.isEmpty())
        {
            synchronized (this.site_queue)
            {
                this.time_accrued = 0L;

                //

                try
                {
                    WebcrawlerParam param = site_queue.poll();

                    NetUtils.dorequestandstoresite(param);

                    if (param.LDEPTH > 0)
                    {
                        NetUtils.dorequestandstoreanchors(param, new ArrayList<String>(), 0, param.LDEPTH);
                    }

                    param = null;
                }
                catch (Exception e)
                {
                    //System.err.println(e.getMessage());
                }
                finally
                {
                    System.gc();
                }
            }

            return;
        }

        //

        if( ModuleOne.recursiveparams!=null && !ModuleOne.recursiveparams.isEmpty() )
        {
            synchronized (ModuleOne.recursiveparams)
            {
                this.time_accrued = 0L;

                //

                try
                {
                    WebcrawlerParam param = ModuleOne.recursiveparams.poll();

                    NetUtils.dorequestandstoresite(param);

                    if (param.LDEPTH > 0)
                    {
                        NetUtils.dorequestandstoreanchors(param, new ArrayList<String>(), 0, param.LDEPTH);
                    }

                    param = null;
                }
                catch (Exception e)
                {
                    //System.err.println(e.getMessage());
                }
                finally
                {
                    System.gc();
                }
            }

            return;
        }
    }

    /**
     *
     * @return
     */
    protected WorkerThread sleepily()
    {
        if( this.site_queue !=null && !this.site_queue.isEmpty()) return this;

        //

        try
        {
            synchronized (this)
            {
                this.wait(this.wait_millis);

                this.inactive(this.wait_millis);

                this.notifyAll();
            }
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }

        return this;
    }

    /**
     *
     * @param millis
     */
    @Override
    public void inactive(long millis)
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

    /**
     *
     */
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

        System.err.println("Thread "+this.getName()+" timed out.");
    }
}
