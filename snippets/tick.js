
// https://github.com/pixijs/pixi.js/wiki/v5-Custom-Application-GameLoop

var app = new PIXI.Application({ width: 800, height: 600, backgroundColor: 0x1099bb });


app.ticker.add(function(delta) {
    sprite.rotation += 0.1 * delta;
});
