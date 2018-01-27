package tcg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.luaj.vm2.LuaFunction;

public class Player extends Character {
	private Deck deck;
	private LinkedList<Spell> earlyLibrary;
	private LinkedList<Spell> midLibrary;
	private LinkedList<Spell> lateLibrary;
	private LinkedList<Spell> hand;
	private LinkedList<Aura> secrets;
	private ArrayList<Minion> board;
	private int gold;
	private int maxGold;
	private int inflation;
	private int flux;
	private Weapon weapon;
	private Player opponent;
	private int maxEnergy;
	private int energy;
	private int mana;
	private int rage;

	public Player(final Game game, final Deck deck) {
		super(game);
		this.earlyLibrary = new LinkedList<Spell>();
		this.setMidLibrary(new LinkedList<Spell>());
		this.lateLibrary = new LinkedList<Spell>();
		this.setHand(new LinkedList<Spell>());
		this.setSecrets(new LinkedList<Aura>());
		this.setBoard(new ArrayList<Minion>());
		this.setGold(0);
		this.setMaxGold(0);
		this.setMaxEnergy(0);
		this.setEnergy(0);
		this.setMana(0);
		this.setRage(0);
		this.setInflation(0);
		this.setFlux(1);
		this.setWeapon(null);
		this.setOwner(this);
		this.setController(this);
		this.health = 500;
		this.maxHealth = 500;
		this.attack = 0;
		this.deck = deck;
		for (final Card card : deck.getEarlySpells())
			this.earlyLibrary.add(new Spell(card, this, type));
		for (final Card card : deck.getMidSpells())
			this.getMidLibrary().add(new Spell(card, this, type));
		for (final Card card : deck.getLateSpells())
			this.lateLibrary.add(new Spell(card, this, type));
		this.shuffle();
	}

	public void shuffle() {
		Collections.shuffle(this.earlyLibrary);
		Collections.shuffle(this.getMidLibrary());
		Collections.shuffle(this.lateLibrary);
		for (int i = 0; (i < 6); i++)
			moveToBack();
	}

	public void moveToBack() {
		int i;
		i = 0;
		for (Spell s : this.earlyLibrary)
			if (s.getCard().drawnLast) {
				Collections.swap(earlyLibrary, earlyLibrary.indexOf(s),
						earlyLibrary.indexOf(earlyLibrary.getLast()) - i);
				i++;
			}
		i = 0;
		for (Spell s : this.getMidLibrary())
			if (s.getCard().drawnLast) {
				Collections.swap(getMidLibrary(), getMidLibrary().indexOf(s), getMidLibrary().indexOf(getMidLibrary().getLast()) - i);
				i++;
			}
		i = 0;
		for (Spell s : this.lateLibrary)
			if (s.getCard().drawnLast) {
				Collections.swap(lateLibrary, lateLibrary.indexOf(s), lateLibrary.indexOf(lateLibrary.getLast()) - i);
				i++;
			}
	}

	@Override
	public int getAttack() {
		return super.getAttack() + ((this.getWeapon() != null) ? this.getWeapon().getAttack() : 0);
	}

	public int getGold() {
		return Math.min(this.getMaxGold(), this.gold + this.sumByStat(Stat.GOLD));
	}

	public int getEnergy() {
		return Math.min(this.getMaxEnergy(), this.energy + this.sumByStat(Stat.ENERGY));
	}

	public int getMana() {
		return this.mana + this.sumByStat(Stat.MANA);
	}

	public int getRage() {
		return this.rage + this.sumByStat(Stat.RAGE);
	}

	public int useUpRage() {
		int x = this.getRage();
		this.setRage(0);
		return x;
	}

	public int useUpMana() {
		int x = this.getMana();
		this.setMana(0);
		return x;
	}

	public int getMaxGold() {
		return this.maxGold + this.sumByStat(Stat.GOLD);
	}

	public int getMaxEnergy() {
		return this.maxEnergy * (1 + this.sumByStat(Stat.ENERGY));
	}

	public void switchHands() {
		swapLinkedList(getGame().currentOpponent().getHand(), getGame().currentPlayer().getHand());
		for (Spell s : getGame().currentOpponent().getHand())
			s.setOwner(getGame().currentOpponent());
		for (Spell s : getGame().currentPlayer().getHand())
			s.setOwner(getGame().currentPlayer());
	}

	public static <E> void swapList(ArrayList<E> list1, ArrayList<E> list2) {
		ArrayList<E> tmpList = new ArrayList<E>(list1);
		list1.clear();
		list1.addAll(list2);
		list2.clear();
		list2.addAll(tmpList);
	}

	public static <E> void swapLinkedList(LinkedList<E> list1, LinkedList<E> list2) {
		LinkedList<E> tmpList = new LinkedList<E>(list1);
		list1.clear();
		list1.addAll(list2);
		list2.clear();
		list2.addAll(tmpList);
	}

