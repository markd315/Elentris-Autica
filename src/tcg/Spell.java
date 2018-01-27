package tcg;

import org.luaj.vm2.LuaValue;

import tcg.Triggerable.Type;

public class Spell {
	private Card card;
	private Player owner;
	private Type type;

	public Spell(final Card card, final Player owner, final Type type) {
		super();
		this.setCard(card);
		this.type = type;
		this.setOwner(owner);
	}

	public int getGoldCost() {
		int c = this.getCard().goldCost;
		if (this.getCard().costModifier != null)
			c += ((LuaValue) this.getOwner().getGame().invoke(this.getCard().costModifier, this.getOwner())).toint();
		for (final Aura a : this.getOwner().getGame().aurasByStat(Stat.GOLDCOST))
			c += a.amountFor(this);
		return Math.max(0, c);
	}

	public int getManaCost() {
		int c = this.getCard().manaCost;
		if (this.getCard().costModifier != null)
			c += ((LuaValue) this.getOwner().getGame().invoke(this.getCard().costModifier, this.getOwner())).toint();
		for (final Aura a : this.getOwner().getGame().aurasByStat(Stat.MANACOST))
			c += a.amountFor(this);
		return Math.max(0, c);
	}

	public int getEnergyCost() {
		int c = this.getCard().energyCost;
		if (this.getCard().costModifier != null)
			c += ((LuaValue) this.getOwner().getGame().invoke(this.getCard().costModifier, this.getOwner())).toint();
		for (final Aura a : this.getOwner().getGame().aurasByStat(Stat.ENERGYCOST))
			c *= a.amountFor(this);
		return Math.max(0, c);
	}

	public int getRageCost() {
		int c = this.getCard().rageCost;
		if (this.getCard().costModifier != null)
			c += ((LuaValue) this.getOwner().getGame().invoke(this.getCard().costModifier, this.getOwner())).toint();
		for (final Aura a : this.getOwner().getGame().aurasByStat(Stat.RAGECOST))
			c += a.amountFor(this);
		return Math.max(0, c);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
}