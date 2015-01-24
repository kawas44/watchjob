package kws.watchjob;

import java.nio.file.Path;


public interface FIleJob {

    void onUpdate(Path path);

}
