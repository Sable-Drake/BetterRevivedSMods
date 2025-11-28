package data.hullmods;

import com.fs.starfarer.api.Global;
import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class NavRelay extends BaseHullMod {
	
        public static float FSPEED_BONUS = 20f;

	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 2f);
		mag.put(HullSize.DESTROYER, 3f);
		mag.put(HullSize.CRUISER, 4f);
		mag.put(HullSize.CAPITAL_SHIP, 5f);
	}
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		stats.getDynamic().getMod(Stats.COORDINATED_MANEUVERS_FLAT).modifyFlat(id, (Float) mag.get(hullSize));
	}
	
	public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
		if (ship.getVariant().getSMods().contains("nav_relay") || ship.getVariant().getSModdedBuiltIns().contains("nav_relay")) {
			MutableShipStatsAPI stats = fighter.getMutableStats();
			stats.getMaxSpeed().modifyPercent(id, FSPEED_BONUS);
			stats.getAcceleration().modifyPercent(id, FSPEED_BONUS);
			stats.getDeceleration().modifyPercent(id, FSPEED_BONUS);
                        stats.getTurnAcceleration().modifyPercent(id, FSPEED_BONUS);
			stats.getMaxTurnRate().modifyPercent(id, FSPEED_BONUS);
		}
	}
        
	@Override
	public void advanceInCombat(ShipAPI ship, float amount) {
		if (!ship.isAlive()) return;
		MutableShipStatsAPI stats = ship.getMutableStats();
		if ((ship.getVariant().getSMods().contains("nav_relay") || (ship.getVariant().getSModdedBuiltIns().contains("nav_relay") && ship.getMutableStats().getMaxSpeed().getPercentStatMod("coord_maneuvers_bonus") != null))) {
                    float bonus = 0f;
                    if (ship.getMutableStats().getMaxSpeed().getPercentStatMod("coord_maneuvers_bonus") != null) {
                            bonus = ship.getMutableStats().getMaxSpeed().getPercentStatMod("coord_maneuvers_bonus").getValue();
                        }
                        stats.getAcceleration().modifyPercent("nav_relay", bonus);
			stats.getDeceleration().modifyPercent("nav_relay", bonus);
			stats.getTurnAcceleration().modifyPercent("nav_relay", bonus * 2f);
			stats.getMaxTurnRate().modifyPercent("nav_relay", bonus);
		}
	}
        
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue() + "%";
		if (index == 1) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue() + "%";
		if (index == 2) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue() + "%";
		if (index == 3) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue() + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
                        tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("The total nav rating of your fleet also increases the maneuverability of this ship.", Misc.getGrayColor(), 10f);
			tooltip.addPara("Fighters gain %s top speed and maneuverability.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+20%");
			return;
		} else if (ship.getVariant().getSMods().contains("nav_relay") || ship.getVariant().getSModdedBuiltIns().contains("nav_relay")) {
                        tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
                        tooltip.addPara("The total nav rating of your fleet also increases the maneuverability of this ship.", Misc.getPositiveHighlightColor(), 10f);
			tooltip.addPara("Fighters gain %s top speed and maneuverability.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+20%");
                } else if (!isForModSpec) {
                        tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
                        tooltip.addPara("The total nav rating of your fleet also increases the maneuverability of this ship.", Misc.getGrayColor(), 10f);
			tooltip.addPara("Fighters gain %s top speed and maneuverability.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+20%");
		}
    }
}




