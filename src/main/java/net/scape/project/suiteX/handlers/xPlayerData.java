package net.scape.project.suiteX.handlers;

import org.bukkit.Location;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class xPlayerData {

    private String displayname;
    private String nickname;
    private long lastSeen;
    private BigDecimal money;
    private BigDecimal bank;
    private List<Location> homes;
    private boolean isVanish;
    private boolean isMuted;
    private boolean isAFK;
    private boolean isBaltopExempt;
    private boolean isGodMode;
    private boolean isTeleport;
    private boolean isJailed;
    private boolean isAcceptingPay;

    public xPlayerData(xPlayer player) {
        this.displayname = player.displayname();
        this.nickname = player.nickname();
        this.lastSeen = player.lastSeen();
        this.money = player.money();
        this.bank = player.bank();
        this.homes = player.homes() != null ? new ArrayList<>(player.homes()) : new ArrayList<>();
        this.isVanish = player.isVanish();
        this.isMuted = player.isMuted();
        this.isAFK = player.isAFK();
        this.isBaltopExempt = player.isBaltopExempt();
        this.isGodMode = player.isGodMode();
        this.isTeleport = player.isTeleport();
        this.isJailed = player.isJailed();
        this.isAcceptingPay = player.isAcceptingPay();
    }

    public xPlayer toRecord() {
        return new xPlayer(
                displayname, nickname, lastSeen, money, bank, homes,
                isVanish, isMuted, isAFK, isBaltopExempt, isGodMode,
                isTeleport, isJailed, isAcceptingPay
        );
    }

    // Getters and Setters

    public String getDisplayname() { return displayname; }
    public void setDisplayname(String displayname) { this.displayname = displayname; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public long getLastSeen() { return lastSeen; }
    public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }

    public BigDecimal getMoney() { return money; }
    public void setMoney(BigDecimal money) { this.money = money; }

    public BigDecimal getBank() { return bank; }
    public void setBank(BigDecimal bank) { this.bank = bank; }

    public List<Location> getHomes() { return homes; }
    public void setHomes(List<Location> homes) { this.homes = homes; }

    public boolean isVanish() { return isVanish; }
    public void setVanish(boolean vanish) { isVanish = vanish; }

    public boolean isMuted() { return isMuted; }
    public void setMuted(boolean muted) { isMuted = muted; }

    public boolean isAFK() { return isAFK; }
    public void setAFK(boolean AFK) { isAFK = AFK; }

    public boolean isBaltopExempt() { return isBaltopExempt; }
    public void setBaltopExempt(boolean baltopExempt) { isBaltopExempt = baltopExempt; }

    public boolean isGodMode() { return isGodMode; }
    public void setGodMode(boolean godMode) { isGodMode = godMode; }

    public boolean isTeleport() { return isTeleport; }
    public void setTeleport(boolean teleport) { isTeleport = teleport; }

    public boolean isJailed() { return isJailed; }
    public void setJailed(boolean jailed) { isJailed = jailed; }

    public boolean isAcceptingPay() { return isAcceptingPay; }
    public void setAcceptingPay(boolean acceptingPay) { isAcceptingPay = acceptingPay; }
}
