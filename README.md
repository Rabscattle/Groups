# Groups

## Task Description

The plugin should function as a group/permission system similar to Pex or LuckyPerms. By default, the player should be in a group. Optionally, they can be assigned to another group permanently or for a certain period. All necessary information should be stored in a relational database.

## Preamble

The plugin is currently designed to run on version 1.19. Most commands are listed below. To get a complete list, use the command 'help' for a more detailed listing. Here are some additional useful commands:

- groups reload (Reload the configuration file and messages)

Most user commands and their output are dynamically adjustable. Sometimes, this dynamism has been omitted (for example, in admin commands), as configurable messages are usually not necessary. These include:
- For the command: group list (listing of all groups, their permissions, and weight)
- For the command: user info [user] (Shows information about a user (as admin))

## Frameworks & Help

https://github.com/aikar/commands

<p>
The ACF (Annotation Command Framework) is used for the commands.
which I regret and would replace with my own framework if given the opportunity.
</p>

https://github.com/JordanOsterberg/JScoreboards

<p>
For the Scoreboard branch (which includes an unfinished version of
scoreboards and the tab list), JScoreboards
by Jordan Osterberg was used.
</p>

https://github.com/LuckPerms/LuckPerms

<p>
Due to years of work with LuckPerms, I have adopted many
ideas and concepts but not copied any code. For example, a similar structure
for the managers (User & Group), which first create their objects and then load them from the database,
which I found quite nice. Also, users and groups
do not have direct variables (from a UML perspective). Instead, everything is controlled through
permissions, such as the prefix and weighting.
</p>

## Minimum Requirements

### Groups can be created and managed in the game

Groups are managed with the main command 'group'.
Subcommands include:

- group create
- group delete
- group prefix set
- group prefix add
- group prefix clear
- group weight set
- group weight add
- group weight clear
- group perm set
- group perm remove

### The group must have at least the following properties

#### Name

The name of a group is fixed and cannot be changed. It is also
the identifier of a group

#### Prefix

Prefixes are regulated through permissions with a specific syntax.
This is: `prefix.<weight>.<prefix>`. The prefix with the highest weight is used.

### Players should be able to be assigned to a group (permanently and with a time specification)

Players/Users are managed with the main command 'user'.
Subcommands include:

- user group set
- user group remove
- user group add
- user perm set
- user perm remove
- user info

A time specification can be provided with all commands. This
is in English format. Example: 5d 5s = 5 days 5 seconds

### The group's prefix should be displayed in the chat and when entering the server<br>

![](img/prefix.png)

### When a player is assigned a new group, it should change immediately (player should not be kicked)

### All messages should be customizable in a configuration file

See the subpoint: Messages

### Through a command, the player learns his current group and possibly how long he still has it

The player has the ability (if they have permission) to receive the following output (configurable) with the command `/rank`: <br>
![](img/rank-info.png)

### One or more signs should be able to be added that display information about an individual player like name & rank

Players can place a sign that must contain the keyword `group-sign`.
After that, the sign formats itself as follows (configurable including language): <br>
![](img/sign.png)

### All necessary information is stored in a relational database (configurable texts are not)

A pre-existing database is required. Currently, the user only has MYSQL and MARIADB
as database options

## Bonus Tasks

### Permissions can be set for a group and should be assigned to the player accordingly. Query via #hasPermission should work

Not implemented. Currently, there is no relationship/inheritance between users and their groups for
permissions

### (*) Permission

Permissions are resolved as wildcards. * Permission would work.

### Support for multiple languages

See the subpoint: Messages

### Tab list and scoreboard

Both are implemented in the scoreboard branch. However, due to the instability of the used
framework,
this is not included in the task.

## Messages

<p>
The plugin uses a dynamically expandable language/message system
and supports any number of languages.
To do this, create a message file in the `Groups/languages` folder.

### Selecting a language

Currently, English (en) is always the default language.
For a player to change their language, the command is needed:
`language set <language-code>`. The language must be configured beforehand!

### Filename and extension

<p>
The name of this file must follow a certain format to be recognized by the
system. The format looks like this: <br>

Format: `messages-(language-code).yml` <br>
Example (English): `messages-en.yml` <br>
</p>

### Populating languages

To create your own language, look at the `LangKeys.java` https://github.com/dschreid/Groups/blob/master/src/main/java/com/github/dschreid/groups/lang/LangKeys.java more closely.
There are all messages that can be changed.
To customize, for example, the `LangKeys.GREET` (The message sent when entering the server),
do the following:

```yml
#messages-en.yml
greet:
  msg: "Welcome to the server!"
```

```yml
#messages-de.yml
greet:
  # Message will not be sent if the content is empty!
  msg: ""
  # Creating titles
  title:
    title: "Wilkommen"
    subtitle: "Auf dem Server!"
```

Currently, there are two types of messages that can be created.
Chat messages and titles. Both should be self-explanatory.

## Setup

In the release directory here on GitHub, you will find the latest version,
as well as a Docker container which you can also use.

### Plugin Version

https://github.com/dschreid/Groups/releases/download/1.0/groups-1.0-SNAPSHOT-all.jar

To use the plugin in your own environment, a pre-configured database is required initially. Pre-configured in the sense that
a database needs to be created (this does not mean setting up a connection).
A restart is required to establish the connection.
The plugin should be started once to load the configurations
and folders.

### Docker Version

### Pre-Installation

Install Docker & Docker Compose for your operating system
Follow all steps from the page https://docs.docker.com/get-docker/

### Setup

1. Download: https://github.com/dschreid/Groups/releases/download/1.0/plugin-test-docker.zip
2. Unzip the .zip file
3. Open a terminal in the unpacked folder
4. Start the container with: `docker-compose up -d`
5. (Optional) to log in to the console, use the following command: `docker attach plugin-test`

### Joining

The server is now running on `localhost:4321` in server version 1.19
All necessary databases have already been created and are running
on a MariaDB client.
