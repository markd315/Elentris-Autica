package tcg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;


public class Deck
{
	LinkedList<Card> earlySpells;
	LinkedList<Card> midSpells;
	LinkedList<Card> lateSpells;

	public Deck(final File file)
	{
		super();
		this.earlySpells = new LinkedList<Card>();
		this.midSpells = new LinkedList<Card>();
		this.lateSpells = new LinkedList<Card>();
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
						this.earlySpells.add(spell);
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
						this.midSpells.add(spell);
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
						this.lateSpells.add(spell);
				else
					System.err.println("spell not found: " + data[1].trim());
			}
			fr.close();
			int y=2;
			if (earlySpells.size() > 8 || lateSpells.size() > 10 || (earlySpells.size()+midSpells.size()+lateSpells.size())>60 || (earlySpells.size()+midSpells.size()+lateSpells.size())<20)
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
}