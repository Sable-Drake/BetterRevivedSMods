package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class DedicatedTargetingCore extends BaseHullMod {

	private static Map mag = new HashMap();
	private static Map smag = new HashMap();
	static {
		smag.put(HullSize.FIGHTER, 0f);
		smag.put(HullSize.FRIGATE, 0f);
		smag.put(HullSize.DESTROYER, 0f);
		smag.put(HullSize.CRUISER, 40f);
		smag.put(HullSize.CAPITAL_SHIP, 60f);
		mag.put(HullSize.FIGHTER, 0f);
		mag.put(HullSize.FRIGATE, 0f);
		mag.put(HullSize.DESTROYER, 0f);
		mag.put(HullSize.CRUISER, 35f);
		mag.put(HullSize.CAPITAL_SHIP, 50f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
		if (index == 1) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Range bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "40%/60%");
			return;
		} else if (ship.getVariant().getSMods().contains("dedicated_targeting_core") || ship.getVariant().getSModdedBuiltIns().contains("dedicated_targeting_core")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Range bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "40%/60%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Range bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "40%/60%");
		}
    }
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("dedicated_targeting_core") || stats.getVariant().getSModdedBuiltIns().contains("dedicated_targeting_core")) {
			stats.getBallisticWeaponRangeBonus().modifyPercent(id, (Float) smag.get(hullSize));
			stats.getEnergyWeaponRangeBonus().modifyPercent(id, (Float) smag.get(hullSize));
		} else {
			stats.getBallisticWeaponRangeBonus().modifyPercent(id, (Float) mag.get(hullSize));
			stats.getEnergyWeaponRangeBonus().modifyPercent(id, (Float) mag.get(hullSize));
		}
		
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return (ship.getHullSize() == HullSize.CAPITAL_SHIP || ship.getHullSize() == HullSize.CRUISER) &&
				!ship.getVariant().getHullMods().contains("targetingunit") &&
				!ship.getVariant().getHullMods().contains("advancedcore");
	}
	
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getHullSize() != HullSize.CAPITAL_SHIP && ship.getHullSize() != HullSize.CRUISER) {
			return "Can only be installed on cruisers and capital ships";
		}
		if (ship.getVariant().getHullMods().contains("targetingunit")) {
			return "Incompatible with Integrated Targeting Unit";
		}
		if (ship.getVariant().getHullMods().contains("advancedcore")) {
			return "Incompatible with Advanced Targeting Core";
		}
		
		return null;
	}
	
}
