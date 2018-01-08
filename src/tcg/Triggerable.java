package tcg;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaFunction;


public class Triggerable
{
	public LuaClosure onCast;
	public LuaClosure onDamage;
	public LuaClosure onDamageDealt;
	public LuaClosure onDeath;
	public LuaClosure onStartOfTurn;
	public LuaClosure onEndOfTurn;
	public LuaClosure onHeal;
	public LuaClosure onDraw;
	public LuaClosure onAttack;
	public LuaClosure onSecredRevealed;
	public LuaClosure onSummon;
	public LuaClosure onDrawn;
	private static/* synthetic */int[] $SWITCH_TABLE$tcg$Event;
	public static enum Type
	{
		NATURE, FIRE, INDUSTRY, DARK, LIGHT, AQUA, TIME, NORMAL, RANDOM
	}
	public LuaFunction getTrigger(final Event e)
	{
		switch($SWITCH_TABLE$tcg$Event()[e.ordinal()])
		{
			case 1:
			{
				return this.onAttack;
			}
			case 2:
			{
				return this.onDamage;
			}
			case 3:
			{
				return this.onDamageDealt;
			}
			case 4:
			{
				return this.onStartOfTurn;
			}
			case 5:
			{
				return this.onEndOfTurn;
			}
			case 6:
			{
				return this.onDeath;
			}
			case 7:
			{
				return this.onCast;
			}
			case 8:
			{
				return this.onHeal;
			}
			case 9:
			{
				return this.onDraw;
			}
			case 10:
			{
				return this.onSecredRevealed;
			}
			case 11:
			{
				return this.onSummon;
			}
			case 12:
			{
				return this.onDrawn;
			}
			default:
			{
				System.err.println("unknown trigger: " + e);
				return null;
			}
		}
	}

	public void applyToAura(final Aura a)
	{
		a.onCast = this.onCast;
		a.onDamage = this.onDamage;
		a.onDamageDealt = this.onDamageDealt;
		a.onDeath = this.onDeath;
		a.onStartOfTurn = this.onStartOfTurn;
		a.onEndOfTurn = this.onEndOfTurn;
		a.onHeal = this.onHeal;
		a.onDraw = this.onDraw;
		a.onAttack = this.onAttack;
		a.onSecredRevealed = this.onSecredRevealed;
		a.onSummon = this.onSummon;
		a.onDrawn = this.onDrawn;
	}

	static/* synthetic */int[] $SWITCH_TABLE$tcg$Event()
	{
		final int[] $switch_TABLE$tcg$Event = Triggerable.$SWITCH_TABLE$tcg$Event;
		if($switch_TABLE$tcg$Event != null)
			return $switch_TABLE$tcg$Event;
		final int[] $switch_TABLE$tcg$Event2 = new int[Event.values().length];
		try
		{
			$switch_TABLE$tcg$Event2[Event.ATTACK.ordinal()] = 1;
		} catch(NoSuchFieldError noSuchFieldError)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.CAST.ordinal()] = 7;
		} catch(NoSuchFieldError noSuchFieldError2)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.DAMAGE.ordinal()] = 2;
		} catch(NoSuchFieldError noSuchFieldError3)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.DAMAGE_DEALT.ordinal()] = 3;
		} catch(NoSuchFieldError noSuchFieldError4)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.DEATH.ordinal()] = 6;
		} catch(NoSuchFieldError noSuchFieldError5)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.DRAW.ordinal()] = 9;
		} catch(NoSuchFieldError noSuchFieldError6)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.END_OF_TURN.ordinal()] = 5;
		} catch(NoSuchFieldError noSuchFieldError7)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.HEAL.ordinal()] = 8;
		} catch(NoSuchFieldError noSuchFieldError8)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.SECRET_REVEALED.ordinal()] = 10;
		} catch(NoSuchFieldError noSuchFieldError9)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.START_OF_TURN.ordinal()] = 4;
		} catch(NoSuchFieldError noSuchFieldError10)
		{
		}
		try
		{
			$switch_TABLE$tcg$Event2[Event.SUMMON.ordinal()] = 11;
		} catch(NoSuchFieldError noSuchFieldError11)
		{
		}
		return Triggerable.$SWITCH_TABLE$tcg$Event = $switch_TABLE$tcg$Event2;
	}
}