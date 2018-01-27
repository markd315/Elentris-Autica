package tcg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.Varargs;

import tcg.Triggerable.Type;


public class Game
{
	public static final int MAXIMUM_BOARD_SIZE = 8;
	public static final int MAXIMUM_HAND_SIZE = 12;
	public ArrayList<Player> players;
	int current;
	public GameState state;
	public Object self;
	public Object other;
	public Aura aura;
	List<Aura> globalAuras;
	List<Option> options;
	public int minionsPlayed;
	public int turn;
	public LuaFunction filter;
	public LuaFunction onTarget;
	public int spellCount;
	LinkedList<Stack> stack;
	public LinkedList<String> combatLog;

	public Game()
	{
		super();
		this.players = new ArrayList<Player>();
		this.current = 0;
		this.state = GameState.MULLIGAN;
		this.globalAuras = new LinkedList<Aura>();
		this.options = new LinkedList<Option>();
		this.minionsPlayed = 0;
		this.turn = 1;
		this.spellCount = 0;
		this.stack = new LinkedList<Stack>();
		this.combatLog = new LinkedList<String>();
	}

	public List<Aura> aurasByStat(final Stat stat)
	{
		final List<Aura> l = new LinkedList<Aura>();
		for(final Aura a : this.globalAuras)
			if(a.stat == stat)
				l.add(a);
		return l;
	}

	public int sumByStat(final Stat stat)
	{
		int s = 0;
		for(final Aura a : this.aurasByStat(stat))
			s += a.amountFor(this);
		return s;
	}

	public void init()
	{
		this.current = 0;
		this.currentPlayer().setOpponent(this.currentOpponent());
		this.currentOpponent().setOpponent(this.currentPlayer());
		for(final Player p : this.players)
			for(int i = 0; i < 4; ++i)
				p.blockedDraw();
		this.currentOpponent().blockedDraw();
		this.combatLog.clear();
	}

	public void start()
	{
		this.state = GameState.CAST_SPELL;
		this.currentPlayer().setMaxGold(1);
		this.currentPlayer().setMaxEnergy(1);
		this.currentPlayer().setMana(3);
		this.currentPlayer().setRage(0);
		this.currentPlayer().setEnergy(1);
		this.currentPlayer().setGold(1);
		for(final Player p : this.players)
			while(p.getHand().size() < 5)
				p.blockedDraw();
		this.currentOpponent().getHand().add(new Spell(Card.get("Tricard"), currentOpponent(), Card.get("Tricard").getTyping()));
		this.combatLog.clear();
		this.log("Turn " + this.turn + ": " + this.currentPlayer().name);
	}

	public Player currentPlayer()
	{
		return this.players.get(this.current);
	}

	public Player currentOpponent()
	{
		return this.players.get((this.current + 1) % this.players.size());
	}

