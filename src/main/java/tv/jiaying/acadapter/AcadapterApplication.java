package tv.jiaying.acadapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tv.jiaying.acadapter.processor.cdn.TransferConfig;

@SpringBootApplication
public class AcadapterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcadapterApplication.class, args);
	}
}
