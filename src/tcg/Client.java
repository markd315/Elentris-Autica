package tcg;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

//TODO Using Kryonet, Receive board data, hand data, numbers data from server
//TODO Send click data to server.  Nothing else.
public class Client extends JFrame
{
	private static final long serialVersionUID = 1L;
	private Game game;
	private Character attacker;
	private Spell spell;
	private Minion hover;
	private LinkedList<Target> targets;
	private LinkedList<Target> minions;

	boolean findTarget(final int x, final int y)
	{
		for(final Target t : this.targets)
			if(t.x <= x && x <= t.x + t.w && t.y <= y && y <= t.y + t.h)
				return true;
		return false;
	}

	Object getTarget(final int x, final int y)
	{
		Object o = null;
		for(final Target t : this.targets)
			if(t.x <= x && x <= t.x + t.w && t.y <= y && y <= t.y + t.h)
				o = t.o;
		return o;
	}

	Object getMinion(final int x, final int y)
	{
		for(final Target t : this.minions)
			if(t.x <= x && x <= t.x + t.w && t.y <= y && y <= t.y + t.h)
				return t.o;
		return null;
	}

	public Client(final Game q)
	{
		super("Game");
		this.targets = new LinkedList<Target>();
		this.minions = new LinkedList<Target>();
		this.game = q;
		this.setResizable(true);
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(final WindowEvent e)
			{
				System.exit(0);
			}
		});
		final JPanel display = new JPanel()
		{
			private static final long serialVersionUID = 1L;
			HashMap<String, Image> images = null;

			@Override
			public void paint(final Graphics gr)
			{
				if(this.images == null)
				{
					this.images = new HashMap<String, Image>();
					File[] listFiles;
					for(int length = (listFiles = new File("data/images").listFiles()).length, i = 0; i < length; ++i)
					{
						final File f = listFiles[i];
						if(f.getName().endsWith(".png"))
							this.images.put(f.getName().replace(".png", ""), new ImageIcon("data/images/" + f.getName()).getImage());
					}
				}
				Client.this.targets.clear();
				Client.this.minions.clear();
				final Graphics2D g = (Graphics2D)gr;
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g.setColor(new Color(47, 49, 51));
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setColor(Color.white);
				int cc = 0;
				for(final String s : Client.this.game.combatLog)
				{
					g.drawString(s, this.getWidth() - 300, 470 - 15 * cc);
					if(++cc == 18)
						break;
				}
				final int cw = this.getWidth() - 270;
				final int maxCards = cw / 140;
				Player player = Client.this.game.players.get(0);
				int y = 330;
				int x = 250;
				for(final Spell spell : player.getHand())
				{
					this.drawSpell(g, spell, x, y + 140);
					x += ((player.getHand().size() <= maxCards) ? 140 : ((cw - 140) / (player.getHand().size() - 1)));
				}
				x = 250;
				for(final Minion minion : player.getBoard())
				{
					this.drawMinion(g, player, minion, x, y + 0);
					x += 100;
				}
				this.drawMinion(g, player, null, x, y + 0);
				this.drawHero(g, player, 10, y + 120);
				player = Client.this.game.players.get(1);
				y = -20;
				x = 250;
				for(final Spell spell : player.getHand())
				{
					this.drawSpell(g, spell, x, y);
					x += ((player.getHand().size() <= maxCards) ? 140 : ((cw - 140) / (player.getHand().size() - 1)));
				}
				x = 250;
				for(final Minion minion : player.getBoard())
				{
					this.drawMinion(g, player, minion, x, y + 210);
					x += 100;
				}
				this.drawMinion(g, player, null, x, y + 210);
				this.drawHero(g, player, 10, y + 60);
				if(Client.this.game.state == GameState.MULLIGAN)
				{
					g.setColor(Color.white);
					g.drawString("Keep or Replace Cards", 350, 350);
					g.drawImage(this.images.get("start_game"), 10, 310, null);
					Client.this.targets.add(new Target(20, 320, 120, 50, "start"));
				} else if(Client.this.game.state == GameState.CAST_SPELL)
				{
					g.drawImage(this.images.get("endturn_active"), 10, 310, null);
					Client.this.targets.add(new Target(20, 320, 120, 50, "end"));
				} else if(Client.this.game.state == GameState.ATTACK)
				{
					g.drawImage(this.images.get("endturn_cancel"), 10, 310, null);
					Client.this.targets.add(new Target(20, 320, 120, 50, "cancel"));
				} else
					g.drawImage(this.images.get("endturn"), 10, 310, null);
				if(Client.this.hover != null)
					g.drawImage(this.getCardImage(Client.this.hover.getController(), null, Client.this.hover.getCard(), false), 40, 200, 217, 337, null);
				if(Client.this.game.state == GameState.CHOOSE_ONE)
				{
					x = 300;
					y = 170;
					for(final Option o : Client.this.game.options)
					{
						g.setColor(Color.black);
						g.fillRect(x, y, 400, 30);
						g.setColor(Color.white);
						g.drawString(o.title, x + 20, y + 22);
						g.setColor(Color.green);
						g.drawRect(x, y, 400, 30);
						Client.this.targets.add(new Target(x, y, 200, 30, o));
						y += 50;
					}
				}
			}

			BufferedImage getCardImage(final Player owner, final Spell s, final Card card, final boolean active)
			{
				final BufferedImage img = new BufferedImage(311, 484, 2);
				final Graphics2D g = (Graphics2D)img.getGraphics();
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				if(card.nr == 10000)
					return img;
				int ty = 0;
				if(card.type.equals("minion"))
				{
					g.setFont(new Font("Impact", Font.PLAIN, 30));
					g.drawImage(this.images.get("cardframe_minion" + (active ? "_active" : "")), 0, 0, null);
					g.drawString(card.name, 71, 83);
					g.drawImage(this.images.get("type_" + card.typing.toString()), 125, 402, null);
					if(card.isDragon)
					{
						g.drawImage(this.images.get("isDrag"), 145, 400, null);
					}
					g.setColor(Color.yellow);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getGoldCost()).toString(), 86, 325);
					else
						g.drawString(new StringBuilder().append(card.goldCost).toString(), 86, 325);
					g.setColor(Color.green);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getEnergyCost()).toString(), 176, 325);
					else
						g.drawString(new StringBuilder().append(card.energyCost).toString(), 176, 325);
					g.setColor(Color.red);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getRageCost()).toString(), 255, 325);
					else
						g.drawString(new StringBuilder().append(card.rageCost).toString(), 255, 325);
					g.setColor(Color.white);
					g.drawString(Integer.toString(card.attack), 95, 430);
					g.drawString(Integer.toString(card.health), 240, 432);
					g.drawImage(this.images.get("art" + card.nr), 89, 71, null);
				} else if(card.type.equals("spell"))
				{
					g.drawImage(this.images.get("cardframe_spell" + (active ? "_active" : "")), 0, 0, null);
					g.drawImage(this.images.get("art" + card.nr), 64, 103, null);
					g.setFont(new Font("Impact", Font.PLAIN, 30));
					g.setColor(Color.white);
					g.drawString(card.name, 71, 83);
					g.drawImage(this.images.get("type_" + card.typing.toString()), 125, 402, null);
					g.setColor(Color.yellow);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getGoldCost()).toString(), 113, 327);
					else
						g.drawString(new StringBuilder().append(card.goldCost).toString(), 113, 327);
					g.setColor(Color.green);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getEnergyCost()).toString(), 113, 354);
					else
						g.drawString(new StringBuilder().append(card.energyCost).toString(), 113, 354);
					g.setColor(Color.blue);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getManaCost()).toString(), 253, 329);
					else
						g.drawString(new StringBuilder().append(card.manaCost).toString(), 253, 329);
					g.setColor(Color.red);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getRageCost()).toString(), 253, 354);
					else
						g.drawString(new StringBuilder().append(card.rageCost).toString(), 253, 354);
					g.setColor(Color.white);
					ty = 14;
				} else if(card.type.equals("weapon"))
				{
					g.setFont(new Font("Impact", Font.PLAIN, 30));
					g.drawImage(this.images.get("cardframe_weapon" + (active ? "_active" : "")), 0, 0, null);
					g.setColor(Color.black);
					g.drawString(Integer.toString(card.attack), 94, 428);
					g.drawString(Integer.toString(card.durability), 247, 428);
					g.setColor(Color.yellow);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getGoldCost()).toString(), 86, 322);
					else
						g.drawString(new StringBuilder().append(card.goldCost).toString(), 86, 322);
					g.setColor(Color.green);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getEnergyCost()).toString(), 170, 322);
					else
						g.drawString(new StringBuilder().append(card.energyCost).toString(), 170, 322);
					g.setColor(Color.red);
					if(s != null)
						g.drawString(new StringBuilder().append(s.getRageCost()).toString(), 251, 322);
					else
						g.drawString(new StringBuilder().append(card.rageCost).toString(), 251, 322);
					g.setColor(Color.white);
					g.drawImage(this.images.get("art" + card.nr), 79, 90, null);
					g.drawString(card.name, 71, 83);
					g.drawImage(this.images.get("type_" + card.typing.toString()), 125, 402, null);
				}
				g.setColor(Color.white);
				if(card.description.length() > 0)
				{
					String d;
					int start;
					int end;
					int amount;
					for(d = card.description; d.contains("$"); d = String.valueOf(d.substring(0, start)) + amount + d.substring(end + 1))
					{
						start = d.indexOf("$");
						end = d.indexOf("$", start + 1);
						final String[] data = d.substring(start + 1, end).split(":");
						amount = Integer.parseInt(data[1]) + owner.sumByStat(Stat.valueOf(data[0]));
					}
					g.setFont(g.getFont().deriveFont(19.0f));
					final FontRenderContext frc = g.getFontRenderContext();
					final AttributedString styledText = new AttributedString(d);
					styledText.addAttribute(TextAttribute.FONT, g.getFont());
					final AttributedCharacterIterator m_iterator = styledText.getIterator();
					final int m_start = m_iterator.getBeginIndex();
					final int m_end = m_iterator.getEndIndex();
					final LineBreakMeasurer measurer = new LineBreakMeasurer(m_iterator, frc);
					measurer.setPosition(m_start);
					final float xx = 65.0f;
					float yy = 333 + ty;
					while(measurer.getPosition() < m_end)
					{
						final TextLayout layout = measurer.nextLayout(190.0f);
						yy += layout.getAscent();
						final float dx = layout.getAdvance();
						layout.draw(g, xx + (190.0f - dx) / 2.0f, yy);
						yy += layout.getDescent() + layout.getLeading();
					}
				}
				return img;
			}

			void drawSpell(final Graphics2D gg, final Spell s, final int x, final int y)
			{
				boolean active = false;
				if(Client.this.game.state == GameState.MULLIGAN || (Client.this.game.state == GameState.CAST_SPELL && Client.this.game.currentPlayer() == s.getOwner() && s.getOwner().getGold() >= s.getGoldCost() && s.getOwner().getMana() >= s.getManaCost() && s.getOwner().getEnergy() >= s.getEnergyCost() && s.getOwner().getRage() >= s.getRageCost()))
				{
					active = true;
					Client.this.targets.add(new Target(x, y, 155, 242, s));
				}
				gg.drawImage(this.getCardImage(s.getOwner(), s, s.getCard(), active), x, y, 155, 242, null);
			}

			void drawMinion(final Graphics2D gg, final Player p, final Minion m, final int x, final int y)
			{
				if(m != null)
				{
					boolean active = false;
					boolean canTarget = false;
					if(Client.this.game.state == GameState.TARGET && !m.isShroud())
						canTarget = "true".equals(Client.this.game.invoke(Client.this.game.filter, m).toString());
					if((Client.this.game.state == GameState.CAST_SPELL && Client.this.game.currentPlayer() == m.getController() && m.getAttack() > 0 && (!m.isSick() || m.hasCharge()) && !m.frozen() && (m.attackCount == 0 || (m.windfury && m.attackCount < 2)) && !m.isDefender()) || (Client.this.game.state == GameState.ATTACK && Client.this.game.currentPlayer() != m.getController() && !m.stealth && (!m.getController().hasTaunt() || m.isTaunt())) || (Client.this.game.state == GameState.TARGET && canTarget))
					{
						gg.setColor(Color.green);
						if(Client.this.game.state == GameState.TARGET)
							gg.setColor(Color.red);
						active = true;
						Client.this.targets.add(new Target(x, y, 100, 150, m));
					}
					Client.this.minions.add(new Target(x, y, 100, 150, m));
					final BufferedImage card = new BufferedImage(311, 484, 2);
					final Graphics2D g = (Graphics2D)card.getGraphics();
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g.drawImage(this.images.get("cardframe_minion_battle" + (m.isTaunt() ? "_taunt" : "") + (active ? ((Client.this.game.state == GameState.TARGET || Client.this.game.state == GameState.ATTACK) ? "_target" : "_active") : "")), 0, 0, null);
					//TODO Minion gfx here, card disp is done.
					if(m.frozen())
						g.drawImage(this.images.get("cardframe_minion_battle_frozen"), 0, 0, null);
					if(m.stealth)
						g.drawImage(this.images.get("cardframe_minion_battle_stealth"), 0, 0, null);
					if(m.angelicHalo)
						g.drawImage(this.images.get("cardframe_minion_battle_shield"), 0, 0, null);
					if(m.isSick() && !m.hasCharge())
						g.drawImage(this.images.get("sick"), 200, 50, null);
					g.setFont(new Font("Impact", Font.PLAIN, 25));
					g.drawString(Integer.toString(m.getAttack()), 92, 238);
					if(m.health != m.maxHealth)
						g.setColor(Color.red);
					g.drawString(Integer.toString(m.getHealth()), 196, 238);
					g.setColor(Color.white);
					gg.drawImage(card, x, y, 155, 242, null);
				}
				if(Client.this.game.state == GameState.SUMMON && p == Client.this.game.currentPlayer())
				{
					gg.setColor(Color.green);
					gg.drawOval(x + 20, y + 70, 20, 20);
					Client.this.targets.add(new Target(x + 20, y + 70, 20, 20, m));
				}
			}

			void drawHero(final Graphics2D g, final Player p, final int x, final int y)
			{
				if(p.frozen())
					g.drawImage(this.images.get("cardframe_minion_battle_frozen"), x, y, 138, 198, null);
				g.setFont(this.getFont().deriveFont(12.0f));
				g.setColor(Color.yellow);
				g.drawString(String.valueOf(p.getGold()) + " / " + p.getMaxGold(), x + 150, y + 120);
				g.setColor(Color.green);
				if(p.getMaxEnergy() == Integer.MAX_VALUE)
					g.drawString(" infinity/infinity ", x + 150, y + 150);
				else
					g.drawString(String.valueOf(p.getEnergy()) + " / " + p.getMaxEnergy(), x + 150, y + 150);
				g.setColor(Color.blue);
				g.drawString(String.valueOf(p.getMana()), x + 150, y + 180);
				g.setColor(Color.red);
				g.drawString(String.valueOf(p.getRage()), x + 150, y + 210);
				g.setFont(this.getFont().deriveFont(20.0f));
				g.setColor(Color.yellow);
				g.drawString(new StringBuilder().append(p.getAttack()).toString(), x + 20, y + 100);
				g.setColor(Color.red);
				g.drawString(new StringBuilder().append(p.getHealth()).toString(), x + 90, y + 100);
				g.setColor(Color.gray);
				g.drawString(new StringBuilder().append(p.armor).toString(), x + 90, y + 80);
				if(p.getWeapon() != null)
				{
					g.setColor(Color.yellow);
					g.drawString(new StringBuilder().append(p.getWeapon().getAttack()).toString(), x + 145, y + 70);
					g.drawString(new StringBuilder().append(p.getWeapon().getDurability()).toString(), x + 195, y + 70);
				}
				boolean canTarget = false;
				if(Client.this.game.state == GameState.TARGET)
					canTarget = "true".equals(Client.this.game.invoke(Client.this.game.filter, p).toString());
				if((Client.this.game.state == GameState.CAST_SPELL && Client.this.game.currentPlayer() == p && p.getAttack() > 0 && !p.frozen() && (p.attackCount == 0 || (p.windfury && p.attackCount < 2))) || (Client.this.game.state == GameState.ATTACK && Client.this.game.currentPlayer() != p && !p.hasTaunt()) || (Client.this.game.state == GameState.TARGET && canTarget))
				{
					g.setColor(Color.green);
					if(Client.this.game.state == GameState.TARGET)
						g.setColor(Color.red);
					g.drawRect(x, y, 138, 198);
					Client.this.targets.add(new Target(x, y, 138, 198, p));
				}
				g.setColor(Color.white);
			}
		};
		display.addMouseMotionListener(new MouseMotionAdapter()
		{
			@Override
			public void mouseMoved(final MouseEvent e)
			{
				Client.this.hover = (Minion)Client.this.getMinion(e.getX(), e.getY());
				if(Client.this.findTarget(e.getX(), e.getY()))
					display.setCursor(Cursor.getPredefinedCursor(12));
				else
					display.setCursor(Cursor.getPredefinedCursor(0));
			}
		});
		display.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(final MouseEvent e)
			{
				if(!Client.this.findTarget(e.getX(), e.getY()))
					return;
				final Object o = Client.this.getTarget(e.getX(), e.getY());
				if(Client.this.game.state == GameState.MULLIGAN)
				{
					if(o.equals("start"))
						Client.this.game.start();
					if(o instanceof Spell)
					{
						final Spell s = (Spell)o;
						s.getOwner().getHand().remove(s);
						s.getOwner().getMidLibrary().add(s);
						s.getOwner().shuffle();
					}
				} else if(Client.this.game.state == GameState.CAST_SPELL)
				{
					if(o.equals("end"))
						Client.this.game.endTurn();
					if(o instanceof Spell)
						if(((Spell)o).getCard().type.equals("minion"))
						{
							Client.this.spell = (Spell)o;
							Client.this.game.state = GameState.SUMMON;
						} else
							Client.this.game.currentPlayer().castSpell((Spell)o);
					if(o instanceof Character)
					{
						Client.this.attacker = (Character)o;
						Client.this.game.state = GameState.ATTACK;
					}
				} else if(Client.this.game.state == GameState.ATTACK)
				{
					if(o instanceof Character)
					{
						final Character defender = (Character)o;
						Client.this.game.combat(Client.this.attacker, defender);
						Client.this.game.state = GameState.CAST_SPELL;
					} else if(o.equals("cancel"))
						Client.this.game.state = GameState.CAST_SPELL;
				} else if(Client.this.game.state == GameState.TARGET)
				{
					Client.this.game.log(String.valueOf(Client.this.game.currentPlayer().name) + " targets " + ((Character)o).name);
					Client.this.game.state = GameState.CAST_SPELL;
					Client.this.game.invoke(Client.this.game.onTarget, o);
				} else if(Client.this.game.state == GameState.SUMMON)
				{
					Client.this.game.state = GameState.CAST_SPELL;
					Client.this.game.currentPlayer().castSpell(Client.this.spell, (Minion)o);
				} else if(Client.this.game.state == GameState.CHOOSE_ONE && o instanceof Option)
				{
					final Option option = (Option)o;
					Client.this.game.state = GameState.CAST_SPELL;
					Client.this.game.invoke(option.handler, option.source);
				}
			}
		});
		this.getContentPane().setLayout(new GridLayout());
		this.getContentPane().add(display);
		this.setSize(1224, 730);
		this.setVisible(true);
		new Thread()
		{
			@Override
			public void run()
			{
				while(true)
				{
					display.repaint();
					try
					{
						Thread.sleep(100L);
					} catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	class Target
	{
		int x;
		int y;
		int w;
		int h;
		Object o;

		public Target(final int x, final int y, final int w, final int h, final Object o)
		{
			super();
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.o = o;
		}
	}
}