package webcrawler.implementations.three.threading;

public interface ShutdownThread
{
    void inactive(long millis);

    void shutdown();
}
