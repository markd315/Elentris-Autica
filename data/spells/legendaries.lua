Card = luajava.bindClass("tcg.Card");

local spell = Card:createMinion(0, 'Eydrifr Malison', 4, 4, 1, 'RANGED.  Heal 20 health.', 25, 35);
  spell.isRanged = true;
  spell.onCast = function() game:chooseTarget(character,function() this():heal(20); end); end;

local spell = Card:createMinion(0, 'Soulgleam Prismaul', 5, 0, 3, 'LIFESTEAL, SPLASH', 30, 30);
  spell.lifeSteal = true;
  spell.splash = true;
  
local spell = Card:createMinion(0, 'Tacchi Allorstone', 5, 16, 0, 'TOXIC SKIN, SHROUD', 24, 55);
  spell.shroud = true;
  spell.isToxic = true;

local spell = Card:createMinion(0, 'Laerze', 6, 20, 0, 'Destroy your opponent\'s weapon and draw cards equal to the durability.', 42, 50);
spell.onCast = function() if this().owner.opponent:hasWeapon() then for i=1, this().owner.opponent.weapon.durability, 1 do this().owner:draw() end; this().owner.opponent.weapon:destroy(); end end;

local spell = Card:createMinion(0,'Xalvador the Conqueror', 6, 28, 0, 'Steal a minion with 30 or fewer attack.', 30, 60);
spell.onCast = function() local source = this(); game:chooseTarget(function() return enemyMinion() and this():getAttack() <= 30 end, function()
  this().controller.board:remove(this());
  this().controller = source.owner;
  source.owner.board:add(this());
  this().sick = true; end
) end;

local spell = Card:createMinion(0, 'Sheoth Sagehelm', 6, 32, 1, 'SHIELD: 50', 36, 55);
spell.shield = 50;

local spell = Card:createMinion(0, 'Drake Hatefall', 6, 100, 0, 'Mark a minion to die at the start of your next turn.', 50, 50);
spell.onCast = function() game:chooseTarget(minion,function() this():markForDeath(); end); end;

local spell = Card:createMinion(0, 'Deceptive Vixen', 'RANDOM', 7, 16, 1, 'Randomly changes types.', 50, 55);

local spell = Card:createMinion(0, 'Annison Runebender', 7, 60, 0, 'All spells cost 3 more mana.', 50, 60);
spell.onCast = function() local source = this(); this():addGlobalAura('MANACOST',function() if this().card.type == 'spell' then return 3 else return 0 end end,false); end;

local spell = Card:createMinion(0, 'Andrejana Fasthoof', 'NATURE', 7, 64, 0, 'CHARGE.  DIVINE SHIELD.', 64, 30);
spell.charge = true;
spell.angelicHalo = true;

local spell = Card:createMinion(0, 'Nightwind', 'DARK', 7, 128, 0, 'STEALTH, DIVINE SHIELD.', 68, 30);
spell.angelicHalo = true;
spell.stealth = true;

local spell = Card:createMinion(0, 'Rouellet', 8, 100, 0, 'Switch decks with your opponent.  On death: Switch back.', 60, 65);
spell.onCast = function() this().controller:switchDecks(); end;
spell.onDeath = function() this().controller:switchDecks(); end;

local spell = Card:createMinion(0, 'Nyssen Illim', 8, 0, 0, 'CHARGE, ENRAGE: +80 Attack', 20, 40);
  spell.onCast = function() this():addAura('ATTACK',function() if damaged() then return 80 else return 0 end end,false); end;
  spell.charge = true;
  
local spell = Card:createMinion(0, 'Thaurum Shadowmaw', 'DARK', 9, 120, 0, 'Consume a minion and gain its attack and health.', 10, 10);
spell.onCast = function() local thaurum = this(); local hp; local atk; game:chooseTarget(minion,function()
  hp = this():getHealth();
  atk = this():getAttack();
  thaurum:gain(atk, hp);
  thaurum:setMaxHealth(10+hp);
  this().controller.board:remove(this())
end); end;

local spell = Card:createMinion(0, 'Kelth Bluelight', 9, 120, 0, 'Taunt.  Heal 50 health to a character.', 44, 80);
spell.onCast = function() game:chooseTarget(character,function() this():heal(50); end); end;
spell.taunt = true;

local spell = Card:createMinion(0,'Gwenyth Bladestar', 9, 256, 0, 'Deal 50 damage to all other characters.', 50, 50);
spell.onCast = function() local source=this(); game:damageForEach(function() return character and this()~=source end, 'NORMAL', 50, source.owner); end;

local spell = Card:createMinion(0, 'Hydraulus', 'WATER', 9, 256, 0, 'Deal between 10 and 100 WATER damage.', 50, 70);
spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(math.random(10,100), 'WATER',game:currentPlayer()); end); end;

local spell = Card:createMinion(0,'Phoenix', 'FIRE', 9, 60, 0, 'On Death: Add a Phoenix to your hand.', 80, 66);
spell.onDeath = function() this().controller:drawCard('Phoenix'); end;

local spell = Card:createMinion(0,'Stormraven', 'NATURE', 10, 128, 1, 'Assume control of a random enemy minion.', 50, 40);
spell.onCast = function() local source = this();
  game:forEachRandom(1,enemyMinion,function()
    this().controller.board:remove(this());
    this().controller = source.owner;
    source.owner.board:add(this());
    this().sick = true;
  end);
end;

local spell = Card:createMinion(0, 'Awryb Steelpaw', 11, 0, 0, 'TAUNT, DIVINE SHIELD', 50, 100); --210 stats, 1.8 power effect
  spell.angelicHalo = true;
  spell.taunt = true;
  
local spell = Card:createMinion(0, 'Loch Ness Monster', 'WATER', 11, 500, 0, 'SPLASH.', 80, 110); --190 stats, 2.0 power effect
spell.splash = true;

local spell = Card:createMinion(0, 'Dawnbringer', 12, 512, 1, 'End your turn and skip your opponents!', 10, 10);
spell.onCast = function() game:skipTurn(); end;

local spell = Card:createMinion(0,'Pyroborn', 'FIRE', 12, 300, 2, 'Deal 20 FIRE damage to all enemies.  On Death: Summon a 30/50 Fire Elemental.', 60, 45);
spell.onDeath = function() this().controller:summon('Fire Elemental'); end;
spell.onCast = function() local source=this(); game:damageForEach(enemy, 'FIRE', 20, source.owner); end;
local spell = Card:createMinion(0, 'Fire Elemental', 'FIRE', 5, 5, 0, 'FIRE', 30, 50);

local spell = Card:createMinion(0, 'Aggrizar Frostrage', 12, 2048, 1, 'Freeze a character.  Freeze anything that this minion deals damage to.', 90, 105);
spell.onCast = function() game:chooseTarget(character,function() this():freeze(); end); end;
spell.onDamageDealt = function() if enemy() and this().controller ~= other().controller then this():freeze(); end end

local spell = Card:createMinion(0, 'Deity', 15, 0, 10, 'CHARGE, WINDFURY, LIFESTEAL, TAUNT, SHROUD, SPLASH, RANGED, STEALTH, DIVINE SHIELD.', 100, 100);
spell.angelicHalo = true;
spell.stealth = true;
spell.charge = true;
spell.windfury = true;
spell.lifeSteal = true;
spell.taunt = true;
spell.shroud = true;
spell.splash = true;
spell.isRanged = true;

  
local spell = Card:createMinion(0, 'Time-lost Drake', 15, 3000, 1, 'Always drawn after all other cards in its library.', 150, 200, "Dragon");
  spell.drawnLast = true;