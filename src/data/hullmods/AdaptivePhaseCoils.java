package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.combat.PhaseCloakStats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AdaptivePhaseCoils extends BaseHullMod {

	public static float FLUX_THRESHOLD_INCREASE_PERCENT = 50f;
	public static float PHASE_SPEED_BONUS = 25f;
	public static float PHASE_COOLDOWN_REDUCTION = 25f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("adaptive_coils") || stats.getVariant().getSModdedBuiltIns().contains("adaptive_coils")) {
                    stats.getDynamic().getMod(Stats.PHASE_CLOAK_SPEED_MOD).modifyFlat(id, PHASE_SPEED_BONUS);
                    stats.getDynamic().getMod(Stats.PHASE_CLOAK_ACCEL_MOD).modifyFlat(id, PHASE_SPEED_BONUS);
                    stats.getPhaseCloakCooldownBonus().modifyMult(id, 1f - PHASE_COOLDOWN_REDUCTION / 100f);
		}
		stats.getDynamic().getMod(
				Stats.PHASE_CLOAK_FLUX_LEVEL_FOR_MIN_SPEED_MOD).modifyPercent(id, FLUX_THRESHOLD_INCREASE_PERCENT);
                
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) Math.round(FLUX_THRESHOLD_INCREASE_PERCENT) + "%";
		if (index == 1) return "" + (int) Math.round(PhaseCloakStats.BASE_FLUX_LEVEL_FOR_MIN_SPEED * 100f) + "%";
		if (index == 2) return "" + (int)Math.round(
				PhaseCloakStats.BASE_FLUX_LEVEL_FOR_MIN_SPEED * 100f * 
				(1f + FLUX_THRESHOLD_INCREASE_PERCENT/100f)) + "%";
		return null;
	}
        
   public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
                        tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s top speed and acceleration while phase cloak active.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
			tooltip.addPara("%s phase cloak cooldown.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-25" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("adaptive_coils") || ship.getVariant().getSModdedBuiltIns().contains("adaptive_coils")) {
                        tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s top speed and acceleration while phase cloak active.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+25" + "%");
			tooltip.addPara("%s phase cloak cooldown.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-25" + "%");
		} else if (!isForModSpec) {
                        tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s top speed and acceleration while phase cloak active.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
			tooltip.addPara("%s phase cloak cooldown.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-25" + "%");
		}
    }
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().hasHullMod(HullMods.PHASE_ANCHOR)) return false;
		return ship.getHullSpec().isPhase();
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().hasHullMod(HullMods.PHASE_ANCHOR)) {
			return "Incompatible with Phase Anchor";
		}
		if (!ship.getHullSpec().isPhase()) {
			return "Can only be installed on phase ships";
		}
		return super.getUnapplicableReason(ship);
	}
	
}

