package tcg;

import java.util.LinkedList;
import java.util.List;

import org.luaj.vm2.LuaClosure;


public class GameObject
{
	public Player owner;
	public Player controller;
	public Game game;
	public static LinkedList<EventObject> eventStack;
	List<Aura> auras;
	static
	{
		GameObject.eventStack = new LinkedList<EventObject>();
	}

	public GameObject(final Game game)
	{
		super();
		this.auras = new LinkedList<Aura>();
		this.game = game;
	}

	protected void runEvent(final Event e, final Object source, final Object other)
	{
		for(final Aura a : this.getAuras())
		{
			this.game.aura = a;
			this.game.invoke(a.getTrigger(e), source, other);
		}
	}

	protected void onEvent(final Event e, final Object source, final Object other)
	{
		GameObject.eventStack.add(new EventObject(e, source, other));
		if(GameObject.eventStack.size() > 1)
			return;
		while(!GameObject.eventStack.isEmpty())
		{
			final EventObject eo = GameObject.eventStack.getFirst();
			this.runEvent(eo.event, eo.source, eo.other);
			GameObject.eventStack.remove(eo);
		}
	}

	public Aura addAura(final String stat, final int amount, final boolean expires)
	{
		final Aura a = new Aura(this, Stat.valueOf(stat), amount, expires);
		this.auras.add(a);
		return a;
	}

	public Aura addAura(final String stat, final LuaClosure calculation, final boolean expires)
	{
		final Aura a = new Aura(this, Stat.valueOf(stat), calculation, expires);
		this.auras.add(a);
		return a;
	}

	public Aura addAura(final Card card)
	{
		final Aura a = new Aura(this, Stat.NONE, 0, false);
		card.applyToAura(a);
		this.auras.add(a);
		return a;
	}

	public Aura addGlobalAura(final String stat, final int amount, final boolean expires)
	{
		final Aura a = new Aura(this, Stat.valueOf(stat), amount, expires);
		this.game.globalAuras.add(a);
		return a;
	}

	public Aura addGlobalAura(final String stat, final LuaClosure calculation, final boolean expires)
	{
		final Aura a = new Aura(this, Stat.valueOf(stat), calculation, expires);
		this.game.globalAuras.add(a);
		return a;
	}

	public void removeAuras()
	{
		this.auras.clear();
		final List<Aura> remove = new LinkedList<Aura>();
		for(final Aura a : this.game.globalAuras)
			if(a.owner == this)
				remove.add(a);
		for(final Aura a : remove)
			this.game.globalAuras.remove(a);
	}

	protected LinkedList<Aura> getAuras()
	{
		final LinkedList<Aura> l = new LinkedList<Aura>();
		l.addAll(this.auras);
		l.addAll(this.getController().secrets);
		l.addAll(this.getController().opponent.secrets);
		l.addAll(this.game.globalAuras);
		return l;
	}

	protected List<Aura> aurasByStat(final Stat stat)
	{
		final List<Aura> l = new LinkedList<Aura>();
		for(final Aura a : this.getAuras())
			if(a.stat == stat)
				l.add(a);
		return l;
	}

	protected int sumByStat(final Stat stat)
	{
		int s = 0;
		for(final Aura a : this.aurasByStat(stat))
			s += a.amountFor(this);
		return s;
	}

	public Player getController()
	{
		if(this.controller == null)
			return this.owner;
		return this.controller;
	}

	public class EventObject
	{
		Event event;
		Object source;
		Object other;

		public EventObject(final Event e, final Object source, final Object other)
		{
			super();
			this.event = e;
			this.source = source;
			this.other = other;
		}

		@Override
		public String toString()
		{
			return this.event + " " + this.source + " " + this.other;
		}
	}
}