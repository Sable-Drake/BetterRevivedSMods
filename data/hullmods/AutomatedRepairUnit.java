package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class AutomatedRepairUnit extends BaseHullMod {

	//public static final float REPAIR_RATE_BONUS = 50f;
	//public static final float CR_RECOVERY_BONUS = 50f;
	public static final float REPAIR_BONUS = 50f;
	public static final float SREPAIR_BONUS = 75f;
        public static final float INSTA_REPAIR_BONUS = 0.15f;
        public static final float CR_DEPLOYMENT_REDUCTION = 0.85f;
        public static final float HULL_DAMAGE_REDUCTION = 10f;
	public static final float SMOD_OVERLOAD_REDUCTION = 25f; // Vanilla bonus (user requested -25% instead of vanilla -33%)
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("autorepair") || stats.getVariant().getSModdedBuiltIns().contains("autorepair")) {
			// Mod bonuses
			stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - SREPAIR_BONUS * 0.01f);
			stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - SREPAIR_BONUS * 0.01f);
                        stats.getDynamic().getMod(Stats.INSTA_REPAIR_FRACTION).modifyFlat(id, INSTA_REPAIR_BONUS);
                        stats.getCRPerDeploymentPercent().modifyMult(id, CR_DEPLOYMENT_REDUCTION);
			// Vanilla bonus: -25% overload duration (user requested -25% instead of vanilla -33%)
			stats.getOverloadTimeMod().modifyMult(id, 1f - SMOD_OVERLOAD_REDUCTION * 0.01f);
			//stats.getHullDamageTakenMult().modifyMult(id, 1f - HULL_DAMAGE_REDUCTION / 100f);
		} else {
			stats.getCombatEngineRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
			stats.getCombatWeaponRepairTimeMult().modifyMult(id, 1f - REPAIR_BONUS * 0.01f);
		}
//		stats.getBaseCRRecoveryRatePercentPerDay().modifyPercent(id, CR_RECOVERY_BONUS);
//		stats.getRepairRatePercentPerDay().modifyPercent(id, REPAIR_RATE_BONUS);
//		stats.getSuppliesToRecover().modifyPercent(id, LOGISTICS_PENALTY);
//		stats.getSuppliesPerMonth().modifyPercent(id, LOGISTICS_PENALTY);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) REPAIR_BONUS + "%";
//		if (index == 1) return "" + (int) CR_RECOVERY_BONUS + "%";
		//if (index == 2) return "" + (int) LOGISTICS_PENALTY;
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Time reduction to repair weapons and engines increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "75" + "%");
			tooltip.addPara("%s of hull and armor damage taken during combat will be repaired after combat ends, at no cost", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
                        tooltip.addPara("%s CR loss from deployment", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-15" + "%");
			tooltip.addPara("Reduces overload duration by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("autorepair") || ship.getVariant().getSModdedBuiltIns().contains("autorepair")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Time reduction to repair weapons and engines increased to %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "75" + "%");
			tooltip.addPara("%s of hull and armor damage taken during combat will be repaired after combat ends, at no cost", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "15" + "%");
                        tooltip.addPara("%s CR loss from deployment", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-15" + "%");
			tooltip.addPara("Reduces overload duration by %s", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "25" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Time reduction to repair weapons and engines increased to %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "75" + "%");
			tooltip.addPara("%s of hull and armor damage taken during combat will be repaired after combat ends, at no cost", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "15" + "%");
                        tooltip.addPara("%s CR loss from deployment", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-15" + "%");
			tooltip.addPara("Reduces overload duration by %s", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "25" + "%");
		}
    }

}
