package tcg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import org.luaj.vm2.LuaFunction;


public class Player extends Character
{
	Deck deck;
	public LinkedList<Spell> earlyLibrary;
	public LinkedList<Spell> midLibrary;
	public LinkedList<Spell> lateLibrary;
	public LinkedList<Spell> hand;
	public LinkedList<Aura> secrets;
	public ArrayList<Minion> board;
	public int gold;
	public int maxGold;
	public int inflation;
	public int flux;
	public Weapon weapon;
	public Player opponent;
	public int maxEnergy;
	public int energy;
	public int mana;
	public int rage;

	public Player(final Game game, final Deck deck)
	{
		super(game);
		this.earlyLibrary = new LinkedList<Spell>();
		this.midLibrary = new LinkedList<Spell>();
		this.lateLibrary = new LinkedList<Spell>();
		this.hand = new LinkedList<Spell>();
		this.secrets = new LinkedList<Aura>();
		this.board = new ArrayList<Minion>();
		this.gold = 0;
		this.maxGold = 0;
		this.maxEnergy = 0;
		this.energy = 0;
		this.mana = 0;
		this.rage = 0;
		this.inflation = 0;
		this.flux = 1;
		this.weapon = null;
		this.owner = this;
		this.controller = this;
		this.health = 500;
		this.maxHealth = 500;
		this.attack = 0;
		this.deck = deck;
		for(final Card card : deck.earlySpells)
			this.earlyLibrary.add(new Spell(card, this, type));
		for(final Card card : deck.midSpells)
			this.midLibrary.add(new Spell(card, this, type));
		for(final Card card : deck.lateSpells)
			this.lateLibrary.add(new Spell(card, this, type));
		this.shuffle();
	}

	public void shuffle()
	{
		Collections.shuffle(this.earlyLibrary);
		Collections.shuffle(this.midLibrary);
		Collections.shuffle(this.lateLibrary);
		for(int i=0; (i<6); i++)
			moveToBack();
	}
	public void moveToBack()
	{
		int i;
		i=0;
		for(Spell s : this.earlyLibrary)
			if(s.card.drawnLast)
			{
				Collections.swap(earlyLibrary, earlyLibrary.indexOf(s), earlyLibrary.indexOf(earlyLibrary.getLast())-i);
				i++;
			}
		i=0;
		for(Spell s : this.midLibrary)
			if(s.card.drawnLast)
			{
				Collections.swap(midLibrary, midLibrary.indexOf(s), midLibrary.indexOf(midLibrary.getLast())-i);
				i++;
			}
		i=0;
		for(Spell s : this.lateLibrary)
			if(s.card.drawnLast)
			{
				Collections.swap(lateLibrary, lateLibrary.indexOf(s), lateLibrary.indexOf(lateLibrary.getLast())-i);
				i++;
			}
	}

	@Override
	public int getAttack()
	{
		return super.getAttack() + ((this.weapon != null) ? this.weapon.getAttack() : 0);
	}

	public int getGold()
	{
		return Math.min(this.getMaxGold(), this.gold + this.sumByStat(Stat.GOLD));
	}

	public int getEnergy()
	{
		return Math.min(this.getMaxEnergy(), this.energy + this.sumByStat(Stat.ENERGY));
	}

	public int getMana()
	{
		return this.mana + this.sumByStat(Stat.MANA);
	}

	public int getRage()
	{
		return this.rage + this.sumByStat(Stat.RAGE);
	}

	public int useUpRage()
	{
		int x = this.rage;
		this.rage = 0;
		return x;
	}

	public int useUpMana()
	{
		int x = this.mana;
		this.mana = 0;
		return x;
	}

	public int getMaxGold()
	{
		return this.maxGold + this.sumByStat(Stat.GOLD);
	}

	public int getMaxEnergy()
	{
		return this.maxEnergy * (1 + this.sumByStat(Stat.ENERGY));
	}

	public void switchHands()
	{
		swapLinkedList(game.currentOpponent().hand, game.currentPlayer().hand);
		for(Spell s : game.currentOpponent().hand)
			s.owner = game.currentOpponent();
		for(Spell s : game.currentPlayer().hand)
			s.owner = game.currentPlayer();
	}

