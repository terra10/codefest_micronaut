package micronaut.meetup;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import micronaut.meetup.model.Model;

@Controller("/restController")
public class RestController {

    private JpaService jpaService;

    public RestController(JpaService jpaService) {
        this.jpaService = jpaService;
    }

    @Get(uri="/{id}", produces="application/json")
    public HttpResponse<Model> index(@PathVariable("id") String id) {
        Model model = jpaService.getModel(id);
        if (model == null) {
            return HttpResponse.noContent();
        } else {
            return HttpResponse.ok(model);
        }
    }

    @Post(uri = "/{id}", consumes = "application/json", produces = "application/json")
    public HttpResponse<String> create(@PathVariable("id") String id) {
        jpaService.saveModel(id);
        return HttpResponse.accepted();
    }


}