	public void flipBoard() {
		swapList(getGame().currentPlayer().getBoard(), getGame().currentOpponent().getBoard());
		for (Minion s : getGame().currentOpponent().getBoard())
			s.setController(getGame().currentOpponent());
		for (Minion s : getGame().currentPlayer().getBoard())
			s.setController(getGame().currentPlayer());
	}

	public void switchDecks() {
		swapLinkedList(getGame().currentPlayer().earlyLibrary, getGame().currentOpponent().earlyLibrary);
		swapLinkedList(getGame().currentPlayer().getMidLibrary(), getGame().currentOpponent().getMidLibrary());
		swapLinkedList(getGame().currentPlayer().lateLibrary, getGame().currentOpponent().lateLibrary);
		for (Spell s : getGame().currentOpponent().earlyLibrary)
			s.setOwner(getGame().currentOpponent());
		for (Spell s : getGame().currentPlayer().earlyLibrary)
			s.setOwner(getGame().currentPlayer());
		for (Spell s : getGame().currentOpponent().getMidLibrary())
			s.setOwner(getGame().currentOpponent());
		for (Spell s : getGame().currentPlayer().getMidLibrary())
			s.setOwner(getGame().currentPlayer());
		for (Spell s : getGame().currentOpponent().lateLibrary)
			s.setOwner(getGame().currentOpponent());
		for (Spell s : getGame().currentPlayer().lateLibrary)
			s.setOwner(getGame().currentPlayer());
	}

	public void flipLibraries(Player p) {
		swapLinkedList(p.earlyLibrary, p.lateLibrary);
	}

	public void draw() {
		Spell s = blockedDraw();
		if (s != null)
			this.getGame().invoke(s.getCard().getOnDrawn(), s);
	}

