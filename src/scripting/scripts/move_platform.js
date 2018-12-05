function update() {
    var x = platform.getX();
    var y = platform.getY();
    var originX = platform.getOriginX();
    var originY = platform.getOriginY();
    var xRange = platform.getXrange();
    var yRange = platform.getYrange();
    var movesX = platform.getMovesX();
    var movesY = platform.getMovesY();
    var speedX = platform.getSpeedX();
    var speedY = platform.getSpeedY();

    if(x <= (originX-xRange) || x >= (originX+xRange)) speedX *= -1;
    if(y <= (originY-yRange) || y >= (originY+yRange)) speedY *= -1;
    if(movesX) x += speedX;
    if(movesY) y += speedY;

    platform.setSpeedX(speedX);
    platform.setSpeedY(speedY);
    platform.setX(x);
    platform.setY(y);
}


