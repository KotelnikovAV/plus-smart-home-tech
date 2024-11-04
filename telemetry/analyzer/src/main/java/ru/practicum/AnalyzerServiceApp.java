package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import ru.practicum.hub.starter.HubEventStarter;
import ru.practicum.sensor.starter.AnalyzerSnapshotsStarter;

@SpringBootApplication
@ConfigurationPropertiesScan("ru.practicum.configuration")
public class AnalyzerServiceApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerServiceApp.class, args);
        AnalyzerSnapshotsStarter analyzerSnapshotsStarter = context.getBean(AnalyzerSnapshotsStarter.class);
        HubEventStarter hubEventStarter = context.getBean(HubEventStarter.class);
        Thread hubEventsThread = new Thread(hubEventStarter);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();
        analyzerSnapshotsStarter.start();
    }
}
