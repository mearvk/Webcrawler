package webcrawler.implementations.three.initialization;

import webcrawler.implementations.three.utils.NetUtils;

public class LocalListLoader implements Runnable
{
    public LocalListLoader()
    {

    }

    @Override
    public void run()
    {
        NetUtils.doenqueuelocalsites();
    }
}
