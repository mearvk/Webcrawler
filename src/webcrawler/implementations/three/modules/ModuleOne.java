package webcrawler.implementations.three.modules;

import webcrawler.common.WebcrawlerParam;
import webcrawler.exceptions.StackDepthException;
import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.three.threading.WorkerThread;
import webcrawler.implementations.three.utils.FileUtils;
import webcrawler.implementations.three.utils.NetUtils;
import webcrawler.implementations.three.utils.ParseUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ModuleOne extends webcrawler.common.ModuleOne implements Runnable
{
    public Integer count = 0;

    public final Integer threadcount = Runtime.getRuntime().availableProcessors();

    public ArrayList<WorkerThread> threads = new ArrayList<>(threadcount);

    /**
     *
     */
    public ModuleOne()
    {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        //

        for(int i = 0; i< threadcount; i++)
        {
            WorkerThread worker = new WorkerThread(this,"Thread "+i);

            //

            threads.add(worker);

            //

            worker.start();
        }
    }

    /**
     *
     */
    public void run()
    {
        return;

        /*ArrayList<String> sitelist = (ArrayList<String>)((Initializer) Webcrawler.modules.get("initializer")).variables.get("predefined");

        //


        for(String site : sitelist)
        {
            WebcrawlerParam param = new WebcrawlerParam();

            //

            try
            {
                URI uri;

                uri = new URI(site);

                uri = uri.normalize();

                //

                param.URL = uri.toString();

                param.HREF = uri.toString();

                param.FULL_DOMAIN_NAME = ParseUtils.dogetfulldomainname(site);

                param.DOMAIN_NAME = ParseUtils.dogetbasedomainname(site);

                //

                if(param.DOMAIN_NAME==null) continue;

                //

                this.dolocalsiterecurse(param, param.LDEPTH);
            }
            catch(Exception e)
            {
                System.err.println(e);
            }
        }
        */
    }

    /**
    *
    */
    protected boolean entryexists(final WebcrawlerParam param)
    {
        for(int i=0; i<this.threads.size(); i++)
        {
            Queue copy = new ConcurrentLinkedQueue<WebcrawlerParam>(this.threads.get(i).queue);

            for(int j=0; j<copy.size(); j++)
            {
                WebcrawlerParam _param = (WebcrawlerParam)copy.poll();

                String compareA = param.HREF.trim();

                String compareB = _param.HREF.trim();

                if(compareA.equalsIgnoreCase(compareB))
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
        //

        if( this.entryexists(param) ) return;

        //

        switch(count%this.threadcount)
        {
            case 11:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(11).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(11).queue.offer(param);

                    break;
                }

                while(threads.get(11).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(11).queue.size()>1)
                        {
                            threads.get(11).processqueue();
                        }

                        if(threads.get(11).queue.size()<=1)
                        {
                            threads.get(11).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 10:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(10).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(10).queue.offer(param);

                    break;
                }

                while(threads.get(10).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(10).queue.size()>1)
                        {
                            threads.get(10).processqueue();
                        }

                        if(threads.get(10).queue.size()<=1)
                        {
                            threads.get(10).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 9:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(9).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(9).queue.offer(param);

                    break;
                }

                while(threads.get(9).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(9).queue.size()>1)
                        {
                            threads.get(9).processqueue();
                        }

                        if(threads.get(9).queue.size()<=1)
                        {
                            threads.get(9).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 8:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(8).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(8).queue.offer(param);

                    break;
                }

                while(threads.get(8).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(8).queue.size()>1)
                        {
                            threads.get(8).processqueue();
                        }

                        if(threads.get(8).queue.size()<=1)
                        {
                            threads.get(8).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 7:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(7).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(7).queue.offer(param);

                    break;
                }

                while(threads.get(7).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(7).queue.size()>1)
                        {
                            threads.get(7).processqueue();
                        }

                        if(threads.get(7).queue.size()<=1)
                        {
                            threads.get(7).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 6:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(6).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(6).queue.offer(param);

                    break;
                }

                while(threads.get(6).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(6).queue.size()>1)
                        {
                            threads.get(6).processqueue();
                        }

                        if(threads.get(6).queue.size()<=1)
                        {
                            threads.get(6).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 5:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(5).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(5).queue.offer(param);

                    break;
                }

                while(threads.get(5).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(5).queue.size()>1)
                        {
                            threads.get(5).processqueue();
                        }

                        if(threads.get(5).queue.size()<=1)
                        {
                            threads.get(5).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 4:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(4).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(4).queue.offer(param);

                    break;
                }

                while(threads.get(4).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(4).queue.size()>1)
                        {
                            threads.get(4).processqueue();
                        }

                        if(threads.get(4).queue.size()<=1)
                        {
                            threads.get(4).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 3:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(3).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(3).queue.offer(param);

                    break;
                }

                while(threads.get(3).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(3).queue.size()>1)
                        {
                            threads.get(3).processqueue();
                        }

                        if(threads.get(3).queue.size()<=1)
                        {
                            threads.get(3).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 2:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(2).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(2).queue.offer(param);

                    break;
                }

                while(threads.get(2).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(2).queue.size()>1)
                        {
                            threads.get(2).processqueue();
                        }

                        if(threads.get(2).queue.size()<=1)
                        {
                            threads.get(2).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 1:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(1).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(1).queue.offer(param);

                    break;
                }

                while(threads.get(1).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(1).queue.size()>1)
                        {
                            threads.get(1).processqueue();
                        }

                        if(threads.get(1).queue.size()<=1)
                        {
                            threads.get(1).queue.offer(param);
                        }
                    }
                    catch(Exception e)
                    {
                        System.err.println(e);
                    }
                }

                break;

            case 0:

                //System.err.println("URL enqueue event for : "+param.HREF);

                if(threads.get(0).queue.size()<10) //9 --> 10, 10 skips
                {
                    threads.get(0).queue.offer(param);

                    break;
                }

                while(threads.get(0).queue.size()>=10)
                {
                    try
                    {
                        while(threads.get(0).queue.size()>1)
                        {
                            threads.get(0).processqueue();
                        }

                        if(threads.get(0).queue.size()<=1)
                        {
                            threads.get(0).queue.offer(param);
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

    /**
     *
     * @param param
     * @param depth
     * @return
     */
    public synchronized String dolocalsiterecurse(WebcrawlerParam param, Integer depth)
    {
        ArrayList<String> anchorset = new ArrayList<String>();

        //

        try
        {
            NetUtils.dorequestandstorepage(param);

            NetUtils.dorequestandstoreanchors(param, anchorset, 0, Integer.MIN_VALUE);

            //

            for(int i=0; i<anchorset.size(); i++)
            {
                System.err.println("Site \""+param.URL +"\" anchor member #"+i+" "+anchorset.get(i));
            }

            //

            for(int i=0; i<anchorset.size(); i++)
            {
                WebcrawlerParam recursiveparam = new WebcrawlerParam();

                try
                {
                    recursiveparam.HREF = anchorset.get(i);

                    recursiveparam.URL = param.URL;

                    recursiveparam.DOMAIN_NAME = ParseUtils.dogetfulldomainname(param.URL);

                    //

                    switch(i%this.threadcount)
                    {
                        case 11:

                            synchronized (threads.get(11).queue)
                            {
                                threads.get(11).queue.offer(recursiveparam);

                                break;
                            }

                        case 10:

                            synchronized (threads.get(10).queue)
                            {
                                threads.get(10).queue.offer(recursiveparam);

                                break;
                            }

                        case 9:

                            synchronized (threads.get(9).queue)
                            {
                                threads.get(9).queue.offer(recursiveparam);

                                break;
                            }

                        case 8:

                            synchronized (threads.get(8).queue)
                            {
                                threads.get(8).queue.offer(recursiveparam);

                                break;
                            }

                        case 7:

                            synchronized (threads.get(7).queue)
                            {
                                threads.get(7).queue.offer(recursiveparam);

                                break;
                            }

                        case 6:

                            synchronized (threads.get(6).queue)
                            {
                                threads.get(6).queue.offer(recursiveparam);

                                break;
                            }

                        case 5:

                            synchronized (threads.get(5).queue)
                            {
                                threads.get(5).queue.offer(recursiveparam);

                                break;
                            }

                        case 4:

                            synchronized (threads.get(4).queue)
                            {
                                threads.get(4).queue.offer(recursiveparam);

                                break;
                            }

                        case 3:

                            synchronized (threads.get(3).queue)
                            {
                                threads.get(3).queue.offer(recursiveparam);

                                break;
                            }

                        case 2:

                            synchronized (threads.get(2).queue)
                            {
                                threads.get(2).queue.offer(recursiveparam);

                                break;
                            }

                        case 1:

                            synchronized (threads.get(1).queue)
                            {
                                threads.get(1).queue.offer(recursiveparam);

                                break;
                            }

                        case 0:

                            synchronized (threads.get(0).queue)
                            {
                                threads.get(0).queue.offer(recursiveparam);

                                break;
                            }

                        default: break;
                    }

                    //
                }
                catch (Exception e)
                {
                    System.err.println("ModuleOne.dolocalsiterecurse :: "+e.getMessage());
                }
            }

            System.out.println("Anchor enqueue complete for site: "+param.HREF);
        }
        catch(Exception e)
        {
            System.err.println("ModuleOne.dolocalsiterecurse :: "+e.getMessage());
        }

        //

        System.gc();

        //

        return "success";
    }

    /**
     *
     * @param param
     * @param depth
     * @return
     */
    public synchronized String doglobalsiterecurse(WebcrawlerParam param, Integer depth)
    {
        ArrayList<String> anchorset = new ArrayList<String>();

        //

        try
        {
            NetUtils.dorequestandstoresite(param);

            NetUtils.dorequestandstoreanchors(param, anchorset, 0, -1);
        }
        catch(Exception e)
        {
            System.err.println("ModuleOne.doglobalsiterecurse :: "+e.getMessage());
        }

        //

        for(int i=0; i<anchorset.size(); i++)
        {
            System.err.println("Site \""+param.URL +"\" anchor member #"+i+" "+anchorset.get(i));
        }

        //

        for(int i=0; i<anchorset.size(); i++)
        {
            WebcrawlerParam singlesiteparam;

            singlesiteparam = new WebcrawlerParam();

            singlesiteparam.HREF = anchorset.get(i);
        }


        System.gc();

        //

        return "success";
    }

    /**
     *
     * @param param
     * @param depth
     * @return
     * @throws Exception
     */
    public synchronized String dorecurse(WebcrawlerParam param, Integer depth) throws Exception
    {
        ArrayList<String> anchors = param.siteAnchors;

        ArrayList<String> errors = null;

        //

        if(anchors==null || anchors.isEmpty())
        {
            System.out.println("Site "+param.URL +" had no links of any kind!");

            return null;
        }
        else
        {
            for(int i=0; i<anchors.size(); i++)
            {
                String href = ParseUtils.parseanchorforhrefattributevalue(anchors.get(i));
            }
        }

        //

        for(int i=0; i<anchors.size(); i++)
        {
            WebcrawlerParam recursiveparam = new WebcrawlerParam();

            String anchor = anchors.get(i);

            if(anchor==null || anchor.isEmpty()) continue;

            try
            {
                //

                recursiveparam.URL = param.URL;

                recursiveparam.HREF = ParseUtils.parseanchorforhrefattributevalue(anchor);

                if(recursiveparam.HREF ==null || recursiveparam.HREF.isEmpty()) continue;

                recursiveparam.HREF = FileUtils.dodeterminefullpathforhttpreference(param, recursiveparam.HREF);

                //

                if(recursiveparam.HREF ==null) continue;

                if(!recursiveparam.HREF.contains("http")) continue;

                //

                if(Webcrawler.visitedsitelinks.add(recursiveparam.HREF)==Boolean.TRUE)
                {
                    FileUtils.dopersistsiteurlasvisited(recursiveparam.HREF); //

                    System.out.println("Global recursion has visited "+ Webcrawler.visitedsitelinks.size()+" site(s).");
                }
                else continue;

                //

                recursiveparam.URL = param.URL;

                recursiveparam.anchor = anchor;

                //

                System.out.println("ModuleOne:dorecurse has HREF value: "+recursiveparam.HREF);

                System.out.println("ModuleOne:dorecurse has URL value: "+recursiveparam.URL);

                //

                if(depth>= Webcrawler.GLOBAL_RECURSE_DEPTH)
                {
                    throw new StackDepthException("Global stack depth exceeded; returning.");
                }

                //

                recursiveparam.HTML = NetUtils.dorequestandstoresite(recursiveparam);

                recursiveparam.siteAnchors = ParseUtils.doparseanchors(recursiveparam);

                recursiveparam.recurseMessage = this.dorecurse(recursiveparam, (1+depth));
            }
            catch(NullPointerException npe)
            {
                System.err.println("ModuleOne.dorecurse :: "+npe.getMessage());
            }
            catch(StackDepthException vdsde)
            {
                System.err.println("ModuleOne.dorecurse :: "+vdsde.getMessage()); return null;
            }
            catch(Exception e)
            {
                System.err.println("ModuleOne.dorecurse :: "+e.getMessage());
            }
        }

        //

        System.gc();

        //

        return "success";
    }
}
