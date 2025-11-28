package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.DefectiveManufactory;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;


//Rename this file and this public class ConvertedHangarOld to ConvertedHangar and comment out the entry in hull_mod.csv, make sure you deleted the other one!
public class ConvertedHangarOld extends BaseHullMod {

	public static final int CREW_REQ = 20;
	//public static final int CARGO_REQ = 80;
	public static final int ALL_FIGHTER_COST_PERCENT = 50;
	public static final int BOMBER_COST_PERCENT = 100;
	public static float SPEED_REDUCTION = 0.1666f;
	public static float DAMAGE_INCREASE = 0.2f;
	
	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 0f);
		mag.put(HullSize.DESTROYER, 75f);
		mag.put(HullSize.CRUISER, 50f);
		mag.put(HullSize.CAPITAL_SHIP, 25f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getFighterRefitTimeMult().modifyPercent(id, ((Float) mag.get(hullSize)));
		stats.getNumFighterBays().modifyFlat(id, 1f);

		stats.getMinCrewMod().modifyFlat(id, CREW_REQ);
		//stats.getDynamic().getMod(Stats.ALL_FIGHTER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		if (stats.getVariant().getSMods().contains("converted_hangar") || stats.getVariant().getSModdedBuiltIns().contains("converted_hangar")) {
		stats.getDynamic().getMod(Stats.BOMBER_COST_MOD).modifyPercent(id, BOMBER_COST_PERCENT-20f);
		stats.getDynamic().getMod(Stats.FIGHTER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT-10f);
		stats.getDynamic().getMod(Stats.INTERCEPTOR_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT-10f);
		stats.getDynamic().getMod(Stats.SUPPORT_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT-10f);} else {
		stats.getDynamic().getMod(Stats.BOMBER_COST_MOD).modifyPercent(id, BOMBER_COST_PERCENT);
		stats.getDynamic().getMod(Stats.FIGHTER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		stats.getDynamic().getMod(Stats.INTERCEPTOR_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		stats.getDynamic().getMod(Stats.SUPPORT_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		}
		//stats.getCargoMod().modifyFlat(id, -CARGO_REQ);
	}
	
	public boolean isApplicableToShip(ShipAPI ship) {
		//if (ship.getCargoMod().computeEffective(ship.getHullSpec().getCargo()) < CARGO_REQ) return false;
		
		return ship != null && !ship.isFrigate() && ship.getHullSpec().getFighterBays() <= 0 &&
								//ship.getNumFighterBays() <= 0 &&
								!ship.getVariant().hasHullMod(HullMods.CONVERTED_BAY) &&
								!ship.getVariant().hasHullMod(HullMods.PHASE_FIELD);
								//ship.getHullSpec().getShieldType() != ShieldType.PHASE;		
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.isFrigate()) return "Can not be installed on a frigate";
		if (ship != null && ship.getHullSpec().getFighterBays() > 0) return "Ship has standard fighter bays";
		if (ship != null && ship.getVariant().hasHullMod(HullMods.CONVERTED_BAY)) return "Ship has fighter bays";
		//if (ship != null && ship.getNumFighterBays() > 0) return "Ship has fighter bays";
		return "Can not be installed on a phase ship";
	}
	
	public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
		float effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		if (ship.getVariant().getSMods().contains("converted_hangar") || ship.getVariant().getSModdedBuiltIns().contains("converted_hangar")) {
			MutableShipStatsAPI stats = fighter.getMutableStats();
			stats.getMaxSpeed().modifyMult(id, 1f - SPEED_REDUCTION * effect);
			stats.getArmorDamageTakenMult().modifyPercent(id, DAMAGE_INCREASE * 100f * effect);
			stats.getShieldDamageTakenMult().modifyPercent(id, DAMAGE_INCREASE * 100f * effect);
			stats.getHullDamageTakenMult().modifyPercent(id, DAMAGE_INCREASE * 100f * effect);
			fighter.setHeavyDHullOverlay();
		} else {
			new DefectiveManufactory().applyEffectsToFighterSpawnedByShip(fighter, ship, id);
		}
		
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 2) return "" + CREW_REQ;
		if (index == 3) return "" + BOMBER_COST_PERCENT + "%";
		if (index == 4) return "" + ALL_FIGHTER_COST_PERCENT + "%";
		return new DefectiveManufactory().getDescriptionParam(index, hullSize, ship);
//		if (index == 0) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
//		if (index == 1) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
//		if (index == 2) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
//		if (index == 3) return "" + CREW_REQ;
//		return null;
		//if (index == 0) return "" + ((Float) mag.get(hullSize)).intValue();
		//return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		float effect = 1f;
		if (ship != null) effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Fighter speed reduction reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), Math.round(effect * 17) + "%");
			tooltip.addPara("Fighter increased damage taken reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), Math.round(effect * 20) + "%");
			tooltip.addPara("Fighter OP Cost Penalty reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "40" + "%");
			tooltip.addPara("Bomber OP Cost Penalty reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "80" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("converted_hangar") || ship.getVariant().getSModdedBuiltIns().contains("converted_hangar")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Fighter speed reduction reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), Math.round(effect * 17) + "%");
			tooltip.addPara("Fighter increased damage taken reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), Math.round(effect * 20) + "%");
			tooltip.addPara("Fighter OP Cost Penalty reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "40" + "%");
			tooltip.addPara("Bomber OP Cost Penalty reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "80" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Fighter speed reduction reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), Math.round(effect * 17) + "%");
			tooltip.addPara("Fighter increased damage taken reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), Math.round(effect * 20) + "%");
			tooltip.addPara("Fighter OP Cost Penalty reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "40" + "%");
			tooltip.addPara("Bomber OP Cost Penalty reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "80" + "%");
		}
    }
	
	@Override
	public boolean affectsOPCosts() {
		return true;
	}
}



