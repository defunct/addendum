package com.goodworkalan.furnish;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Creates a library of implementations available in the
 * class path.
 * 
 * @author Alan Gutierrez
 */
public class Furnish<S> implements Iterable<S>
{
    /** The list of available dialects. */
    private final List<S> services = new ArrayList<S>();

    /**
     * Furnish the given service.
     * 
     * @param service
     *            The service to furnish.
     */
    public Furnish(Class<S> service)
    {
        try
        {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources("META-INF/services/" + service.getName());
            while (resources.hasMoreElements())
            {
                URL url = resources.nextElement();
                InputStream is = url.openStream();
                try
                {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    while (true)
                    {
                        String line = reader.readLine();
                        if (line == null)
                        {
                            break;
                        }
                        int comment = line.indexOf('#');
                        if (comment >= 0)
                        {
                            line = line.substring(0, comment);
                        }
                        String name = line.trim();
                        if (name.length() == 0)
                        {
                            continue;
                        }
                        S instance;
                        try
                        {
                            Class<?> clz = Class.forName(name, true, classLoader);
                            Class<? extends S> impl = clz.asSubclass(service);
                            Constructor<? extends S> ctor = impl.getConstructor();
                            instance = ctor.newInstance();
                        }
                        catch (Exception e)
                        {
                            continue;
                        }
                        services.add(instance);
                    }
                }
                finally
                {
                    is.close();
                }
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    public Iterator<S> iterator()
    {
        return services.iterator();
    }
}

/* vim: set et sw=4 ts=4 ai tw=78 nowrap: */