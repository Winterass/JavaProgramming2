package net.server.commands;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import net.server.UserGroup;
import org.bson.Document;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class GroupCommand implements CommandExecutor {

    private final MongoCollection<Document> mongoCollection;

    public GroupCommand(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 3) {

            switch (args[0]) {
                case "add":
                    String name = args[1];
                    String group = args[2];

                    this.mongoCollection.updateOne(Filters.eq("name", name),
                            new Document("$set", new Document("group", group)));

                    break;
            }

        }else if(args.length == 0) {
            Player player = (Player) sender;
            Document document = this.mongoCollection.find(Filters.eq("_id", player.getUniqueId())).first();
            UserGroup group = UserGroup.valueOf(document.getString("group").toUpperCase(Locale.ROOT));
            player.sendMessage("Du bist in der Gruppe " + group);
        }


        return false;
    }


}