	public static <E> void swapList(ArrayList<E> list1, ArrayList<E> list2)
	{
		ArrayList<E> tmpList = new ArrayList<E>(list1);
		list1.clear();
		list1.addAll(list2);
		list2.clear();
		list2.addAll(tmpList);
	}

	public static <E> void swapLinkedList(LinkedList<E> list1, LinkedList<E> list2)
	{
		LinkedList<E> tmpList = new LinkedList<E>(list1);
		list1.clear();
		list1.addAll(list2);
		list2.clear();
		list2.addAll(tmpList);
	}

	public void flipBoard()
	{
		swapList(game.currentPlayer().board, game.currentOpponent().board);
		for(Minion s : game.currentOpponent().board)
			s.controller = game.currentOpponent();
		for(Minion s : game.currentPlayer().board)
			s.controller = game.currentPlayer();
	}

	public void switchDecks()
	{
		swapLinkedList(game.currentPlayer().earlyLibrary, game.currentOpponent().earlyLibrary);
		swapLinkedList(game.currentPlayer().midLibrary, game.currentOpponent().midLibrary);
		swapLinkedList(game.currentPlayer().lateLibrary, game.currentOpponent().lateLibrary);
		for(Spell s : game.currentOpponent().earlyLibrary)
			s.owner = game.currentOpponent();
		for(Spell s : game.currentPlayer().earlyLibrary)
			s.owner = game.currentPlayer();
		for(Spell s : game.currentOpponent().midLibrary)
			s.owner = game.currentOpponent();
		for(Spell s : game.currentPlayer().midLibrary)
			s.owner = game.currentPlayer();
		for(Spell s : game.currentOpponent().lateLibrary)
			s.owner = game.currentOpponent();
		for(Spell s : game.currentPlayer().lateLibrary)
			s.owner = game.currentPlayer();
	}

	public void flipLibraries(Player p)
	{
		swapLinkedList(p.earlyLibrary, p.lateLibrary);
	}

