package data.hullmods;

import java.util.HashMap;
import java.util.Map;

import com.fs.starfarer.api.impl.hullmods.BaseLogisticsHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class ConvertedFighterBay extends BaseLogisticsHullMod {

	private static Map mag = new HashMap();
	static {
		mag.put(HullSize.FRIGATE, 100f);
		mag.put(HullSize.DESTROYER, 200f);
		mag.put(HullSize.CRUISER, 250f);
		mag.put(HullSize.CAPITAL_SHIP, 300f);
	}
	public static final int CREW_REQ_PER_BAY = 20;
	public static final int MAX_CREW = 80;
	public static final int CARGO_PER_BAY = 50;
	public static final float FLUX_PER_BAY = 0.02f;
	public static final int logisticMax = Global.getSettings().getInt("maxLogisticsHullmods");
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		int bays = (int) Math.round(stats.getNumFighterBays().getBaseValue());
		stats.getNumFighterBays().modifyFlat(id, -bays);

		int crewReduction = CREW_REQ_PER_BAY * bays;
		if (crewReduction > MAX_CREW) crewReduction = MAX_CREW;
		int cargo = CARGO_PER_BAY * bays;
		
		stats.getMinCrewMod().modifyPercent(id, -crewReduction);
		stats.getCargoMod().modifyFlat(id, cargo);
		
		// Vanilla S-Mod bonus: -10% monthly maintenance cost per bay
		if (stats.getVariant().getSMods().contains("converted_fighterbay") || stats.getVariant().getSModdedBuiltIns().contains("converted_fighterbay")) {
			if (bays > 0) {
				float bonus = bays * 0.10f;
				if (bonus > 1f) bonus = 1f;
				stats.getSuppliesPerMonth().modifyMult(id, 1f - bonus);
			}
		}
	}
	
	public boolean isApplicableToShip(ShipAPI ship) {
		int builtIn = ship.getHullSpec().getBuiltInWings().size();
		int bays = (int) Math.round(ship.getMutableStats().getNumFighterBays().getBaseValue());
        int logistic = 0;
        for (String s : ship.getVariant().getNonBuiltInHullmods()) {
            if (Misc.getMod(s).hasUITag("Logistics") && !Misc.getMod(s).getId().equals("converted_fighterbay")) {
                logistic += 1;
            }
        }
		if (builtIn <= 0 || bays > builtIn) return false;
		if (logistic >= ship.getMutableStats().getDynamic().getMod(Stats.MAX_LOGISTICS_HULLMODS_MOD).computeEffective(logisticMax)) return false;
		return true;
	}

    public String getUnapplicableReason(ShipAPI ship) {
		int builtIn = ship.getHullSpec().getBuiltInWings().size();
		int bays = (int) Math.round(ship.getMutableStats().getNumFighterBays().getBaseValue());
        int logistic = 0;
        for (String s : ship.getVariant().getNonBuiltInHullmods()) {
            if (Misc.getMod(s).hasUITag("Logistics")) {
                logistic += 1;
            }
        }
		if (builtIn <= 0 || bays > builtIn) return "Requires built-in fighter wings only";
		if (logistic >= ship.getMutableStats().getDynamic().getMod(Stats.MAX_LOGISTICS_HULLMODS_MOD).computeEffective(logisticMax)) return "Maximum of " + String.valueOf(ship.getMutableStats().getDynamic().getMod(Stats.MAX_LOGISTICS_HULLMODS_MOD).computeEffective(logisticMax)) + " non-built-in \"Logistics\" hullmods per hull";
		return "";
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + CARGO_PER_BAY;
		if (index == 1) return "" + CREW_REQ_PER_BAY + "%";
		if (index == 2) return "" + MAX_CREW + "%";
		return null;
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Monthly maintenance cost reduced by %s per fighter bay.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10%");
			return;
		} else if (ship.getVariant().getSMods().contains("converted_fighterbay") || ship.getVariant().getSModdedBuiltIns().contains("converted_fighterbay")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("Monthly maintenance cost reduced by %s per fighter bay.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "10%");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Monthly maintenance cost reduced by %s per fighter bay.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "10%");
		}
    }	
}