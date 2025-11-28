package data.hullmods;

import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class MilitarizedSubsystems extends BaseLogisticsHullMod {
	private static int BURN_LEVEL_BONUS = 1;
	private static float MAINTENANCE_PERCENT = 100;
	private static float SFLUX_PERCENT = 15;
	private static float SARMOR_BONUS = 20;
	private static float FLUX_DISSIPATION_PERCENT = 10;
	private static float ARMOR_BONUS = 10;
        
        
	private static float FLUX_CAPACITY_PERCENT = 15;
	private static float SHULL_PERCENT = 10;
	private static float SARMOR_PERCENT = 10;
	private static float SMANEUVER_PERCENT = 20;
	private static float SPD_RANGE = 30;
	private static float SFIGHTER_DAMAGE_BONUS = 25f;
	private static float SMISSILE_DAMAGE_BONUS = 25f;
        
	//private static final float DEPLOY_COST_MULT = 0.7f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getSensorStrength().unmodify(HullMods.CIVGRADE);
		stats.getSensorProfile().unmodify(HullMods.CIVGRADE);
		
		stats.getMaxBurnLevel().modifyFlat(id, BURN_LEVEL_BONUS);

		float mult = getEffectMult(stats);
		if (stats.getVariant().getSMods().contains("militarized_subsystems") || stats.getVariant().getSModdedBuiltIns().contains("militarized_subsystems")) {
			stats.getFluxDissipation().modifyPercent(id, SFLUX_PERCENT * mult);
			stats.getEffectiveArmorBonus().modifyFlat(id, SARMOR_BONUS * mult);
                        
			stats.getDamageToFighters().modifyFlat(id, SFIGHTER_DAMAGE_BONUS / 100f * mult);
			stats.getDamageToMissiles().modifyFlat(id, SMISSILE_DAMAGE_BONUS / 100f * mult);
			stats.getBeamPDWeaponRangeBonus().modifyFlat(id, SPD_RANGE * mult);
			stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, SPD_RANGE * mult);
			stats.getAcceleration().modifyPercent(id, SMANEUVER_PERCENT * mult);
			stats.getDeceleration().modifyPercent(id, SMANEUVER_PERCENT * mult);
			stats.getTurnAcceleration().modifyPercent(id, SMANEUVER_PERCENT * 2f * mult);
			stats.getMaxTurnRate().modifyPercent(id, SMANEUVER_PERCENT * mult);
                        
			stats.getHullBonus().modifyPercent(id, SHULL_PERCENT * mult);
			stats.getArmorBonus().modifyPercent(id, SARMOR_PERCENT * mult);
			stats.getFluxCapacity().modifyPercent(id, FLUX_CAPACITY_PERCENT * mult);
		} else {
			stats.getFluxDissipation().modifyPercent(id, FLUX_DISSIPATION_PERCENT * mult);
			stats.getEffectiveArmorBonus().modifyFlat(id, ARMOR_BONUS * mult);
		}
		
		//stats.getSuppliesPerMonth().modifyPercent(id, MAINTENANCE_PERCENT);
		stats.getMinCrewMod().modifyPercent(id, MAINTENANCE_PERCENT);
		
		stats.getDynamic().getMod(Stats.ACT_AS_COMBAT_SHIP).modifyFlat(id, 1f);
		
	}
	
	public static float getEffectMult(MutableShipStatsAPI stats) {
		float bonus = getBonusPercent(stats);
		return 1f + bonus / 100f;
	}
	
	//public static float getBonusPercent(ShipAPI ship) {
	public static float getBonusPercent(MutableShipStatsAPI stats) {
		if (Global.getSettings().getCurrentState() == GameState.TITLE) return 0f;
		//FleetMemberAPI member = ship.getFleetMember();
		MutableCharacterStatsAPI cStats = null;
		if (stats == null) {
			cStats = Global.getSector().getPlayerStats();
		} else {
			FleetMemberAPI member = stats.getFleetMember();
			if (member == null) return 0f;
			PersonAPI commander = member.getFleetCommanderForStats();
			if (commander == null) {
				commander = member.getFleetCommander();
			}
			if (commander == null) return 0f;
			cStats = commander.getStats();
		}
		float bonus = cStats.getDynamic().getMod(Stats.AUXILIARY_EFFECT_ADD_PERCENT).computeEffective(0f);
		return Math.round(bonus);
	}
	
