package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class OperationsCenter extends BaseHullMod {

	public static final float RECOVERY_BONUS = 250f;
	public static final String MOD_ID = "operations_center_mod";
        public static float DP_REDUCTION = 0.15f;
	public static float DP_REDUCTION_MAX = 10f;
        
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("operations_center") || stats.getVariant().getSModdedBuiltIns().contains("operations_center")) {
			float baseCost = stats.getSuppliesToRecover().getBaseValue();
			float reduction = Math.min(DP_REDUCTION_MAX, Math.max(1, baseCost * DP_REDUCTION));
			if (stats.getFleetMember() != null &&
                                stats.getFleetMember().getVariant() != null && 
                                (stats.getFleetMember().getCaptain().isPlayer() || stats.getFleetMember().getOwner() == 1)) {
                            stats.getSuppliesPerMonth().modifyFlat(id, -reduction);
                            stats.getSuppliesToRecover().modifyFlat(id, -reduction);
                            stats.getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).modifyFlat(id, -reduction);
                            stats.getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(MOD_ID, 5f);
                            stats.getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).modifyFlat(MOD_ID, 5f);
			} //I don't know how to find the enemy commander to apply.. so we will let any enemy ship be applicable?
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) RECOVERY_BONUS + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("If flagship or neurally linked, gain %s ECM rating, %s Nav rating, and retain command point recovery.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5%", "5%");
			tooltip.addPara("If flagship, deployment point cost and supplies to recover and maintain are reduced by %s or %s points, whichever is less.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15%", "10");
			return;
		} else if (ship.getVariant().getSMods().contains("operations_center") || ship.getVariant().getSModdedBuiltIns().contains("operations_center")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("If flagship or neurally linked, gain %s ECM rating and %s Nav rating, and retain command point recovery.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "5%", "5%");
			tooltip.addPara("If flagship, deployment point cost and supplies to recover and maintain are reduced by %s or %s points, whichever is less.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15%", "10");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("If flagship or neurally linked, gain %s ECM rating and %s Nav rating, and retain command point recovery.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "5%", "5%");
			tooltip.addPara("If flagship, deployment point cost and supplies to recover and maintain are reduced by %s or %s points, whichever is less.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15%", "10");
		}
    }
	
	
	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		CombatEngineAPI engine = Global.getCombatEngine();
		if (engine == null) return;
		
		CombatFleetManagerAPI manager = engine.getFleetManager(ship.getOriginalOwner());
		if (manager == null) return;
		
		DeployedFleetMemberAPI member = manager.getDeployedFleetMember(ship);
		if (member == null) return; // happens in refit screen etc
		
		boolean apply = ship == engine.getPlayerShip();
		PersonAPI commander = null;
		if (member.getMember() != null) {
			commander = member.getMember().getFleetCommander();
			if (member.getMember().getFleetCommanderForStats() != null) {
				commander = member.getMember().getFleetCommanderForStats();
			}
		}
                boolean hasSmod = ship.getVariant().getSMods().contains("operations_center") || ship.getVariant().getSModdedBuiltIns().contains("operations_center");
		apply |= commander != null && (ship.getCaptain() == commander || (hasSmod && ship.getOriginalCaptain() == commander));
		
		if (apply) {
			ship.getMutableStats().getDynamic().getMod(Stats.COMMAND_POINT_RATE_FLAT).modifyFlat(MOD_ID, RECOVERY_BONUS * 0.01f);
                        if (hasSmod) {
                            ship.getMutableStats().getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).modifyFlat(MOD_ID, 5f);
                            ship.getMutableStats().getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).modifyFlat(MOD_ID, 5f);
                        }
		} else {
			ship.getMutableStats().getDynamic().getMod(Stats.COMMAND_POINT_RATE_FLAT).unmodify(MOD_ID);
                        ship.getMutableStats().getDynamic().getMod(Stats.ELECTRONIC_WARFARE_FLAT).unmodify(MOD_ID);
                        ship.getMutableStats().getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).unmodify(MOD_ID);
		}
	}

}








