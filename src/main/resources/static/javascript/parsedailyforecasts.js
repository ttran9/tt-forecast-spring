/*
 * generates both the low and high temperature bar graphs.
 */
function generateDailyForecasts(listWithHighTemps, listWithLowTemps) {
    var lowTempsTitle = "Daily Forecast (Low Temps)";
    var lowTempsYAxisLabel = "Low Temperature";
    var highTempsTitle = "Daily Forecast (High Temps)";
    var highTempsYAxisLabel = "High Temperature";
    var xAxisLabel = "Time And Date";
    var highTempsChartId = "dailyForecastChartHighTemps";
    var lowTempsChartId = "dailyForecastChartLowTemps";
    generateDailyForecast(listWithHighTemps, highTempsChartId, highTempsTitle, highTempsYAxisLabel,
        xAxisLabel);
    generateDailyForecast(listWithLowTemps, lowTempsChartId, lowTempsTitle, lowTempsYAxisLabel,
        xAxisLabel);
}




/*
 * generates the daily forecast graph with low or high temperatures depending on the value
  * stored inside the object with the "y" key.
 */
function generateDailyForecast(listWithTempsAndDateTime, chartId, title, yAxisLabel, xAxisLabel) {
    var dailyForecasts = [];
    for (var i = 0; i < listWithTempsAndDateTime.length; i++) {
        var dailyForecast = listWithTempsAndDateTime[i];
        dailyForecasts.push({
            label : dailyForecast.label,
            y : dailyForecast.y
        });
    }

    var chart = new CanvasJS.Chart(chartId, {
        theme: "light2", // "light1", "dark1", "dark2"
        animationEnabled: true,
        title: {
            text: title
        },
        axisY: {
            title: yAxisLabel
        },
        axisX: {
            title: xAxisLabel
        },
        data: [{
            type: "column",
            dataPoints: dailyForecasts
        }]
    });

    chart.render();
}