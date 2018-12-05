function update() {

    var grounded = player.getGrounded();
    var speedYp = player.getSpeedY();

    if(grounded) {
        speedYp -=8;
    }

    player.setSpeedY(speedYp);
}