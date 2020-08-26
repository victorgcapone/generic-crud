package generic.crud;

import generic.crud.managers.UserCrudManager;
import io.micronaut.runtime.Micronaut;

import javax.inject.Inject;

public class Application {

    @Inject
    protected static UserCrudManager manager;

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}