	public void endTurn()
	{
		List<Character> l = new LinkedList<Character>();
		for(final Player p : this.players)
		{
			l.add(p);
			for(final Minion m : p.getBoard())
				l.add(m);
		}
		for(final Character c : l)
		{
			if(c.getController() == this.currentPlayer())
			{
				c.unfreeze();
				if(c.poisoned > 0)
					c.damage(c.poisoned, "NORMAL", this.currentOpponent());
				if(c.burned > 0)
					c.damage(c.burned, "FIRE", this.currentOpponent());
				if(c instanceof Minion && ((Minion)c).isMarkedForDeath())
					c.destroy();
			}
			c.onEvent(Event.END_OF_TURN, c, null);
			final List<Aura> remove = new LinkedList<Aura>();
			for(final Aura a : this.globalAuras)
				if(a.expires)
					remove.add(a);
			for(final Aura a : remove)
				this.globalAuras.remove(a);
			remove.clear();
			for(final Aura a : c.getAuras())
				if(a.expires)
					remove.add(a);
			for(final Aura a : remove)
				c.getAuras().remove(a);
			if(c instanceof Player)
			{
				final Player p2 = (Player)c;
				if(!p2.hasWeapon())
					continue;
				remove.clear();
				for(final Aura a2 : p2.getWeapon().getAuras())
					if(a2.expires)
						remove.add(a2);
				for(final Aura a2 : remove)
					p2.getWeapon().getAuras().remove(a2);
			}
		}
		this.current = (this.current + 1) % this.players.size();
		if(this.current == 0)
			++this.turn;
		this.log("");
		this.log("Turn " + this.turn + ": " + this.currentPlayer().name);
		this.spellCount = 0;
		this.minionsPlayed = 0;
		final Player p3 = this.currentPlayer();
		if(p3.getMaxGold() < 15)
		{
			final Player player = p3;
			player.setMaxGold(player.getMaxGold() + 1);
		}
		p3.setGold(p3.getMaxGold() - p3.getInflation());
		p3.setMana(p3.getMana() + 1);
		p3.setRage(Math.max(0, p3.getRage() - 1));
		p3.setInflation(0);
		p3.setFlux(1);
		p3.attackCount = 0;
		for(final Minion i : p3.getBoard())
		{
			i.setSick(false);
			i.attackCount = 0;
		}
		p3.draw();
		l = new LinkedList<Character>();
		for(final Player p : this.players)
		{
			l.add(p);
			for(final Minion m : p.getBoard())
				l.add(m);
		}
		for(final Character c : l)
			c.onEvent(Event.START_OF_TURN, c, null);
		p3.setMaxEnergy(2 * p3.getMaxEnergy());
		if(p3.getMaxEnergy() == 0)
			p3.setMaxEnergy(1);
		p3.setEnergy(p3.getMaxEnergy() / p3.getFlux());
		if(p3.getMaxGold() == 15)
			p3.setEnergy(p3.setMaxEnergy(Integer.MAX_VALUE));
	}

	public Character randomEnemyExcept(Character d)
	{
		ArrayList<Character> c = new ArrayList<Character>();
		for(Minion m : d.getOwner().getBoard())
			c.add(m);
		c.add(d.getOwner());
		Random r = new Random();
		return c.get(r.nextInt(c.size()));
	}

	public void skipTurn()
	{
		List<Character> l = new LinkedList<Character>();
		for(final Player p : this.players)
		{
			p.setEnergy(p.getMaxEnergy());
			l.add(p);
			for(final Minion m : p.getBoard())
				l.add(m);
		}
		for(final Character c : l)
		{
			if(c.getController() == this.currentPlayer())
			{
				c.unfreeze();
				if(c.poisoned > 0)
					c.damage(c.poisoned, "NORMAL", this.currentOpponent());
				if(c.burned > 0)
					c.damage(c.burned, "FIRE", this.currentOpponent());
				if(c instanceof Minion && ((Minion)c).isMarkedForDeath())
					c.destroy();
			}
			c.onEvent(Event.END_OF_TURN, c, null);
			final List<Aura> remove = new LinkedList<Aura>();
			for(final Aura a : this.globalAuras)
				if(a.expires)
					remove.add(a);
			for(final Aura a : remove)
				this.globalAuras.remove(a);
			remove.clear();
			for(final Aura a : c.getAuras())
				if(a.expires)
					remove.add(a);
			for(final Aura a : remove)
				c.getAuras().remove(a);
			if(c instanceof Player)
			{
				final Player p2 = (Player)c;
				if(!p2.hasWeapon())
					continue;
				remove.clear();
				for(final Aura a2 : p2.getWeapon().getAuras())
					if(a2.expires)
						remove.add(a2);
				for(final Aura a2 : remove)
					p2.getWeapon().getAuras().remove(a2);
			}
		}
		++this.turn;
		this.log("");
		this.log("Turn " + this.turn + ": " + this.currentPlayer().name);
		this.spellCount = 0;
		this.minionsPlayed = 0;
		final Player p3 = this.currentPlayer();
		if(p3.getMaxGold() < 15)
		{
			final Player player = p3;
			player.setMaxGold(player.getMaxGold() + 1);
		}
		p3.setGold(p3.getMaxGold() - p3.getInflation());
		p3.setMana(p3.getMana() + 1);
		p3.setRage(Math.max(0, p3.getRage() - 1));
		p3.setInflation(0);
		p3.setFlux(1);
		p3.attackCount = 0;
		for(final Minion i : p3.getBoard())
		{
			i.setSick(false);
			i.attackCount = 0;
		}
		p3.draw();
		l = new LinkedList<Character>();
		for(final Player p : this.players)
		{
			l.add(p);
			for(final Minion m : p.getBoard())
				l.add(m);
		}
		for(final Character c : l)
			c.onEvent(Event.START_OF_TURN, c, null);
		p3.setMaxEnergy(2 * p3.getMaxEnergy());
		if(p3.getMaxEnergy() == 0)
			p3.setMaxEnergy(1);
		p3.setEnergy(p3.getMaxEnergy() / p3.getFlux());
		if(p3.getMaxGold() == 15)
			p3.setEnergy(p3.setMaxEnergy(Integer.MAX_VALUE));
	}

