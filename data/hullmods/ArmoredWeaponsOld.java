package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.ui.Alignment;

public class ArmoredWeaponsOld extends BaseHullMod {

	public static final float HEALTH_BONUS = 100f;
	public static final float ARMOR_BONUS = 10f;
	public static final float TURN_PENALTY = 25f;
	public static final float RECOIL_BONUS = 25f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("armoredweapons") || stats.getVariant().getSModdedBuiltIns().contains("armoredweapons")) {
			float weapons = 0f;
			for (WeaponSlotAPI slot : stats.getVariant().getHullSpec().getAllWeaponSlotsCopy()) {
				if (slot.isDecorative()) continue;
				if (slot.isHidden()) continue;
				if (slot.isStationModule()) continue;
				if (slot.isSystemSlot()) continue;
				weapons++;
			}
			stats.getArmorBonus().modifyFlat(id, weapons*5f);
			stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS+50f);
		} else {
			stats.getWeaponHealthBonus().modifyPercent(id, HEALTH_BONUS);
		}
		stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
		stats.getMaxRecoilMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilPerShotMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getRecoilDecayMult().modifyMult(id, 1f - (0.01f * RECOIL_BONUS));
		stats.getWeaponTurnRateBonus().modifyMult(id, 1f - TURN_PENALTY * 0.01f);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) HEALTH_BONUS + "%";
		if (index == 1) return "" + (int) RECOIL_BONUS + "%";
		if (index == 2) return "" + (int) TURN_PENALTY + "%";
		if (index == 3) return "" + (int) ARMOR_BONUS + "%";
		return null;
	}

   public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
                float armorfromweapon = 0;
                if (ship != null && ship.getMutableStats().getArmorBonus().getFlatBonus("armoredweapons") != null) {
                    armorfromweapon = ship.getMutableStats().getArmorBonus().getFlatBonus("armoredweapons").getValue();
                }
		if (isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Weapon durability bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "150" + "%");
			tooltip.addPara("%s armor per weapon mount.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+5");
			return;
		} else if (ship.getVariant().getSMods().contains("armoredweapons") || ship.getVariant().getSModdedBuiltIns().contains("armoredweapons")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);	
			tooltip.addPara("Weapon durability bonus increased to %s.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "150" + "%");
			tooltip.addPara("%s armor per weapon mount (%s).", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5", Misc.getRoundedValue(armorfromweapon));
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);
			tooltip.addPara("Weapon durability bonus increased to %s.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "150" + "%");
			tooltip.addPara("%s armor per weapon mount.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+5");
		}
    }
	
}
