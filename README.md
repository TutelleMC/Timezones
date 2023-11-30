# Timezones

The Timezones plugin for Bukkit/Paper is an addition to your Minecraft server that brings a unique twist to the game's time system. This plugin introduces the concept of timezones, allowing players to experience different times of the day based on their in-game location. This adds an extra layer of realism to the game, making it feel more like a world where time can vary depending on your geographical location.

## Commands

Currently there is 1 command, `/timezones` or `/tz`
* You can run `/timezones disable` to disable timezones 
* You can run `/timezones enable` to enable timezones again
* You can run `/timezones version` to display what version you are currently using

Note: The plugin enables itself on server launch, no need to run timezone enable

## Configuration

To configure the plugin, in your `config` folder create the `Timezones` folder and then create a `config.yml` folder.

These are the allowed values:

```yaml
plugin:
  amount_of_blocks_for_gradual_longitude_time_offset: 10 # how many blocks do players have to walk until the timezone changes
  amount_of_ticks_per_player_update: 20 # how many ticks until every player has their timezone updated
worlds: # allows multiple worlds
  - name: "world" # the exact name of the world we want to configure timezones on
    width: 1000 # the width of the world from west to east (a value of 1000 means the world goes from -500 to 499)
    height: 500 # the height of the world from top to bottom
    x_offset: 0 # how much to offset the center of the world, positive values go east and negative values go west
    # there is no z_offset right now, since the z value won't matter
  - name: "world2"
    width: 2500
    height: 2500
    x_offset: 500
```

## Plugin Dependencies

None :)

But I highly recommend [Chunky](https://github.com/pop4959/Chunky) 
and [Chunky Borders](https://github.com/pop4959/ChunkyBorder), both by
pop4959, so you can set the wrap to be like earth, thus making this
mod make sense. 

## Build

Run `./gradlew clean shadowjar`

## How it works

1. Upon server boot, the plugin creates a 2D set of points based on the world configuration. This set of points represents different timezones within the game world.
2. The plugin registers a couple of events related to mobs to ensure the game mechanics are functioning correctly.
 * It prevents mobs from combusting during the daytime.
 * It ensures mobs only spawn during the nighttime.
 * It makes sure daylight detectors function properly.
 * It updates the player's client time of day when they die, teleport, or undergo other events that would cause them to change faster than the default update tick configuration.
3. Player Time Management: The plugin starts a task that runs every X amount of ticks. This task iterates over every online player, queries the 2D set of points to find the nearest timezone, and then updates the player's client time accordingly. This ensures that the game time aligns with the player's location within the game world.

## Unimplemented features that I would like to implement

* Latitude changing length of day
  * Blocked by lack of seasons/calendar system
* Improve DaylightDetector behaviour by 
* Better commands
  * Defining which world to enable and disable
  * Create command

