package net.server;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCache {

    private static final Map<UserCache, List<String>> PERMISSIONSFORPLAYER = new HashMap<>();

    public static void load(MongoCollection<Document> collection) {
        for (Document document : collection.find()) {


        }
    }

    public static List<String> getPermissionsForPlayer(UserCache permissions) {
        return PERMISSIONSFORPLAYER.get(permissions);
    }

}
