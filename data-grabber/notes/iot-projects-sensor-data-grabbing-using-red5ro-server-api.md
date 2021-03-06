
## INTRODUCTION

In this new era of smart web, IOT applications and are finding a special place in the heart of the IT industry world wide. Multiple platforms such as `Arduino`, `Raspberry Pi` etc have come up are blooming in the scope of IOT application development.

Hardware devices are becoming more and more context aware with the use of cheap and efficient sensors. The only true challenge of IOT projects is data gathering, management and utilization which makes the context worthwile and promising. Thats where Red5 pro comes in!! 

Unlike many other use case servers `Red5pro` is a versatile application server which depicts polymorphism in its role as a solution for projects. `Red5pro` can be seens as a simple `java http server`, a `websocket` server, a live streaming `media server` or even a complex all in one data processing hub, which in-turn automatically introduces `Red5pro` as versatile candidate for IOT projects. 



## SIMPLE DATA GATHERING FROM SENSORS USING RED5PRO SERVER API
---

<img src="images/arduino-setup.jpg" alt="arduino-dht11" class="inline"/>


In this section we shall see how to gather temperature and humidity data from a `DHT11` sensor through `arduino uno` and get it into `Red5pro` quickly and with minimal effort.

====

#### PREREQUISITES - 
 
* Red5Pro server with the latest api built-in app (Red5pro server api)
* A custom red5pro application of your own. (data-grabber)

---

[`Red5pro server api`](#https://www.red5pro.com/docs/server/serverapi/) is a recently launched  component within the composite `Red5pro` architecture, which allows you to interact with Red5pro and its applications at various levels using a simple REST api interface. You will need to enable and configure  the `api`component in order to make it accessible to your IOT clients.You can get more info on Red5pro server api [here](https://www.red5pro.com/docs/server/serverapi/)

>> NOTE: Make sure that the security settings on the api component are properly configured to allow your IOT client's IP address and also note down the `accessToken` configured in the `api` component for use in your IOT client calls.

For this case we will be concerning ourselves with a special `api` called `invoke`. As the name suggests you can use the `invoke` call to invoke any custom method in your main Red5 application class (ApplicationAdapter) without needing to code anything. All you need to do is create a `public` method in your Red5 application class (ApplicationAdapter) which you wish to call (The IOT client data receiever / handler). Given below is the Application class of our sample Red5pro application called `data-grabber`. You can grab the sample application code from GITHUB [here](https://github.com/rajdeeprath/iot-data-grabber-demo).


#### Red5 Application Class: (IOT server side)
====

```
package com.red5pro.demos.datagrabber;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.scope.IScope;
import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

public class Application extends MultiThreadedApplicationAdapter 
{
	private static Logger logger = Red5LoggerFactory.getLogger(Application.class, "data-grabber");
	
	/**
	 * Receives json string from IOT client 
	 * @param data
	 * @return
	 */
	public String receiveSensorData(String data)
	{
		JsonParser parser = new JsonParser();
		JsonObject o = parser.parse(data).getAsJsonObject();		
		logger.info(o.toString());
		
		return "200";
	}
}
```

**EXPLANATION:**

Our custom `IOT` client data handler method is called `receiveSensorData` which expects a json string as a parameter. The invoke  `api` call currently supports only Integer, Double, String and Array, and it must return a data value in order to be invoked successfully. The `IOT`client makes a `invoke' REST api call to Red5pro with the method name to trigger and the parameter array to pass. ([Read more about the invoke api](#https://www.red5pro.com/docs/server/serverapi/#invoke))

The api call is routed to your application class (ApplicationAdapter), where it scans for the requested method. If the method is found and parameters are compatible then the passed parameters are injected intto that method. From there on you can choose what you wish to do with the data - (store in database / relay over websocket / plot a chart).

**[SCREENSHOT]**
<img src="images/1.png" alt="data-grabber-red5-app" class="inline"/>

>>FOOD FOR THOUGHT : 
You could even use the `invoke` api to create a getter method which other IOT clients can use to read from your Red5 application!!


Next we can see the sample IOT arduino client which reads temperature and humidity data via  [DHT11](#https://www.adafruit.com/product/386) and sends it to a Red5pro server running at `192.168.1.100`. This sample arduino sketch can be downloaded from GITHUB repository [here](https://github.com/rajdeeprath/iot-data-grabber-demo).

---

#### PREREQUISITES - 
 
* Arduino Uno with `Ethernet shield`
* `DHT11` sensor

<img src="images/DHT11-Arduino-Sketch-Diagram.png" alt="data-grabber-red5-app" class="inline"/> 

>>NOTE 1: Ethernet shield is not shown in this diagram and is outside the scope of this article. It is assumed that you have basic knowledge of the relevent field.

>>NOTE 2: Make sure all server side stuff is done and your red5ro server is running with the data-grabber application  in it before running the IOT client.


#### Arduino code (IOT client side)
====


```
#include <dht.h>
#include <SPI.h>
#include <Ethernet.h>

#define dht_apin A0 // Analog Pin DHT11 sensor is connected to


// assign a MAC address for the ethernet controller.
// fill in your address here: 
// de:ad:be:ef:fe:ed
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
}; // DUMMY MAC ID

IPAddress ip(192, 168, 1, 115); // local ip
IPAddress myDns(4, 2, 2, 2); // DNS
EthernetClient client;

char server[] = "192.168.1.100"; // RED5PRO IP
int port = 5080; // RED5PRO PORT

dht DHT;

String humidity;
String temp;
String data;
String uri = "/api/v1/applications/data-grabber/invoke?accessToken=";
String accessToken = "xyz123";

void setup() 
{
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }   
  delay(2000); // give the ethernet module time to boot up:
  
  Ethernet.begin(mac, ip, myDns);
  Serial.print("My IP address: ");
  Serial.println(Ethernet.localIP());

  // prepare endpoint uri
  uri = uri + accessToken;
  Serial.print("Connection endpoint: ");
  Serial.print(uri);
  Serial.println();

  delay(2000);//Wait before accessing Sensor
  Serial.println("DHT11 Humidity & temperature Sensor to Red5pro\n\n");  
}



