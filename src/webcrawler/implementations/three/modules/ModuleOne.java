package webcrawler.implementations.three.modules;

import webcrawler.common.WebcrawlerParam;
import webcrawler.implementations.three.threading.WorkerThread;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ModuleOne extends webcrawler.common.ModuleOne implements Runnable
{
    public Integer count = 0;

    public final Integer threadcount = Runtime.getRuntime().availableProcessors();

    public ArrayList<WorkerThread> threads = new ArrayList<>(threadcount);

    public static Queue<WebcrawlerParam> recursiveparams = new ConcurrentLinkedQueue<>();

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
    public void recursiveoffer(WebcrawlerParam param)
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

                if(threads.get(11).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(11).site_queue.offer(param);

                    return;
                }

                while(threads.get(11).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(11).site_queue.size()>1)
                        {
                            threads.get(11).processqueue();
                        }

                        if(threads.get(11).site_queue.size()<=1)
                        {
                            threads.get(11).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 10:

                if(threads.get(10).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(10).site_queue.offer(param);

                    return;
                }

                while(threads.get(10).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(10).site_queue.size()>1)
                        {
                            threads.get(10).processqueue();
                        }

                        if(threads.get(10).site_queue.size()<=1)
                        {
                            threads.get(10).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 9:

                if(threads.get(9).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(9).site_queue.offer(param);

                    return;
                }

                while(threads.get(9).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(9).site_queue.size()>1)
                        {
                            threads.get(9).processqueue();
                        }

                        if(threads.get(9).site_queue.size()<=1)
                        {
                            threads.get(9).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 8:

                if(threads.get(8).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(8).site_queue.offer(param);

                    return;
                }

                while(threads.get(8).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(8).site_queue.size()>1)
                        {
                            threads.get(8).processqueue();
                        }

                        if(threads.get(8).site_queue.size()<=1)
                        {
                            threads.get(8).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 7:

                if(threads.get(7).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(7).site_queue.offer(param);

                    return;
                }

                while(threads.get(7).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(7).site_queue.size()>1)
                        {
                            threads.get(7).processqueue();
                        }

                        if(threads.get(7).site_queue.size()<=1)
                        {
                            threads.get(7).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 6:

                if(threads.get(6).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(6).site_queue.offer(param);

                    return;
                }

                while(threads.get(6).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(6).site_queue.size()>1)
                        {
                            threads.get(6).processqueue();
                        }

                        if(threads.get(6).site_queue.size()<=1)
                        {
                            threads.get(6).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 5:

                if(threads.get(5).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(5).site_queue.offer(param);

                    return;
                }

                while(threads.get(5).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(5).site_queue.size()>1)
                        {
                            threads.get(5).processqueue();
                        }

                        if(threads.get(5).site_queue.size()<=1)
                        {
                            threads.get(5).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 4:

                if(threads.get(4).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(4).site_queue.offer(param);

                    return;
                }

                while(threads.get(4).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(4).site_queue.size()>1)
                        {
                            threads.get(4).processqueue();
                        }

                        if(threads.get(4).site_queue.size()<=1)
                        {
                            threads.get(4).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 3:

                if(threads.get(3).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(3).site_queue.offer(param);

                    return;
                }

                while(threads.get(3).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(3).site_queue.size()>1)
                        {
                            threads.get(3).processqueue();
                        }

                        if(threads.get(3).site_queue.size()<=1)
                        {
                            threads.get(3).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 2:

                if(threads.get(2).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(2).site_queue.offer(param);

                    return;
                }

                while(threads.get(2).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(2).site_queue.size()>1)
                        {
                            threads.get(2).processqueue();
                        }

                        if(threads.get(2).site_queue.size()<=1)
                        {
                            threads.get(2).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 1:

                if(threads.get(1).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(1).site_queue.offer(param);

                    return;
                }

                while(threads.get(1).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(1).site_queue.size()>1)
                        {
                            threads.get(1).processqueue();
                        }

                        if(threads.get(1).site_queue.size()<=1)
                        {
                            threads.get(1).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 0:

                if(threads.get(0).site_queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(0).site_queue.offer(param);

                    return;
                }

                while(threads.get(0).site_queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(0).site_queue.size()>1)
                        {
                            threads.get(0).processqueue();
                        }

                        if(threads.get(0).site_queue.size()<=1)
                        {
                            threads.get(0).site_queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            default: break;
        }

        count++;
    }
}
