# MonitorX
Elegant Montior.

# Screenshot

![https://github.com/qianlifeng/MonitorX/blob/master/doc/screenshot/index.png](https://github.com/qianlifeng/MonitorX/blob/master/doc/screenshot/index.png)

# Supported metric type
* number
  value = int number
* gauge
  value = int number
* line
  value =
* pie
  value = [{"name":"titl1","value":12},{"name":"titl2","value":15}]

# metric example
``` json
{
    "status":"up",
    "metrics":[
        {"title":"CPU","type":"line","value":23},
        {"title":"OPS","type":"gauge","value":20},
        {"title":"MEM","type":"number","value":40},
        {"title":"Pie","type":"pie","value":[
            {"value":22,"name":"title1"},
            {"value":5,"name":"ertyyu"}
        ]}
    ]
}
```
