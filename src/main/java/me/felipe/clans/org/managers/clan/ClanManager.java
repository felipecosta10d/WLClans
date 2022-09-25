package me.felipe.clans.org.managers.clan;

import me.felipe.clans.org.WLClansRegister;
import me.felipe.clans.org.managers.clan.profile.ClanPlayerProfile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClanManager {

    private WLClansRegister wlClansRegister;
    private Set<Clan> clans = new HashSet<>();
    private Set<ClanPlayerProfile> clanPlayerProfiles = new HashSet<>();

    public ClanManager(WLClansRegister wlClansRegister) {
        this.wlClansRegister = wlClansRegister;
    }

    public void createClan(String clanName, String clanTag, UUID leader) {
        Clan clan = new Clan.Builder()
                .clanName(clanName)
                .clanTag(clanTag)
                .build();
        clan.addMember(getClanProfile(leader));
        clan.setLeader(leader);
        clans.add(clan);
    }

    public void loadClan(Clan clan) {
        clans.add(clan);
    }

    public void deleteClan(Clan clan) {
        clans.remove(clan);
    }

    public boolean playerHasClan(String name) {
        for (ClanPlayerProfile clanPlayerProfile : clanPlayerProfiles) {
            if (clanPlayerProfile.getName().equals(name) && clanPlayerProfile.hasClan()) {
                return true;
            }
        }
        return false;
    }

    public boolean playerHasClan(UUID uuid) {
        for (ClanPlayerProfile clanPlayerProfile : clanPlayerProfiles) {
            if (clanPlayerProfile.getUuid().equals(uuid) && clanPlayerProfile.hasClan()) {
                return true;
            }
        }
        return false;
    }

    public Clan getPlayerClan(UUID uuid) {
        for (Clan clan : clans) {
            if (clan.isMember(uuid)) {
                return clan;
            }
        }
        return null;
    }

    public boolean existClanWithName(String clanName) {
        return clans.stream().anyMatch(clan -> clan.getName().equalsIgnoreCase(clanName));
    }

    public boolean existClanWithTag(String clanTag) {
        return clans.stream().anyMatch(clan -> clan.getTag().equalsIgnoreCase(clanTag));
    }

    public Clan getClanByName(String clanName) {
        for (Clan clan : clans) {
            if (clan.getName().equals(clanName)) {
                return clan;
            }
        }
        return null;
    }

    public Set<Clan> getClans() {
        return clans;
    }

    public ClanPlayerProfile getClanProfile(UUID uuid) {
        for (ClanPlayerProfile clanPlayerProfile : clanPlayerProfiles) {
            if (clanPlayerProfile.getUuid().equals(uuid)) {
                return clanPlayerProfile;
            }
        }
        return null;
    }

    public boolean hasClanProfile(UUID uuid) {
        return clanPlayerProfiles.stream().anyMatch(clanPlayerProfile -> clanPlayerProfile.getUuid().equals(uuid));
    }

    public void createPlayerClanProfile(UUID uuid) {
        clanPlayerProfiles.add(new ClanPlayerProfile.Builder()
                .uuid(uuid)
                .build());
    }

    public void loadPlayerClanProfile(ClanPlayerProfile profile) {
        clanPlayerProfiles.add(profile);
    }

    public Set<ClanPlayerProfile> getClanPlayerProfiles() {
        return clanPlayerProfiles;
    }
}
