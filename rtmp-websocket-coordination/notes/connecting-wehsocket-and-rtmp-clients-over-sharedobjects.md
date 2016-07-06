Hey there.. . So this last week i have been working on some websocket stuff on red5 i came across quite a few ‘gotchas’ and ‘oohs’ that i would really like share with all of you right here.

It has been a good few years since that you've been hearing the “flash is dead! all hail HTML5!” campaign. And as we migratory birds start flocking south you would have come across the new guy in town “Websockets”. Well honestly “websocket” isn't that old and certainly not at the time of writing this article. But to the world of Red5 and the exciting new Red5pro it definitely is newer than the stuff you have already been using in Red5. 

I won't start explaining what is websockets how it evolved..blah blah because that is not important now. What is important is what it means to us in the context of Red5 and how you can leverage it to build engaging real-time applications. Websocket support in Red5 is implemented as a plugin. This means you don't have to do anything special to get websockets support in there. 

NOTE:
Don't get ahead of yourself looking up websocket frameworks such as spring websockets or similar. This will just create more trouble. Websockets is already a part of red5 / red5 pro and it knows how to do handshake, how to decode , encode etc. Avoid any third party java-websocket frameworks and just start with vanilla.

There is a very clean and simple websocket integration example by Paul Gregoire posted on github : https://github.com/Red5/red5-websocket. Make sure to study it first if you are going to build something of your own.


Traditional system of using a flash client for a chat application involved connecting the flash client to the application a at a particular scope in Red5. Before we proceed to websockets let's try to understand what a scope is in Red5. It is important to understand what scopes are and how a traditional flash client uses them before we try to address the issues of using scopes with websockets.

---
##### A Simple understanding of scope

A scope is a logical separation within Red5, much like logical partitions on a physical hard drive. Scopes give you the advantage of better management of resources when building applications that involve lots of connections. Traditionally scopes were used as “rooms” in  chat application. Interestingly the application itself is a scope within a larger global scope in red5.

![GitHub Logo](/images/scopes.png)
Format: ![Alt Text](url)


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


### Something to know about websockets In Red5

As i mentioned before websocket is available as a Red5 plugin which means that the protocol level communications and websocket implementation standards are in place. But it does not mean all Red5 features are supported over websockets out of the box.

It still requires some degree of programing knowledge and understanding of websockets and Red5 scopes in general to be able achieve a seamless integration.

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

* Your clients will all be connecting to the top most level (application level scope).

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


No matter which option you choose the challenge lies iny connecting the websocket client to a scope for sending  / receiving messages to / from RTMP clients. The answer to this can be found in the virtual `route`” implementation `(Router.java or CommLinkRouter.java)`. Given below is the function which achieves that.

NOTE: this funtion is adapted from the original example of [red5 websocket chat](#https://github.com/Red5/red5-websocket-chat) posted by Paul Gregoire.
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