	public void combat(final Character attacker, final Character defender)
	{
		combat(attacker, defender, false);
	}

	public void combat(final Character attacker, final Character defender, boolean alreadyMissed)
	{
		this.log(String.valueOf(attacker.name) + " attacks " + String.valueOf(defender.name));
		if(((Math.random() < attacker.missChance || Math.random() < defender.dodgeChance)) && defender.getOwner().getBoard().size() > 0 && !alreadyMissed)
		{
			this.log("But misses instead!");
			combat(attacker, randomEnemyExcept(defender), true);
			return;
		}
		++attacker.attackCount;
		attacker.stealth = false;
		attacker.onEvent(Event.ATTACK, attacker, defender);
		int a1 = attacker.getAttack();
		int a2 = (defender instanceof Minion) ? defender.getAttack() : 0;
		if(a1 > 0)
		{
			int mitigation = Math.min(a1, defender.armor);
			defender.armor -= mitigation;
			a1 -= mitigation;
			int lft = 0;
			if(defender.lifeSteal && !attacker.isRanged)
			{
				lft = (int)(defender.attack * attacker.hitCoefficient(defender.getCamo()));
				int d = a1;
				a1 -= lft;
				lft -= d;
				defender.heal(lft);
			}
			defender.switchC();
			double hitco = defender.hitCoefficient(attacker.getType());
			//Next 2 lines account for dualtyping
			if(attacker.getType() != attacker.getType2())
				hitco*= defender.hitCoefficient(attacker.getType2());
			if(defender.combatDamage((int)(a1 * hitco), attacker))
			{
				if(attacker.isToxic && defender instanceof Minion)
					defender.destroy();
				if(defender.isToxic && attacker instanceof Minion)
					attacker.destroy();
				attacker.onEvent(Event.DAMAGE_DEALT, defender, attacker);
				defender.onEvent(Event.DAMAGE, defender, attacker);
			}
			defender.switchC();
			if(defender instanceof Minion && attacker.splash)
			{
				Minion ds = (Minion)defender;
				for(Minion m : ds.getAdjacent())
					if(m.combatDamage((int)(a1 * attacker.hitCoefficient(m.getCamo())), attacker))
					{
						if(attacker.lifeSteal)
						{
							m.switchC();
							lft = (int)(attacker.attack * defender.hitCoefficient(attacker.getType()));
							m.switchC();
							int d = a2;
							a2 -= lft;
							lft -= d;
							attacker.heal(lft);
						}
						attacker.onEvent(Event.DAMAGE_DEALT, defender, attacker);
						defender.onEvent(Event.DAMAGE, defender, attacker);
					}
			}
		}
		if(a2 > 0 && !attacker.isRanged)
		{
			a2 -= attacker.shield;
			int mitigation = Math.min(a2, attacker.armor);
			if(mitigation > 0)
				attacker.armor -= mitigation;
			a2 -= mitigation;
			int lft = 0;
			if(attacker.lifeSteal)
			{
				defender.switchC();
				double hitco = defender.hitCoefficient(attacker.getType());
				//Next 2 lines account for dualtyping
				if(attacker.getType() != attacker.getType2())
					hitco*= defender.hitCoefficient(attacker.getType2());
				lft = (int)(attacker.attack * hitco);
				defender.switchC();
				int d = a2;
				a2 -= lft;
				lft -= d;
				attacker.heal(lft);
			}
			if(attacker.getCamo() != attacker.getType()) //If defender is camoed
			{
			if(attacker.combatDamage((int)(a2 * attacker.hitCoefficient(defender.getCamo())), defender))
			{
				if(attacker.isToxic && defender instanceof Minion)
					defender.destroy();
				if(defender.isToxic && attacker instanceof Minion)
					attacker.destroy();
				attacker.onEvent(Event.DAMAGE, attacker, defender);
				defender.onEvent(Event.DAMAGE_DEALT, attacker, defender);
			}
			}
			else //If defender is NOT camoed.
			{
				double hitco = attacker.hitCoefficient(defender.getType());
				//Next 2 lines account for dualtyping
				if(defender.getType() != defender.getType2())
					hitco*= attacker.hitCoefficient(defender.getType2());
				if(attacker.combatDamage((int)(a2 * hitco), defender))
				{
					if(attacker.isToxic && defender instanceof Minion)
						defender.destroy();
					if(defender.isToxic && attacker instanceof Minion)
						attacker.destroy();
					attacker.onEvent(Event.DAMAGE, attacker, defender);
					defender.onEvent(Event.DAMAGE_DEALT, attacker, defender);
				}
			}
		}
		if(attacker.cooldown)
			attacker.freeze(2);
		this.postDamage();
		if(attacker instanceof Player && ((Player)attacker).hasWeapon())
			((Player)attacker).getWeapon().looseDurability(-1);
	}

