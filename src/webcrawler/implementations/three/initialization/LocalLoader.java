package webcrawler.implementations.three.initialization;

import webcrawler.implementations.three.utils.NetUtils;

public class LocalLoader implements Runnable
{
    public LocalLoader()
    {

    }

    @Override
    public void run()
    {
        NetUtils.doenqueuemanualsites();
    }
}
