Card = luajava.bindClass("tcg.Card");

local spell = Card:createMinion(0, 'Water Elemental', 'WATER', 5, 5, 0, 'WATER', 40, 50);

local spell = Card:createMinion(0, 'Fire Elemental', 'FIRE', 5, 5, 0, 'FIRE', 40, 50);

local spell = Card:createMinion(0, 'Nature Elemental', 'NATURE', 5, 5, 0, 'NATURE', 40, 50);

local spell = Card:createMinion(0, 'Time Elemental', 'TIME', 5, 5, 0, 'TIME', 40, 50);

local spell = Card:createMinion(0, 'Unity Elemental', 'ALLIANCE', 5, 5, 0, 'ALLIANCE', 40, 50);

local spell = Card:createMinion(0, 'Dusk Elemental', 'DARK', 5, 5, 0, 'DARK', 40, 50);

local spell = Card:createMinion(0, 'Light Elemental', 'LIGHT', 5, 5, 0, 'LIGHT', 40, 50);

local spell = Card:createMinion(0, 'Other Elemental', 'SPIRIT', 5, 5, 0, 'SPIRIT', 40, 50);

local spell = Card:createMinion(0, 'Progress Elemental', 'INDUSTRY', 5, 5, 0, 'INDUSTRY', 40, 50);

local spell = Card:createMinion(0, 'Water Dancer', 'WATER', 2, 2, 0, 'WATER', 26, 28);

local spell = Card:createMinion(0, 'Fire Dancer', 'FIRE', 2, 2, 0, 'FIRE', 26, 28);

local spell = Card:createMinion(0, 'Nature Dancer', 'NATURE', 2, 2, 0, 'NATURE', 26, 28);

local spell = Card:createMinion(0, 'Tempo Dancer', 'TIME', 2, 2, 0, 'TIME', 26, 28);

local spell = Card:createMinion(0, 'Alliance Dancer', 'ALLIANCE', 2, 2, 0, 'ALLIANCE', 26, 28);

local spell = Card:createMinion(0, 'Night Dancer', 'DARK', 2, 2, 0, 'DARK', 26, 28);

local spell = Card:createMinion(0, 'Sunrise Dancer', 'LIGHT', 2, 2, 0, 'LIGHT', 26, 28);

local spell = Card:createMinion(0, 'Mystic Dancer', 'SPIRIT', 2, 2, 0, 'SPIRIT', 26, 28);

local spell = Card:createMinion(0, 'Modern Dancer', 'INDUSTRY', 2, 2, 0, 'INDUSTRY', 26, 28);

local spell = Card:createMinion(0, 'Water Priest', 'WATER', 4, 8, 0, 'WATER Deal 20  WATER damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'WATER',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Fire Priest', 'FIRE', 4, 8, 0, 'FIRE Deal 20  FIRE damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'FIRE',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Forest Priest', 'NATURE', 4, 8, 0, 'NATURE Deal 20  NATURE damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'NATURE',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Time Priest', 'TIME', 4, 8, 0, 'TIME Deal 20  TIME damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'TIME',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Alliance Priest', 'ALLIANCE', 4, 8, 0, 'ALLIANCE Deal 20  ALLIANCE damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'ALLIANCE',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Twilight Priest', 'DARK', 4, 8, 0, 'DARK Deal 20  DARK damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'DARK',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Light Priest', 'LIGHT', 4, 8, 0, 'LIGHT Deal 20  LIGHT damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'LIGHT',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Spirit Priest', 'SPIRIT', 4, 8, 0, 'SPIRIT Deal 20  SPIRIT damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'SPIRIT',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Mechanized Priest', 'INDUSTRY', 4, 8, 0, 'INDUSTRY Deal 20  INDUSTRY damage.', 22, 22);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(20, 'INDUSTRY',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Water Archmage', 'WATER', 10, 450, 0, 'WATER Deal 60  WATER damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'WATER',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Fire Archmage', 'FIRE', 10, 450, 0, 'FIRE Deal 60  FIRE damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'FIRE',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Nature Archmage', 'NATURE', 10, 450, 0, 'NATURE Deal 60  NATURE damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'NATURE',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Time Archmage', 'TIME', 10, 450, 0, 'TIME Deal 60  TIME damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'TIME',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Midnight Archmage', 'DARK', 10, 450, 0, 'DARK Deal 60  DARK damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'DARK',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Light Archmage', 'LIGHT', 10, 450, 0, 'LIGHT Deal 60  LIGHT damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'LIGHT',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Alliance Archmage', 'ALLIANCE', 10, 450, 0, 'ALLIANCE Deal 60  ALLIANCE damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'ALLIANCE',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Spirit Archmage', 'SPIRIT', 10, 450, 0, 'FIRE Deal 60  FIRE damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'SPIRIT',game:currentPlayer()); end); end;

local spell = Card:createMinion(0, 'Industrial Archmage', 'INDUSTRY', 10, 450, 0, 'INDUSTRY Deal 60  INDUSTRY damage.', 70, 65);
	spell.onCast = function() local source = this(); game:chooseTarget(character, function() this():typedDamage(60, 'INDUSTRY',game:currentPlayer()); end); end;

