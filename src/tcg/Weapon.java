package tcg;

import tcg.Triggerable.Type;

public class Weapon extends GameObject {
	private Card card;
	private int attack;
	private int durability;
	private int durabilityLoss;
	private Type type;
	private Type secondType;

	public Weapon(final String name, final Type typing, final Type type2, final int attack, final int durability,
			final Card card, final Player owner) {
		super(owner.getGame());
		this.card = card;
		this.type = typing;
		this.secondType = type2;
		this.setController(owner);
		this.setOwner(owner);
		this.attack = attack;
		this.setDurability(durability);
		this.durabilityLoss = card.durabilityLoss;
	}

	public int getAttack() {
		return Math.max(0, this.attack + this.sumByStat(Stat.ATTACK));
	}

	public void looseAttack(final int a) {
		this.attack -= a;
		if (this.attack < 0)
			this.attack = 0;
	}

	public void looseDurability(int d) {
		if (d == -1)
			d = this.durabilityLoss;
		this.setDurability(this.getDurability() - d);
		if (this.getDurability() == 0 && this.getOwner().getWeapon() == this)
			this.destroy();
	}

	public void gainDurability(final int d) {
		this.setDurability(this.getDurability() + d);
	}

	public void destroy() {
		this.removeAuras();
		this.getOwner().shield = 0;
		this.getOwner().isRanged = false;
		this.getOwner().splash = false;
		this.getOwner().lifeSteal = false;
		this.getOwner().isToxic = false;
		this.getOwner().missChance = 0.0;
		this.getOwner().dodgeChance = 0.0;
		this.getOwner().cooldown = false;
		this.getOwner().isToxic = false;
		this.getOwner().camouflage = Type.NORMAL;
		this.getOwner().setWeapon(null);
		this.getOwner().type = Type.NORMAL;
	}

	public Type getType() {
		return type;
	}

	public Type getType2() {
		return secondType;
	}

	public int getDurability() {
		return durability;
	}

	public void setDurability(int durability) {
		this.durability = durability;
	}
}