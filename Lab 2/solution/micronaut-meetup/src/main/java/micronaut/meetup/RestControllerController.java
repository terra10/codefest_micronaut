package micronaut.meetup;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

@Controller("/restController")
public class RestControllerController {

    @Get(uri="/", produces="text/plain")
    public String index() {
        return "Example Response";
    }

    @Post(uri = "/", consumes = "application/json", produces = "application/json")
    public HttpResponse<String> create(@Body String body) {
        return HttpResponse.accepted().body(body);
    }
}
