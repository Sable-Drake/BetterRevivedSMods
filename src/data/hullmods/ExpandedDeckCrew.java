package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;

public class ExpandedDeckCrew extends BaseHullMod {

	public static final float RATE_DECREASE_MODIFIER = 10f;
	public static final float RATE_INCREASE_MODIFIER = 20f;
	public static final float REPLACEMENT_RATE_PERCENT = 25f;
	
	public static final float CREW_PER_DECK = 20f;
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("expanded_deck_crew") || stats.getVariant().getSModdedBuiltIns().contains("expanded_deck_crew")) {
                    stats.getFighterRefitTimeMult().modifyMult(id, 1f / ((100f + REPLACEMENT_RATE_PERCENT) / 100f));
		}
                stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(id, 1f - RATE_DECREASE_MODIFIER / 100f);
                stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyPercent(id, RATE_INCREASE_MODIFIER);
		
		
		int crew = (int) (stats.getNumFighterBays().getBaseValue() * CREW_PER_DECK);
		stats.getMinCrewMod().modifyFlat(id, crew);
	}
		
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) RATE_DECREASE_MODIFIER + "%";
		if (index == 1) return "" + (int) RATE_INCREASE_MODIFIER + "%";
		if (index == 2) return "" + (int) CREW_PER_DECK + "";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s faster fighter replacement rate.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
			return;
		} else if (ship.getVariant().getSMods().contains("expanded_deck_crew") || ship.getVariant().getSModdedBuiltIns().contains("expanded_deck_crew")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("%s faster fighter replacement rate.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+25" + "%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("%s faster fighter replacement rate.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+25" + "%");
		}
    }
	
	public boolean isApplicableToShip(ShipAPI ship) {
		int baysModified = (int) ship.getMutableStats().getNumFighterBays().getModifiedValue();
		if (baysModified <= 0) return false; // only count removed bays, not added bays for this
		
		int bays = (int) ship.getMutableStats().getNumFighterBays().getBaseValue();
//		if (ship != null && ship.getVariant().getHullSpec().getBuiltInWings().size() >= bays) {
//			return false;
//		}
		return ship != null && bays > 0; 
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		return "Ship does not have standard fighter bays";
	}
}




