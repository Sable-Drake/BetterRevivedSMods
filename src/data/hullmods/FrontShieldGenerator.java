package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class FrontShieldGenerator extends BaseHullMod {
	
	public static final float SHIELD_ARC = 90f;
	public static final float SPEED_MULT = 0.8f;
	
	
//	private static Map mag = new HashMap();
//	static {
//		mag.put(HullSize.FRIGATE, 40f);
//		mag.put(HullSize.DESTROYER, 30f);
//		mag.put(HullSize.CRUISER, 25f);
//		mag.put(HullSize.CAPITAL_SHIP, 15f);
//	}
	
	//public void applyEffectsAfterShipCreationFirstPass(ShipAPI ship, String id) {
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ShieldAPI shield = ship.getShield();
		if (shield == null) {
			if (ship.getVariant().getSMods().contains("frontshield") || ship.getVariant().getSModdedBuiltIns().contains("frontshield")) {
				ship.setShield(ShieldType.OMNI, 0.4f, 1.08f, SHIELD_ARC);
			} else {
				ship.setShield(ShieldType.FRONT, 0.5f, 1.2f, SHIELD_ARC);
			}
		}
	}
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getMaxSpeed().modifyFlat(id, (Float) mag.get(hullSize) * -1f);
		if (stats.getVariant().getSMods().contains("frontshield") || stats.getVariant().getSModdedBuiltIns().contains("frontshield")) {
			//stats.getMaxSpeed().modifyMult(id, SPEED_MULT*1.1875f);
		} else {
			stats.getMaxSpeed().modifyMult(id, SPEED_MULT);
		}
	}



	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) {
			return "" + (int) SHIELD_ARC;
		}
		if (index == 1) {
			return "" + (int) Math.round((1f - SPEED_MULT) * 100f) + "%";
		}
		
//		if (index == 1) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
//		if (index == 2) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
//		if (index == 3) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
//		if (index == 4) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		
		return null;
	}
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Unlocks the shield emitter to omni-directional with %s upkeep and %s improved efficiency.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20%", "+10%");
			tooltip.addPara("No top speed reduction.", Misc.getGrayColor(), 10f);
			return;
		} else if (ship.getVariant().getSMods().contains("frontshield") || ship.getVariant().getSModdedBuiltIns().contains("frontshield")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Unlocks the shield emitter to omni-directional with %s upkeep and %s improved efficiency.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-20%", "+10%");
			tooltip.addPara("No top speed reduction.", Misc.getPositiveHighlightColor(), 10f);
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Unlocks the shield emitter to omni-directional with %s upkeep and %s improved efficiency.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-20%", "+10%");
			tooltip.addPara("No top speed reduction.", Misc.getGrayColor(), 10f);
			
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		//return ship != null && ship.getShield() == null && ship.getPhaseCloak() == null;
		return ship != null && ship.getHullSpec().getDefenseType() == ShieldType.NONE;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getHullSpec().getDefenseType() == ShieldType.PHASE) {
			return "Ship can not have shields";
		} 
		return "Ship already has shields";
	}
}
