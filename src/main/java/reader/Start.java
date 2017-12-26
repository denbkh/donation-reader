package reader;

import javax.servlet.http.Cookie;

import com.google.appengine.api.datastore.*;
import ro.pippo.core.Application;
import ro.pippo.freemarker.FreemarkerTemplateEngine;

public class Start extends Application {
    public Start() {
        setTemplateEngine(new FreemarkerTemplateEngine());
    }

    @Override
    protected void onInit() {

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        GET("/", routeContext -> {

//            Entity employee = new Entity("Employee", "asalieri");
//            employee.setProperty("firstName", "Antonio1");
//            employee.setProperty("lastName", "Salieri1");
//
//            datastore.put(employee);

            Cookie refreshToken = routeContext.getRequest().getCookie("refresh_token");

            try {
                Entity entity = datastore.get(KeyFactory.createKey("Employee", "asalieri"));
                routeContext.setLocal("greeting", entity.getProperty("lastName"));
            } catch (EntityNotFoundException e) {
                routeContext.setLocal("greeting", "not found");
            }

            routeContext.render("index");
        });

        addPublicResourceRoute();

        //this.getPippoSettings().overrideSetting();
    }
}
