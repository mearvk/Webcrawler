package webcrawler.common;

public abstract class ModuleOne
{
    public Object runner;

    public FunctionList functions = new FunctionList();

    //

    public ModuleOne()
    {

    }

    public void setrunner(Object runner)
    {
        this.runner = this.functions.runner = runner;
    }
}
