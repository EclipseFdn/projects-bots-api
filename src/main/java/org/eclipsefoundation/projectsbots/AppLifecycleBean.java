package org.eclipsefoundation.projectsbots;

import java.io.IOException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipsefoundation.projectsbots.db.BotsDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class AppLifecycleBean {

    @Inject BotsDB db;
    
    void onStart(@Observes StartupEvent ev) throws IOException {               
        db.refresh();
    }

}