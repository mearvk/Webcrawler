package webcrawler.implementations.three;

import webcrawler.implementations.three.initialization.Initializer;
import webcrawler.implementations.three.initialization.Preinitializer;
import webcrawler.implementations.three.modules.ModuleOne;
import webcrawler.implementations.three.modules.ModuleThree;
import webcrawler.implementations.three.modules.ModuleTwo;
import webcrawler.implementations.utils.Utils;
import webcrawler.registration.Registrar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author Max Rupplin
 */
public class Webcrawler implements Runnable
{
    public Registrar registrar = new Registrar();

    public Preinitializer preinitializer = new Preinitializer();

    public Initializer initializer = new Initializer();
    
    //
    
    public static Map<String, Object> values = new HashMap();

    public static HashSet<String> readyanchorlinks = new HashSet();

    public static HashSet<String> visitedsitelinks = new HashSet();
    
    public static HashSet<String> visitedresourcelinks = new HashSet();
    
    //
    
    public static final Integer LOCAL_RECURSE_DEPTH = 10;
    
    public static final Integer GLOBAL_RECURSE_DEPTH = 10;
    
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
        
        webcrawler.initializer.initialize();              
        
        //
        
        Webcrawler.values.put("webcrawler", webcrawler);

        Webcrawler.values.put("preinitializer", webcrawler.preinitializer);

        Webcrawler.values.put("initializer", webcrawler.initializer);

        Webcrawler.values.put("registrar", webcrawler.registrar);
        
        //
        
        webcrawler.run();
    }
    
    public void run()
    {
        for(Class _class : registrar.classes)
        {
            Object object=null; 
            
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