package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ShieldShunt extends BaseHullMod {

	public static float EMP_RESISTANCE = 50f;
	//public static float VENT_RATE_BONUS = 50f;
	public static float ARMOR_BONUS = 15f;
        public static float SARMOR_BONUS = 30f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getVentRateMult().modifyPercent(id, VENT_RATE_BONUS);
		stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
		if (stats.getVariant().getSMods().contains("shield_shunt") || stats.getVariant().getSModdedBuiltIns().contains("shield_shunt")) {
                    stats.getArmorBonus().modifyPercent(id, SARMOR_BONUS);
		}
		
	}
	
	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.setShield(ShieldType.NONE, 0f, 1f, 1f);
	}


	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int) EMP_RESISTANCE + "%";
		//if (index == 0) return "" + (int) VENT_RATE_BONUS + "%";
		if (index == 0) return "" + (int) ARMOR_BONUS + "%";
		return null;
	}
        
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Armor bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30%");
			return;
		} else if (ship.getVariant().getSMods().contains("shield_shunt") || ship.getVariant().getSModdedBuiltIns().contains("shield_shunt")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Armor bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "30%");
		} else if (!isForModSpec) {
					tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Armor bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30%");
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().getHullSpec().getShieldType() == ShieldType.NONE && 
				!ship.getVariant().hasHullMod("frontshield")) return false;
		if (ship.getVariant().hasHullMod("shield_shunt")) return true;
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shields";
	}
	
	
}









