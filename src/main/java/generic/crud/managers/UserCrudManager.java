package generic.crud.managers;

import generic.crud.entities.User;
import io.micronaut.core.annotation.Introspected;

import javax.inject.Singleton;
import javax.json.JsonPatch;
import java.util.HashMap;
import java.util.UUID;

@Singleton
@Introspected
@Collection("users")
public class UserCrudManager extends CrudManager<User> {

    private HashMap<String, User> users = new HashMap<>();

    @Override
    public User post(User user) {
        user.setUuid(UUID.randomUUID().toString());
        users.put(user.getUuid(), user);
        return user;
    }

    @Override
    public User get(String id) {
        return users.get(id);
    }

    @Override
    public User put(String id, User update) {
        User u = users.get(id);
        if(users != null) {
            update.setUuid(u.getUuid());
            users.put(id, update);
            return update;
        }
        return null;
    }

    @Override
    public User delete(String id) {
        return users.remove(id);
    }

    @Override
    public User patch(String id, JsonPatch patch) {
        User user = users.get(id);
        if(user != null){
            return applyPatch(user, patch);
        }
        return null;
    }
}
