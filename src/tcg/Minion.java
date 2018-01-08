package tcg;

import java.util.ArrayList;

import org.luaj.vm2.LuaFunction;

import tcg.Triggerable.Type;


public class Minion extends Character
{
	public Card card;
	public boolean token;
	public boolean sick;
	public boolean charge;
	public boolean taunt;
	public boolean defender;
	public boolean shroud;
	public boolean isDragon;
	public boolean markedForDeath;
   

	public Minion(final String name, Type type, final int attack, final int health, final Card card, final Player p, final boolean isDragon)
	{
		super(p.game);
		this.token = false;
		this.sick = true;
		this.charge = false;
		this.taunt = false;
		this.defender = false;
		this.shroud = false;
		this.owner = p;
		this.controller = p;
		this.card = card;
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.maxHealth = health;
		if(card != null)
		{
			this.card = card;
			this.type = card.typing;
			this.secondType = card.secondType;
			this.camouflage = card.camouflage;
			this.token = card.token;
			this.charge = card.charge;
			this.defender = card.defender;
			this.taunt = card.taunt;
			this.angelicHalo = card.angelicHalo;
			this.windfury = card.windfury;
			this.stealth = card.stealth;
			this.shroud = card.shroud;
			this.splash = card.splash;
			this.lifeSteal = card.lifeSteal;
			this.isDragon = card.isDragon;
			this.poisoned = card.poisoned;
			this.cooldown = card.cooldown;
			this.isToxic = card.isToxic;
			this.dodgeChance = card.dodgeChance;
			this.missChance = card.missChance;
			this.poisoned = card.poisoned;
			this.burned = card.burned;
			this.addAura(card);
		}
	}

	@Override
	protected void onEvent(final Event e, final Object source, final Object other)
	{
		super.onEvent(e, source, other);
		if(e == Event.DEATH)
			this.removeAuras();
	}
	public void markForDeath()
	{
		this.markedForDeath= true;
	}

	public void silence()
	{
		this.game.log(String.valueOf(this.name) + " silenced");
		this.taunt = false;
		this.charge = false;
		this.windfury = false;
		this.angelicHalo = false;
		this.stealth = false;
		this.shroud = false;
		this.type = Type.NORMAL;
		this.isRanged = false;
		this.lifeSteal = false;
		this.armor = 0;
		this.shield = 0;
		this.cooldown = false;
		this.frozen = 0;
		this.camouflage = Type.NORMAL;
		this.secondType = this.type;
		this.defender = false;
		this.dodgeChance = 0;
		this.splash = false;
		this.missChance = 0;
		this.poisoned = 0;
		this.burned = 0;
		this.isToxic = false;
		this.removeAuras();
	}

	public boolean isDragon()
	{
		return isDragon;
	}

	public void unsummon()
	{
		this.controller.board.remove(this);
		this.removeAuras();
		if(!this.token)
		{
			this.game.log(String.valueOf(this.name) + " returned to " + this.owner.name + "'s hand");
			this.owner.hand.add(new Spell(this.card, this.owner, this.type));
		}
	}

	public boolean hasCharge()
	{
		return this.sumByStat(Stat.CHARGE) > 0 || this.charge;
	}

	public boolean hasWindfury()
	{
		return this.sumByStat(Stat.WINDFURY) > 0 || this.windfury;
	}

	
	public boolean adjacent(final Minion m)
	{
		final int index = this.controller.board.indexOf(this);
		final int index2 = this.controller.board.indexOf(m);
		return index2 != -1 && Math.abs(index - index2) == 1;
	}
	public ArrayList<Minion> getAdjacent()
	{
		ArrayList<Minion> x = this.owner.board;
		ArrayList<Minion> y = new ArrayList<Minion>();
		for(Minion v : x)
			if(this.adjacent(v))
				y.add(v);
		return y;
	}

	public void forEachNeighbor(final LuaFunction handler)
	{
		final int index = this.controller.board.indexOf(this);
		final int right = index + 1;
		if(right < this.controller.board.size())
			this.game.invoke(handler, this.controller.board.get(right));
		final int left = index - 1;
		if(left >= 0)
			this.game.invoke(handler, this.controller.board.get(left));
	}

	public void transform(final String name)
	{
		final Card card = Card.get(name);
		this.removeAuras();
		this.card = card;
		this.name = name;
		this.type = card.typing;
		this.secondType = card.secondType;
		this.attack = card.attack;
		this.health = card.health;
		this.maxHealth = card.health;
		this.charge = card.charge;
		this.defender = card.defender;
		this.taunt = card.taunt;
		this.angelicHalo = card.angelicHalo;
		this.windfury = card.windfury;
		this.stealth = card.stealth;
		this.shroud = card.shroud;
	}
}