//	@Override
//	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
//		if (ship == null) return;
//		if (Global.getSettings().getCurrentState() == GameState.TITLE) return;
//		
//		float bonus = getBonusPercent(ship);
//		if(bonus <= 0) return;
//		
//		float opad = 10f;
//		tooltip.addSectionHeading("Auxiliary Support", Alignment.MID, opad);
//	}



	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + BURN_LEVEL_BONUS;
		//if (index == 1) return "" + (int)Math.round((1f - DEPLOY_COST_MULT) * 100f) + "%";
		float mult = getEffectMult(null);
		if (index == 1) return "" + (int) Math.round(FLUX_DISSIPATION_PERCENT * mult) + "%";
		if (index == 2) return "" + (int) Math.round(ARMOR_BONUS * mult);
		if (index == 3) return "" + (int)Math.round(MAINTENANCE_PERCENT) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Flux dissipation bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
			tooltip.addPara("Armor bonus for the damage reduction calculation increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20");
			tooltip.addPara("+%s Hull integrity", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10%");
			tooltip.addPara("+%s Armor bonus", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10%");
			tooltip.addPara("+%s Flux capacity", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10%");
			tooltip.addPara("+%s Maneuverability", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "20" + "%");
			tooltip.addPara("+%s Point-defense weapon range", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "30");
			tooltip.addPara("+%s Damage to fighter and missile", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
                        return;
		} else if (ship.getVariant().getSMods().contains("militarized_subsystems") || ship.getVariant().getSModdedBuiltIns().contains("militarized_subsystems")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			float mult = getEffectMult(null);
			tooltip.addPara("Flux dissipation bonus increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int) Math.round(SFLUX_PERCENT * mult) + "%");
			tooltip.addPara("Armor bonus for the damage reduction calculation increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int) Math.round(SARMOR_BONUS * mult));
			tooltip.addPara("+%s Hull integrity", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int) Math.round(SHULL_PERCENT * mult) + "%");
			tooltip.addPara("+%s Armor bonus", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int) Math.round(SARMOR_PERCENT * mult) + "%");
			tooltip.addPara("+%s Flux capacity", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int)Math.round(SFLUX_PERCENT * mult) + "%");
                        tooltip.addPara("+%s Maneuverability", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int) Math.round(SMANEUVER_PERCENT * mult) + "%");
			tooltip.addPara("+%s Point-defense weapon range", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int) Math.round(SPD_RANGE * mult));
			tooltip.addPara("+%s Damage to fighter and missile", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+(int)Math.round(SFIGHTER_DAMAGE_BONUS * mult) + "%");
                } else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			float mult = getEffectMult(null);
			tooltip.addPara("Flux dissipation bonus increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int) Math.round(SFLUX_PERCENT * mult) + "%");
			tooltip.addPara("Armor bonus for the damage reduction calculation increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int) Math.round(SARMOR_BONUS * mult));
			tooltip.addPara("+%s Hull integrity", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int) Math.round(SHULL_PERCENT * mult) + "%");
			tooltip.addPara("+%s Armor bonus", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int) Math.round(SARMOR_PERCENT * mult) + "%");
			tooltip.addPara("+%s Flux capacity", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int)Math.round(SFLUX_PERCENT * mult) + "%");
                        tooltip.addPara("+%s Maneuverability", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int) Math.round(SMANEUVER_PERCENT * mult) + "%");
			tooltip.addPara("+%s Point-defense weapon range", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int) Math.round(SPD_RANGE * mult));
			tooltip.addPara("+%s Damage to fighter and missile", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), ""+(int)Math.round(SFIGHTER_DAMAGE_BONUS * mult) + "%");
		}
    }

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return ship.getVariant().hasHullMod(HullMods.CIVGRADE) && super.isApplicableToShip(ship);
	}

	@Override
	public String getUnapplicableReason(ShipAPI ship) {
		if (!ship.getVariant().hasHullMod(HullMods.CIVGRADE)) {
			return "Can only be installed on civilian-grade hulls";
		}
		return super.getUnapplicableReason(ship);
	}
	
	
	
	
}