	public void postDamage()
	{
		this.currentPlayer().setRage(this.currentPlayer().getRage() + 1);
		final LinkedList<Minion> dead = new LinkedList<Minion>();
		for(final Player p : this.players)
			for(final Minion m : p.getBoard())
				if(m.getHealth() <= 0)
					dead.add(m);
		for(final Minion i : dead)
		{
			i.getController().getBoard().remove(i);
			this.log(String.valueOf(i.name) + " dies");
			i.onEvent(Event.DEATH, i, null);
		}
		for(final Player p : this.players)
			if(p.getHealth() <= 0)
				p.lose();
	}

	public List<Character> validTargets(final LuaFunction filter)
	{
		final LinkedList<Character> targets = new LinkedList<Character>();
		for(final Player p : this.players)
		{
			if("true".equals(this.invoke(filter, p).toString()))
				targets.add(p);
			for(final Minion m : p.getBoard())
				if(!m.isShroud() && "true".equals(this.invoke(filter, m).toString()))
					targets.add(m);
		}
		return targets;
	}

	public int countTargets(final LuaFunction filter)
	{
		return this.validTargets(filter).size();
	}

	public void chooseTarget(final LuaFunction filter, final LuaFunction handler)
	{
		if(this.countTargets(filter) == 0)
			return;
		this.filter = filter;
		this.onTarget = handler;
		this.state = GameState.TARGET;
	}

	public void forEach(final LuaFunction filter, final LuaFunction handler)
	{
		for(final Character c : this.validTargets(filter))
			if(c.getHealth() > 0)
				this.invoke(handler, c);
	}

	public void forEachRandom(final int count, final LuaFunction filter, final LuaFunction handler)
	{
		List<Character> targets = this.validTargets(filter);
		Collections.shuffle(targets);
		targets = targets.subList(0, Math.min(count, targets.size()));
		for(final Character c : targets)
			if(c.getHealth() > 0)
				this.invoke(handler, c);
	}

	public void forEachExceptRandom(final int count, final LuaFunction filter, final LuaFunction handler)
	{
		List<Character> targets = this.validTargets(filter);
		Collections.shuffle(targets);
		targets = targets.subList(0, Math.max(0, targets.size() - count));
		for(final Character c : targets)
			if(c.getHealth() > 0)
				this.invoke(handler, c);
	}

	private void assignDamageForEach(final List<Character> list, Type type, final int d, final Character source)
	{
		for(final Character c : list)
		{
			c.typedDamage(d, type.name(), c);
			c.onEvent(Event.DAMAGE, c, source);
		}
		this.postDamage();
	}

