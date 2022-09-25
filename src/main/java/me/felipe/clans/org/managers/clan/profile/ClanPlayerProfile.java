package me.felipe.clans.org.managers.clan.profile;

import lombok.Getter;
import me.felipe.clans.org.managers.clan.profile.invite.Invite;
import me.felipe.clans.org.managers.clan.ranks.ClanRank;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ClanPlayerProfile {

    private final UUID uuid;
    private final String name;
    private ClanRank clanRank;
    private double kills;
    private double deaths;
    private List<Invite> clanInvites;
    private boolean hasClan;

    public ClanPlayerProfile(Builder builder) {
        this.uuid = builder.uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.clanRank = builder.clanRank;
        this.kills = builder.kills;
        this.deaths = builder.deaths;
        this.hasClan = (clanRank != ClanRank.NONE);
    }

    public void setClanRank(ClanRank rank) {
        this.clanRank = rank;
    }

    public boolean isLeader() {
        return clanRank.equals(ClanRank.LEADER);
    }

    public boolean isCoLeader() {
        return clanRank.equals(ClanRank.CO_LEADER);
    }

    public boolean isMember() {
        return clanRank.equals(ClanRank.MEMBER);
    }

    public boolean isStaff() {
        return clanRank.equals(ClanRank.LEADER) || clanRank.equals(ClanRank.CO_LEADER);
    }

    public void setHasClan(boolean value) {
        this.hasClan = value;
    }

    public boolean hasClan() {
        return hasClan;
    }

    public double getKdr() {
        double kdr = (float) (this.kills / this.deaths);
        return kdr;
    }

    public static class Builder {
        private UUID uuid;
        private ClanRank clanRank = ClanRank.NONE;
        private double kills = 0;
        private double deaths = 0;
        private List<Invite> clanInvites = new ArrayList<>();

        public Builder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder clanRank(ClanRank clanRank) {
            this.clanRank = clanRank;
            return this;
        }

        public Builder kills(double kills) {
            this.kills = kills;
            return this;
        }

        public Builder deaths(double deaths) {
            this.deaths = deaths;
            return this;
        }

        public ClanPlayerProfile build() {
            return new ClanPlayerProfile(this);
        }
    }
}
