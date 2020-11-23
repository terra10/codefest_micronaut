package micronaut.meetup.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Model {

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return id;
    }
}
