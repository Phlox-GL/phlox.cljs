// https://jsfiddle.net/bigtimebuddy/Lu52dgmv/

const circle = new PIXI.Graphics()
  .beginTextureFill(gradient('#9ff', '#033'))
  .drawStar(50, 50, 5, 50, 20);

function gradient(from, to) {
  const c = document.createElement("canvas");
  const ctx = c.getContext("2d");
  const grd = ctx.createLinearGradient(0,0,100,100);
  grd.addColorStop(0, from);
  grd.addColorStop(1, to);
  ctx.fillStyle = grd;
  ctx.fillRect(0,0,100,100);
  return new PIXI.Texture.from(c);
}
