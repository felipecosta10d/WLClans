package me.felipe.clans.org.managers.clan.profile.invite;

import me.felipe.clans.org.managers.clan.Clan;

public class Invite {

    private final Clan clan;
    private int time = 60;

    public Invite(Builder builder) {
        this.clan = builder.clan;
    }

    public Clan getClan() {
        return clan;
    }

    public int getTime() {
        return time;
    }

    public void countTime() {
        this.time -= 1;
    }

    public static class Builder {
        private Clan clan;

        public Builder clan(Clan clan) {
            this.clan = clan;
            return this;
        }

        public Invite build() {
            return new Invite(this);
        }
    }
}
