package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AugmentedEngines extends BaseLogisticsHullMod {
	private static final int BURN_LEVEL_BONUS = 2;
	private static final int SMOD_BURN_LEVEL_BONUS = 1; // Vanilla bonus
	public static final float MAINTENANCE_MULT = 0.7f; // Mod bonus
	
//	private static final int STRENGTH_PENALTY = 50;
//	private static final int PROFILE_PENALTY = 50;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
//		stats.getSensorProfile().modifyPercent(id, PROFILE_PENALTY);
//		stats.getSensorStrength().modifyMult(id, 1f - STRENGTH_PENALTY * 0.01f);
		if (stats.getVariant().getSMods().contains("augmentedengines") || stats.getVariant().getSModdedBuiltIns().contains("augmentedengines")) {
			// Mod bonus
			stats.getFuelUseMod().modifyMult(id, MAINTENANCE_MULT);
			// Vanilla bonus
			stats.getMaxBurnLevel().modifyFlat(id, SMOD_BURN_LEVEL_BONUS);
		}

		stats.getMaxBurnLevel().modifyFlat(id, BURN_LEVEL_BONUS);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + BURN_LEVEL_BONUS;
//		if (index == 1) return "" + STRENGTH_PENALTY + "%";
//		if (index == 2) return "" + PROFILE_PENALTY + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases burn level by an additional %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+1");
			tooltip.addPara("Reduce fuel use by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-30" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("augmentedengines") || ship.getVariant().getSModdedBuiltIns().contains("augmentedengines")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Increases burn level by an additional %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+1");
			tooltip.addPara("Reduce fuel use by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-30" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases burn level by an additional %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+1");
			tooltip.addPara("Reduce fuel use by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-30" + "%");
		}
    }
	

}


