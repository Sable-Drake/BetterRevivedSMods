package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class StabilizedShieldEmitter extends BaseHullMod {

	public static final float SHIELD_UPKEEP_BONUS = 50f;
	public static final float FLUX_SHUNT_DISSIPATION = 12f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {		
		if (stats.getVariant().getSMods().contains("stabilizedshieldemitter") || stats.getVariant().getSModdedBuiltIns().contains("stabilizedshieldemitter")) {
			stats.getHardFluxDissipationFraction().modifyFlat(id, FLUX_SHUNT_DISSIPATION / 100f);
		}
		stats.getShieldUpkeepMult().modifyMult(id, 1f - SHIELD_UPKEEP_BONUS * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_UPKEEP_BONUS + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s hard flux dissipation while shields are active.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "12" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("stabilizedshieldemitter") || ship.getVariant().getSModdedBuiltIns().contains("stabilizedshieldemitter")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s hard flux dissipation while shields are active.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "12" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s hard flux dissipation while shields are active.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "12" + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shields";
	}
}
