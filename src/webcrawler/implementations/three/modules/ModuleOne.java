package webcrawler.implementations.three.modules;

import webcrawler.common.WebcrawlerParam;
import webcrawler.exceptions.StackDepthException;
import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.three.threading.WorkerThread;
import webcrawler.implementations.three.utils.FileUtils;
import webcrawler.implementations.three.utils.NetUtils;
import webcrawler.implementations.three.utils.ParseUtils;

import java.util.ArrayList;

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
     * @param param
     */
    public void offer(WebcrawlerParam param)
    {
        switch(count%this.threadcount)
        {
            case 11:

                //synchronized (threads.get(11).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(11).queue.offer(param);

                    //threads.get(11).queue.notify();
                //}

                break;

            case 10:

                //synchronized (threads.get(10).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(10).queue.offer(param);

                    //threads.get(10).queue.notify();
                //}

                break;

            case 9:

                //synchronized (threads.get(9).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(9).queue.offer(param);

                    //threads.get(9).queue.notify();
                //}

                break;

            case 8:

                //synchronized (threads.get(8).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(8).queue.offer(param);

                    //threads.get(8).queue.notify();
                //}

                break;

            case 7:

                //synchronized (threads.get(7).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(7).queue.offer(param);

                    //threads.get(7).queue.notify();
                //}

                break;

            case 6:

                //synchronized (threads.get(6).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(6).queue.offer(param);

                    //threads.get(6).queue.notify();
                //}

                break;

            case 5:

                //synchronized (threads.get(5).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(5).queue.offer(param);

                    //threads.get(5).queue.notify();
                //}

                break;

            case 4:

                //synchronized (threads.get(4).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(4).queue.offer(param);

                    //threads.get(4).queue.notify();
                //}

                break;

            case 3:

                //synchronized (threads.get(3).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(3).queue.offer(param);

                    //threads.get(3).queue.notify();
                //}

                break;

            case 2:

                //synchronized (threads.get(2).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(2).queue.offer(param);

                    //threads.get(2).queue.notify();
                //}

                break;

            case 1:

                //synchronized (threads.get(1).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(1).queue.offer(param);

                    //threads.get(1).queue.notify();
                //}

                break;

            case 0:

                //synchronized (threads.get(0).queue)
                //{
                    System.err.println("URL enqueue event for : "+param.HREF);

                    threads.get(0).queue.offer(param);

                    //threads.get(0).queue.notify();
                //}

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
