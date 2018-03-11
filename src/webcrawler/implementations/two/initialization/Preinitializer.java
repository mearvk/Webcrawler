/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.implementations.two.initialization;

import webcrawler.registration.Registrar;

/**
 *
 * @author Max Rupplin (sr scientologist)
 */
public class Preinitializer extends webcrawler.intialization.Preinitializer implements Runnable
{
    public Registrar registrar = null;

    //

    public Preinitializer()
    {
        this.registrar.register(WebsiteListLoader.class);
    }

    //

    public void preinitialize()
    {

    }

    //

    public void run()
    {
        for(Class _class : registrar.classes)
        {
            Object object = null;

            Runnable runner=null;

            try
            {
                if(object==null)
                    object = _class.newInstance();
            }
            catch(Exception e)
            {
                e.printStackTrace(); return;
            }

            if(object instanceof Runnable)
            {
                runner = (Runnable)object;
            }

            if(runner!=null)
            {
                runner.run();
            }
            else
            {
                System.out.println("No module object was found for class "+_class.getName());
            }
        }
    }
}

