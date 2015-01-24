package kws.watchjob;

import java.nio.file.Path;


public class FileJobImpl implements FileJob {

    @Override
    public void onUpdate(Path path) {
        System.err.println("  onUpdate for file:  " + path);
    }

}
