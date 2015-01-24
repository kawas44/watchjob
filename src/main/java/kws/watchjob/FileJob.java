package kws.watchjob;

import java.nio.file.Path;


public interface FileJob {

    void onUpdate(Path path);

}
