package net.server;

public enum UserGroup {

    PLAYER("§7" , "09players"),
    VIP("§5Vip §7| " , "07premium"),
    SUP("§aSup §7| " , "06supporter"),
    BUILDER("§eBuilder §7| " , "05builder"),
    DEV("§bDev §7| " , "04developers"),
    MOD("§cMod §7| " , "03moderator"),
    SIRMOD("§cMod §7| " , "02sirmoderator"),
    ADMIN("§4Admin §7| " , "01admins");

    private final String prefix;
    private final String teams;

    UserGroup(String prefix , String teams) {
        this.prefix = prefix;
        this.teams = teams;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTeams() {
        return teams;
    }
}
