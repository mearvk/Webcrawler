package webcrawler.implementations.three.initialization;

import webcrawler.registration.Registrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Max Rupplin
 */
public class Initializer extends webcrawler.intialization.Initializer implements Runnable
{
    public Registrar registrar = new Registrar();

    //
    
    public Map<String, Object> variables = new HashMap();

    public ArrayList<String> websites = new ArrayList();

    //
    
    public Initializer()
    {
        this.registrar.register(WebsiteListLoader.class);
    }
    
    public void initialize()
    {
        variables.put("initializer", this);
    }

    @Override
    public void run()
    {
        for(Class _class : registrar.classes)
        {
            Object object = null;

            Runnable runner = null;

            try
            {
                object = _class.newInstance();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return;
            }

            if (object instanceof Runnable)
            {
                runner = (Runnable) object;
            }

            if (runner != null)
            {
                runner.run();
            }
            else
            {
                System.out.println("No module object was found for class " + _class.getName());
            }
        }
    }
}
