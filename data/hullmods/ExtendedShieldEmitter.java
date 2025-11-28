package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ExtendedShieldEmitter extends BaseHullMod {

	public static final float SHIELD_ARC_BONUS = 60f;
	public static final float SHIELD_EXCESS_ARC = 360f;
	public static final float SHIELD_EXCESS_ARC_DIVISOR = 10f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("extendedshieldemitter") || stats.getVariant().getSModdedBuiltIns().contains("extendedshieldemitter")) {
			stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS*2f);
			//Using this method created issues involving hullmod orders.
            //float SHIELD_MODIFIED_ARC = stats.getShieldArcBonus().computeEffective(stats.getVariant().getHullSpec().getShieldSpec().getArc());
			//if (SHIELD_MODIFIED_ARC > SHIELD_EXCESS_ARC) {
            //float FLUX_CAPACITY_SHIELD_ARC_BONUS = (SHIELD_MODIFIED_ARC-SHIELD_EXCESS_ARC)/SHIELD_EXCESS_ARC_DIVISOR*stats.getVariant().getHullSpec().getShieldSpec().getUpkeepCost();
			//if (FLUX_CAPACITY_SHIELD_ARC_BONUS > 20f*stats.getVariant().getHullSpec().getShieldSpec().getUpkeepCost()) {
			//	FLUX_CAPACITY_SHIELD_ARC_BONUS = 20f*stats.getVariant().getHullSpec().getShieldSpec().getUpkeepCost();
			//	stats.getFluxCapacity().modifyFlat(id, Math.round(FLUX_CAPACITY_SHIELD_ARC_BONUS));
			//} else {
            //    stats.getFluxCapacity().modifyFlat(id, Math.round(FLUX_CAPACITY_SHIELD_ARC_BONUS));
            //}}
		} else {
			stats.getShieldArcBonus().modifyFlat(id, SHIELD_ARC_BONUS);
		}
	}
	
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		if (ship.getVariant().getSMods().contains("extendedshieldemitter") || ship.getVariant().getSModdedBuiltIns().contains("extendedshieldemitter")) {
            float SHIELD_MODIFIED_ARC = ship.getMutableStats().getShieldArcBonus().computeEffective(ship.getHullSpec().getShieldSpec().getArc());
			if (SHIELD_MODIFIED_ARC > SHIELD_EXCESS_ARC) {
            float FLUX_CAPACITY_SHIELD_ARC_BONUS = (SHIELD_MODIFIED_ARC-SHIELD_EXCESS_ARC)/SHIELD_EXCESS_ARC_DIVISOR*ship.getHullSpec().getShieldSpec().getUpkeepCost();
			if (FLUX_CAPACITY_SHIELD_ARC_BONUS > 12f*ship.getHullSpec().getShieldSpec().getUpkeepCost()) {
				FLUX_CAPACITY_SHIELD_ARC_BONUS = 12f*ship.getHullSpec().getShieldSpec().getUpkeepCost();
			}
                ship.getMutableStats().getFluxCapacity().modifyFlat(id, Math.round(FLUX_CAPACITY_SHIELD_ARC_BONUS));
            }
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) SHIELD_ARC_BONUS;
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "60");
			tooltip.addPara("Excessive shield arc beyond 360 degree increase ship's flux capacity, scaling with shield upkeep.", Misc.getGrayColor(), 10f);
			return;
		} else if (ship.getVariant().getSMods().contains("extendedshieldemitter")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "60");
			float SHIELD_MODIFIED_ARC = ship.getMutableStats().getShieldArcBonus().computeEffective(ship.getHullSpec().getShieldSpec().getArc());
            if (SHIELD_MODIFIED_ARC > SHIELD_EXCESS_ARC) {
            float FLUX_CAPACITY_SHIELD_ARC_BONUS = (SHIELD_MODIFIED_ARC-SHIELD_EXCESS_ARC)/SHIELD_EXCESS_ARC_DIVISOR*ship.getHullSpec().getShieldSpec().getUpkeepCost();
				if (FLUX_CAPACITY_SHIELD_ARC_BONUS > 12f*ship.getHullSpec().getShieldSpec().getUpkeepCost()) {
					FLUX_CAPACITY_SHIELD_ARC_BONUS = 12f*ship.getHullSpec().getShieldSpec().getUpkeepCost();
				}
				tooltip.addPara("Increased flux capacity from excessive shield arc: %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+Misc.	getRoundedValue(FLUX_CAPACITY_SHIELD_ARC_BONUS));
            } else {
				tooltip.addPara("Excessive shield arc beyond 360 degree increase ship's flux capacity, scaling with shield upkeep.", Misc.getPositiveHighlightColor(), 10f);
			}
			
		} else if (ship.getVariant().getSModdedBuiltIns().contains("extendedshieldemitter")) {
			tooltip.addSectionHeading("Built-in bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
 			tooltip.addPara("Built-in Bonus: Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "60");
			float SHIELD_MODIFIED_ARC = ship.getMutableStats().getShieldArcBonus().computeEffective(ship.getHullSpec().getShieldSpec().getArc());
            if (SHIELD_MODIFIED_ARC > SHIELD_EXCESS_ARC) {
            float FLUX_CAPACITY_SHIELD_ARC_BONUS = (SHIELD_MODIFIED_ARC-SHIELD_EXCESS_ARC)/SHIELD_EXCESS_ARC_DIVISOR*ship.getMutableStats().getVariant().getHullSpec().getShieldSpec().getUpkeepCost();
				if (FLUX_CAPACITY_SHIELD_ARC_BONUS > 12f*ship.getHullSpec().getShieldSpec().getUpkeepCost()) {
					FLUX_CAPACITY_SHIELD_ARC_BONUS = 12f*ship.getHullSpec().getShieldSpec().getUpkeepCost();
				}
				tooltip.addPara("Increased flux capacity from excessive shield arc: %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), ""+Misc.	getRoundedValue(FLUX_CAPACITY_SHIELD_ARC_BONUS));
            } else {
				tooltip.addPara("Excessive shield arc beyond 360 degree increase ship's flux capacity, scaling with shield upkeep.", Misc.getPositiveHighlightColor(), 10f);
			}
        } else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases the shield's coverage by an additional %s degrees.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "60");
			tooltip.addPara("Excessive shield arc beyond 360 degree increase ship's flux capacity, scaling with shield upkeep.", Misc.getGrayColor(), 10f);
		}
    }

	public boolean isApplicableToShip(ShipAPI ship) {
		return ship != null && ship.getShield() != null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship has no shields";
	}
}
