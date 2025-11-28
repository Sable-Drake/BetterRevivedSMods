package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AuxiliaryThrusters extends BaseHullMod {

	public static final float MANEUVER_BONUS = 50f;
	public static final float SMANEUVER_BONUS = 66f;
	public static final float SMOD_SPEED_BONUS = 10f; // Vanilla bonus
	public static final float SMOD_TURN_MULT = 2f; // Vanilla bonus
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("auxiliarythrusters") || stats.getVariant().getSModdedBuiltIns().contains("auxiliarythrusters")) {
			// Mod bonuses
			stats.getAcceleration().modifyPercent(id, SMANEUVER_BONUS * 2f);
			stats.getDeceleration().modifyPercent(id, SMANEUVER_BONUS);
			stats.getTurnAcceleration().modifyPercent(id, SMANEUVER_BONUS * 2f);
			stats.getMaxTurnRate().modifyPercent(id, SMANEUVER_BONUS);
			// Vanilla bonuses
			stats.getDynamic().getStat(Stats.ZERO_FLUX_BOOST_TURN_RATE_BONUS_MULT).modifyMult(id, SMOD_TURN_MULT);
			stats.getZeroFluxSpeedBoost().modifyFlat(id, SMOD_SPEED_BONUS);
		} else {
			stats.getAcceleration().modifyPercent(id, MANEUVER_BONUS * 2f);
			stats.getDeceleration().modifyPercent(id, MANEUVER_BONUS);
			stats.getTurnAcceleration().modifyPercent(id, MANEUVER_BONUS * 2f);
			stats.getMaxTurnRate().modifyPercent(id, MANEUVER_BONUS);
		}

	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) MANEUVER_BONUS + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Maneuverability's bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("Increases the 0-flux speed boost by %s, and doubles the 0-flux turn rate bonus.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10");
			return;
		} else if (ship.getVariant().getSMods().contains("auxiliarythrusters") || ship.getVariant().getSModdedBuiltIns().contains("auxiliarythrusters")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Maneuverability's bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("Increases the 0-flux speed boost by %s, and doubles the 0-flux turn rate bonus.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "10");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Maneuverability's bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "66" + "%");
			tooltip.addPara("Increases the 0-flux speed boost by %s, and doubles the 0-flux turn rate bonus.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10");
		}
    }
	
}
