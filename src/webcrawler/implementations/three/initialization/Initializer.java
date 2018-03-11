package webcrawler.implementations.three.initialization;

import webcrawler.registration.Registrar;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Max Rupplin
 */
public class Initializer extends webcrawler.intialization.Initializer implements Runnable
{
    public Registrar registrar = new Registrar();
    
    public Map<String, Object> variables = new HashMap();

    //
    
    public Initializer()
    {
        this.registrar.register(LocalLoader.class);

        //this.registrar.register(RemoteLoader.class);
    }
    
    public void initialize()
    {
        this.variables.put("initializer", this);
    }

    @Override
    public void run()
    {
        for(Class _class : registrar.classes)
        {
            Object object = registrar.classes_objects.get(_class);

            Runnable runner = null;

            try
            {
                if(object==null)
                {
                    object = _class.newInstance();
                }
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
