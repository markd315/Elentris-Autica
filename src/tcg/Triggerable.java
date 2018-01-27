package tcg;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaFunction;

public class Triggerable {
	public LuaClosure onCast;
	public LuaClosure onDamage;
	public LuaClosure onDamageDealt;
	public LuaClosure onDeath;
	public LuaClosure onStartOfTurn;
	public  LuaClosure onEndOfTurn;
	public  LuaClosure onHeal;
	public LuaClosure onDraw;
	public  LuaClosure onAttack;
	public  LuaClosure onSecretRevealed;
	public  LuaClosure onSummon;
	public  LuaClosure onDrawn;
	private static/* synthetic */int[] $SWITCH_TABLE$tcg$Event;

	public static enum Type {
		NATURE, FIRE, INDUSTRY, DARK, LIGHT, AQUA, TIME, NORMAL, RANDOM
	}

	public LuaFunction getTrigger(final Event e) {
		switch ($SWITCH_TABLE$tcg$Event()[e.ordinal()]) {
		case 1: {
			return this.getOnAttack();
		}
		case 2: {
			return this.getOnDamage();
		}
		case 3: {
			return this.getOnDamageDealt();
		}
		case 4: {
			return this.getOnStartOfTurn();
		}
		case 5: {
			return this.getOnEndOfTurn();
		}
		case 6: {
			return this.getOnDeath();
		}
		case 7: {
			return this.getOnCast();
		}
		case 8: {
			return this.getOnHeal();
		}
		case 9: {
			return this.getOnDraw();
		}
		case 10: {
			return this.getOnSecretRevealed();
		}
		case 11: {
			return this.getOnSummon();
		}
		case 12: {
			return this.getOnDrawn();
		}
		default: {
			System.err.println("unknown trigger: " + e);
			return null;
		}
		}
	}

	public void applyToAura(final Aura a) {
		a.setOnCast(this.getOnCast());
		a.setOnDamage(this.getOnDamage());
		a.setOnDamageDealt(this.getOnDamageDealt());
		a.setOnDeath(this.getOnDeath());
		a.setOnStartOfTurn(this.getOnStartOfTurn());
		a.setOnEndOfTurn(this.getOnEndOfTurn());
		a.setOnHeal(this.getOnHeal());
		a.setOnDraw(this.getOnDraw());
		a.setOnAttack(this.getOnAttack());
		a.setOnSecretRevealed(this.getOnSecretRevealed());
		a.setOnSummon(this.getOnSummon());
		a.setOnDrawn(this.getOnDrawn());
	}

	private LuaClosure getOnSecretRevealed() {
		return onSecretRevealed;
	}

	public void setOnSecretRevealed(LuaClosure onSecretRevealed) {
		this.onSecretRevealed = onSecretRevealed;

	}

	static/* synthetic */int[] $SWITCH_TABLE$tcg$Event() {
		final int[] $switch_TABLE$tcg$Event = Triggerable.$SWITCH_TABLE$tcg$Event;
		if ($switch_TABLE$tcg$Event != null)
			return $switch_TABLE$tcg$Event;
		final int[] $switch_TABLE$tcg$Event2 = new int[Event.values().length];
		try {
			$switch_TABLE$tcg$Event2[Event.ATTACK.ordinal()] = 1;
		} catch (NoSuchFieldError noSuchFieldError) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.CAST.ordinal()] = 7;
		} catch (NoSuchFieldError noSuchFieldError2) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.DAMAGE.ordinal()] = 2;
		} catch (NoSuchFieldError noSuchFieldError3) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.DAMAGE_DEALT.ordinal()] = 3;
		} catch (NoSuchFieldError noSuchFieldError4) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.DEATH.ordinal()] = 6;
		} catch (NoSuchFieldError noSuchFieldError5) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.DRAW.ordinal()] = 9;
		} catch (NoSuchFieldError noSuchFieldError6) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.END_OF_TURN.ordinal()] = 5;
		} catch (NoSuchFieldError noSuchFieldError7) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.HEAL.ordinal()] = 8;
		} catch (NoSuchFieldError noSuchFieldError8) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.SECRET_REVEALED.ordinal()] = 10;
		} catch (NoSuchFieldError noSuchFieldError9) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.START_OF_TURN.ordinal()] = 4;
		} catch (NoSuchFieldError noSuchFieldError10) {
		}
		try {
			$switch_TABLE$tcg$Event2[Event.SUMMON.ordinal()] = 11;
		} catch (NoSuchFieldError noSuchFieldError11) {
		}
		return Triggerable.$SWITCH_TABLE$tcg$Event = $switch_TABLE$tcg$Event2;
	}

	public LuaClosure getOnDrawn() {
		return onDrawn;
	}

	public void setOnDrawn(LuaClosure onDrawn) {
		this.onDrawn = onDrawn;
	}

	public LuaClosure getOnCast() {
		return onCast;
	}

	public void setOnCast(LuaClosure onCast) {
		this.onCast = onCast;
	}

	public LuaClosure getOnDamage() {
		return onDamage;
	}

	public void setOnDamage(LuaClosure onDamage) {
		this.onDamage = onDamage;
	}

	public LuaClosure getOnDamageDealt() {
		return onDamageDealt;
	}

	public void setOnDamageDealt(LuaClosure onDamageDealt) {
		this.onDamageDealt = onDamageDealt;
	}

	public LuaClosure getOnDeath() {
		return onDeath;
	}

	public void setOnDeath(LuaClosure onDeath) {
		this.onDeath = onDeath;
	}

	public LuaClosure getOnStartOfTurn() {
		return onStartOfTurn;
	}

	public void setOnStartOfTurn(LuaClosure onStartOfTurn) {
		this.onStartOfTurn = onStartOfTurn;
	}

	public LuaClosure getOnEndOfTurn() {
		return onEndOfTurn;
	}

	public void setOnEndOfTurn(LuaClosure onEndOfTurn) {
		this.onEndOfTurn = onEndOfTurn;
	}

	public LuaClosure getOnHeal() {
		return onHeal;
	}

	public void setOnHeal(LuaClosure onHeal) {
		this.onHeal = onHeal;
	}

	public LuaClosure getOnDraw() {
		return onDraw;
	}

	public void setOnDraw(LuaClosure onDraw) {
		this.onDraw = onDraw;
	}

	public LuaClosure getOnAttack() {
		return onAttack;
	}

	public void setOnAttack(LuaClosure onAttack) {
		this.onAttack = onAttack;
	}

	public LuaClosure getOnSummon() {
		return onSummon;
	}

	public void setOnSummon(LuaClosure onSummon) {
		this.onSummon = onSummon;
	}
}