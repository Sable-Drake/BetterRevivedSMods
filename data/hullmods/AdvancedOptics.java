package data.hullmods;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;

public class AdvancedOptics extends BaseHullMod {

	public static final float BEAM_RANGE_BONUS = 200f;
	//public static final float BEAM_DAMAGE_PENALTY = 25f;
	public static final float BEAM_DAMAGE_BONUS = 5f;
	public static final float BEAM_TURN_PENALTY = 30f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
		if (stats.getVariant().getSMods().contains("advancedoptics") || stats.getVariant().getSModdedBuiltIns().contains("advancedoptics")) {
                    //stats.getBeamWeaponDamageMult().modifyPercent(id, BEAM_DAMAGE_BONUS);
		} else {
                    stats.getBeamWeaponRangeBonus().modifyFlat(id, BEAM_RANGE_BONUS);
                }
		//stats.getBeamWeaponDamageMult().modifyPercent(id, -BEAM_DAMAGE_PENALTY);
		stats.getBeamWeaponTurnRateBonus().modifyMult(id, 1f - BEAM_TURN_PENALTY * 0.01f);
	}
        
        public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		if (ship.getVariant().getSMods().contains("advancedoptics") || ship.getVariant().getSModdedBuiltIns().contains("advancedoptics")) {
                    ship.addListener(new AdvancedOpticsRangeMod());
		}
	}
        
        public static class AdvancedOpticsRangeMod implements WeaponBaseRangeModifier {
		public AdvancedOpticsRangeMod() {
		}
		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0;
		}
		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1f;
		}
		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
			if (weapon.isBeam()) {
				return BEAM_RANGE_BONUS;
			}
			return 0f;
		}
	}
	
    public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
        float opad = 10f;
        Color color1 = new Color(Misc.getBasePlayerColor().getRed(), Misc.getBasePlayerColor().getGreen(), Misc.getBasePlayerColor().getBlue(), Math.round(Misc.getBasePlayerColor().getAlpha()/2f));
        Color color2 = new Color(Misc.getDarkPlayerColor().getRed(), Misc.getDarkPlayerColor().getGreen(), Misc.getDarkPlayerColor().getBlue(), Math.round(Misc.getDarkPlayerColor().getAlpha()/2f));
		if (isForModSpec) {
                        tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);

			//tooltip.addPara("S-mod Bonus: %s Beam Damage.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+5" + "%");
                        tooltip.addPara("Bonus range converted to base range increase", Misc.getGrayColor(), 10f);
			return;
		} else if (ship.getVariant().getSMods().contains("advancedoptics") || ship.getVariant().getSModdedBuiltIns().contains("advancedoptics")) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getStoryOptionColor(), Misc.getStoryDarkColor(), Alignment.MID, 10f);

			//tooltip.addPara("S-mod Bonus: %s Beam Damage.", 10f, Misc.getPositiveHighlightColor(), Misc.getHighlightColor(), "+5" + "%");
                        tooltip.addPara("Bonus range converted to base range increase", Misc.getPositiveHighlightColor(), 10f);
                        tooltip.addSectionHeading("Interactions with other modifiers", Alignment.MID, opad);
		tooltip.addPara("The base range is increased, thus percentage and multiplicative modifiers - such as from Integrated Targeting Unit, "
				+ "skills, or similar sources - apply to the increased base value.", opad);
		} else if (!isForModSpec) {
			tooltip.addSectionHeading("S-mod bonus", Misc.getGrayColor(), Misc.setAlpha(Misc.scaleColorOnly(Misc.getGrayColor(), 0.4f), 175), Alignment.MID, 10f);

			//tooltip.addPara("S-mod Bonus: %s Beam Damage.", 10f, Misc.getGrayColor(), Misc.getHighlightColor(), "+5" + "%");
                        tooltip.addPara("Bonus range converted to base range increase", Misc.getGrayColor(), 10f);
		}
    }
	
	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) return "" + (int) BEAM_RANGE_BONUS;
		//if (index == 1) return "" + (int) BEAM_DAMAGE_PENALTY;
		if (index == 1) return "" + (int) BEAM_TURN_PENALTY + "%";
		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return !ship.getVariant().getHullMods().contains(HullMods.HIGH_SCATTER_AMP);
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullMods().contains(HullMods.HIGH_SCATTER_AMP)) {
			return "Incompatible with High Scatter Amplifier";
		}
		return null;
	}
}
