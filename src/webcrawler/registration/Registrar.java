/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webcrawler.registration;

import webcrawler.implementations.three.Webcrawler;
import webcrawler.implementations.three.initialization.Initializer;
import webcrawler.implementations.three.initialization.LocalListLoader;
import webcrawler.implementations.three.initialization.Preinitializer;
import webcrawler.implementations.three.modules.ModuleOne;
import webcrawler.implementations.three.modules.ModuleThree;
import webcrawler.implementations.three.modules.ModuleTwo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Max Rupplin
 */
public class Registrar
{
    public Webcrawler webcrawler;

    public ArrayList<Class> classes = new ArrayList();

    public HashMap<Class,Object> classes_objects = new HashMap<>();

    //

    public Registrar(Webcrawler webcrawler)
    {
        this.webcrawler = webcrawler;
    }

    public Registrar(){}

    //

    public void register(Class _class)
    {
        this.classes.add(_class);

        try
        {
            if(_class.newInstance() instanceof LocalListLoader)
            {
                LocalListLoader manualloader = (LocalListLoader)_class.newInstance();

                this.classes_objects.put(_class, manualloader);

                Webcrawler.modules.put("manualloader", manualloader);
            }

            if(_class.newInstance() instanceof Preinitializer)
            {
                Preinitializer preinitializer = (Preinitializer)_class.newInstance();

                this.webcrawler.preinitializer = preinitializer;

                this.classes_objects.put(_class, preinitializer);

                Webcrawler.modules.put("preinitializer", preinitializer);
            }

            if(_class.newInstance() instanceof Initializer)
            {
                Initializer initializer = (Initializer)_class.newInstance();

                this.webcrawler.initializer = initializer;

                this.classes_objects.put(_class, initializer);

                Webcrawler.modules.put("initializer", initializer);
            }

            if(_class.newInstance() instanceof ModuleOne)
            {
                ModuleOne moduleone = (ModuleOne)_class.newInstance();

                this.webcrawler.moduleone = (ModuleOne)_class.newInstance();

                this.classes_objects.put(_class, moduleone);

                Webcrawler.modules.put("moduleone", moduleone);
            }

            if(_class.newInstance() instanceof ModuleTwo)
            {
                ModuleTwo moduletwo = (ModuleTwo)_class.newInstance();

                this.webcrawler.moduletwo = (ModuleTwo)_class.newInstance();

                this.classes_objects.put(_class, moduletwo);

                Webcrawler.modules.put("moduletwo", moduletwo);
            }

            if(_class.newInstance() instanceof ModuleThree)
            {
                ModuleThree modulethree = (ModuleThree)_class.newInstance();

                this.webcrawler.modulethree = (ModuleThree)_class.newInstance();

                this.classes_objects.put(_class, modulethree);

                Webcrawler.modules.put("modulethree", modulethree);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();

            System.err.println("No registrar handler found for class: "+_class);
        }
    }

    public void initialize()
    {
        System.out.println("Not yet implemented.");
    }
}
