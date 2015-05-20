package com.mycompany.httpserverclient;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
//import com.example.helloworld.resources.HelloWorldResource;
//import com.example.helloworld.health.TemplateHealthCheck;

public class App extends Application<conf> {
    public static void main(String[] args) throws Exception {
        new App().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<conf> bootstrap) {
        // nothing to do yet
    }

    @Override
    public void run(conf configuration, Environment environment) {
        final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
        environment.healthChecks().register("template", healthCheck);
        
        
        final AppResource resource = new AppResource(configuration.getTemplate(), configuration.getDefaultName());
        environment.jersey().register(resource);

    }

}