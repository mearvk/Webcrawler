package webcrawler.implementations.three.modules;

import webcrawler.common.WebcrawlerParam;
import webcrawler.exceptions.StackDepthException;
import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.three.initialization.Initializer;
import webcrawler.implementations.three.threading.WorkerThread;
import webcrawler.implementations.three.utils.FileUtils;
import webcrawler.implementations.three.utils.NetUtils;
import webcrawler.implementations.three.utils.ParseUtils;

import java.net.URI;
import java.util.ArrayList;

public class ModuleOne extends webcrawler.common.ModuleOne implements Runnable
{
    public final Integer THREADCOUNT = 12;

    //pull the website recursively

    WorkerThread wthread_001 = new WorkerThread(this,"Thread 01");

    WorkerThread wthread_002 = new WorkerThread(this,"Thread 02");

    WorkerThread wthread_003 = new WorkerThread(this,"Thread 03");

    WorkerThread wthread_004 = new WorkerThread(this,"Thread 04");

    WorkerThread wthread_005 = new WorkerThread(this,"Thread 05");

    WorkerThread wthread_006 = new WorkerThread(this,"Thread 06");

    WorkerThread wthread_007 = new WorkerThread(this,"Thread 07");

    WorkerThread wthread_008 = new WorkerThread(this,"Thread 08");

    WorkerThread wthread_009 = new WorkerThread(this,"Thread 09");

    WorkerThread wthread_010 = new WorkerThread(this,"Thread 10");

    WorkerThread wthread_011 = new WorkerThread(this,"Thread 11");

    WorkerThread wthread_012 = new WorkerThread(this,"Thread 12");

    //

    public ModuleOne()
    {
        this.wthread_001.start();

        this.wthread_002.start();

        this.wthread_003.start();

        this.wthread_004.start();

        this.wthread_005.start();

        this.wthread_006.start();

        this.wthread_007.start();

        this.wthread_008.start();

        this.wthread_009.start();

        this.wthread_010.start();

        this.wthread_011.start();

        this.wthread_012.start();
    }

    //

    public void run()
    {
        ArrayList<String> websites = (ArrayList<String>)((Initializer) Webcrawler.modules.get("initializer")).variables.get("websites");

        //

        for(int i=0; i<websites.size(); i++)
        {
            WebcrawlerParam param = new WebcrawlerParam();

            //

            try
            {
                URI uri;

                uri = new URI(websites.get(i));

                uri = uri.normalize();

                //

                param.url = uri.toString();

                param.href = uri.toString();

                param.baseurl = ParseUtils.parseforbaseurl(websites.get(i));

                //

                if(param.baseurl==null) continue;

                //

                this.dolocalsiterecurse(param, Webcrawler.LOCAL_RECURSE_DEPTH);
            }
            catch(Exception e)
            {

            }
        }
    }

