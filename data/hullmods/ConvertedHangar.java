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
import static com.fs.starfarer.api.impl.hullmods.DefectiveManufactory.DAMAGE_INCREASE;
import static com.fs.starfarer.api.impl.hullmods.DefectiveManufactory.SPEED_REDUCTION;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;

public class ConvertedHangar extends BaseHullMod {

	public static final int CREW_REQ = 20;
	//public static final int CARGO_REQ = 80;
	public static final int ALL_FIGHTER_COST_PERCENT = 50;
	public static final int BOMBER_COST_PERCENT = 100;
	
	//private static Map mag = new HashMap();
        private static Map RATE_INCREASE_MODIFIER = new HashMap();
	static {
		/*mag.put(HullSize.FRIGATE, 0f);
		mag.put(HullSize.DESTROYER, 75f);
		mag.put(HullSize.CRUISER, 50f);
		mag.put(HullSize.CAPITAL_SHIP, 25f);*/
                RATE_INCREASE_MODIFIER.put(HullSize.DEFAULT, 0f);
                RATE_INCREASE_MODIFIER.put(HullSize.FIGHTER, 0f);
                RATE_INCREASE_MODIFIER.put(HullSize.FRIGATE, 0f);
		RATE_INCREASE_MODIFIER.put(HullSize.DESTROYER, 0f);
		RATE_INCREASE_MODIFIER.put(HullSize.CRUISER, 10f);
		RATE_INCREASE_MODIFIER.put(HullSize.CAPITAL_SHIP, 25f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getFighterRefitTimeMult().modifyPercent(id, ((Float) mag.get(hullSize)));
		stats.getNumFighterBays().modifyFlat(id, 1f);
		stats.getMinCrewMod().modifyFlat(id, CREW_REQ);
		//stats.getDynamic().getMod(Stats.ALL_FIGHTER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
                if (stats.getVariant().getHullSpec().isBuiltInMod("converted_hangar")) {
                    stats.getDynamic().getMod(Stats.BOMBER_COST_MOD).modifyPercent(id, BOMBER_COST_PERCENT);
                    stats.getDynamic().getMod(Stats.FIGHTER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
                    stats.getDynamic().getMod(Stats.INTERCEPTOR_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
                    stats.getDynamic().getMod(Stats.SUPPORT_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
		} else {
                    if (stats.getVariant().getSMods().contains("converted_hangar")) {
                        stats.getDynamic().getMod(Stats.BOMBER_COST_MOD).modifyPercent(id, ALL_FIGHTER_COST_PERCENT);
                        stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyPercent(id, (Float) RATE_INCREASE_MODIFIER.get(hullSize));
                    } else {
                        stats.getDynamic().getMod(Stats.BOMBER_COST_MOD).modifyPercent(id, BOMBER_COST_PERCENT);
                    }
                    stats.getFighterRefitTimeMult().modifyMult(id, 1.5f);
                    float mult = 1f / ((100f + 50f) / 100f);
                    stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id, mult);
                    stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyMult(id, mult);
                    float penalty = 0;
                    if (!stats.getVariant().getFittedWings().isEmpty()) {
                        for (String wingid: stats.getVariant().getFittedWings()) {
                            if (wingid != null && Global.getSettings().getFighterWingSpec(wingid) != null) {
                                penalty += Math.round(Global.getSettings().getFighterWingSpec(wingid).getOpCost(stats)/5);
                            }
                        }
                        penalty = Math.max(1, penalty);
                    }
                    stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, penalty);
                    stats.getSuppliesToRecover().modifyFlat(id, penalty);
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
		if (ship.getVariant().getHullSpec().isBuiltInMod("converted_hangar")) {
                    new DefectiveManufactory().applyEffectsToFighterSpawnedByShip(fighter, ship, id);
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
                //if (index == 2) return "" + BOMBER_COST_PERCENT + "%";
		//if (index == 3) return "" + CREW_REQ;
		//if (index == 4) return "" + ALL_FIGHTER_COST_PERCENT + "%";
		//return new DefectiveManufactory().getDescriptionParam(index, hullSize, ship);
//		if (index == 0) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
//		if (index == 1) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
//		if (index == 2) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
//		if (index == 3) return "" + CREW_REQ;
//		return null;
		//if (index == 0) return "" + ((Float) mag.get(hullSize)).intValue();
		return null;
	}
	
	@Override
	public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
		return false;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
                Color h = Misc.getHighlightColor();
                float opad = 10f;
                if (ship != null && ship.getHullSpec().isBuiltInMod("converted_hangar")) {
                    float effect = 1f;
                    effect = ship.getMutableStats().getDynamic().getValue(Stats.DMOD_EFFECT_MULT);
                    tooltip.addPara("Converts the ship's standard shuttle hangar to house a fighter bay. The improvised manufactory that produces fighter chassis is unreliable, and most components have minor defects. Fighter speed reduced by %s, and damage taken increased by %s.", opad, h, (int) Math.round(SPEED_REDUCTION * 100f * effect) + "%",(int) Math.round(DAMAGE_INCREASE * 100f * effect) + "%");
                    tooltip.addPara("Increases the minimum crew required by %s to account for pilots and bay crews. Increases the ordnance point cost of bombers by %s, and of all other fighters by %s.", opad, h, "" + BOMBER_COST_PERCENT + "%", "" + CREW_REQ, "" + ALL_FIGHTER_COST_PERCENT + "%");
                } else {
                    tooltip.addPara("Converts the ship's standard shuttle hangar to house a fighter bay. The improvised flight deck, its crew, and the related machinery all lack the speed and precision found on a dedicated carrier.", opad);
                    tooltip.addPara("Increases fighter refit time by %s, and the fighter replacement rate both decays and recovers %s more slowly. Increases the ordnance point cost of bombers by %s. Increases the minimum crew by %s to account for pilots and flight crews.", opad, h, "1.5x", "1.5x", BOMBER_COST_PERCENT + "%", "" + CREW_REQ);
                    if (ship != null && ship.getMutableStats().getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).getFlatBonus("converted_hangar") != null) {
                        tooltip.addPara("Increases the ship's deployment points and supply cost to recover from deployment by at least %s for every %s ordnance points spent on fighters (%s points for the currently installed fighter wing).", opad, h, "1", "5", Misc.getRoundedValue(ship.getMutableStats().getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).getFlatBonus("converted_hangar").getValue()));
                    } else {
                        tooltip.addPara("Increases the ship's deployment points and supply cost to recover from deployment by at least %s for every %s ordnance points spent on fighters.", opad, h, "1", "5");
                    }
                }
                tooltip.addPara("Can not be installed on frigates, phase ships, or ships that already have proper fighter bays.", opad);
                if (isForModSpec) {
                    tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
                    tooltip.addPara("Increases the rate at which the fighter replacement rate recovers by %s for cruisers, and by %s for capital ships. No added effects for destroyers.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10%", "25%");
                    tooltip.addPara("Bomber OP Cost Penalty reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ALL_FIGHTER_COST_PERCENT + "%");
                    return;
                } else if (ship.getVariant().getSMods().contains("converted_hangar")) {
                    tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
                    tooltip.addPara("Increases the rate at which the fighter replacement rate recovers by %s for cruisers, and by %s for capital ships. No added effects for destroyers.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "10%", "25%");
                    tooltip.addPara("Bomber OP Cost Penalty reduced to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ALL_FIGHTER_COST_PERCENT + "%");
                } else if (!isForModSpec) {
                    tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
                    tooltip.addPara("Increases the rate at which the fighter replacement rate recovers by %s for cruisers, and by %s for capital ships. No added effects for destroyers.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10%", "25%");
                    tooltip.addPara("Bomber OP Cost Penalty reduced to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ALL_FIGHTER_COST_PERCENT + "%");
                }   
    }
	
	@Override
	public boolean affectsOPCosts() {
		return true;
	}
}



