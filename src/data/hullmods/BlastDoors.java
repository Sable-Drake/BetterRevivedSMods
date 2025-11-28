package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class BlastDoors extends BaseHullMod {

	public static final float HULL_BONUS = 20f;
	public static final float CASUALTY_REDUCTION = 50f; // Mod base (vanilla is 60%)
	public static final float VANILLA_CASUALTY_REDUCTION = 60f; // Vanilla base
	public static final float SMOD_CASUALTY_BONUS = 25f; // Vanilla S-Mod bonus (total: 85%)
    public static final float HE_REDUCTION = 25f; // Mod bonus
	public static float ARMOR_DAMAGE_REDUCTION = 10f; // Mod bonus
	//public static final float HULL_DAMAGE_CR_MULT = 0.25f;
	

	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
//		stats.getCargoMod().modifyPercent(id, -50f);
//		stats.getCargoMod().modifyPercent(id + "sfsdfd", +25f);
//		stats.getMaxCrewMod().modifyPercent(id, 100);
		if (stats.getVariant().getSMods().contains("blast_doors") || stats.getVariant().getSModdedBuiltIns().contains("blast_doors")) {
			// Vanilla bonus: -85% total crew casualties (60% base + 25% S-Mod)
			stats.getCrewLossMult().modifyMult(id, 1f - (VANILLA_CASUALTY_REDUCTION + SMOD_CASUALTY_BONUS) * 0.01f);
			// Mod bonuses
                    stats.getHighExplosiveDamageTakenMult().modifyMult(id, 1f - HE_REDUCTION / 100f);
					stats.getArmorDamageTakenMult().modifyMult(id, 1f - ARMOR_DAMAGE_REDUCTION / 100f);
		} else {
			stats.getCrewLossMult().modifyMult(id, 1f - CASUALTY_REDUCTION * 0.01f);
		}
                stats.getHullBonus().modifyPercent(id, HULL_BONUS);
		//stats.getDynamic().getStat(Stats.HULL_DAMAGE_CR_LOSS).modifyMult(id, HULL_DAMAGE_CR_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HULL_BONUS + "%";
		if (index == 1) return "" + (int) CASUALTY_REDUCTION + "%";
		//if (index == 2) return "" + (int) ((1f - HULL_DAMAGE_CR_MULT) * 100f);
		return null;
	}
	
	public String getSModDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) Math.round(VANILLA_CASUALTY_REDUCTION + SMOD_CASUALTY_BONUS) + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases the crew casualty reduction to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "85" + "%");
			tooltip.addPara("%s high explosive damage taken by hull and armor", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-25%");
			tooltip.addPara("%s armor damage taken", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-10%");
			return;
		} else if (ship.getVariant().getSMods().contains("blast_doors") || ship.getVariant().getSModdedBuiltIns().contains("blast_doors")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Increases the crew casualty reduction to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "85" + "%");
			tooltip.addPara("%s high explosive damage taken by hull and armor", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-25%");
			tooltip.addPara("%s armor damage taken", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "-10%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Increases the crew casualty reduction to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "85" + "%");
			tooltip.addPara("%s high explosive damage taken by hull and armor", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-25%");
			tooltip.addPara("%s armor damage taken", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "-10%");
		}
    }
}
