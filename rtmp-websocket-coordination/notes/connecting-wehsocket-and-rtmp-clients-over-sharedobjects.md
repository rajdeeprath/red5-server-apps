Hey there.. . So this last week i have been working on some websocket stuff on Red5Pro and i came across quite a few ‘gotchas’ and ‘oohs’ that i would really like share with all of you right here.

It has been a good few years since that you've been hearing the “flash is dead! all hail HTML5!” campaign. And as we migratory birds start flocking south you would have come across the new guy in town “Websockets”. Well honestly “websocket” isn't that old and certainly not at the time of writing this article. But to the world of Red5 and the exciting new Red5Pro it definitely is newer than the stuff you have already been using till now. 

I won't start explaining what is websockets how it evolved..blah blah because that is not important now. What is important is what it means to us in the context of Red5Pro and how you can leverage it to build engaging real-time applications. Websocket support in Red5Pro / Red5 Open source is implemented as a plugin. This means you don't have to do anything special to get websockets support in there. 

NOTE:
Don't get ahead of yourself looking up websocket frameworks such as spring websockets or similar. This will just create more trouble. Websockets is already a part of Red5 / Red5Pro and it knows how to do handshake, how to decode , encode etc. Avoid any third party java-websocket frameworks and just start with vanilla.

There is a very clean and simple websocket integration example by Paul Gregoire posted on github : https://github.com/Red5/red5-websocket-chat. Make sure to study it first if you are going to build something of your own.


Traditional system of using a flash client for a chat application involved connecting the flash client to the application a at a particular scope in Red5. Before we proceed to websockets let's try to understand what a scope is in Red5. It is important to understand what scopes are and how a traditional flash client uses them before we try to address the issues of using scopes with websockets.

---
##### A Simple understanding of scope

A scope is a logical separation within Red5, much like logical partitions on a physical hard drive. Scopes give you the advantage of better management of resources when building applications that involve lots of connections. Traditionally scopes were used as “rooms” in  chat application. Interestingly the application itself is a scope within a larger global scope in red5.

