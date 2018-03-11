package webcrawler.implementations.two;

import webcrawler.implementations.two.initialization.Initializer;
import webcrawler.implementations.two.initialization.Preinitializer;
import webcrawler.implementations.two.modules.ModuleOne;
import webcrawler.implementations.two.modules.ModuleThree;
import webcrawler.implementations.two.modules.ModuleTwo;
import webcrawler.implementations.utils.Utils;
import webcrawler.registration.Registrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Max Rupplin
 */
public class Webcrawler implements Runnable
{
    public Registrar registrar = null;

    public Preinitializer preinitializer = null;

    public Initializer initializer = null;
    
    //
    
    public static Map<String, Object> values = new HashMap();

    public static ArrayList<String> readyanchorlinks = new ArrayList();

    public static Map<String, String> visitedsitelinks = new HashMap();
    
    public static Map<String, String> visitedresourcelinks = new HashMap();
    
    //
    
    public static final Integer LOCAL_RECURSE_DEPTH = 2;
    
    public static final Integer GLOBAL_RECURSE_DEPTH = 4;
    
    //
    
    public static final String BASEDIR = Utils.dofileseparatornormalization("C:\\Users\\mearv\\OneDrive\\Desktop\\Websites\\storage");
            
    //
    
    public static void main(String[] args)
    {
        Webcrawler webcrawler = new Webcrawler();
                
        //
        
        webcrawler.registrar.register(Preinitializer.class);

        webcrawler.registrar.register(Initializer.class);
        
        webcrawler.registrar.register(ModuleOne.class);
        
        webcrawler.registrar.register(ModuleTwo.class);
        
        webcrawler.registrar.register(ModuleThree.class);
        
        //

        webcrawler.preinitializer.preinitialize();

        webcrawler.initializer.initialize();
        
        //
        
        webcrawler.run();
    }
    
    public void run()
    {
        for(Class _class : registrar.classes)
        {
            Object object = registrar.classes_objects.get(_class);
            
            Runnable runner=null;
            
            try
            {
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