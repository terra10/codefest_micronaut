# Lab 6 - Advanced Bean Control

In this lab we will take a look a more advanced options to controlling bean injection and 
life-cycle management of beans. 

## Bean Factories

There are moment where you want to expose one or more beans that require you to instantiate them with one or more
parameters. Let's assume we have a bean called Meetup like this:

````java
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
````

We might want to expose this bean as a Singleton to be injected in our Controller. We cannot use the @Singleton annotation
here, as we need a value for 'name'. We can use a Bean Factory to do this for us. Let's create a MeetupFactory:

````java
@Factory
public class MeetupFactory {
    //
}
````

Then, we can create a method annotated with @Singleton to create and return our Meetup:

````java
    @Singleton
    public Meetup meetup(){
        return new Meetup("Meetup!");
    }
````

We can now Inject our Meetup class just like any other Singleton. For example, we can add it to our RestController:

````java

    private JpaService jpaService;
    private MeetupConfiguration meetupConfiguration;
    private Meetup meetup;

    public RestController(JpaService jpaService, MeetupConfiguration meetupConfiguration, Meetup meetup) {
        this.jpaService = jpaService;
        this.meetupConfiguration = meetupConfiguration;
        this.meetup = meetup;
    }
````

And add it to one of our endpoints to return the name we set previously;

````java
    @Get(uri = "/property/meetup", processes = "text/plain")
    public String meetup() {
        return meetup.getName();
    }
````

Build and run it, and head over to http://localhost:8080/restController/property/meetup:
````
Meetup!
````

## Conditional Beans

Now assume that we are running in a cloud environment, with properties being provided via an external source. We might 
want to create some beans only when a certain property is set. We can control bean and singleton creation
by using the @Requires annotation together with the @Singleton annotation. 

Add the following to the meetup method in our MeetupFactory:

````java
    @Singleton
    @Requires(property = "meetup.today")
    public Meetup meetup(){
        return new Meetup("Meetup!");
    }
````

When we now try to call our endpoint, we should see a NoSuchBeanException. Micronaut could not find the instance of 
Meetup when resolving our API call. Let's fix it and add the meetup.today property to our application.yml:

````yaml

meetup:
  hello-world: 'Hello World'
  today: true
````

Rebuild and run, and head over to http://localhost:8080/restController/property/meetup:
````
Meetup!
````

The @Requires annotation also supports the option to require a certain class to be loaded. We can for example annotated our
RestController to require our JpaService.class. As you've seen, injection is only evaluated on the first call to the bean. 
By using @Requires we can control this when booting the application and thus earlier in the life-cycle of a bean.

These are only some of the most used options to control beans, more information can be found in the [documentation](https://docs.micronaut.io/latest/guide/index.html#ioc)
and will largely follow javaEE/Jakarta standards.
