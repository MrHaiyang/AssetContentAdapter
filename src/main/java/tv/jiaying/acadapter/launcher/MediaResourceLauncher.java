package tv.jiaying.acadapter.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import tv.jiaying.acadapter.processor.MediaResourceProcessor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 媒资文件处理启动器
 */
@Component
@ConfigurationProperties(prefix = "adapter.processor.media")
public class MediaResourceLauncher implements AbstractLauncher {

    private static Logger logger = LoggerFactory.getLogger(MediaResourceLauncher.class);

    //设置轮询时间
    private long timeInterval;
    //media文件位置
    private String location;
    //开启轮询 读取media文件
    private Boolean enable;
    //最大线程数
    private int maxNumber;

    @Resource
    MediaResourceProcessor mediaResourceProcessor;

    private static final String READY_FILE_PREFIX = "go_";

    @Override
    @PostConstruct
    public void run() {

        ExecutorService executorService = Executors.newFixedThreadPool(maxNumber);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (enable) {

                    try {
                        File locationDirectory = new File(location);
                        if (!locationDirectory.exists() || !locationDirectory.isDirectory()) {
                            locationDirectory.mkdirs();
                        }

                        File[] files = locationDirectory.listFiles();
                        for (File file : files) {

                            //logger.info("file.name:{},size:{}",file.getName(),file.length());

                            //子文件夹不解析,做备份和出错存储用
                            if (!file.isFile()) {
                                //logger.info("file {} is not file",file.getName());
                                continue;
                            }

                            if(!file.getName().trim().startsWith(READY_FILE_PREFIX)){
                                //logger.info("file not start with go:{}",file.getName());
                                continue;
                            }

                            executorService.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mediaResourceProcessor.handle(file.getAbsolutePath());
                                    } catch (Throwable t) {
                                        logger.error(t.getMessage(), t);
                                    }
                                }
                            });

                        }
                    } catch (Exception e) {
                        logger.info(e.getMessage(), e);
                    }

                    try {
                        Thread.sleep(timeInterval);
                    } catch (InterruptedException e) {
                        logger.info(e.getMessage(), e);
                    }
                }

            }
        });
        thread.setName("MediaResourceLauncher");
        thread.start();
    }

    public long getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }
}
