package net.server;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.server.commands.GroupCommand;
import net.server.listeners.JoinListener;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RangSystem extends JavaPlugin {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private MongoCollection<Document> mongoCollection;
    private MongoCollection<Document> groupCollection;

    private static RangSystem plugin;

    public static RangSystem getPlugin(){return plugin;}

    @Override
    public void onLoad() {plugin = this;}

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.mongoClient = new MongoClient("localhost", new MongoClientOptions.Builder()
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build());

        this.mongoDatabase = this.mongoClient.getDatabase("server");
        this.mongoCollection = this.mongoDatabase.getCollection("rangsystem");
        this.groupCollection = this.mongoDatabase.getCollection("groups");

        GroupCache.load(this.groupCollection);
        UserCache.load(this.mongoCollection);

        PluginManager manager = Bukkit.getPluginManager();

        manager.registerEvents(new JoinListener(mongoCollection), this);

        getCommand("rang").setExecutor(new GroupCommand(mongoCollection));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
