package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.ui.Alignment;

public class ArmoredWeapons extends BaseHullMod {

	public static final float HEALTH_BONUS = 100f;
	public static final float ARMOR_BONUS = 10f;
	public static final float TURN_PENALTY = 25f;
	public static final float RECOIL_BONUS = 25f;
        public static final float ROF_BONUS = 10f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("armoredweapons") || stats.getVariant().getSModdedBuiltIns().contains("armoredweapons")) {
                    stats.getBallisticRoFMult().modifyPercent(id, ROF_BONUS);
                    stats.getEnergyRoFMult().modifyPercent(id, ROF_BONUS);
		}
		stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
		stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getWeaponTurnRateBonus().modifyMult(id, 1f - TURN_PENALTY * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 1) return "" + (int) RECOIL_BONUS + "%";
		if (index == 2) return "" + (int) TURN_PENALTY + "%";
		if (index == 3) return "" + (int) ARMOR_BONUS + "%";
		return null;
	}

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases the rate of fire of all non-missile weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("armoredweapons") || ship.getVariant().getSModdedBuiltIns().contains("armoredweapons")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);	
			tooltip.addPara("Increases the rate of fire of all non-missile weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "10" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases the rate of fire of all non-missile weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10" + "%");
		}
    }
	
}
