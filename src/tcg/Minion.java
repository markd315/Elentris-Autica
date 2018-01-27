package tcg;

import java.util.ArrayList;

import org.luaj.vm2.LuaFunction;

import tcg.Triggerable.Type;

public class Minion extends Character {
	private Card card;
	private boolean token;
	private boolean sick;
	private boolean charge;
	private boolean taunt;
	private boolean defender;
	private boolean shroud;
	private boolean isDragon;
	private boolean markedForDeath;

	public Minion(final String name, Type type, final int attack, final int health, final Card card, final Player p,
			final boolean isDragon) {
		super(p.getGame());
		this.token = false;
		this.setSick(true);
		this.charge = false;
		this.setTaunt(false);
		this.setDefender(false);
		this.setShroud(false);
		this.setOwner(p);
		this.setController(p);
		this.setCard(card);
		this.name = name;
		this.attack = attack;
		this.health = health;
		this.maxHealth = health;
		if (card != null) {
			this.setCard(card);
			this.type = card.typing;
			this.secondType = card.secondType;
			this.camouflage = card.camouflage;
			this.token = card.token;
			this.charge = card.charge;
			this.setDefender(card.defender);
			this.setTaunt(card.taunt);
			this.angelicHalo = card.angelicHalo;
			this.windfury = card.windfury;
			this.stealth = card.stealth;
			this.setShroud(card.shroud);
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
	protected void onEvent(final Event e, final Object source, final Object other) {
		super.onEvent(e, source, other);
		if (e == Event.DEATH)
			this.removeAuras();
	}

	public void markForDeath() {
		this.setMarkedForDeath(true);
	}

	public void silence() {
		this.getGame().log(String.valueOf(this.name) + " silenced");
		this.setTaunt(false);
		this.charge = false;
		this.windfury = false;
		this.angelicHalo = false;
		this.stealth = false;
		this.setShroud(false);
		this.type = Type.NORMAL;
		this.isRanged = false;
		this.lifeSteal = false;
		this.armor = 0;
		this.shield = 0;
		this.cooldown = false;
		this.frozen = 0;
		this.camouflage = Type.NORMAL;
		this.secondType = this.type;
		this.setDefender(false);
		this.dodgeChance = 0;
		this.splash = false;
		this.missChance = 0;
		this.poisoned = 0;
		this.burned = 0;
		this.isToxic = false;
		this.removeAuras();
	}

	public boolean isDragon() {
		return isDragon;
	}

	public void unsummon() {
		this.getController().getBoard().remove(this);
		this.removeAuras();
		if (!this.token) {
			this.getGame().log(String.valueOf(this.name) + " returned to " + this.getOwner().name + "'s hand");
			this.getOwner().getHand().add(new Spell(this.getCard(), this.getOwner(), this.type));
		}
	}

	public boolean hasCharge() {
		return this.sumByStat(Stat.CHARGE) > 0 || this.charge;
	}

	public boolean hasWindfury() {
		return this.sumByStat(Stat.WINDFURY) > 0 || this.windfury;
	}

	public boolean adjacent(final Minion m) {
		final int index = this.getController().getBoard().indexOf(this);
		final int index2 = this.getController().getBoard().indexOf(m);
		return index2 != -1 && Math.abs(index - index2) == 1;
	}

	public ArrayList<Minion> getAdjacent() {
		ArrayList<Minion> x = this.getOwner().getBoard();
		ArrayList<Minion> y = new ArrayList<Minion>();
		for (Minion v : x)
			if (this.adjacent(v))
				y.add(v);
		return y;
	}

	public void forEachNeighbor(final LuaFunction handler) {
		final int index = this.getController().getBoard().indexOf(this);
		final int right = index + 1;
		if (right < this.getController().getBoard().size())
			this.getGame().invoke(handler, this.getController().getBoard().get(right));
		final int left = index - 1;
		if (left >= 0)
			this.getGame().invoke(handler, this.getController().getBoard().get(left));
	}

	public void transform(final String name) {
		final Card card = Card.get(name);
		this.removeAuras();
		this.setCard(card);
		this.name = name;
		this.type = card.typing;
		this.secondType = card.secondType;
		this.attack = card.attack;
		this.health = card.health;
		this.maxHealth = card.health;
		this.charge = card.charge;
		this.setDefender(card.defender);
		this.setTaunt(card.taunt);
		this.angelicHalo = card.angelicHalo;
		this.windfury = card.windfury;
		this.stealth = card.stealth;
		this.setShroud(card.shroud);
	}

	public boolean isMarkedForDeath() {
		return markedForDeath;
	}

	public void setMarkedForDeath(boolean markedForDeath) {
		this.markedForDeath = markedForDeath;
	}

	public boolean isSick() {
		return sick;
	}

	public void setSick(boolean sick) {
		this.sick = sick;
	}

	public boolean isShroud() {
		return shroud;
	}

	public void setShroud(boolean shroud) {
		this.shroud = shroud;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public boolean isDefender() {
		return defender;
	}

	public void setDefender(boolean defender) {
		this.defender = defender;
	}

	public boolean isTaunt() {
		return taunt;
	}

	public void setTaunt(boolean taunt) {
		this.taunt = taunt;
	}
}