package net.scape.project.suiteX.handlers;

import org.bukkit.Location;

import java.math.BigDecimal;
import java.util.List;

/**
 * @param money    economy
 * @param homes    homes
 * @param isVanish bool's
 */
public record xPlayer(String displayname, String nickname, Long lastSeen, BigDecimal money, BigDecimal bank,
                      List<Location> homes, boolean isVanish, boolean isMuted, boolean isAFK, boolean isBaltopExempt,
                      boolean isGodMode, boolean isTeleport, boolean isJailed, boolean isAcceptingPay) {
}
