package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class EfficiencyOverhaul extends BaseLogisticsHullMod {
	public static final float MAINTENANCE_MULT = 0.8f;
	public static final float SMAINTENANCE_MULT = 0.5f;
	public static final float MAX_CR_BONUS = 20f;
	
	public static final float REPAIR_RATE_BONUS = 50f;
	public static final float SREPAIR_RATE_BONUS = 100f;
	public static final float CR_RECOVERY_BONUS = 50f;
	public static final float SCR_RECOVERY_BONUS = 100f;
	public static final float REPAIR_BONUS = 50f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("efficiency_overhaul") || stats.getVariant().getSModdedBuiltIns().contains("efficiency_overhaul")) {
			stats.getMaxCombatReadiness().modifyFlat(id, MAX_CR_BONUS * 0.01f, "Efficiency Overhaul");
		}
        
		if (stats.getVariant().isCivilian() && (stats.getVariant().getSMods().contains("efficiency_overhaul") || stats.getVariant().getSModdedBuiltIns().contains("efficiency_overhaul"))) {
			stats.getMinCrewMod().modifyMult(id, SMAINTENANCE_MULT);
			stats.getSuppliesPerMonth().modifyMult(id, SMAINTENANCE_MULT);
		} else {
			stats.getMinCrewMod().modifyMult(id, MAINTENANCE_MULT);
			stats.getSuppliesPerMonth().modifyMult(id, MAINTENANCE_MULT);
		}
		stats.getFuelUseMod().modifyMult(id, MAINTENANCE_MULT);
		
		if (!stats.getVariant().isCivilian() && (stats.getVariant().getSMods().contains("efficiency_overhaul") || stats.getVariant().getSModdedBuiltIns().contains("efficiency_overhaul"))) {
			stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, SCR_RECOVERY_BONUS);
			stats.getRepairRatePercentPerDay().modifyPercent(id, SREPAIR_RATE_BONUS);
		} else {
			stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, CR_RECOVERY_BONUS);
			stats.getRepairRatePercentPerDay().modifyPercent(id, REPAIR_RATE_BONUS);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round((1f - MAINTENANCE_MULT) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round(CR_RECOVERY_BONUS) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("S-mod Bonus: %s maximum combat readiness", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+20" + "%");
			tooltip.addPara("Civilian S-Mod Bonus: Supply use for maintenance and minimum crew requirement reduction increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-50" + "%");
			tooltip.addPara("Combat S-Mod Bonus: Combat readiness recovery and repair rate increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("efficiency_overhaul")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("S-mod Bonus: %s maximum combat readiness", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+20" + "%");
			if (ship.getVariant().isCivilian()) {
				tooltip.addPara("Civilian S-mod Bonus: Supply use for maintenance and minimum crew requirement reduction increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-50" + "%");
			}
			if (!ship.getVariant().isCivilian()) {
				tooltip.addPara("Combat S-mod Bonus: Combat readiness recovery and repair rate increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100" + "%");
			}
		} else if (ship.getVariant().getSModdedBuiltIns().contains("efficiency_overhaul")) {
			tooltip.addSectionHeading("Built-in bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Built-in Bonus: %s maximum combat readiness", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+20" + "%");
			if (ship.getVariant().isCivilian()) {
				tooltip.addPara("Civilian Built-in Bonus: Supply use for maintenance and minimum crew requirement reduction increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-50" + "%");
			}
			if (!ship.getVariant().isCivilian()) {
				tooltip.addPara("Combat Built-in Bonus: Combat readiness recovery and repair rate increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100" + "%");
			}
        } else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("S-mod Bonus: %s maximum combat readiness", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+20" + "%");
			tooltip.addPara("Civilian S-Mod Bonus: Supply use for maintenance and minimum crew requirement reduction increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-50" + "%");
			tooltip.addPara("Combat S-Mod Bonus: Combat readiness recovery and repair rate increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100" + "%");
		}
    }

	
}







