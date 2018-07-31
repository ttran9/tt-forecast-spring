/*
 * generates the column graph with the hourly forecasts.
 */
function generateHourlyForecast(hourlyList) {
    var title = "Hourly Forecast";
    var yAxisLabel = "Temperature (In Fahrenheit)";
    var xAxisLabel = "Time And Date";
    var chartId = "hourlyForecastChart";

    var hourlyForecasts = [];
    for (var i = 0; i < hourlyList.length; i++) {
        var hourlyForecast = hourlyList[i];
        hourlyForecasts.push({
            label : hourlyForecast.label,
            y : hourlyForecast.y
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
            dataPoints: hourlyForecasts
        }]
    });

    chart.render();
}