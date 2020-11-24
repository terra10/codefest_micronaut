package micronaut.meetup;

public class Meetup {

    private final String name;

    public Meetup(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