	public void damageForEach(final LuaFunction filter, final String t, final int d, final Character source)
	{
		Type type = Card.typeTranslate(t);
		this.assignDamageForEach(this.validTargets(filter), type, d, source);
	}

	public void spellDamageForEach(final LuaFunction filter, Type t, int d, final Character source)
	{
		d += source.getOwner().sumByStat(Stat.SPELL_POWER);
		this.assignDamageForEach(this.validTargets(filter), t, d, source);
	}

	public void damageForEachRandom(final int count, final LuaFunction filter, final String t, final int d, final Character source)
	{
		List<Character> targets = this.validTargets(filter);
		Collections.shuffle(targets);
		Type type = Card.typeTranslate(t);
		targets = targets.subList(0, Math.max(0, targets.size() - count));
		this.assignDamageForEach(targets, type, d, source);
	}


	public void spellDamageForEachRandom(final int count, final LuaFunction filter, final String t, int d, final Character source)
	{
		d += source.getOwner().sumByStat(Stat.SPELL_POWER);
		List<Character> targets = this.validTargets(filter);
		Collections.shuffle(targets);
		Type type = Card.typeTranslate(t);
		targets = targets.subList(0, Math.max(0, targets.size() - count));
		this.assignDamageForEach(targets, type, d, source);
	}

	public void chooseOne(final String s1, final LuaFunction h1, final String s2, final LuaFunction h2)
	{
		this.options.clear();
		this.options.add(new Option(this.self, s1, h1));
		this.options.add(new Option(this.self, s2, h2));
		this.state = GameState.CHOOSE_ONE;
	}

	public void chooseOne(final String s1, final LuaFunction h1, final String s2, final LuaFunction h2, final String s3, final LuaFunction h3)
	{
		this.options.clear();
		this.options.add(new Option(this.self, s1, h1));
		this.options.add(new Option(this.self, s2, h2));
		this.options.add(new Option(this.self, s3, h3));
		this.state = GameState.CHOOSE_ONE;
	}

	public void chooseOne(final String s1, final LuaFunction h1, final String s2, final LuaFunction h2, final String s3, final LuaFunction h3, final String s4, final LuaFunction h4, final String s5, final LuaFunction h5, final String s6, final LuaFunction h6, final String s7, final LuaFunction h7, final String s8, final LuaFunction h8)
	{
		this.options.clear();
		this.options.add(new Option(this.self, s1, h1));
		this.options.add(new Option(this.self, s2, h2));
		this.options.add(new Option(this.self, s3, h3));
		this.options.add(new Option(this.self, s4, h4));
		this.options.add(new Option(this.self, s5, h5));
		this.options.add(new Option(this.self, s6, h6));
		this.options.add(new Option(this.self, s7, h7));
		this.options.add(new Option(this.self, s8, h8));
		this.state = GameState.CHOOSE_ONE;
	}

	public void chooseOne(final String s1, final LuaFunction h1, final String s2, final LuaFunction h2, final String s3, final LuaFunction h3, final String s4, final LuaFunction h4, final String s5, final LuaFunction h5, final String s6, final LuaFunction h6, final String s7, final LuaFunction h7, final String s8, final LuaFunction h8, final String s9, final LuaFunction h9, final String s10, final LuaFunction h10)
	{
		this.options.clear();
		this.options.add(new Option(this.self, s1, h1));
		this.options.add(new Option(this.self, s2, h2));
		this.options.add(new Option(this.self, s3, h3));
		this.options.add(new Option(this.self, s4, h4));
		this.options.add(new Option(this.self, s5, h5));
		this.options.add(new Option(this.self, s6, h6));
		this.options.add(new Option(this.self, s7, h7));
		this.options.add(new Option(this.self, s8, h8));
		this.options.add(new Option(this.self, s9, h9));
		this.options.add(new Option(this.self, s10, h10));
		this.state = GameState.CHOOSE_ONE;
	}

	public boolean combo()
	{
		return this.spellCount > 1;
	}

