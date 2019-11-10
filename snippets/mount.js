// https://github.com/pixijs/pixi.js/wiki/Getting-Started

var app = new PIXI.Application({ width: 640, height: 360 });
document.body.appendChild(app.view);
var circle = new PIXI.Graphics();
app.stage.addChild(circle);
