package tcg;

import java.util.HashMap;
import java.util.Random;

import org.luaj.vm2.LuaClosure;

public class Card extends Triggerable {
	protected int nr;
	protected boolean token;
	protected String name;
	protected String description;
	protected Type typing;
	protected Type secondType;
	protected int goldCost;
	protected int energyCost;
	protected int rageCost;
	protected int manaCost;
	protected int inflation;
	protected int flux;
	protected int attack;
	protected int health;
	protected int durability;
	protected int durabilityLoss;
	protected boolean charge;
	protected boolean taunt;
	protected boolean defender;
	protected boolean windfury;
	protected boolean angelicHalo;
	protected boolean stealth;
	protected boolean shroud;
	protected boolean splash;
	protected LuaClosure costModifier;
	protected int shield;
	protected boolean isRanged;
	protected boolean isDragon;
	protected String type;
	protected boolean lifeSteal;
	protected boolean cooldown;
	protected Type camouflage;
	protected double missChance;
	protected double dodgeChance;
	protected boolean isToxic;
	protected boolean drawnLast;
	protected int poisoned;
	protected int burned;
	protected static HashMap<String, Card> spells;
	static {
		Card.spells = new HashMap<String, Card>();
	}

	public Card(final String classification, final int nr, final String name, Type division, Type c, int goldCost,
			int energyCost, int manaCost, int rageCost, final String description) {
		super();
		this.nr = 45;
		this.flux = 1;
		this.durabilityLoss = 1;
		this.nr = nr;
		if (nr == 10000)
			this.nr = 0;
		this.type = classification;
		this.name = name;
		this.goldCost = goldCost;
		this.energyCost = energyCost;
		this.rageCost = rageCost;
		this.manaCost = manaCost;
		this.typing = this.secondType = division;
		this.camouflage = c;
		this.description = description;
	}

	public Card(final String classification, final int nr, final String name, Type division, Type division2, Type c,
			int goldCost, int energyCost, int manaCost, int rageCost, final String description) {
		super();
		this.nr = 45;
		this.flux = 1;
		this.durabilityLoss = 1;
		this.nr = nr;
		if (nr == 10000)
			this.nr = 0;
		this.type = classification;
		this.name = name;
		this.goldCost = goldCost;
		this.energyCost = energyCost;
		this.rageCost = rageCost;
		this.manaCost = manaCost;
		this.typing = division;
		this.secondType = division2;
		this.camouflage = c;
		this.description = description;
	}

	public static Card createMinion(final int nr, final String name, int goldCost, int energyCost, int rageCost,
			final String description, final int attack, final int health) {
		return createMinion(nr, name, "NORMAL", goldCost, energyCost, rageCost, description, attack, health);
	}

	public static Card createMinion(final int nr, final String name, String stringType, int goldCost, int energyCost,
			int rageCost, final String description, final int attack, final int health) {
		return createMinion(nr, name, stringType, stringType, goldCost, energyCost, rageCost, description, attack,
				health);
	}

	public static Card createMinion(final int nr, final String name, String stringType, String camo, int goldCost,
			int energyCost, int rageCost, final String description, final int attack, final int health,
			final String d) {
		final Card spell = new Card("minion", nr, name, typeTranslate(stringType), typeTranslate(camo), goldCost,
				energyCost, 0, rageCost, description);
		spell.attack = attack;
		spell.health = health;
		spell.isDragon = true;
		Card.spells.put(name, spell);
		return spell;
	}

	// TODO Add hella cards
	public static Card createMinion(final int nr, final String name, String stringType, String camo, int goldCost,
			int energyCost, int rageCost, final String description, final int attack, final int health) {
		final Card spell = new Card("minion", nr, name, typeTranslate(stringType), typeTranslate(camo), goldCost,
				energyCost, 0, rageCost, description);
		spell.attack = attack;
		spell.health = health;
		Card.spells.put(name, spell);
		return spell;
	}

	// dual typing createMinion
	public static Card createMinion(final int nr, final String name, String stringType, String altStringType,
			String camo, int goldCost, int energyCost, int rageCost, final String description, final int attack,
			final int health) {
		final Card spell = new Card("minion", nr, name, typeTranslate(stringType), typeTranslate(altStringType),
				typeTranslate(camo), goldCost, energyCost, 0, rageCost, description);
		spell.attack = attack;
		spell.health = health;
		Card.spells.put(name, spell);
		return spell;
	}

	public static Type typeTranslate(String stringType) {
		if (stringType.equalsIgnoreCase("NORMAL"))
			return Type.NORMAL;
		if (stringType.equalsIgnoreCase("FIRE"))
			return Type.FIRE;
		if (stringType.equalsIgnoreCase("AQUA"))
			return Type.AQUA;
		if (stringType.equalsIgnoreCase("WATER"))
			return Type.AQUA;
		if (stringType.equalsIgnoreCase("NATURE"))
			return Type.NATURE;
		if (stringType.equalsIgnoreCase("TIME"))
			return Type.TIME;
		if (stringType.equalsIgnoreCase("INDUSTRY"))
			return Type.INDUSTRY;
		if (stringType.equalsIgnoreCase("DARK"))
			return Type.DARK;
		if (stringType.equalsIgnoreCase("LIGHT"))
			return Type.LIGHT;
		if (stringType.equalsIgnoreCase("RANDOM"))
			return Type.RANDOM;
		return null;
	}

