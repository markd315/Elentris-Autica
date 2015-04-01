Card = luajava.bindClass("tcg.Card");

local spell = Card:createMinion(0, 'Dawnbringer', 12, 512, 1, 'End your turn and skip your opponents!', 10, 10);
	spell.onCast = function() game:skipTurn(); end;

local spell = Card:createMinion(0, 'Laerze', 6, 20, 0, 'Destroy your opponent\'s weapon and draw cards equal to the durability.', 34, 50);
	spell.onCast = function() if this().owner.opponent:hasWeapon() then for i=1, this().owner.opponent.weapon.durability, 1 do this().owner:draw() end; this().owner.opponent.weapon:destroy(); end end;
	
local spell = Card:createMinion(0, 'Rouellet', 8, 100, 0, 'Switch decks with your opponent.  On death: Switch back.', 80, 80);
	spell.onCast = function() this().controller:switchDecks(); end;
	spell.onDeath = function() this().controller:switchDecks(); end;

local spell = Card:createMinion(0, 'Aggrizar Frostrage', 12, 2048, 0, 'Freeze a character.  Freeze anything that this minion deals damage to.', 120, 125);
	spell.onCast = function() game:chooseTarget(character,function() this():freeze(); end); end;
	spell.onDamageDealt = function() if enemy() and this().controller ~= other().controller then this():freeze(); end end

local spell = Card:createMinion(0, 'Annison Runebender', 7, 60, 0, 'All spells cost 3 more mana.', 60, 80);
	spell.onCast = function() local source = this(); this():addGlobalAura('MANACOST',function() if this().card.type == 'spell' then return 3 else return 0 end end,false); end;
	
local spell = Card:createMinion(0, 'Kelth Bluelight', 8, 120, 0, 'Taunt.  Heal 50 health to a character.', 50, 95);
	spell.onCast = function() game:chooseTarget(character,function() this():heal(50); end); end;
	spell.taunt = true;

local spell = Card:createMinion(0,'Gwenyth Bladestar', 9, 60, 0, 'Deal 60 damage to all characters.', 60, 60);
	spell.onCast = function() local source=this(); game:damageForEach(function() return character and this()~=source end, 'NORMAL', 60, source.owner); end;

local spell = Card:createMinion(0, 'Andrejana Fasthoof', 'NATURE', 7, 64, 0, 'NATURE.  CHARGE.  DIVINE SHIELD.', 50, 75);
	spell.charge = true;
	spell.divineShield = true;
	
local spell = Card:createMinion(0,'Stormraven', 'NATURE', 10, 128, 1, 'NATURE.  Assume control of a random enemy minion.', 50, 40);
	spell.onCast = function() local source = this();
		game:forEachRandom(1,enemyMinion,function()
				this().controller.board:remove(this()); 
				this().controller = source.owner; 
				source.owner.board:add(this()); 
				this().sick = true;
			end);
		end;
		
local spell = Card:createMinion(0,'Pyroborn', 'FIRE', 12, 300, 2, 'FIRE.  Deal 20 FIRE damage to all enemies.  On Death: Summon a 30/50 Fire Elemental.', 120, 100);
	spell.onDeath = function() this().controller:summon('Fire Elemental'); end;
	spell.onCast = function() local source=this(); game:damageForEach(enemy, 'FIRE', 20, source.owner); end;
local spell = Card:createMinion(0, 'Fire Elemental', 'FIRE', 5, 5, 0, 'FIRE', 30, 50);

local spell = Card:createMinion(0,'Phoenix', 'FIRE', 9, 60, 0, 'FIRE.  On Death: Add a Phoenix to your hand.', 90, 70);
	spell.onDeath = function() this().controller:drawCard('Phoenix'); end;
	
local spell = Card:createMinion(0, 'Thaurum Shadowmaw', 'DARK', 8, 120, 0, 'Consume a minion and gain its attack and health.', 10, 10);
	spell.onCast = function()  local thaurum = this(); local hp; local atk; game:chooseTarget(minion,function() 
	hp = this():getHealth(); 
	atk = this():getAttack(); 
	thaurum:gain(atk, hp);
	thaurum:setMaxHealth(10+hp); 
	this().controller.board:remove(this()) 
	end); end;