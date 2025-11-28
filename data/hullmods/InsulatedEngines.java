package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class InsulatedEngines extends BaseLogisticsHullMod {

	public static final float PROFILE_MULT = 0.5f;
	public static final float HEALTH_BONUS = 100f;
	public static final float HULL_BONUS = 10f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("insulatedengine") || stats.getVariant().getSModdedBuiltIns().contains("insulatedengine")) {
			stats.getEngineHealthBonus().modifyPercent(id, HEALTH_BONUS*2);
			stats.getSensorProfile().modifyMult(id, PROFILE_MULT/2);
		} else {
			stats.getEngineHealthBonus().modifyPercent(id, HEALTH_BONUS);
			stats.getSensorProfile().modifyMult(id, PROFILE_MULT);
		}
		stats.getHullBonus().modifyPercent(id, HULL_BONUS);
		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 1) return "" + (int) HULL_BONUS + "%";
		if (index == 2) return "" + (int) ((1f - PROFILE_MULT) * 100f) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s ship's engine durability.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+100" + "%");
			tooltip.addPara("Sensor profile reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "75" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("insulatedengine") || ship.getVariant().getSModdedBuiltIns().contains("insulatedengine")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s ship's engine durability.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+100" + "%");
			tooltip.addPara("Sensor profile reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "75" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s ship's engine durability.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+100" + "%");
			tooltip.addPara("Sensor profile reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "75" + "%");
		}
    }


}
