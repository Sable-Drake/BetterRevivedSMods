package data.hullmods;

import java.awt.Color;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.WeaponAPI.AIHints;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponSize;
import com.fs.starfarer.api.combat.WeaponAPI.WeaponType;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.impl.campaign.ids.Strings;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

public class BallisticRangefinder extends BaseHullMod {

	public static float BONUS_MAX_1 = 800;
	public static float BONUS_MAX_2 = 800;
	public static float BONUS_MAX_3 = 900;
	public static float BONUS_SMALL_1 = 100;
	public static float BONUS_SMALL_2 = 100;
	public static float BONUS_SMALL_3 = 200;
	public static float BONUS_MEDIUM_3 = 100;
	
	public static float HYBRID_MULT = 2f;
	public static float HYBRID_BONUS_MIN = 100f;
	
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
	}

	@Override
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		WeaponSize largest = null;
		for (WeaponSlotAPI slot : ship.getHullSpec().getAllWeaponSlotsCopy()) {
			if (slot.isDecorative() ) continue;
			if (slot.getWeaponType() == WeaponType.BALLISTIC) {
				if (largest == null || largest.ordinal() < slot.getSlotSize().ordinal()) {
					largest = slot.getSlotSize();
				}
			}
		}
		if (largest == null) return;
		float small = 0f;
		float medium = 0f;
		float max = 0f;
                if (largest == WeaponSize.LARGE) {
                    small = BONUS_SMALL_3;
                    medium = BONUS_MEDIUM_3;
                    max = BONUS_MAX_3;
                } else if (largest == WeaponSize.MEDIUM) {
                    small = BONUS_SMALL_2;
                    max = BONUS_MAX_2;
                } else if (largest == WeaponSize.SMALL) {
                    small = BONUS_SMALL_1;
                    max = BONUS_MAX_1;
                }
		/*switch (largest) {
		case LARGE:
			small = BONUS_SMALL_3;
			medium = BONUS_MEDIUM_3;
			max = BONUS_MAX_3;
			break;
		case MEDIUM:
			small = BONUS_SMALL_2;
			max = BONUS_MAX_2;
			break;
		case SMALL:
			small = BONUS_SMALL_1;
			max = BONUS_MAX_1;
			break;
		} We can't use switch statements!*/
                ship.addListener(new RangefinderRangeModifier(small, medium, max));
		
	}
	
	public static class RangefinderRangeModifier implements WeaponBaseRangeModifier {
		public float small, medium, max;
		public RangefinderRangeModifier(float small, float medium, float max) {
			this.small = small;
			this.medium = medium;
			this.max = max;
		}
		
		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0;
		}
		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1f;
		}
		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
			if (weapon.getSlot() == null) {
				return 0f;
			}
			if (weapon.hasAIHint(AIHints.PD)) {
				return 0f;
			}
                        
                        float bonus = 0;
			if (weapon.getSpec().getMountType() == WeaponType.BALLISTIC || weapon.getSpec().getMountType() == WeaponType.HYBRID) {
                            if (weapon.getSize() == WeaponSize.SMALL) {
                                    bonus = small;
                            } else if (weapon.getSize() == WeaponSize.MEDIUM) {
                                    bonus = medium;
                            }
                            if (weapon.getSpec().getMountType() == WeaponType.HYBRID) {
                                    bonus *= HYBRID_MULT;
                                    if (bonus < HYBRID_BONUS_MIN) {
                                            bonus = HYBRID_BONUS_MIN;
                                    }
                            }
                        }
			
			if (bonus == 0f) return 0f;
			
			float base = weapon.getSpec().getMaxRange();
			if (base + bonus > max) {
				bonus = max - base;
			}
			if (bonus < 0) bonus = 0;
			return bonus;
		}
	}

	public String getDescriptionParam(int index, HullSize hullSize) {
		//if (index == 0) return "" + (int)RANGE_PENALTY_PERCENT + "%";
		return null;
	}
	
	@Override
	public boolean shouldAddDescriptionToTooltip(HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
		return false;
	}

	@Override
	public void addPostDescriptionSection(TooltipMakerAPI tooltip, HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
		float opad = 10f;
		Color h = Misc.getHighlightColor();
		Color gray = Misc.getGrayColor();
		
                LabelAPI label = tooltip.addPara("Utilizes targeting data from the ship's largest ballistic slot to benefit certain weapons, extending the base range of typical ballistic weapons to match similar but larger weapons. Also benefits hybrid weapons. Point-defense weapons are unaffected.", opad, h);
                label.setHighlight("ship's largest ballistic slot", "base range");
                label.setHighlightColors(h, h);
                
                tooltip.addPara("The increase to base range is capped, based on the largest slot.", opad);

                if (!isForModSpec && ship != null) {
                    WeaponSize largest = null;
                    for (WeaponSlotAPI slot : ship.getHullSpec().getAllWeaponSlotsCopy()) {
                            if (slot.isDecorative() ) continue;
                            if (slot.getWeaponType() == WeaponType.BALLISTIC) {
                                    if (largest == null || largest.ordinal() < slot.getSlotSize().ordinal()) {
                                            largest = slot.getSlotSize();
                                            if (largest == WeaponSize.LARGE) break;
                                    }
                            }
                    }
                    if (largest == WeaponSize.LARGE) {
                        tooltip.addSectionHeading("Ballistic weapon range", Alignment.MID, opad);
                        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(), 20, "Largest b. slot", 115, "Small wpn", 85, "Medium wpn", 85, "Range cap", 75);
                        tooltip.addRow(Alignment.MID, gray, "Small / Medium", Alignment.MID, gray, "+100", Alignment.MID, gray, "---", Alignment.MID, gray, "800");
                        tooltip.addRow(Alignment.MID, h, "Large", Alignment.MID, h, "+200", Alignment.MID, h, "+100", Alignment.MID, h, "900");
                        tooltip.addTable("", 0, opad);

                        tooltip.addSectionHeading("Hybrid weapon range", Alignment.MID, opad);
                        tooltip.addPara("Affects hybrid weapons (those that can fit into both ballistic and energy slots) of all size.", opad);
                        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(), 20, "Largest b. slot", 115, "Small", 55, "Medium", 55, "Large", 55, "Range cap", 75);
                        tooltip.addRow(Alignment.MID, gray, "Small / Medium", Alignment.MID, gray, "+200", Alignment.MID, gray, "+100", Alignment.MID, gray, "+100", Alignment.MID, gray, "800");
                        tooltip.addRow(Alignment.MID, h, "Large", Alignment.MID, h, "+400", Alignment.MID, h, "+200", Alignment.MID, h, "+100", Alignment.MID, h, "900");
                        tooltip.addTable("", 0, opad);
                    } else if (largest == WeaponSize.MEDIUM || largest == WeaponSize.SMALL) {
                        tooltip.addSectionHeading("Ballistic weapon range", Alignment.MID, opad);
                        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(), 20, "Largest b. slot", 115, "Small wpn", 85, "Medium wpn", 85, "Range cap", 75);
                        tooltip.addRow(Alignment.MID, h, "Small / Medium", Alignment.MID, h, "+100", Alignment.MID, h, "---", Alignment.MID, h, "800");
                        tooltip.addRow(Alignment.MID, gray, "Large", Alignment.MID, gray, "+200", Alignment.MID, gray, "+100", Alignment.MID, gray, "900");
                        tooltip.addTable("", 0, opad);

                        tooltip.addSectionHeading("Hybrid weapon range", Alignment.MID, opad);
                        tooltip.addPara("Affects hybrid weapons (those that can fit into both ballistic and energy slots) of all size.", opad);
                        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(), 20, "Largest b. slot", 115, "Small", 55, "Medium", 55, "Large", 55, "Range cap", 75);
                        tooltip.addRow(Alignment.MID, h, "Small / Medium", Alignment.MID, h, "+200", Alignment.MID, h, "+100", Alignment.MID, h, "+100", Alignment.MID, h, "800");
                        tooltip.addRow(Alignment.MID, gray, "Large", Alignment.MID, gray, "+400", Alignment.MID, gray, "+200", Alignment.MID, gray, "+100", Alignment.MID, gray, "900");
                        tooltip.addTable("", 0, opad);
                    } else {
                        tooltip.addSectionHeading("Ballistic weapon range", Alignment.MID, opad);
                        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(), 20, "Largest b. slot", 115, "Small wpn", 85, "Medium wpn", 85, "Range cap", 75);
                        tooltip.addRow(Alignment.MID, gray, "Small / Medium", Alignment.MID, gray, "+100", Alignment.MID, gray, "---", Alignment.MID, gray, "800");
                        tooltip.addRow(Alignment.MID, gray, "Large", Alignment.MID, gray, "+200", Alignment.MID, gray, "+100", Alignment.MID, gray, "900");
                        tooltip.addTable("", 0, opad);

                        tooltip.addSectionHeading("Hybrid weapon range", Alignment.MID, opad);
                        tooltip.addPara("Affects hybrid weapons (those that can fit into both ballistic and energy slots) of all sizes.", opad);
                        tooltip.beginTable(Misc.getBasePlayerColor(), Misc.getDarkPlayerColor(), Misc.getBrightPlayerColor(), 20, "Largest b. slot", 115, "Small", 55, "Medium", 55, "Large", 55, "Range cap", 75);
                        tooltip.addRow(Alignment.MID, gray, "Small / Medium", Alignment.MID, gray, "+200", Alignment.MID, gray, "+100", Alignment.MID, gray, "+100", Alignment.MID, gray, "800");
                        tooltip.addRow(Alignment.MID, gray, "Large", Alignment.MID, gray, "+400", Alignment.MID, gray, "+200", Alignment.MID, gray, "+100", Alignment.MID, gray, "900");
                        tooltip.addTable("", 0, opad);
                    }
                    
                }
                
		tooltip.addSectionHeading("Interactions with other modifiers", Alignment.MID, opad);
		tooltip.addPara("The base range is increased, thus percentage and multiplicative modifiers - such as from Integrated Targeting Unit, "
				+ "skills, or similar sources - apply to the increased base value.", opad);
	}
	
	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		return getUnapplicableReason(ship) == null;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && 
				ship.getHullSize() != HullSize.CAPITAL_SHIP && 
				ship.getHullSize() != HullSize.DESTROYER && 
				ship.getHullSize() != HullSize.CRUISER) {
			return "Can only be installed on destroyer-class hulls and larger";
		}
		return null;
	}
	
}