	public static Card createSpell(final int nr, final String name, String stringType, int goldCost, int energyCost,
			int manaCost, int rageCost, final String description) {
		final Card spell = new Card("spell", nr, name, typeTranslate(stringType), typeTranslate(stringType), goldCost,
				energyCost, manaCost, rageCost, description);
		Card.spells.put(name, spell);
		return spell;
	}

	public static Card createWeapon(final int nr, final String name, String stringType, int goldCost, int energyCost,
			int rageCost, final String description, final int attack, final int durability) {
		final Card spell = new Card("weapon", nr, name, typeTranslate(stringType), typeTranslate(stringType), goldCost,
				energyCost, 0, rageCost, description);
		spell.attack = attack;
		spell.durability = durability;
		Card.spells.put(name, spell);
		return spell;
	}

	// for dualtypes
	public static Card createWeapon(final int nr, final String name, String stringType, String type2, int goldCost,
			int energyCost, int rageCost, final String description, final int attack, final int durability) {
		final Card spell = new Card("weapon", nr, name, typeTranslate(stringType), typeTranslate(type2), goldCost,
				energyCost, 0, rageCost, description);
		spell.attack = attack;
		spell.durability = durability;
		Card.spells.put(name, spell);
		return spell;
	}

	public static Card get(final String name) {
		return Card.spells.get(name);
	}

	public static HashMap<String, Card> getSpells() {
		return Card.spells;
	}

	public static Card getMinionByGold(final int x) {
		Card c = null;
		while ((c == null) || (!c.type.equalsIgnoreCase("minion")) || (!(c.goldCost == x))) {
			Random generator = new Random();
			Object[] values = spells.values().toArray();
			c = (Card) values[generator.nextInt(values.length)];
		}
		return c;
	}

	public static Card getMinionByType(final Type t) {
		Card c = null;
		while ((c == null) || (!c.type.equalsIgnoreCase("minion")) || (!(c.typing.equals(t)))) {
			Random generator = new Random();
			Object[] values = spells.values().toArray();
			c = (Card) values[generator.nextInt(values.length)];
		}
		return c;
	}

	public static Card getCardByType(final Type t) {
		Card c = null;
		while ((c == null) || (!(c.typing.equals(t)))) {
			Random generator = new Random();
			Object[] values = spells.values().toArray();
			c = (Card) values[generator.nextInt(values.length)];
		}
		return c;
	}

	public static Card getDragon() {
		Card c = null;
		while ((c == null) || (!c.type.equalsIgnoreCase("minion")) || (!(c.isDragon))) {
			Random generator = new Random();
			Object[] values = spells.values().toArray();
			c = (Card) values[generator.nextInt(values.length)];
		}
		return c;
	}

	public static Card getWeapon() {
		Card c = null;
		while ((c == null) || (!c.type.equalsIgnoreCase("weapon"))) {
			Random generator = new Random();
			Object[] values = spells.values().toArray();
			c = (Card) values[generator.nextInt(values.length)];
		}
		return c;
	}

	public static Card randomCard() {
		Random generator = new Random();
		Object[] values = spells.values().toArray();
		return (Card) values[generator.nextInt(values.length)];
	}

	public Type getTyping() {
		return this.typing;
	}

	public String getSubtype() {
		if (this.typing == Type.NORMAL)
			return "NORMAL";
		if (this.typing == Type.FIRE)
			return "FIRE";
		if (this.typing == Type.AQUA)
			return "WATER";
		if (this.typing == Type.NATURE)
			return "NATURE";
		if (this.typing == Type.TIME)
			return "TIME";
		if (this.typing == Type.INDUSTRY)
			return "INDUSTRY";
		if (this.typing == Type.DARK)
			return "DARK";
		if (this.typing == Type.LIGHT)
			return "LIGHT";
		if (this.typing == Type.RANDOM)
			return "RANDOM";
		return "";
	}

	public static String getType(Type t) {
		if (t == Type.NORMAL)
			return "NORMAL";
		if (t == Type.FIRE)
			return "FIRE";
		if (t == Type.AQUA)
			return "WATER";
		if (t == Type.NATURE)
			return "NATURE";
		if (t == Type.TIME)
			return "TIME";
		if (t == Type.INDUSTRY)
			return "INDUSTRY";
		if (t == Type.DARK)
			return "DARK";
		if (t == Type.LIGHT)
			return "LIGHT";
		if (t == Type.LIGHT)
			return "RANDOM";
		return "";
	}
}