package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class RecoveryShuttles extends BaseHullMod {

	public static final float CREW_LOSS_MULT = 0.25f;
        public static final float SCREW_LOSS_MULT = 0.1f;
	public static final float RATE_DECREASE_MODIFIER = 0.6f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		if (stats.getVariant().getSMods().contains("recovery_shuttles") || stats.getVariant().getSModdedBuiltIns().contains("recovery_shuttles")) {
                    stats.getDynamic().getStat(Stats.FIGHTER_CREW_LOSS_MULT).modifyMult(id, SCREW_LOSS_MULT);
		} else {
                    stats.getDynamic().getStat(Stats.FIGHTER_CREW_LOSS_MULT).modifyMult(id, CREW_LOSS_MULT);
                }
	}
        
        	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {

		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		if ((ship.getVariant().getSMods().contains("recovery_shuttles") || ship.getVariant().getSModdedBuiltIns().contains("recovery_shuttles"))) {
			stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult("RecoveryShuttles", RATE_DECREASE_MODIFIER);
			CombatEngineAPI engine = Global.getCombatEngine();
                        if (engine.getPlayerShip() == ship) {
                            if (!ship.isPullBackFighters()) {
				engine.maintainStatusForPlayerShip("RecoveryShuttles", "graphics/icons/hullsys/reserve_deployment.png",
                            "Recovery Shuttles", "-40% Replacement Rate Loss", false);
                            }
			}
		} else {
			stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).unmodifyMult("RecoveryShuttles");
		}
		
	}
		
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) ((1f - CREW_LOSS_MULT) * 100f) + "%";
		return null;
	}
        
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
                        tooltip.addPara("Reduction of casualties suffered increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "90" + "%");
                        tooltip.addPara("Reduces the rate at which fighter replacement rate is lost when engaging by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "40" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("recovery_shuttles") || ship.getVariant().getSModdedBuiltIns().contains("recovery_shuttles")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
                        tooltip.addPara("Reduction of casualties suffered increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "90" + "%");
                        tooltip.addPara("Reduces the rate at which fighter replacement rate is lost when engaging by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "40" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
                        tooltip.addPara("Reduction of casualties suffered increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "90" + "%");		
                        tooltip.addPara("Reduces the rate at which fighter replacement rate is lost when engaging by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "40" + "%");
		}
    }
	
	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().hasHullMod(HullMods.AUTOMATED)) return false;
		
		//int bays = (int) ship.getMutableStats().getNumFighterBays().getBaseValue();
		int bays = (int) ship.getMutableStats().getNumFighterBays().getModifiedValue();
//		if (ship != null && ship.getVariant().getHullSpec().getBuiltInWings().size() >= bays) {
//			return false;
//		}
		return ship != null && bays > 0; 
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getVariant().hasHullMod(HullMods.AUTOMATED)) {
			return "Can not be installed on automated ships";
		}
		return "Ship does not have fighter bays";
	}
}




