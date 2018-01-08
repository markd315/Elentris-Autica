package tcg;

import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;


public class Aura extends Triggerable
{
	public GameObject owner;
	public boolean expires;
	public Stat stat;
	public int amount;
	public boolean dynamic;
	LuaClosure calculation;
	public LuaFunction onTrigger;
//TODO Fix health buffing
	public Aura(final GameObject owner, final Stat stat, final int amount, final boolean expires)
	{
		super();
		this.expires = false;
		this.stat = Stat.NONE;
		this.amount = 0;
		this.dynamic = false;
		this.owner = owner;
		this.stat = stat;
		this.amount = amount;
		this.expires = expires;
		this.dynamic = false;
	}

	public Aura(final GameObject owner, final Stat stat, final LuaClosure calculation, final boolean expires)
	{
		super();
		this.expires = false;
		this.stat = Stat.NONE;
		this.amount = 0;
		this.dynamic = false;
		this.owner = owner;
		this.stat = stat;
		this.calculation = calculation;
		this.expires = expires;
		this.dynamic = true;
	}

	public Aura(final GameObject owner, final LuaFunction onTrigger)
	{
		super();
		this.expires = false;
		this.stat = Stat.NONE;
		this.amount = 0;
		this.dynamic = false;
		this.owner = owner;
		this.onTrigger = onTrigger;
	}

	public int amountFor(final Object c)
	{
		if(!this.dynamic)
			return this.amount;
		return ((LuaValue)this.owner.game.invoke(this.calculation, c)).toint();
	}

	public void trigger(final Object source)
	{
		this.owner.game.removeAura(this);
		((Player)this.owner).secrets.remove(this);
		this.owner.onEvent(Event.SECRET_REVEALED, this, source);
		this.owner.game.invoke(this.onTrigger, this.owner, source);
	}
}