package me.felipe.clans.org.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {

    public static String formattedLocation(Location location) {
        return location.getX() +
                ";" + location.getY() +
                ";" + location.getZ() +
                ";" + location.getYaw() +
                ";" + location.getPitch() +
                ";" + location.getWorld().getName();
    }

    public static Location unformattedLocation(String string) {
        if (string == null || string.isEmpty()) return null;
        String[] parts = string.split(";");
        double x = Double.parseDouble(parts[0]);
        double y = Double.parseDouble(parts[1]);
        double z = Double.parseDouble(parts[2]);
        float yaw = Float.parseFloat(parts[3]);
        float pitch = Float.parseFloat(parts[4]);
        World world = Bukkit.getWorld(parts[5]);
        Location location = new Location(world, x, y, z, yaw, pitch);
        return location;
    }
}

