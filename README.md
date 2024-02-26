This API is free and does not require a key. You should not need to change anything when you clone this down for it to run.

I've configured the release variant to just use debug signing in the app/build.gradle. 
Compose performance is massively improved when you can disable minification, disable the debuggable flag, and use proguard.

This is a silly API and the functionality is simple and I hope that the content will get a smile.
However, the technical design and implementation differs little from what I would use for a large commercial application.
...with a few caveats:
- There's no local storage. I just didn't think it was worth the implementation here. That means that each call to each detail
  page calls the relevant endpoints, and that the results are not cached.
- In a big app, we'd go multi-module. But I didn't think that was a good use of time on this.
- There are some hacks to get around the api limitations, but nothing too bad. I make some assumptions about the data.
- On the list, there is simply a pokeball for each entry. The api does not include any imagery in the list calls, and I didn't
  want to make 454 api calls just to initialize the app. However, I'm putting a 'feature not a bug' spin on this; I've modeled
  the list ui after the original pokedex in the show (if my memory isn't failing), which had a pokeball on each entry.
- I didn't do any animation.

Some fun notes!
- Long press the pokemon logo at the top to change your game version. I thought it was a fun way to integrate a CompositionLocal
  in a way that was actually an appropriate use. Changing color is overkill, that could be done with theme updates the same way
  that the app bar is. But, flavor text changes also take advantage, and so could any smaller more custom components.
- I can't believe the api gave me an actual reason to write a recursive function in an app so simple.
- Omanyte and Omastar have a fun callback to the pokemon zeitgesit circa 2014.
