// http://scottmcdonnell.github.io/pixi-examples/index.html?s=basics&f=click.js&title=Click


var stage = new PIXI.Container();

sprite.on('mousedown', onDown);
sprite.on('touchstart', onDown);

stage.addChild(sprite);

function onDown (eventData) {

    sprite.scale.x += 0.3;
    sprite.scale.y += 0.3;
}
// start animating