![Scope structure](https://github.com/rajdeeprath/red5-server-apps/blob/master/rtmp-websocket-coordination/notes/images/scopes.png)


Conceptually the scope structure in Red5 would look similar to the diagram shown above. The Global Scope exists from the time the server starts, the application scope is created for each application found in the `RED5_HOME/webapps/` directory. The dynamic user scopes are created on demand, only when a connection requests it and are removed when they are not needed any more. In the context of a ‘chat’ chat application, it suffices to refer to scopes as rooms.

The standard way of connecting to a Red5 application via a RTMP client is to use the following RTMP url format:

```
rtmp://host:5080/{application}
```

To connect to a scope within the application the following format is applicable:

```
rtmp://host:5080/{application}/{scopename}
```

This will automatically create a subscope in your application by the name {scopename}. Now you can publish / subscribe a stream or acquire a shared object for communication. All this will happen inside the new scope. So anyone wishing to be a part of this will need to connect to this scope. To get more information on scopes refer to official Red5 javaDocs for 
[IScope](#http://red5.org/javadoc/red5-server-common/org/red5/server/api/scope/IScope.html) 
and related interface [IScopeStatistics](#http://red5.org/javadoc/red5-server-common/org/red5/server/api/statistics/IScopeStatistics.html).

---

### Something to know about websockets In Red5

As i mentioned before websocket is available as a Red5 plugin which means that the protocol level communications and websocket implementation standards are in place. But it does not mean all Red5 features are supported over websockets out of the box.

It still requires some degree of programing knowledge and understanding of websockets and Red5 scopes in general to be able achieve a seamless integration.

Throughout this article we will be referencing snippets from [red5 websocket chat](#https://github.com/Red5/red5-websocket-chat) sampel application posted by `Paul Gregoire`. If you dont have this application on your system i recommend you clone it to your desktop before proceeeding.

If you look at the red5 web.xml file of this application, you will see  how a reference of the red5 application is made available to the websocket listener via the virtual router (Router.java) using spring bean configuration.

```
<bean id="web.handler" class="org.red5.demos.chat.Application" />

    <bean id="router" class="org.red5.demos.chat.Router">
        <property name="app" ref="web.handler" />
    </bean>

    <!-- WebSocket scope with our listeners -->
    <bean id="webSocketScopeDefault" class="org.red5.net.websocket.WebSocketScope">
        <!-- Application scope -->
        <property name="scope" ref="web.scope" />
        <!-- The path to which this scope is attached. The special value of "default" 
            means that it will be added to all paths -->
        <property name="path" value="default" />
        <property name="listeners">
            <list>
                <bean id="chatListener" class="org.red5.demos.chat.WebSocketChatDataListener">
                    <property name="router" ref="router" />
                </bean>
            </list>
        </property>
    </bean>
```
You can use similar technique to connect Red5 application adapter and websocket data listener classes.


### The webSocket dilemma of scopes

By its standard implementation a websocket connection knows nothing about the internals of Red5 its capabilities and scopes. 

To connect a websocket to a Red5 application following url syntax is used:
```
ws:://{host}:8081/{application}
```

You can then pass the scope path of interest to the application via query string or url path. how you wish to do this depends on your application design.

---

#### OPTION 1: 

```
rtmp:://{host}:1935/{application}?scope=conference1
ws:://{host}:8081/{application}?scope=conference1
```

* When you use this method you will need to parse out the scope variable value from querystring.

* Your clients will all be connecting to the top most level (application level).

* You need to design a mechanism to generate a unique name for your shared objects using the scope requested (since all connections are on single level) . You then need to check if the shared object exists else create it with the evaluated name. 

* RTMP clients can push messages to shared objects via client side API.

* You need to design a special logic for websocket clients to resolve a SharedObject using scope name on server side and then push messages to it.

#### OPTION 2: 

```
rtmp:://{host}:1935/{application}/conference1
ws:://{host}:8081/{application}/conference1
```


When you use this method you will need to capture the `path` from the connection url for websocket handler. RTMP side will automatically create its sub scope(s).

```
WebSocketConnection.getPath()
```

Your RTMP clients will all be connecting to the scope specified in the RTMP url and websocket connections will connect as they normally do.

RTMP clients can use same shared object name since the scope automatically manages isolation of shared object by same name. ie: /conference => SO and /conference1/SO are automatically separated and uniquely identified using the scope path.

RTMP clients can push messages to shared objects via client side API.

You need to design a special logic for websocket clients to resolve a SharedObject using scope name on server side and then push messages to it.


Or you can also use a mix of **Option 1 and 2** :


#### OPTION 3: 

```
rtmp:://{host}:1935/{application}/conference1
ws:://{host}:8081/{application}?scope=conference1
```

#### OPTION 4: 

```
rtmp:://{host}:1935/{application}?scope=conference1
ws:://{host}:8081/{application}/conference1
```

![websocket-rtmp-clients-connection](https://github.com/rajdeeprath/red5-server-apps/blob/master/rtmp-websocket-coordination/notes/images/rtmp-websocket-clients.png "Websocket client vs RTMP client connection")


No matter which option you choose the challenge lies in connecting the websocket client to a scope for sending  / receiving messages to / from RTMP clients. The answer to this can be found in the virtual `router`” implementation `(Router.java)` of the sample  websocket app [red5 websocket chat](#https://github.com/Red5/red5-websocket-chat). Given below is the function which achieves that.

>>NOTE: This function is adapted from the original example of [red5 websocket chat](#https://github.com/Red5/red5-websocket-chat) posted by `Paul Gregoire`. I would recommend you to clone the project repo to your own system and open it up in your eclipse IDE. 


```
    /**
     * Get the Shared object for a given path.
     * 
     * @param path
     * @return the shared object for the path or null if its not available
     */
    private ISharedObject getSharedObject(String path, String soname) 
    {
        // get the application level scope
        IScope appScope = app.getScope();
        // resolve the path given to an existing scope
        IScope scope = ScopeUtils.resolveScope(appScope, path);
        if (scope == null) 
        {
            // attempt to create the missing scope for the given path
            if (!appScope.createChildScope(path)) 
            {
                log.warn("Scope creation failed for {}", path);
                return null;
            }
            scope = ScopeUtils.resolveScope(appScope, path);
        }
        // get the shared object
        ISharedObject so = app.getSharedObject(scope, soname);
        if (so == null) 
        {
            if (!app.createSharedObject(scope, soname, false))
            {
                log.warn("Chat SO creation failed");
                return null;
            }
            // get the newly created shared object
            so = app.getSharedObject(scope, "chat");
        }
        // ensure the so is acquired and our listener has been added
        if (!so.isAcquired()) 
        {
            // acquire the so to prevent it being removed unexpectedly
            so.acquire(); // TODO in a "real" world implementation, this would need to be paired with a call to release when the so is no longer needed
            // add a listener for detecting sync on the so
            so.addSharedObjectListener(new SharedObjectListener(this, scope, path));
        }
        return so;
    }

```


---

### Explaination:

The function accepts a `path` which is the location of the scope that the websocket client is interested in for messages. This can be parsed from a query string or the websocket path (as discussed before). the second parameter is the schared object name that is wishes to convery messages on. This name nseds to be same for rtmp and websocket clients. 

The function tries to first find the scope by at the given location `path`. If it fails to find one it wil attempt to create one. If atleast one RTMP client is connected to the scope it will persist automatically else it will be lost.

Once we have a scope we attempt to connect to a shared object in it by the name `soname`. As with the scope, we have to force create a new shared object if we cant find an existing one. Finally you `acquire` it and register a [`ISharedObjectListener`](#http://red5.org/javadoc/red5-server-common/org/red5/server/api/so/class-use/ISharedObjectListener.html) on it. this is to receieve notification when an event occurs on the SharedObject. A `SharedObjectListener` is used to monitor events occuring on the acquired [`SharedObject`](#http://red5.org/javadoc/red5-server-common/org/red5/server/so/SharedObject.html). The typical logic here is to update a attribute on the shared object such that it automatically triggers a sync event to all listeners (including flash clients).

>> NOTE: As a good programming habit make sure to `release` the acquired object when you know it wont be used anymore.


Now if we look at our ISharedObjectListener implementation for listening to this shared object, it would look like this:

```
private final class SharedObjectListener implements ISharedObjectListener 
{

    private final Router router;

    private final IScope scope;

    private final String path;

    SharedObjectListener(Router router, IScope scope, String path) {
        log.debug("path: {} scope: {}", path, scope);
        this.router = router;
        this.scope = scope;
        this.path = path;
    }

    @Override
	public void onSharedObjectClear(ISharedObjectBase so) {
        log.debug("onSharedObjectClear path: {}", path);
    }

    @Override
	public void onSharedObjectConnect(ISharedObjectBase so) {
        log.debug("onSharedObjectConnect path: {}", path);
    }

    @Override
	public void onSharedObjectDelete(ISharedObjectBase so, String key) {
        log.debug("onSharedObjectDelete path: {} key: {}", path, key);
    }

    @Override
	public void onSharedObjectDisconnect(ISharedObjectBase so) {
        log.debug("onSharedObjectDisconnect path: {}", path);
    }

    @Override
	public void onSharedObjectSend(ISharedObjectBase so, String method, List<?> attributes) {
        log.debug("onSharedObjectSend path: {} - method: {} {}", path, method, attributes);
    }

    @Override
	public void onSharedObjectUpdate(ISharedObjectBase so, IAttributeStore attributes) {
        log.debug("onSharedObjectUpdate path: {} - {}", path, attributes);
    }

    @Override
	public void onSharedObjectUpdate(ISharedObjectBase so, Map<String, Object> attributes) {
        log.debug("onSharedObjectUpdate path: {} - {}", path, attributes);
    }

    @Override
	public void onSharedObjectUpdate(ISharedObjectBase so, String key, Object value) {
        log.debug("onSharedObjectUpdate path: {} - {} = {}", path, key, value);
        // route to the websockets if we have an RTMP connection as the originator, otherwise websockets will get duplicate messages
        if (Red5.getConnectionLocal() != null) {
            router.route(scope, value.toString());
        }
    }
}
```

---

In the above class `SharedObjectListener` take a note of the method `onSharedObjectUpdate`. On shared object update event we check to make sure that only messages from RTMP clients are relayed to WebSocket Clients. Messages from WebSocket clients are not to prevent duplicate messages. If however you want to send messages from websocket to websocket as well you can design your own unicast / multicast where you check certain parameters such as IP address and relay messages only to specific websocket connections. This is a food for thought and would be left of as a implementation exercise for **truth seekers** :).