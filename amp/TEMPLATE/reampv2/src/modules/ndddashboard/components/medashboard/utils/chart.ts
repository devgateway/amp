class ChartUtils {
    public static generateTickValues = (min: number, max: number,  step: number) => {
        const tickValues = [];
        for (let i = min; i <= max; i += step) {
            tickValues.push(i);
        }
        return tickValues;
    }
}

export default ChartUtils;