	public Spell blockedDraw() {
		Spell s = null;
		if (this.getHand().size() < Game.MAXIMUM_HAND_SIZE) {
			double Erng = Math.random() * 100;
			double Lrng = Math.random() * 100;
			final Game game = this.getGame();
			int e = 45 - (game.turn * 10);
			if (e < 2)
				e = 2;
			int l = 4 + (game.turn * 2);
			if (l > 100)
				l = 100;
			if (Erng < e && this.earlyLibrary.size() > 0) {
				s = this.earlyLibrary.pop();
				this.getGame().log(String.valueOf(this.getOwner().name) + " draws a card *Early*");
				this.getHand().add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if (Lrng < l && this.lateLibrary.size() > 0) {
				s = this.lateLibrary.pop();
				this.getGame().log(String.valueOf(this.getOwner().name) + " draws a card *Reserve*");
				this.getHand().add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if (this.getMidLibrary().size() > 0) {
				s = this.getMidLibrary().pop();
				this.getGame().log(String.valueOf(this.getOwner().name) + " draws a card");
				this.getHand().add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if (this.lateLibrary.size() > 0) {
				s = this.lateLibrary.pop();
				this.getGame().log(String.valueOf(this.getOwner().name) + " draws a card *Reserve*");
				this.getHand().add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if (this.earlyLibrary.size() > 0) {
				s = this.earlyLibrary.pop();
				this.getGame().log(String.valueOf(this.getOwner().name) + " draws a card *Early*");
				this.getHand().add(s);
				this.onEvent(Event.DRAW, s, null);
			} else
				this.getGame().log("You're out of cards, bro!");
		} else
			System.out.println("maxmimum hand size reached");
		return s;
	}

	public void drawCard(final String name) {
		if (this.getHand().size() < Game.MAXIMUM_HAND_SIZE) {
			final Card c = Card.get(name);
			if (c != null)
				this.getHand().add(new Spell(Card.get(name), this, Card.get(name).getTyping()));
			else
				System.err.println("Card " + name + " not found");
		} else
			System.out.println("maxmimum hand size reached");
	}

	public void castSpell(final Spell s) {
		this.castSpell(s, null);
	}

	public void castSpell(final Spell s, final Minion before) {
		this.getGame().log(String.valueOf(this.name) + " casts " + s.getCard().name);
		this.setGold(this.getGold() - s.getGoldCost());
		if (this.getMaxEnergy() != Integer.MAX_VALUE)
			this.setEnergy(this.getEnergy() - s.getEnergyCost());
		this.setRage(this.getRage() - s.getRageCost());
		this.setMana(this.getMana() - s.getManaCost());
		this.setInflation(this.getInflation() + s.getCard().inflation);
		this.setFlux(this.getFlux() + s.getCard().flux);
		this.getHand().remove(s);
		final Game game = this.getGame();
		++game.spellCount;
		if (s.getCard().type.equals("minion")) {
			if (this.getBoard().size() < Game.MAXIMUM_BOARD_SIZE) {
				final Minion m = new Minion(s.getCard().name, s.getCard().typing, s.getCard().attack, s.getCard().health, s.getCard(), this,
						s.getCard().isDragon);
				final int i = this.getBoard().indexOf(before);
				if (i >= 0)
					this.getBoard().add(i, m);
				else
					this.getBoard().add(m);
				m.onEvent(Event.CAST, m, null);
				m.onEvent(Event.SUMMON, m, null);
				++this.getGame().minionsPlayed;
			} else
				System.out.println("maxmimum board size reached");
		} else if (s.getCard().type.equals("weapon")) {
			equip(s.getCard().name);
			this.onEvent(Event.CAST, s, null);
			this.getGame().invoke(s.getCard().getOnCast(), this.getWeapon());
		} else {
			this.onEvent(Event.CAST, s, null);
			this.getGame().invoke(s.getCard().getOnCast(), s);
		}
	}

	public void summon(final String name) {
		if (this.getBoard().size() < 8) {
			final Card card = Card.get(name);
			final Minion m = new Minion(card.name, card.typing, card.attack, card.health, card, this, card.isDragon);
			this.getBoard().add(m);
			this.getGame().invoke(card.getOnCast(), m);
			m.onEvent(Event.SUMMON, m, null);
		} else
			System.out.println("maxmimum board size reached");
	}

	public void equip(final String name) {
		final Card card = Card.get(name);
		this.setWeapon(new Weapon(card.name, card.typing, card.secondType, card.attack, card.durability, card, this));
		this.shield = card.shield;
		this.isRanged = card.isRanged;
		this.splash = card.splash;
		this.lifeSteal = card.lifeSteal;
		this.cooldown = card.cooldown;
		this.missChance = card.missChance;
		this.dodgeChance = card.dodgeChance;
		this.isToxic = card.isToxic;
		this.camouflage = card.camouflage;
		this.poisoned = card.poisoned;
		this.burned = card.burned;
		this.changeType(this.getWeapon().getType());
		// for dualtype weapons
		if (this.getWeapon().getType() != this.getWeapon().getType2())
			this.changeType(this.getWeapon().getType(), this.getWeapon().getType2());
	}

	public boolean hasTaunt() {
		for (final Minion m : this.getBoard())
			if (m.isTaunt() && !m.stealth)
				return true;
		return false;
	}

	public boolean hasWeapon() {
		return this.getWeapon() != null && this.getWeapon().getDurability() > 0;
	}

	public void discardRandom(final int c) {
		for (int i = 0; i < c; ++i)
			if (this.getHand().size() > 0)
				this.getHand().remove((int) (Math.random() * this.getHand().size()));
	}

	public Aura addSecretAura(final LuaFunction trigger) {
		final Aura a = new Aura(this, trigger);
		this.getSecrets().add(a);
		return a;
	}

	public void gainGold(final int m) {
		this.setGold(this.getGold() + m);
		if (this.getGold() > this.getMaxGold())
			this.setGold(this.getMaxGold());
	}

	public void gainGoldCrystals(final int m) {
		this.setMaxGold(this.getMaxGold() + m);
		if (this.getMaxGold() > 10)
			this.setMaxGold(10);
	}

	public void destroyGoldCrystals(final int m) {
		this.setMaxGold(this.getMaxGold() - m);
		if (this.getMaxGold() < 0)
			this.setMaxGold(0);
		if (this.getGold() > this.getMaxGold())
			this.setGold(this.getMaxGold());
	}

	public void lose() {
		this.getGame().log(this.name + " dies!");
		if (this.getGame().currentPlayer().equals(this))
			this.getGame().log(this.getGame().currentOpponent().name + " wins!");
		else
			this.getGame().log(this.getGame().currentPlayer().name + " wins!");
	}

	public LinkedList<Aura> getSecrets() {
		return secrets;
	}

	public void setSecrets(LinkedList<Aura> secrets) {
		this.secrets = secrets;
	}

	public Player getOpponent() {
		return opponent;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	public void setMaxGold(int maxGold) {
		this.maxGold = maxGold;
	}

	public int setMaxEnergy(int maxEnergy) {
		this.maxEnergy = maxEnergy;
		return maxEnergy;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void setRage(int rage) {
		this.rage = rage;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public LinkedList<Spell> getHand() {
		return hand;
	}

	public void setHand(LinkedList<Spell> hand) {
		this.hand = hand;
	}

	public ArrayList<Minion> getBoard() {
		return board;
	}

	public void setBoard(ArrayList<Minion> board) {
		this.board = board;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	public int getInflation() {
		return inflation;
	}

	public void setInflation(int inflation) {
		this.inflation = inflation;
	}

	public int getFlux() {
		return flux;
	}

	public void setFlux(int flux) {
		this.flux = flux;
	}

	public LinkedList<Spell> getMidLibrary() {
		return midLibrary;
	}

	public void setMidLibrary(LinkedList<Spell> midLibrary) {
		this.midLibrary = midLibrary;
	}
}