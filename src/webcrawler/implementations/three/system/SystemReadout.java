package webcrawler.implementations.three.system;

import java.util.Date;

public class SystemReadout implements Runnable
{
    @Override
    public void run()
    {
        System.err.println("\nBase Web Mirroring Package - "+new Date()+"\n");
    }
}
