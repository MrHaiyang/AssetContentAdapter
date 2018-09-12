package tv.jiaying.acadapter.launcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import tv.jiaying.acadapter.processor.ADIResourceProcessor;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * adi文件处理启动器
 */
@Component
@ConfigurationProperties(prefix = "adapter.processor.adi")
public class ADIResourceLauncher implements AbstractLauncher {

    private static Logger logger = LoggerFactory.getLogger(ADIResourceLauncher.class);

    //设置轮询时间
    private long timeInterval;
    //adi文件位置
    private String location;
    //开启轮询 读取adi文件
    private Boolean enable;
    //最大线程数
    private int maxNumber;

    @Autowired
    ADIResourceProcessor adiResourceProcessor;

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

                            //子文件夹不解析,做备份用
                            if (!file.isFile()) {
                                continue;
                            }

                            executorService.submit(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        adiResourceProcessor.handle(file.getAbsolutePath());
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
        thread.setName("ADIResourceLauncher");
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
