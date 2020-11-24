package micronaut.meetup;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("meetup")
public class MeetupConfiguration {

    private String helloWorld;

    public String getHelloWorld() {
        return helloWorld;
    }

    public void setHelloWorld(String helloWorld) {
        this.helloWorld = helloWorld;
    }
}
