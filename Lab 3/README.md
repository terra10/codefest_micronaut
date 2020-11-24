# Lab 3 - Adding a database

In this lab we'll be adding a database connection to our application. A H2 in memory database will 
be the most easy so we'll be using that. 

We'll also be using Hibernate to interact with that database and wire it all together in our previously created
Rest Controller.

## Adding jdbc-tomcat and h2

First step will be to add the required dependencies. You can either continue from our 
previous lab, or create a new project. Make sure to add the '--features jdbc-tomact' as argument 
to the mn create-application when creating a new application, or select that feature from the 
launch website. 

This will add the micronaut-jdbc-tomcat to your project. 

When continueing from the previous lab, make sure to add the following dependency:

build.gradle
````
runtime("io.micronaut.sql:micronaut-jdbc-tomcat")
````

pom.xml
````xml
<dependency>
    <groupId>io.micronaut.sql</groupId>
    <artifactId>micronaut-jdbc-tomcat</artifactId>
    <scope>runtime</scope>
</dependency>
````

Next up we need to add our H2 database. You'll need to add the following dependency:

build.gradle
````
runtime("com.h2database:h2")
````

pom.xml
````xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
````

## Configuring the datasource

It fairly easy to setup the basic datasource connection. Similar to Spring Boot, Micronaut will configure it for you
and create a bean that can be inject. All you need to do is add the following lines to the application.yml:

````yaml
datasources:
    default:
        url: jdbc:h2:mem:default;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
        username: sa
        password: ""
        driverClassName: org.h2.Driver
````

This will connect to a in memory H2 database. 
The 'datasources' can contain multiple configurations. Each of them can be named by changing the
'datasources.{name}'.

For example, to add a datasource named 'meetup':
````yaml
datasources:
    meetup:
        ...
````

We can use the @Inject annotation to use the datasource in our code:

````java
@Inject DataSource dataSource // "default" will be injected
@Inject @Named("meetup") DataSource dataSource // "meetup" will be injected
````

We will however not be using the datasource directly, but instead us Hibernate to interact with our database objects.

## Setting up Hibernate

[Hibernate](https://hibernate.org/) is an ORM framework that will allow us to use Java POJO's to interact
with our database without the need to write queries. For now, that's all you need to know. Micronaut offers seamless
integration with Hibernate by adding some dependencies and some lines to our config file.

We need to add a quite a lot of dependencies, so the next listings will include the full list of depencies needed.

build.gradle
````groovy
dependencies {
    annotationProcessor("javax.persistence:javax.persistence-api:2.2")
    annotationProcessor("io.micronaut.data:micronaut-data-processor")
    implementation("javax.annotation:javax.annotation-api")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.h2database:h2")
}
````

pom.xml - dependencies
````xml
<dependencies>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-inject</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-validation</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-http-server-netty</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-http-client</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut</groupId>
      <artifactId>micronaut-runtime</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>javax.annotation</groupId>
      <artifactId>javax.annotation-api</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-jdbc-hikari</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.data</groupId>
      <artifactId>micronaut-data-hibernate-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.sql</groupId>
      <artifactId>micronaut-hibernate-jpa</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>com.h2database</groupId>
      <artifactId>h2</artifactId>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>io.micronaut.test</groupId>
      <artifactId>micronaut-test-junit5</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
````
pom.xml - plugins
````xml
  <build>
    <plugins>
      <plugin>
        <groupId>io.micronaut.build</groupId>
        <artifactId>micronaut-maven-plugin</artifactId>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <!-- Uncomment to enable incremental compilation -->
          <!-- <useIncrementalCompilation>false</useIncrementalCompilation> -->
          <annotationProcessorPaths combine.children="append">
            <path>
              <groupId>io.micronaut.data</groupId>
              <artifactId>micronaut-data-processor</artifactId>
              <version>${micronaut.data.version}</version>
            </path>
          </annotationProcessorPaths>
          <compilerArgs>
            <arg>-Amicronaut.processing.group=micronaut</arg>
            <arg>-Amicronaut.processing.module=meetup</arg>
          </compilerArgs>
        </configuration>
      </plugin>
    </plugins>
  </build>
````


Before we can finish the setup, we need to create a simple Entity class that we can store and retrieve from the database.
Let's create a 'model' package first, and then add a Model.java class in it:

````java
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
````

Few things that are important here:

1. The @javax.persistence.Entity annotation. This indicates that this class represents a database model
1. The @javax.persistence.Id annotation. This flags a field or getter as a unique and primary key in the database.

You can expand the Model class to your liking, just make sure to include an @Id field.

We can now finish our configuration by adding the following lines to our application.yml:

````yml
jpa:
  default:
    entity-scan:
      packages:
        - 'micronaut.meetup.model'
    properties:
      hibernate:
        hbm2ddl:
          auto: update
        show_sql: true
````

This will:
 - configure hibernate to look in the 'micronaut.meetup.model' package for any classes with the @Entity annotation.
 - set hibernate to update/create the database on startup
 - show us the sql executed in the console, for debugging purposes.

## Create a service

Next up, we need a service that will interact with our Hibernate model and datasource. To keep it simple, we'll call it
JpaService. 

In this service we will use the Jpa EntityManager directly to store and retrieve our Model objects:

The class itself should be annotated with @Singleton to mark it as a bean that we can inject.

Each method that interacts with our entityManager should be marked as @Transactional. This will ensure that we run thread-safe.
When we do not add it, the application will start throwing exceptions at us.

Finally, we can simply add a constructor that wil set our EntityManager. No annotations needed. Micronaut wil find  
valid beans and inject them automatically.

The final JpaService should look something like:

````java
package micronaut.meetup;

import micronaut.meetup.model.Model;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Singleton
public class JpaService {

    EntityManager entityManager;

    public JpaService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Model getModel(String id){
        return entityManager.find(Model.class, id);
    }

    @Transactional
    public void saveModel(String name){
        Model model = new Model();
        model.setId(name);
        entityManager.merge(model);
    }

}

````

## Wire into our webserver

Last thing to do is to actually wire it all together. As with the EntityManager we don't need any annotations
in the RestController:

````java
@Controller("/restController")
public class RestController {

    private JpaService jpaService;

    public RestController(JpaService jpaService) {
        this.jpaService = jpaService;
    }
//
}
````

After that, we can change both our GET and POST methods to call the JpaService. Make sure to 
set both to produce and accept application/json. If you want to got the extra mile, add a pathVariable
for the id of the model and use a HttpResponse<Model> on the GET:

````java
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

````

## Disable tests for now

For now, we'll disable or remove the test classes. You can try to update them to work with 
our new setup. For the sake of this meetup it is far easier and quicker to just remove them.

## Run It

If you have not ran the application in between steps, now is the moment!
First thing you'll see when you run it, is logging from Hikari and Hibernate. If those
show up, all good. If not, something broke...

You can now also try both the GET and POST endpoints. Don't forget to update your url to include the pathVariable.
Also note that the response of the GET is nicely mapped to a json object of your Model. Micronaut wil do this for you without
any configuration needed.