	public int random(final int min, final int max)
	{
		return (int)(Math.random() * (max - min + 1)) + min;
	}

	public Varargs invoke(final LuaFunction handler, final Object self)
	{
		return this.invoke(handler, self, null);
	}

	public Varargs invoke(final LuaFunction handler, final Object self, final Object other)
	{
		Varargs result = null;
		this.stack.push(new Stack(this.self, this.other, this.aura));
		this.self = self;
		this.other = other;
		if(handler != null)
			result = handler.invoke();
		final Stack s = this.stack.pop();
		this.self = s.self;
		this.other = s.other;
		this.aura = s.aura;
		return result;
	}

	public void removeAura(final Aura aura)
	{
		this.globalAuras.remove(aura);
		for(final Player player : this.players)
		{
			player.getSecrets().remove(aura);
			player.getAuras().remove(aura);
			for(final Minion m : player.getBoard())
				m.getAuras().remove(aura);
		}
	}

	public void log(final String s)
	{
		System.out.println(s);
		this.combatLog.push(s);
	}

	public class Stack
	{
		Object self;
		Object other;
		Aura aura;

		public Stack(final Object self, final Object other, final Aura aura)
		{
			super();
			this.self = self;
			this.other = other;
			this.aura = aura;
		}
	}
}
// General ideas: Give opponent same card(s) when killed.  Proc/toggle cards? Eh.  Cards with poison/burn.
// gain stats until next turn. dr/bc: gain mana crystal or eMultiplier.  BC: gain stats if opponent weapon.
// BC: Gain stats if friendly armor.  BC: Steal a character's armor.  0/0, BC: gain stats.  BC: whirlwind.
// BC: Shuffle the order of minions on the board.
// CostMod: energy cost reduced by energy spent while it's in your hand.
// onGoldSpent: gain 1 attack.
// Negative spell damage.
/*Elentris Autica as a name for HOADG.

Playtest variations on different AOE vulnerable board-floods with different stats, splits, etc.  hypothesis: uneven stat splits are favorable unless health/attack is split as well.  There should be strictly more stats than one-off minions, right?

Market HOADG as a competitive, cutting edge game theory TCG with a game-controlling, anti-rush, innovative synergy mantra.

HOADG: do more complicated stuff with deck reads, shuffles, reveal to opponent's, etc. have some cards draw a card and cost only energy as a way to generate information and strategy.

Laser gate which damages every minion played. Another which restricts minion costing.

Sinistral and Dextral mages and/or strikes for HOADG. Spells and minions that remove from the right or left of the board. Make one kinda cheaper and both high-value to create a mixed strategy nash equilibrium.
Have two cards, a really strong one, and a weaker one that automatically wins the game when your opponent plays the other one.
*/

// Legendary sounding names
// Vicewing, Alin Gashagar(lifesteal windfury)
// Darius Svinhildr(BC: Stealth minions gain +4 attack).  Bane Norwood(At the end of your turn, summon a 20/40 with taunt).
// Tyra Swiftheart(Dealt damage heals your face.  CHARGE)
// Time: Silvesse(At the start of your turn, double all of your resources), Seath Dar(Recall a minion).
// Light: Oromis Dawnsoar(Deathrattle Heal 50 to a damaged friendly. Taunt DivineShield), Eothas Featherlight(Split 50 light damage across all enemies.).
// Industry: Misoxenus Dominus(Destroy a NATURE minion), John Rockfeller(Randomly spawn a company).
// Companies: Oil company: Doubles energy output.  Weapons Company: Gives +10 attack to all friendly minions.  Rail company: You have an extra gold each turn.  Steel company: Gives +12 health to all minions.  Automotive company: Grant a random friendly minion windfury at end of turn.

