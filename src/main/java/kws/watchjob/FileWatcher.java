package kws.watchjob;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;


public class FileWatcher {

    private final Path pathToWatch;
    private FIleJob fileJob;

    public FileWatcher(String filePath) {
        pathToWatch = Paths.get(filePath);
    }

    public synchronized void register(FileJobImpl fileJob) {
        this.fileJob = fileJob;
    }

    public void watch() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        pathToWatch.register(watchService,
                             StandardWatchEventKinds.ENTRY_CREATE,
                             StandardWatchEventKinds.ENTRY_MODIFY);

        Set<Path> pathsUpdated = new HashSet<>();
        for (;;) {
            pathsUpdated.clear();

            WatchKey watchKey = watchService.poll(500, TimeUnit.MILLISECONDS);
            if (null == watchKey) {
                continue;
            }

            if (!watchKey.isValid()) {
                System.err.println("WatchKey is not valid");
                break;
            }

            List<WatchEvent<?>> events = watchKey.pollEvents();
            for (WatchEvent<?> event: events) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == OVERFLOW) {
                    continue;
                }

                System.err.println("KIND: " + kind);
                Path path = (Path) event.context();
                pathsUpdated.add(path);
            }
            notifyJob(new HashSet(pathsUpdated));

            boolean reset = watchKey.reset();
            if (!reset) {
                System.err.println("WatchKey can not be reset");
                break;
            }
        }
    }

    private void notifyJob(Set<Path> filesUpdated) {
        if (null != fileJob) {
            for (Path path: filesUpdated) {
                fileJob.onUpdate(path);
            }
        }
    }

}
