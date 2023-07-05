package net.erdfelt;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

public class ArgsDump
{
    public static void main(String[] args)
    {
        System.getProperties().entrySet().stream()
            .filter(entry -> !entry.getKey().toString().matches("^(java|jdk|sun|user|file|os|line|native|path)\\..*"))
            .sorted(Comparator.comparing(e -> e.getKey().toString()))
            .forEach(entry -> System.err.printf("System Property[%s]: [%s]%n", entry.getKey(), entry.getValue()));

        System.out.printf("args.length=%d%n", args.length);

        int i = 0;
        for (String arg : args)
        {
            System.out.printf("args[%d]: [%s]%n", i++, arg);
            if (arg.endsWith(".properties"))
            {
                dumpProperties(arg);
            }
        }
    }

    private static void dumpProperties(String arg)
    {
        Path propPath = Path.of(arg);
        if (!Files.exists(propPath))
        {
            System.out.printf(" !! Path does not exist: %s%n", propPath);
            return;
        }

        if (!Files.isRegularFile(propPath))
        {
            System.out.printf(" !! Path is not a file: %s%n", propPath);
            return;
        }

        try (InputStream inputStream = Files.newInputStream(propPath))
        {
            Properties props = new Properties();
            props.load(inputStream);
            //noinspection unchecked
            List<String> keys = (List<String>)Collections.list(props.propertyNames());
            Collections.sort(keys);
            for (String key : keys)
            {
                System.out.printf("  > [%s]: [%s]%n", key, props.getProperty(key));
            }
        }
        catch (IOException e)
        {
            System.out.printf(" !! Unable to read properties: %s%n", propPath);
            e.printStackTrace();
        }
    }
}
