package webcrawler.implementations.two.modules;

import webcrawler.common.WebcrawlerParam;
import webcrawler.exceptions.StackDepthException;
import webcrawler.implementations.two.Webcrawler;
import webcrawler.implementations.two.initialization.Initializer;
import webcrawler.implementations.two.threading.WorkerThread;
import webcrawler.implementations.two.utils.FileUtils;
import webcrawler.implementations.two.utils.NetUtils;
import webcrawler.implementations.two.utils.ParseUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ModuleOne extends webcrawler.common.ModuleOne implements Runnable
{
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

    //

    public void run()
    {
        ArrayList<String> websites = (ArrayList<String>)((Initializer) Webcrawler.values.get("initializer")).variables.get("websites");

        //

        for(int i=0; i<websites.size(); i++)
        {
            WebcrawlerParam param = new WebcrawlerParam();

            //

            try
            {
                param.baseurl = ParseUtils.parseforbaseurl(websites.get(i));

                param.url = websites.get(i);

                param.href = websites.get(i);

                //
                if (param == null)
                {
                    System.out.println(param.url + " is unsupported or such.");
                }
                else if (i % 10 == 9)
                {
                    wthread_010.queue.offer(param);
                }
                else if (i % 10 == 8)
                {
                    wthread_009.queue.offer(param);
                }
                else if (i % 10 == 7)
                {
                    wthread_008.queue.offer(param);
                }
                else if (i % 10 == 6)
                {
                    wthread_007.queue.offer(param);
                }
                else if (i % 10 == 5)
                {
                    wthread_006.queue.offer(param);
                }
                else if (i % 10 == 4)
                {
                    wthread_005.queue.offer(param);
                }
                else if (i % 10 == 3)
                {
                    wthread_004.queue.offer(param);
                }
                else if (i % 10 == 2)
                {
                    wthread_003.queue.offer(param);
                }
                else if (i % 10 == 1)
                {
                    wthread_002.queue.offer(param);
                }
                else if (i % 10 == 0)
                {
                    wthread_001.queue.offer(param);
                }
            }
            catch(Exception e)
            {
                System.out.println(e);
            }
        }

        //

        wthread_001.start();

        wthread_002.start();

        wthread_003.start();

        wthread_004.start();

        wthread_005.start();

        wthread_006.start();

        wthread_007.start();

        wthread_008.start();

        wthread_009.start();

        wthread_010.start();
    }

    //
    public synchronized String dolocalsiterecurse(WebcrawlerParam param, Integer depth)
    {
        ArrayList<String> anchorset = new ArrayList<String>();

        //

        try
        {
            NetUtils.dorequestandstoresite(param); //initial HTML load

            NetUtils.dorequestandstoreanchors(param, anchorset, 0); //rget anchors

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

                    NetUtils.dorequestandstoresite(recursiveparam);

                    //

                    System.out.println("Site link \""+recursiveparam.href+"\" queried and stored.");
                }
                catch(FileNotFoundException fnfe)
                {
                    System.err.println("ModuleOne.dolocalsiterecurse :: Site/link not found: "+recursiveparam.href);
                }
                catch (Exception e)
                {
                    System.err.println("ModuleOne.dolocalsiterecurse :: "+e.getMessage());
                }
            }
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
    public synchronized String doglobalsiterecurse(WebcrawlerParam param, Integer depth)
    {
        ArrayList<String> anchorset = new ArrayList<String>();

        //

        try
        {
            param.html = NetUtils.dorequestandstoresite(param);

            anchorset = NetUtils.dorequestandstoreanchors(param, null, 0);
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

                if(Webcrawler.visitedsitelinks.get(recursiveparam.href)==null || Webcrawler.visitedsitelinks.get(recursiveparam.href).isEmpty())
                {
                    Webcrawler.visitedsitelinks.put(recursiveparam.href, "visited");

                    FileUtils.dopersistsiteurlasvisited(recursiveparam.href);

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