    //
    //@Description("Extension Safe")
    public synchronized String dolocalsiterecurse(WebcrawlerParam param, Integer depth)
    {
        ArrayList<String> anchorset = new ArrayList<String>();

        //

        try
        {
            NetUtils.dorequestandstoresite(param);

            NetUtils.dorequestandstoreanchors(param, anchorset, 0);

            NetUtils.dorequestandstorespecializedanchors(param, anchorset);

            //

            for(int i=0; i<anchorset.size(); i++)
            {
                System.err.println("Site \""+param.url +"\" anchor member #"+i+" "+anchorset.get(i));
            }

            //

            for(int i=0; i<anchorset.size(); i++)
            {
                WebcrawlerParam recursiveparam = new WebcrawlerParam();

                try
                {
                    recursiveparam.href = anchorset.get(i);

                    recursiveparam.url = param.url;

                    recursiveparam.baseurl = ParseUtils.parseforbaseurl(param.url);

                    //

                    switch(i%this.THREADCOUNT)
                    {
                        case 11:

                            synchronized (wthread_012.queue)
                            {
                                wthread_012.queue.offer(recursiveparam);

                                break;
                            }

                        case 10:

                            synchronized (wthread_011.queue)
                            {
                                wthread_011.queue.offer(recursiveparam);

                                break;
                            }

                        case 9:

                            synchronized (wthread_010.queue)
                            {
                                wthread_010.queue.offer(recursiveparam);

                                break;
                            }

                        case 8:

                            synchronized (wthread_009.queue)
                            {
                                wthread_009.queue.offer(recursiveparam);

                                break;
                            }

                        case 7:

                            synchronized (wthread_008.queue)
                            {
                                wthread_008.queue.offer(recursiveparam);

                                break;
                            }

                        case 6:

                            synchronized (wthread_007.queue)
                            {
                                wthread_007.queue.offer(recursiveparam);

                                break;
                            }

                        case 5:

                            synchronized (wthread_006.queue)
                            {
                                wthread_006.queue.offer(recursiveparam);

                                break;
                            }

                        case 4:

                            synchronized (wthread_005.queue)
                            {
                                wthread_005.queue.offer(recursiveparam);

                                break;
                            }

                        case 3:

                            synchronized (wthread_004.queue)
                            {
                                wthread_004.queue.offer(recursiveparam);

                                break;
                            }

                        case 2:

                            synchronized (wthread_003.queue)
                            {
                                wthread_003.queue.offer(recursiveparam);

                                break;
                            }

                        case 1:

                            synchronized (wthread_002.queue)
                            {
                                wthread_002.queue.offer(recursiveparam);

                                break;
                            }

                        case 0:

                            synchronized (wthread_001.queue)
                            {
                                wthread_001.queue.offer(recursiveparam);

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

            System.out.println("Anchor enqueue complete for site: "+param.href);
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

    //
    //
    public synchronized String doglobalsiterecurse(WebcrawlerParam param, Integer depth)
    {
        ArrayList<String> anchorset = new ArrayList<String>();

        //

        try
        {
            NetUtils.dorequestandstoresite(param);

            NetUtils.dorequestandstoreanchors(param, anchorset, 0);

            NetUtils.dorequestandstorespecializedanchors(param, anchorset);
        }
        catch(Exception e)
        {
            System.err.println("ModuleOne.doglobalsiterecurse :: "+e.getMessage());
        }

        //

        for(int i=0; i<anchorset.size(); i++)
        {
            System.err.println("Site \""+param.url +"\" anchor member #"+i+" "+anchorset.get(i));
        }

        //

        for(int i=0; i<anchorset.size(); i++)
        {
            WebcrawlerParam singlesiteparam;

            singlesiteparam = new WebcrawlerParam();

            singlesiteparam.href = anchorset.get(i);
        }


        System.gc();

        //

        return "success";
    }

    //
    //
    public synchronized String dorecurse(WebcrawlerParam param, Integer depth) throws Exception
    {
        ArrayList<String> anchors = param.siteAnchors;

        ArrayList<String> errors = null;

        //

        if(anchors==null || anchors.isEmpty())
        {
            System.out.println("Site "+param.url +" had no links of any kind!");

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

                recursiveparam.url = param.url;

                recursiveparam.href = ParseUtils.parseanchorforhrefattributevalue(anchor);

                if(recursiveparam.href==null || recursiveparam.href.isEmpty()) continue;

                recursiveparam.href = FileUtils.dodeterminefullpathforhttpreference(param, recursiveparam.href);

                //

                if(recursiveparam.href==null) continue;

                if(!recursiveparam.href.contains("http")) continue;

                //

                if(Webcrawler.visitedsitelinks.add(recursiveparam.href)==Boolean.TRUE)
                {
                    FileUtils.dopersistsiteurlasvisited(recursiveparam.href); //

                    System.out.println("Global recursion has visited "+ Webcrawler.visitedsitelinks.size()+" site(s).");
                }
                else continue;

                //

                recursiveparam.url = param.url;

                recursiveparam.anchor = anchor;

                //

                System.out.println("ModuleOne:dorecurse has href value: "+recursiveparam.href);

                System.out.println("ModuleOne:dorecurse has url value: "+recursiveparam.url);

                //

                if(depth>= Webcrawler.GLOBAL_RECURSE_DEPTH)
                {
                    throw new StackDepthException("Global stack depth exceeded; returning.");
                }

                //

                recursiveparam.html = NetUtils.dorequestandstoresite(recursiveparam);

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