// Less than legendary sounding names
// Fire: Ember elf(Deals 10 random fire damage to an enemy at SOT), Lava behemoth(big ass minion. 90/90?), igneous frog(Spawns a sand pile on death), glassblower(Creates something from sand), sand pile.
// Nature: Ivory walker, Perish quail(All other minions die at the start of your turn), Underwhelming centaur(0 attack windfury charge), Speeding bronco(charger), Forest entity(taunt), stegosaurus(defense biased divineshield), purple ram, definitely not a ram(camos to normal)
// Dark: Shadow hound(Gains +20/+20 for every dark minion), Dark lord(Gives dark minions +16/+22), Ghast(Silence a minion), Eclipsed imp(When it dies, spawn your opponent one.), Cloaked bandit(stealth. On attack, steal a gold.), Impending crow(at the start of your turn, destroy an enemy minion),
// Black covenant assassin(randomly destroy one of your minions).
// Light: Bleeding acolyte(Loses 10 health on end of your turn), Hylayan lancer(Any minion hit becomes LIGHT type), High priest(Change a minion to LIGHT type), grieving paladin(gains +16/+30 for all minions that died this turn), sacred archer(ranged, gives you a holy arrow on death),
// peacelock(gives all other minions defender)
// Industry: G-Unit-132x(End of turn: deal 20 fire damage, 20 industry damage, 10 ice damage, and 10 alliance damage.), Genesis phalanx(If it's the only minion, gain 30 health and divine shield), Supervisor(deal 20 damage and give windfury), Unstable quark, Mech, Disillusioned worker.
// Time: Time lord(all friendly minions have windfury), Spacetime surfer(splash), Clockwork drake(Gains health equal to the turn number), reTARDIS(EOT: shuffle your board ),
// Steam punk=water+fire
// Dusktrooper=dark
// Pontius Pilot= Light+industry
// Grimm falconer= Nature+dark
// Warhawk= Fire+alliance
// Sacred Eagle=Light+Nature
// Flamethrower Tank=Fire+Industry
// Laser cannon= Light+Industry
// Bionic Turtle=Nature+Industry
// Twilight mage=Light+Dark
// Dragons:
// Norse Icemauler: Aqua
// Indian Rubyback= Fire
// Chinese Onyxfang= Dark
// American Goldwatcher= Industry
// Amazonian Vanthhorn = Nature.
// Japanese Hourcrest= Time
// Carribbean Scaleglimmer= Light
// Mexican Mourning Wyvern
// Dagxus of the Four Kingdoms
// runic summoner(summon a runechild of random type), crimson cultist(lifesteal), car salesman(your opponent loses a gold)
// brown bear(on damaged: spawn a brown cub), wise unicorn(spell damage +20, charge), disillusioned worker(adjacent minions have -20 attack), generic doge, Almarfell Blacksmith(Give a random friendly minion 15 armor).
// annoying hipster, double agent(deal 30 damage to a random friendly character), rogue demon-hunter(kill a minion with over 100 attack), retarded dwarf, dragon tamer(steal a dragon), potentially fraudulent banker,
// indeterminately tailed fox(has random attack 10-90), scantly clad maid(adjacent minions can't attack), trueshot bowman(ranged deal 100 damage), Deadshear pioneer(if it's the only minion, gain +20/+20)
// tea partiers(whenever a tea partier takes damage and lives, spawn 2 more), budget knight(shield: 20 armor: 20), flamboyant pope, ant(atk bias cheap), baby hippopotamus, glorious sea lion,
// Blissful dove(all minions have 0 attack), unfortunately misgendered starfish, miraculously cloaked giant(stealth),
// shockingly rude ocelot, worm(hp bias cheap), mana totem(gain a mana at the start of every turn), scarecrow(freezes attackers), blind dartsman(miss 75, randomly deals 20 dmg at end of turn), Federation Senator, Misty Isle sailor.
// druid of peace(all damage is halved this turn), druid of war(all damage is doubled this turn), psychotic sniper(randomly deal 80 damage), X-wing fighter, vengeful elf(deals the damage it takes), malignant elf(mark for death upon attacking), evil elf(steal 10 attack whenever it attacks), magus
// ascendant(spell damage +30)
// commander of azgar(adjacent minions have +30/+10), navy seal, bionic turtle, psychic oracle(Reveal the 3 cards on the top of your main library.), Lost arrow page(start of turn transform into a lost arrow squire which trasnsforms into a lost arrow knight), Iron kingdom mercenary(CHOICE: pay 2 gold
// to deal 35 damage).
// VeganGator matt homage
// Aluminum skeleton
// Frumious Bandersnatch
// Slithy Tove
// Mediocre ghost(dodge 75), Gravetender(Gains +2/+2 for every dead minion this game), Adrenal scout(give minions you play this turn 20 attack)
// Assault rifleman(ranged), Trojan mech(on death, spawn a gift for your opponents board), Peacekeeper(give a minion defender)
// Weapons
// Vorpal sword
// Dark: Banished Blade, Runesword(Heals the hero after attacking.).
// Light: Holy lance(heal a random damaged minion to full health on attack).
// Industry: MP7(ranged high dura), Desert Eagle(ranged low dura), .50 cal(ranged high dmg), bayonet
// Overcompensating longsword, Fatal scimitar(If this kills a minion, gain +1 durability), poisoned dartgun(RANGED poisons attacked minions), Gladius(),
// Spear(choose one: RANGED or +2 durability), Lyserias Halberd(ondeath spawn Lyseria), Runic dragon blade(Triple damage to dragons),
// Rallying Flag(Minions have +10/+10), Sword of the Legion(When you attack, give a minion +30atk),
// Staff of the Beyond(can attack stealthed shit).
// Spell names
// Normal: Elemental resolver(all minions lose type), Destroy x (Where x is every type of minion) Expel armor(everything loses its armor)
// elemental oath(Gain 100 attack and any type. Lose 50 health.), Dragon arrow(deals 100 damage, double to dragons),
// feint(Trigger on attack function. Draw card.), golden bullet(Destroy a minion), Silence, Restore mana(Gives 5 mana),
// Energize(Triple your maximum energy), Effort(Consume your rage and deal 10 damage for each).
// Posioned bullet(Deal normal damage and poison), Arcane blast(deal normal dmg), Arcane wave(Normal AOE)
// Teen Spirit(Give all minions +30+30), Equality(All minions have 1 hp), Law of the Land(Attacking costs 1 gold until next turn)
// reinforcements(fill your board with 10/10s), Null magic brace(Gives health and shroud), punish(destroy a minion that attacked last turn)
// runic shield(Give a minion a type and +30 hp), seal of renewal(Cure poison and heal 20 health every turn), reincarnation(Summon a dead minion).

