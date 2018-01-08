package tcg;

import tcg.Triggerable.Type;


public class Weapon extends GameObject
{
	public Card card;
	public int attack;
	public int durability;
	public int durabilityLoss;
	public Type type;
	public Type secondType;

	public Weapon(final String name, final Type typing, final Type type2, final int attack, final int durability, final Card card, final Player owner)
	{
		super(owner.game);
		this.card = card;
		this.type = typing;
		this.secondType = type2;
		this.controller = owner;
		this.owner = owner;
		this.attack = attack;
		this.durability = durability;
		this.durabilityLoss = card.durabilityLoss;
	}

	public int getAttack()
	{
		return Math.max(0, this.attack + this.sumByStat(Stat.ATTACK));
	}

	public void looseAttack(final int a)
	{
		this.attack -= a;
		if(this.attack < 0)
			this.attack = 0;
	}

	public void looseDurability(int d)
	{
		if(d == -1)
			d = this.durabilityLoss;
		this.durability -= d;
		if(this.durability == 0 && this.owner.weapon == this)
			this.destroy();
	}

	public void gainDurability(final int d)
	{
		this.durability += d;
	}

	public void destroy()
	{
		this.removeAuras();
		this.owner.shield = 0;
		this.owner.isRanged = false;
		this.owner.splash = false;
		this.owner.lifeSteal = false;
		this.owner.isToxic = false;
		this.owner.missChance = 0.0;
		this.owner.dodgeChance = 0.0;
		this.owner.cooldown = false;
		this.owner.isToxic = false;
		this.owner.camouflage = Type.NORMAL;
		this.owner.weapon = null;
		this.owner.type = Type.NORMAL;
	}

	public Type getType()
	{
		return type;
	}
	public Type getType2()
	{
		return secondType;
	}
}