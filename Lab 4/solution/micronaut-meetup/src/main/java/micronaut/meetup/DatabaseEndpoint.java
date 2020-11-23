package micronaut.meetup;

import io.micronaut.http.annotation.Body;
import io.micronaut.management.endpoint.annotation.Endpoint;
import io.micronaut.management.endpoint.annotation.Read;
import io.micronaut.management.endpoint.annotation.Sensitive;
import io.micronaut.management.endpoint.annotation.Write;

import javax.sql.DataSource;
import java.sql.SQLException;

@Endpoint(id = "database", defaultSensitive = false)
public class DatabaseEndpoint {

    private DataSource dataSource;

    public DatabaseEndpoint(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Read
    public boolean dataSource() throws SQLException {
        return !dataSource.getConnection().isClosed();
    }

    @Write(consumes = "application/json")
    public String write(@Body String message){
       return message;
    }
}


