# MonitorX
Elegant Montior.

# Screenshot

![https://github.com/qianlifeng/MonitorX/blob/master/doc/screenshot/node.png](https://github.com/qianlifeng/MonitorX/blob/master/doc/screenshot/node.png)

# Download

Please download from Release page.

# How to run

You need JDK7 before run MonitorX. If you already have JDK installed. Just execute
```
./MonitorX.jar
```

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
