package reader;

import ro.pippo.core.Application;
import ro.pippo.freemarker.FreemarkerTemplateEngine;

public class Start extends Application {
    public Start() {
        setTemplateEngine(new FreemarkerTemplateEngine());
    }

    @Override
    protected void onInit() {

        GET("/", routeContext -> {
            routeContext.setLocal("greeting", "Тест New1234");
            routeContext.render("hello");
        });

        addPublicResourceRoute();

        //this.getPippoSettings().overrideSetting();
    }
}
