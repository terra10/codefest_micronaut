package micronaut.meetup;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;

import javax.inject.Singleton;

@Factory
public class MeetupFactory {

    @Singleton
    @Requires(property = "meetup.today")
    public Meetup meetup(){
        return new Meetup("Meetup!");
    }

}
