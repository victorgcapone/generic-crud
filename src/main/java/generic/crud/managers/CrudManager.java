package generic.crud.managers;

import javax.inject.Singleton;

@Singleton
public abstract class CrudManager<T> {

    public abstract T post(T t);
    public abstract T get (String id);
    public abstract T put (String id, T update);
    public abstract T delete (String id);

}
