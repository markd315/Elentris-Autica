package tcg;

import java.util.Random;

import tcg.Triggerable.Type;

public abstract class Character extends GameObject {
	protected String name;
	protected int attack;
	protected int health;
	protected int maxHealth;
	protected int armor;
	protected int attackCount;
	protected int frozen;
	protected boolean windfury;
	protected boolean angelicHalo;
	protected boolean stealth;
	protected boolean splash;
	protected Type type = Type.NORMAL;
	protected Type secondType = Type.NORMAL;
	protected int shield;
	protected boolean isRanged;
	protected boolean lifeSteal;
	protected boolean cooldown;
	protected boolean isToxic;
	protected double missChance;
	protected double dodgeChance;
	protected Type camouflage;
	protected int poisoned;
	protected int burned;

	public Character(final Game game) {
		super(game);
		this.armor = 0;
		this.attackCount = 0;
		this.frozen = 0;
		this.windfury = false;
		this.angelicHalo = false;
		this.stealth = false;
		this.splash = false;
		this.cooldown = false;
	}

	public void setMaxHealth(int x) {
		this.maxHealth = x;
	}

	public int getAttack() {
		return Math.max(0, this.attack + this.sumByStat(Stat.ATTACK));
	}

	public int getHealth() {
		return Math.max(0, this.health + this.sumByStat(Stat.HEALTH));
	}

	public boolean damaged() {
		return this.getHealth() < this.maxHealth;
	}

