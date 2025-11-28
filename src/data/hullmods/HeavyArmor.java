package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class HeavyArmor extends BaseHullMod {

	public static final float MANEUVER_PENALTY = 10f;
	public static final float SMANEUVER_PENALTY = 25f;
	public static final float SMOD_ARMOR_BONUS = 20f; // Custom bonus: +20% armor
	
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 150f);
		mag.put(HullSize.DESTROYER, 300f);
		mag.put(HullSize.CRUISER, 400f);
		mag.put(HullSize.CAPITAL_SHIP, 500f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		
		if (stats.getVariant().getSMods().contains("heavyarmor") || stats.getVariant().getSModdedBuiltIns().contains("heavyarmor")) {
			// Mod penalty: -25% maneuverability
			stats.getAcceleration().modifyMult(id, 1f - SMANEUVER_PENALTY * 0.01f);
                        stats.getDeceleration().modifyMult(id, 1f - SMANEUVER_PENALTY * 0.01f);
                        stats.getTurnAcceleration().modifyMult(id, 1f - SMANEUVER_PENALTY * 0.01f);
                        stats.getMaxTurnRate().modifyMult(id, 1f - SMANEUVER_PENALTY * 0.01f);
			// Custom bonus: +20% armor
			stats.getArmorBonus().modifyPercent(id, SMOD_ARMOR_BONUS);
		}  else {
			stats.getAcceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
                        stats.getDeceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
                        stats.getTurnAcceleration().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
                        stats.getMaxTurnRate().modifyMult(id, 1f - MANEUVER_PENALTY * 0.01f);
		}
                stats.getArmorBonus().modifyFlat(id, (Float) mag.get(hullSize));
		
//		stats.getDynamic().getMod(Stats.MAX_LOGISTICS_HULLMODS_MOD).modifyFlat(id, 1);
//		//stats.getCargoMod().modifyFlat(id, -70);
//		stats.getTimeMult().modifyMult(id, 4f);
//		//if (stats.getEntity() != null && stats.getEntity() == Global.getCombatEngine().getPlayerShip()) {
//			Global.getCombatEngine().getTimeMult().modifyMult(id, 1f / 4f);
//		//}
		//stats.getNumFighterBays().modifyFlat(id, -1f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE))
			.intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 4) return "" + (int) MANEUVER_PENALTY + "%";
		return null;
		//if (index == 0) return "" + ((Float) mag.get(hullSize)).intValue();
		//return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Maneuverability penalty increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25%");
			tooltip.addPara("Increases the ship's armor by an additional %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20%");
			return;
		} else if (ship.getVariant().getSMods().contains("heavyarmor") || ship.getVariant().getSModdedBuiltIns().contains("heavyarmor")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Maneuverability penalty increased to %s.", 10f, Misc.getNegativeHighlightColor(), Misc.getHighlightColor(), "25%");
			tooltip.addPara("Increases the ship's armor by an additional %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "20%");
		//} else if (ship.getHullSpec().isBuiltInMod("heavyarmor")) {
			//No penalties!
                } else if (!isForModSpec) {
					tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Maneuverability penalty increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25%");
			tooltip.addPara("Increases the ship's armor by an additional %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		//if (ship.getCargoMod().computeEffective(ship.getHullSpec().getCargo()) < 70) return false;

		return true;
	}
}
