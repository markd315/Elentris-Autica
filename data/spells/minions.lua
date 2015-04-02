Card = luajava.bindClass("tcg.Card");

local spell = Card:createMinion(0, 'Circumcised Badger', 4, 7, 0, 'Why...', 30, 39);

local spell = Card:createMinion(0, 'Lumbering Ox', 6, 16, 0, 'COOLDOWN', 44, 100);
	spell.cooldown = true;

local spell = Card:createMinion(0, 'Slumbering Giant', 7, 64, 1, 'SLOW START', 120, 85);
	spell.onCast = function()  this():freeze(2); end;
	
local spell = Card:createMinion(0, 'Blood Golem', 4, 4, 1, 'LIFESTEAL', 34, 25);
	spell.lifeSteal = true;
	
local spell = Card:createMinion(0, 'Schrodinger\'s Paramecium', 2, 0, 0, 'TOXIC SKIN.  DODGE: 50%', 10, 10);
	spell.isToxic = true;
	spell.dodgeChance = 0.5;
	
local spell = Card:createMinion(0, 'Shapeshifter', 5, 16, 0, 'Secretly choose a type for this minion.', 38, 38);
	spell.onCast = function() game:chooseOne(
	"NORMAL", function() this():changeType('NORMAL'); end,
	"FIRE", function() this():changeType('FIRE'); end,
	"WATER", function() this():changeType('WATER'); end,
	"NATURE", function() this():changeType('NATURE'); end,
	"TIME", function() this():changeType('TIME'); end,
	"LIGHT", function() this():changeType('LIGHT'); end,
	"DARK", function() this():changeType('DARK'); end,
	"ALLIANCE", function() this():changeType('ALLIANCE'); end,
	"INDUSTRY", function() this():changeType('INDUSTRY'); end,
	"SPIRIT", function() this():changeType('SPIRIT'); end
	); end;