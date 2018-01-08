package tcg;

import org.luaj.vm2.LuaFunction;


public class Option
{
	public Object source;
	public String title;
	public LuaFunction handler;

	public Option(final Object source, final String title, final LuaFunction handler)
	{
		super();
		this.source = source;
		this.title = title;
		this.handler = handler;
	}
}