	public double hitCoefficient(Type t) {
		double d = 1;
		Type x = this.type;
		if (this.getOwner().equals(this.getGame().currentOpponent()))
			x = this.camouflage;
		if (x == Type.RANDOM || t == Type.RANDOM) {
			Type save1 = x;
			Type save2 = t;
			if (x == Type.RANDOM) {
				Random r = new Random();
				switch (r.nextInt(8)) {
				case 0:
					x = Type.AQUA;
					break;
				case 1:
					x = Type.DARK;
					break;
				case 2:
					x = Type.FIRE;
					break;
				case 3:
					x = Type.INDUSTRY;
					break;
				case 4:
					x = Type.LIGHT;
					break;
				case 5:
					x = Type.NATURE;
					break;
				case 6:
					x = Type.NORMAL;
					break;
				case 7:
					x = Type.TIME;
					break;
				default:
					x = Type.NORMAL;
				}
			}
			if (t == Type.RANDOM) {
				Random r = new Random();
				switch (r.nextInt(9)) {
				case 0:
					t = Type.AQUA;
					break;
				case 1:
					t = Type.DARK;
					break;
				case 2:
					t = Type.FIRE;
					break;
				case 3:
					t = Type.INDUSTRY;
					break;
				case 4:
					t = Type.LIGHT;
					break;
				case 5:
					t = Type.NATURE;
					break;
				case 6:
					t = Type.NORMAL;
					break;
				case 7:
					t = Type.TIME;
					break;
				default:
					t = Type.NORMAL;
				}
			}
			double g = hitCoefficient(x, t);
			x = save1;
			t = save2;
			return g;
		}
		if (x == Type.NATURE)
			switch (t) {
			case LIGHT:
			case AQUA:
			case TIME:
				d = .5;
				break;
			case INDUSTRY:
			case DARK:
			case FIRE:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.INDUSTRY)
			switch (t) {
			case NATURE:
				break;
			case TIME:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.TIME)
			switch (t) {
			case INDUSTRY:
				d = .5;
				break;
			case NATURE:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.LIGHT)
			switch (t) {
			case DARK:
				d = .5;
				break;
			case NATURE:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.DARK)
			switch (t) {
			case NATURE:
				d = .5;
				break;
			case LIGHT:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.FIRE)
			switch (t) {
			case NATURE:
				d = .5;
				break;
			case AQUA:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.AQUA)
			switch (t) {
			case FIRE:
				d = .5;
				break;
			case NATURE:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (!(this.secondType == this.type)
				&& (this.type.equals(this.camouflage) || this.getOwner().equals(this.getGame().currentPlayer()))) {
			x = this.secondType;
			double y = d;
			d = 1;
			if (x == Type.RANDOM || t == Type.RANDOM) {
				Type save1 = x;
				Type save2 = t;
				if (x == Type.RANDOM) {
					Random r = new Random();
					switch (r.nextInt(8)) {
					case 0:
						x = Type.AQUA;
						break;
					case 1:
						x = Type.DARK;
						break;
					case 2:
						x = Type.FIRE;
						break;
					case 3:
						x = Type.INDUSTRY;
						break;
					case 4:
						x = Type.LIGHT;
						break;
					case 5:
						x = Type.NATURE;
						break;
					case 6:
						x = Type.NORMAL;
						break;
					case 7:
						x = Type.TIME;
						break;
					default:
						x = Type.NORMAL;
					}
				}
				if (t == Type.RANDOM) {
					Random r = new Random();
					switch (r.nextInt(9)) {
					case 0:
						t = Type.AQUA;
						break;
					case 1:
						t = Type.DARK;
						break;
					case 2:
						t = Type.FIRE;
						break;
					case 3:
						t = Type.INDUSTRY;
						break;
					case 4:
						t = Type.LIGHT;
						break;
					case 5:
						t = Type.NATURE;
						break;
					case 6:
						t = Type.NORMAL;
						break;
					case 7:
						t = Type.TIME;
						break;
					default:
						t = Type.NORMAL;
					}
				}
				double g = hitCoefficient(x, t);
				x = save1;
				t = save2;
				return g;
			}
			if (x == Type.NATURE)
				switch (t) {
				case LIGHT:
				case AQUA:
				case TIME:
					d = .5;
					break;
				case INDUSTRY:
				case DARK:
				case FIRE:
					d = 2;
					break;
				default:
					d = 1;
				}
			if (x == Type.INDUSTRY)
				switch (t) {
				case NATURE:
					break;
				case TIME:
					d = 2;
					break;
				default:
					d = 1;
				}
			if (x == Type.TIME)
				switch (t) {
				case INDUSTRY:
					d = .5;
					break;
				case NATURE:
					d = 2;
					break;
				default:
					d = 1;
				}
			if (x == Type.LIGHT)
				switch (t) {
				case DARK:
					d = .5;
					break;
				case NATURE:
					d = 2;
					break;
				default:
					d = 1;
				}
			if (x == Type.DARK)
				switch (t) {
				case NATURE:
					d = .5;
					break;
				case LIGHT:
					d = 2;
					break;
				default:
					d = 1;
				}
			if (x == Type.FIRE)
				switch (t) {
				case NATURE:
					d = .5;
					break;
				case AQUA:
					d = 2;
					break;
				default:
					d = 1;
				}
			if (x == Type.AQUA)
				switch (t) {
				case FIRE:
					d = .5;
					break;
				case NATURE:
					d = 2;
					break;
				default:
					d = 1;
				}
			d = d * y;

		}
		return d;
	}

	// this method only exists for random purposes, SHOULDN'T break dual typing.
	private double hitCoefficient(Type x, Type t) {
		double d = 1.0;
		if (x == Type.NATURE)
			switch (t) {
			case LIGHT:
			case AQUA:
			case TIME:
				d = .5;
				break;
			case INDUSTRY:
			case DARK:
			case FIRE:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.INDUSTRY)
			switch (t) {
			case NATURE:
				d = .5;
				break;
			case TIME:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.TIME)
			switch (t) {
			case INDUSTRY:
				d = .5;
				break;
			case NATURE:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.LIGHT)
			switch (t) {
			case DARK:
				d = .5;
				break;
			case NATURE:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.DARK)
			switch (t) {
			case NATURE:
				d = .5;
				break;
			case LIGHT:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.FIRE)
			switch (t) {
			case NATURE:
				d = .5;
				break;
			case AQUA:
				d = 2;
				break;
			default:
				d = 1;
			}
		if (x == Type.AQUA)
			switch (t) {
			case FIRE:
				d = .5;
				break;
			case NATURE:
				d = 2;
				break;
			default:
				d = 1;
			}
		return d;
	}

	public boolean combatDamage(int f, final Character source) {
		if (f <= 0)
			return false;
		if (this.angelicHalo) {
			this.getGame().log(String.valueOf(this.name) + " loses angelic halo!");
			return this.angelicHalo = false;
		}
		this.getGame().log(String.valueOf(source.name) + " deals " + f + " damage to " + this.name);
		final int migitation = Math.min(f, this.armor);
		this.armor -= migitation;
		f -= migitation;
		if (f > 0)
			this.health -= f;
		return f > 0;
	}

	public void changeType(Type x) {
		this.type = x;
		this.secondType = x;
		this.camouflage = x;
	}

	public void changeType(Type x, Type y) {
		this.type = x;
		this.secondType = y;
		this.camouflage = x;
	}

	public void changeType(String x) {
		this.type = Card.typeTranslate(x);
		this.secondType = Card.typeTranslate(x);
		this.camouflage = Card.typeTranslate(x);
	}

	public void damage(final int d, final String t, final Character source) {
		if (d <= 0)
			return;
		final boolean b = this.combatDamage(d, source);
		if (!b)
			return;
		this.onEvent(Event.DAMAGE, this, source);
		this.getGame().postDamage();
	}

	public void typedDamage(int d, String q, final Character source) {
		if (d <= 0)
			return;
		boolean b = this.combatSpellDamage(d, Card.typeTranslate(q), source);
		if (!b)
			return;
		this.onEvent(Event.DAMAGE, this, source);
		this.getGame().postDamage();
	}

	public void spellDamage(int d, String q, final Character source) {
		d += source.getOwner().sumByStat(Stat.SPELL_POWER);
		if (d <= 0)
			return;
		boolean b = this.combatSpellDamage(d, Card.typeTranslate(q), source);
		if (!b)
			return;
		this.onEvent(Event.DAMAGE, this, source);
		this.getGame().postDamage();
	}

	public boolean combatSpellDamage(int f, Type t, final Character source) {
		if (f <= 0)
			return false;
		if (this.angelicHalo) {
			this.getGame().log(String.valueOf(this.name) + " loses divine shield");
			return this.angelicHalo = false;
		}
		f *= this.hitCoefficient(t);
		this.getGame().log(String.valueOf(source.name) + " deals " + f + " damage to " + this.name);
		final int migitation = Math.min(f, this.armor);
		this.armor -= migitation;
		f -= migitation;
		this.health -= f;
		return f > 0;
	}

	public void destroy() {
		this.health = -10000;
		this.getGame().postDamage();
	}

	public void heal(int d) {
		d = Math.min(d, Math.max(this.maxHealth - this.getHealth(), 0));
		if (d > 0) {
			this.health += d;
			this.onEvent(Event.HEAL, this, null);
		}
	}

	public void gain(final int a, final int h) {
		this.getGame().log(String.valueOf(this.name) + " gains +" + a + "/+" + h);
		this.attack += a;
		this.health += h;
	}

	public void freeze() {
		this.freeze(1);
	}

	public void freeze(final int turns) {
		this.frozen = Math.max(turns, this.frozen);
		this.getGame().log(String.valueOf(this.name) + " frozen");
	}

	public boolean frozen() {
		return this.frozen > 0;
	}

	public void unfreeze() {
		if (this.frozen > 0)
			--this.frozen;
	}

	public Type getType() {
		return type;
	}

	public Type getCamo() {
		return camouflage;
	}

	public void switchC() {
		Type temp;
		temp = this.type;
		this.type = this.camouflage;
		this.camouflage = temp;
	}

	public Type getType2() {
		return secondType;
	}
}