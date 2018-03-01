package webcrawler.implementations.three.threading;

public interface ShutdownThread
{
    void doisinactive(long millis);

    void shutdown();
}
