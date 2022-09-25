package me.felipe.clans.org.managers.storage;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.database.CoreDB;
import me.felipe.clans.org.database.MySQLCore;
import me.felipe.clans.org.database.SQLiteCore;
import me.felipe.clans.org.managers.clan.Clan;
import me.felipe.clans.org.managers.clan.ClanManager;
import me.felipe.clans.org.managers.clan.profile.ClanPlayerProfile;
import me.felipe.clans.org.managers.clan.ranks.ClanRank;
import me.felipe.clans.org.utils.LocationUtil;
import me.felipe.clans.org.utils.Settings;
import org.bukkit.Location;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class StorageManager {

    private WLClansRegister wlClansRegister;

    private CoreDB coreDB;
    private Logger logger;

    private ClanManager clanManager;

    public StorageManager(WLClansRegister wlClansRegister) {
        this.wlClansRegister = wlClansRegister;
        this.logger = wlClansRegister.getPlugin().getLogger();
        this.clanManager = wlClansRegister.getClanManager();
        startConnection();
        setupTables();
        load();
    }

    public void startConnection() {
        if (Settings.USE_MYSQL) {
            coreDB = new MySQLCore(Settings.MYSQL_HOST, Settings.MYSQL_PORT, Settings.MYSQL_USER, Settings.MYSQL_PASSWORD, Settings.MYSQL_DATABASE, wlClansRegister.getPlugin());
            if (coreDB.checkConnection()) {
                logger.info("A conexao mysql foi estabelecida.");
            } else {
                logger.info("A conexao mysql não foi estabelecida.");
            }
        } else {
            coreDB = new SQLiteCore(wlClansRegister.getPlugin().getDataFolder().getPath(), wlClansRegister.getPlugin());
            if (coreDB.checkConnection()) {
                logger.info("A conexao sqlite foi estabelecida.");
            } else {
                logger.info("A conexao sqlite não foi estabelecida.");
            }
        }
    }

    private void setupTables() {
        if (!coreDB.existsTable("wlc_profiles")) {
            String query = "CREATE TABLE IF NOT EXISTS `wlc_profiles` (" +
                    "`uuid` VARCHAR(64), " +
                    "`clanRank` TEXT, " +
                    "`kills` DOUBLE, " +
                    "`deaths` DOUBLE)";
            coreDB.execute(query);
        }

        if (!coreDB.existsTable("wlc_clans")) {
            String query = "CREATE TABLE IF NOT EXISTS `wlc_clans` (" +
                    "`name` VARCHAR(16), " +
                    "`tag` VARCHAR(3), " +
                    "`members` TEXT, " +
                    "`baseLocation` TEXT)";
            coreDB.execute(query);
        }
    }

    public void closeConnection() {
        coreDB.close();
    }

    public void insertProfile(ClanPlayerProfile profile) {
        String query = "INSERT INTO `wlc_profiles` (`uuid`, `clanRank`, `kills`, `deaths`) ";
        String values = "VALUES " +
                "('" + profile.getUuid().toString() +
                "', '" + profile.getClanRank().name() +
                "', '" + profile.getKills() +
                "', '" + profile.getDeaths() +
                "')";
        coreDB.insert(query + values);
    }

    public void insertClan(Clan clan) {
        String query = "INSERT INTO `wlc_clans` (`name`, `tag`, `members`, `baseLocation`) ";
        String values = "VALUES (" +
                "'" + clan.getName() +
                "', '" + clan.getTag() +
                "', '" + clan.getPackedMembers() +
                "', '" + clan.getBaseLocationFormatted() +
                "')";
        coreDB.insert(query + values);
    }

    public void deleteProfile(ClanPlayerProfile profile) {
        String query = "DELETE FROM `wlc_profiles` WHERE " +
                "`uuid` = '" + profile.getUuid() + "'";
        coreDB.update(query);
    }

    public void deleteClan(Clan clan) {
        String query = "DELETE FROM `wlc_clans` WHERE `name` = '" + clan.getName() + "'";
        coreDB.update(query);
    }

    public void updateProfile(ClanPlayerProfile profile) {
        String query = "UPDATE `wlc_profiles` SET " +
                "`clanRank` = '" + profile.getClanRank().name() + "', " +
                "`kills` = '" + profile.getKills() + "', " +
                "`deaths` = '" + profile.getDeaths() + "' " +
                "WHERE `uuid` = '" + profile.getUuid() + "'";
        coreDB.update(query);
    }

    public void updateClan(Clan clan) {
        String query = "UPDATE `wlc_clans` SET " +
                "`members` = '" + clan.getPackedMembers() + "', " +
                "`baseLocation` = '" + clan.getBaseLocationFormatted() + "' " +
                "WHERE `name` = '" + clan.getName() + "'";
        coreDB.update(query);
    }

    public boolean profileExist(ClanPlayerProfile profile) {
        String query = "SELECT * FROM `wlc_profiles` WHERE " +
                "`uuid` = '" + profile.getUuid() + "'";
        ResultSet resultSet = coreDB.select(query);

        if (resultSet != null) {
            try {
                if (resultSet.next()) {
                    return true;
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    public boolean clanExist(Clan clan) {
        String query = "SELECT * FROM `wlc_clans` WHERE " +
                "`name` = '" + clan.getName() + "'";
        ResultSet resultSet = coreDB.select(query);

        if (resultSet != null) {
            try {
                if (resultSet.next()) {
                    return true;
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    public void retrieveProfiles() {
        String query = "SELECT * FROM `wlc_profiles`";
        ResultSet resultSet = coreDB.select(query);

        if (resultSet != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    while (resultSet.next()) {
                        try {
                            String uuidString = resultSet.getString("uuid");
                            String clanRank = resultSet.getString("clanRank");
                            UUID uuid = UUID.fromString(uuidString);
                            double kills = resultSet.getDouble("kills");
                            double deaths = resultSet.getDouble("deaths");
                            clanManager.loadPlayerClanProfile(new ClanPlayerProfile.Builder()
                                    .uuid(uuid)
                                    .clanRank(ClanRank.valueOf(clanRank))
                                    .kills(kills)
                                    .deaths(deaths)
                                    .build());
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }).thenAccept(unused -> {
                retrieveClans();
            });
        }
    }

    public void retrieveClans() {
        String query = "SELECT * FROM `wlc_clans`";
        ResultSet resultSet = coreDB.select(query);

        if (resultSet != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    while (resultSet.next()) {
                        try {
                            String clanName = resultSet.getString("name");
                            String clanTag = resultSet.getString("tag");
                            Location clanBaseLocation = LocationUtil.unformattedLocation(resultSet.getString("baseLocation"));
                            String clanMembersUuid = resultSet.getString("members");
                            List<ClanPlayerProfile> clanMembers = new ArrayList<>();
                            System.out.println(clanMembersUuid);
                            for (String member : clanMembersUuid.split(";")) {
                                System.out.println(member + " MEMBROS");
                                clanMembers.add(clanManager.getClanProfile(UUID.fromString(member)));
                            }
                            System.out.println(clanMembers);
                            clanManager.loadClan(new Clan.Builder()
                                    .clanName(clanName)
                                    .clanTag(clanTag)
                                    .clanMembers(clanMembers)
                                    .baseLocation(clanBaseLocation)
                                    .build());
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            });
        }
    }

    public void save() {
        for (ClanPlayerProfile profile : clanManager.getClanPlayerProfiles()) {
            if (profileExist(profile)) {
                updateProfile(profile);
            } else {
                insertProfile(profile);
            }
        }
        for (Clan clan : clanManager.getClans()) {
            if (clanExist(clan)) {
                updateClan(clan);
            } else {
                insertClan(clan);
            }
        }
    }

    public void load() {
        retrieveProfiles();
    }
}

