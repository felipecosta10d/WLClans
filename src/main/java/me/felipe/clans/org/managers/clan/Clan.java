package me.felipe.clans.org.managers.clan;

import lombok.Getter;
import me.felipe.clans.org.managers.clan.profile.ClanPlayerProfile;
import me.felipe.clans.org.managers.clan.ranks.ClanRank;
import me.felipe.clans.org.utils.LocationUtil;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Clan {

    private final String name;
    private final String tag;
    private List<ClanPlayerProfile> members;
    private Location baseLocation;

    public Clan(Builder builder) {
        this.name = builder.name;
        this.tag = builder.tag;
        this.members = builder.members;
        this.baseLocation = builder.baseLocation;
    }

    public ClanPlayerProfile getLeader() {
        for (ClanPlayerProfile member : members) {
            if (member.isLeader()) {
                return member;
            }
        }
        return null;
    }

    public void setLeader(UUID uuid) {
        for (ClanPlayerProfile member : members) {
            if (member.getUuid().equals(uuid)) {
                member.setClanRank(ClanRank.LEADER);
            }
        }
    }

    public boolean isMember(UUID uuid) {
        for (ClanPlayerProfile member : members) {
            if (member.getUuid().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    public boolean isMember(String name) {
        for (ClanPlayerProfile member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public ClanPlayerProfile getMember(UUID uuid) {
        for (ClanPlayerProfile member : members) {
            if (member.getUuid().equals(uuid)) {
                return member;
            }
        }
        return null;
    }

    public ClanPlayerProfile getMember(String name) {
        for (ClanPlayerProfile member : members) {
            if (member.getName().equalsIgnoreCase(name)) {
                return member;
            }
        }
        return null;
    }

    public void addMember(ClanPlayerProfile clanPlayerProfile) {
        clanPlayerProfile.setClanRank(ClanRank.MEMBER);
        clanPlayerProfile.setHasClan(true);
        members.add(clanPlayerProfile);
    }

    public void removeMember(ClanPlayerProfile clanPlayerProfile) {
        clanPlayerProfile.setClanRank(ClanRank.NONE);
        clanPlayerProfile.setHasClan(false);
        members.remove(clanPlayerProfile);
    }

    public String getPackedMembers() {
        String membersPacked = "";
        for (ClanPlayerProfile member : members) {
            membersPacked += member.getUuid() + ";";
        }
        return membersPacked;
    }

    public List<ClanPlayerProfile> getMembers() {
        return members;
    }

    public Location getBaseLocation() {
        return this.baseLocation;
    }

    public String getBaseLocationFormatted() {
        return LocationUtil.formattedLocation(this.baseLocation);
    }

    public void setBaseLocation(Location location) {
        this.baseLocation = location;
    }

    public boolean baseIsSet() {
        return baseLocation != null;
    }

    public static class Builder {
        private String name;
        private String tag;
        private List<ClanPlayerProfile> members = new ArrayList<>();
        private Location baseLocation;

        public Builder clanName(String name) {
            this.name = name;
            return this;
        }

        public Builder clanTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder clanMembers(List<ClanPlayerProfile> members) {
            this.members = members;
            return this;
        }

        public Builder baseLocation(Location baseLocation) {
            this.baseLocation = baseLocation;
            return this;
        }

        public Clan build() {
            return new Clan(this);
        }
    }
}
