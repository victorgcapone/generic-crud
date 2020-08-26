package generic.crud.managers;

import generic.crud.entities.User;
import io.micronaut.core.annotation.Introspected;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.UUID;

@Singleton
@Introspected
@Collection("users")
public class UserCrudManager extends CrudManager<User> {

    private HashMap<String, User> users = new HashMap<>();

    @Override
    public User post(User user) {
        users.put(UUID.randomUUID().toString(), user);
        return user;
    }

    @Override
    public User get(String id) {
        return users.get(id);
    }

    @Override
    public User put(String id, User update) {
        return null;
    }

    @Override
    public User delete(String id) {
        return users.remove(id);
    }
}
