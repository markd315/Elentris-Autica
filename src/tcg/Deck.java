package tcg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class Deck
{
	private LinkedList<Card> earlySpells;
	private LinkedList<Card> midSpells;
	private LinkedList<Card> lateSpells;

	public Deck(final File file)
	{
		super();
		this.setEarlySpells(new LinkedList<Card>());
		this.setMidSpells(new LinkedList<Card>());
		this.setLateSpells(new LinkedList<Card>());
		try
		{
			final BufferedReader fr = new BufferedReader(new FileReader(file));
			String line;
			while((line = fr.readLine()) != null && !line.equals("*"))
			{
				final String[] data = line.split("x ");
				final Card spell = Card.get(data[1].trim());
				if(spell != null)
					for(int i = 0; i < Integer.parseInt(data[0].trim()); ++i)
						this.getEarlySpells().add(spell);
				else
					System.err.println("spell not found: " + data[1].trim());
			}
			while((line = fr.readLine()) != null && !line.equals("*"))
			{
				final String[] data = line.split("x ");
				final Card spell = Card.get(data[1].trim());
				if(spell != null)
					for(int i = 0; i < Integer.parseInt(data[0].trim()); ++i)
					{
						this.getMidSpells().add(spell);
					}
				else
					System.err.println("spell not found: " + data[1].trim());
			}
			while((line = fr.readLine()) != null && !line.equals("*"))
			{
				final String[] data = line.split("x ");
				final Card spell = Card.get(data[1].trim());
				if(spell != null)
					for(int i = 0; i < Integer.parseInt(data[0].trim()); ++i)
						this.getLateSpells().add(spell);
				else
					System.err.println("spell not found: " + data[1].trim());
			}
			fr.close();
			int y=2;
			if (getEarlySpells().size() > 8 || getLateSpells().size() > 10 || (getEarlySpells().size()+getMidSpells().size()+getLateSpells().size())>60 || (getEarlySpells().size()+getMidSpells().size()+getLateSpells().size())<20)
			{
				System.out.println("Invalid deck!");
				y=0;
			}
			y=2/y;
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public LinkedList<Card> getEarlySpells() {
		return earlySpells;
	}

	public void setEarlySpells(LinkedList<Card> earlySpells) {
		this.earlySpells = earlySpells;
	}

	public LinkedList<Card> getMidSpells() {
		return midSpells;
	}

	public void setMidSpells(LinkedList<Card> midSpells) {
		this.midSpells = midSpells;
	}

	public LinkedList<Card> getLateSpells() {
		return lateSpells;
	}

	public void setLateSpells(LinkedList<Card> lateSpells) {
		this.lateSpells = lateSpells;
	}
}