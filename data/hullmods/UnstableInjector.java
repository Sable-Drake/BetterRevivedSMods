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

public class UnstableInjector extends BaseHullMod {

	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 25f);
		mag.put(HullSize.DESTROYER, 20f);
		mag.put(HullSize.CRUISER, 15f);
		mag.put(HullSize.CAPITAL_SHIP, 15f);
	}
	
	private static final float RANGE_MULT = 0.85f;
	private static final float FIGHTER_RATE = 25f;
	
	//private static final float ACCELERATION_BONUS = 100f;
	//private static final float EXTRA_DAMAGE = 300f;
	//private static final int BURN_LEVEL_BONUS = 1;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("unstable_injector") || stats.getVariant().getSModdedBuiltIns().contains("unstable_injector")) {
                    stats.getZeroFluxSpeedBoost().modifyFlat(id, (Float) mag.get(hullSize));
		}
		stats.getMaxSpeed().modifyFlat(id, (Float) mag.get(hullSize));
		stats.getFighterRefitTimeMult().modifyPercent(id, FIGHTER_RATE);
		
		stats.getBallisticWeaponRangeBonus().modifyMult(id, RANGE_MULT);
		stats.getEnergyWeaponRangeBonus().modifyMult(id, RANGE_MULT);
		
		//stats.getAcceleration().modifyPercent(id, ACCELERATION_BONUS);
		//stats.getEngineDamageTakenMult().modifyPercent(id, EXTRA_DAMAGE);
		
		//stats.getMaxBurnLevel().modifyFlat(id, BURN_LEVEL_BONUS);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		if (index == 4) return "" + (int) Math.round((1f - RANGE_MULT) * 100f) + "%";
		if (index == 5) return "" + (int) Math.round(FIGHTER_RATE) + "%";
//		if (index == 4) return "" + (int) ACCELERATION_BONUS;
//		//if (index == 5) return "four times";
//		if (index == 5) return "4" + Strings.X;
//		if (index == 6) return "" + BURN_LEVEL_BONUS;
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increase the ship's zero-flux engine boost to top speed by %s/%s/%s/%s, depending on hull size.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25", "20", "15", "15");
			return;
		} else if (ship.getVariant().getSMods().contains("unstable_injector") || ship.getVariant().getSModdedBuiltIns().contains("unstable_injector")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Increase the ship's zero-flux engine boost to top speed by %s/%s/%s/%s, depending on hull size.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25", "20", "15", "15");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increase the ship's zero-flux engine boost to top speed by %s/%s/%s/%s, depending on hull size.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25", "20", "15", "15");
		}
    }

}
