package generic.crud.managers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.BeanContext;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.JsonPatch;
import javax.json.JsonValue;

@Singleton
public abstract class CrudManager<T> {

    @Inject
    private ObjectMapper mapper;

    @Inject
    private BeanContext context;

    public abstract T post(T t);
    public abstract T get (String id);
    public abstract T put (String id, T update);
    public abstract T delete (String id);
    public abstract T patch (String id, JsonPatch patch);

    public T applyPatch(T bean, JsonPatch patch){
        JsonValue jsonBean = mapper.convertValue(bean, JsonValue.class);
        JsonValue patched = patch.apply(jsonBean.asJsonObject());
        Class<T> klazz = getManagedClass();
        return mapper.convertValue(patched, klazz);
    }

    public Class<T> getManagedClass(){
        return context.getBeanDefinition(this.getClass()).getTypeParameters(CrudManager.class)[0];
    }


}
