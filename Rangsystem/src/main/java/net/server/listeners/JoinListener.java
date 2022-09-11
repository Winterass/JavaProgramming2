package net.server.listeners;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import net.server.GroupCache;
import net.server.RangSystem;
import net.server.UserCache;
import net.server.UserGroup;
import org.bson.Document;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JoinListener implements Listener {

    private final MongoCollection<Document> mongoCollection;

    public JoinListener(MongoCollection<Document> mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Document document = this.mongoCollection.find(Filters.eq("_id", player.getUniqueId())).first();

        if (document == null) {
            document = new Document("_id", player.getUniqueId())
                    .append("name", player.getName())
                    .append("group", "player")
                    .append("permissions", new ArrayList<>());

            this.mongoCollection.insertOne(document);
        }

        String name = document.getString("name");
        UserGroup group = UserGroup.valueOf(document.getString("group").toUpperCase(Locale.ROOT));

        if (!name.equals(player.getName())) {
            this.mongoCollection.updateOne(Filters.eq("_id", player.getUniqueId()), new Document("$set", new Document("name", player.getName())));
        }

        player.sendMessage("Du hast den Rang " + group);

       Scoreboard scoreboard = player.getScoreboard();


       for(UserGroup userGroup : UserGroup.values()){
           Team team = scoreboard.getTeam(userGroup.getTeams());
           if(team == null){
               team = scoreboard.registerNewTeam(userGroup.getTeams());
           }
           team.setPrefix(userGroup.getPrefix());
           team.setColor(ChatColor.GRAY);
       }

        Team team = scoreboard.getTeam(group.getTeams());


        team.addPlayer(player);
        player.setDisplayName(group.getPrefix() + player.getName());


        List<String> permissions = GroupCache.getPermissions(group);

        //player.sendMessage("hi 1");
        PermissionAttachment attachment = player.addAttachment(RangSystem.getPlugin());
        //player.sendMessage("hi 2");

        for(String permission : permissions) {
            //player.sendMessage("hi 3");
            if(permission.startsWith("-")){
                attachment.setPermission(permission.substring(1), false);

            }else{
                //player.sendMessage("hi 4");
                attachment.setPermission(permission, true);
                player.updateCommands();
                //player.sendMessage("hi 5");
            }
        }

        for(String permission : document.getList("permissions", String.class)) {
            if(permission.startsWith("-")){
                attachment.setPermission(permission.substring(1), false);
                player.updateCommands();
            }else{
                attachment.setPermission(permission, true);
                player.updateCommands();
            }
        }

    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event){
        final Player p = event.getPlayer();
        final String message = event.getMessage().replace("%" , "%%");

        event.setFormat(p.getDisplayName() + message);

    }

}
