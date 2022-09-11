package net.server;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupCache {

    private static final Map<UserGroup, List<String>> PERMISSIONS = new HashMap<>();

    public static void load(MongoCollection<Document> collection) {
        for (Document document : collection.find()) {
            UserGroup group = UserGroup.valueOf(document.getString("group"));
            PERMISSIONS.put(group, document.getList("permissions", String.class));
        }
    }

    public static List<String> getPermissions(UserGroup group) {
        return PERMISSIONS.get(group);
    }
}
