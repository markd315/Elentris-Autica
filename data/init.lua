function this()
	return game.self;
end

function other()
	return game.other;
end

function aura()
	return game.aura;
end

function isType(o,t)
	t = 'tcg.'..t;
	return string.sub(tostring(o), 1, string.len(t)) == t
end

function minion()
	return isType(this(),'Minion');
end

function hero()
	return isType(this(),'Player');
end

function enemyHero()
	return hero() and this() ~= game:currentPlayer();
end

function enemyMinion()
	return minion() and this().controller ~= game:currentPlayer();
end

function enemy()
	return enemyHero() or enemyMinion();
end

function friendlyHero()
	return hero() and this() == game:currentPlayer();
end

function friendlyMinion()
	return minion() and this().controller == game:currentPlayer();
end

function friendly()
	return friendlyHero() or friendlyMinion();
end

function character()
	return minion() or hero();
end

function damaged()
	return this():damaged();
end