package webcrawler.common;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class FunctionList
{
    public Object runner;

    public Method method;

    //

    public FunctionList()
    {

    }

    public Object run(Object runner, String methodname, Object returnvalue, Class returnvalueclass, Object...parameters)
    {
        try
        {
            Class[] types = new Class[parameters.length];

            for(int a=0; a<types.length; a++)
            {
                types[a] = parameters[a].getClass();
            }

            //

            Method method = null;

            //

            if(types.length==1)
            {
                method = runner.getClass().getMethod(methodname, types[0]);
            }
            else if(types.length==2)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1]);
            }
            else if(types.length==3)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2]);
            }
            else if(types.length==4)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2], types[3]);
            }
            else if(types.length==5)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2], types[3], types[4]);
            }
            else if(types.length==6)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2], types[3], types[4], types[5]);
            }
            else if(types.length==7)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2], types[3], types[4], types[5], types[6]);
            }
            else if(types.length==8)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2], types[3], types[4], types[5], types[6], types[7]);
            }
            else if(types.length==9)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2], types[3], types[4], types[5], types[6], types[7], types[8]);
            }
            else if(types.length==10)
            {
                method = runner.getClass().getMethod(methodname, types[0], types[1], types[2], types[3], types[4], types[5], types[6], types[7], types[8], types[9]);
            }

            //

            if(method!=null)
            {
                if(returnvalueclass.newInstance() instanceof String)
                {
                    returnvalue = (String)method.invoke(runner, parameters);
                }

                if(returnvalueclass.newInstance() instanceof ArrayList)
                {
                    returnvalue = (ArrayList<String>)method.invoke(runner, parameters);
                }

                return returnvalue;
            }
        }
        catch(SecurityException se)
        {
            se.printStackTrace();
        }
        catch(NullPointerException npe)
        {
            npe.printStackTrace();
        }
        catch(NoSuchMethodException nsme)
        {
            System.err.println("No such method: "+nsme.getMessage());
        }
        catch(Exception e)
        {
            if(e instanceof FileNotFoundException)
            {
                System.err.println("File not found: "+e.getMessage());
            }
            else
            {
                e.printStackTrace();
            }
        }

        return null;
    }
}
