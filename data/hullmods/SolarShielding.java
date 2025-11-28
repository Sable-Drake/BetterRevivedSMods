package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class SolarShielding extends BaseLogisticsHullMod {

	public static final float CORONA_EFFECT_REDUCTION = 0.25f;
	public static final float ENERGY_DAMAGE_REDUCTION = 0.8f;
	public static final float SENERGY_DAMAGE_REDUCTION = 0.67f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("solar_shielding") || stats.getVariant().getSModdedBuiltIns().contains("solar_shielding")) {
			stats.getEnergyDamageTakenMult().modifyMult(id, SENERGY_DAMAGE_REDUCTION);
			stats.getEnergyShieldDamageTakenMult().modifyMult(id, SENERGY_DAMAGE_REDUCTION);
		} else {
			stats.getEnergyDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);
			stats.getEnergyShieldDamageTakenMult().modifyMult(id, ENERGY_DAMAGE_REDUCTION);
			//stats.getBeamDamageTakenMult().modifyMult(id, BEAM_DAMAGE_REDUCTION);
		}
		stats.getDynamic().getStat(Stats.CORONA_EFFECT_MULT).modifyMult(id, CORONA_EFFECT_REDUCTION);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) Math.round((1f - CORONA_EFFECT_REDUCTION) * 100f) + "%";
		if (index == 1) return "" + (int) Math.round((1f - ENERGY_DAMAGE_REDUCTION) * 100f) + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Energy damage reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("solar_shielding") || ship.getVariant().getSModdedBuiltIns().contains("solar_shielding")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Energy damage reduction increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "33" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Energy damage reduction increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "33" + "%");
		}
    }

}