// Fire: Blazing nova(40 damage to all characters), Lava jet(Deal 50 damage and burn.), Firebolt(Deal 100 damage.), Volcanic fissure(Split the board in two.),
// Hellfire arrow(Deal 30 damage, spawn a 25/30 if it kills something.), flare blitz(Give a minion charge and +40/+20), Ignite(burn), burnt offering(Burn a friendly minion(10), draw a card.)
// Aqua: Ice ray(freeze), whirlpool(Freeze all enemy minions), Aqua jet(60 damage), Tidal wave(50 to all enemy minions), Poseidon's blessing(Make a minion WATER and give it +20/+60), Call of the sea(Spawn 3 water 25/50's),
// frostbite(destroy a frozen minion), Ice shard(deal 50 damage to a frozen character), Alteration beam(Change a minion into another with the same cost)
// Nature: Tranquility, Static tempest, Darwin's beam, Divisive rift, Shock, Life beam, Earthquake, Mark of the forest.
// Mirage, Toxic, Slowsand(give a minion cooldown)
// Dark: Burst of Death, Abomination of the light, Death's door, Curse of the forsaken, Nocturnal projection
// Armageddon, Shadow nova, Eclipse, Shadow form, Demonic fury, Shadow blast,
// Light: Divine intervention, Seppuku, Sacred fire, Hymn of Negation, Falling star, Orb of light, Dawn
// Radiant calling, Dispel, universal decree.
// Time: Stasis hold, Spellshield, Meditation, Sap, Barrier of time, Accelerate, Rewind, Time bomb, general relativity, windup, Chaos wave.
// Industry: Flux Resolver, Capitalism, Incentive, Alteration ray, Shrink machine, Auction, Third law of robotics
// reinforced steel, Artillery Barrage, Conscription