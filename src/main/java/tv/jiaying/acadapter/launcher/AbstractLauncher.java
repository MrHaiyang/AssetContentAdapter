package tv.jiaying.acadapter.launcher;

import javax.annotation.PostConstruct;

public interface AbstractLauncher {
    @PostConstruct
    void run();
}
