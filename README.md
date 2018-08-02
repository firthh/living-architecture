# living-architecture

One of the biggest problems with architecture diagrams is they become out of date quickly. Often this is because no-one looks at them.

What if we could create beautiful architecture diagrams that were also information radiators that at a glance provided us with some high level information about how the system is performing.

This project aims to do that...

![screenshot](https://github.com/firthh/living-architecture/blob/master/screenshot.png "Screenshot")

What is out of scope:
 - Graphing metrics
 
## TODO

 - Pull dashboard configuration from the server instead of being hard coded
 - Have multiple dashboard configurations on the server (maybe static files, maybe in the database)
 - Dashboard able to update metric values from the server (e.g `GET /metric/MET123/current-value -> {"value": "100", "unit": "RPS"}`)
   - Start with a button you can click to update the values
 - Server able to query a datasource for metric values:
   - [ ] Prometheus
   - [ ] AWS Cloudwatch
   - [ ] influxDB
   - [ ] SQL database
   - [ ] Others?...
 - Embrace the [C4 architecture model](https://www.infoq.com/articles/C4-architecture-model)
   - Each component on the dashboard should be a link, usually to another dashboard so you can navigate down into components
 - Use Compojure API on the server so we get documentation
 - Allow for more than just box components
   - [ ] Queues (horizontal cylinder)
   - [ ] Databases (vertical cylinder)
   - [ ] Free text
 - Ability to edit a dashboard and save what it looks like
 - Change the colour of components
 - Have the components (or parts of the component) change colour based on metric values (rules for good or bad metric values)

## Development Mode

### Compile css:

Compile css file once.

```
lein less once
```

Automatically recompile css file on change.

```
lein less auto
```

### Run application:

```
lein clean
lein figwheel dev
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Build

```
lein clean
lein with-profile prod uberjar
```

That should compile the clojurescript code first, and then create the standalone jar.

When you run the jar you can set the port the ring server will use by setting the environment variable PORT.
If it's not set, it will run on port 3000 by default.

To deploy to heroku, first create your app:

```
heroku create
```

Then deploy the application:

```
git push heroku master
```

To compile clojurescript to javascript:

```
lein clean
lein cljsbuild once min
```
