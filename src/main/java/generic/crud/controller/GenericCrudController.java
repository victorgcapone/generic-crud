package generic.crud.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import generic.crud.managers.Collection;
import generic.crud.managers.CrudManager;
import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;


@Controller("/crud/")
public class GenericCrudController {

    protected HashMap<String, CrudManager> managers = new HashMap<>();

    @Inject
    protected ObjectMapper mapper;

    @Inject
    protected BeanContext context;

    /**
     * We use the init method to cache all the registered Crud Managers in a hash map
     * as to speed up the querying of the managers by managed collection
     * @param managers Autowired list with all the CrudManager beans
     */
    @Inject
    @PostConstruct
    public void init(List<CrudManager> managers){
        String managedCollection;
        for (CrudManager manager : managers){
            try {
                managedCollection = BeanIntrospection.getIntrospection(manager.getClass())
                        .getAnnotation(Collection.class).getValue(String.class).orElse(null);
            } catch (NullPointerException npe) {
                throw new RuntimeException("Cannot register crud manager without a collection; Missing @Collection annotation");
            }
            CrudManager currentRegisteredManager = this.managers.get(managedCollection);
            if(managedCollection == null){
                throw new RuntimeException("Cannot register crud manager without a collection");
            }
            if(currentRegisteredManager != null) {
                throw new RuntimeException("Cannot register manager for a collection that's already managed");
            }
            this.managers.put(managedCollection, manager);
        }
    }

    @Post("/{collection}")
    public HttpResponse<?> post(String collection, @Body String object) throws JsonProcessingException {
        CrudManager manager = this.managers.get(collection);
        Class klazz = context.getBeanDefinition(manager.getClass()).getTypeParameters(CrudManager.class)[0];
        if(manager != null){
            manager.post(mapper.readerFor(klazz).readValue(object));
        }
        return HttpResponse.badRequest();
    }

    @Get("/{collection}/{id}")
    public HttpResponse<?> get(String collection, String id){
        CrudManager manager = this.managers.get(collection);
        if(manager != null) {
            return HttpResponse.ok(manager.get(id));
        }
        // return 500
        return HttpResponse.badRequest();
    }

    @Put("/{collection}/{id}")
    public HttpResponse<?> put(String collection, String id, @Body String object) throws JsonProcessingException {
        CrudManager manager = this.managers.get(collection);
        Class klazz = context.getBeanDefinition(manager.getClass()).getTypeParameters(CrudManager.class)[0];
        if(manager != null){
            manager.put(id, mapper.readerFor(klazz).readValue(object));
        }
        return HttpResponse.badRequest();
    }

    @Delete("/{collection}/{id}")
    public HttpResponse<?> delete(String collection, String id){
        CrudManager manager = this.managers.get(collection);
        if(manager != null) {
            return HttpResponse.ok(manager.delete(id));
        }
        // return 500
        return HttpResponse.badRequest();
    }

}
