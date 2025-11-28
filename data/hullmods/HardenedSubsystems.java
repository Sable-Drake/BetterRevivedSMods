package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class HardenedSubsystems extends BaseHullMod {

	public static final float SPEAK_BONUS_FLAT = 30f;
	public static final float PEAK_BONUS_PERCENT = 50f;
	public static final float DEGRADE_REDUCTION_PERCENT = 25f;
        public static final float MALFUNCTION_REDUCTION = 0.5f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("hardened_subsystems") || stats.getVariant().getSModdedBuiltIns().contains("hardened_subsystems")) {
			stats.getPeakCRDuration().modifyFlat(id, SPEAK_BONUS_FLAT);
			stats.getWeaponMalfunctionChance().modifyMult(id, MALFUNCTION_REDUCTION);
                        stats.getEngineMalfunctionChance().modifyMult(id, MALFUNCTION_REDUCTION);
                        stats.getCriticalMalfunctionChance().modifyMult(id, MALFUNCTION_REDUCTION);
		}
                stats.getPeakCRDuration().modifyPercent(id, PEAK_BONUS_PERCENT);
                stats.getCRLossPerSecondPercent().modifyMult(id, 1f - DEGRADE_REDUCTION_PERCENT / 100f);
		
	}
	

	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) PEAK_BONUS_PERCENT + "%";
		if (index == 1) return "" + (int) DEGRADE_REDUCTION_PERCENT + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s seconds peak operating time.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+30");
			tooltip.addPara("%s less chance to suffer from weapon, engine, and critical malfunctions", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-50" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("hardened_subsystems") || ship.getVariant().getSModdedBuiltIns().contains("hardened_subsystems")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s seconds peak operating time.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+30");
			tooltip.addPara("%s less chance to suffer from weapon, engine, and critical malfunctions", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-50" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s seconds peak operating time.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+30");
			tooltip.addPara("%s less chance to suffer from weapon, engine, and critical malfunctions", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-50" + "%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && (ship.getHullSpec().getNoCRLossTime() < 10000 || ship.getHullSpec().getCRLossPerSecond() > 0); 
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship does not suffer from CR degradation";
	}

}
