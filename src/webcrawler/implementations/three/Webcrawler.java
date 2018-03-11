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
import java.util.Map;

/**
 * @author Max Rupplin
 */
public class Webcrawler implements Runnable
{
    public Registrar registrar = new Registrar(this);

    public Preinitializer preinitializer = null;

    public Initializer initializer = null;

    public ModuleOne moduleone = null;

    public ModuleTwo moduletwo = null;

    public ModuleThree modulethree = null;
    
    //
    
    public static Map<String, Object> modules = new HashMap();

    public static ArrayList<String> readyanchorlinks = new ArrayList();

    public static ArrayList<String> visitedsitelinks = new ArrayList();
    
    public static ArrayList<String> visitedresourcelinks = new ArrayList();
    
    //
    
    public static final Integer LOCAL_RECURSE_DEPTH = 1;
    
    public static final Integer GLOBAL_RECURSE_DEPTH = 1;
    
    //
    
    public static final String BASEDIR = Utils.dofileseparatornormalization("/home/abasatho/sprug");
            
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
                if(object==null)
                {
                    object = _class.newInstance();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();

                return;
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