	public void draw()
	{
		Spell s = blockedDraw();
		if(s != null)
			this.game.invoke(s.card.onDrawn, s);
	}
	public Spell blockedDraw()
	{
		Spell s= null;
		if(this.hand.size() < Game.MAXIMUM_HAND_SIZE)
		{
			double Erng = Math.random() * 100;
			double Lrng = Math.random() * 100;
			final Game game = this.game;
			int e = 45 - (game.turn * 10);
			if(e < 2)
				e = 2;
			int l = 4 + (game.turn * 2);
			if(l > 100)
				l = 100;
			if(Erng < e && this.earlyLibrary.size() > 0)
			{
				s = this.earlyLibrary.pop();
				this.game.log(String.valueOf(this.owner.name) + " draws a card *Early*");
				this.hand.add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if(Lrng < l && this.lateLibrary.size() > 0)
			{
				s = this.lateLibrary.pop();
				this.game.log(String.valueOf(this.owner.name) + " draws a card *Reserve*");
				this.hand.add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if(this.midLibrary.size() > 0)
			{
				s = this.midLibrary.pop();
				this.game.log(String.valueOf(this.owner.name) + " draws a card");
				this.hand.add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if(this.lateLibrary.size() > 0)
			{
				s = this.lateLibrary.pop();
				this.game.log(String.valueOf(this.owner.name) + " draws a card *Reserve*");
				this.hand.add(s);
				this.onEvent(Event.DRAW, s, null);
			} else if(this.earlyLibrary.size() > 0)
			{
				s = this.earlyLibrary.pop();
				this.game.log(String.valueOf(this.owner.name) + " draws a card *Early*");
				this.hand.add(s);
				this.onEvent(Event.DRAW, s, null);
			} else
				this.game.log("You're out of cards, bro!");
		} else
			System.out.println("maxmimum hand size reached");
		return s;
	}

	public void drawCard(final String name)
	{
		if(this.hand.size() < Game.MAXIMUM_HAND_SIZE)
		{
			final Card c = Card.get(name);
			if(c != null)
				this.hand.add(new Spell(Card.get(name), this, Card.get(name).getTyping()));
			else
				System.err.println("Card " + name + " not found");
		} else
			System.out.println("maxmimum hand size reached");
	}

	public void castSpell(final Spell s)
	{
		this.castSpell(s, null);
	}

	public void castSpell(final Spell s, final Minion before)
	{
		this.game.log(String.valueOf(this.name) + " casts " + s.card.name);
		this.gold -= s.getGoldCost();
		if(this.getMaxEnergy() != Integer.MAX_VALUE)
			this.energy -= s.getEnergyCost();
		this.rage -= s.getRageCost();
		this.mana -= s.getManaCost();
		this.inflation += s.card.inflation;
		this.flux += s.card.flux;
		this.hand.remove(s);
		final Game game = this.game;
		++game.spellCount;
		if(s.card.type.equals("minion"))
		{
			if(this.board.size() < Game.MAXIMUM_BOARD_SIZE)
			{
				final Minion m = new Minion(s.card.name, s.card.typing, s.card.attack, s.card.health, s.card, this, s.card.isDragon);
				final int i = this.board.indexOf(before);
				if(i >= 0)
					this.board.add(i, m);
				else
					this.board.add(m);
				m.onEvent(Event.CAST, m, null);
				m.onEvent(Event.SUMMON, m, null);
				++this.game.minionsPlayed;
			} else
				System.out.println("maxmimum board size reached");
		} else if(s.card.type.equals("weapon"))
		{
			equip(s.card.name);
			this.onEvent(Event.CAST, s, null);
			this.game.invoke(s.card.onCast, this.weapon);
		} else
		{
			this.onEvent(Event.CAST, s, null);
			this.game.invoke(s.card.onCast, s);
		}
	}

	public void summon(final String name)
	{
		if(this.board.size() < 8)
		{
			final Card card = Card.get(name);
			final Minion m = new Minion(card.name, card.typing, card.attack, card.health, card, this, card.isDragon);
			this.board.add(m);
			this.game.invoke(card.onCast, m);
			m.onEvent(Event.SUMMON, m, null);
		} else
			System.out.println("maxmimum board size reached");
	}

	public void equip(final String name)
	{
		final Card card = Card.get(name);
		this.weapon = new Weapon(card.name, card.typing, card.secondType, card.attack, card.durability, card, this);
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
		this.changeType(this.weapon.getType());
		//for dualtype weapons
		if (this.weapon.getType() != this.weapon.getType2())
			this.changeType(this.weapon.getType(), this.weapon.getType2());
	}

	public boolean hasTaunt()
	{
		for(final Minion m : this.board)
			if(m.taunt && !m.stealth)
				return true;
		return false;
	}

	public boolean hasWeapon()
	{
		return this.weapon != null && this.weapon.durability > 0;
	}

	public void discardRandom(final int c)
	{
		for(int i = 0; i < c; ++i)
			if(this.hand.size() > 0)
				this.hand.remove((int)(Math.random() * this.hand.size()));
	}

	public Aura addSecretAura(final LuaFunction trigger)
	{
		final Aura a = new Aura(this, trigger);
		this.secrets.add(a);
		return a;
	}

	public void gainGold(final int m)
	{
		this.gold += m;
		if(this.gold > this.maxGold)
			this.gold = this.maxGold;
	}

	public void gainGoldCrystals(final int m)
	{
		this.maxGold += m;
		if(this.maxGold > 10)
			this.maxGold = 10;
	}

	public void destroyGoldCrystals(final int m)
	{
		this.maxGold -= m;
		if(this.maxGold < 0)
			this.maxGold = 0;
		if(this.gold > this.maxGold)
			this.gold = this.maxGold;
	}

	public void lose()
	{
		this.game.log(this.name + " dies!");
		if(this.game.currentPlayer().equals(this))
			this.game.log(this.game.currentOpponent().name + " wins!");
		else
			this.game.log(this.game.currentPlayer().name + " wins!");
	}
}