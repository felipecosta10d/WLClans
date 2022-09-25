package me.felipe.clans.org.utils;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.config.ConfigHandler;
import me.felipe.clans.org.config.ConfigType;

import java.util.List;

public class Settings {

    public static boolean USE_MYSQL;
    public static String MYSQL_HOST;
    public static int MYSQL_PORT;
    public static String MYSQL_USER;
    public static String MYSQL_PASSWORD;
    public static String MYSQL_DATABASE;

    public static List<String> CASH_FORMATTED_LIST;

    public Settings(WLClansRegister wlClansRegister) {
        loadSettings(wlClansRegister);
    }

    public void loadSettings(WLClansRegister wlClansRegister) {
        ConfigHandler file = wlClansRegister.getConfigManager().getFile(ConfigType.DATABASE);
        USE_MYSQL = file.get().getBoolean("MySQL-settings.Use");
        if (USE_MYSQL) {
            MYSQL_HOST = file.get().getString("MySQL-settings.Host");
            MYSQL_PORT = file.get().getInt("MySQL-settings.Port");
            MYSQL_USER = file.get().getString("MySQL-settings.User");
            MYSQL_PASSWORD = file.get().getString("MySQL-settings.Password");
            MYSQL_DATABASE = file.get().getString("MySQL-settings.Database");
        }
        CASH_FORMATTED_LIST = file.get().getStringList("Money.Formatters");
    }
}
