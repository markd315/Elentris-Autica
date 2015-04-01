Card = luajava.bindClass("tcg.Card");

local spell = Card:createWeapon(231, 'Flaming Broadsword', 'FIRE', 5, 12, 0, 'FIRE', 60, 3);
local spell = Card:createWeapon(231, 'Spy Watch', 'TIME', 4, 8, 0, 'TIME.  RANGED', 16, 5);
	spell.isRanged = true;
local spell = Card:createWeapon(231, 'Sword and Shield', 'NORMAL', 3, 4, 1, 'SHIELD: 15', 20, 3);
	spell.shield = 15;