void loop() 
{
    DHT.read11(dht_apin);

    humidity = String(DHT.humidity);
    temp = String(DHT.temperature);    
    data = "";
    data = data + "{\"method\": \"receiveSensorData\",\"parameters\": [\"{\\\"humidity\\\": " + humidity + ",\\\"temperature\\\": " + temp + "}\"]}";

   Serial.println(data);
   doPost(data);

   Serial.println("sleeping");
   delay(10000);//Wait 10 seconds before accessing sensor again.
}



void doPost(String data)
{
   client.stop();

    if (client.connect(server, port)) 
    {
      Serial.println("connecting...");
      
      // send the HTTP POST request:
      client.println("POST " + uri + " HTTP/1.1");
      client.println("Host: 192.168.1.100");
      client.println("User-Agent: Arduino/1.0");
      client.println("Content-Type: application/json;");
      client.println("Connection: close");
      client.print("Content-Length: ");
      client.println(data.length());
      client.println();
      client.println(data);
    } 
    else 
    {
      Serial.println("connection failed");
    }
}
```

**EXPLANATION:**


While the sketch is self explainatory through its code, the important thing to note is the creating of json data string which will be posted along the `invoke` api call.Since we do not support json directly (yet!), you need to convert your data to a string format and pass it as a parameter. And on the java side dont forget to return a value (even if its not required).

```
data = "";
data = data + "{\"method\": \"receiveSensorData\",\"parameters\": [\"{\\\"humidity\\\": " + humidity + ",\\\"temperature\\\": " + temp + "}\"]}";
```

The string canbe received in your method and converted to a json object to extract attributes from it by name.


## TEASERS: - SOME IDEAS ON WHAT YOU CAN DO WITH RED5PRO

The applicaation scope of IOT projects with red5 is vast. Unlike nodejs, Red5pro is also a media server capable of offering complex  streaming services. Here are a few use cases where you can employ Red5pro in your IOT projects.

1. IP camera streaming : Red5pro is capable of restreaming a RTSP network camera. You can build application to live stream - restream an IP camera and alongside capture various types of data events (such as motion level) that your device may be broadcasting. You may deploy additional sensors to gather environment data such as temperature , air quality etc at the location where your IP camera is deployed.

2. Drone camera streaming : You could have a baseball / soccer match to cover using a remote drone which not only broadcasts live stream but also captures the temperature / humidity / light ambience  / sound and other necessary data to be added for live commentary.

3. Remote Image processing : Your could have a IP camera deployed at a remote location which is used for capturing still images along with live stream. You can transmit these live images over to Red5pro where you can then use `Google Cloud Vision API` or a custom mechanism to process the image and identify objects in it. Your live stream will always be available in parallel.

4. Designing Smart Home Projects : Red5pro can easily handle data based realtime home automation where you capture data from sensors and dispatch data to relays etc:. You can make it more challenging and accurate using IP cameras to capture and process images for object detection and control room switches based on that.


## CONCLUSION:

`Red5pro` is perfect candidate for creating rich IOT applications and the availability of the red5pro server api makes it equally easy to integrate your IOT clients to your server code without having to create any HTTP api interface on your own.

