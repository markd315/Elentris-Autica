Card = luajava.bindClass("tcg.Card");

local spell = Card:createMinion(0, 'Magic Carp', 'WATER', 3, 2, 1, 'WATER.  SPLASH.  STEALTH.', 25, 20);
	spell.splash = true;
	spell.stealth = true;
local spell = Card:createMinion(0,'Snorkeler', 'WATER', 5, 16, 1, 'WATER.  STEALTH', 60, 20);
	spell.stealth = true;
local spell = Card:createMinion(0,'Lesser Nessie', 'WATER', 6, 16, 1, 'WATER', 45, 55);
local spell = Card:createMinion(0,'Ice Omen', 'WATER', 8, 120, 0, 'WATER.  Freezes attackers.', 90, 90);
	spell.onDamageDealt = function() if enemy() and this().controller ~= other().controller then this():freeze(); end end
local spell = Card:createMinion(0,'Platypus', 'WATER', 2, 2, 0, 'WATER', 20, 32);
local spell = Card:createMinion(0,'Fisherman', 'WATER', 3, 4, 0, 'WATER.  Draw a card.', 21, 35);
	spell.onCast = function() this().owner:draw(); end;

local spell = Card:createMinion(0,'Firefly', 'FIRE', 'NORMAL', 1, 1, 0, 'FIRE.  CAMOUFLAGE:NORMAL', 22, 15);

local spell = Card:createMinion(0, '3 Tailed Scorpion', 'NATURE', 1, 1, 0, 'NATURE.  SPLASH.', 15, 10);
	spell.splash = true;