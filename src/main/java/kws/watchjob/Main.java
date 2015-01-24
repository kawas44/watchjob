package kws.watchjob;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException,
                                                  InterruptedException {
        FileWatcher fileWatcher = new FileWatcher("/tmp/watch");
        FileJobImpl fileJob = new FileJobImpl();
        fileWatcher.register(fileJob);

        System.err.println("Start watcher");
        fileWatcher.watch();
    }

}
