package net.erdfelt;

import java.util.Comparator;

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
        }
    }
}
