package data.hullmods;

import java.util.Iterator;
import java.util.List;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.Alignment;

public class IntegratedPointDefenseAI extends BaseHullMod {

	public static final float DAMAGE_BONUS = 50f;
	public static float PD_RANGE = 100f;
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		//stats.getRecoilPerShotMultSmallWeaponsOnly().modifyMult(id, 0f);
		//stats.getRecoilDecayMult().modifyMult(id, 10f);
		if (stats.getVariant().getSMods().contains("pointdefenseai") || stats.getVariant().getHullSpec().isBuiltInMod("pointdefenseai")) {
			stats.getBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE);
			stats.getNonBeamPDWeaponRangeBonus().modifyFlat(id, PD_RANGE);
			stats.getDamageToFighters().modifyPercent(id, DAMAGE_BONUS);
		}
		stats.getDynamic().getMod(Stats.PD_IGNORES_FLARES).modifyFlat(id, 1f);
		stats.getDynamic().getMod(Stats.PD_BEST_TARGET_LEADING).modifyFlat(id, 1f);
		stats.getDamageToMissiles().modifyPercent(id, DAMAGE_BONUS);
		//stats.getRecoilDecayMult().modifyMult(id, 2f);
//		stats.getDamageToMissiles().modifyPercent(id, DAMAGE_BONUS);
//		stats.getDamageToFighters().modifyPercent(id, DAMAGE_BONUS);
		//stats.getProjectileSpeedMult().modifyMult(id, 100f);
		//stats.getWeaponTurnRateBonus().modifyMult(id, 2f);
		//stats.getDamageToFighters().modifyPercent(id, 50f);
		//stats.getProjectileSpeedMult().modifyPercent(id, 50f);
		//stats.getAutofireAimAccuracy().modifyFlat(id, 1f);
		
	}

	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		List weapons = ship.getAllWeapons();
		Iterator iter = weapons.iterator();
		while (iter.hasNext()) {
			WeaponAPI weapon = (WeaponAPI)iter.next();
//			if (weapon.hasAIHint(AIHints.PD)) {
//				weapon.get
//			}
			boolean sizeMatches = weapon.getSize() == WeaponSize.SMALL;
			//sizeMatches |= weapon.getSize() == WeaponSize.MEDIUM;
			
			if (sizeMatches && weapon.getType() != WeaponType.MISSILE) {
				weapon.setPD(true);
			}
		}
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int)Math.round(DAMAGE_BONUS) + "%";
		return null;
	}

    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("All damage dealt to fighters is increased by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50%");
			tooltip.addPara("Increases the range of all point-defense weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100");
			return;
		} else if (ship.getVariant().getSMods().contains("pointdefenseai") || ship.getVariant().getSModdedBuiltIns().contains("pointdefenseai")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);
			tooltip.addPara("All damage dealt to fighters is increased by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "50%");
			tooltip.addPara("Increases the range of all point-defense weapons by %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "100");
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("All damage dealt to fighters is increased by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "50%");
			tooltip.addPara("Increases the range of all point-defense weapons by %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "100");
		}
    }

}
