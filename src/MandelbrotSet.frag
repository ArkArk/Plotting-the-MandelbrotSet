uniform vec2 resolution;
uniform float zoom; // 拡大縮小の倍率
uniform vec2 diff;  // 平行移動の差分のベクトル

vec2 mult(vec2 c1, vec2 c2) {
    return vec2(c1.x*c2.x - c1.y*c2.y, c1.x*c2.y + c1.y*c2.x);
}

float calc(vec2 c) {
    const int MAX = 256;
    vec2 z = vec2(0);
    int i;
    for(i=0; i<MAX; i++) {
        z = mult(z, z) + c;
        if (length(z) > 512.0) break;
    }
    if (i == MAX) {
        return 0.0;
    } else {
        return mod(float(i), 16.0) / 16.0;
    }
}

void main() {
    vec2 uv = (gl_FragCoord.xy*2.0 - resolution) / min(resolution.x, resolution.y);
    uv *= 1.5;

    uv *= zoom; // 拡大縮小
    uv -= diff; // 平行移動

    gl_FragColor = vec4(vec3(calc(uv)), 1.0);
}