local spell = Card:createMinion(0,'Water Summoner', 'WATER', 7, 32, 0, 'WATER.  Summon a WATER 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Water Summonee'); end;
local spell = Card:createMinion(0,'Water Summonee', 'WATER', 3, 2, 0, 'WATER', 32, 32);
	spell.token = true;

local spell = Card:createMinion(0,'Fire Summoner', 'fire', 7, 32, 0, 'FIRE.  Summon a FIRE 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Fire Summonee'); end;
local spell = Card:createMinion(0,'Fire Summonee', 'fire', 3, 2, 0, 'FIRE', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Nature Summoner', 'Nature', 7, 32, 0, 'NATURE.  Summon a NATURE 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Nature Summonee'); end;
local spell = Card:createMinion(0,'Nature Summonee', 'Nature', 3, 2, 0, 'NATURE', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Dark Summoner', 'Dark', 7, 32, 0, 'DARK.  Summon a DARK 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Dark Summonee'); end;
local spell = Card:createMinion(0,'Dark Summonee', 'Dark', 3, 2, 0, 'DARK', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Light Summoner', 'Light', 7, 32, 0, 'LIGHT.  Summon a LIGHT 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Light Summonee'); end;
local spell = Card:createMinion(0,'Light Summonee', 'Light', 3, 2, 0, 'LIGHT', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Alliance Summoner', 'Alliance', 7, 32, 0, 'ALLIANCE.  Summon a ALLIANCE 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Alliance Summonee'); end;
local spell = Card:createMinion(0,'Alliance Summonee', 'Alliance', 3, 2, 0, 'ALLIANCE', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Spirit Summoner', 'Spirit', 7, 32, 0, 'SPIRIT.  Summon a SPIRIT 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Spirit Summonee'); end;
local spell = Card:createMinion(0,'Spirit Summonee', 'Spirit', 3, 2, 0, 'SPIRIT', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Time Summoner', 'Time', 7, 32, 0, 'TIME.  Summon a TIME 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Time Summonee'); end;
local spell = Card:createMinion(0,'Time Summonee', 'Time', 3, 2, 0, 'TIME', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Industry Summoner', 'Industry', 7, 32, 0, 'INDUSTRY.  Summon a INDUSTRY 32/32', 60, 50);
	spell.onCast = function() this().owner:summon('Industry Summonee'); end;
local spell = Card:createMinion(0,'Industry Summonee', 'Industry', 3, 2, 0, 'INDUSTRY', 32, 32);
	spell.token = true;
	
local spell = Card:createMinion(0,'Aquatic Commander', 'WATER', 6, 32, 0, 'WATER.  Your other WATER minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='WATER' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='WATER' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Pyrotechnic Commander', 'FIRE', 6, 32, 0, 'FIRE.  Your other FIRE minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='FIRE' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='FIRE' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Forest Commander', 'NATURE', 6, 32, 0, 'NATURE.  Your other NATURE minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='NATURE' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='NATURE' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Photon Commander', 'LIGHT', 6, 32, 0, 'LIGHT.  Your other LIGHT minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='LIGHT' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='LIGHT' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Eclipsed Commander', 'DARK', 6, 32, 0, 'DARK.  Your other DARK minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='DARK' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='DARK' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Beyond Commander', 'SPIRIT', 6, 32, 0, 'SPIRIT.  Your other SPIRIT minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='SPIRIT' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='SPIRIT' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Executive Commander', 'INDUSTRY', 6, 32, 0, 'INDUSTRY.  Your other INDUSTRY minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='INDUSTRY' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='INDUSTRY' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Era Commander', 'TIME', 6, 32, 0, 'TIME.  Your other TIME minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='TIME' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='TIME' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0,'Allied Commander', 'ALLIANCE', 6, 32, 0, 'ALLIANCE.  Your other ALLIANCE minions have +25/+25.', 62, 62);
	spell.onCast = function() local source = this();
		this():addGlobalAura('ATTACK',function() if minion() and this().card:getSubtype()=='ALLIANCE' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
		this():addGlobalAura('HEALTH',function() if minion() and this().card:getSubtype()=='ALLIANCE' and this().controller == source.controller and this() ~= source then return 25 else return 0 end end,false); 
	end;
local spell = Card:createMinion(0, 'Light Legionary', 'LIGHT', 8, 60, 0, 'LIGHT', 130, 40);
local spell = Card:createMinion(0, 'Fire Legionary', 'FIRE', 8, 60, 0, 'FIRE', 130, 40);
local spell = Card:createMinion(0, 'Water Legionary', 'WATER', 8, 60, 0, 'WATER', 130, 40);
local spell = Card:createMinion(0, 'Nature Legionary', 'NATURE', 8, 60, 0, 'NATURE', 130, 40);
local spell = Card:createMinion(0, 'Industry Legionary', 'INDUSTRY', 8, 60, 0, 'INDUSTRY', 130, 40);
local spell = Card:createMinion(0, 'Spirit Legionary', 'SPIRIT', 8, 60, 0, 'SPIRIT', 130, 40);
local spell = Card:createMinion(0, 'Dusk Legionary', 'DARK', 8, 60, 0, 'DARK', 130, 40);
local spell = Card:createMinion(0, 'Alliance Legionary', 'ALLIANCE', 8, 60, 0, 'ALLIANCE', 130, 40);
local spell = Card:createMinion(0, 'Time Legionary', 'TIME', 8, 60, 0, 'TIME', 130, 40);

	