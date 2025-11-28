package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class FluxBreakers extends BaseHullMod {

	public static final float OVERLOAD_REDUCTION = 20f;
	public static final float FLUX_RESISTANCE = 50f;
	public static final float VENT_RATE_BONUS = 25f;
	public static final float SVENT_RATE_BONUS = 33f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("fluxbreakers") || stats.getVariant().getSModdedBuiltIns().contains("fluxbreakers")) {
			stats.getOverloadTimeMod().modifyMult(id, 1f - OVERLOAD_REDUCTION / 100f);
			stats.getVentRateMult().modifyPercent(id, SVENT_RATE_BONUS);
		} else {
			stats.getVentRateMult().modifyPercent(id, VENT_RATE_BONUS);
		}
		stats.getEmpDamageTakenMult().modifyMult(id, 1f - FLUX_RESISTANCE * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) FLUX_RESISTANCE + "%";
		if (index == 1) return "" + (int) VENT_RATE_BONUS + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s overload duration", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20" + "%");
			tooltip.addPara("Flux Dissipation Bonus while venting increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("fluxbreakers") || ship.getVariant().getSModdedBuiltIns().contains("fluxbreakers")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s overload duration.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-20" + "%");
			tooltip.addPara("Flux Dissipation Bonus while venting increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s overload duration.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20" + "%");
			tooltip.addPara("Flux Dissipation Bonus while venting increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
		}
    }

}
