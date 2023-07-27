# <img src="./dnt_logo.svg" height="80"/> DNT-Plugin


<p align="center"><b>Translation plugin for IntelliJ based IDEs/Android Studio.</b></p>
<p align="center"><img src="./screen/domain_transition.gif" alt="screenshots"></p>

<br/><br/><br/>

[![Getting Started][badge:get-started-en]][get-started-en]
[![시작하기][badge:get-started-ko]][get-started-ko]

------

- [Features](#features)
- [Compatibility](#compatibility)
- [Installation](#installation)
- [Using the Plugin](#using-the-plugin)
- [Actions](#actions)
- [FAQ](#faq)
- [Support](#support-and-donations)

## Features

- Domain Name Recommendation
- Variable conversion
  - camel case to snake case
  - snake case to camel case
- Search Domain Name
- Domain Name Registration

## Compatibility

- Android Studio
- IntelliJ IDEA


## Installation


- **Installing from the plugin repository within the IDE:**
    - <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search and find <b>"
      DNT"</b></kbd> > <kbd>Install Plugin</kbd>.

- **Installing manually:**
    - <kbd>Preferences(Settings)</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd> >
      Select the plugin package and install (no need to unzip)

Restart the **IDE** after installation.


## Using The Plugin

1. **Sign up for a translation service (optional)**

   Most translation services require user registration to access their services
   (DNT as OpenAI, etc.).
   Therefore, you may need to create an account, obtain an **Authentication Key**,
   and then bind the **Authentication Key** within the plugin：<kbd>Preferences(Settings)</kbd> > <kbd>
   Tools</kbd> > <kbd>
   DNT</kbd> > <kbd>Configure...</kbd>

2. **Begin translating**

   <kbd>Select a text or hover the mouse over the text</kbd> > <kbd>Control</kbd> + <kbd>r</kbd> Or use shortcuts for translation, as detailed in **[Actions](#actions)**.

3. **camel <=> snake replace**

   <kbd>Select a text or hover the mouse over the text</kbd> > <kbd>Control</kbd> + <kbd>k</kbd> Or use shortcuts for translation, as detailed in **[Actions](#actions)**.

4. **.dntconfig <b>file</b>**

    - {ROOT}\.dntconfig Create a file
   ``` 
      projectName=demo1
   ```

## Actions

- **Domain Name Registration...**

    - Windows - <kbd>Ctrl</kbd> + <kbd>0</kbd>
    - Mac OS - <kbd>Control</kbd> + <kbd>0</kbd>


## API Server or Domain Manager
- [API Server](https://github.com/mucoo2762/DNT_api) [https://github.com/mucoo2762/DNT_api]
- [Manager](https://github.com/Mika0203/DNT_web) [https://github.com/Mika0203/DNT_web]


    
## FAQ


## Support

You can contribute and support this project by doing any of the following:

* Star the project on GitHub
* Give feedback
* Commit PR
* Contribute your ideas/suggestions
* Share the plugin with your friends/colleagues
* If you love this plugin, please consider donating. It will inspire me to continue development on the project:


[plugin-logo]: ./dnt_logo.svg