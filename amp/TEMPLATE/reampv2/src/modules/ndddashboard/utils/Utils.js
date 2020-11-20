/* eslint-disable  no-bitwise */
import { DIRECT_PROGRAM_COLOR, NDD_COLOR_MAP } from "./constants";

export function hashCode(str) { // java String#hashCode
	let hash = 0;
	for (let i = 0; i < str.length; i++) {
		hash = str.charCodeAt(i) + ((hash << 5) - hash);
	}
	return hash;
}

export function intToRGB(i) {
	const c = (i & 0x00FFFFFF)
		.toString(16)
		.toUpperCase();

	return '00000'.substring(0, 6 - c.length) + c;
}

export function addAlpha(color, opacity) {
	const _opacity = Math.round(Math.min(Math.max(opacity || 1, 0), 1) * 255);
	return color + _opacity.toString(16).toUpperCase();
}

export function getCustomColor(item, program) {
	let colorMap;
	let color;
	colorMap = NDD_COLOR_MAP.get(program);
	if (!colorMap) {
		colorMap = new Map();
		NDD_COLOR_MAP.set(program, colorMap);
	}
	color = colorMap.get(item.code);
	if (!color) {
		color = DIRECT_PROGRAM_COLOR.shift();
		colorMap.set(item.code, color);
	}
	return color;
}