package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.util.HashMap;
import java.util.Map;

public class AdvancedTurretGyros extends BaseHullMod {

	public static final float TURRET_SPEED_BONUS = 75f;
        public static final float DAMAGE_TO_MISSILE = 25f;
        public static final float DAMAGE_TO_FIGHTERS = 25f;
	public static Map DAMAGE_TO_FRIGATES = new HashMap();
        public static Map DAMAGE_TO_DESTROYERS = new HashMap();
        public static Map DAMAGE_TO_CRUISERS = new HashMap();
        public static Map DAMAGE_TO_CAPITALS = new HashMap();
	static {
                DAMAGE_TO_FRIGATES.put(HullSize.DEFAULT, 0f);
                DAMAGE_TO_FRIGATES.put(HullSize.FIGHTER, 0f);
		DAMAGE_TO_FRIGATES.put(HullSize.FRIGATE, 0f);
		DAMAGE_TO_FRIGATES.put(HullSize.DESTROYER, 5f);
		DAMAGE_TO_FRIGATES.put(HullSize.CRUISER, 10f);
		DAMAGE_TO_FRIGATES.put(HullSize.CAPITAL_SHIP, 20f);
                DAMAGE_TO_DESTROYERS.put(HullSize.DEFAULT, 0f);
                DAMAGE_TO_DESTROYERS.put(HullSize.FIGHTER, 0f);
		DAMAGE_TO_DESTROYERS.put(HullSize.FRIGATE, 0f);
		DAMAGE_TO_DESTROYERS.put(HullSize.DESTROYER, 0f);
		DAMAGE_TO_DESTROYERS.put(HullSize.CRUISER, 5f);
		DAMAGE_TO_DESTROYERS.put(HullSize.CAPITAL_SHIP, 10f);
                DAMAGE_TO_CRUISERS.put(HullSize.DEFAULT, 0f);
                DAMAGE_TO_CRUISERS.put(HullSize.FIGHTER, 0f);
		DAMAGE_TO_CRUISERS.put(HullSize.FRIGATE, 0f);
		DAMAGE_TO_CRUISERS.put(HullSize.DESTROYER, 0f);
		DAMAGE_TO_CRUISERS.put(HullSize.CRUISER, 0f);
		DAMAGE_TO_CRUISERS.put(HullSize.CAPITAL_SHIP, 5f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("turretgyros") || stats.getVariant().getSModdedBuiltIns().contains("turretgyros")) {
			stats.getDamageToFrigates().modifyPercent(id, (Float) DAMAGE_TO_FRIGATES.get(hullSize));
			stats.getDamageToDestroyers().modifyPercent(id, (Float) DAMAGE_TO_DESTROYERS.get(hullSize));
			stats.getDamageToCruisers().modifyPercent(id, (Float) DAMAGE_TO_CRUISERS.get(hullSize));
			stats.getDamageToFighters().modifyPercent(id, DAMAGE_TO_FIGHTERS);
                        stats.getDamageToMissiles().modifyPercent(id, DAMAGE_TO_MISSILE);
		}
			stats.getWeaponTurnRateBonus().modifyPercent(id, TURRET_SPEED_BONUS);
			stats.getBeamWeaponTurnRateBonus().modifyPercent(id, TURRET_SPEED_BONUS);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) TURRET_SPEED_BONUS + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s damage to fighters and missiles.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
			tooltip.addPara("%s/%s/%s damage bonus to ships smaller than the ship scaling with hull size.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+20" + "%", "10"+"%", "5"+"%");
			return;
		} else if (ship.getVariant().getSMods().contains("turretgyros")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s damage to fighters and missiles.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+25" + "%");
			if (ship.isCapital()) {
                            tooltip.addPara("%s damage to frigates", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+20" + "%");
                            tooltip.addPara("%s damage to destroyers", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+10" + "%");
                            tooltip.addPara("%s damage to cruisers", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
                        }
                        if (ship.isCruiser()) {
                            tooltip.addPara("%s damage to frigates", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+10" + "%");
                            tooltip.addPara("%s damage to destroyers", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
                        }
                        if (ship.isDestroyer()) {
                            tooltip.addPara("%s damage to frigates", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
                        }
		} else if (ship.getVariant().getSModdedBuiltIns().contains("turretgyros")) {
			tooltip.addSectionHeading("Built-in bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s damage to fighters and missiles.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+25" + "%");
			if (ship.isCapital()) {
                            tooltip.addPara("%s damage to frigates", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+20" + "%");
                            tooltip.addPara("%s damage to destroyers", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+10" + "%");
                            tooltip.addPara("%s damage to cruisers", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
                        }
                        if (ship.isCruiser()) {
                            tooltip.addPara("%s damage to frigates", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+10" + "%");
                            tooltip.addPara("%s damage to destroyers", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
                        }
                        if (ship.isDestroyer()) {
                            tooltip.addPara("%s damage to frigates", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
                        }
        } else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s damage to fighters and missiles.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
			tooltip.addPara("%s/%s/%s damage bonus to ships smaller than the ship scaling with hull size.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+20" + "%", "10"+"%", "5"+"%");
		}
    }


}
