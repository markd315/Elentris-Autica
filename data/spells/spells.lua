Card = luajava.bindClass("tcg.Card");

local spell = Card:createSpell(156,'Firebolt', 'FIRE', 3, 2, 1, 0, 'Deal $SPELL_POWER:40$ FIRE damage');
	spell.onCast = function() game:chooseTarget(character, function() this():spellDamage(40,'FIRE',game:currentPlayer()); end ); end

local spell = Card:createSpell(198,'Ice Ray', 'WATER', 2, 0, 1, 0, 'Deal $SPELL_POWER:28$ WATER damage and freeze');
	spell.onCast = function() game:chooseTarget(character,function() this():spellDamage(28,'WATER',game:currentPlayer()); this():freeze(); end); end;

local spell = Card:createSpell(326,'Draw 4', 'NORMAL', 5, 16, 5, 0, 'Draw 4 cards.');
	spell.onCast = function() for i=1,4,1 do this().owner:draw() end end

local spell = Card:createSpell(326,'Draw 2', 'NORMAL', 2, 2, 2, 0, 'Draw 2 cards.');
	spell.onCast = function() for i=1,2,1 do this().owner:draw() end end
	
local spell = Card:createSpell(269,'Tricard', 'NORMAL', 0, 0, 0, 0, 'Spawn a 10/10, Gain one Gold this turn only, or Double your energy this turn only.');
	spell.onCast = function() local source = this(); game:chooseOne(
						"Spawn a 10/10", function() this().owner:summon('Cardmaster'); end,
						"Gain one Gold this turn only.", function() this().owner:addAura('GOLD',1,true); end,
						"Double your energy this turn only.", function() this().owner:addAura('ENERGY',1,true); end
						); end;
local spell = Card:createMinion(0, 'Cardmaster', 'NORMAL', 1, 1, 0, ' ', 10, 10);

local spell = Card:createSpell(41,'Arcane Foresight', 'NORMAL', 1, 16, 0, 0, 'Consume all of your mana and draw that many cards.');
	spell.onCast = function() for i=1,this().owner:useUpMana(),1 do this().owner:draw() end end
	
local spell = Card:createSpell(41,'Strategic Fury', 'NORMAL', 2, 4, 0, 0, 'Consume all of your rage and draw that many cards.');
	spell.onCast = function() for i=1,this().owner:useUpRage(),1 do this().owner:draw() end end