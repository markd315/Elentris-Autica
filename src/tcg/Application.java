package tcg;

import java.io.File;
import java.io.FileReader;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JFileChooser;


public class Application
{
	static ScriptEngine engine;
	static Bindings bindings;

	public static void main(final String[] args) throws Exception
	{
		final ScriptEngineManager sem = new ScriptEngineManager();
		Application.engine = sem.getEngineByExtension(".lua");
		Application.bindings = Application.engine.createBindings();
		final Game game = new Game();
		Application.bindings.put("game", game);
		Application.engine.eval(new FileReader(new File("data/init.lua")), Application.bindings);
		File[] listFiles;
		for(int length = (listFiles = new File("data/spells").listFiles()).length, j = 0; j < length; ++j)
		{
			final File file = listFiles[j];
			if(file.getName().toLowerCase().endsWith(".lua"))
			{
				System.out.println("load " + file);
				Application.engine.eval(new FileReader(file), Application.bindings);
			}
		}
		for(int i = 0; i < 2; ++i)
		{
			final JFileChooser fc = new JFileChooser("data/decks");
			fc.setDialogTitle("Choose deck for player " + (i + 1));
			if(fc.showOpenDialog(null) != 0)
				return;
			final Deck deck = new Deck(fc.getSelectedFile());
			final Player p = new Player(game, deck);
			p.name = "Player " + (i + 1);
			game.players.add(p);
		}
		new Client(game);
		game.init();
	}
}