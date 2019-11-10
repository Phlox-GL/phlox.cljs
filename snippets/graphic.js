// https://stackoverflow.com/questions/34031777/animate-the-drawing-of-a-line-in-pixi-js

graphics.clear();
graphics.lineStyle(20, 0x33FF00);
graphics.moveTo(30,30);
graphics.lineTo(30 + (600 - 30)*p, 30 + (300 - 30)*p);

renderer.render(stage);
