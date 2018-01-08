package tcg;

import org.luaj.vm2.LuaValue;

import tcg.Triggerable.Type;


public class Spell
{
	public Card card;
	public Player owner;
	public Type type;

	public Spell(final Card card, final Player owner, final Type type)
	{
		super();
		this.card = card;
		this.type = type;
		this.owner = owner;
	}

	public int getGoldCost()
	{
		int c = this.card.goldCost;
		if(this.card.costModifier != null)
			c += ((LuaValue)this.owner.game.invoke(this.card.costModifier, this.owner)).toint();
		for(final Aura a : this.owner.game.aurasByStat(Stat.GOLDCOST))
			c += a.amountFor(this);
		return Math.max(0, c);
	}

	public int getManaCost()
	{
		int c = this.card.manaCost;
		if(this.card.costModifier != null)
			c += ((LuaValue)this.owner.game.invoke(this.card.costModifier, this.owner)).toint();
		for(final Aura a : this.owner.game.aurasByStat(Stat.MANACOST))
			c += a.amountFor(this);
		return Math.max(0, c);
	}

	public int getEnergyCost()
	{
		int c = this.card.energyCost;
		if(this.card.costModifier != null)
			c += ((LuaValue)this.owner.game.invoke(this.card.costModifier, this.owner)).toint();
		for(final Aura a : this.owner.game.aurasByStat(Stat.ENERGYCOST))
			c *= a.amountFor(this);
		return Math.max(0, c);
	}

	public int getRageCost()
	{
		int c = this.card.rageCost;
		if(this.card.costModifier != null)
			c += ((LuaValue)this.owner.game.invoke(this.card.costModifier, this.owner)).toint();
		for(final Aura a : this.owner.game.aurasByStat(Stat.RAGECOST))
			c += a.amountFor(this);
		return Math.max(0, c);
	}
}